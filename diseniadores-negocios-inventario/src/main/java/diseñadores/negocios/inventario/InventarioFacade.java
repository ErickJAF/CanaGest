package diseñadores.negocios.inventario;

import diseñadores.negocios.dto.ProductoDTO;
import excepciones.NegocioException;
import java.util.List;

public class InventarioFacade implements IInventario {

  private final InventarioControl control;

  public InventarioFacade() {
    this.control = new InventarioControl();
  }

  public InventarioFacade(InventarioControl control) {
    this.control = control;
  }

  @Override
  public ProductoDTO obtenerProductoPorCodigo(String codigo) throws NegocioException {
    return control.obtenerProductoPorCodigo(codigo);
  }

  @Override
  public List<ProductoDTO> obtenerTodos() throws NegocioException {
    return control.obtenerTodos();
  }

  @Override
  public List<ProductoDTO> obtenerProductosBajoMinimo() throws NegocioException {
    return control.obtenerProductosBajoMinimo();
  }

  @Override
  public List<ProductoDTO> necesitanReorden() throws NegocioException {
    return control.necesitanReorden();
  }

  @Override
  public boolean verificarStock(String codigo, int cantidad) throws NegocioException {
    return control.verificarStock(codigo, cantidad);
  }

  @Override
  public void descontarStock(String codigo, int cantidad) throws NegocioException {
    control.descontarStock(codigo, cantidad);
  }

  @Override
  public void actualizarStock(String codigo, int nuevaCantidad) throws NegocioException {
    control.actualizarStock(codigo, nuevaCantidad);
  }

  @Override
  public void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws NegocioException {
    control.actualizarStockCompleto(codigo, nuevoStock, nuevoMinimo, nuevoMaximo);
  }

  @Override
  public void ajustarStock(String codigo, int stockFisico) throws NegocioException {
    control.ajustarStock(codigo, stockFisico);
  }

  @Override
  public int[] obtenerEstadisticasConteo() throws NegocioException {
    return control.obtenerEstadisticasConteo();
  }
}