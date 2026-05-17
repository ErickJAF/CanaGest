package diseñadores.negocios.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompraDTO {

    private String numero;
    private String fecha;
    private String estado;
    private BigDecimal total;
    private ProveedorDTO proveedor;
    private UsuarioDTO usuario;
    private List<DetalleOrdenCompraDTO> productos;

    public OrdenCompraDTO() {
        this.productos = new ArrayList<>();
    }

    public OrdenCompraDTO(String numero, String fecha, String estado, BigDecimal total, ProveedorDTO proveedor, UsuarioDTO usuario, List<DetalleOrdenCompraDTO> productos) {
        this.numero = numero;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.productos = productos;
    }
    
    public OrdenCompraDTO(String numero, String fecha, ProveedorDTO proveedor, String estado, int cantidad, BigDecimal total) {
        this.numero = numero;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.estado = estado;
        this.total = total;
        this.productos = new ArrayList<>();
        // Como este constructor viejo no pedía usuario, le creamos uno por defecto temporal
        this.usuario = new UsuarioDTO("Sistema", "1234", UsuarioRol.ADMINISTRADOR);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ProveedorDTO getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorDTO proveedor) {
        this.proveedor = proveedor;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public List<DetalleOrdenCompraDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleOrdenCompraDTO> productos) {
        this.productos = productos;
    }
    
    public int getCantidadProductos() {
        return this.productos != null ? this.productos.size() : 0;
    }

    @Override
    public String toString() {
        return "OrdenCompraDTO{" + "numero=" + numero + ", fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", proveedor=" + proveedor + ", usuario=" + usuario + ", productos=" + productos + '}';
    }
}
