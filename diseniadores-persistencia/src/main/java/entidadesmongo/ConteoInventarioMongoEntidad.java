/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

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

    @BsonId
    private ObjectId id;
    private String codigo;
    private String fecha;
    private String estado;

    private UsuarioResumenMongoEntidad usuario;
    private ProductoResumenInventarioMongoEntidad producto;
    
    private int cantidadContada;
    private int diferencia;
    private String estadoConteo;
    private String detalleConteo;

    public ConteoInventarioMongoEntidad(ObjectId id, String codigo, String fecha, String estado, UsuarioResumenMongoEntidad usuario, ProductoResumenInventarioMongoEntidad producto, int cantidadContada, int diferencia, String estadoConteo, String detalleConteo) {
        this.id = id;
        this.codigo = codigo;
        this.fecha = fecha;
        this.estado = estado;
        this.usuario = usuario;
        this.producto = producto;
        this.cantidadContada = cantidadContada;
        this.diferencia = diferencia;
        this.estadoConteo = estadoConteo;
        this.detalleConteo = detalleConteo;
    }

    public ConteoInventarioMongoEntidad() {
    }

    public ObjectId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
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

    public String getEstadoConteo() {
        return estadoConteo;
    }

    public String getDetalleConteo() {
        return detalleConteo;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
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

    public void setEstadoConteo(String estadoConteo) {
        this.estadoConteo = estadoConteo;
    }

    public void setDetalleConteo(String detalleConteo) {
        this.detalleConteo = detalleConteo;
    }

    @Override
    public String toString() {
        return "ConteoInventarioMongoEntidad{" + "id=" + id + ", codigo=" + codigo + ", fecha=" + fecha + ", estado=" + estado + ", usuario=" + usuario + ", producto=" + producto + ", cantidadContada=" + cantidadContada + ", diferencia=" + diferencia + ", estadoConteo=" + estadoConteo + ", detalleConteo=" + detalleConteo + '}';
    }
}
