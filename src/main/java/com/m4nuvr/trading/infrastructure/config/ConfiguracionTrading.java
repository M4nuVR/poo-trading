package com.m4nuvr.trading.infrastructure.config;

import com.m4nuvr.trading.application.ServicioMercado;
import com.m4nuvr.trading.domain.port.CatalogoActivos;
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
    public ServicioMercado servicioMercado(CatalogoActivos catalogoActivos,
                                           ProveedorPrecios proveedorPrecios,
                                           ReglaDeAlerta reglaDeAlerta,
                                           RepositorioCotizaciones repositorioCotizaciones,
                                           RepositorioSenales repositorioSenales,
                                           Notificador notificador) {
        return new ServicioMercado(catalogoActivos, proveedorPrecios, reglaDeAlerta,
                repositorioCotizaciones, repositorioSenales, notificador);
    }
}