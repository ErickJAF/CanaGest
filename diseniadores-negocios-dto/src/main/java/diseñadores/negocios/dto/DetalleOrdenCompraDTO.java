package diseñadores.negocios.dto;

import java.math.BigDecimal;

/**
 * Objeto de Transferencia de Datos (DTO) que representa el detalle 
 * de un producto específico dentro de una orden de compra.
 * * @author icoro
 */
public class DetalleOrdenCompraDTO {

    private String idProducto;
    private String nombreProducto;
    private int cantidad;
    private BigDecimal precioCosto;
    private BigDecimal subtotal;

    /**
     * Constructor vacío por defecto.
     */
    public DetalleOrdenCompraDTO() {
    }

    /**
     * Constructor con los campos obligatorios del detalle.
     * Calcula automáticamente el subtotal basado en la cantidad y el precio de costo.
     * * @param idProducto     Identificador único del producto.
     * @param nombreProducto Nombre comercial del producto.
     * @param cantidad       Cantidad de unidades solicitadas.
     * @param precioCosto    Precio unitario de costo del producto.
     * @author icoro
     */
    public DetalleOrdenCompraDTO(String idProducto, String nombreProducto, int cantidad, BigDecimal precioCosto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioCosto = precioCosto;
        this.subtotal = precioCosto.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * @return El identificador del producto.
     */
    public String getIdProducto() { 
        return idProducto; 
    }
    
    /**
     * @param idProducto El nuevo identificador del producto a establecer.
     */
    public void setIdProducto(String idProducto) { 
        this.idProducto = idProducto; 
    }

    /**
     * @return El nombre del producto.
     */
    public String getNombreProducto() { 
        return nombreProducto; 
    }
    
    /**
     * @param nombreProducto El nuevo nombre del producto a establecer.
     */
    public void setNombreProducto(String nombreProducto) { 
        this.nombreProducto = nombreProducto; 
    }

    /**
     * @return La cantidad de unidades en este detalle.
     */
    public int getCantidad() { 
        return cantidad; 
    }
    
    /**
     * Establece la cantidad y recalcula automáticamente el subtotal.
     * * @param cantidad La nueva cantidad.
     */
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        recalcularSubtotal();
    }

    /**
     * @return El precio de costo unitario del producto.
     */
    public BigDecimal getPrecioCosto() { 
        return precioCosto; 
    }
    
    /**
     * Establece el precio de costo y recalcula automáticamente el subtotal.
     * * @param precioCosto El nuevo precio de costo.
     */
    public void setPrecioCosto(BigDecimal precioCosto) { 
        this.precioCosto = precioCosto; 
        recalcularSubtotal();
    }

    /**
     * @return El subtotal calculado para esta línea de detalle.
     */
    public BigDecimal getSubtotal() { 
        return subtotal; 
    }
    
    /**
     * @param subtotal El nuevo subtotal manual a establecer.
     */
    public void setSubtotal(BigDecimal subtotal) { 
        this.subtotal = subtotal; 
    }

    /**
     * Método interno para actualizar el subtotal en base a cambios de cantidad o precio.
     */
    private void recalcularSubtotal() {
        if (this.precioCosto != null && this.cantidad > 0) {
            this.subtotal = this.precioCosto.multiply(BigDecimal.valueOf(this.cantidad));
        }
    }
}