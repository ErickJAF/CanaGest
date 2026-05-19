/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author ERICK
 */
public class ConteoInventarioGeneralMongoEntidad {

    @BsonId
    private ObjectId id;
    private String codigoGeneral;
    private String fechaRegistro;
    private Boolean verificadoGlobal;

    private int cantidadVerificados;
    private int cantidadNoVerificados;
    private int diferenciasTotales;

    private List<ConteoInventarioMongoEntidad> todosLosConteos;

    public ConteoInventarioGeneralMongoEntidad() {
    }

    public ConteoInventarioGeneralMongoEntidad(ObjectId id, String codigoGeneral, String fechaRegistro, Boolean verificadoGlobal, int cantidadVerificados, int cantidadNoVerificados, int diferenciasTotales, List<ConteoInventarioMongoEntidad> todosLosConteos) {
        this.id = id;
        this.codigoGeneral = codigoGeneral;
        this.fechaRegistro = fechaRegistro;
        this.verificadoGlobal = verificadoGlobal;
        this.cantidadVerificados = cantidadVerificados;
        this.cantidadNoVerificados = cantidadNoVerificados;
        this.diferenciasTotales = diferenciasTotales;
        this.todosLosConteos = todosLosConteos;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCodigoGeneral() {
        return codigoGeneral;
    }

    public void setCodigoGeneral(String codigoGeneral) {
        this.codigoGeneral = codigoGeneral;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getVerificadoGlobal() {
        return verificadoGlobal;
    }

    public void setVerificadoGlobal(Boolean verificadoGlobal) {
        this.verificadoGlobal = verificadoGlobal;
    }

    public int getCantidadVerificados() {
        return cantidadVerificados;
    }

    public void setCantidadVerificados(int cantidadVerificados) {
        this.cantidadVerificados = cantidadVerificados;
    }

    public int getCantidadNoVerificados() {
        return cantidadNoVerificados;
    }

    public void setCantidadNoVerificados(int cantidadNoVerificados) {
        this.cantidadNoVerificados = cantidadNoVerificados;
    }

    public int getDiferenciasTotales() {
        return diferenciasTotales;
    }

    public void setDiferenciasTotales(int diferenciasTotales) {
        this.diferenciasTotales = diferenciasTotales;
    }

    public List<ConteoInventarioMongoEntidad> getTodosLosConteos() {
        return todosLosConteos;
    }

    public void setTodosLosConteos(List<ConteoInventarioMongoEntidad> todosLosConteos) {
        this.todosLosConteos = todosLosConteos;
    }

    
}