/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

/**
 *
 * @author ERICK
 */
public class DomicilioMongoEntidad {
    private String calle;
    private String numero;
    private String colonia;

    public DomicilioMongoEntidad(String calle, String numero, String colonia) {
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
    }

    public DomicilioMongoEntidad() {
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    @Override
    public String toString() {
        return "DomicilioMongoEntidad{" + "calle=" + calle + ", numero=" + numero + ", colonia=" + colonia + '}';
    }
}
