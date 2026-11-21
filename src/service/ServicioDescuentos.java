package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import model.ReglaDescuento;

/** Logica de pedidos independiente de Swing y de la persistencia. */
public final class ServicioDescuentos {
    // Se conserva la firma Double del modelo existente. BigDecimal evita
    // errores de punto flotante en importes como 10.70 * 0.15 = 1.605.
    public static final ReglaDescuento DESCUENTO_POR_MONTO = monto ->
            monto > 10.0
                    ? BigDecimal.valueOf(monto).multiply(new BigDecimal("0.15")).doubleValue()
                    : 0.0;

    private final ReglaDescuento regla;

    public ServicioDescuentos() {
        this(DESCUENTO_POR_MONTO);
    }

    /** Permite agregar otra regla mediante una lambda sin modificar el servicio. */
    public ServicioDescuentos(ReglaDescuento regla) {
        this.regla = Objects.requireNonNull(regla, "La regla de descuento es obligatoria.");
    }

    /** Normaliza el total a centavos antes de evaluar el umbral del descuento. */
    public ResultadoDescuento calcular(double montoTotal) {
        if (!Double.isFinite(montoTotal) || montoTotal < 0.0) {
            throw new IllegalArgumentException("El monto debe ser un numero finito mayor o igual a cero.");
        }
        BigDecimal total = BigDecimal.valueOf(montoTotal).setScale(2, RoundingMode.HALF_UP);
        double descuentoCalculado = regla.calcularDescuento(total.doubleValue());
        if (!Double.isFinite(descuentoCalculado) || descuentoCalculado < 0.0
                || BigDecimal.valueOf(descuentoCalculado).compareTo(total) > 0) {
            throw new IllegalArgumentException("La regla debe devolver un descuento entre cero y el total.");
        }
        BigDecimal descuento = BigDecimal.valueOf(descuentoCalculado)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoDescuento(total, descuento);
    }
}
