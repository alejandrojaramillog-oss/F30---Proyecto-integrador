package model;

public class Comida extends Producto {
    private final Boolean esVegetariana;

    public Comida(String nombre, Double precio, Boolean esVegetariana)  {
        super(nombre, precio);
        this.esVegetariana = esVegetariana;
    }

    public  Boolean isEsVegetariana() {
        return esVegetariana;
    }

    @Override 
    public String getCategoria() {
        return "Comida";
    }
    
}
