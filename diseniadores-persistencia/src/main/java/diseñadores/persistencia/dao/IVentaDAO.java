package diseñadores.persistencia.dao;

import entidades.Venta;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de Notas de Venta en el sistema utilizando entidades del dominio limpio.
 * * @author ERICK
 */
public interface IVentaDAO {

    /**
     * Recupera el histórico completo de ventas almacenadas en la base de datos.
     * * @return Lista con todas las ventas encontradas en el dominio.
     * @throws PersistenciaException Si ocurre un error durante la consulta en MongoDB.
     */
    List<Venta> obtenerTodas() throws PersistenciaException;

    /**
     * Busca una nota de venta específica mediante su código único comercial.
     * * @param codigoVenta El código identificador único de la venta.
     * @return El objeto de dominio de la venta encontrada, o null si no existe.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    Venta obtenerPorCodigoVenta(String codigoVenta) throws PersistenciaException;

    /**
     * Registra de forma permanente una nueva transacción de venta en el sistema.
     * * @param venta El objeto de dominio con los datos de la transacción a guardar.
     * @throws PersistenciaException Si el código ya existe o falla el motor de MongoDB.
     */
    void guardar(Venta venta) throws PersistenciaException;

    /**
     * Reemplaza o actualiza el registro de una nota de venta existente.
     * * @param venta El objeto de dominio de la venta con los cambios aplicados.
     * @throws PersistenciaException Si la venta no existe o la actualización falla.
     */
    void actualizar(Venta venta) throws PersistenciaException;

    /**
     * Elimina una nota de venta del sistema usando su código único identificador.
     * * @param codigoVenta El código de la venta que se desea remover.
     * @throws PersistenciaException Si la venta no existe o falla la base de datos.
     */
    void eliminar(String codigoVenta) throws PersistenciaException;
}