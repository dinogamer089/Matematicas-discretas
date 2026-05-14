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
        // Nota: Idealmente deberías inyectar datos de prueba aquí si los .dat están vacíos
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
    public void testCU003_RegistrarVenta_Exitoso() {
        boolean resultado = concesionaria.registrarVenta("V1", "Juan Perez", "Calle 1", "555", "VIN123", "Contado", 20000.0, 0);
        assertTrue(resultado);
        Automovil auto = concesionaria.buscarAutomovilPorVIN("VIN123");
        assertEquals("Vendido", auto.getEstado());
    }
}