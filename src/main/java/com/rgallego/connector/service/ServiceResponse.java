package com.rgallego.connector.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ServiceResponse implements Serializable {

    private int code;
    private String text;
    private String detail;

    public ServiceResponse(ResponseCodeEnum responseCodeEnum) {
        code = responseCodeEnum.getCode();
        text = responseCodeEnum.getText();
    }

    public ServiceResponse(ResponseCodeEnum responseCodeEnum, String detail) {
        code = responseCodeEnum.getCode();
        text = responseCodeEnum.getText();
        this.detail = detail;
    }
}
