package com.m4nuvr.trading.infrastructure.notification;

import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.port.Notificador;
import org.springframework.stereotype.Component;

@Component
public class NotificadorConsola implements Notificador {

    @Override
    public void notificar(SenalInversion senal) {
        System.out.println("*** ALERTA DE SISTEMA: SENAL DE " + senal.getTipo() + " EN " + senal.getCodigo() + " ***");
        System.out.println("    " + senal.getMensaje() + " | " + senal.aPayload());
    }
}