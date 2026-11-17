package com.m4nuvr.trading.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
public class CotizacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private double precio;
    private LocalDateTime fecha;

    protected CotizacionEntity() {
    }

    public CotizacionEntity(String codigo, double precio, LocalDateTime fecha) {
        this.codigo = codigo;
        this.precio = precio;
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getPrecio() {
        return precio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
