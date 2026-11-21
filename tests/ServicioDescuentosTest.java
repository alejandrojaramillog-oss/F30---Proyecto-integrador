import java.math.BigDecimal;
import service.ResultadoDescuento;
import service.ServicioDescuentos;

/** Pruebas ejecutables con Java estandar, sin JUnit ni otros frameworks. */
public final class ServicioDescuentosTest {
    private static int casos;

    public static void main(String[] args) {
        ServicioDescuentos servicio = new ServicioDescuentos();
        verificar(servicio, 0, "0.00", "0.00", "0.00");
        verificar(servicio, 8, "8.00", "0.00", "8.00");
        verificar(servicio, 10, "10.00", "0.00", "10.00");
        verificar(servicio, 10.01, "10.01", "1.50", "8.51");
        verificar(servicio, 20, "20.00", "3.00", "17.00");
        verificar(servicio, 10.70, "10.70", "1.61", "9.09");
        verificar(servicio, 10.004, "10.00", "0.00", "10.00");
        verificar(servicio, 10.005, "10.01", "1.50", "8.51");
        // Se prueba abierto/cerrado con reglas distintas sin alterar el servicio.
        verificar(new ServicioDescuentos(total -> 2.0), 20, "20.00", "2.00", "18.00");
        verificar(new ServicioDescuentos(total -> total), 20, "20.00", "20.00", "0.00");
        rechazar(() -> servicio.calcular(-1));
        rechazar(() -> servicio.calcular(Double.NaN));
        rechazar(() -> servicio.calcular(Double.POSITIVE_INFINITY));
        rechazar(() -> servicio.calcular(Double.NEGATIVE_INFINITY));
        rechazar(() -> new ServicioDescuentos(total -> -1).calcular(20));
        rechazar(() -> new ServicioDescuentos(total -> 21).calcular(20));
        rechazar(() -> new ServicioDescuentos(total -> Double.NaN).calcular(20));
        rechazar(() -> new ServicioDescuentos(total -> Double.POSITIVE_INFINITY).calcular(20));
        try {
            new ServicioDescuentos(null);
            throw new AssertionError("Se acepto una regla nula.");
        } catch (NullPointerException esperado) {
            casos++;
        }
        System.out.println("OK: " + casos + " casos de logica y validacion.");
    }

    private static void verificar(ServicioDescuentos servicio, double monto,
            String original, String descuento, String totalFinal) {
        ResultadoDescuento resultado = servicio.calcular(monto);
        igual(original, resultado.getTotalOriginal());
        igual(descuento, resultado.getDescuento());
        igual(totalFinal, resultado.getTotalFinal());
        casos++;
    }

    private static void igual(String esperado, BigDecimal actual) {
        if (!new BigDecimal(esperado).equals(actual)) {
            throw new AssertionError("Esperado: " + esperado + "; obtenido: " + actual);
        }
    }

    private static void rechazar(Runnable accion) {
        try {
            accion.run();
            throw new AssertionError("Se acepto un monto o una regla invalida.");
        } catch (IllegalArgumentException esperado) {
            casos++;
        }
    }
}
