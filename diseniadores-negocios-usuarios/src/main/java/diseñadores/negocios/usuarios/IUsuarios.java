package diseñadores.negocios.usuarios;

import diseñadores.negocios.dto.UsuarioDTO;
import diseñadores.negocios.dto.UsuarioRol;
import excepciones.NegocioException;

import java.util.List;
import java.util.Optional;

public interface IUsuarios {

  Optional<UsuarioDTO> autenticarse(String nombre, String contrasena) throws NegocioException;

  List<UsuarioDTO> obtenerTodos() throws NegocioException;

  void guardarUsuario(UsuarioDTO usuario) throws NegocioException;

  void actualizarUsuario(UsuarioDTO usuario) throws NegocioException;

  void eliminarUsuario(String nombre) throws NegocioException;

  void cambiarRol(String nombre, UsuarioRol nuevoRol) throws NegocioException;

}
