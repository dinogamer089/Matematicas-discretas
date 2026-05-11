package concesionario;
import java.io.*;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre, direccion, telefono;

    public Cliente(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() {
        return nombre + " (" + telefono + ")";
    }
}