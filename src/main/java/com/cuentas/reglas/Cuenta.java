package com.cuentas.reglas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase abstracta Cuenta: contiene la lógica común de consignar, retirar,
 * validar fondos y almacenar el historial de transacciones.
 */
public abstract class Cuenta {

    protected double saldo = 0;
    protected final List<Transaccion> historial = new ArrayList<>();

    public abstract String getTipo();

    /**
     * Punto de extensión polimórfico que se ejecuta tras cada transacción exitosa.
     * @return observación generada por la aplicación de interés (vacío si no aplica)
     */
    protected abstract String aplicarInteres();

    /**
     * RF-04: En una consignación, suma el valor al saldo y aplica interés si corresponde.
     */
    public Transaccion consignar(double valor) {
        validarPositivo(valor);
        saldo = redondear(saldo + valor);
        String observacion = aplicarInteres();
        return registrarEnHistorial(TipoTransaccion.CONSIGNACION, valor, observacion, true);
    }

    /**
     * RF-05: En un retiro, resta el valor al saldo solo si valor <= saldo.
     * Si es mayor, rechaza el movimiento, deja el saldo igual y registra
     * la observación "Fondos insuficientes" sin aplicar interés.
     */
    public Transaccion retirar(double valor) {
        validarPositivo(valor);
        if (valor > saldo) {
            return registrarEnHistorial(TipoTransaccion.RETIRO, valor, "Fondos insuficientes", false);
        }
        saldo = redondear(saldo - valor);
        String observacion = aplicarInteres();
        return registrarEnHistorial(TipoTransaccion.RETIRO, valor, observacion, true);
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Transaccion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    private Transaccion registrarEnHistorial(TipoTransaccion tipo, double valor, String observaciones, boolean exitosa) {
        int numero = historial.size() + 1;
        Transaccion transaccion = new Transaccion(numero, tipo, valor, saldo, observaciones, exitosa);
        historial.add(transaccion);
        return transaccion;
    }

    private void validarPositivo(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("El valor ingresado debe ser numérico y mayor que cero.");
        }
    }

    protected static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
