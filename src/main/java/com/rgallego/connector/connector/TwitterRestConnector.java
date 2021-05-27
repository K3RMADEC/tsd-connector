package com.rgallego.connector.connector;

import com.rgallego.connector.connector.bean.RulesRequest;
import com.rgallego.connector.connector.bean.RulesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
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
@Slf4j
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
    public void init() {
        // Base URL and Authorization
        webClient = WebClient.builder()
                .baseUrl("https://api.twitter.com")
                .defaultHeader("Authorization", token)
                .build();
    }

    /**
     * Get Tweet Streaming with the specified fields
     *
     * @return {@link Flux} Flux Object to get the tweets
     */
    public Flux<String> getSearchStreaming() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/api-reference/get-tweets-search-stream
        params.add("expansions", "author_id,geo.place_id");
        params.add("tweet.fields", "created_at,geo");
        params.add("place.fields", "full_name,country,geo,name,place_type");
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
