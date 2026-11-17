package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.strategy.IndicadorTecnico;
import com.m4nuvr.trading.domain.strategy.MediaMovilExponencial;
import com.m4nuvr.trading.domain.strategy.MediaMovilSimple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicadoresTest {

    @Test
    void smaPromediaLasUltimasCotizaciones() {
        IndicadorTecnico sma = new MediaMovilSimple(3);
        double resultado = sma.calcular(List.of(1.0, 10.0, 20.0, 30.0)).getAsDouble();
        assertEquals(20.0, resultado, 0.0001);
    }

    @Test
    void smaSinSuficientesDatosNoDevuelveValor() {
        IndicadorTecnico sma = new MediaMovilSimple(5);
        assertTrue(sma.calcular(List.of(1.0, 2.0)).isEmpty());
    }

    @Test
    void emaDaMasPesoAlUltimoPrecio() {
        IndicadorTecnico ema = new MediaMovilExponencial(3);
        double resultado = ema.calcular(List.of(10.0, 20.0, 30.0, 40.0)).getAsDouble();
        assertEquals(30.0, resultado, 0.0001);
    }

    @Test
    void emaSinSuficientesDatosNoDevuelveValor() {
        IndicadorTecnico ema = new MediaMovilExponencial(3);
        assertTrue(ema.calcular(List.of(10.0)).isEmpty());
    }
}
