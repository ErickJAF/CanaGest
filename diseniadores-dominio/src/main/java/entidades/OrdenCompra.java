/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.List;

/**
 * Clase del dominio limpio que representa una Orden de Compra para reabastecimiento.
 * Modela la solicitud formal de mercancía enviada a un proveedor. Contiene información
 * de negocio consolidada que vincula al usuario que genera la orden, al proveedor 
 * destino y el desglose de productos solicitados.
 *
 * @author ERICK
 */
public class OrdenCompra {
    private String id;
    private String codigoOrden; 
    private String fecha;
    private String estado;
    private double total;

    private UsuarioResumen usuario;
    private ProveedorResumenOrden proveedor;
    private List<DetalleOrdenCompra> productos;

    public OrdenCompra(String id, String codigoOrden, String fecha, String estado, double total, UsuarioResumen usuario, ProveedorResumenOrden proveedor, List<DetalleOrdenCompra> productos) {
        this.id = id;
        this.codigoOrden = codigoOrden;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.usuario = usuario;
        this.proveedor = proveedor;
        this.productos = productos;
    }

    public OrdenCompra() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public UsuarioResumen getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumen usuario) {
        this.usuario = usuario;
    }

    public ProveedorResumenOrden getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorResumenOrden proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleOrdenCompra> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleOrdenCompra> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "OrdenCompra{" + "id=" + id + ", codigoOrden=" + codigoOrden + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", usuario=" + usuario + ", proveedor=" + proveedor + ", productos=" + productos + '}';
    }
}
