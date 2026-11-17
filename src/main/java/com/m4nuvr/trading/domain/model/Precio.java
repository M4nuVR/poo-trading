package com.m4nuvr.trading.domain.model;

public record Precio(double valor) {

    public Precio {
        if (valor <= 0 || Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new IllegalArgumentException("El precio debe ser positivo: " + valor);
        }
    }

    public boolean esMayorQue(double otro) {
        return valor > otro;
    }
}
