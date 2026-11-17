package com.m4nuvr.trading.web;

import com.m4nuvr.trading.application.ResumenActivo;
import com.m4nuvr.trading.application.ServicioMercado;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MercadoController {

    private final ServicioMercado servicioMercado;

    public MercadoController(ServicioMercado servicioMercado) {
        this.servicioMercado = servicioMercado;
    }

    @GetMapping("/activos")
    public List<ResumenActivo> activos() {
        return servicioMercado.resumen();
    }

    @GetMapping("/activos/{codigo}/cotizaciones")
    public List<CotizacionRespuesta> cotizaciones(@PathVariable String codigo) {
        return servicioMercado.historial(codigo).stream()
                .map(CotizacionRespuesta::desde)
                .toList();
    }

    @GetMapping("/senales")
    public List<SenalRespuesta> senales() {
        return servicioMercado.senales().stream()
                .map(SenalRespuesta::desde)
                .toList();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarError(IllegalArgumentException error) {
        return Map.of("error", error.getMessage());
    }
}
