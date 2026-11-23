package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.rules.NivelMomentum;
import com.m4nuvr.trading.domain.state.AlertaDisparada;
import com.m4nuvr.trading.domain.state.EstadoSeguimiento;
import com.m4nuvr.trading.domain.state.MomentumAlto;
import com.m4nuvr.trading.domain.state.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadosTest {

    @Test
    void normalPasaAMomentumAlto() {
        EstadoSeguimiento estado = new Normal();

        EstadoSeguimiento siguiente = NivelMomentum.ALTO.aplicarA(estado);

        assertEquals("MOMENTUM_ALTO", siguiente.nombre());
    }

    @Test
    void normalPasaDirectoAAlertaYLaDispara() {
        EstadoSeguimiento estado = new Normal();

        EstadoSeguimiento siguiente = NivelMomentum.SOBRE_UMBRAL.aplicarA(estado);

        assertEquals("ALERTA_DISPARADA", siguiente.nombre());
        assertTrue(siguiente.disparaAlertaDesde(estado));
    }

    @Test
    void alertaDisparadaNoSeRepite() {
        EstadoSeguimiento estado = new AlertaDisparada();

        EstadoSeguimiento siguiente = NivelMomentum.SOBRE_UMBRAL.aplicarA(estado);

        assertEquals("ALERTA_DISPARADA", siguiente.nombre());
        assertFalse(siguiente.disparaAlertaDesde(estado));
    }

    @Test
    void alertaDisparadaVuelveANormal() {
        EstadoSeguimiento siguiente = NivelMomentum.NORMAL.aplicarA(new AlertaDisparada());

        assertEquals("NORMAL", siguiente.nombre());
    }

    @Test
    void momentumAltoNoDisparaAlerta() {
        EstadoSeguimiento estado = new Normal();

        EstadoSeguimiento siguiente = NivelMomentum.ALTO.aplicarA(estado);

        assertFalse(siguiente.disparaAlertaDesde(estado));
    }

    @Test
    void momentumAltoVuelveANormal() {
        EstadoSeguimiento siguiente = NivelMomentum.NORMAL.aplicarA(new MomentumAlto());

        assertEquals("NORMAL", siguiente.nombre());
    }
} 