package com.rgallego.connector.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseCodeEnum {

    ERR_ALREADY_OPEN_STREAM(-1, "The stream is already started."),
    SUC_OPEN_STREAM(1, "Stream started successfully"),
    ERR_STREAM_NOT_EXIST(-2, "There is currently no open stream"),
    SUC_STREAM_STOPPED(2, "Stream stopped successfully"),
    CREATE_RULE_SUCCESS(4, "Rule successfully created"),
    CREATE_RULE_ERROR(-4, "Error creating new rule.");


    @Getter
    private int code;

    @Getter
    private String text;
}
