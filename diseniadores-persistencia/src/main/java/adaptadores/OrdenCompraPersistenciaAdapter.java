/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * Adapter encargado de procesar la conversión de Órdenes de Compra con su triple 
 * desnormalización incrustada (Usuario, Proveedor e Historial de Productos).
 * 
 * @author ERICK
 */
public class OrdenCompraPersistenciaAdapter {

    /**
     * Convierte una orden de compra del dominio a entidad Mongo.
     *
     * @param orden orden de compra limpia.
     * @return documento de orden de compra listo para guardarse.
     * @throws PersistenciaException si el id tiene formato inválido.
     */
    public OrdenCompraMongoEntidad convertirAMongo(OrdenCompra orden) throws PersistenciaException {
        if (orden == null) return null;

        OrdenCompraMongoEntidad mongo = new OrdenCompraMongoEntidad();
        mongo.setId(convertirStringAObjectId(orden.getId()));
        mongo.setCodigoOrden(orden.getCodigoOrden());
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
     * Convierte una entidad de Mongo a un objeto orden de compra de dominio.
     *
     * @param mongo entidad recuperada de Mongo.
     * @return orden de compra limpia.
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
     * Convierte una lista de entidades de orden de compra a lista de dominio.
     *
     * @param entidadesMongo lista de entidades.
     * @return lista de ordenes limpias.
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
     * Convierte una cadena de texto en un objeto ObjectId válido de Mongo.
     *
     * @param id cadena id.
     * @return ObjectId.
     * @throws PersistenciaException si falla el casteo.
     */
    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) return null;
        if (!ObjectId.isValid(id)) throw new PersistenciaException("Id inváido para mapeo en Orden.");
        return new ObjectId(id);
    }

    /**
     * Traduce un ObjectId técnico de Mongo en una representación de String simple.
     *
     * @param id identificador ObjectId.
     * @return id en String.
     */
    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}
