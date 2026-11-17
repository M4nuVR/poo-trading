package com.m4nuvr.trading.domain.strategy;

import java.util.List;
import java.util.OptionalDouble;

public class MediaMovilExponencial implements IndicadorTecnico {

    private final int periodo;

    public MediaMovilExponencial(int periodo) {
        if (periodo < 1) {
            throw new IllegalArgumentException("El periodo debe ser mayor que cero");
        }
        this.periodo = periodo;
    }

    @Override
    public String nombre() {
        return "EMA" + periodo;
    }

    @Override
    public OptionalDouble calcular(List<Double> precios) {
        if (precios.size() < periodo) {
            return OptionalDouble.empty();
        }
        double factor = 2.0 / (periodo + 1);
        double ema = precios.subList(0, periodo).stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow();
        for (int i = periodo; i < precios.size(); i++) {
            ema = precios.get(i) * factor + ema * (1 - factor);
        }
        return OptionalDouble.of(ema);
    }
}
