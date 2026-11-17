package com.m4nuvr.trading.domain.port;

import com.m4nuvr.trading.domain.model.SenalInversion;

public interface Notificador {

    void notificar(SenalInversion senal);
}
