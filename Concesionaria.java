import java.util.*;

import concesionario.*;

public class Concesionaria {
    private ArrayList<Vendedor> vendedores;
    private ArrayList<Automovil> automoviles;
    private ArrayList<Venta> ventas;

    public Concesionaria() {
        vendedores = GestorArchivos.cargarDatos("vendedores.dat");
        automoviles = GestorArchivos.cargarDatos("automoviles.dat");
        ventas = GestorArchivos.cargarDatos("ventas.dat");
    }

    public boolean registrarVenta(String idVendedor, String nombreCliente, String direccionCliente, String telefonoCliente,
                              String vinAutomovil, String tipoVenta, double precioFinal, int mensualidades) {
        Vendedor vendedor = buscarVendedor(idVendedor);
        Automovil auto = buscarAutomovilPorVIN(vinAutomovil);

        if (vendedor == null || auto == null) {
            System.out.println("Error: vendedor o automóvil no encontrado.");
            return false;
        }

        String estado = tipoVenta.equals("Contado") ? "Vendido" : "Siendo financiado";
        auto.setEstado(estado);

        Venta venta = new Venta(vendedor, nombreCliente, direccionCliente, telefonoCliente, auto, tipoVenta, precioFinal, mensualidades);
        ventas.add(venta);
        System.out.println("Venta registrada:\n" + venta);
        return true;
    }

    private Vendedor buscarVendedor(String id) {
        for (Vendedor v : vendedores) {
            if (v.getId().equals(id)) return v;
        }
        return null;
    }

    public Automovil buscarAutomovilPorVIN(String vin) {
        return buscarAutoRecursivo(automoviles, vin, 0);
    }

    private Automovil buscarAutoRecursivo(ArrayList<Automovil> lista, String vin, int index) {
        if (index >= lista.size()) return null;
        if (lista.get(index).getVin().equalsIgnoreCase(vin)) return lista.get(index);
        return buscarAutoRecursivo(lista, vin, index + 1);
    }

    public void mostrarAutosPorEstado(String estado) {
        for (Automovil a : automoviles) {
            if (a.getEstado().equalsIgnoreCase(estado)) {
                System.out.println(a);
            }
        }
    }

    public void mostrarVentasPorVendedor(String idVendedor) {
        for (Venta v : ventas) {
            if (v.getVendedor().getId().equals(idVendedor)) {
                System.out.println(v);
            }
        }
    }

    public void buscarAutoBinarioPorModelo(String modelo) {
        automoviles.sort(Comparator.comparing(Automovil::getModelo));
        int index = busquedaBinaria(automoviles, modelo, 0, automoviles.size() - 1);
        if (index >= 0) {
            System.out.println("Auto encontrado: " + automoviles.get(index));
        } else {
            System.out.println("Auto no encontrado.");
        }
    }

    private int busquedaBinaria(ArrayList<Automovil> lista, String modelo, int izquierda, int derecha) {
        if (izquierda > derecha) return -1;
        int medio = (izquierda + derecha) / 2;
        int comparacion = lista.get(medio).getModelo().compareToIgnoreCase(modelo);
        if (comparacion == 0) return medio;
        else if (comparacion < 0) return busquedaBinaria(lista, modelo, medio + 1, derecha);
        else return busquedaBinaria(lista, modelo, izquierda, medio - 1);
    }

    public void guardarDatos() {
        GestorArchivos.guardarDatos(vendedores, "vendedores.dat");
        GestorArchivos.guardarDatos(automoviles, "automoviles.dat");
        GestorArchivos.guardarDatos(ventas, "ventas.dat");
    }

    public ArrayList<Automovil> getAutomoviles() {
        return automoviles;
    }

    public ArrayList<Venta> getVentas() {
        return ventas;
    }

    public int buscarAutoBinarioPorModeloIndex(String modelo) {
        automoviles.sort(Comparator.comparing(Automovil::getModelo));
        return busquedaBinaria(automoviles, modelo, 0, automoviles.size() - 1);
    }

    public String consultaGeneralAutos() {
        StringBuilder sb = new StringBuilder();
        if (automoviles.isEmpty()) {
            sb.append("No hay automóviles en el inventario.");
        } else {
            for (Automovil auto : automoviles) {
                sb.append(auto.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    public ArrayList<Vendedor> getVendedores() {
        return vendedores;
    }
}
