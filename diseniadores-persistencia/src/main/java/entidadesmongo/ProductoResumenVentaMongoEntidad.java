/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class ProductoResumenVentaMongoEntidad {
    private String idProducto;
    private double precio;
    private String nombre;

    public ProductoResumenVentaMongoEntidad(String idProducto, double precio, String nombre) {
        this.idProducto = idProducto;
        this.precio = precio;
        this.nombre = nombre;
    }

    public ProductoResumenVentaMongoEntidad() {
    }
    
    public String getIdProducto() {
        return idProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ProductoResumenVentaMongoEntidad{" + "idProducto=" + idProducto + ", precio=" + precio + ", nombre=" + nombre + '}';
    }
}
