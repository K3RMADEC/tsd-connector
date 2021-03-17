package com.rgallego.connector.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseCodeEnum {

    ERR_ALREADY_OPEN_STREAM(-1, "The stream is already started, if you want to open a new one you should stop the previous one."),
    SUC_OPEN_STREAM(1, "Stream started successfully"),
    ERR_STREAM_NOT_EXIST(-2, "There is currently no open stream"),
    SUC_STREAM_STOPPED(2, "Stream stopped successfully"),
    STREAM_STATUS_STARTED(3, "Stream is open"),
    STREAM_STATUS_STOPPED(4, "Stream is closed");


    @Getter
    private int code;

    @Getter
    private String text;
}
