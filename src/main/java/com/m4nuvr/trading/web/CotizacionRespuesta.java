package com.m4nuvr.trading.web;

import com.m4nuvr.trading.domain.model.Cotizacion;

import java.time.LocalDateTime;

public record CotizacionRespuesta(String codigo, double precio, LocalDateTime fecha) {

    public static CotizacionRespuesta desde(Cotizacion cotizacion) {
        return new CotizacionRespuesta(cotizacion.codigo().valor(), cotizacion.precio().valor(), cotizacion.fecha());
    }
}
