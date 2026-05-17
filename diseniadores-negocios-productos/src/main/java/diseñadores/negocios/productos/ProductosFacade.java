package diseñadores.negocios.productos;

import diseñadores.negocios.dto.EscanearProductoDTO;
import diseñadores.negocios.dto.ProductoDTO;
import excepciones.NegocioException;
import java.util.List;

public class ProductosFacade implements IProductos {

  private final ProductosControl control;

  public ProductosFacade() {
    this.control = new ProductosControl();
  }

  public ProductosFacade(ProductosControl control) {
    this.control = control;
  }

  @Override
  public List<ProductoDTO> obtenerCatalogo() throws NegocioException {
    return control.obtenerCatalogo();
  }

  @Override
  public ProductoDTO buscarProductoPorCodigo(EscanearProductoDTO dto) throws NegocioException {
    return control.buscarProductoPorCodigo(dto);
  }

  @Override
  public boolean existeProducto(EscanearProductoDTO dto) throws NegocioException {
    return control.existeProducto(dto);
  }

  @Override
  public boolean tieneStock(EscanearProductoDTO dto, int cantidad) throws NegocioException {
    return control.tieneStock(dto, cantidad);
  }

  @Override
  public void descontarStock(String codigo, int cantidad) throws NegocioException {
    control.descontarStock(codigo, cantidad);
  }

  @Override
  public void guardarProducto(ProductoDTO producto) throws NegocioException {
    control.guardarProducto(producto);
  }

  @Override
  public void actualizarProducto(ProductoDTO producto) throws NegocioException {
    control.actualizarProducto(producto);
  }

  @Override
  public void eliminarProducto(String codigo) throws NegocioException {
    control.eliminarProducto(codigo);
  }
}