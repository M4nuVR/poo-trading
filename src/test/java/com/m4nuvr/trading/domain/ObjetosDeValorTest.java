package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjetosDeValorTest {

    @Test
    void precioNegativoOCeroNoEsValido() {
        assertThrows(IllegalArgumentException.class, () -> new Precio(-10));
        assertThrows(IllegalArgumentException.class, () -> new Precio(0));
    }

    @Test
    void codigoSeGuardaEnMayusculas() {
        assertEquals("BTC", new CodigoActivo(" btc ").valor());
    }

    @Test
    void codigoConCaracteresInvalidosNoEsValido() {
        assertThrows(IllegalArgumentException.class, () -> new CodigoActivo("B1"));
        assertThrows(IllegalArgumentException.class, () -> new CodigoActivo(""));
        assertThrows(IllegalArgumentException.class, () -> new CodigoActivo(null));
    }

    @Test
    void precioNoNumericoNoEsValido() {
        assertThrows(IllegalArgumentException.class, () -> new Precio(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Precio(Double.POSITIVE_INFINITY));
    }

    @Test
    void cotizacionSinDatosNoEsValida() {
        CodigoActivo codigo = new CodigoActivo("BTC");
        assertThrows(IllegalArgumentException.class, () -> new Cotizacion(codigo, null, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> new Cotizacion(null, new Precio(1), LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> new Cotizacion(codigo, new Precio(1), null));
    }
}