package com.m4nuvr.trading.domain.state;

public class Vigilando implements EstadoSeguimiento {

    @Override
    public String nombre() {
        return "VIGILANDO";
    }

    @Override
    public boolean permiteAlerta() {
        return true;
    }

    @Override
    public EstadoSeguimiento siguiente(boolean condicionCumplida) {
        return condicionCumplida ? new AlertaEmitida() : this;
    }
}
