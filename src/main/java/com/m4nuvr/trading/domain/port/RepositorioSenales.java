package com.m4nuvr.trading.domain.port;

import com.m4nuvr.trading.domain.model.SenalInversion;

import java.util.List;

public interface RepositorioSenales {

    void guardar(SenalInversion senal);

    List<SenalInversion> listarRecientes();
}
