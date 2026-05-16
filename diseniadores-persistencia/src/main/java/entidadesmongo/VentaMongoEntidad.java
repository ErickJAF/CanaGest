/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import java.util.ArrayList;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * Entidad de persistencia que representa un documento principal en la colección "ventas" de MongoDB.
 * Utiliza el patrón de desnormalización "Extended Reference" e incrusta una lista de subdocumentos 
 * de detalles de venta (`List<DetalleVentaMongoEntidad>`) logrando que toda la transacción se procese
 * 
 * de manera atómica y simplificando las consultas de facturación.
 * @author ERICK
 */
public class VentaMongoEntidad {

    @BsonId
    private ObjectId id;
    private String codigoVenta;
    private String fechaVenta;
    private double total;
    private String metodoPago;
    private List<DetalleVentaMongoEntidad> detalles;

    public VentaMongoEntidad(ObjectId id, String codigoVenta, String fechaVenta, double total, String metodoPago, List<DetalleVentaMongoEntidad> detalles) {
        this.id = id;
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }
    
    public VentaMongoEntidad() {
        this.detalles = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public List<DetalleVentaMongoEntidad> getDetalles() {
        return detalles;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setDetalles(List<DetalleVentaMongoEntidad> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "VentaMongoEntidad{" + "id=" + id + ", codigoVenta=" + codigoVenta + ", fechaVenta=" + fechaVenta + ", total=" + total + ", metodoPago=" + metodoPago + ", detalles=" + detalles + '}';
    }
}