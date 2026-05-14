import concesionario.Automovil;
import concesionario.Vendedor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConcesionariaTest {
    private Concesionaria concesionaria;

    @BeforeEach
    public void setUp() {
        concesionaria = new Concesionaria();
        concesionaria.getAutomoviles().add(new Automovil("VIN123", "Toyota", "Corolla", 20000.0, "En concesionaria"));
        concesionaria.getVendedores().add(new Vendedor("V1", "Ana", "555-1234"));
    }

    @Test
    public void testCU001_BuscarAutoPorVIN_Exitoso() {
        Automovil auto = concesionaria.buscarAutomovilPorVIN("VIN123");
        assertNotNull(auto);
        assertEquals("Toyota", auto.getMarca());
    }

    @Test
    public void testCU002_BuscarAutoPorVIN_Fallido() {
        Automovil auto = concesionaria.buscarAutomovilPorVIN("VIN999");
        assertNull(auto, "El método debe retornar null cuando el VIN no está registrado");
    }

    @Test
    public void testCU003_RegistrarVenta_Exitoso() {
        boolean resultado = concesionaria.registrarVenta("V1", "Juan Perez", "Calle 1", "555", "VIN123", "Contado", 20000.0, 0);
        assertTrue(resultado);
        Automovil auto = concesionaria.buscarAutomovilPorVIN("VIN123");
        assertEquals("Vendido", auto.getEstado());
    }

    @Test
    public void testCU004_RegistrarVenta_Fallido_VendedorNoExiste() {
        int cantidadVentasInicial = concesionaria.getVentas() != null ? concesionaria.getVentas().size() : 0;
        
        boolean resultado = concesionaria.registrarVenta("V99", "Juan Perez", "Calle 1", "555", "VIN123", "Contado", 20000.0, 0);
        
        assertFalse(resultado, "El método debe retornar false si el vendedor no existe");
        
        if (concesionaria.getVentas() != null) {
            assertEquals(cantidadVentasInicial, concesionaria.getVentas().size(), "La lista de ventas no debe haberse modificado");
        }
    }

    @Test
    public void testCU005_BusquedaBinariaPorModelo() {
        int indice = concesionaria.buscarAutoBinarioPorModeloIndex("Corolla");
        
        assertTrue(indice >= 0, "El método debe retornar un índice >= 0 si encuentra el modelo");
        
        assertEquals("Corolla", concesionaria.getAutomoviles().get(indice).getModelo(), "El auto encontrado debe ser un Corolla");
    }
}