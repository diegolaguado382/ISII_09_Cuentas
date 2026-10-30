package com.cuentas.controller;

import com.cuentas.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cuenta")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        return ResponseEntity.ok(transaccionService.obtenerEstado(null, false));
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Map<String, Object>> iniciarCuenta(@RequestBody Map<String, String> payload) {
        String tipoCuenta = payload.getOrDefault("tipoCuenta", TransaccionService.CUENTA_CORRIENTE);
        return ResponseEntity.ok(transaccionService.iniciarCuenta(tipoCuenta));
    }

    @PostMapping("/transaccion")
    public ResponseEntity<Map<String, Object>> registrarTransaccion(@RequestBody Map<String, String> payload) {
        String movimiento = payload.getOrDefault("movimiento", TransaccionService.CONSIGNACION);
        String valor = payload.getOrDefault("valor", "");
        return ResponseEntity.ok(transaccionService.registrarTransaccion(movimiento, valor));
    }

    @PostMapping("/reiniciar")
    public ResponseEntity<Map<String, Object>> reiniciarCuenta() {
        return ResponseEntity.ok(transaccionService.reiniciar());
    }
}
