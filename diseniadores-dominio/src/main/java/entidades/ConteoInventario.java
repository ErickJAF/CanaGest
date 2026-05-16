/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 * Clase del dominio limpio que representa una auditoría física o Conteo de Inventario.
 * Modela el acto de verificar las existencias físicas de un producto en el almacén 
 * por parte de un usuario auditor, permitiendo registrar observaciones y calcular 
 * las discrepancias contra los datos registrados en el sistema.
 *
 * @author ERICK
 */
public class ConteoInventario {
    private String id;
    private String codigo; 
    private String fecha;
    private String estado;

    private UsuarioResumen usuario;
    private ProductoResumenInventario producto;
    
    private int cantidadContada;
    private int diferencia;
    private String estadoConteo;
    private String detailleConteo;

    public ConteoInventario(String id, String codigo, String fecha, String estado, UsuarioResumen usuario, ProductoResumenInventario producto, int cantidadContada, int diferencia, String estadoConteo, String detailleConteo) {
        this.id = id;
        this.codigo = codigo;
        this.fecha = fecha;
        this.estado = estado;
        this.usuario = usuario;
        this.producto = producto;
        this.cantidadContada = cantidadContada;
        this.diferencia = diferencia;
        this.estadoConteo = estadoConteo;
        this.detailleConteo = detailleConteo;
    }

    public ConteoInventario() {
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

    public UsuarioResumen getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumen usuario) {
        this.usuario = usuario;
    }

    public ProductoResumenInventario getProducto() {
        return producto;
    }

    public void setProducto(ProductoResumenInventario producto) {
        this.producto = producto;
    }

    public int getCantidadContada() {
        return cantidadContada;
    }

    public void setCantidadContada(int cantidadContada) {
        this.cantidadContada = cantidadContada;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstadoConteo() {
        return estadoConteo;
    }

    public void setEstadoConteo(String estadoConteo) {
        this.estadoConteo = estadoConteo;
    }

    public String getDetailleConteo() {
        return detailleConteo;
    }

    public void setDetailleConteo(String detailleConteo) {
        this.detailleConteo = detailleConteo;
    }

    @Override
    public String toString() {
        return "ConteoInventario{" + "id=" + id + ", codigo=" + codigo + ", fecha=" + fecha + ", estado=" + estado + ", usuario=" + usuario + ", producto=" + producto + ", cantidadContada=" + cantidadContada + ", diferencia=" + diferencia + ", estadoConteo=" + estadoConteo + ", detailleConteo=" + detailleConteo + '}';
    }
}
