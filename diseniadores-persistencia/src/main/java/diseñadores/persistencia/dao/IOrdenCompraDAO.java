package diseñadores.persistencia.dao;

import entidades.OrdenCompra;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de Órdenes de Compra en el sistema utilizando entidades del dominio.
 * 
 * * @author ERICK
 */
public interface IOrdenCompraDAO {

    /**
     * Recupera el historial completo de órdenes de compra almacenadas.
     * * @return Lista con todas las órdenes de compra encontradas en dominio.
     * @throws PersistenciaException Si ocurre un error durante la consulta en MongoDB.
     */
    List<OrdenCompra> obtenerTodas() throws PersistenciaException;

    /**
     * Busca una orden de compra específica mediante su número único de identificación (código de orden).
     * * @param numero El número único o código identificador de la orden de compra.
     * @return El objeto de dominio de la orden de compra encontrada, o null si no existe.
     * @throws PersistenciaException Si los datos son inválidos o falla la base de datos.
     */
    OrdenCompra obtenerPorNumero(String numero) throws PersistenciaException;

    /**
     * Registra una nueva orden de compra de forma permanente en la base de datos.
     * * @param orden El objeto de dominio con toda la información de la orden a registrar.
     * @throws PersistenciaException Si la orden es nula, ya existe el número o falla MongoDB.
     */
    void guardar(OrdenCompra orden) throws PersistenciaException;

    /**
     * Reemplaza o actualiza los datos de una orden de compra existente.
     * * @param orden El objeto de dominio con la información actualizada.
     * @throws PersistenciaException Si la orden no existe o la actualización falla.
     */
    void actualizar(OrdenCompra orden) throws PersistenciaException;

    /**
     * Elimina del sistema una orden de compra usando su número identificador.
     * * @param numero El número o código identificador de la orden que se desea remover.
     * @throws PersistenciaException Si la orden no existe o falla la base de datos.
     */
    void eliminar(String numero) throws PersistenciaException;
}