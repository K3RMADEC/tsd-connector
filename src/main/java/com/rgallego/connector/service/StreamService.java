package com.rgallego.connector.service;

import com.rgallego.connector.connector.TwitterRestConnector;
import com.rgallego.connector.connector.bean.*;
import com.rgallego.connector.kafka.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@EnableScheduling
public class StreamService {

    private final int STREAM_OPENED = 0;
    private final int STREAM_CLOSED = 1;
    private final int STREAM_INTERRUPTED = 2;

    private Disposable openStream;

    @Autowired
    private TwitterRestConnector twitterRestConnector;

    @Autowired
    private Producer kafkaProducer;

    public ServiceResponse startTwitterStreaming() {
        if (STREAM_OPENED == getStreamStatus()) {
            return new ServiceResponse(ResponseCodeEnum.ERR_ALREADY_OPEN_STREAM);
        } else {
            Flux<String> tweetsFlux = twitterRestConnector.getSearchStreaming();
            log.debug("Starting streaming...");
            openStream = tweetsFlux.subscribe(kafkaProducer::send, ex -> {
                log.error("Error body: {}", ((WebClientResponseException.BadRequest) ex).getResponseBodyAsString());
            });
            return new ServiceResponse(ResponseCodeEnum.SUC_OPEN_STREAM);
        }
    }

    public ServiceResponse stopTwitterStreaming() {
        if (STREAM_CLOSED != getStreamStatus()) {
            openStream.dispose();
            openStream = null;
            return new ServiceResponse(ResponseCodeEnum.SUC_STREAM_STOPPED);
        } else {
            return new ServiceResponse(ResponseCodeEnum.ERR_STREAM_NOT_EXIST);
        }
    }

    public int getStreamStatus() {
        if (openStream == null) {
            return STREAM_CLOSED;
        } else if (openStream.isDisposed()) {
            return STREAM_INTERRUPTED;
        } else {
            return STREAM_OPENED;
        }
    }

    public Mono<ServiceResponse> createStreamingRule(Rule rule) {
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        RulesRequest rulesRequest = new RulesRequest();
        rulesRequest.setAdd(rules);
        Mono<RulesResponse> apiResponse = twitterRestConnector.createStreamingRules(rulesRequest);
        return apiResponse.map(response -> {
            log.debug("Create Rule Twitter Response: {}", response);
            if (response.getMeta().getSummary().getNot_created() > 0 && !response.getErrors().isEmpty()) {
                String details;
                if (response.getErrors().get(0).getDetails().isEmpty()) {
                    details = response.getErrors().get(0).getTitle();
                } else {
                    details = response.getErrors().get(0).getDetails().toString();
                }
                return new ServiceResponse(ResponseCodeEnum.CREATE_RULE_ERROR, details);
            } else {
                return new ServiceResponse(ResponseCodeEnum.CREATE_RULE_SUCCESS);
            }
        });
    }

    /**
     * Delete streaming rules
     *
     * @param ruleId
     * @return
     */
    public Mono<RulesResponse> deleteStreamingRule(String ruleId) {
        List<String> ruleIdList = new ArrayList<>();
        ruleIdList.add(ruleId);
        RulesRequest rulesRequest = new RulesRequest();
        rulesRequest.setDelete(new DeleteRule(ruleIdList));
        return twitterRestConnector.deleteStreamRules(rulesRequest);
    }

    /**
     * Get created streaming rules
     *
     * @return
     */
    public Mono<RulesResponse> getStreamingRules() {
        return twitterRestConnector.getCreatedRules();
    }

    /**
     * Scheduled Task that check every 5 minutes if the streaming was interrupted by a server error.
     * If the streaming was interrupted this task will start a new one.
     */
    @Scheduled(fixedDelay = 300000)
    private void streamingRetryScheduledTask() {
        if(STREAM_INTERRUPTED == getStreamStatus()) {
            log.debug("Executing Streaming Retry...");
            startTwitterStreaming();
        }
    }
}
