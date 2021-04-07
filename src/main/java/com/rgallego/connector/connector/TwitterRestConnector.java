package com.rgallego.connector.connector;

import com.rgallego.connector.connector.bean.RulesRequest;
import com.rgallego.connector.connector.bean.RulesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * Twitter API v2 Rest Connector
 */
@Service
public class TwitterRestConnector {

    private WebClient webClient;

    /**
     * Twitter API v2 Bearer Token
     */
    @Value("${twitter.token}")
    private String token;

    public TwitterRestConnector() {
    }

    @PostConstruct
    public void init() { //todo se puede crear un webclient custom para logear request y body. (Probar con subscribe como el flux)
        // Base URL and Authorization
        webClient = WebClient.builder()
                .baseUrl("https://api.twitter.com")
                .defaultHeader("Authorization", token)
                .build();
    }

    /**
     * Get Tweet Streaming with the specified fields
     *
     * @param params Fields that the API will return
     * @return {@link Flux} Flux Object to get the tweets
     */
    public Flux<String> getSearchStreaming(MultiValueMap<String, String> params) {
        return this.webClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path("/2/tweets/search/stream")
                        .queryParams(params)
                        .build())
                .retrieve()
                .bodyToFlux(String.class);
    }

    /**
     * Get the created rules for the streaming
     *
     * @return Rules in JSON format
     */
    public Mono<RulesResponse> getCreatedRules() {
        return this.webClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path("/2/tweets/search/stream/rules")
                        .build())
                .retrieve()
                .bodyToMono(RulesResponse.class);
    }

    /**
     * Create or delete rule/s for the streaming.
     * Allows 25 rules, each one up to 512 characters long.
     *
     */
    public Mono<RulesResponse> createStreamingRules(RulesRequest rules) {

        return this.webClient.method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder
                        .path("/2/tweets/search/stream/rules")
                        .build())
                .body(BodyInserters.fromValue(rules))
                .retrieve()
                .bodyToMono(RulesResponse.class);
    }

    /**
     * Delete rules from stream
     * @param rules Rule ID list
     * @return
     */
    public Mono<RulesResponse> deleteStreamRules(RulesRequest rules) {
        return this.webClient.method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder
                        .path("/2/tweets/search/stream/rules")
                        .build())
                .body(BodyInserters.fromValue(rules))
                .retrieve()
                .bodyToMono(RulesResponse.class);
    }
}
