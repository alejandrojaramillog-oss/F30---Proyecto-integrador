package ui;

import model.Comida;
import model.Producto;
import model.ReglaDescuento;

import javax.swing.*;
import java.awt.*;

public class VentanaCafeteria extends JFrame {

    private final JTextField txtMonto;
    private final JButton btnCalcular;
    private final JTextArea txtAreaResultado;
    private final ReglaDescuento reglaDescuento;
    private final String estadoConexionInicial;

    public VentanaCafeteria(ReglaDescuento reglaDescuento, String estadoConexionInicial) {
        this.reglaDescuento = reglaDescuento;
        this.estadoConexionInicial = estadoConexionInicial;

        setTitle("Cafetería - Gestión de Descuentos");
        setSize(500, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título superior
        JLabel lblTitulo = new JLabel("Calculadora de Descuentos - Cafetería", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de entrada con JTextField y JButton
        JPanel panelEntrada = new JPanel(new GridLayout(2, 2, 10, 10));
        panelEntrada.add(new JLabel("Monto total del pedido ($):"));

        txtMonto = new JTextField();
        panelEntrada.add(txtMonto);

        panelEntrada.add(new JLabel(""));
        btnCalcular = new JButton("Calcular Descuento");
        btnCalcular.setBackground(new Color(41, 128, 185));
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFocusPainted(false);
        panelEntrada.add(btnCalcular);

        panelPrincipal.add(panelEntrada, BorderLayout.CENTER);

        // JTextArea para mensajes de conexión y resultado final
        txtAreaResultado = new JTextArea(6, 35);
        txtAreaResultado.setEditable(false);
        txtAreaResultado.setLineWrap(true);
        txtAreaResultado.setWrapStyleWord(true);
        txtAreaResultado.setText("Estado de Base de Datos:\n" + estadoConexionInicial);

        JScrollPane scrollResultado = new JScrollPane(txtAreaResultado);
        panelPrincipal.add(scrollResultado, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Evento ActionListener del botón
        btnCalcular.addActionListener(e -> procesarCalculo());
    }

    private void procesarCalculo() {
        String textoIngresado = txtMonto.getText().trim();

        if (textoIngresado.isEmpty()) {
            mostrarMensaje("Error: Ingresa un monto total antes de calcular.");
            return;
        }

        try {
            double monto = Double.parseDouble(textoIngresado);

            // Uso de la jerarquía Producto/Comida creada en model
            Producto pedido = new Comida("Pedido Cafetería", monto, false);
            double descuentoAplicado = (reglaDescuento != null) ? reglaDescuento.calcularDescuento(monto) : 0.0;
            double totalFinal = pedido.calcularPrecioConDescuento(reglaDescuento);

            String resumen = String.format(
                    "Monto original: $%.2f%nDescuento aplicado: $%.2f%nTotal final a pagar: $%.2f",
                    monto, descuentoAplicado, totalFinal
            );
            mostrarMensaje(resumen);

        } catch (NumberFormatException ex) {
            mostrarMensaje("Error: Ingresa un valor numérico válido (ejemplo: 15.50).");
        } catch (IllegalArgumentException ex) {
            mostrarMensaje("Error de validación: " + ex.getMessage());
        }
    }

    private void mostrarMensaje(String detalle) {
        txtAreaResultado.setText(
                "Estado de Base de Datos:\n" + estadoConexionInicial +
                "\n----------------------------------------\n" + detalle
        );
    }
}