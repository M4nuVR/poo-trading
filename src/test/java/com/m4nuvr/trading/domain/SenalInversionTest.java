package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.model.SenalPayload;
import com.m4nuvr.trading.domain.model.TipoSenal;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SenalInversionTest {

    private SenalInversion nuevaSenal() {
        return new SenalInversion(new CodigoActivo("BTC"), TipoSenal.COMPRA, new Precio(60000),
                "Momentum Crossover", "Precio supera la SMA5", LocalDateTime.now());
    }

    @Test
    void marcarNotificadaCambiaElEstado() {
        // Arrange
        SenalInversion senal = nuevaSenal();

        // Act
        senal.marcarNotificada();

        // Assert
        assertTrue(senal.isNotificada());
    }

    @Test
    void noSePuedeNotificarDosVeces() {
        SenalInversion senal = nuevaSenal();
        senal.marcarNotificada();

        assertThrows(IllegalStateException.class, senal::marcarNotificada);
    }

    @Test
    void generaElPayloadDelEvento() {
        SenalPayload payload = nuevaSenal().aPayload();

        assertEquals(new SenalPayload("BTC", "BUY", "Momentum Crossover"), payload);
    }

    @Test
    void rechazaSenalSinTipo() {
        assertThrows(IllegalArgumentException.class, () -> new SenalInversion(
                new CodigoActivo("BTC"), null, new Precio(1), "motivo", "mensaje", LocalDateTime.now()));
    }

    @Test
    void rechazaSenalSinMotivo() {
        assertThrows(IllegalArgumentException.class, () -> new SenalInversion(
                new CodigoActivo("BTC"), TipoSenal.COMPRA, new Precio(1), " ", "mensaje", LocalDateTime.now()));
    }

    @Test
    void payloadRechazaCamposVacios() {
        assertThrows(IllegalArgumentException.class, () -> new SenalPayload("BTC", "", "motivo"));
    }
} 