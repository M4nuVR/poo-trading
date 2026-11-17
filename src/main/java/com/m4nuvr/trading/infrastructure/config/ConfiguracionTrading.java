package com.m4nuvr.trading.infrastructure.config;

import com.m4nuvr.trading.application.ServicioMercado;
import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.TipoActivo;
import com.m4nuvr.trading.domain.port.Notificador;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import com.m4nuvr.trading.domain.rules.ReglaPrecioSobreMedia;
import com.m4nuvr.trading.domain.strategy.IndicadorTecnico;
import com.m4nuvr.trading.domain.strategy.MediaMovilSimple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConfiguracionTrading {

    @Bean
    public IndicadorTecnico indicadorTecnico() {
        return new MediaMovilSimple(5);
    }

    @Bean
    public ReglaDeAlerta reglaDeAlerta(IndicadorTecnico indicadorTecnico) {
        return new ReglaPrecioSobreMedia(indicadorTecnico, 5.0);
    }

    @Bean
    public ServicioMercado servicioMercado(ProveedorPrecios proveedorPrecios,
                                           ReglaDeAlerta reglaDeAlerta,
                                           RepositorioCotizaciones repositorioCotizaciones,
                                           RepositorioSenales repositorioSenales,
                                           Notificador notificador) {
        return new ServicioMercado(activos(), proveedorPrecios, reglaDeAlerta,
                repositorioCotizaciones, repositorioSenales, notificador);
    }

    private List<ActivoFinanciero> activos() {
        return List.of(
                activo("BTC", "Bitcoin", TipoActivo.CRIPTOMONEDA, 65000),
                activo("ETH", "Ethereum", TipoActivo.CRIPTOMONEDA, 3200),
                activo("EURUSD", "Euro / Dolar", TipoActivo.DIVISA, 1.08),
                activo("USDCOP", "Dolar / Peso colombiano", TipoActivo.DIVISA, 4100),
                activo("AAPL", "Apple", TipoActivo.ACCION, 190),
                activo("TSLA", "Tesla", TipoActivo.ACCION, 240));
    }

    private ActivoFinanciero activo(String codigo, String nombre, TipoActivo tipo, double precioInicial) {
        return new ActivoFinanciero(new CodigoActivo(codigo), nombre, tipo, new Precio(precioInicial));
    }
}
