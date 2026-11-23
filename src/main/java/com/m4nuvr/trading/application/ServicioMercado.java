package com.m4nuvr.trading.application;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.RastreadorActivo;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.port.CatalogoActivos;
import com.m4nuvr.trading.domain.port.Notificador;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioMercado {

    private final Map<CodigoActivo, RastreadorActivo> rastreadores = new HashMap<>();
    private final CatalogoActivos catalogoActivos;
    private final ProveedorPrecios proveedorPrecios;
    private final ReglaDeAlerta regla;
    private final RepositorioCotizaciones repositorioCotizaciones;
    private final RepositorioSenales repositorioSenales;
    private final Notificador notificador;

    public ServicioMercado(CatalogoActivos catalogoActivos,
                           ProveedorPrecios proveedorPrecios,
                           ReglaDeAlerta regla,
                           RepositorioCotizaciones repositorioCotizaciones,
                           RepositorioSenales repositorioSenales,
                           Notificador notificador) {
        this.catalogoActivos = catalogoActivos;
        this.proveedorPrecios = proveedorPrecios;
        this.regla = regla;
        this.repositorioCotizaciones = repositorioCotizaciones;
        this.repositorioSenales = repositorioSenales;
        this.notificador = notificador;
    }

    public synchronized void actualizarMercado() {
        for (ActivoFinanciero activo : catalogoActivos.listar()) {
            Cotizacion cotizacion = proveedorPrecios.obtenerCotizacion(activo);
            repositorioCotizaciones.guardar(cotizacion);
            rastreadorDe(activo).registrarCotizacion(cotizacion, regla).ifPresent(this::emitir);
        }
    }

    private RastreadorActivo rastreadorDe(ActivoFinanciero activo) {
        return rastreadores.computeIfAbsent(activo.getCodigo(), codigo -> new RastreadorActivo(activo));
    }

    private void emitir(SenalInversion senal) {
        notificador.notificar(senal);
        senal.marcarNotificada();
        repositorioSenales.guardar(senal);
    }

    public synchronized List<ResumenActivo> resumen() {
        return catalogoActivos.listar().stream()
                .map(this::rastreadorDe)
                .map(r -> new ResumenActivo(
                        r.getActivo().getCodigo().valor(),
                        r.getActivo().getNombre(),
                        r.getActivo().getTipo().name(),
                        r.getUltimoPrecio().orElse(null),
                        r.getEstado()))
                .toList();
    }

    public List<Cotizacion> historial(String codigo) {
        CodigoActivo codigoActivo = new CodigoActivo(codigo);
        boolean registrado = catalogoActivos.listar().stream()
                .anyMatch(activo -> activo.getCodigo().equals(codigoActivo));
        if (!registrado) {
            throw new IllegalArgumentException("Activo no registrado: " + codigo);
        }
        return repositorioCotizaciones.buscarUltimas(codigoActivo, 30);
    }

    public List<SenalInversion> senales() {
        return repositorioSenales.listarRecientes();
    }
}