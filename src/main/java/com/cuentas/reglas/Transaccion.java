package com.cuentas.reglas;

import java.util.Locale;

/**
 * Representa un movimiento del historial de una cuenta bancaria.
 */
public class Transaccion {

    private final int numero;
    private final TipoTransaccion tipo;
    private final double valor;
    private final double saldoResultante;
    private final String observaciones;
    private final boolean exitosa;

    public Transaccion(int numero, TipoTransaccion tipo, double valor, double saldoResultante, String observaciones, boolean exitosa) {
        this.numero = numero;
        this.tipo = tipo;
        this.valor = valor;
        this.saldoResultante = saldoResultante;
        this.observaciones = observaciones == null ? "" : observaciones;
        this.exitosa = exitosa;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo.getEtiqueta();
    }

    public TipoTransaccion getTipoEnum() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getValorFormateado() {
        return formatear(valor);
    }

    public double getSaldoResultante() {
        return saldoResultante;
    }

    public String getSaldoResultanteFormateado() {
        return formatear(saldoResultante);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isExitosa() {
        return exitosa;
    }

    public static String formatear(double monto) {
        if (monto == Math.floor(monto) && !Double.isInfinite(monto)) {
            return String.format(Locale.US, "%.2f", monto);
        }
        return String.format(Locale.US, "%.2f", monto);
    }
}
