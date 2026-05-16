/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.DetalleVenta;
import entidades.ProductoResumenVenta;
import entidades.Venta;
import entidadesmongo.DetalleVentaMongoEntidad;
import entidadesmongo.ProductoResumenVentaMongoEntidad;
import entidadesmongo.VentaMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adapter encargado de gestionar las conversiones de notas de venta de dominio limpio
 * a la estructura extendida y embebida requerida por MongoDB.
 * 
 * @author ERICK
 */
public class VentaPersistenciaAdapter {

    /**
     * Convierte una venta del dominio a una entidad estructurada de MongoDB.
     *
     * @param venta venta del dominio limpio.
     * @return entidad de venta para MongoDB.
     * @throws PersistenciaException si el formato del id es inválido.
     */
    public VentaMongoEntidad convertirAMongo(Venta venta) throws PersistenciaException {
        if (venta == null) {
            return null;
        }

        VentaMongoEntidad entidadMongo = new VentaMongoEntidad();
        entidadMongo.setId(convertirStringAObjectId(venta.getId()));
        entidadMongo.setCodigoVenta(venta.getCodigoVenta());
        entidadMongo.setFechaVenta(venta.getFechaVenta());
        entidadMongo.setTotal(venta.getTotal());
        entidadMongo.setMetodoPago(venta.getMetodoPago());
        entidadMongo.setDetalles(convertirDetallesAMongo(venta.getDetalles()));

        return entidadMongo;
    }

    /**
     * Convierte una entidad de MongoDB a una venta del dominio limpio.
     *
     * @param entidadMongo entidad de MongoDB.
     * @return venta del dominio.
     */
    public Venta convertirADominio(VentaMongoEntidad entidadMongo) {
        if (entidadMongo == null) {
            return null;
        }

        Venta venta = new Venta();
        venta.setId(convertirObjectIdAString(entidadMongo.getId()));
        venta.setCodigoVenta(entidadMongo.getCodigoVenta());
        venta.setFechaVenta(entidadMongo.getFechaVenta());
        venta.setTotal(entidadMongo.getTotal());
        venta.setMetodoPago(entidadMongo.getMetodoPago());
        venta.setDetalles(convertirDetallesADominio(entidadMongo.getDetalles()));

        return venta;
    }

    /**
     * Convierte una lista de entidades MongoDB a una lista de ventas del dominio limpio.
     *
     * @param entidadesMongo lista de entidades de MongoDB.
     * @return lista de ventas.
     */
    public List<Venta> convertirListaADominio(List<VentaMongoEntidad> entidadesMongo) {
        List<Venta> ventas = new ArrayList<>();
        if (entidadesMongo == null) return ventas;
        for (VentaMongoEntidad entidadMongo : entidadesMongo) {
            ventas.add(convertirADominio(entidadMongo));
        }
        return ventas;
    }

    /**
     * Convierte una lista de detalles de venta a subdocumentos de MongoDB.
     *
     * @param detalles lista de detalles del dominio.
     * @return lista de detalles mongo.
     */
    private List<DetalleVentaMongoEntidad> convertirDetallesAMongo(List<DetalleVenta> detalles) {
        List<DetalleVentaMongoEntidad> listaMongo = new ArrayList<>();
        if (detalles == null) return listaMongo;
        for (DetalleVenta d : detalles) {
            if (d != null) {
                DetalleVentaMongoEntidad m = new DetalleVentaMongoEntidad();
                m.setCantidad(d.getCantidad());
                m.setSubtotal(d.getSubtotal());
                m.setProducto(convertirProductoResumenAMongo(d.getProducto()));
                listaMongo.add(m);
            }
        }
        return listaMongo;
    }

    /**
     * Convierte una lista de subdocumentos mongo a detalles de venta de dominio.
     *
     * @param mongoLista lista de detalles de MongoDB.
     * @return lista de detalles de dominio.
     */
    private List<DetalleVenta> convertirDetallesADominio(List<DetalleVentaMongoEntidad> mongoLista) {
        List<DetalleVenta> listaDominio = new ArrayList<>();
        if (mongoLista == null) return listaDominio;
        for (DetalleVentaMongoEntidad m : mongoLista) {
            if (m != null) {
                DetalleVenta d = new DetalleVenta();
                d.setCantidad(m.getCantidad());
                d.setSubtotal(m.getSubtotal());
                d.setProducto(convertirProductoResumenADominio(m.getProducto()));
                listaDominio.add(d);
            }
        }
        return listaDominio;
    }

    /**
     * Convierte un resumen de producto de dominio a entidad mongo.
     *
     * @param p objeto resumen del dominio.
     * @return entidad resumen mongo.
     */
    private ProductoResumenVentaMongoEntidad convertirProductoResumenAMongo(ProductoResumenVenta p) {
        if (p == null) return null;
        ProductoResumenVentaMongoEntidad m = new ProductoResumenVentaMongoEntidad();
        m.setIdProducto(p.getIdProducto());
        m.setPrecio(p.getPrecio());
        m.setNombre(p.getNombre());
        return m;
    }

    /**
     * Convierte una entidad resumen de mongo a un objeto de dominio.
     *
     * @param m entidad resumen mongo.
     * @return resumen del producto en dominio.
     */
    private ProductoResumenVenta convertirProductoResumenADominio(ProductoResumenVentaMongoEntidad m) {
        if (m == null) return null;
        ProductoResumenVenta p = new ProductoResumenVenta();
        p.setIdProducto(m.getIdProducto());
        p.setPrecio(m.getPrecio());
        p.setNombre(m.getNombre());
        return p;
    }

    /**
     * Convierte un id de texto a ObjectId.
     *
     * @param id identificador de texto.
     * @return ObjectId correspondiente.
     * @throws PersistenciaException si el formato es inválido.
     */
    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) return null;
        if (!ObjectId.isValid(id)) throw new PersistenciaException("El id recibido no es un ObjectId válido.");
        return new ObjectId(id);
    }

    /**
     * Convierte un ObjectId a String hexadecimal.
     *
     * @param id identificador ObjectId.
     * @return identificador como texto.
     */
    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}
