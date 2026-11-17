package com.m4nuvr.trading.domain.model;

import java.time.LocalDateTime;

public class SenalInversion {

    private final CodigoActivo codigo;
    private final Precio precio;
    private final String mensaje;
    private final LocalDateTime fecha;
    private boolean notificada;

    public SenalInversion(CodigoActivo codigo, Precio precio, String mensaje, LocalDateTime fecha) {
        this(codigo, precio, mensaje, fecha, false);
    }

    public SenalInversion(CodigoActivo codigo, Precio precio, String mensaje, LocalDateTime fecha, boolean notificada) {
        this.codigo = codigo;
        this.precio = precio;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.notificada = notificada;
    }

    public void marcarNotificada() {
        if (notificada) {
            throw new IllegalStateException("La senal ya fue notificada");
        }
        notificada = true;
    }

    public CodigoActivo getCodigo() {
        return codigo;
    }

    public Precio getPrecio() {
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
