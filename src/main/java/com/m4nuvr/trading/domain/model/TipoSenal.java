package com.m4nuvr.trading.domain.model;

public enum TipoSenal {
    COMPRA("BUY"),
    VENTA("SELL");

    private final String codigo;

    TipoSenal(String codigo) {
        this.codigo = codigo;
    }

    public String codigo() {
        return codigo;
    }
}