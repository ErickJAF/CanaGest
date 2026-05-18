package adaptadores;

import entidades.DetalleOrdenCompra;
import entidades.OrdenCompra;
import entidades.ProductoResumenCompra;
import entidades.ProveedorResumenOrden;
import entidades.UsuarioResumen;
import entidadesmongo.DetalleOrdenCompraMongoEntidad;
import entidadesmongo.OrdenCompraMongoEntidad;
import entidadesmongo.ProductoResumenCompraMongoEntidad;
import entidadesmongo.ProveedorResumenOrdenMongoEntidad;
import entidadesmongo.UsuarioResumenMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adaptador de persistencia perimetral encargado de gestionar la conversión binaria
 * y estructural entre entidades del dominio y los esquemas de entidades MongoDB.
 * Procesa colecciones desnormalizadas incrustadas en cascada (Usuario, Proveedor y Productos).
 * * @author ERICK
 * @version 1.0
 */
public class OrdenCompraPersistenciaAdapter {

    /**
     * Transforma una entidad pura de la capa de negocio/dominio en una entidad 
     * estructurada compatible con el mapeo directo BSON en colecciones MongoDB.
     *
     * @param orden Instancia de la clase OrdenCompra del dominio técnico.
     * @return Objeto mapeado OrdenCompraMongoEntidad listo para almacenamiento.
     * @throws PersistenciaException Si la cadena identificadora del ID no cuenta con un formato ObjectId válido.
     */
    public OrdenCompraMongoEntidad convertirAMongo(OrdenCompra orden) throws PersistenciaException {
        if (orden == null) return null;

        OrdenCompraMongoEntidad mongo = new OrdenCompraMongoEntidad();
        mongo.setId(convertirStringAObjectId(orden.getId()));
        mongo.setCodigoOrden(orden.getCodigoOrden());
        mongo.setFecha(orden.getFecha());
        mongo.setEstado(orden.getEstado());
        mongo.setTotal(orden.getTotal());
        mongo.setUsuario(convertirUsuarioAMongo(orden.getUsuario()));
        mongo.setProveedor(convertirProveedorAMongo(orden.getProveedor()));
        mongo.setProductos(convertirDetallesAMongo(orden.getProductos()));

        return mongo;
    }

    /**
     * Transforma una entidad documental tipada extraída desde MongoDB hacia un objeto 
     * limpio de la capa de dominio utilizable por las reglas del negocio.
     *
     * @param mongo Entidad de persistencia mapeada OrdenCompraMongoEntidad.
     * @return Instancia limpia reconstruida de tipo OrdenCompra.
     */
    public OrdenCompra convertirADominio(OrdenCompraMongoEntidad mongo) {
        if (mongo == null) return null;

        OrdenCompra orden = new OrdenCompra();
        orden.setId(convertirObjectIdAString(mongo.getId()));
        orden.setCodigoOrden(mongo.getCodigoOrden());
        orden.setFecha(mongo.getFecha());
        orden.setEstado(mongo.getEstado());
        orden.setTotal(mongo.getTotal());
        orden.setUsuario(convertirUsuarioADominio(mongo.getUsuario()));
        orden.setProveedor(convertirProveedorADominio(mongo.getProveedor()));
        orden.setProductos(convertirDetallesADominio(mongo.getProductos()));

        return orden;
    }

    /**
     * Convierte una lista completa de entidades documentales nativas de MongoDB 
     * en una lista estructurada compatible con objetos puros de la capa de dominio.
     *
     * @param entidadesMongo Lista de objetos provenientes de la persistencia OrdenCompraMongoEntidad.
     * @return Lista dinámica procesada de entidades OrdenCompra.
     */
    public List<OrdenCompra> convertirListaADominio(List<OrdenCompraMongoEntidad> entidadesMongo) {
        List<OrdenCompra> lista = new ArrayList<>();
        if (entidadesMongo == null) return lista;
        for (OrdenCompraMongoEntidad m : entidadesMongo) {
            lista.add(convertirADominio(m));
        }
        return lista;
    }

    private UsuarioResumenMongoEntidad convertirUsuarioAMongo(UsuarioResumen u) {
        if (u == null) return null;
        UsuarioResumenMongoEntidad m = new UsuarioResumenMongoEntidad();
        m.setIdUsuario(u.getIdUsuario());
        m.setNombre(u.getNombre());
        m.setRol(u.getRol());
        return m;
    }

    private UsuarioResumen convertirUsuarioADominio(UsuarioResumenMongoEntidad m) {
        if (m == null) return null;
        UsuarioResumen u = new UsuarioResumen();
        u.setIdUsuario(m.getIdUsuario());
        u.setNombre(m.getNombre());
        u.setRol(m.getRol());
        return u;
    }

    private ProveedorResumenOrdenMongoEntidad convertirProveedorAMongo(ProveedorResumenOrden p) {
        if (p == null) return null;
        ProveedorResumenOrdenMongoEntidad m = new ProveedorResumenOrdenMongoEntidad();
        m.setIdProveedor(p.getIdProveedor());
        m.setNombre(p.getNombre());
        m.setTelefono(p.getTelefono());
        m.setGmail(p.getGmail());
        return m;
    }

    private ProveedorResumenOrden convertirProveedorADominio(ProveedorResumenOrdenMongoEntidad m) {
        if (m == null) return null;
        ProveedorResumenOrden p = new ProveedorResumenOrden();
        p.setIdProveedor(m.getIdProveedor());
        p.setNombre(m.getNombre());
        p.setTelefono(m.getTelefono());
        p.setGmail(m.getGmail());
        return p;
    }

    private List<DetalleOrdenCompraMongoEntidad> convertirDetallesAMongo(List<DetalleOrdenCompra> list) {
        List<DetalleOrdenCompraMongoEntidad> r = new ArrayList<>();
        if (list == null) return r;
        for (DetalleOrdenCompra d : list) {
            if (d != null) {
                DetalleOrdenCompraMongoEntidad m = new DetalleOrdenCompraMongoEntidad();
                m.setCantidad(d.getCantidad());
                m.setSubtotal(d.getSubtotal());
                
                if (d.getProducto() != null) {
                    ProductoResumenCompraMongoEntidad pm = new ProductoResumenCompraMongoEntidad();
                    pm.setIdProducto(d.getProducto().getIdProducto());
                    pm.setNombre(d.getProducto().getNombre());
                    pm.setPrecioCompra(d.getProducto().getPrecioCompra());
                    m.setProducto(pm);
                }
                r.add(m);
            }
        }
        return r;
    }

    private List<DetalleOrdenCompra> convertirDetallesADominio(List<DetalleOrdenCompraMongoEntidad> list) {
        List<DetalleOrdenCompra> r = new ArrayList<>();
        if (list == null) return r;
        for (DetalleOrdenCompraMongoEntidad m : list) {
            if (m != null) {
                DetalleOrdenCompra d = new DetalleOrdenCompra();
                d.setCantidad(m.getCantidad());
                d.setSubtotal(m.getSubtotal());
                
                if (m.getProducto() != null) {
                    ProductoResumenCompra p = new ProductoResumenCompra();
                    p.setIdProducto(m.getProducto().getIdProducto());
                    p.setNombre(m.getProducto().getNombre());
                    p.setPrecioCompra(m.getProducto().getPrecioCompra());
                    d.setProducto(p);
                }
                r.add(d);
            }
        }
        return r;
    }

    /**
     * Convierte una cadena de texto plana (String hex) en un objeto ObjectId estructural nativo de MongoDB.
     *
     * @param id Cadena representativa del identificador único de la orden.
     * @return El objeto transformado ObjectId técnico.
     * @throws PersistenciaException Si la cadena de caracteres posee un formato hexadecimal inválido.
     */
    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) return null;
        if (!ObjectId.isValid(id)) throw new PersistenciaException("Id inválido para mapeo en Orden.");
        return new ObjectId(id);
    }

    /**
     * Traduce un objeto técnico ObjectId proveniente de MongoDB a una representación
     * alfanumérica simple de tipo String.
     *
     * @param id Identificador nativo de MongoDB de tipo ObjectId.
     * @return Cadena hexadecimal simple equivalente al ID, o null si el objeto es nulo.
     */
    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}