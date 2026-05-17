/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.Domicilio;
import entidades.Producto;
import entidades.Proveedor;
import entidadesmongo.DomicilioMongoEntidad;
import entidadesmongo.ProductoMongoEntidad;
import entidadesmongo.ProveedorMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adapter encargado de convertir objetos del dominio de Productos (con sus Proveedores 
 * y Domicilios embebidos) a sus respectivas entidades de persistencia de MongoDB y viceversa.
 *
 * @author ERICK
 */
public class ProductoPersistenciaAdapter {

    /**
     * Convierte un producto del dominio limpio a una entidad de MongoDB.
     *
     * @param producto producto del dominio limpio.
     * @return entidad de producto lista para persistirse.
     * @throws PersistenciaException si el id no tiene formato válido de ObjectId.
     */
    public ProductoMongoEntidad convertirAMongo(Producto producto) throws PersistenciaException {
        if (producto == null) {
            return null;
        }

        ProductoMongoEntidad entidadMongo = new ProductoMongoEntidad();
        entidadMongo.setId(convertirStringAObjectId(producto.getId()));
        entidadMongo.setCodigo(producto.getCodigo());
        entidadMongo.setNombre(producto.getNombre());
        entidadMongo.setPrecioCompra(producto.getPrecioCompra());
        entidadMongo.setPrecioVenta(producto.getPrecioVenta());
        entidadMongo.setUnidades(producto.getUnidades());
        entidadMongo.setStockMinimo(producto.getStockMinimo());
        entidadMongo.setStockMaximo(producto.getStockMaximo());
        entidadMongo.setProveedor(convertirProveedorAMongo(producto.getProveedor()));

        return entidadMongo;
    }

    /**
     * Convierte una entidad de MongoDB a un producto del dominio limpio.
     *
     * @param entidadMongo entidad recuperada de MongoDB.
     * @return producto del dominio limpio.
     */
    public Producto convertirADominio(ProductoMongoEntidad entidadMongo) {
        if (entidadMongo == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setId(convertirObjectIdAString(entidadMongo.getId()));
        producto.setCodigo(entidadMongo.getCodigo());
        producto.setNombre(entidadMongo.getNombre());
        producto.setPrecioCompra(entidadMongo.getPrecioCompra());
        producto.setPrecioVenta(entidadMongo.getPrecioVenta());
        producto.setUnidades(entidadMongo.getUnidades());
        producto.setStockMinimo(entidadMongo.getStockMinimo());
        producto.setStockMaximo(entidadMongo.getStockMaximo());
        producto.setProveedor(convertirProveedorADominio(entidadMongo.getProveedor()));

        return producto;
    }

    /**
     * Convierte una lista de entidades MongoDB a una lista de productos del dominio limpio.
     *
     * @param entidadesMongo lista de entidades de MongoDB.
     * @return lista de productos limpios.
     */
    public List<Producto> convertirListaADominio(List<ProductoMongoEntidad> entidadesMongo) {
        List<Producto> productos = new ArrayList<>();
        if (entidadesMongo == null) {
            return productos;
        }
        for (ProductoMongoEntidad entidadMongo : entidadesMongo) {
            productos.add(convertirADominio(entidadMongo));
        }
        return productos;
    }

    /**
     * Convierte un proveedor de dominio a un subdocumento de persistencia de Mongo.
     *
     * @param proveedor proveedor de dominio.
     * @return entidad mongo de proveedor.
     */
    public ProveedorMongoEntidad convertirProveedorAMongo(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }
        ProveedorMongoEntidad mongo = new ProveedorMongoEntidad();
        mongo.setCodigo(proveedor.getCodigo());
        mongo.setNombre(proveedor.getNombre());
        mongo.setTelefono(proveedor.getTelefono());
        mongo.setGmail(proveedor.getGmail());
        mongo.setTerminosPago(proveedor.getTerminosPago());
        mongo.setDomicilio(convertirDomicilioAMongo(proveedor.getDomicilio()));
        return mongo;
    }

    /**
     * Convierte un subdocumento de proveedor de Mongo a un objeto de dominio limpio.
     *
     * @param mongo entidad mongo de proveedor.
     * @return proveedor del dominio.
     */
    public Proveedor convertirProveedorADominio(ProveedorMongoEntidad mongo) {
        if (mongo == null) {
            return null;
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setCodigo(mongo.getCodigo());
        proveedor.setNombre(mongo.getNombre());
        proveedor.setTelefono(mongo.getTelefono());
        proveedor.setGmail(mongo.getGmail());
        proveedor.setTerminosPago(mongo.getTerminosPago());
        proveedor.setDomicilio(convertirDomicilioADominio(mongo.getDomicilio()));
        return proveedor;
    }

    /**
     * Convierte un domicilio de dominio a un subdocumento de persistencia.
     *
     * @param domicilio domicilio de dominio.
     * @return entidad mongo de domicilio.
     */
    private DomicilioMongoEntidad convertirDomicilioAMongo(Domicilio domicilio) {
        if (domicilio == null) {
            return null;
        }
        DomicilioMongoEntidad mongo = new DomicilioMongoEntidad();
        mongo.setCalle(domicilio.getCalle());
        mongo.setNumero(domicilio.getNumero());
        mongo.setColonia(domicilio.getColonia());
        return mongo;
    }

    /**
     * Convierte un subdocumento de domicilio a un objeto de dominio limpio.
     *
     * @param mongo entidad mongo de domicilio.
     * @return domicilio de dominio.
     */
    private Domicilio convertirDomicilioADominio(DomicilioMongoEntidad mongo) {
        if (mongo == null) {
            return null;
        }
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(mongo.getCalle());
        domicilio.setNumero(mongo.getNumero());
        domicilio.setColonia(mongo.getColonia());
        return domicilio;
    }

    /**
     * Convierte un id de texto a ObjectId.
     *
     * @param id identificador como texto.
     * @return ObjectId correspondiente.
     * @throws PersistenciaException si el formato no es válido.
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
