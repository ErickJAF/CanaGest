package diseñadores.persistencia.dao;

import diseñadores.negocios.dto.ProductoDTO;
import entidades.Producto;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de Productos en el sistema utilizando entidades del dominio.
 * 
 * @author ERICK
 */
public interface IProductoDAO {

    /**
     * Recupera el catálogo completo de productos almacenados en la base de datos.
     * * @return Lista con todos los productos transformados a dominio.
     * @throws PersistenciaException Si ocurre un error de comunicación con MongoDB.
     */
    List<Producto> obtenerTodos() throws PersistenciaException;

    /**
     * Busca un producto específico mediante su código único de negocio.
     * * @param codigo El código único identificador del producto.
     * @return El objeto de dominio del producto encontrado, o null si no existe.
     * @throws PersistenciaException Si el código es inválido o falla la base de datos.
     */
    Producto obtenerPorCodigo(String codigo) throws PersistenciaException;

    /**
     * Registra un nuevo producto de forma permanente en la base de datos.
     * * @param producto El objeto de dominio con toda la información del producto a registrar.
     * @throws PersistenciaException Si el producto es nulo, el código ya existe o falla MongoDB.
     */
    void guardar(Producto producto) throws PersistenciaException;

    /**
     * Reemplaza o actualiza los datos de un producto existente.
     * * @param producto El objeto de dominio con la información actualizada.
     * @throws PersistenciaException Si el producto no existe o la actualización falla.
     */
    void actualizar(Producto producto) throws PersistenciaException;

    /**
     * Elimina del sistema un producto usando su código identificador.
     * * @param codigo El código del producto que se desea remover.
     * @throws PersistenciaException Si el producto no existe o falla la base de datos.
     */
    void eliminar(String codigo) throws PersistenciaException;
}
