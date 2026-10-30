package Reglas;

/**
 * Cuenta de ahorros: igual a la cuenta corriente, pero abona un interés del 1 %
 * sobre el saldo resultante después de cada transacción exitosa.
 */
public class CuentaAhorros extends CuentaCorriente {

    // Antes era 2 (anual, dividido en 12). Ahora: 1 % por transacción.
    private double tasaIntereses = 1;

    @Override
    public String getTipo() {
        return "Cuenta de ahorros";
    }

    @Override
    protected double aplicarInteres() {
        double interes = redondear(balance * tasaIntereses / 100);
        balance = redondear(balance + interes);
        return interes;
    }
}
