package com.m4nuvr.trading.domain.state;

public class AlertaDisparada implements EstadoSeguimiento {

    @Override
    public String nombre() {
        return "ALERTA_DISPARADA";
    }

    @Override
    public boolean permiteAlerta() {
        return false;
    }

    @Override
    public EstadoSeguimiento conPrecioNormal() {
        return new Normal();
    }

    @Override
    public EstadoSeguimiento conMomentumAlto() {
        return new MomentumAlto();
    }

    @Override
    public EstadoSeguimiento conUmbralSuperado() {
        return this;
    }

    @Override
    public boolean disparaAlertaDesde(EstadoSeguimiento anterior) {
        return anterior.permiteAlerta();
    }
}