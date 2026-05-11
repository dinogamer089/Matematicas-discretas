package concesionario;

import java.util.ArrayList;

public class DatosIniciales {

    public static void cargarDatos() {
        ArrayList<Automovil> autos = new ArrayList<>();
        ArrayList<Vendedor> vendedores = new ArrayList<>();

        // Automóviles
        autos.add(new Automovil("KGG001", "Koenigsegg", "Jesko", 3000000, "En concesionaria"));
        autos.add(new Automovil("KGG002", "Koenigsegg", "Regera", 2700000, "En concesionaria"));
        autos.add(new Automovil("POR001", "Porsche", "911 Turbo S", 210000, "En concesionaria"));
        autos.add(new Automovil("POR002", "Porsche", "Cayenne GTS", 140000, "En concesionaria"));
        autos.add(new Automovil("LAM001", "Lamborghini", "Huracán EVO", 320000, "En concesionaria"));
        autos.add(new Automovil("LAM002", "Lamborghini", "Aventador SVJ", 500000, "En concesionaria"));
        autos.add(new Automovil("CAD001", "Cadillac", "Escalade V", 160000, "En concesionaria"));
        autos.add(new Automovil("CAD002", "Cadillac", "CT5-V Blackwing", 95000, "En concesionaria"));
        autos.add(new Automovil("MBZ001", "Mercedes-Benz", "AMG GT R", 180000, "En concesionaria"));
        autos.add(new Automovil("MBZ002", "Mercedes-Benz", "S 580 Maybach", 250000, "En concesionaria"));

        // Vendedores
        vendedores.add(new Vendedor("V001", "Carlos Méndez", "6861234567"));
        vendedores.add(new Vendedor("V002", "Ana Torres", "6649876543"));
        vendedores.add(new Vendedor("V003", "Luis Ramírez", "6463456789"));

        // Guardar usando GestorArchivos
        GestorArchivos.guardarDatos(autos, "automoviles.dat");
        GestorArchivos.guardarDatos(vendedores, "vendedores.dat");

        System.out.println("Datos iniciales guardados en archivos exitosamente.");
    }

    public static void main(String[] args) {
        cargarDatos();
    }
}