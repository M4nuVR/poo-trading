package com.m4nuvr.trading.domain;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import org.junit.jupiter.api.Test;

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
}
