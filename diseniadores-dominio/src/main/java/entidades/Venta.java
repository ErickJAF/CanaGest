/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.List;

/**
 * Clase del dominio limpio que representa una Venta o transacción comercial.
 * Modela el encabezado de la nota de venta (totales, fechas, métodos de pago) 
 * y agrupa la lista de los detalles correspondientes a los artículos adquiridos 
 * por el cliente en una operación atómica.
 *
 * @author ERICK
 */
public class Venta {
    private String id;
    private String codigoVenta; 
    private String fechaVenta;
    private double total;
    private String metodoPago;
    private List<DetalleVenta> detalles;

    public Venta(String id, String codigoVenta, String fechaVenta, double total, String metodoPago, List<DetalleVenta> detalles) {
        this.id = id;
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }

    public Venta() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Venta{" + "id=" + id + ", codigoVenta=" + codigoVenta + ", fechaVenta=" + fechaVenta + ", total=" + total + ", metodoPago=" + metodoPago + ", detalles=" + detalles + '}';
    }
}
