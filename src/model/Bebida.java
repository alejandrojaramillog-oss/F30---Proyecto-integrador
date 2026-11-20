package model;

public class Bebida extends Producto {
    private final Boolean esFria;

    public Bebida(String nombre, Double precio, Boolean esFria) {
        super(nombre, precio);
        this.esFria = esFria;
    }

    public boolean isEsFria() {
        return esFria;
    }

    @Override 
    public String getCategoria() {
        return "Bebida";
    }
}
