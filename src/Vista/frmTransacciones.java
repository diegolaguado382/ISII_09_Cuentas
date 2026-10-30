package Vista;

import Reglas.CuentaAhorros;
import Reglas.CuentaCorriente;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmTransacciones extends javax.swing.JFrame {

    private static final String CONSIGNACION = "Consignación";
    private static final String RETIRO = "Retiro";

    // Cuenta con la que se trabaja durante toda la ejecución (se elige al iniciar)
    private CuentaCorriente ctaCrr;
    private int numTransaccion = 0;
    private DefaultTableModel modeloHistorial;

    public frmTransacciones() {
        initComponents();
        setSize(720, 520);
        setLocationRelativeTo(null);
        txtSaldo.setText("0.00");
        txtValor.setText("");
        habilitarTransacciones(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblTipoCuenta = new javax.swing.JLabel();
        cmbTipoCuenta = new javax.swing.JComboBox<>();
        btnIniciar = new javax.swing.JButton();
        lblValor = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        lblMovimiento = new javax.swing.JLabel();
        cmbMovimiento = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JButton();
        lblSaldo = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();

        setTitle("Transacciones Bancarias");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 14)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Transacciones Bancarias");
        getContentPane().add(lblTitulo);
        lblTitulo.setBounds(200, 10, 300, 20);

        lblTipoCuenta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTipoCuenta.setText("Tipo de cuenta:");
        getContentPane().add(lblTipoCuenta);
        lblTipoCuenta.setBounds(10, 50, 110, 25);

        cmbTipoCuenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cuenta corriente", "Cuenta de ahorros" }));
        getContentPane().add(cmbTipoCuenta);
        cmbTipoCuenta.setBounds(130, 50, 160, 25);

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniciar);
        btnIniciar.setBounds(300, 50, 90, 25);

        lblValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblValor.setText("Valor:");
        getContentPane().add(lblValor);
        lblValor.setBounds(10, 100, 110, 25);

        txtValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(txtValor);
        txtValor.setBounds(130, 100, 160, 25);

        lblMovimiento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMovimiento.setText("Movimiento:");
        getContentPane().add(lblMovimiento);
        lblMovimiento.setBounds(10, 135, 110, 25);

        cmbMovimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { CONSIGNACION, RETIRO }));
        getContentPane().add(cmbMovimiento);
        cmbMovimiento.setBounds(130, 135, 160, 25);

        btnRegistrar.setText("Registrar transacción");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnRegistrar);
        btnRegistrar.setBounds(130, 175, 160, 30);

        lblSaldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSaldo.setText("Balance:");
        getContentPane().add(lblSaldo);
        lblSaldo.setBounds(10, 220, 110, 25);

        txtSaldo.setEditable(false);
        getContentPane().add(txtSaldo);
        txtSaldo.setBounds(130, 220, 160, 25);

        modeloHistorial = new DefaultTableModel(
            new Object[][] {},
            new String[] { "N° transacción", "Tipo de transacción", "Valor", "Saldo", "Observación" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHistorial.setModel(modeloHistorial);
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(300);
        jScrollPane3.setViewportView(tblHistorial);
        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(20, 265, 670, 205);
    }// </editor-fold>

    /** Activa o desactiva los controles de transacción (solo tras elegir la cuenta). */
    private void habilitarTransacciones(boolean activo) {
        txtValor.setEnabled(activo);
        cmbMovimiento.setEnabled(activo);
        btnRegistrar.setEnabled(activo);
        cmbTipoCuenta.setEnabled(!activo);
        btnIniciar.setEnabled(!activo);
    }

    public void limpiar() {
        txtValor.setText("");
        txtValor.requestFocusInWindow();
    }

    public void consultar() {
        txtSaldo.setText(formato(ctaCrr.getbalance()));
    }

    private static String formato(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private void agregarFila(String tipo, double valor, String observacion) {
        numTransaccion++;
        modeloHistorial.addRow(new Object[] {
            numTransaccion, tipo, formato(valor), formato(ctaCrr.getbalance()), observacion });
        int ultima = modeloHistorial.getRowCount() - 1;
        tblHistorial.scrollRectToVisible(tblHistorial.getCellRect(ultima, 0, true));
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        if (cmbTipoCuenta.getSelectedIndex() == 1) {
            ctaCrr = new CuentaAhorros();
        } else {
            ctaCrr = new CuentaCorriente();
        }
        setTitle("Transacciones Bancarias - " + ctaCrr.getTipo());
        habilitarTransacciones(true);
        consultar();
        limpiar();
    }

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (ctaCrr == null) {
            return;
        }
        String tipo = (String) cmbMovimiento.getSelectedItem();
        String texto = txtValor.getText().trim().replace(',', '.');
        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.",
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
            limpiar();
            return;
        }
        try {
            double interes;
            if (RETIRO.equals(tipo)) {
                interes = ctaCrr.retirar(valor);
            } else {
                interes = ctaCrr.depositar(valor);
            }
            String obs = "Transacción exitosa";
            if (interes > 0) {
                obs += " - interés 1% abonado: " + formato(interes);
            }
            agregarFila(tipo, valor, obs);
        } catch (IllegalArgumentException ex) {
            // Transacción rechazada: el saldo no cambia y se deja constancia en el historial
            String obs = "Rechazada: " + ex.getMessage();
            if (valor > 0 && !Double.isNaN(valor) && !Double.isInfinite(valor)) {
                agregarFila(tipo, valor, obs);
            } else {
                agregarFila(tipo, 0, obs + " (valor ingresado: " + texto + ")");
            }
            JOptionPane.showMessageDialog(this, "La operación no fue posible.\n" + ex.getMessage(),
                    "Transacción rechazada", JOptionPane.WARNING_MESSAGE);
        }
        limpiar();
        consultar();
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTransacciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTransacciones().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cmbMovimiento;
    private javax.swing.JComboBox<String> cmbTipoCuenta;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblMovimiento;
    private javax.swing.JLabel lblSaldo;
    private javax.swing.JLabel lblTipoCuenta;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblValor;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtValor;
    // End of variables declaration
}
