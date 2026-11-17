package com.m4nuvr.trading.domain.model;

public record CodigoActivo(String valor) {

    public CodigoActivo {
        if (valor == null || !valor.trim().matches("[A-Za-z]{2,10}")) {
            throw new IllegalArgumentException("Codigo de activo invalido: " + valor);
        }
        valor = valor.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return valor;
    }
}
