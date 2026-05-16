/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class ProveedorMongoEntidad {
    private String codigo;
    private String nombre;
    private String telefono;
    private String gmail;
    private String terminosPago;
    private DomicilioMongoEntidad domicilio;

    public ProveedorMongoEntidad(String codigo, String nombre, String telefono, String gmail, String terminosPago, DomicilioMongoEntidad domicilio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.gmail = gmail;
        this.terminosPago = terminosPago;
        this.domicilio = domicilio;
    }

    public ProveedorMongoEntidad() {
    }

    public String getCodigo() {
        return codigo;
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

    public String getTerminosPago() {
        return terminosPago;
    }

    public DomicilioMongoEntidad getDomicilio() {
        return domicilio;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public void setTerminosPago(String terminosPago) {
        this.terminosPago = terminosPago;
    }

    public void setDomicilio(DomicilioMongoEntidad domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return "ProveedorMongoEntidad{" + "codigo=" + codigo + ", nombre=" + nombre + ", telefono=" + telefono + ", gmail=" + gmail + ", terminosPago=" + terminosPago + ", domicilio=" + domicilio + '}';
    }
}
