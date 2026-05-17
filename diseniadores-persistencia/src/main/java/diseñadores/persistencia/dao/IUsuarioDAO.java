package diseñadores.persistencia.dao;

import entidades.Usuario;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia permitidas para la gestión
 * de Usuarios en el sistema utilizando entidades del dominio limpio.
 * * @author ERICK
 */
public interface IUsuarioDAO {

    /**
     * Recupera el catálogo completo de usuarios registrados en el sistema.
     * * @return Lista con todos los usuarios encontrados en dominio.
     * @throws PersistenciaException Si ocurre un error durante la consulta en MongoDB.
     */
    List<Usuario> obtenerTodos() throws PersistenciaException;

    /**
     * Busca un usuario específico mediante su identificador único de cuenta (idUsuario).
     * * @param idUsuario El identificador o login único del usuario.
     * @return El objeto de dominio del usuario encontrado, o null si no existe.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    Usuario obtenerPorIdUsuario(String idUsuario) throws PersistenciaException;

    /**
     * Registra un nuevo usuario de forma permanente en la base de datos.
     * * @param usuario El objeto de dominio con la información del usuario a registrar.
     * @throws PersistenciaException Si el identificador ya existe o falla MongoDB.
     */
    void guardar(Usuario usuario) throws PersistenciaException;

    /**
     * Reemplaza o actualiza los datos de un usuario existente en el sistema.
     * * @param usuario El objeto de dominio con la información actualizada.
     * @throws PersistenciaException Si el usuario no existe o la actualización falla.
     */
    void actualizar(Usuario usuario) throws PersistenciaException;

    /**
     * Elimina del sistema a un usuario usando su identificador único.
     * * @param idUsuario El identificador de cuenta del usuario que se desea remover.
     * @throws PersistenciaException Si el usuario no existe o falla la base de datos.
     */
    void eliminar(String idUsuario) throws PersistenciaException;
}