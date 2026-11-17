package com.m4nuvr.trading.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotizacionJpaRepository extends JpaRepository<CotizacionEntity, Long> {

    List<CotizacionEntity> findByCodigoOrderByFechaDesc(String codigo, Pageable pageable);
}
