package com.m4nuvr.trading.domain.rules;

import com.m4nuvr.trading.domain.model.TipoSenal;

import java.util.List;

public interface ReglaDeAlerta {

    NivelMomentum evaluar(List<Double> preciosAnteriores, double precioActual);

    TipoSenal tipo();

    String motivo();

    String describir(double precioActual);
}