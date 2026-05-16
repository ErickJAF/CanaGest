/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class ProveedorResumenOrdenMongoEntidad {
    private String idProveedor;
    private String nombre;
    private String telefono;
    private String gmail;

    public ProveedorResumenOrdenMongoEntidad(String idProveedor, String nombre, String telefono, String gmail) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.gmail = gmail;
    }

    public ProveedorResumenOrdenMongoEntidad() {
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGmail() {
        return gmail;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    @Override
    public String toString() {
        return "ProveedorResumenOrdenMongoEntidad{" + "idProveedor=" + idProveedor + ", nombre=" + nombre + ", telefono=" + telefono + ", gmail=" + gmail + '}';
    }
}
