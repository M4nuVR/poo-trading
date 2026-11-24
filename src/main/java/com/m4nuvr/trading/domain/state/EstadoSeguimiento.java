package com.m4nuvr.trading.domain.state;

/**
 * Patron State: cada estado decide a cual pasar segun el nivel de momentum
 * del precio, sin if-else ni switch.
 */
public interface EstadoSeguimiento {

    String nombre();

    boolean permiteAlerta();

    EstadoSeguimiento conPrecioNormal();

    EstadoSeguimiento conMomentumAlto();

    EstadoSeguimiento conUmbralSuperado();

    /**
     * Indica si entrar a este estado desde el anterior dispara una alerta.
     * Solo AlertaDisparada lo sobrescribe.
     */
    default boolean disparaAlertaDesde(EstadoSeguimiento anterior) {
        return false;
    }
}