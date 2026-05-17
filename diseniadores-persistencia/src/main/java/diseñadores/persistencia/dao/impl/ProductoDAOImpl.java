package diseñadores.persistencia.dao.impl;

import adaptadores.ProductoPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IProductoDAO;
import entidades.Producto;
import entidadesmongo.ProductoMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar la colección de productos en MongoDB.
 * Utiliza la conexión Singleton del sistema y delega las transformaciones
 * estructurales a un adaptador perimetral de objetos de dominio.
 * 
 * @author ERICK
 */
public class ProductoDAOImpl implements IProductoDAO {

    private final MongoCollection<ProductoMongoEntidad> coleccion;
    private final ProductoPersistenciaAdapter adaptador;

    /**
     * Constructor que inicializa la colección tipada de MongoDB para Productos
     * y levanta el adaptador de persistencia.
     */
    public ProductoDAOImpl() {
        // Asumiendo que tu Singleton maneja un método similar para obtener la colección de productos mapeada
        this.coleccion = Conexion.getInstancia().obtenerColeccionProductos(); 
        this.adaptador = new ProductoPersistenciaAdapter();
    }

    @Override
    public List<Producto> obtenerTodos() throws PersistenciaException {
        try {
            List<ProductoMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible obtener la lista de productos desde MongoDB.", ex);
        }
    }

    @Override
    public Producto obtenerPorCodigo(String codigo) throws PersistenciaException {
        validarCodigoRequerido(codigo);
        try {
            ProductoMongoEntidad entidad = buscarDocumentoPorCodigo(codigo);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible buscar el producto por su código.", ex);
        }
    }

    @Override
    public void guardar(Producto producto) throws PersistenciaException {
        validarDatosProducto(producto);
        validarCodigoDisponible(producto.getCodigo());
        try {
            ejecutarInsercion(producto);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible guardar el producto en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(Producto producto) throws PersistenciaException {
        validarDatosProducto(producto);
        validarProductoExiste(producto.getCodigo());
        try {
            ejecutarReemplazo(producto);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar el producto seleccionado.", ex);
        }
    }

    @Override
    public void eliminar(String codigo) throws PersistenciaException {
        validarCodigoRequerido(codigo);
        validarProductoExiste(codigo);
        try {
            ejecutarEliminacion(codigo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar el producto de MongoDB.", ex);
        }
    }

    /**
     * Valida que el parámetro del código no sea nulo ni esté vacío.
     */
    private void validarCodigoRequerido(String codigo) throws PersistenciaException {
        if (codigo == null || codigo.isBlank()) {
            throw new PersistenciaException("El código del producto es obligatorio y no puede estar vacío.");
        }
    }

    /**
     * Verifica la integridad estructural del producto de dominio antes de operar sobre él.
     */
    private void validarDatosProducto(Producto producto) throws PersistenciaException {
        if (producto == null) {
            throw new PersistenciaException("El producto no puede ser nulo.");
        }
        validarCodigoRequerido(producto.getCodigo());
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new PersistenciaException("El nombre del producto es requerido.");
        }
    }

    /**
     * Garantiza que el código de producto no esté duplicado en el sistema.
     */
    private void validarCodigoDisponible(String codigo) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigo) != null) {
            throw new PersistenciaException("El código de producto '" + codigo + "' ya se encuentra registrado.");
        }
    }

    /**
     * Comprueba si el producto buscado existe físicamente en MongoDB.
     */
    private void validarProductoExiste(String codigo) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigo) == null) {
            throw new PersistenciaException("El producto con código '" + codigo + "' no existe en el sistema.");
        }
    }

    /**
     * Realiza la consulta directa en la colección tipada mediante filtros BSON buscando por código comercial.
     */
    private ProductoMongoEntidad buscarDocumentoPorCodigo(String codigo) {
        return coleccion.find(Filters.eq("codigo", codigo)).first();
    }

    /**
     * Invoca la operación atómica de inserción delegando la transformación al adapter.
     */
    private void ejecutarInsercion(Producto producto) throws PersistenciaException {
        ProductoMongoEntidad entidadMongo = adaptador.convertirAMongo(producto);
        coleccion.insertOne(entidadMongo);
    }

    /**
     * Invoca la sustitución completa de un documento resguardando la inmutabilidad del _id original de Mongo.
     */
    private void ejecutarReemplazo(Producto producto) throws PersistenciaException {
        ProductoMongoEntidad documentoActual = buscarDocumentoPorCodigo(producto.getCodigo());
        ProductoMongoEntidad entidadMongo = adaptador.convertirAMongo(producto);

        // Clave: Conservamos el ID técnico interno para evitar el error de inmutabilidad del _id
        if (documentoActual != null) {
            entidadMongo.setId(documentoActual.getId());
        }

        coleccion.replaceOne(Filters.eq("codigo", producto.getCodigo()), entidadMongo);
    }

    /**
     * Ejecuta la eliminación física del documento en MongoDB.
     */
    private void ejecutarEliminacion(String codigo) {
        coleccion.deleteOne(Filters.eq("codigo", codigo));
    }
}