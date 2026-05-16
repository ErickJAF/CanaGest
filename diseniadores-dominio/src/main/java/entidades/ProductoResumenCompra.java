/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author ERICK
 */
public class ProductoResumenCompra {
    private String idProducto; 
    private String nombre;
    private double precioCompra;

    public ProductoResumenCompra(String idProducto, String nombre, double precioCompra) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
    }

    public ProductoResumenCompra() {
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    @Override
    public String toString() {
        return "ProductoResumenCompra{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", precioCompra=" + precioCompra + '}';
    }
}
