package com.m4nuvr.trading.domain.rules;

import com.m4nuvr.trading.domain.state.EstadoSeguimiento;

/**
 * Resultado de evaluar un precio contra la media. Cada valor sabe que
 * transicion pedirle al estado actual (polimorfismo en el enum).
 */
public enum NivelMomentum {

    NORMAL {
        @Override
        public EstadoSeguimiento aplicarA(EstadoSeguimiento estado) {
            return estado.conPrecioNormal();
        }
    },
    ALTO {
        @Override
        public EstadoSeguimiento aplicarA(EstadoSeguimiento estado) {
            return estado.conMomentumAlto();
        }
    },
    SOBRE_UMBRAL {
        @Override
        public EstadoSeguimiento aplicarA(EstadoSeguimiento estado) {
            return estado.conUmbralSuperado();
        }
    };

    public abstract EstadoSeguimiento aplicarA(EstadoSeguimiento estado);
} 