package adaptadores;

import diseñadores.negocios.dto.ConteoInventarioGeneralDTO;
import diseñadores.negocios.dto.ItemConteoDTO;
import entidades.ConteoInventarioGeneral;
import entidades.ConteoInventario;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de la capa de Negocio encargado de las conversiones bidireccionales
 * entre el DTO maestro de la interfaz de usuario (ConteoInventarioGeneralDTO) 
 * y la entidad raíz de dominio (ConteoInventarioGeneral).
 * 
 * Reutiliza el ItemConteoNegocioAdapter para delegar la conversión de los sub-conteos.
 * 
 * @author ERICK
 */
public class ConteoInventarioGeneralNegocioAdapter {

    private final ItemConteoNegocioAdapter itemAdapter;

    /**
     * Constructor por defecto que inicializa el adaptador secundario 
     * para mapear la colección interna de ítems.
     */
    public ConteoInventarioGeneralNegocioAdapter() {
        this.itemAdapter = new ItemConteoNegocioAdapter();
    }

    /**
     * Transforma un ConteoInventarioGeneralDTO de la UI a la entidad pura de Dominio.
     * Mapea de forma directa y fiel todos los campos estadísticos y métricas de auditoría.
     */
    public ConteoInventarioGeneral aDominio(ConteoInventarioGeneralDTO dto) {
        if (dto == null) return null;

        ConteoInventarioGeneral dominio = new ConteoInventarioGeneral();
        
        // Mapeo de identificadores y control de auditoría
        dominio.setId(dto.getId());
        dominio.setCodigoGeneral(dto.getCodigoGeneral());
        dominio.setFechaRegistro(dto.getFechaRegistro());
        dominio.setVerificadoGlobal(dto.getVerificadoGlobal());
        
        // Mapeo de métricas acumuladas y contadores
        dominio.setCantidadVerificados(dto.getCantidadVerificados());
        dominio.setCantidadNoVerificados(dto.getCantidadNoVerificados());
        dominio.setDiferenciasTotales(dto.getDiferenciasTotales());

        // Mapeo de la lista interna delegando la transformación de cada elemento
        List<ConteoInventario> listaDominio = new ArrayList<>();
        if (dto.getTodosLosConteos() != null) {
            for (ItemConteoDTO itemDto : dto.getTodosLosConteos()) {
                listaDominio.add(itemAdapter.aDominio(itemDto));
            }
        }
        dominio.setTodosLosConteos(listaDominio);

        return dominio;
    }

    /**
     * Transforma una entidad ConteoInventarioGeneral de Dominio al DTO maestro requerido por la UI.
     * Restaura los estados de control y la lista unificada de registros para los componentes visuales.
     */
    public ConteoInventarioGeneralDTO aDTO(ConteoInventarioGeneral dominio) {
        if (dominio == null) return null;

        ConteoInventarioGeneralDTO dto = new ConteoInventarioGeneralDTO();
        
        // Recuperación de identificadores y cabecera general
        dto.setId(dominio.getId());
        dto.setCodigoGeneral(dominio.getCodigoGeneral());
        dto.setFechaRegistro(dominio.getFechaRegistro());
        dto.setVerificadoGlobal(dominio.getVerificadoGlobal());
        
        // Recuperación de contadores analíticos para los tableros o resúmenes de la UI
        dto.setCantidadVerificados(dominio.getCantidadVerificados());
        dto.setCantidadNoVerificados(dominio.getCantidadNoVerificados());
        dto.setDiferenciasTotales(dominio.getDiferenciasTotales());

        // Recuperación segura de la lista interna traduciéndola a DTOs de presentación
        List<ItemConteoDTO> listaDTO = new ArrayList<>();
        if (dominio.getTodosLosConteos() != null) {
            for (ConteoInventario itemDominio : dominio.getTodosLosConteos()) {
                listaDTO.add(itemAdapter.aDTO(itemDominio));
            }
        }
        dto.setTodosLosConteos(listaDTO);

        return dto;
    }

    /**
     * Convierte una lista completa de sesiones globales de dominio de la BD a una colección de DTOs maestros.
     * Ideal para llenar tablas históricas o paneles de consulta masiva.
     */
    public List<ConteoInventarioGeneralDTO> listaADTO(List<ConteoInventarioGeneral> listaDominio) {
        List<ConteoInventarioGeneralDTO> listaDTO = new ArrayList<>();
        if (listaDominio == null) return listaDTO;
        
        for (ConteoInventarioGeneral cg : listaDominio) {
            listaDTO.add(aDTO(cg));
        }
        return listaDTO;
    }
}