package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.RastreadorActivo;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.model.TipoActivo;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import com.m4nuvr.trading.domain.rules.ReglaPrecioSobreMedia;
import com.m4nuvr.trading.domain.strategy.MediaMovilSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RastreadorActivoTest {

    private final CodigoActivo codigo = new CodigoActivo("BTC");
    private final ReglaDeAlerta regla = new ReglaPrecioSobreMedia(new MediaMovilSimple(3), 5);
    private RastreadorActivo rastreador;

    @BeforeEach
    void preparar() {
        ActivoFinanciero activo = new ActivoFinanciero(codigo, "Bitcoin", TipoActivo.CRIPTOMONEDA, new Precio(100));
        rastreador = new RastreadorActivo(activo);
        registrar(100);
        registrar(100);
        registrar(100);
    }

    private Optional<SenalInversion> registrar(double precio) {
        return rastreador.registrarCotizacion(new Cotizacion(codigo, new Precio(precio), LocalDateTime.now()), regla);
    }

    @Test
    void generaSenalYCambiaDeEstado() {
        assertTrue(registrar(110).isPresent());
        assertEquals("ALERTA_DISPARADA", rastreador.getEstado());
    }

    @Test
    void noRepiteLaSenalMientrasSigaLaCondicion() {
        assertTrue(registrar(110).isPresent());
        assertTrue(registrar(130).isEmpty());
    }

    @Test
    void vuelveANormalCuandoLaCondicionDesaparece() {
        registrar(110);
        registrar(100);
        assertEquals("NORMAL", rastreador.getEstado());
    }

    @Test
    void pasaAMomentumAltoSinEmitirSenal() {
        Optional<SenalInversion> senal = registrar(103);

        assertTrue(senal.isEmpty());
        assertEquals("MOMENTUM_ALTO", rastreador.getEstado());
    }

    @Test
    void desdeMomentumAltoEmiteAlSuperarElUmbral() {
        registrar(103);

        Optional<SenalInversion> senal = registrar(110);

        assertTrue(senal.isPresent());
        assertEquals("ALERTA_DISPARADA", rastreador.getEstado());
    }

    @Test
    void rechazaCotizacionesDeOtroActivo() {
        Cotizacion otra = new Cotizacion(new CodigoActivo("ETH"), new Precio(10), LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> rastreador.registrarCotizacion(otra, regla));
    }
} 