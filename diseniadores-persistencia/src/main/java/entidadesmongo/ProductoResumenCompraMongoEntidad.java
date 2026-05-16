/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class ProductoResumenCompraMongoEntidad {
    private String idProducto;
    private String nombre;
    private double precioCompra;

    public ProductoResumenCompraMongoEntidad(String idProducto, String nombre, double precioCompra) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
    }

    public ProductoResumenCompraMongoEntidad() {
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    @Override
    public String toString() {
        return "ProductoResumenCompraMongoEntidad{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", precioCompra=" + precioCompra + '}';
    }
}
