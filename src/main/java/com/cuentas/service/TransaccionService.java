package com.cuentas.service;

import com.cuentas.reglas.Cuenta;
import com.cuentas.reglas.CuentaAhorros;
import com.cuentas.reglas.CuentaCorriente;
import com.cuentas.reglas.TipoTransaccion;
import com.cuentas.reglas.Transaccion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Conecta la capa web con el dominio (Cuenta) y valida la entrada (RF-03).
 */
@Service
public class TransaccionService {

    public static final String CONSIGNACION = "Consignación";
    public static final String RETIRO = "Retiro";
    public static final String CUENTA_AHORROS = "Cuenta de ahorros";
    public static final String CUENTA_CORRIENTE = "Cuenta corriente";

    private Cuenta cuenta;

    public synchronized Map<String, Object> iniciarCuenta(String tipoCuenta) {
        if (CUENTA_AHORROS.equalsIgnoreCase(tipoCuenta) || "Ahorros".equalsIgnoreCase(tipoCuenta)) {
            cuenta = new CuentaAhorros();
        } else {
            cuenta = new CuentaCorriente();
        }
        return obtenerEstado(null, false);
    }

    public synchronized Map<String, Object> reiniciar() {
        cuenta = null;
        return obtenerEstado(null, false);
    }

    public synchronized Map<String, Object> registrarTransaccion(String tipoMovimiento, String valorTexto) {
        if (cuenta == null) {
            return obtenerEstado("Debe seleccionar e iniciar una cuenta antes de registrar movimientos.", true);
        }

        // RF-03: Validar que el valor ingresado sea numérico y mayor que cero.
        // Un dato inválido detiene el flujo y NO genera una Transaccion en el historial.
        String texto = valorTexto == null ? "" : valorTexto.trim().replace(',', '.');
        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException ex) {
            return obtenerEstado("Ingrese un valor numérico válido y mayor que cero.", true);
        }

        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            return obtenerEstado("El valor ingresado debe ser mayor que cero.", true);
        }

        TipoTransaccion tipo = TipoTransaccion.desdeTexto(tipoMovimiento);
        Transaccion transaccion;
        if (tipo == TipoTransaccion.RETIRO) {
            transaccion = cuenta.retirar(valor);
        } else {
            transaccion = cuenta.consignar(valor);
        }

        if (!transaccion.isExitosa()) {
            return obtenerEstado("Fondos insuficientes: el retiro supera el saldo disponible.", true);
        }

        return obtenerEstado(null, false);
    }

    public synchronized Map<String, Object> obtenerEstado(String mensajeAlerta, boolean esError) {
        boolean iniciada = cuenta != null;
        String tipoCuenta = iniciada ? cuenta.getTipo() : "";
        String titulo = iniciada ? "Transacciones Bancarias - " + tipoCuenta : "Transacciones Bancarias";
        String saldo = iniciada ? formato(cuenta.getSaldo()) : "0.00";
        List<Transaccion> historial = iniciada ? cuenta.getHistorial() : List.of();

        return Map.of(
                "iniciada", iniciada,
                "tipoCuenta", tipoCuenta,
                "titulo", titulo,
                "saldo", saldo,
                "historial", historial,
                "mensaje", mensajeAlerta == null ? "" : mensajeAlerta,
                "error", esError
        );
    }

    public synchronized Cuenta getCuenta() {
        return cuenta;
    }

    private static String formato(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
