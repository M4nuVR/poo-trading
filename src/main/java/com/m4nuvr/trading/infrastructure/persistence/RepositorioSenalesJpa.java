package com.m4nuvr.trading.infrastructure.persistence;

import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.model.TipoSenal;
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
                senal.getTipo().name(),
                senal.getPrecio().valor(),
                senal.getMotivo(),
                senal.getMensaje(),
                senal.getFecha(),
                senal.isNotificada()));
    }

    @Override
    public List<SenalInversion> listarRecientes() {
        return jpa.findTop20ByOrderByFechaDesc().stream()
                .map(e -> SenalInversion.reconstruir(
                        new CodigoActivo(e.getCodigo()),
                        TipoSenal.valueOf(e.getTipo()),
                        new Precio(e.getPrecio()),
                        e.getMotivo(),
                        e.getMensaje(),
                        e.getFecha(),
                        e.isNotificada()))
                .toList();
    }
}