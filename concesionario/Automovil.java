package concesionario;
import java.io.Serializable;

public class Automovil implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vin;
    private String marca;
    private String modelo;
    private double precio;
    private String estado; // "En concesionaria", "Siendo financiado", "Vendido"

    public Automovil(String vin, String marca, String modelo, double precio, String estado) {
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.estado = estado;
    }

    public String getVin() { return vin; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getPrecio() { return precio; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return vin + " - " + marca + " " + modelo + " - $" + precio + " - " + estado;
    }
}
