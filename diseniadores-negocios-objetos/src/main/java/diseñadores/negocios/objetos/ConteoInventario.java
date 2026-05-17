/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diseñadores.negocios.objetos;

import adaptadores.ItemConteoNegocioAdapter;
import diseñadores.negocios.dto.ItemConteoDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Clase de Objeto de Negocio (BO) encargada de gestionar el flujo de auditorías
 * físicas y conteos de inventario por producto de manera unitaria.
 * 
 * @author ERICK
 */
public class ConteoInventario {

    private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
    private static final ItemConteoNegocioAdapter ADAPTADOR = new ItemConteoNegocioAdapter();

    /**
     * Recupera el historial completo de auditorías físicas de la base de datos
     * y las transforma en DTOs aptos para ser mostrados en la UI.
     */
    public static List<ItemConteoDTO> obtenerTodos() throws PersistenciaException {
        // 1. Buscamos la lista de auditorías en formato de dominio limpio
        List<entidades.ConteoInventario> listaDominio = PERSISTENCIA.obtenerConteosInventario();
        // 2. Las mapeamos y retornamos a la interfaz gráfica
        return ADAPTADOR.listaADTO(listaDominio);
    }

    /**
     * Busca un registro de conteo de inventario específico utilizando su código único.
     */
    public static ItemConteoDTO obtenerPorCodigo(String codigo) throws PersistenciaException {
        // 1. Buscamos el documento en MongoDB por su código único de auditoría
        entidades.ConteoInventario dominio = PERSISTENCIA.obtenerConteoInventarioPorCodigo(codigo);
        // 2. Lo retornamos adaptado de forma segura a DTO (si no existe, el adaptador maneja el null)
        return ADAPTADOR.aDTO(dominio);
    }

    /**
     * Manda a registrar una nueva auditoría física de producto en el almacén.
     */
    public static void guardar(ItemConteoDTO conteo) throws PersistenciaException {
        if (conteo == null) return;
        // 1. Convertimos el renglón de la UI al documento de dominio estructurado
        entidades.ConteoInventario dominio = ADAPTADOR.aDominio(conteo);
        // 2. Lo enviamos a guardar de forma atómica a MongoDB
        PERSISTENCIA.guardarConteoInventario(dominio);
    }

    /**
     * Actualiza la información o estado de una auditoría física existente.
     */
    public static void actualizar(ItemConteoDTO conteo) throws PersistenciaException {
        if (conteo == null) return;
        // 1. Convertimos los cambios actuales del DTO a la entidad de dominio
        entidades.ConteoInventario dominio = ADAPTADOR.aDominio(conteo);
        // 2. Aplicamos la actualización en la persistencia
        PERSISTENCIA.actualizarConteoInventario(dominio);
    }

    /**
     * Elimina del historial un registro de conteo mediante su código único.
     */
    public static void eliminar(String codigo) throws PersistenciaException {
        if (codigo == null || codigo.trim().isEmpty()) return;
        // Se ejecuta la eliminación directa en la base de datos usando el String del folio
        PERSISTENCIA.eliminarConteoInventario(codigo);
    }
}
