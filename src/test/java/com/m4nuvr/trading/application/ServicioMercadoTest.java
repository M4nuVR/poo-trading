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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServicioMercadoTest {

    private final ActivoFinanciero activo = new ActivoFinanciero(
            new CodigoActivo("AAPL"), "Apple", TipoActivo.ACCION, new Precio(190));

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

        when(proveedor.obtenerCotizacion(activo))
                .thenReturn(new Cotizacion(activo.getCodigo(), new Precio(200), LocalDateTime.now()));
        when(regla.describir(anyDouble())).thenReturn("alerta de prueba");
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
