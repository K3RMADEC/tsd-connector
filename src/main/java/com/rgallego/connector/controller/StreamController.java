package com.rgallego.connector.controller;

import com.rgallego.connector.connector.bean.Rule;
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
@CrossOrigin(origins = "*")
public class StreamController {

    @Autowired
    private StreamService streamService;

    @GetMapping("/status")
    public ResponseEntity<Integer> streamStatus() {
        log.info("Stream Status Request");
        int status = streamService.getStreamStatus();
        log.info("Stream Status Response: {}", status);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

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
    }

    @PostMapping("/createRule")
    public Mono<ResponseEntity> createRule(@RequestBody Rule rule) {
        log.info("Create Rule Request");
        Mono<ServiceResponse> response = streamService.createStreamingRules(rule);
        return response.map(r -> {
            log.info("Create Rule Response: {}", r);
            if (r.getCode() < 0) {
                return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(r, HttpStatus.OK);
            }
        });
    }

    @DeleteMapping("/deleteRule")
    public Mono<RulesResponse> deleteRule(@RequestParam String ruleId) {
        log.info("Delete Rule Request");
        Mono<RulesResponse> response = streamService.deleteStreamingRules(ruleId);
        log.info("Delete Rule Response: {}", response);
        return response;
    }
}
