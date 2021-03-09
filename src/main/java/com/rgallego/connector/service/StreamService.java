package com.rgallego.connector.service;

import com.rgallego.connector.connector.TwitterRestConnector;
import com.rgallego.connector.connector.bean.*;
import com.rgallego.connector.kafka.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StreamService {

    private Disposable openStream;

    @Autowired
    private TwitterRestConnector twitterRestConnector;

    @Autowired
    private Producer kafkaProducer;

    public ServiceResponse startTwitterStreaming() {
        ServiceResponse serviceResponse = new ServiceResponse();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("expansions", "author_id,geo.place_id");
        params.add("tweet.fields", "created_at");//entities,context_annotations

        Flux<String> tweetsFlux = twitterRestConnector.getSearchStreaming(params);
        if (isStreamingStarted()) {
            serviceResponse.setResponse(ResponseCodeEnum.ERR_ALREADY_OPEN_STREAM);
        } else {
            log.debug("Starting streaming...");
            openStream = tweetsFlux.subscribe(kafkaProducer::send);
            serviceResponse.setResponse(ResponseCodeEnum.SUC_OPEN_STREAM);
        }

        return serviceResponse;
    }

    private void processTweet(String tweet) {
        log.debug(tweet);
    }

    public ServiceResponse stopTwitterStreaming() {
        ServiceResponse serviceResponse = new ServiceResponse();
        if (isStreamingStarted()) {
            openStream.dispose();
            openStream = null;
            serviceResponse.setResponse(ResponseCodeEnum.SUC_STREAM_STOPPED);
        } else {
            serviceResponse.setResponse(ResponseCodeEnum.ERR_STREAM_NOT_EXIST);
        }
        return serviceResponse;
    }

    public boolean isStreamingStarted() {
        if (openStream != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Operadores sucesivos separados por espacios son AND. Ej: "snow day" (snow AND day)
     * Si se escribe "OR" corresponde a un OR. Ej: "snow OR day".
     * Se puede utilizar parentesis para agrupar.
     * Para usar la negación se utiliza "-" delante del keyword. Ej: -is:retweet (solo tweets originales, excluye retweets).
     *
     * 512 characters long
     * @return
     */
    public Mono<RulesResponse> createStreamingRules(RulesRequest rulesRequest) {
        return twitterRestConnector.createStreamingRules(rulesRequest, false);
    }

    /**
     * Validate Streaming rules without submit them.
     * @param rulesRequest
     * @return
     */
    public Mono<RulesResponse> validateStreamingRules(RulesRequest rulesRequest) {
        return twitterRestConnector.createStreamingRules(rulesRequest, true);
    }

    /**
     * Delete streaming rules
     * @param rulesRequest
     * @return
     */
    public Mono<RulesResponse> deleteStreamingRules(RulesRequest rulesRequest) {
        return twitterRestConnector.deleteStreamRules(rulesRequest);
    }

    /**
     * Get created streaming rules
     * @return
     */
    public Mono<RulesResponse> getStreamingRules() {
        return twitterRestConnector.getCreatedRules();
    }

}
