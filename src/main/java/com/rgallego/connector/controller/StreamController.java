package com.rgallego.connector.controller;

import com.rgallego.connector.connector.bean.RulesRequest;
import com.rgallego.connector.connector.bean.RulesResponse;
import com.rgallego.connector.service.ServiceResponse;
import com.rgallego.connector.service.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Stream Controller
 * Allows start and stop the streaming
 */

@RestController
@RequestMapping("/stream")
@Slf4j
public class StreamController {

    @Autowired
    private StreamService streamService;

    @GetMapping("/start")
    public ResponseEntity<ServiceResponse> startStream() {
        log.info("Start Stream Request");
        ServiceResponse response = streamService.startTwitterStreaming();
        log.info("Start Stream Response: {}", response);
        if (response.getCode() < 0) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/stop")
    public ResponseEntity<ServiceResponse> stopStream() {
        log.info("Stop Stream Request");
        ServiceResponse response = streamService.stopTwitterStreaming();
        log.info("Stop Stream Response: {}", response);
        if (response.getCode() < 0) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/getRules")
    public Mono<RulesResponse> getRules() {
        log.info("Get Rules Request");
        Mono<RulesResponse> response = streamService.getStreamingRules();
        log.info("Get Rules Response: {}", response);
        return response;
//        if(response.getCode() < 0) {
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } else {
//        return new ResponseEntity<>(response, HttpStatus.OK);
//        }
    }

    @PostMapping("/createRules")
    public Mono<RulesResponse> createRules(@RequestBody RulesRequest rulesRequest) {
        log.info("Create Rules Request");
        Mono<RulesResponse> response = streamService.createStreamingRules(rulesRequest);
        log.info("Create Rules Response: {}", response);
        return response;
    }

    @PostMapping("/validateRules")
    public Mono<RulesResponse> validateRules(@RequestBody RulesRequest rulesRequest) {
        log.info("Validate Rules Request");
        Mono<RulesResponse> response = streamService.validateStreamingRules(rulesRequest);
        log.info("Validate Rules Response: {}", response);
        return response;
    }

    @DeleteMapping("/deleteRules")
    public Mono<RulesResponse> deleteRules(@RequestBody RulesRequest rulesRequest) {
        log.info("Create Rules Request");
        Mono<RulesResponse> response = streamService.deleteStreamingRules(rulesRequest);
        log.info("Create Rules Response: {}", response);
        return response;
    }
}
