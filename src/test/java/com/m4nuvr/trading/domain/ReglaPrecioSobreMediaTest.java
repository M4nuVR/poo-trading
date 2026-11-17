package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import com.m4nuvr.trading.domain.rules.ReglaPrecioSobreMedia;
import com.m4nuvr.trading.domain.strategy.MediaMovilExponencial;
import com.m4nuvr.trading.domain.strategy.MediaMovilSimple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReglaPrecioSobreMediaTest {

    private final List<Double> precios = List.of(100.0, 100.0, 100.0, 100.0, 100.0);

    @Test
    void seCumpleCuandoElPrecioSuperaElUmbral() {
        ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilSimple(5), 5);
        assertTrue(regla.seCumple(precios, 106));
    }

    @Test
    void noSeCumpleBajoElUmbral() {
        ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilSimple(5), 5);
        assertFalse(regla.seCumple(precios, 105));
    }

    @Test
    void noSeCumpleSinSuficientesDatos() {
        ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilSimple(5), 5);
        assertFalse(regla.seCumple(List.of(100.0, 100.0), 200));
    }

    @Test
    void funcionaIgualConOtraEstrategia() {
        ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilExponencial(5), 5);
        assertTrue(regla.seCumple(precios, 106));
    }
}
