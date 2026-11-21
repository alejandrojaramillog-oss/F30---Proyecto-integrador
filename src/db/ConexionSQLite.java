package db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/** Cada operacion obtiene su propia conexion y la cierra con try-with-resources. */
public final class ConexionSQLite {
    private final Path archivo;

    public ConexionSQLite(Path archivo) {
        this.archivo = Objects.requireNonNull(archivo, "El archivo de base de datos es obligatorio.")
                .toAbsolutePath().normalize();
    }

    public Connection abrir() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException error) {
            throw new SQLException("Falta el controlador SQLite JDBC. Ejecuta configurar-sqlite.sh "
                    + "y agrega lib/* al classpath.", error);
        }
        try {
            Files.createDirectories(archivo.getParent());
        } catch (IOException error) {
            throw new SQLException("No se pudo preparar la carpeta de la base de datos: "
                    + archivo.getParent(), error);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + archivo);
    }
}
