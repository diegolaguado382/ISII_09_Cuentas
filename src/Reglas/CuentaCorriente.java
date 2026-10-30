package Reglas;

/**
 * Cuenta corriente: consigna, retira y consulta el saldo. No genera intereses.
 * Es la clase base; CuentaAhorros hereda de ella y solo añade el interés.
 *
 * REINGENIERÍA: el saldo dejó de ser estático (ahora pertenece a cada cuenta),
 * las operaciones validan el valor antes de modificar el saldo y se agregó el
 * método aplicarInteres() como punto de extensión para las subclases.
 */
public class CuentaCorriente {

    protected double balance = 0;

    public void setCuenta(double saldo) {
        balance = saldo;
    }

    /** Nombre del tipo de cuenta (se usa en la interfaz). */
    public String getTipo() {
        return "Cuenta corriente";
    }

    /**
     * Consigna un valor en la cuenta.
     * @return interés generado por la operación (0 en cuenta corriente)
     * @throws IllegalArgumentException si el valor no es positivo
     */
    public double depositar(double consignacion) {
        validarValor(consignacion);
        balance = redondear(balance + consignacion);
        return aplicarInteres();
    }

    /**
     * Retira un valor de la cuenta.
     * @return interés generado por la operación (0 en cuenta corriente)
     * @throws IllegalArgumentException si el valor no es positivo o supera el saldo;
     *         en ese caso el saldo NO se modifica
     */
    public double retirar(double retiro) {
        validarValor(retiro);
        if (retiro > balance) {
            throw new IllegalArgumentException("Fondos insuficientes: el retiro supera el saldo disponible");
        }
        balance = redondear(balance - retiro);
        return aplicarInteres();
    }

    public double getbalance() {
        return balance;
    }

    /**
     * Se ejecuta después de cada transacción exitosa.
     * La cuenta corriente no genera intereses.
     * @return interés abonado al saldo
     */
    protected double aplicarInteres() {
        return 0;
    }

    private void validarValor(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser un número positivo");
        }
    }

    protected static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
