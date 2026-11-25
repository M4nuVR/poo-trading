package com.m4nuvr.trading.application;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.model.TipoActivo;
import com.m4nuvr.trading.domain.port.Notificador;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ServicioMercadoTest {

    private final ActivoFinanciero activo = new ActivoFinanciero(
            new CodigoActivo("AAPL"), "Apple", TipoActivo.ACCION, new Precio(190));
    private final Cotizacion cotizacionValida = new Cotizacion(
            activo.getCodigo(), new Precio(200), LocalDateTime.of(2026, 1, 15, 9, 30));

    private ProveedorPrecios proveedor;
    private ReglaDeAlerta regla;
    private RepositorioCotizaciones repositorioCotizaciones;
    private RepositorioSenales repositorioSenales;
    private Notificador notificador;
    private ServicioMercado servicio;

    @BeforeEach
    void preparar() {
        proveedor = mock(ProveedorPrecios.class);
        regla = mock(ReglaDeAlerta.class);
        repositorioCotizaciones = mock(RepositorioCotizaciones.class);
        repositorioSenales = mock(RepositorioSenales.class);
        notificador = mock(Notificador.class);
        servicio = new ServicioMercado(List.of(activo), proveedor, regla,
                repositorioCotizaciones, repositorioSenales, notificador);

        when(proveedor.obtenerCotizacion(activo)).thenReturn(cotizacionValida);
        when(regla.describir(anyDouble())).thenReturn("alerta de prueba");
    }

    @Test
    void rechazaCotizacionDeOtroActivoAntesDeGuardarla() {
        ActivoFinanciero bitcoin = new ActivoFinanciero(
                new CodigoActivo("BTC"), "Bitcoin", TipoActivo.CRIPTOMONEDA, new Precio(100));
        ServicioMercado mercado = new ServicioMercado(List.of(activo, bitcoin), proveedor, regla,
                repositorioCotizaciones, repositorioSenales, notificador);
        Cotizacion cotizacionAjena = new Cotizacion(bitcoin.getCodigo(), new Precio(105), cotizacionValida.fecha());
        when(proveedor.obtenerCotizacion(activo)).thenReturn(cotizacionAjena);
        List<ResumenActivo> resumenAnterior = mercado.resumen();

        assertThrows(IllegalArgumentException.class, mercado::actualizarMercado);

        verifyNoInteractions(repositorioCotizaciones, regla, notificador, repositorioSenales);
        assertEquals(resumenAnterior, mercado.resumen());
    }

    @Test
    void permiteReintentarLaAlertaSiFallaElGuardadoDeLaCotizacion() {
        when(regla.seCumple(anyList(), anyDouble())).thenReturn(true);
        doThrow(new IllegalStateException("Fallo de escritura")).doNothing()
                .when(repositorioCotizaciones).guardar(cotizacionValida);
        List<ResumenActivo> resumenAnterior = servicio.resumen();

        assertThrows(IllegalStateException.class, servicio::actualizarMercado);
        assertEquals(resumenAnterior, servicio.resumen());
        verifyNoInteractions(notificador, repositorioSenales);

        servicio.actualizarMercado();

        verify(repositorioCotizaciones, times(2)).guardar(cotizacionValida);
        verify(notificador).notificar(any(SenalInversion.class));
        verify(repositorioSenales).guardar(any(SenalInversion.class));
        assertEquals("ALERTA_EMITIDA", servicio.resumen().get(0).estado());
    }

    @Test
    void guardaLaCotizacionYNotificaCuandoSeCumpleLaRegla() {
        when(regla.seCumple(anyList(), anyDouble())).thenReturn(true);

        servicio.actualizarMercado();

        verify(repositorioCotizaciones).guardar(any(Cotizacion.class));
        verify(notificador).notificar(any(SenalInversion.class));
        verify(repositorioSenales).guardar(any(SenalInversion.class));
    }

    @Test
    void noNotificaCuandoLaReglaNoSeCumple() {
        when(regla.seCumple(anyList(), anyDouble())).thenReturn(false);

        servicio.actualizarMercado();

        verify(repositorioCotizaciones).guardar(any(Cotizacion.class));
        verify(notificador, never()).notificar(any());
        verify(repositorioSenales, never()).guardar(any());
    }

    @Test
    void elResumenMuestraElUltimoPrecio() {
        when(regla.seCumple(anyList(), anyDouble())).thenReturn(false);

        servicio.actualizarMercado();

        ResumenActivo resumen = servicio.resumen().get(0);
        assertEquals("AAPL", resumen.codigo());
        assertEquals(200.0, resumen.ultimoPrecio().doubleValue(), 0.0001);
        assertEquals("VIGILANDO", resumen.estado());
    }

    @Test
    void historialDeActivoDesconocidoLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> servicio.historial("XYZ"));
    }
}
