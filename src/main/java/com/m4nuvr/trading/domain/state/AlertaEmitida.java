package com.m4nuvr.trading.domain.state;

public class AlertaEmitida implements EstadoSeguimiento {

    @Override
    public String nombre() {
        return "ALERTA_EMITIDA";
    }

    @Override
    public boolean permiteAlerta() {
        return false;
    }

    @Override
    public EstadoSeguimiento siguiente(boolean condicionCumplida) {
        return condicionCumplida ? this : new Vigilando();
    }
}
