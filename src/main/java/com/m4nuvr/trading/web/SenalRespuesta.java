package com.m4nuvr.trading.web;

import com.m4nuvr.trading.domain.model.SenalInversion;

import java.time.LocalDateTime;

public record SenalRespuesta(String codigo, double precio, String mensaje, LocalDateTime fecha, boolean notificada) {

    public static SenalRespuesta desde(SenalInversion senal) {
        return new SenalRespuesta(
                senal.getCodigo().valor(),
                senal.getPrecio().valor(),
                senal.getMensaje(),
                senal.getFecha(),
                senal.isNotificada());
    }
}
