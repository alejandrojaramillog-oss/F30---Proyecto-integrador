package service;

import java.math.BigDecimal;

/** Resultado inmutable que puede consultar la futura interfaz Swing. */
public final class ResultadoDescuento {
    private final BigDecimal totalOriginal;
    private final BigDecimal descuento;
    private final BigDecimal totalFinal;

    ResultadoDescuento(BigDecimal totalOriginal, BigDecimal descuento) {
        this.totalOriginal = totalOriginal;
        this.descuento = descuento;
        this.totalFinal = totalOriginal.subtract(descuento);
    }

    public BigDecimal getTotalOriginal() {
        return totalOriginal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }
}
