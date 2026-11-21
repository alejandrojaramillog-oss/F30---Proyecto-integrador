import model.ReglaDescuento;
import ui.VentanaCafeteria;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Expresión lambda (Unidad II): 15% de descuento si el monto total es mayor a $10
        ReglaDescuento reglaCafeteria = montoTotal -> (montoTotal > 10.0) ? montoTotal * 0.15 : 0.0;

        // Estado provisional mientras Santiago integra la conexión SQLite en src/db
        String estadoBD = "Pendiente de integrar conexión SQLite (módulo db).";

        SwingUtilities.invokeLater(() -> {
            VentanaCafeteria ventana = new VentanaCafeteria(reglaCafeteria, estadoBD);
            ventana.setVisible(true);
        });
    }
}