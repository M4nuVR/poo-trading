package com.m4nuvr.trading.domain.strategy;

import java.util.List;
import java.util.OptionalDouble;

public class MediaMovilSimple implements IndicadorTecnico {

    private final int periodo;

    public MediaMovilSimple(int periodo) {
        if (periodo < 1) {
            throw new IllegalArgumentException("El periodo debe ser mayor que cero");
        }
        this.periodo = periodo;
    }

    @Override
    public String nombre() {
        return "SMA" + periodo;
    }

    @Override
    public OptionalDouble calcular(List<Double> precios) {
        if (precios.size() < periodo) {
            return OptionalDouble.empty();
        }
        return precios.subList(precios.size() - periodo, precios.size())
                .stream()
                .mapToDouble(Double::doubleValue)
                .average();
    }
}
