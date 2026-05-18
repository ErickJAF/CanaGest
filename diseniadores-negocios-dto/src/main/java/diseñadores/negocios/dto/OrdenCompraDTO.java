package diseñadores.negocios.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Transferencia de Datos (DTO) que agrupa la información principal
 * de una orden de compra, incluyendo sus relaciones (proveedor, usuario y detalles).
 * * @author icoro
 */
public class OrdenCompraDTO {

    private String numero;
    private String fecha;
    private String estado;
    private BigDecimal total;
    private ProveedorDTO proveedor;
    private UsuarioDTO usuario;
    private List<DetalleOrdenCompraDTO> productos;

    /**
     * Constructor por defecto que inicializa la lista de productos vacía.
     */
    public OrdenCompraDTO() {
        this.productos = new ArrayList<>();
    }

    /**
     * Constructor completo para instanciar una orden de compra con todos sus atributos.
     * * @param numero    Código único de la orden.
     * @param fecha     Fecha de emisión de la orden.
     * @param estado    Estado de gestión (ej. Pendiente, Aprobada).
     * @param total     Costo total calculado de la orden.
     * @param proveedor DTO del proveedor asociado.
     * @param usuario   DTO del usuario que genera la orden.
     * @param productos Lista con los detalles de los productos ordenados.
     */
    public OrdenCompraDTO(String numero, String fecha, String estado, BigDecimal total, ProveedorDTO proveedor, UsuarioDTO usuario, List<DetalleOrdenCompraDTO> productos) {
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.productos = productos;
    }
    
    /**
     * Constructor simplificado para compatibilidad hacia atrás en la creación de órdenes.
     * * @param numero    Código de la orden.
     * @param fecha     Fecha de emisión.
     * @param proveedor DTO del proveedor.
     * @param estado    Estado de la orden.
     * @param cantidad  Cantidad global (obsoleto si se usan los detalles directamente).
     * @param total     Total de la orden.
     */
    public OrdenCompraDTO(String numero, String fecha, ProveedorDTO proveedor, String estado, int cantidad, BigDecimal total) {
        this.numero = numero;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.estado = estado;
        this.total = total;
        this.productos = new ArrayList<>();
        // Se crea un usuario por defecto temporal para evitar nulos
        this.usuario = new UsuarioDTO("Sistema", "1234", UsuarioRol.ADMINISTRADOR);
    }

    /** @return El número o código de la orden. */
    public String getNumero() { return numero; }

    /** @param numero El número a asignar a la orden. */
    public void setNumero(String numero) { this.numero = numero; }

    /** @return La fecha de la orden en formato texto. */
    public String getFecha() { return fecha; }

    /** @param fecha La nueva fecha a asignar. */
    public void setFecha(String fecha) { this.fecha = fecha; }

    /** @return El estado de la orden. */
    public String getEstado() { return estado; }

    /** @param estado El nuevo estado a establecer. */
    public void setEstado(String estado) { this.estado = estado; }

    /** @return El costo total global de la orden. */
    public BigDecimal getTotal() { return total; }

    /** @param total El nuevo costo total. */
    public void setTotal(BigDecimal total) { this.total = total; }

    /** @return El proveedor asociado a esta orden. */
    public ProveedorDTO getProveedor() { return proveedor; }

    /** @param proveedor El nuevo proveedor a asignar. */
    public void setProveedor(ProveedorDTO proveedor) { this.proveedor = proveedor; }

    /** @return El usuario encargado de la orden. */
    public UsuarioDTO getUsuario() { return usuario; }

    /** @param usuario El nuevo usuario encargado. */
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

    /** @return La lista de líneas de detalle (productos) de la orden. */
    public List<DetalleOrdenCompraDTO> getProductos() { return productos; }

    /** @param productos La nueva lista de productos. */
    public void setProductos(List<DetalleOrdenCompraDTO> productos) { this.productos = productos; }
    
    /**
     * Método auxiliar que devuelve el total de líneas de detalle en la orden.
     * * @return El tamaño de la lista de productos.
     */
    public int getCantidadProductos() {
        return this.productos != null ? this.productos.size() : 0;
    }

    @Override
    public String toString() {
        return "OrdenCompraDTO{" + "numero=" + numero + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", proveedor=" + proveedor + ", usuario=" + usuario + ", productos=" + productos + '}';
    }
    /**
     * Método auxiliar para obtener el nombre del proveedor directamente.
     * @return El nombre del proveedor o un texto por defecto si es nulo.
     */
    public String getProveedorNombre() {
        if (this.proveedor != null) {
            return this.proveedor.getNombre(); // Cambia getNombre() si tu ProveedorDTO usa getRazonSocial() u otro método
        }
        return "Sin Proveedor";
    }
}