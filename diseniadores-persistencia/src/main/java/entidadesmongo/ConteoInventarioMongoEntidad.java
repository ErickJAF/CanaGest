/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 * Entidad de persistencia que representa un documento principal en la colección "conteosInventario".
 * 
 * Registra los eventos de auditoría física de mercancía en el almacén. Embebe al usuario 
 * auditor y al producto verificado para calcular discrepancias físicas directas de manera eficiente.
 * 
 * @author ERICK
 */
/**

 */
public class ConteoInventarioMongoEntidad {

    private String codigo;
    private String fecha;
    private Boolean estado;

    private UsuarioResumenMongoEntidad usuario;
    private ProductoResumenInventarioMongoEntidad producto;
    
    private int cantidadContada;
    private int diferencia;
    private String comentario;

    public ConteoInventarioMongoEntidad(String codigo, String fecha, Boolean estado, UsuarioResumenMongoEntidad usuario, ProductoResumenInventarioMongoEntidad producto, int cantidadContada, int diferencia, String comentario) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.estado = estado;
        this.usuario = usuario;
        this.producto = producto;
        this.cantidadContada = cantidadContada;
        this.diferencia = diferencia;
        this.comentario = comentario;
    }

    public ConteoInventarioMongoEntidad() {
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public UsuarioResumenMongoEntidad getUsuario() {
        return usuario;
    }

    public ProductoResumenInventarioMongoEntidad getProducto() {
        return producto;
    }

    public int getCantidadContada() {
        return cantidadContada;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public String getComentario() {
        return comentario;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setUsuario(UsuarioResumenMongoEntidad usuario) {
        this.usuario = usuario;
    }

    public void setProducto(ProductoResumenInventarioMongoEntidad producto) {
        this.producto = producto;
    }

    public void setCantidadContada(int cantidadContada) {
        this.cantidadContada = cantidadContada;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "ConteoInventarioMongoEntidad{" + "codigo=" + codigo + ", fecha=" + fecha + ", estado=" + estado + ", usuario=" + usuario + ", producto=" + producto + ", cantidadContada=" + cantidadContada + ", diferencia=" + diferencia + ", comentario=" + comentario + '}';
    }
}
