package entidades;

import java.util.List;

/**
 * Clase de dominio que representa el modelo de negocio para un control unificado
 * o sesión global de auditoría de inventario.
 * 
 * Está completamente desacoplada de tecnologías de persistencia específicas.
 * 
 * @author ERICK
 */
public class ConteoInventarioGeneral {

    private String id; // Representación en String del identificador único
    private String codigoGeneral;
    private String fechaRegistro;
    private Boolean verificadoGlobal;

    private int cantidadVerificados;
    private int cantidadNoVerificados;
    private int diferenciasTotales;

    // Lista de objetos de dominio individuales (asegúrate de tener también la clase de dominio para cada conteo)
    private List<ConteoInventario> todosLosConteos;

    public ConteoInventarioGeneral() {
    }

    public ConteoInventarioGeneral(String id, String codigoGeneral, String fechaRegistro, Boolean verificadoGlobal, int cantidadVerificados, int cantidadNoVerificados, int diferenciasTotales, List<ConteoInventario> todosLosConteos) {
        this.id = id;
        this.codigoGeneral = codigoGeneral;
        this.fechaRegistro = fechaRegistro;
        this.verificadoGlobal = verificadoGlobal;
        this.cantidadVerificados = cantidadVerificados;
        this.cantidadNoVerificados = cantidadNoVerificados;
        this.diferenciasTotales = diferenciasTotales;
        this.todosLosConteos = todosLosConteos;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<ConteoInventario> getTodosLosConteos() {
        return todosLosConteos;
    }

    public void setTodosLosConteos(List<ConteoInventario> todosLosConteos) {
        this.todosLosConteos = todosLosConteos;
    }

    @Override
    public String toString() {
        return "ConteoInventarioGeneral{" + "id=" + id + ", codigoGeneral=" + codigoGeneral + ", verificadoGlobal=" + verificadoGlobal + ", cantidadVerificados=" + cantidadVerificados + ", cantidadNoVerificados=" + cantidadNoVerificados + ", diferenciasTotales=" + diferenciasTotales + '}';
    }
}