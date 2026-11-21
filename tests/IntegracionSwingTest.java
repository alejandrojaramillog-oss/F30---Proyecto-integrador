import db.ConexionSQLite;
import db.RepositorioPedidos;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import ui.VentanaCafeteria;

/** Comprueba el guardado y los mensajes del boton sin mostrar ventanas. */
public final class IntegracionSwingTest {
    public static void main(String[] args) throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("OMITIDO: Swing requiere un entorno con pantalla.");
            return;
        }
        Path carpeta = Files.createTempDirectory("cafeteria-swing-");
        try {
            RepositorioPedidos repo = new RepositorioPedidos(
                    new ConexionSQLite(carpeta.resolve("cafeteria.db")));
            repo.inicializar();
            // Una conexion invalida para verificar el mensaje real al guardar.
            RepositorioPedidos roto = new RepositorioPedidos(new ConexionSQLite(carpeta));
            SwingUtilities.invokeAndWait(() -> {
                VentanaCafeteria ventana = new VentanaCafeteria(
                        monto -> monto > 10 ? monto * 0.15 : 0, "Conexion SQLite establecida.", repo);
                try {
                    JTextField entrada = buscar(ventana, JTextField.class);
                    JButton boton = buscar(ventana, JButton.class);
                    JTextArea salida = buscar(ventana, JTextArea.class);
                    entrada.setText("20");
                    boton.doClick();
                    comprobar(salida.getText().contains("Pedido guardado en SQLite"));
                    entrada.setText("texto");
                    boton.doClick();
                    comprobar(salida.getText().contains("Error:"));
                    entrada.setText("NaN");
                    boton.doClick();
                    comprobar(salida.getText().contains("Error de validación:"));
                } finally {
                    ventana.dispose();
                }
                VentanaCafeteria fallida = new VentanaCafeteria(
                        monto -> monto > 10 ? monto * 0.15 : 0, "No se pudo iniciar la base de datos.", roto);
                try {
                    buscar(fallida, JTextField.class).setText("20");
                    buscar(fallida, JButton.class).doClick();
                    String texto = buscar(fallida, JTextArea.class).getText();
                    comprobar(texto.contains("No se pudo guardar el pedido:"));
                    comprobar(!texto.contains("Pedido guardado en SQLite"));
                } finally {
                    fallida.dispose();
                }
            });
            comprobar(repo.listar().size() == 1);
            System.out.println("OK: Swing guarda el pedido y muestra errores de entrada y de persistencia.");
        } finally {
            try (Stream<Path> rutas = Files.walk(carpeta)) {
                for (Path ruta : (Iterable<Path>) rutas.sorted(Comparator.reverseOrder())::iterator) {
                    Files.delete(ruta);
                }
            }
        }
    }

    private static <T extends Component> T buscar(Container contenedor, Class<T> tipo) {
        for (Component componente : contenedor.getComponents()) {
            if (tipo.isInstance(componente)) {
                return tipo.cast(componente);
            }
            if (componente instanceof Container) {
                T encontrado = buscar((Container) componente, tipo);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }

    private static void comprobar(boolean condicion) {
        if (!condicion) {
            throw new AssertionError("Fallo en la integracion Swing y SQLite.");
        }
    }
}
