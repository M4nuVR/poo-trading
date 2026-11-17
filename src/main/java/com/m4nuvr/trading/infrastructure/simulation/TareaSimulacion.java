package com.m4nuvr.trading.infrastructure.simulation;

import com.m4nuvr.trading.application.ServicioMercado;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TareaSimulacion {

    private final ServicioMercado servicioMercado;

    public TareaSimulacion(ServicioMercado servicioMercado) {
        this.servicioMercado = servicioMercado;
    }

    @Scheduled(fixedRateString = "${simulacion.intervalo-ms:3000}")
    public void ejecutar() {
        servicioMercado.actualizarMercado();
    }
}
