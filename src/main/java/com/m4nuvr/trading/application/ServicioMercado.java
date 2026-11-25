package com.m4nuvr.trading.application;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.RastreadorActivo;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.port.Notificador;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioMercado {

    private final Map<CodigoActivo, RastreadorActivo> rastreadores = new LinkedHashMap<>();
    private final ProveedorPrecios proveedorPrecios;
    private final ReglaDeAlerta regla;
    private final RepositorioCotizaciones repositorioCotizaciones;
    private final RepositorioSenales repositorioSenales;
    private final Notificador notificador;

    public ServicioMercado(List<ActivoFinanciero> activos,
                           ProveedorPrecios proveedorPrecios,
                           ReglaDeAlerta regla,
                           RepositorioCotizaciones repositorioCotizaciones,
                           RepositorioSenales repositorioSenales,
                           Notificador notificador) {
        activos.forEach(activo -> rastreadores.put(activo.getCodigo(), new RastreadorActivo(activo)));
        this.proveedorPrecios = proveedorPrecios;
        this.regla = regla;
        this.repositorioCotizaciones = repositorioCotizaciones;
        this.repositorioSenales = repositorioSenales;
        this.notificador = notificador;
    }

    public synchronized void actualizarMercado() {
        for (RastreadorActivo rastreador : rastreadores.values()) {
            Cotizacion cotizacion = proveedorPrecios.obtenerCotizacion(rastreador.getActivo());
            rastreador.validarPertenencia(cotizacion);
            repositorioCotizaciones.guardar(cotizacion);
            rastreador.registrarCotizacion(cotizacion, regla).ifPresent(this::emitir);
        }
    }

    private void emitir(SenalInversion senal) {
        notificador.notificar(senal);
        senal.marcarNotificada();
        repositorioSenales.guardar(senal);
    }

    public synchronized List<ResumenActivo> resumen() {
        return rastreadores.values().stream()
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
        if (!rastreadores.containsKey(codigoActivo)) {
            throw new IllegalArgumentException("Activo no registrado: " + codigo);
        }
        return repositorioCotizaciones.buscarUltimas(codigoActivo, 30);
    }

    public List<SenalInversion> senales() {
        return repositorioSenales.listarRecientes();
    }
}
