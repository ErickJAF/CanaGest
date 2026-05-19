package diseñadores.persistencia.dao;

import entidades.ConteoInventarioGeneral;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia para el control
 * unificado de Conteos Generales de Inventario.
 * 
 * @author ERICK
 */
public interface IConteoInventarioGeneralDAO {

    /**
     * Recupera el historial completo de todas las sesiones de auditoría global.
     * 
     * @return Una lista con todos los conteos generales en formato de dominio.
     * @throws PersistenciaException Si ocurre un error físico en el servidor de base de datos.
     */
    List<ConteoInventarioGeneral> obtenerTodos() throws PersistenciaException;

    /**
     * Busca una sesión de auditoría global específica mediante su código comercial.
     * 
     * @param codigoGeneral Código único de la sesión (ej. "AUD-2026-05-18").
     * @return El objeto de dominio correspondiente, o null si no se encuentra.
     * @throws PersistenciaException Si la clave de búsqueda es inválida o falla la conexión.
     */
    ConteoInventarioGeneral obtenerPorCodigoGeneral(String codigoGeneral) throws PersistenciaException;

    /**
     * Registra un nuevo documento maestro de inventario unificado en la base de datos.
     * 
     * @param conteoGeneral Objeto de dominio con los datos y submódulos a persistir.
     * @throws PersistenciaException Si el código ya existe o se violan reglas de integridad.
     */
    void guardar(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException;

    /**
     * Actualiza el documento maestro completo en MongoDB (reemplazo atómico).
     * Sobreescribe los totales calculados y el arreglo embebido de sub-conteos.
     * 
     * @param conteoGeneral Objeto de dominio actualizado.
     * @throws PersistenciaException Si el registro no existe o falla la operación en el driver.
     */
    void actualizar(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException;

    /**
     * Elimina de manera permanente una auditoría global y todos sus conteos asociados.
     * 
     * @param codigoGeneral Código único de la sesión a dar de baja.
     * @throws PersistenciaException Si el registro no existe o no se puede procesar la baja.
     */
    void eliminar(String codigoGeneral) throws PersistenciaException;
}