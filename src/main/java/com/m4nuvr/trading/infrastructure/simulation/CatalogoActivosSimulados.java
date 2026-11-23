package com.m4nuvr.trading.infrastructure.simulation;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.TipoActivo;
import com.m4nuvr.trading.domain.port.CatalogoActivos;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatalogoActivosSimulados implements CatalogoActivos {

    private final List<ActivoFinanciero> activos = List.of(
            activo("BTC", "Bitcoin", TipoActivo.CRIPTOMONEDA, 65000),
            activo("ETH", "Ethereum", TipoActivo.CRIPTOMONEDA, 3200),
            activo("EURUSD", "Euro / Dolar", TipoActivo.DIVISA, 1.08),
            activo("USDCOP", "Dolar / Peso colombiano", TipoActivo.DIVISA, 4100),
            activo("AAPL", "Apple", TipoActivo.ACCION, 190),
            activo("TSLA", "Tesla", TipoActivo.ACCION, 240));

    @Override
    public List<ActivoFinanciero> listar() {
        return activos;
    }

    private static ActivoFinanciero activo(String codigo, String nombre, TipoActivo tipo, double precioInicial) {
        return new ActivoFinanciero(new CodigoActivo(codigo), nombre, tipo, new Precio(precioInicial));
    }
}