/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class DetalleVentaMongoEntidad {
    private ProductoResumenVentaMongoEntidad producto;
    private int cantidad;
    private double subtotal;

    public DetalleVentaMongoEntidad(ProductoResumenVentaMongoEntidad producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public DetalleVentaMongoEntidad() {
    }

    public ProductoResumenVentaMongoEntidad getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setProducto(ProductoResumenVentaMongoEntidad producto) {
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
        return "DetalleVentaMongoEntidad{" + "producto=" + producto + ", cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }
}
