package model;

public abstract class Producto {
    private String nombre;
    private Double precio;

    public Producto(String nombre, Double precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public abstract String getCategoria();

    public double calcularPrecioConDescuento(ReglaDescuento regla) {
        Double descuento = (regla != null) ? regla.calcularDescuento(precio) : 0.0;
        Double precioFinal = precio - descuento;
        return Math.max(0.0, precioFinal);
    }
};
