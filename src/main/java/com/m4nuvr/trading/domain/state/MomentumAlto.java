package com.m4nuvr.trading.domain.state;

public class MomentumAlto implements EstadoSeguimiento {

    @Override
    public String nombre() {
        return "MOMENTUM_ALTO";
    }

    @Override
    public boolean permiteAlerta() {
        return true;
    }

    @Override
    public EstadoSeguimiento conPrecioNormal() {
        return new Normal();
    }

    @Override
    public EstadoSeguimiento conMomentumAlto() {
        return this;
    }

    @Override
    public EstadoSeguimiento conUmbralSuperado() {
        return new AlertaDisparada();
    }
}