import db.ConexionSQLite;
import db.PedidoGuardado;
import db.RepositorioPedidos;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/** Integracion real con archivos SQLite temporales, sin frameworks de pruebas. */
public final class RepositorioPedidosTest {
    private static int casos;

    public static void main(String[] args) throws Exception {
        Path carpeta = Files.createTempDirectory("cafeteria-jdbc-");
        try {
            if (args.length > 0 && args[0].equals("--sin-driver")) {
                try {
                    repositorio(carpeta.resolve("sin-driver.db")).inicializar();
                    throw new AssertionError("Se acepto una conexion sin controlador.");
                } catch (SQLException esperado) {
                    comprobar(esperado.getMessage().contains("Falta el controlador SQLite JDBC"));
                    comprobar(!Files.exists(carpeta.resolve("sin-driver.db")));
                    System.out.println("OK: error de controlador ausente capturado como SQLException.");
                }
                return;
            }
            Path archivo = carpeta.resolve("datos/cafeteria.db");
            RepositorioPedidos repo = repositorio(archivo);
            repo.inicializar();
            repo.inicializar();
            comprobar(repo.listar().isEmpty());
            casos++;

            long id = repo.guardar(20, 3, 17);
            comprobar(id > 0);
            casos++;

            // Otra instancia vuelve a abrir el archivo: los datos sobreviven a la conexion.
            List<PedidoGuardado> guardados = repositorio(archivo).listar();
            comprobar(guardados.size() == 1);
            PedidoGuardado primero = guardados.get(0);
            comprobar(primero.getId() == id && primero.getMonto() == 20
                    && primero.getDescuento() == 3 && primero.getTotalFinal() == 17);
            Instant.parse(primero.getFecha());
            casos++;

            long segundo = repo.guardar(0, 0, 0);
            long tercero = repo.guardar(10, 10, 0);
            comprobar(segundo > id && tercero > segundo);
            repo.inicializar();
            comprobar(repo.listar().size() == 3);
            casos++;

            double[][] invalidos = {{-1, 0, 0}, {Double.NaN, 0, 0},
                    {Double.POSITIVE_INFINITY, 0, 0}, {20, -1, 21}, {20, 21, 0},
                    {20, 3, -1}, {20, 3, 20}, {20, Double.NaN, 17},
                    {20, 3, Double.POSITIVE_INFINITY}};
            for (double[] datos : invalidos) {
                try {
                    repo.guardar(datos[0], datos[1], datos[2]);
                    throw new AssertionError("Se guardaron importes invalidos.");
                } catch (IllegalArgumentException esperado) {
                    comprobar(repo.listar().size() == 3);
                    casos++;
                }
            }

            Path directorio = Files.createDirectory(carpeta.resolve("no-es-archivo"));
            rechazarConexion(repositorio(directorio));
            Path padreArchivo = carpeta.resolve("no-es-carpeta");
            Files.writeString(padreArchivo, "archivo");
            rechazarConexion(repositorio(padreArchivo.resolve("cafeteria.db")));
            Path corrupto = carpeta.resolve("corrupto.db");
            Files.writeString(corrupto, "Esto no es una base de datos SQLite.");
            rechazarConexion(repositorio(corrupto));

            // Un fallo en otra conexion no deja bloqueada la base valida.
            repo.guardar(8, 0, 8);
            comprobar(repositorio(archivo).listar().size() == 4);
            casos++;
            System.out.println("OK: " + casos + " casos de persistencia SQLite real y errores JDBC.");
        } finally {
            try (Stream<Path> rutas = Files.walk(carpeta)) {
                for (Path ruta : (Iterable<Path>) rutas.sorted(Comparator.reverseOrder())::iterator) {
                    Files.delete(ruta);
                }
            }
        }
    }

    private static RepositorioPedidos repositorio(Path archivo) {
        return new RepositorioPedidos(new ConexionSQLite(archivo));
    }

    private static void rechazarConexion(RepositorioPedidos repo) throws Exception {
        try {
            repo.inicializar();
            throw new AssertionError("No se propago el error JDBC.");
        } catch (SQLException esperado) {
            casos++;
        }
    }

    private static void comprobar(boolean condicion) {
        if (!condicion) {
            throw new AssertionError("La verificacion de persistencia fallo.");
        }
    }
}
