package diseñadores.negocios.usuarios;

import diseñadores.negocios.dto.UsuarioDTO;
import diseñadores.negocios.dto.UsuarioRol;
import diseñadores.negocios.objetos.Usuario;
import excepciones.NegocioException;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.Optional;

public class UsuariosControl {

  public Optional<UsuarioDTO> autenticar(String nombre, String contrasena) throws NegocioException {
    validarCredencialesEntrada(nombre, contrasena);

    return ejecutarAutenticacion(nombre, contrasena);
  }

  public List<UsuarioDTO> obtenerTodos() throws NegocioException {
    try {
      return Usuario.obtenerTodos();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar el listado global de usuarios", e);
    }
  }

  public void guardarUsuario(UsuarioDTO usuario) throws NegocioException {
    validarUsuarioNoNulo(usuario);
    validarNombreObligatorio(usuario.getNombre());
    validarContrasenaObligatoria(usuario.getContrasena());
    validarFormatoContrasena(usuario.getContrasena());
    validarRolObligatorio(usuario.getRol());
    validarNombreDisponible(usuario.getNombre());

    ejecutarGuardado(usuario);
  }

  public void actualizarUsuario(UsuarioDTO usuario) throws NegocioException {
    validarUsuarioNoNulo(usuario);
    validarNombreObligatorio(usuario.getNombre());
    validarFormatoContrasenaOpcional(usuario.getContrasena());
    validarRolObligatorio(usuario.getRol());
    validarExistenciaUsuario(usuario.getNombre());

    ejecutarActualizacion(usuario);
  }

  public void eliminarUsuario(String nombre) throws NegocioException {
    validarNombreObligatorio(nombre);
    validarExistenciaUsuario(nombre);

    ejecutarEliminacion(nombre);
  }

  public void cambiarRol(String nombre, UsuarioRol nuevoRol) throws NegocioException {
    validarNombreObligatorio(nombre);
    validarNuevoRolNoNulo(nuevoRol);

    UsuarioDTO usuario = buscarUsuarioPorNombre(nombre);
    asignarNuevoRol(usuario, nuevoRol);

    ejecutarActualizacion(usuario);
  }

  // --- Métodos de Validación y Soporte ---

  private void validarCredencialesEntrada(String nombre, String contrasena) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
    }
    if (contrasena == null || contrasena.isBlank()) {
      throw new IllegalArgumentException("La contraseña no puede estar vacía.");
    }
  }

  private void validarUsuarioNoNulo(UsuarioDTO usuario) {
    if (usuario == null) {
      throw new IllegalArgumentException("El usuario no puede ser nulo.");
    }
  }

  private void validarNombreObligatorio(String nombre) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
    }
  }

  private void validarContrasenaObligatoria(String contrasena) {
    if (contrasena == null || contrasena.isBlank()) {
      throw new IllegalArgumentException("La contraseña es obligatoria.");
    }
  }

  private void validarFormatoContrasena(String contrasena) {
    if (contrasena.length() < 4) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
    }
  }

  private void validarFormatoContrasenaOpcional(String contrasena) {
    if (contrasena != null) {
      validarFormatoContrasena(contrasena);
    }
  }

  private void validarRolObligatorio(UsuarioRol rol) {
    if (rol == null) {
      throw new IllegalArgumentException("El rol del usuario es obligatorio.");
    }
  }

  private void validarNuevoRolNoNulo(UsuarioRol rol) {
    if (rol == null) {
      throw new IllegalArgumentException("El nuevo rol no puede ser nulo.");
    }
  }

  private void validarNombreDisponible(String nombre) throws NegocioException {
    boolean existe = obtenerTodos().stream()
      .anyMatch(u -> u.getNombre().equalsIgnoreCase(nombre));
    if (existe) {
      throw new IllegalStateException("Ya existe un usuario con el nombre: " + nombre);
    }
  }

  private void validarExistenciaUsuario(String nombre) throws NegocioException {
    boolean existe = obtenerTodos().stream()
      .anyMatch(u -> u.getNombre().equals(nombre));
    if (!existe) {
      throw new IllegalStateException("No existe un usuario con el nombre: " + nombre);
    }
  }

  private Optional<UsuarioDTO> ejecutarAutenticacion(String nombre, String contrasena) throws NegocioException {
    try {
      String nombreNormalizado = nombre.toLowerCase().trim();
      return Usuario.autenticar(nombreNormalizado, contrasena);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error de comunicación durante el proceso de inicio de sesión", e);
    }
  }

  private void ejecutarGuardado(UsuarioDTO usuario) throws NegocioException {
    try {
      Usuario.guardar(usuario);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al registrar las credenciales del nuevo usuario", e);
    }
  }

  private void ejecutarActualizacion(UsuarioDTO usuario) throws NegocioException {
    try {
      Usuario.actualizar(usuario);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al guardar las modificaciones del perfil de usuario", e);
    }
  }

  private void ejecutarEliminacion(String nombre) throws NegocioException {
    try {
      Usuario.eliminar(nombre);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error de baja: No se pudo eliminar al usuario de la base de datos", e);
    }
  }

  private UsuarioDTO buscarUsuarioPorNombre(String nombre) throws NegocioException {
    return obtenerTodos().stream()
      .filter(u -> u.getNombre().equals(nombre))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("No existe un usuario con el nombre: " + nombre));
  }

  private void asignarNuevoRol(UsuarioDTO usuario, UsuarioRol rol) {
    usuario.setRol(rol);
  }

}