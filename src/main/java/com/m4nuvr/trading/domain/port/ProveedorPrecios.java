package com.m4nuvr.trading.domain.port;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.Cotizacion;

public interface ProveedorPrecios {

    Cotizacion obtenerCotizacion(ActivoFinanciero activo);
}
