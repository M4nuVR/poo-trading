package com.m4nuvr.trading.domain.port;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;

import java.util.List;

public interface CatalogoActivos {

    List<ActivoFinanciero> listar();
}