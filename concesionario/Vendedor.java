package concesionario;

import java.io.Serializable;

public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String telefono;

    public Vendedor(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + telefono + ")";
    }
}