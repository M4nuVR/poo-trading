package com.m4nuvr.trading.domain.state;

public class Normal implements EstadoSeguimiento {

    @Override
    public String nombre() {
        return "NORMAL";
    }

    @Override
    public boolean permiteAlerta() {
        return true;
    }

    @Override
    public EstadoSeguimiento conPrecioNormal() {
        return this;
    }

    @Override
    public EstadoSeguimiento conMomentumAlto() {
        return new MomentumAlto();
    }

    @Override
    public EstadoSeguimiento conUmbralSuperado() {
        return new AlertaDisparada();
    }
}