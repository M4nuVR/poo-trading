package com.m4nuvr.trading.application;

import com.m4nuvr.trading.domain.model.ActivoFinanciero;
import com.m4nuvr.trading.domain.model.CodigoActivo;
import com.m4nuvr.trading.domain.model.Cotizacion;
import com.m4nuvr.trading.domain.model.Precio;
import com.m4nuvr.trading.domain.model.SenalInversion;
import com.m4nuvr.trading.domain.model.TipoActivo;
import com.m4nuvr.trading.domain.model.TipoSenal;
import com.m4nuvr.trading.domain.port.CatalogoActivos;
import com.m4nuvr.trading.domain.port.Notificador;
import com.m4nuvr.trading.domain.port.ProveedorPrecios;
import com.m4nuvr.trading.domain.port.RepositorioCotizaciones;
import com.m4nuvr.trading.domain.port.RepositorioSenales;
import com.m4nuvr.trading.domain.rules.NivelMomentum;
import com.m4nuvr.trading.domain.rules.ReglaDeAlerta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioMercadoTest {

    private final ActivoFinanciero activo = new ActivoFinanciero(
            new CodigoActivo("AAPL"), "Apple", TipoActivo.ACCION, new Precio(190));

    @Mock
    private CatalogoActivos catalogoActivos;
    @Mock
    private ProveedorPrecios proveedorPrecios;
    @Mock
    private ReglaDeAlerta regla;
    @Mock
    private RepositorioCotizaciones repositorioCotizaciones;
    @Mock
    private RepositorioSenales repositorioSenales;
    @Mock
    private Notificador notificador;

    @InjectMocks
    private ServicioMercado servicio;

    private void prepararMercadoConPrecio(double precio) {
        when(catalogoActivos.listar()).thenReturn(List.of(activo));
        when(proveedorPrecios.obtenerCotizacion(activo))
                .thenReturn(new Cotizacion(activo.getCodigo(), new Precio(precio), LocalDateTime.now()));
    }

    @Test
    void guardaLaCotizacionYNotificaCuandoSeSuperaElUmbral() {
        // Arrange
        prepararMercadoConPrecio(200);
        when(regla.evaluar(anyList(), anyDouble())).thenReturn(NivelMomentum.SOBRE_UMBRAL);
        when(regla.tipo()).thenReturn(TipoSenal.COMPRA);
        when(regla.motivo()).thenReturn("Momentum Crossover");
        when(regla.describir(anyDouble())).thenReturn("alerta de prueba");

        // Act
        servicio.actualizarMercado();

        // Assert
        verify(repositorioCotizaciones).guardar(any(Cotizacion.class));
        verify(notificador).notificar(any(SenalInversion.class));
        ArgumentCaptor<SenalInversion> captor = ArgumentCaptor.forClass(SenalInversion.class);
        verify(repositorioSenales).guardar(captor.capture());
        assertTrue(captor.getValue().isNotificada());
        assertEquals(TipoSenal.COMPRA, captor.getValue().getTipo());
    }

    @Test
    void noNotificaCuandoElPrecioEsNormal() {
        // Arrange
        prepararMercadoConPrecio(200);
        when(regla.evaluar(anyList(), anyDouble())).thenReturn(NivelMomentum.NORMAL);

        // Act
        servicio.actualizarMercado();

        // Assert
        verify(repositorioCotizaciones).guardar(any(Cotizacion.class));
        verify(notificador, never()).notificar(any());
        verify(repositorioSenales, never()).guardar(any());
    }

    @Test
    void noNotificaConMomentumAlto() {
        prepararMercadoConPrecio(200);
        when(regla.evaluar(anyList(), anyDouble())).thenReturn(NivelMomentum.ALTO);

        servicio.actualizarMercado();

        verify(notificador, never()).notificar(any());
        assertEquals("MOMENTUM_ALTO", servicio.resumen().get(0).estado());
    }

    @Test
    void elResumenMuestraElUltimoPrecio() {
        // Arrange
        prepararMercadoConPrecio(200);
        when(regla.evaluar(anyList(), anyDouble())).thenReturn(NivelMomentum.NORMAL);

        // Act
        servicio.actualizarMercado();
        ResumenActivo resumen = servicio.resumen().get(0);

        // Assert
        assertEquals("AAPL", resumen.codigo());
        assertEquals(200.0, resumen.ultimoPrecio().doubleValue(), 0.0001);
        assertEquals("NORMAL", resumen.estado());
    }

    @Test
    void historialConsultaElRepositorio() {
        when(catalogoActivos.listar()).thenReturn(List.of(activo));
        when(repositorioCotizaciones.buscarUltimas(activo.getCodigo(), 30)).thenReturn(List.of());

        servicio.historial("aapl");

        verify(repositorioCotizaciones).buscarUltimas(new CodigoActivo("AAPL"), 30);
    }

    @Test
    void historialDeActivoDesconocidoLanzaError() {
        when(catalogoActivos.listar()).thenReturn(List.of(activo));

        assertThrows(IllegalArgumentException.class, () -> servicio.historial("XYZ"));
        verify(repositorioCotizaciones, never()).buscarUltimas(any(), anyInt());
    }
}