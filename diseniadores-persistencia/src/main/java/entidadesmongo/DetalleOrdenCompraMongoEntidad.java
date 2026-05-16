/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class DetalleOrdenCompraMongoEntidad {
    private ProductoResumenCompraMongoEntidad producto;
    private int cantidad;
    private double subtotal;

    public DetalleOrdenCompraMongoEntidad(ProductoResumenCompraMongoEntidad producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public DetalleOrdenCompraMongoEntidad() {
    }

    public ProductoResumenCompraMongoEntidad getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setProducto(ProductoResumenCompraMongoEntidad producto) {
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleOrdenCompraMongoEntidad{" + "producto=" + producto + ", cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }
}
