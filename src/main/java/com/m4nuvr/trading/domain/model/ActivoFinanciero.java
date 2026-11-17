package com.m4nuvr.trading.domain.model;

public class ActivoFinanciero {

    private final CodigoActivo codigo;
    private final String nombre;
    private final TipoActivo tipo;
    private final Precio precioInicial;

    public ActivoFinanciero(CodigoActivo codigo, String nombre, TipoActivo tipo, Precio precioInicial) {
        if (codigo == null || tipo == null || precioInicial == null) {
            throw new IllegalArgumentException("El activo necesita codigo, tipo y precio inicial");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El activo necesita un nombre");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioInicial = precioInicial;
    }

    public CodigoActivo getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoActivo getTipo() {
        return tipo;
    }

    public Precio getPrecioInicial() {
        return precioInicial;
    }
}
