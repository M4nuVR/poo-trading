package com.m4nuvr.trading.domain.rules;

import com.m4nuvr.trading.domain.strategy.IndicadorTecnico;

import java.util.List;

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
    public boolean seCumple(List<Double> preciosAnteriores, double precioActual) {
        return indicador.calcular(preciosAnteriores)
                .stream()
                .anyMatch(media -> precioActual > media * (1 + porcentaje / 100));
    }

    @Override
    public String describir(double precioActual) {
        return String.format("Precio %.4f supera en mas de %.1f%% la %s", precioActual, porcentaje, indicador.nombre());
    }
}
