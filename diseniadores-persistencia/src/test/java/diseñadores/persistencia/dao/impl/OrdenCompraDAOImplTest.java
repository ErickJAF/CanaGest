package diseñadores.persistencia.dao.impl;

import entidades.OrdenCompra;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrdenCompraDAOImplTest {

    private final OrdenCompraDAOImpl dao =
            new OrdenCompraDAOImpl();

    @Test
    public void guardar_ordenNull_debeLanzarException() {

        PersistenciaException ex = assertThrows(
                PersistenciaException.class,
                () -> dao.guardar(null)
        );

        assertEquals(
                "La orden de compra no puede ser nula.",
                ex.getMessage()
        );
    }

    @Test
    public void obtenerPorNumero_vacio_debeLanzarException() {

        PersistenciaException ex = assertThrows(
                PersistenciaException.class,
                () -> dao.obtenerPorNumero("")
        );

        assertTrue(
                ex.getMessage().contains("obligatorio")
        );
    }

    @Test
    public void eliminar_numeroNull_debeLanzarException() {

        PersistenciaException ex = assertThrows(
                PersistenciaException.class,
                () -> dao.eliminar(null)
        );

        assertNotNull(ex);
    }

    @Test
    public void obtenerTodas_noDebeRetornarNull()
            throws PersistenciaException {

        assertNotNull(
                dao.obtenerTodas()
        );
    }

    @Test
    public void aggregate_noDebeRetornarNull()
            throws PersistenciaException {

        assertNotNull(
                dao.obtenerConteoOrdenesPorProveedor()
        );
    }
}