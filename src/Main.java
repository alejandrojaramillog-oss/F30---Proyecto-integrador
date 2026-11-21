import model.ReglaDescuento;
import ui.VentanaCafeteria;
import db.ConexionSQLite;
import db.RepositorioPedidos;

import java.nio.file.Paths;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Expresión lambda (Unidad II): 15% de descuento si el monto total es mayor a $10
        ReglaDescuento reglaCafeteria = montoTotal -> (montoTotal > 10.0) ? montoTotal * 0.15 : 0.0;

        RepositorioPedidos repositorio = new RepositorioPedidos(new ConexionSQLite(
                Paths.get(System.getProperty("cafeteria.db", "datos/cafeteria.db"))));
        String estadoBD = iniciarBaseDatos(repositorio);

        SwingUtilities.invokeLater(() -> {
            VentanaCafeteria ventana = new VentanaCafeteria(reglaCafeteria, estadoBD, repositorio);
            ventana.setVisible(true);
        });
    }

    private static String iniciarBaseDatos(RepositorioPedidos repositorio) {
        try {
            repositorio.inicializar();
            return "Conexion SQLite establecida. Los calculos se guardaran en la base de datos.";
        } catch (SQLException error) {
            return "No se pudo iniciar la base de datos. " + error.getMessage();
        }
    }
}
