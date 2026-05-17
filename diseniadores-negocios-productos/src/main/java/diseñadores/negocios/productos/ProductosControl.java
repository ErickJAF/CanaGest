package diseñadores.negocios.productos;

import diseñadores.negocios.dto.EscanearProductoDTO;
import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.negocios.objetos.Producto;
import diseñadores.negocios.inventario.IInventario;
import diseñadores.negocios.inventario.InventarioFacade;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;

public class ProductosControl {

  private final IInventario serviciosInventario;

  public ProductosControl() {
    this.serviciosInventario = new InventarioFacade();
  }

  public ProductosControl(IInventario serviciosInventario) {
    this.serviciosInventario = serviciosInventario;
  }

  public List<ProductoDTO> obtenerCatalogo() throws NegocioException {
    try {
      return Producto.obtenerTodos();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar el catálogo de productos", e);
    }
  }

  public ProductoDTO buscarProductoPorCodigo(EscanearProductoDTO dto) throws NegocioException {
    validarDtoEscaneo(dto);
    ProductoDTO p = obtenerProductoBase(dto.getCodigo());
    validarProductoExistente(p);
    sincronizarStockDesdeInventario(p, dto.getCodigo());
    return p;
  }

  public boolean existeProducto(EscanearProductoDTO dto) throws NegocioException {
    if (esDtoInvalido(dto)) {
      return false;
    }
    return obtenerProductoBase(dto.getCodigo()) != null;
  }

  public boolean tieneStock(EscanearProductoDTO dto, int cantidad) throws NegocioException {
    if (esDtoInvalido(dto)) {
      return false;
    }
    return verificarStockServicio(dto.getCodigo(), cantidad);
  }

  public void descontarStock(String codigo, int cantidad) throws NegocioException {
    validarCodigoRequerido(codigo);
    validarCantidadPositiva(cantidad);
    validarExistenciaParaDescuento(codigo);
    validarDisponibilidadStock(codigo, cantidad);

    ejecutarDescuentoStock(codigo, cantidad);
  }

  public void guardarProducto(ProductoDTO producto) throws NegocioException {
    validarDatosProducto(producto);
    registrarNuevoProducto(producto);
  }

  public void actualizarProducto(ProductoDTO producto) throws NegocioException {
    validarDatosProducto(producto);
    ejecutarActualizacion(producto);
  }

  public void eliminarProducto(String codigo) throws NegocioException {
    validarCodigoRequerido(codigo);
    ejecutarEliminacion(codigo);
  }

  // --- Métodos de Validación y Soporte ---

  private void validarDtoEscaneo(EscanearProductoDTO dto) {
    if (dto == null || dto.getCodigo() == null || dto.getCodigo().isBlank()) {
      throw new IllegalArgumentException("Código inválido");
    }
  }

  private boolean esDtoInvalido(EscanearProductoDTO dto) {
    return dto == null || dto.getCodigo() == null;
  }

  private void validarProductoExistente(ProductoDTO p) {
    if (p == null) {
      throw new IllegalStateException("Producto no existe");
    }
  }

  private void validarDatosProducto(ProductoDTO producto) {
    if (producto == null || producto.getCodigo() == null) {
      throw new IllegalArgumentException("Datos insuficientes");
    }
  }

  private void validarCodigoRequerido(String codigo) {
    if (codigo == null || codigo.isBlank()) {
      throw new IllegalArgumentException("Código requerido");
    }
  }

  private void validarCantidadPositiva(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero");
    }
  }

  private void validarExistenciaParaDescuento(String codigo) throws NegocioException {
    if (obtenerProductoBase(codigo) == null) {
      throw new IllegalStateException("No se puede descontar stock de un producto inexistente");
    }
  }

  private void validarDisponibilidadStock(String codigo, int cantidad) throws NegocioException {
    if (!verificarStockServicio(codigo, cantidad)) {
      throw new IllegalStateException("Stock insuficiente para realizar el descuento");
    }
  }

  private ProductoDTO obtenerProductoBase(String codigo) throws NegocioException {
    try {
      return Producto.obtenerPorCodigo(codigo);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al buscar el producto por código", e);
    }
  }

  private void sincronizarStockDesdeInventario(ProductoDTO p, String codigo) throws NegocioException {
      ProductoDTO infoInventario = serviciosInventario.obtenerProductoPorCodigo(codigo);
      actualizarStockSiExiste(p, infoInventario);
  }

  private boolean verificarStockServicio(String codigo, int cantidad) throws NegocioException {
      return serviciosInventario.verificarStock(codigo, cantidad);
  }

  private void ejecutarDescuentoStock(String codigo, int cantidad) throws NegocioException {
      serviciosInventario.descontarStock(codigo, cantidad);
  }

  private void registrarNuevoProducto(ProductoDTO producto) throws NegocioException {
    try {
      Producto.guardar(producto);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al registrar el producto en el sistema", e);
    }
  }

  private void ejecutarActualizacion(ProductoDTO producto) throws NegocioException {
    try {
      Producto.actualizar(producto);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al actualizar los datos del producto", e);
    }
  }

  private void ejecutarEliminacion(String codigo) throws NegocioException {
    try {
      Producto.eliminar(codigo);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al dar de baja el producto", e);
    }
  }

  private void actualizarStockSiExiste(ProductoDTO producto, ProductoDTO productoInventario) {
    if (productoInventario != null) {
      producto.setStock(productoInventario.getStock());
    }
  }
}