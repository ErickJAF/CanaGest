/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 * Clase del dominio limpio que representa un Producto dentro del catálogo del sistema.
 * Centraliza los atributos comerciales y de control de existencias para la gestión 
 * del inventario. Incluye la composición directa con la información del proveedor que 
 * surte el artículo.
 *
 * @author ERICK
 */
public class Producto {
    private String id; 
    private String codigo; 
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private int unidades;
    private int stockMinimo;
    private int stockMaximo;
    private Proveedor proveedor;

    public Producto(String id, String codigo, String nombre, double precioCompra, double precioVenta, int unidades, int stockMinimo, int stockMaximo, Proveedor proveedor) {
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

    public Producto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta + ", unidades=" + unidades + ", stockMinimo=" + stockMinimo + ", stockMaximo=" + stockMaximo + ", proveedor=" + proveedor + '}';
    }
}
