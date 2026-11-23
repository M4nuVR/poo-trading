package com.m4nuvr.trading.domain.model;

import java.time.LocalDateTime;

public class SenalInversion {

    private final CodigoActivo codigo;
    private final TipoSenal tipo;
    private final Precio precio;
    private final String motivo;
    private final String mensaje;
    private final LocalDateTime fecha;
    private boolean notificada;

    public SenalInversion(CodigoActivo codigo, TipoSenal tipo, Precio precio,
                          String motivo, String mensaje, LocalDateTime fecha) {
        this(codigo, tipo, precio, motivo, mensaje, fecha, false);
    }

    private SenalInversion(CodigoActivo codigo, TipoSenal tipo, Precio precio,
                           String motivo, String mensaje, LocalDateTime fecha, boolean notificada) {
        if (codigo == null || tipo == null || precio == null || fecha == null) {
            throw new IllegalArgumentException("La senal necesita codigo, tipo, precio y fecha");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("La senal necesita un motivo");
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("La senal necesita un mensaje");
        }
        this.codigo = codigo;
        this.tipo = tipo;
        this.precio = precio;
        this.motivo = motivo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.notificada = notificada;
    }

    /**
     * Reconstruye una senal ya guardada (por ejemplo desde la base de datos).
     */
    public static SenalInversion reconstruir(CodigoActivo codigo, TipoSenal tipo, Precio precio,
                                             String motivo, String mensaje, LocalDateTime fecha, boolean notificada) {
        return new SenalInversion(codigo, tipo, precio, motivo, mensaje, fecha, notificada);
    }

    public void marcarNotificada() {
        if (notificada) {
            throw new IllegalStateException("La senal ya fue notificada");
        }
        notificada = true;
    }

    public SenalPayload aPayload() {
        return new SenalPayload(codigo.valor(), tipo.codigo(), motivo);
    }

    public CodigoActivo getCodigo() {
        return codigo;
    }

    public TipoSenal getTipo() {
        return tipo;
    }

    public Precio getPrecio() {
        return precio;
    }

    public String getMotivo() {
        return motivo;
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