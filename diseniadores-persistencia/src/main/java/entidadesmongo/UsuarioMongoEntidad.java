/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * Entidad de persistencia que representa un documento principal en la colección "usuarios" de MongoDB.
 * 
 * Esta clase almacena las credenciales y roles del personal del sistema. Se mantiene como una 
 * colección independiente ya que su información es referenciada y reutilizada en múltiples 
 * eventos del sistema, tales como auditorías de inventario y órdenes de compra.
 * 
 * @author ERICK
 */
public class UsuarioMongoEntidad {

    @BsonId
    private ObjectId id;
    private String idUsuario;
    private String nombre;
    private String contraseña;
    private String rol;

    public UsuarioMongoEntidad(ObjectId id, String idUsuario, String nombre, String contraseña, String rol) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public UsuarioMongoEntidad() {
    }

    public ObjectId getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UsuarioMongoEntidad{" + "id=" + id + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", contrase\u00f1a=" + contraseña + ", rol=" + rol + '}';
    }
}