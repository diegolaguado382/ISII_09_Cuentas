# Sistema de Cuentas Bancarias — Monolito Spring Boot

Aplicación web monolítica desarrollada con **Spring Boot 3** y **HTML5/CSS3/JS estático**, resultado de la reingeniería y migración del sistema de transacciones bancarias para el laboratorio de **Ingeniería de Software II (UIS)**.

---

## 1. Resumen del Proyecto

El sistema permite a un cliente bancario operar una única cuenta durante la sesión de ejecución (sin persistencia en base de datos, manteniendo el estado en memoria conforme a **RF-10**):

- **Jerarquía Polimórfica (`Cuenta`)**: Una clase abstracta `Cuenta` encapsula el saldo, la lista de objetos `Transaccion` del historial y la lógica común de consignar y retirar, delegando el cálculo de intereses al método polimórfico `aplicarInteres()`.
- **Cuenta Corriente (`CuentaCorriente`)**: Permite realizar consignaciones y retiros validando que existan fondos suficientes (`valor <= saldo`, permitiendo retirar todo el saldo) sin generar intereses (**RF-04, RF-05, RF-07**).
- **Cuenta de Ahorros (`CuentaAhorros`)**: Aplica automáticamente un **1 % de interés acumulativo** sobre el saldo resultante después de cada transacción exitosa, registrando la observación `"Interés 1 % aplicado"` (**RF-06**).
- **Historial de Movimientos (`Transaccion` y `TipoTransaccion`)**: Registra con número consecutivo cada transacción (`# Transacción`, `Tipo`, `Valor`, `Saldo Resultante`, `Observaciones`), incluyendo los retiros rechazados por `"Fondos insuficientes"` y mostrando lo más reciente en la parte superior (**RF-08, RF-09**).

---

## 2. Arquitectura y Estructura del Proyecto

```text
ISII_09_Cuentas/
├── pom.xml                                      # Configuración Maven y dependencias Spring Boot
├── src/
│   ├── main/
│   │   ├── java/com/cuentas/
│   │   │   ├── CuentasApplication.java          # Clase principal (@SpringBootApplication)
│   │   │   ├── controller/
│   │   │   │   └── TransaccionController.java   # Endpoints REST (/api/cuenta/*)
│   │   │   ├── service/
│   │   │   │   └── TransaccionService.java      # Controlador de dominio y validación de entrada (RF-03)
│   │   │   └── reglas/
│   │   │       ├── Cuenta.java                  # Clase abstracta base con saldo e historial
│   │   │       ├── CuentaCorriente.java         # Subclase sin intereses
│   │   │       ├── CuentaAhorros.java           # Subclase con interés acumulativo del 1 %
│   │   │       ├── Transaccion.java             # Entidad que representa un movimiento del historial
│   │   │       └── TipoTransaccion.java         # Enum (CONSIGNACION, RETIRO)
│   │   └── resources/
│   │       ├── application.properties           # Configuración del servidor (puerto 8080)
│   │       └── static/                          # Frontend estático servido por el monolito
│   │           ├── index.html                   # Interfaz de usuario (HTML5)
│   │           ├── styles.css                   # Estilos visuales (CSS3)
│   │           └── app.js                       # Consumo de API REST y renderizado del DOM
│   └── test/
│       └── java/com/cuentas/
│           └── TransaccionServiceTest.java      # Pruebas unitarias de los requerimientos RF-01 a RF-09
```

---

## 3. Requisitos Previos

- **Java JDK 21** (o JDK 17+)
- **Apache Maven 3.8+** (o soporte Maven integrado en VS Code / IntelliJ IDEA)

---

## 4. Cómo Compilar, Levantar y Ejecutar

### Opción A: Desde Visual Studio Code o IntelliJ IDEA
1. Abre la carpeta raíz `ISII_09_Cuentas` en el IDE.
2. Ubica el archivo [`src/main/java/com/cuentas/CuentasApplication.java`](src/main/java/com/cuentas/CuentasApplication.java).
3. Haz clic en **Run** (o presiona `F5` en VS Code usando la configuración incluida en `.vscode/launch.json`).
4. Abre en tu navegador: **`http://localhost:8080`**

### Opción B: Desde la Terminal (con Maven)
1. Ubícate en la carpeta raíz del proyecto:
   ```bash
   cd D:\ISII_09_Cuentas
   ```
2. Levanta el servidor Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
3. Accede desde el navegador a **`http://localhost:8080`**.

### Ejecutar las Pruebas Unitarias
```bash
mvn test
```

---

## 5. Cómo Funciona el Sistema

1. **Selección e Inicio de Cuenta (`POST /api/cuenta/iniciar` — RF-01)**:
   - Al cargar la página, los campos de movimiento están deshabilitados y el saldo inicial es `0.00`.
   - El cliente elige entre **Cuenta corriente** o **Cuenta de ahorros** y pulsa **Iniciar**.
   - Se instancia en memoria el tipo de cuenta elegido, se bloquea el selector de cuenta y se habilita el formulario de movimientos.

2. **Validación y Registro de Movimientos (`POST /api/cuenta/transaccion` — RF-02 a RF-09)**:
   - El cliente ingresa el **Valor**, elige el **Tipo de transacción** (**Consignación** o **Retiro**) y pulsa **Registrar movimiento**.
   - **Validación de entrada (RF-03)**: Si el valor ingresado no es numérico o es menor o igual a cero (`<= 0`), el sistema notifica el error mediante una alerta en pantalla y detiene el flujo sin generar una fila en el historial.
   - **Consignación (RF-04)**: Suma el monto al saldo disponible.
   - **Retiro (RF-05)**: Verifica que `valor <= saldo`. Si el monto supera el saldo disponible, rechaza el movimiento conservando el saldo intacto y agrega la fila al historial con la observación `"Fondos insuficientes"`.
   - **Aplicación de Intereses (RF-06 y RF-07)**: Si la cuenta es de **Ahorros** y el movimiento fue exitoso, calcula un `1 %` sobre el saldo resultante, lo suma al saldo acumulado y registra la observación `"Interés 1 % aplicado"`. En **Cuenta Corriente**, no se calcula interés y la observación queda vacía.

3. **Consulta de Estado e Historial (`GET /api/cuenta/estado` — RF-08 y RF-10)**:
   - Tras cada operación, la interfaz actualiza el saldo resultante y la tabla de historial (`# Transacción`, `Tipo`, `Valor`, `Saldo Resultante`, `Observaciones`) mostrando el movimiento más reciente en la parte superior.
