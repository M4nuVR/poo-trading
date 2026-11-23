package com.m4nuvr.trading.domain.rules;

import com.m4nuvr.trading.domain.strategy.IndicadorTecnico;

import java.util.List;
import java.util.OptionalDouble;

public class ReglaPrecioSobreMedia implements ReglaDeAlerta {

    private final IndicadorTecnico indicador;
    private final double porcentaje;

    public ReglaPrecioSobreMedia(IndicadorTecnico indicador, double porcentaje) {
        if (indicador == null) {
            throw new IllegalArgumentException("La regla necesita un indicador");
        }
        if (porcentaje <= 0) {
            throw new IllegalArgumentException("El porcentaje debe ser positivo");
        }
        this.indicador = indicador;
        this.porcentaje = porcentaje;
    }

    @Override
    public NivelMomentum evaluar(List<Double> preciosAnteriores, double precioActual) {
        OptionalDouble media = indicador.calcular(preciosAnteriores);
        if (media.isEmpty() || precioActual <= media.getAsDouble()) {
            return NivelMomentum.NORMAL;
        }
        if (precioActual > media.getAsDouble() * (1 + porcentaje / 100)) {
            return NivelMomentum.SOBRE_UMBRAL;
        }
        return NivelMomentum.ALTO;
    }

    @Override
    public String describir(double precioActual) {
        return String.format("Precio %.4f supera en mas de %.1f%% la %s", precioActual, porcentaje, indicador.nombre());
    }
}
