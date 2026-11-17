package com.m4nuvr.trading.domain.state;

public interface EstadoSeguimiento {

    String nombre();

    boolean permiteAlerta();

    EstadoSeguimiento siguiente(boolean condicionCumplida);
}
