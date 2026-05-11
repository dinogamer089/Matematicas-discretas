import java.io.Serializable;

import concesionario.*;

public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorVentas = 1;

    private int idVenta;
    private Vendedor vendedor;
    private String nombreCliente;
    private String direccionCliente;
    private String telefonoCliente;
    private Automovil automovil;
    private String tipoVenta; // "Contado" o "Financiado"
    private double precioFinal;
    private int mensualidades;

    public Venta(Vendedor vendedor, String nombreCliente, String direccionCliente, String telefonoCliente,
                 Automovil automovil, String tipoVenta, double precioFinal, int mensualidades) {
        this.idVenta = contadorVentas++;
        this.vendedor = vendedor;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.automovil = automovil;
        this.tipoVenta = tipoVenta;
        this.precioFinal = precioFinal;
        this.mensualidades = mensualidades;
    }

    public int getIdVenta() { return idVenta; }
    public Vendedor getVendedor() { return vendedor; }
    public String getNombreCliente() { return nombreCliente; }
    public Automovil getAutomovil() { return automovil; }
    public String getTipoVenta() { return tipoVenta; }
    public double getPrecioFinal() { return precioFinal; }
    public int getMensualidades() { return mensualidades; }

    @Override
    public String toString() {
        return "Venta #" + idVenta + ": " + vendedor.getNombre() + " vendió a " + nombreCliente +
                " un " + automovil + " [" + tipoVenta + "]";
    }
}
