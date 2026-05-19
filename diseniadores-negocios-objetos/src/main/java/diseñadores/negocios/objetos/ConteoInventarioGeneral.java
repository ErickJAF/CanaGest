package diseñadores.negocios.objetos;

import adaptadores.ConteoInventarioGeneralNegocioAdapter;
import diseñadores.negocios.dto.ConteoInventarioGeneralDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Clase de Objeto de Negocio (BO) encargada de gestionar el flujo unificado
 * de sesiones globales de auditoría de inventario y sus métricas analíticas.
 * 
 * @author ERICK
 */
public class ConteoInventarioGeneral {

    private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
    private static final ConteoInventarioGeneralNegocioAdapter ADAPTADOR = new ConteoInventarioGeneralNegocioAdapter();

    /**
     * Recupera el historial completo de sesiones globales de auditoría de la base de datos
     * y las transforma en DTOs maestros aptos para ser mostrados en los paneles generales de la UI.
     */
    public static List<ConteoInventarioGeneralDTO> obtenerTodos() throws PersistenciaException {
        // 1. Buscamos la lista de auditorías generales en formato de dominio limpio
        List<entidades.ConteoInventarioGeneral> listaDominio = PERSISTENCIA.obtenerConteosInventarioGenerales();
        // 2. Las mapeamos en lote y retornamos a la interfaz gráfica
        return ADAPTADOR.listaADTO(listaDominio);
    }

    /**
     * Busca una sesión de auditoría masiva específica utilizando su código unificado.
     */
    public static ConteoInventarioGeneralDTO obtenerPorCodigo(String codigoGeneral) throws PersistenciaException {
        // 1. Buscamos el documento raíz en MongoDB por su código general de auditoría
        entidades.ConteoInventarioGeneral dominio = PERSISTENCIA.obtenerConteoInventarioGeneralPorCodigo(codigoGeneral);
        // 2. Lo retornamos adaptado de forma segura a DTO maestro (si no existe, maneja el null)
        return ADAPTADOR.aDTO(dominio);
    }

    /**
     * Manda a registrar una nueva sesión global de auditoría con todos sus sub-conteos e índices.
     */
    public static void guardar(ConteoInventarioGeneralDTO conteoGeneral) throws PersistenciaException {
        if (conteoGeneral == null) return;
        // 1. Convertimos el paquete de datos de la UI al documento de dominio estructurado
        entidades.ConteoInventarioGeneral dominio = ADAPTADOR.aDominio(conteoGeneral);
        // 2. Lo enviamos a guardar de forma atómica en la colección unificada de MongoDB
        PERSISTENCIA.guardarConteoInventarioGeneral(dominio);
    }

    /**
     * Actualiza la información, contadores o estado de verificación de una sesión global existente.
     */
    public static void actualizar(ConteoInventarioGeneralDTO conteoGeneral) throws PersistenciaException {
        if (conteoGeneral == null) return;
        // 1. Convertimos los cambios actuales del DTO maestro a la entidad de dominio
        entidades.ConteoInventarioGeneral dominio = ADAPTADOR.aDominio(conteoGeneral);
        // 2. Aplicamos la actualización completa en la persistencia
        PERSISTENCIA.actualizarConteoInventarioGeneral(dominio);
    }

    /**
     * Elimina del historial una sesión completa de auditoría mediante su código único general.
     */
    public static void eliminar(String codigoGeneral) throws PersistenciaException {
        if (codigoGeneral == null || codigoGeneral.trim().isEmpty()) return;
        // Se ejecuta la remoción directa del documento maestro y sus sub-conteos embebidos en la BD
        PERSISTENCIA.eliminarConteoInventarioGeneral(codigoGeneral);
    }
}