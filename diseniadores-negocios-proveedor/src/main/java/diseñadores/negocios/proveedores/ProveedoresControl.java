package diseñadores.negocios.proveedores;

import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.negocios.objetos.OrdenCompra;
import diseñadores.negocios.objetos.Proveedor;
import excepciones.NegocioException;
import excepciones.PersistenciaException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ProveedoresControl {

  public List<ProveedorDTO> obtenerTodos() throws NegocioException {
    try {
      return Proveedor.obtenerTodos();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar la lista completa de proveedores", e);
    }
  }

  public ProveedorDTO obtenerPorCodigo(String codigo) throws NegocioException {
    validarCodigoRequerido(codigo);
    return ejecutarObtencionPorCodigo(codigo);
  }

  public int contarActivos() throws NegocioException {
    return (int) obtenerTodos().stream()
      .filter(ProveedorDTO::isActivo)
      .count();
  }

  public void guardar(ProveedorDTO proveedor) throws NegocioException {
    validarProveedorNoNulo(proveedor);
    validarDatosObligatoriosProveedor(proveedor);

    asignarNuevoCodigo(proveedor);
    ejecutarGuardadoProveedor(proveedor);
  }

  public void actualizar(ProveedorDTO proveedor) throws NegocioException {
    validarProveedorNoNulo(proveedor);
    validarCodigoObligatorio(proveedor.getCodigo());
    validarNombreObligatorio(proveedor.getNombre());
    validarExistenciaProveedor(proveedor.getCodigo());

    ejecutarActualizacionProveedor(proveedor);
  }

  public List<OrdenCompraDTO> obtenerOrdenesCompra() throws NegocioException {
    try {
      return OrdenCompra.obtenerTodas();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al obtener el historial de órdenes de compra", e);
    }
  }

  public OrdenCompraDTO obtenerOrdenPorNumero(String numero) throws NegocioException {
    validarNumeroOrdenRequerido(numero);

    OrdenCompraDTO orden = ejecutarObtencionOrden(numero);
    validarExistenciaOrden(orden, numero);

    return orden;
  }

 public void guardarOrdenCompra(ProveedorDTO proveedor, int cantidadProductos, BigDecimal total, List<diseñadores.negocios.dto.DetalleOrdenCompraDTO> productos) throws NegocioException {
    validarProveedorNoNulo(proveedor);
    validarCantidadProductos(cantidadProductos);
    validarTotalOrden(total);
    validarExistenciaProveedor(proveedor.getCodigo());

    // Se pasa la lista de productos al método de creación
    OrdenCompraDTO orden = crearNuevaOrden(proveedor, cantidadProductos, total, productos);
    ejecutarGuardadoOrden(orden);
  }

  public void actualizarOrdenCompra(OrdenCompraDTO orden) throws NegocioException {
    validarOrdenNoNula(orden);
    validarNumeroOrdenRequerido(orden.getNumero());
    validarExistenciaOrdenPorNumero(orden.getNumero());

    ejecutarActualizacionOrden(orden);
  }

  public void cambiarEstadoOrden(String numero, String nuevoEstado) throws NegocioException {
    validarNumeroOrdenRequerido(numero);
    validarEstadoRequerido(nuevoEstado);

    OrdenCompraDTO orden = ejecutarObtencionOrden(numero);
    validarExistenciaOrden(orden, numero);
    validarEstadoValido(nuevoEstado);

    actualizarEstadoEnOrden(orden, nuevoEstado);
    ejecutarActualizacionOrden(orden);
  }

  private void validarCodigoRequerido(String codigo) {
    if (codigo == null || codigo.isBlank()) {
      throw new IllegalArgumentException("El código del proveedor no puede estar vacío.");
    }
  }

  private void validarProveedorNoNulo(ProveedorDTO proveedor) {
    if (proveedor == null) {
      throw new IllegalArgumentException("El proveedor no puede ser nulo.");
    }
  }

  private void validarDatosObligatoriosProveedor(ProveedorDTO proveedor) {
    validarNombreObligatorio(proveedor.getNombre());
    if (proveedor.getContacto() == null || proveedor.getContacto().isBlank()) {
      throw new IllegalArgumentException("El contacto del proveedor es obligatorio.");
    }
    if (proveedor.getTelefono() == null || proveedor.getTelefono().isBlank()) {
      throw new IllegalArgumentException("El teléfono del proveedor es obligatorio.");
    }
    if (proveedor.getEmail() == null || proveedor.getEmail().isBlank()) {
      throw new IllegalArgumentException("El email del proveedor es obligatorio.");
    }
  }

  private void validarNombreObligatorio(String nombre) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre del proveedor es obligatorio.");
    }
  }

  private void validarCodigoObligatorio(String codigo) {
    if (codigo == null || codigo.isBlank()) {
      throw new IllegalArgumentException("El código del proveedor es obligatorio.");
    }
  }

  private void validarExistenciaProveedor(String codigo) throws NegocioException {
    if (ejecutarObtencionPorCodigo(codigo) == null) {
      throw new IllegalStateException("No existe un proveedor con el código: " + codigo);
    }
  }

  private void validarNumeroOrdenRequerido(String numero) {
    if (numero == null || numero.isBlank()) {
      throw new IllegalArgumentException("El número de orden no puede estar vacío.");
    }
  }

  private void validarExistenciaOrden(OrdenCompraDTO orden, String numero) {
    if (orden == null) {
      throw new IllegalStateException("No existe una orden con el número: " + numero);
    }
  }

  private void validarExistenciaOrdenPorNumero(String numero) throws NegocioException {
    if (ejecutarObtencionOrden(numero) == null) {
      throw new IllegalStateException("No existe una orden con el número: " + numero);
    }
  }

  private void validarCantidadProductos(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad de productos debe ser mayor a cero.");
    }
  }

  private void validarTotalOrden(BigDecimal total) {
    if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El total de la orden debe ser mayor a cero.");
    }
  }

  private void validarOrdenNoNula(OrdenCompraDTO orden) {
    if (orden == null) {
      throw new IllegalArgumentException("La orden no puede ser nula.");
    }
  }

  private void validarEstadoRequerido(String estado) {
    if (estado == null || estado.isBlank()) {
      throw new IllegalArgumentException("El nuevo estado no puede estar vacío.");
    }
  }

  private void validarEstadoValido(String estado) {
    List<String> estadosValidos = List.of("Pendiente", "Aprobada", "Recibida", "Cancelada");
    if (!estadosValidos.contains(estado)) {
      throw new IllegalArgumentException("Estado inválido. Los estados válidos son: " + estadosValidos);
    }
  }

  private void asignarNuevoCodigo(ProveedorDTO proveedor) {
    proveedor.setCodigo(generarCodigoProveedor());
  }

  private void actualizarEstadoEnOrden(OrdenCompraDTO orden, String estado) {
    orden.setEstado(estado);
  }

  private ProveedorDTO ejecutarObtencionPorCodigo(String codigo) throws NegocioException {
    try {
      return Proveedor.obtenerPorCodigo(codigo);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al buscar el proveedor con código: " + codigo, e);
    }
  }

  private void ejecutarGuardadoProveedor(ProveedorDTO proveedor) throws NegocioException {
    try {
      Proveedor.guardar(proveedor);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al dar de alta al nuevo proveedor", e);
    }
  }

  private void ejecutarActualizacionProveedor(ProveedorDTO proveedor) throws NegocioException {
    try {
      Proveedor.actualizar(proveedor);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al actualizar la ficha informativa del proveedor", e);
    }
  }

  private OrdenCompraDTO ejecutarObtencionOrden(String numero) throws NegocioException {
    try {
      return OrdenCompra.obtenerPorNumero(numero);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al recuperar la orden de compra número: " + numero, e);
    }
  }

  private void ejecutarGuardadoOrden(OrdenCompraDTO orden) throws NegocioException {
    try {
      OrdenCompra.guardar(orden);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al registrar la orden de compra en el sistema", e);
    }
  }

  private void ejecutarActualizacionOrden(OrdenCompraDTO orden) throws NegocioException {
    try {
      OrdenCompra.actualizar(orden);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al guardar las modificaciones de la orden de compra", e);
    }
  }

  private OrdenCompraDTO crearNuevaOrden(ProveedorDTO proveedor, int cantidad, BigDecimal total, List<diseñadores.negocios.dto.DetalleOrdenCompraDTO> productos) {
    ProveedorDTO provRef = new ProveedorDTO(
      proveedor.getNombre(), proveedor.getCodigo(),
      proveedor.getContacto(), proveedor.getTelefono(), proveedor.getEmail(),
      proveedor.getDireccion(), proveedor.getTerminosPago(), proveedor.isActivo()
    );

    OrdenCompraDTO nuevaOrden = new OrdenCompraDTO(
      generarNumeroOrden(),
      LocalDate.now().toString(),
      provRef,
      "Pendiente",
      cantidad,
      total
    );
    
    // Inyección de los productos reales provenientes de la interfaz de usuario
    nuevaOrden.setProductos(productos);
    
    return nuevaOrden;
  }

  private String generarCodigoProveedor() {
    return "PROV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  private String generarNumeroOrden() {
    return String.format("OC-%d-%s",
      LocalDate.now().getYear(),
      UUID.randomUUID().toString().substring(0, 8).toUpperCase());
  }
}