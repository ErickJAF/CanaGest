package diseñadores.negocios.inventario;

import diseñadores.negocios.dto.ProductoDTO;
import excepciones.NegocioException;
import java.util.List;

public interface IInventario {

  ProductoDTO obtenerProductoPorCodigo(String codigo) throws NegocioException;

  boolean verificarStock(String codigo, int cantidad) throws NegocioException;

  void descontarStock(String codigo, int cantidad) throws NegocioException;

  void actualizarStock(String codigo, int nuevaCantidad) throws NegocioException;

  void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws NegocioException;

  List<ProductoDTO> obtenerTodos() throws NegocioException;

  List<ProductoDTO> obtenerProductosBajoMinimo() throws NegocioException;

  List<ProductoDTO> necesitanReorden() throws NegocioException;

  void ajustarStock(String codigo, int stockFisico) throws NegocioException;

  int[] obtenerEstadisticasConteo() throws NegocioException;

}