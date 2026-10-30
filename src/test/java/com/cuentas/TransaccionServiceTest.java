package com.cuentas;

import com.cuentas.reglas.Transaccion;
import com.cuentas.service.TransaccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionServiceTest {

    private TransaccionService service;

    @BeforeEach
    void setUp() {
        service = new TransaccionService();
    }

    @Test
    void criterioDeAceptacionRF08CuentaCorriente() {
        service.iniciarCuenta(TransaccionService.CUENTA_CORRIENTE);

        // #1 Consignación 100 -> Saldo 100, Observaciones ""
        Map<String, Object> r1 = service.registrarTransaccion(TransaccionService.CONSIGNACION, "100");
        assertEquals("100.00", r1.get("saldo"));
        assertFalse((Boolean) r1.get("error"));

        // #2 Retiro 50 -> Saldo 50, Observaciones ""
        Map<String, Object> r2 = service.registrarTransaccion(TransaccionService.RETIRO, "50");
        assertEquals("50.00", r2.get("saldo"));
        assertFalse((Boolean) r2.get("error"));

        // #3 Retiro 60 -> Saldo 50, Observaciones "Fondos insuficientes"
        Map<String, Object> r3 = service.registrarTransaccion(TransaccionService.RETIRO, "60");
        assertEquals("50.00", r3.get("saldo"));
        assertTrue((Boolean) r3.get("error"));

        @SuppressWarnings("unchecked")
        List<Transaccion> historial = (List<Transaccion>) r3.get("historial");
        assertEquals(3, historial.size());
        assertEquals("", historial.get(0).getObservaciones());
        assertEquals("", historial.get(1).getObservaciones());
        assertEquals("Fondos insuficientes", historial.get(2).getObservaciones());
    }

    @Test
    void cuentaAhorrosAbonaUnoPorcientoTrasCadaTransaccionExitosa() {
        service.iniciarCuenta(TransaccionService.CUENTA_AHORROS);

        // Consigna 100 -> interés 1% = 1.00 -> saldo 101.00
        Map<String, Object> r1 = service.registrarTransaccion(TransaccionService.CONSIGNACION, "100");
        assertEquals("101.00", r1.get("saldo"));

        // Retira 51 -> queda 50 -> interés 1% = 0.50 -> saldo 50.50
        Map<String, Object> r2 = service.registrarTransaccion(TransaccionService.RETIRO, "51");
        assertEquals("50.50", r2.get("saldo"));

        @SuppressWarnings("unchecked")
        List<Transaccion> historial = (List<Transaccion>) r2.get("historial");
        assertEquals("Interés 1 % aplicado", historial.get(0).getObservaciones());
        assertEquals("Interés 1 % aplicado", historial.get(1).getObservaciones());
    }

    @Test
    void valorInvalidoOMenorIgualACeroNoGeneraTransaccionEnHistorialRF03() {
        service.iniciarCuenta(TransaccionService.CUENTA_CORRIENTE);
        service.registrarTransaccion(TransaccionService.CONSIGNACION, "50");

        Map<String, Object> rechazoTexto = service.registrarTransaccion(TransaccionService.CONSIGNACION, "abc");
        assertTrue((Boolean) rechazoTexto.get("error"));

        Map<String, Object> rechazoNegativo = service.registrarTransaccion(TransaccionService.CONSIGNACION, "-10");
        assertTrue((Boolean) rechazoNegativo.get("error"));

        Map<String, Object> rechazoCero = service.registrarTransaccion(TransaccionService.RETIRO, "0");
        assertTrue((Boolean) rechazoCero.get("error"));

        @SuppressWarnings("unchecked")
        List<Transaccion> historial = (List<Transaccion>) rechazoCero.get("historial");
        assertEquals(1, historial.size());
        assertEquals("50.00", rechazoCero.get("saldo"));
    }
}
