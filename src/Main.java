

import java.util.Locale;
import service.ResultadoDescuento;
import service.ServicioDescuentos;

/** Demostracion por consola de la Unidad II; Swing queda para la Unidad III. */
public class Main {
    public static void main(String[] args) {
        ServicioDescuentos servicio = new ServicioDescuentos();
        String[] montos = args.length == 0 ? new String[] {"8", "10", "20", "10.70"} : args;
        boolean huboErrores = false;
        System.out.println("Cafeteria - demostracion de descuentos (Unidad II)");
        for (String monto : montos) {
            try {
                ResultadoDescuento resultado = servicio.calcular(Double.parseDouble(monto));
                System.out.printf(Locale.ROOT,
                        "Total: $%.2f | Descuento: $%.2f | Total final: $%.2f%n",
                        resultado.getTotalOriginal(), resultado.getDescuento(), resultado.getTotalFinal());
            } catch (NumberFormatException error) {
                System.err.println("Monto '" + monto + "': escribe un numero usando punto decimal, por ejemplo 10.70.");
                huboErrores = true;
            } catch (IllegalArgumentException error) {
                System.err.println("Monto '" + monto + "': " + error.getMessage());
                huboErrores = true;
            }
        }
        if (huboErrores) {
            System.exit(1);
        }
    }
}
