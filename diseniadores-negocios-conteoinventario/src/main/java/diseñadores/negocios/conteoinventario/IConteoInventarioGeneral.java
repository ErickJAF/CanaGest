package diseñadores.negocios.conteoinventario;

import diseñadores.negocios.dto.ConteoInventarioGeneralDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 * Interfaz que define las operaciones permitidas para el subsistema de 
 * auditorías globales y control unificado de conteos de inventario.
 * 
 * @author ERICK
 */
public interface IConteoInventarioGeneral {

    /**
     * Crea y pre-registra una sesión global de auditoría en estado de borrador.
     */
    void crearSesionAuditoria(ConteoInventarioGeneralDTO sesion) throws NegocioException;

    /**
     * Consolida la sesión de auditoría en la base de datos e impacta los
     * stocks físicos directamente en el catálogo del sistema.
     */
    void registrarYAplicarAuditoriaGlobal(ConteoInventarioGeneralDTO sesion) throws NegocioException;

    /**
     * Recupera el historial completo de las sesiones masivas de inventario.
     */
    List<ConteoInventarioGeneralDTO> obtenerHistorialSesiones() throws NegocioException;

    /**
     * Busca los detalles de una sesión de auditoría unificada mediante su código.
     */
    ConteoInventarioGeneralDTO buscarSesionPorCodigo(String codigoGeneral) throws NegocioException;
    
    /**
     * Guarda modificaciones progresivas sobre los ítems (comentarios y firmas) 
     * sin cerrar la auditoría ni alterar los stocks del catálogo.
     */
    void guardarProgresoAuditoria(ConteoInventarioGeneralDTO sesion) throws NegocioException;
}