package com.m4nuvr.trading.web;

import com.m4nuvr.trading.domain.model.SenalInversion;

import java.time.LocalDateTime;

public record SenalRespuesta(String codigo, String tipo, double precio, String motivo, String mensaje,
                             LocalDateTime fecha, boolean notificada) {

    public static SenalRespuesta desde(SenalInversion senal) {
        return new SenalRespuesta(
                senal.getCodigo().valor(),
                senal.getTipo().name(),
                senal.getPrecio().valor(),
                senal.getMotivo(),
                senal.getMensaje(),
                senal.getFecha(),
                senal.isNotificada());
    }
}