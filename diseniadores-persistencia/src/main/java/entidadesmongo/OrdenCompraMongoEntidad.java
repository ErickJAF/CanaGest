/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import java.util.ArrayList;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * Entidad de persistencia que representa un documento principal en la colección "ordenesCompra" de MongoDB.
 * 
 * Diseñada bajo un esquema de desnormalización masiva. Embebe resúmenes del usuario solicitante, 
 * del proveedor objetivo y la lista de artículos pedidos para agilizar drásticamente las consultas.
 * 
 * @author ERICK
 */
public class OrdenCompraMongoEntidad {

    @BsonId
    private ObjectId id;
    private String codigoOrden;
    private String fecha;
    private String estado;
    private double total;
    
    private UsuarioResumenMongoEntidad usuario;
    private ProveedorResumenOrdenMongoEntidad proveedor;
    private List<DetalleOrdenCompraMongoEntidad> productos;

    public OrdenCompraMongoEntidad(ObjectId id, String codigoOrden, String fecha, String estado, double total, UsuarioResumenMongoEntidad usuario, ProveedorResumenOrdenMongoEntidad proveedor, List<DetalleOrdenCompraMongoEntidad> productos) {
        this.id = id;
        this.codigoOrden = codigoOrden;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.usuario = usuario;
        this.proveedor = proveedor;
        this.productos = productos;
    }
    
    public OrdenCompraMongoEntidad() {
        this.productos = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public UsuarioResumenMongoEntidad getUsuario() {
        return usuario;
    }

    public ProveedorResumenOrdenMongoEntidad getProveedor() {
        return proveedor;
    }

    public List<DetalleOrdenCompraMongoEntidad> getProductos() {
        return productos;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setUsuario(UsuarioResumenMongoEntidad usuario) {
        this.usuario = usuario;
    }

    public void setProveedor(ProveedorResumenOrdenMongoEntidad proveedor) {
        this.proveedor = proveedor;
    }

    public void setProductos(List<DetalleOrdenCompraMongoEntidad> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "OrdenCompraMongoEntidad{" + "id=" + id + ", codigoOrden=" + codigoOrden + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", usuario=" + usuario + ", proveedor=" + proveedor + ", productos=" + productos + '}';
    }
}
