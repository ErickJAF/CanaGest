/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author ERICK
 */
public class ProductoResumenInventario {
    private String idProducto; 
    private String nombre;
    private int stockSistema;

    public ProductoResumenInventario(String idProducto, String nombre, int stockSistema) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.stockSistema = stockSistema;
    }

    public ProductoResumenInventario() {
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

    public int getStockSistema() {
        return stockSistema;
    }

    public void setStockSistema(int stockSistema) {
        this.stockSistema = stockSistema;
    }

    @Override
    public String toString() {
        return "ProductoResumenInventario{" + "idProducto=" + idProducto + ", nombre=" + nombre + ", stockSistema=" + stockSistema + '}';
    }
}
