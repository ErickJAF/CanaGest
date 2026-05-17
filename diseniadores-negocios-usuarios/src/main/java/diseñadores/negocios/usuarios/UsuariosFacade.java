package diseñadores.negocios.usuarios;

import diseñadores.negocios.dto.UsuarioDTO;
import diseñadores.negocios.dto.UsuarioRol;
import excepciones.NegocioException;

import java.util.List;
import java.util.Optional;

public class UsuariosFacade implements IUsuarios {

  private final UsuariosControl control;

  public UsuariosFacade() {
    this.control = new UsuariosControl();
  }

  public UsuariosFacade(UsuariosControl control) {
    this.control = control;
  }

  @Override
  public Optional<UsuarioDTO> autenticarse(String nombre, String contrasena) throws NegocioException {
    return control.autenticar(nombre, contrasena);
  }

  @Override
  public List<UsuarioDTO> obtenerTodos() throws NegocioException {
    return control.obtenerTodos();
  }

  @Override
  public void guardarUsuario(UsuarioDTO usuario) throws NegocioException {
    control.guardarUsuario(usuario);
  }

  @Override
  public void actualizarUsuario(UsuarioDTO usuario) throws NegocioException {
    control.actualizarUsuario(usuario);
  }

  @Override
  public void eliminarUsuario(String nombre) throws NegocioException {
    control.eliminarUsuario(nombre);
  }

  @Override
  public void cambiarRol(String nombre, UsuarioRol nuevoRol) throws NegocioException {
    control.cambiarRol(nombre, nuevoRol);
  }

}