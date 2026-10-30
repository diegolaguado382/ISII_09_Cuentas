package com.cuentas.reglas;

/**
 * Cuenta corriente: hereda de Cuenta. No calcula ni abona intereses (RF-07).
 */
public class CuentaCorriente extends Cuenta {

    @Override
    public String getTipo() {
        return "Cuenta corriente";
    }

    @Override
    protected String aplicarInteres() {
        return "";
    }
}
