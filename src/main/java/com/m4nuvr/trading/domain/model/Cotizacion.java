package com.m4nuvr.trading.domain.model;

import java.time.LocalDateTime;

public record Cotizacion(CodigoActivo codigo, Precio precio, LocalDateTime fecha) {

    public Cotizacion {
        if (codigo == null || precio == null || fecha == null) {
            throw new IllegalArgumentException("La cotizacion necesita codigo, precio y fecha");
        }
    }
}
