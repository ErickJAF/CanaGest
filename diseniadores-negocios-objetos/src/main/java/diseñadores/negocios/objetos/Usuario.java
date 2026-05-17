package diseñadores.negocios.objetos;

import adaptadores.UsuarioNegocioAdapter;
import diseñadores.negocios.dto.UsuarioDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.Optional;

/**
 * Puente de conexión directo para operaciones de Usuarios y Autenticación.
 * @author ERICK
 */
public class Usuario {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final UsuarioNegocioAdapter ADAPTADOR = new UsuarioNegocioAdapter();

  public static List<UsuarioDTO> obtenerTodos() throws PersistenciaException {
    // 1. Recupera la lista de entidades de dominio limpio
    List<entidades.Usuario> listaDominio = PERSISTENCIA.obtenerUsuarios();
    // 2. Las mapea a la lista de DTOs para la UI
    return ADAPTADOR.listaADTO(listaDominio);
  }

  public static Optional<UsuarioDTO> autenticar(String nombre, String contrasena) throws PersistenciaException {
    if (nombre == null || contrasena == null) return Optional.empty();
    
    // Normalizamos el nombre para buscar por el ID seguro (minúsculas y sin espacios)
    String idUsuarioBusqueda = nombre.toLowerCase().trim();
    
    // 1. Buscamos en la persistencia por el IdUsuario exacto
    Optional<entidades.Usuario> resultadoDominio = PERSISTENCIA.obtenerUsuarioPorIdUsuario(idUsuarioBusqueda);
    
    // 2. Si existe, lo transformamos a DTO usando el adaptador y filtramos por contraseña
    return resultadoDominio
        .map(ADAPTADOR::aDTO)
        .filter(u -> u.getContrasena().equals(contrasena));
  }

  public static void guardar(UsuarioDTO usuarioDTO) throws PersistenciaException {
    // 1. Transforma el DTO de la UI al dominio limpio de Mongo
    entidades.Usuario usuarioDominio = ADAPTADOR.aDominio(usuarioDTO);
    // 2. Lo manda a guardar a la base de datos
    PERSISTENCIA.guardarUsuario(usuarioDominio);
  }

  public static void actualizar(UsuarioDTO usuarioDTO) throws PersistenciaException {
    // 1. Transforma a dominio limpio
    entidades.Usuario usuarioDominio = ADAPTADOR.aDominio(usuarioDTO);
    // 2. Ejecuta la actualización en la persistencia
    PERSISTENCIA.actualizarUsuario(usuarioDominio);
  }

  public static void eliminar(String nombre) throws PersistenciaException {
    if (nombre == null) return;
    
    // Normalizamos el nombre para mandar a eliminar por el idUsuario exacto que usa Mongo
    String idUsuarioEliminar = nombre.toLowerCase().trim();
    PERSISTENCIA.eliminarUsuario(idUsuarioEliminar);
  }

}