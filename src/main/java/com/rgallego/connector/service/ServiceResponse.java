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

    public void setResponse(ResponseCodeEnum responseCodeEnum) {
        code = responseCodeEnum.getCode();
        text = responseCodeEnum.getText();
    }
}
