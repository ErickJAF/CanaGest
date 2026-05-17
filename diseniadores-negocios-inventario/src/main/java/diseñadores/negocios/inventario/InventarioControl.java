package diseñadores.negocios.inventario;

import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.negocios.objetos.Inventario;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;

public class InventarioControl {

  public ProductoDTO obtenerProductoPorCodigo(String codigo) throws NegocioException {
    try {
      return Inventario.obtenerProductoPorCodigo(codigo);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al buscar el producto en el inventario por código", e);
    }
  }

  public List<ProductoDTO> obtenerTodos() throws NegocioException {
    try {
      return Inventario.obtenerTodos();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar las existencias de inventario", e);
    }
  }

  public List<ProductoDTO> obtenerProductosBajoMinimo() throws NegocioException {
    try {
      return Inventario.obtenerProductosBajoMinimo();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar productos con stock crítico (bajo mínimo)", e);
    }
  }

  public List<ProductoDTO> necesitanReorden() throws NegocioException {
    try {
      return Inventario.necesitanReorden();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al procesar la lista de productos que requieren reorden", e);
    }
  }

  public boolean verificarStock(String codigo, int cantidad) throws NegocioException {
    if (esConsultaInvalida(codigo, cantidad)) {
      return false;
    }
    return ejecutarVerificacionStock(codigo, cantidad);
  }

  public void descontarStock(String codigo, int cantidad) throws NegocioException {
    validarQuantityPositiva(cantidad);

    ProductoDTO producto = ejecutarObtencionPorCodigo(codigo); 
    validarExistenciaProducto(producto);
    validarSuficienciaStock(producto, cantidad);

    try {
      Inventario.descontarStock(codigo, cantidad);
    } catch (PersistenciaException e) {
      throw new NegocioException("No se pudo procesar la disminución de stock en el inventario", e);
    }
  }

  public void actualizarStock(String codigo, int nuevaCantidad) throws NegocioException {
    validarStockNoNegativo(nuevaCantidad);
    validarExistenciaProductoPorCodigo(codigo);

    try {
      Inventario.actualizarStock(codigo, nuevaCantidad);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al actualizar la cantidad de stock disponible", e);
    }
  }

  public void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws NegocioException {
    validarValoresNoNegativos(nuevoStock, nuevoMinimo, nuevoMaximo);
    validarJerarquiaStock(nuevoMinimo, nuevoMaximo);
    validarExistenciaProductoPorCodigo(codigo);

    ejecutarActualizacionCompleta(codigo, nuevoStock, nuevoMinimo, nuevoMaximo);
  }

  public void ajustarStock(String codigo, int stockFisico) throws NegocioException {
    validarStockFisicoNoNegativo(stockFisico);
    validarExistenciaProductoPorCodigo(codigo);

    try {
      Inventario.ajustarStock(codigo, stockFisico);
    } catch (PersistenciaException e) {
      throw new NegocioException("No se pudo registrar el ajuste de stock físico", e);
    }
  }

  public int[] obtenerEstadisticasConteo() throws NegocioException {
    try {
      return Inventario.obtenerEstadisticasConteo();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al calcular las métricas y estadísticas de conteos", e);
    }
  }

  private boolean esConsultaInvalida(String codigo, int cantidad) {
    return codigo == null || codigo.isBlank() || cantidad <= 0;
  }

  private void validarQuantityPositiva(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("Cantidad mayor a cero requerida");
    }
  }

  private void validarStockNoNegativo(int cantidad) {
    if (cantidad < 0) {
      throw new IllegalArgumentException("Stock negativo no permitido");
    }
  }

  private void validarStockFisicoNoNegativo(int stockFisico) {
    if (stockFisico < 0) {
      throw new IllegalArgumentException("Stock físico negativo");
    }
  }

  private void validarValoresNoNegativos(int nuevoStock, int nuevoMinimo, int nuevoMaximo) {
    if (nuevoStock < 0 || nuevoMinimo < 0 || nuevoMaximo < 0) {
      throw new IllegalArgumentException("Valores negativos no permitidos");
    }
  }

  private void validarJerarquiaStock(int minimo, int maximo) {
    if (minimo > maximo) {
      throw new IllegalArgumentException("Mínimo mayor al máximo");
    }
  }

  private void validarExistenciaProducto(ProductoDTO p) {
    if (p == null) {
      throw new IllegalStateException("Producto no existe");
    }
  }

  private void validarExistenciaProductoPorCodigo(String codigo) throws NegocioException {
    if (ejecutarObtencionPorCodigo(codigo) == null) {
      throw new IllegalStateException("Producto no existe en el inventario");
    }
  }

  private void validarSuficienciaStock(ProductoDTO p, int cantidad) {
    if (p.getStock() < cantidad) {
      throw new IllegalStateException("Stock insuficiente");
    }
  }

  private ProductoDTO ejecutarObtencionPorCodigo(String codigo) throws NegocioException {
    try {
      return Inventario.obtenerProductoPorCodigo(codigo);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al validar existencia del producto", e);
    }
  }

  private boolean ejecutarVerificacionStock(String codigo, int cantidad) throws NegocioException {
    try {
      return Inventario.verificarStock(codigo, cantidad);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al consultar el estado de disponibilidad del stock", e);
    }
  }

  private void ejecutarActualizacionCompleta(String c, int s, int min, int max) throws NegocioException {
    try {
      Inventario.actualizarStockCompleto(c, s, min, max);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al guardar la reconfiguración completa de los límites del stock", e);
    }
  }
}