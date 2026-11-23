package com.m4nuvr.trading.domain.model;


public record SenalPayload(String asset, String signal, String reason) {

    public SenalPayload {
        if (asset == null || asset.isBlank()) {
            throw new IllegalArgumentException("El payload necesita el activo");
        }
        if (signal == null || signal.isBlank()) {
            throw new IllegalArgumentException("El payload necesita el tipo de senal");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("El payload necesita el motivo");
        }
    }
}