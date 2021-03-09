package com.rgallego.connector.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseCodeEnum {
    ERR_ALREADY_OPEN_STREAM(-1, "Ya existe un stream abierto, si desea abrir uno nuevo debe cerrar el anterior."),
    SUC_OPEN_STREAM(1, "Stream iniciado correctamente."),
    ERR_STREAM_NOT_EXIST(-2, "No existe ningún stream abierto actualmente."),
    SUC_STREAM_STOPPED(2, "El stream se ha detenido correctamente.");


    @Getter
    private int code;

    @Getter
    private String text;
}
