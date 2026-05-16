/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class ProductoResumenInventarioMongoEntidad {
    private String idProducto;
    private String nombre;
    private int stockSistema;

    public ProductoResumenInventarioMongoEntidad(String idProducto, String nombre, int stockSistema) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.stockSistema = stockSistema;
    }

    public ProductoResumenInventarioMongoEntidad() {
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStockSistema() {
        return stockSistema;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStockSistema(int stockSistema) {
        this.stockSistema = stockSistema;
    }

    @Override
    public String toString() {
        return "ProductoResumenInventarioMongoEntidad{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", stockSistema=" + stockSistema + '}';
    }
}
