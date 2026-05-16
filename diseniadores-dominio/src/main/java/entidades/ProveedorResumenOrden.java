/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author ERICK
 */
public class ProveedorResumenOrden {
    private String idProveedor; 
    private String nombre;
    private String telefono;
    private String gmail;

    public ProveedorResumenOrden(String idProveedor, String nombre, String telefono, String gmail) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.gmail = gmail;
    }

    public ProveedorResumenOrden() {
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    @Override
    public String toString() {
        return "ProveedorResumenOrden{" + "idProveedor=" + idProveedor + ", nombre=" + nombre + ", telefono=" + telefono + ", gmail=" + gmail + '}';
    }
}
