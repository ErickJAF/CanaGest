/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diseñadores.negocios.dto;

import java.math.BigDecimal;

/**
 *
 * @author ERICK
 */
public class DetalleOrdenCompraDTO {

    private String idProducto;
    private String nombreProducto;
    private int cantidad;
    private BigDecimal precioCosto;
    private BigDecimal subtotal;

    public DetalleOrdenCompraDTO() {
    }

    public DetalleOrdenCompraDTO(String idProducto, String nombreProducto, int cantidad, BigDecimal precioCosto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioCosto = precioCosto;
        this.subtotal = precioCosto.multiply(BigDecimal.valueOf(cantidad));
    }

    public String getIdProducto() { 
        return idProducto; 
    }
    
    public void setIdProducto(String idProducto) { 
        this.idProducto = idProducto; 
    }

    public String getNombreProducto() { 
        return nombreProducto; 
    }
    
    public void setNombreProducto(String nombreProducto) { 
        this.nombreProducto = nombreProducto; 
    }

    public int getCantidad() { 
        return cantidad; 
    }
    
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        recalcularSubtotal();
    }

    public BigDecimal getPrecioCosto() { 
        return precioCosto; 
    }
    
    public void setPrecioCosto(BigDecimal precioCosto) { 
        this.precioCosto = precioCosto; 
        recalcularSubtotal();
    }

    public BigDecimal getSubtotal() { 
        return subtotal; 
    }
    
    public void setSubtotal(BigDecimal subtotal) { 
        this.subtotal = subtotal; 
    }

    private void recalcularSubtotal() {
        if (this.precioCosto != null && this.cantidad > 0) {
            this.subtotal = this.precioCosto.multiply(BigDecimal.valueOf(this.cantidad));
        }
    }
}