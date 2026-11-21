package db;

/** Datos de un calculo persistido; no implementa reglas de descuento. */
public final class PedidoGuardado {
    private final long id;
    private final double monto;
    private final double descuento;
    private final double totalFinal;
    private final String fecha;

    PedidoGuardado(long id, double monto, double descuento, double totalFinal, String fecha) {
        this.id = id;
        this.monto = monto;
        this.descuento = descuento;
        this.totalFinal = totalFinal;
        this.fecha = fecha;
    }

    public long getId() { return id; }
    public double getMonto() { return monto; }
    public double getDescuento() { return descuento; }
    public double getTotalFinal() { return totalFinal; }
    public String getFecha() { return fecha; }
}
