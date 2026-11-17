package com.m4nuvr.trading.infrastructure.persistence;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class RepositorioCotizacionesJpa implements RepositorioCotizaciones {

    private final CotizacionJpaRepository jpa;

    public RepositorioCotizacionesJpa(CotizacionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(Cotizacion cotizacion) {
        jpa.save(new CotizacionEntity(cotizacion.codigo().valor(), cotizacion.precio().valor(), cotizacion.fecha()));
    }

    @Override
    public List<Cotizacion> buscarUltimas(CodigoActivo codigo, int cantidad) {
        List<Cotizacion> cotizaciones = new ArrayList<>(jpa.findByCodigoOrderByFechaDesc(codigo.valor(), PageRequest.of(0, cantidad))
                .stream()
                .map(e -> new Cotizacion(new CodigoActivo(e.getCodigo()), new Precio(e.getPrecio()), e.getFecha()))
                .toList());
        Collections.reverse(cotizaciones);
        return cotizaciones;
    }
}
