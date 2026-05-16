/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * Entidad de persistencia que representa un documento principal en la colección "productos" de MongoDB.
 * 
 * Contiene las propiedades críticas para la gestión del inventario y las ventas, además de incrustar 
 * de forma directa (1 a 1) los datos del proveedor responsable del artículo.
 * 
 * @author ERICK
 */
public class ProductoMongoEntidad {

    @BsonId
    private ObjectId id;
    private String codigo;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private int unidades;
    private int stockMinimo;
    private int stockMaximo;
    private ProveedorMongoEntidad proveedor;

    public ProductoMongoEntidad(ObjectId id, String codigo, String nombre, double precioCompra, double precioVenta, int unidades, int stockMinimo, int stockMaximo, ProveedorMongoEntidad proveedor) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.unidades = unidades;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.proveedor = proveedor;
    }

    public ProductoMongoEntidad() {
    }

    public ObjectId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getUnidades() {
        return unidades;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public ProveedorMongoEntidad getProveedor() {
        return proveedor;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public void setProveedor(ProveedorMongoEntidad proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "ProductoMongoEntidad{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta + ", unidades=" + unidades + ", stockMinimo=" + stockMinimo + ", stockMaximo=" + stockMaximo + ", proveedor=" + proveedor + '}';
    }
}
