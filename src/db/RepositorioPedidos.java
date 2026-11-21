package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Guarda y recupera resultados calculados por el modelo existente. */
public final class RepositorioPedidos {
    private final ConexionSQLite conexion;

    public RepositorioPedidos(ConexionSQLite conexion) {
        this.conexion = Objects.requireNonNull(conexion);
    }

    public void inicializar() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS pedidos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "monto REAL NOT NULL CHECK(monto >= 0), "
                + "descuento REAL NOT NULL CHECK(descuento >= 0 AND descuento <= monto), "
                + "total_final REAL NOT NULL CHECK(total_final >= 0 AND total_final <= monto), "
                + "fecha TEXT NOT NULL)";
        try (Connection db = conexion.abrir(); Statement consulta = db.createStatement()) {
            consulta.executeUpdate(sql);
        }
    }

    public long guardar(double monto, double descuento, double totalFinal) throws SQLException {
        if (!Double.isFinite(monto) || !Double.isFinite(descuento) || !Double.isFinite(totalFinal)
                || monto < 0 || descuento < 0 || descuento > monto || totalFinal < 0
                || totalFinal > monto || Math.abs(monto - descuento - totalFinal) > 0.000001) {
            throw new IllegalArgumentException("Los importes del pedido deben ser finitos, "
                    + "no negativos y corresponder al calculo del descuento.");
        }
        String sql = "INSERT INTO pedidos(monto, descuento, total_final, fecha) VALUES (?, ?, ?, ?)";
        try (Connection db = conexion.abrir(); PreparedStatement consulta = db.prepareStatement(sql)) {
            consulta.setDouble(1, monto);
            consulta.setDouble(2, descuento);
            consulta.setDouble(3, totalFinal);
            consulta.setString(4, Instant.now().toString());
            consulta.executeUpdate();
            try (Statement identidad = db.createStatement();
                    ResultSet resultado = identidad.executeQuery("SELECT last_insert_rowid()")) {
                if (!resultado.next()) {
                    throw new SQLException("No se pudo obtener el identificador del pedido guardado.");
                }
                return resultado.getLong(1);
            }
        }
    }

    public List<PedidoGuardado> listar() throws SQLException {
        List<PedidoGuardado> pedidos = new ArrayList<>();
        String sql = "SELECT id, monto, descuento, total_final, fecha FROM pedidos ORDER BY id";
        try (Connection db = conexion.abrir(); PreparedStatement consulta = db.prepareStatement(sql);
                ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                pedidos.add(new PedidoGuardado(resultado.getLong("id"), resultado.getDouble("monto"),
                        resultado.getDouble("descuento"), resultado.getDouble("total_final"),
                        resultado.getString("fecha")));
            }
        }
        return List.copyOf(pedidos);
    }
}
