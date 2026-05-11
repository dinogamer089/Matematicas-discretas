package concesionario;

import java.io.*;
import java.util.ArrayList;

public class GestorArchivos {

    public static <T> void guardarDatos(ArrayList<T> lista, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public static <T> ArrayList<T> cargarDatos(String nombreArchivo) {
        File file = new File(nombreArchivo);
        System.out.println("Cargando archivo: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("Archivo no encontrado: " + nombreArchivo);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return (ArrayList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos de " + nombreArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
