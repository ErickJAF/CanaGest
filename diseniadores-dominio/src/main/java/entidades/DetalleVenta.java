/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author ERICK
 */
public class DetalleVenta {
    private ProductoResumenVenta producto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(ProductoResumenVenta producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public DetalleVenta() {
    }

    public ProductoResumenVenta getProducto() {
        return producto;
    }

    public void setProducto(ProductoResumenVenta producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" + "producto=" + producto + ", cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }
}
