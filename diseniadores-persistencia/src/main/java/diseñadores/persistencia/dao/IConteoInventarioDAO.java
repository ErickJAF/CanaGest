/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package diseñadores.persistencia.dao;

import entidades.ConteoInventario;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de auditorías y conteos físicos de inventario en el sistema.
 * 
 * @author ERICK
 */
public interface IConteoInventarioDAO {

    /**
     * Recupera el histórico completo de conteos de inventario registrados.
     * * @return Lista con todos los conteos de inventario en el dominio limpio.
     * @throws PersistenciaException Si ocurre un error de lectura en MongoDB.
     */
    List<ConteoInventario> obtenerTodos() throws PersistenciaException;

    /**
     * Busca un registro de conteo físico específico a través de su código único de auditoría.
     * * @param codigo El código identificador único del conteo de inventario.
     * @return El objeto de dominio del conteo, o null si no se encuentra registrado.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    ConteoInventario obtenerPorCodigo(String codigo) throws PersistenciaException;

    /**
     * Registra de forma permanente una nueva auditoría o conteo físico en la base de datos.
     * * @param conteo El objeto de dominio con los datos del conteo a guardar.
     * @throws PersistenciaException Si el código ya existe o falla el motor de MongoDB.
     */
    void guardar(ConteoInventario conteo) throws PersistenciaException;

    /**
     * Reemplaza o actualiza el registro de un conteo de inventario existente.
     * * @param conteo El objeto de dominio del conteo con los cambios aplicados.
     * @throws PersistenciaException Si el registro no existe o la actualización falla.
     */
    void actualizar(ConteoInventario conteo) throws PersistenciaException;

    /**
     * Elimina una auditoría de conteo del sistema usando su código identificador.
     * * @param codigo El código del conteo de inventario que se desea remover.
     * @throws PersistenciaException Si el registro no existe o falla la base de datos.
     */
    void eliminar(String codigo) throws PersistenciaException;
}
