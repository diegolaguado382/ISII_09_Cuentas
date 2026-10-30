package com.cuentas.reglas;

public enum TipoTransaccion {
    CONSIGNACION("Consignación"),
    RETIRO("Retiro");

    private final String etiqueta;

    TipoTransaccion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static TipoTransaccion desdeTexto(String texto) {
        if (texto != null && (texto.equalsIgnoreCase("Retiro") || texto.equalsIgnoreCase("RETIRO"))) {
            return RETIRO;
        }
        return CONSIGNACION;
    }
}
