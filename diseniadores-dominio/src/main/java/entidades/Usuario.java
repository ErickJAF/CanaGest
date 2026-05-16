/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 * Clase del dominio limpio que representa a un Usuario del sistema.
 * Almacena la información de identidad, credenciales y privilegios de acceso
 * de los empleados. Al pertenecer al dominio, no contiene anotaciones tecnológicas 
 * de bases de datos y maneja sus identificadores estrictamente como cadenas de texto.
 * 
 * @author ERICK
 */
public class Usuario {
    private String id; 
    private String idUsuario; 
    private String nombre;
    private String contraseña;
    private String rol;

    public Usuario(String id, String idUsuario, String nombre, String contraseña, String rol) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public Usuario() {
    }

    public String getId() {
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

    public void setId(String id) {
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
        return "Usuario{" + "id=" + id + ", idUsuario=" + idUsuario + ", nombre=" + nombre + ", contrase\u00f1a=" + contraseña + ", rol=" + rol + '}';
    }
}
