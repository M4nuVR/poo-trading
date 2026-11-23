package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.rules.NivelMomentum;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import com.m4nuvr.trading.domain.rules.ReglaPrecioSobreMedia;
import com.m4nuvr.trading.domain.strategy.MediaMovilExponencial;
import com.m4nuvr.trading.domain.strategy.MediaMovilSimple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReglaPrecioSobreMediaTest {

    private final List<Double> precios = List.of(100.0, 100.0, 100.0, 100.0, 100.0);
    private final ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilSimple(5), 5);

    @Test
    void superaElUmbralCuandoElPrecioPasaDel5PorCiento() {
        // Arrange + Act
        NivelMomentum nivel = regla.evaluar(precios, 106);

        // Assert
        assertEquals(NivelMomentum.SOBRE_UMBRAL, nivel);
    }

    @Test
    void momentumAltoCuandoEstaSobreLaMediaPeroBajoElUmbral() {
        NivelMomentum nivel = regla.evaluar(precios, 105);

        assertEquals(NivelMomentum.ALTO, nivel);
    }

    @Test
    void normalCuandoElPrecioNoSuperaLaMedia() {
        NivelMomentum nivel = regla.evaluar(precios, 100);

        assertEquals(NivelMomentum.NORMAL, nivel);
    }

    @Test
    void normalSinSuficientesDatos() {
        NivelMomentum nivel = regla.evaluar(List.of(100.0, 100.0), 200);

        assertEquals(NivelMomentum.NORMAL, nivel);
    }

    @Test
    void funcionaIgualConOtraEstrategia() {
        ReglaDeAlerta conEma = new ReglaPrecioSobreMedia(new MediaMovilExponencial(5), 5);

        NivelMomentum nivel = conEma.evaluar(precios, 106);

        assertEquals(NivelMomentum.SOBRE_UMBRAL, nivel);
    }
}