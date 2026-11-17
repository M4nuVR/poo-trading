package com.m4nuvr.trading.infrastructure.persistence;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioSenalesJpa implements RepositorioSenales {

    private final SenalJpaRepository jpa;

    public RepositorioSenalesJpa(SenalJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(SenalInversion senal) {
        jpa.save(new SenalEntity(
                senal.getCodigo().valor(),
                senal.getPrecio().valor(),
                senal.getMensaje(),
                senal.getFecha(),
                senal.isNotificada()));
    }

    @Override
    public List<SenalInversion> listarRecientes() {
        return jpa.findTop20ByOrderByFechaDesc().stream()
                .map(e -> new SenalInversion(
                        new CodigoActivo(e.getCodigo()),
                        new Precio(e.getPrecio()),
                        e.getMensaje(),
                        e.getFecha(),
                        e.isNotificada()))
                .toList();
    }
}
