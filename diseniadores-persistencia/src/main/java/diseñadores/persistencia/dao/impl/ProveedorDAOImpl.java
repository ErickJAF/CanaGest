package diseñadores.persistencia.dao.impl;

import adaptadores.ProductoPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IProveedorDAO;
import entidades.Proveedor;
import entidadesmongo.ProductoMongoEntidad;
import entidadesmongo.ProveedorMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar la persistencia de proveedores. Al estar embebidos
 * en la colección de productos, este DAO consume y modifica de forma directa
 * la colección "productos" utilizando el adaptador de productos del sistema.
 * 
 * @author ERICK
 */
public class ProveedorDAOImpl implements IProveedorDAO {

    private final MongoCollection<ProductoMongoEntidad> coleccionProductos;
    private final ProductoPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la colección de productos y 
     * levanta el adaptador de productos del sistema.
     */
    public ProveedorDAOImpl() {
        this.coleccionProductos = Conexion.getInstancia().obtenerColeccionProductos();
        this.adaptador = new ProductoPersistenciaAdapter();
    }

    @Override
    public List<Proveedor> obtenerTodos() throws PersistenciaException {
        try {
            List<Proveedor> listaDominio = new ArrayList<>();
            // .distinct extrae directamente los subdocumentos del campo "proveedor" sin repetir ninguno
            List<ProveedorMongoEntidad> entidadesMongo = coleccionProductos
                    .distinct("proveedor", ProveedorMongoEntidad.class)
                    .into(new ArrayList<>());

            for (ProveedorMongoEntidad m : entidadesMongo) {
                if (m != null) {
                    listaDominio.add(adaptador.convertirProveedorADominio(m));
                }
            }
            return listaDominio;
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible obtener la lista de proveedores desde MongoDB.", ex);
        }
    }

    @Override
    public Proveedor obtenerPorCodigo(String codigo) throws PersistenciaException {
        validarCodigoRequerido(codigo);
        try {
            // Buscamos cualquier producto que contenga a este proveedor en su subdocumento
            ProductoMongoEntidad producto = coleccionProductos
                    .find(Filters.eq("proveedor.codigo", codigo))
                    .first();

            return (producto != null) ? adaptador.convertirProveedorADominio(producto.getProveedor()) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al buscar el proveedor con código: " + codigo, ex);
        }
    }

    @Override
    public void guardar(Proveedor proveedor) throws PersistenciaException {
        // Al estar acoplado fuertemente al producto, un proveedor no se inserta al aire.
        throw new PersistenciaException("Operación no soportada: Para registrar un proveedor, "
                + "este debe asignarse a un producto utilizando el IProductoDAO.");
    }

    @Override
    public void actualizar(Proveedor proveedor) throws PersistenciaException {
        if (proveedor == null) {
            throw new PersistenciaException("El proveedor a actualizar no puede ser nulo.");
        }
        validarCodigoRequerido(proveedor.getCodigo());
        validarProveedorExiste(proveedor.getCodigo());

        try {
            ProveedorMongoEntidad mongoEntidad = adaptador.convertirProveedorAMongo(proveedor);

            // Actualizamos en cascada el subdocumento completo en todos los productos vinculados
            coleccionProductos.updateMany(
                Filters.eq("proveedor.codigo", proveedor.getCodigo()),
                Updates.set("proveedor", mongoEntidad)
            );
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar los datos del proveedor en el catálogo.", ex);
        }
    }

    @Override
    public void eliminar(String codigo) throws PersistenciaException {
        // Respetamos la regla de negocio: si no vive sin un producto, no se puede borrar de forma aislada
        throw new PersistenciaException("Operación no soportada: Un proveedor no puede ser eliminado "
                + "si tiene productos asociados. Elimine los productos correspondientes desde IProductoDAO.");
    }
    
    /**
     * Valida defensivamente que el código del proveedor sea válido.
     */
    private void validarCodigoRequerido(String codigo) throws PersistenciaException {
        if (codigo == null || codigo.isBlank()) {
            throw new PersistenciaException("El código del proveedor es requerido para la operación.");
        }
    }

    /**
     * Comprueba que el proveedor realmente exista en algún producto antes de disparar actualizaciones.
     */
    private void validarProveedorExiste(String codigo) throws PersistenciaException {
        if (obtenerPorCodigo(codigo) == null) {
            throw new PersistenciaException("El proveedor con código '" + codigo + "' no existe en el sistema.");
        }
    }
}