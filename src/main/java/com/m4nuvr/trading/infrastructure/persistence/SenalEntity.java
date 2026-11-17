package com.m4nuvr.trading.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "senales")
public class SenalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private double precio;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean notificada;

    protected SenalEntity() {
    }

    public SenalEntity(String codigo, double precio, String mensaje, LocalDateTime fecha, boolean notificada) {
        this.codigo = codigo;
        this.precio = precio;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.notificada = notificada;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public boolean isNotificada() {
        return notificada;
    }
}
