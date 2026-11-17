package com.m4nuvr.trading.domain.model;

import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import com.m4nuvr.trading.domain.state.EstadoSeguimiento;
import com.m4nuvr.trading.domain.state.Vigilando;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RastreadorActivo {

    private static final int MAXIMO_HISTORIAL = 50;

    private final ActivoFinanciero activo;
    private final List<Double> historial = new ArrayList<>();
    private EstadoSeguimiento estado = new Vigilando();

    public RastreadorActivo(ActivoFinanciero activo) {
        this.activo = activo;
    }

    public Optional<SenalInversion> registrarCotizacion(Cotizacion cotizacion, ReglaDeAlerta regla) {
        if (!cotizacion.codigo().equals(activo.getCodigo())) {
            throw new IllegalArgumentException("La cotizacion no pertenece a " + activo.getCodigo());
        }
        double precioActual = cotizacion.precio().valor();
        boolean condicionCumplida = regla.seCumple(List.copyOf(historial), precioActual);

        SenalInversion senal = null;
        if (condicionCumplida && estado.permiteAlerta()) {
            senal = new SenalInversion(activo.getCodigo(), cotizacion.precio(), regla.describir(precioActual), cotizacion.fecha());
        }
        estado = estado.siguiente(condicionCumplida);
        agregarAlHistorial(precioActual);
        return Optional.ofNullable(senal);
    }

    private void agregarAlHistorial(double precio) {
        historial.add(precio);
        if (historial.size() > MAXIMO_HISTORIAL) {
            historial.remove(0);
        }
    }

    public ActivoFinanciero getActivo() {
        return activo;
    }

    public String getEstado() {
        return estado.nombre();
    }

    public Optional<Double> getUltimoPrecio() {
        return historial.isEmpty() ? Optional.empty() : Optional.of(historial.get(historial.size() - 1));
    }
}
