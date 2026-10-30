package com.cuentas.reglas;

/**
 * Cuenta de ahorros: hereda de Cuenta y abona un interés acumulativo del 1 %
 * sobre el saldo resultante después de cada transacción exitosa (RF-06).
 */
public class CuentaAhorros extends Cuenta {

    private static final double TASA_INTERES = 0.01;

    @Override
    public String getTipo() {
        return "Cuenta de ahorros";
    }

    @Override
    protected String aplicarInteres() {
        double interes = redondear(saldo * TASA_INTERES);
        saldo = redondear(saldo + interes);
        return "Interés 1 % aplicado";
    }
}
