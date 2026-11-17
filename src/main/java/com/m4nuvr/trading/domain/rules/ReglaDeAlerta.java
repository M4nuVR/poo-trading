package com.m4nuvr.trading.domain.rules;

import java.util.List;

public interface ReglaDeAlerta {

    boolean seCumple(List<Double> preciosAnteriores, double precioActual);

    String describir(double precioActual);
}
