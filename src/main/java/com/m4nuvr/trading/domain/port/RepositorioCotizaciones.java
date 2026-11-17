package com.m4nuvr.trading.domain.port;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;

import java.util.List;

public interface RepositorioCotizaciones {

    void guardar(Cotizacion cotizacion);

    List<Cotizacion> buscarUltimas(CodigoActivo codigo, int cantidad);
}
