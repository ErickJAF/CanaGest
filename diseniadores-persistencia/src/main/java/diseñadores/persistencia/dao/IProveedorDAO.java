package diseñadores.persistencia.dao;

import entidades.Proveedor;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de Proveedores en el sistema. Dado que el proveedor no puede existir sin un producto,
 * las operaciones impactan directamente la colección de Productos.
 * * @author ERICK
 */
public interface IProveedorDAO {

    /**
     * Recupera todos los proveedores únicos que tienen al menos un producto asociado.
     * * @return Lista de todos los proveedores únicos en dominio.
     * @throws PersistenciaException Si ocurre un error en MongoDB.
     */
    List<Proveedor> obtenerTodos() throws PersistenciaException;

    /**
     * Busca un proveedor específico por su código único de identificación comercial.
     * * @param codigo El código único identificador del proveedor.
     * @return El objeto de dominio del proveedor, o null si no se encuentra.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    Proveedor obtenerPorCodigo(String codigo) throws PersistenciaException;

    /**
     * Nota: En este diseño, un proveedor no se guarda de forma aislada. 
     * Debe asociarse directamente a un producto a través del DAO de Productos.
     * Este método queda documentado para lanzar una excepción si se intenta usar solo.
     */
    void guardar(Proveedor proveedor) throws PersistenciaException;

    /**
     * Actualiza los datos de un proveedor en cascada en todos los productos 
     * que lo tengan asignado en el sistema.
     * * @param proveedor El objeto de dominio con la información actualizada.
     * @throws PersistenciaException Si el proveedor no existe o falla MongoDB.
     */
    void actualizar(Proveedor proveedor) throws PersistenciaException;

    /**
     * Nota: Eliminar un proveedor de forma aislada rompería la regla de que 
     * "no puede vivir sin un producto". Para removerlo, se deben eliminar sus productos 
     * desde el DAO de Productos.
     */
    void eliminar(String codigo) throws PersistenciaException;
}
