package com.m4nuvr.trading.domain.strategy;

import java.util.List;
import java.util.OptionalDouble;

public interface IndicadorTecnico {

    String nombre();

    OptionalDouble calcular(List<Double> precios);
}
