package com.m4nuvr.trading.infrastructure.simulation;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class SimuladorPrecios implements ProveedorPrecios {

    private static final double VARIACION_MAXIMA = 0.04;

    private final Map<CodigoActivo, Double> ultimosPrecios = new HashMap<>();
    private final Random random = new Random();

    @Override
    public Cotizacion obtenerCotizacion(ActivoFinanciero activo) {
        double anterior = ultimosPrecios.getOrDefault(activo.getCodigo(), activo.getPrecioInicial().valor());
        double variacion = (random.nextDouble() * 2 - 1) * VARIACION_MAXIMA;
        double nuevo = Math.round(anterior * (1 + variacion) * 10000) / 10000.0;
        ultimosPrecios.put(activo.getCodigo(), nuevo);
        return new Cotizacion(activo.getCodigo(), new Precio(nuevo), LocalDateTime.now());
    }
}
