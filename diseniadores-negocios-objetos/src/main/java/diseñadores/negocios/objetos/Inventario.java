package diseñadores.negocios.objetos;

import adaptadores.ProductoProveedorNegocioAdapter;
import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Puente de conexión y control logístico para el Inventario de Productos.
 * @author ERICK
 */
public class Inventario {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final ProductoProveedorNegocioAdapter ADAPTADOR = new ProductoProveedorNegocioAdapter();

  public static List<ProductoDTO> obtenerTodos() throws PersistenciaException {
    List<entidades.Producto> listaDominio = PERSISTENCIA.obtenerProductos();
    return ADAPTADOR.listaProductosADTO(listaDominio);
  }

  public static ProductoDTO obtenerProductoPorCodigo(String codigo) throws PersistenciaException {
    entidades.Producto productoDominio = PERSISTENCIA.obtenerProductoPorCodigo(codigo);
    return ADAPTADOR.productoADTO(productoDominio);
  }

  public static List<ProductoDTO> obtenerProductosBajoMinimo() throws PersistenciaException {
    // Obtenemos todos los productos ya transformados a DTO para evaluar sus filtros de la UI
    return obtenerTodos().stream()
      .filter(ProductoDTO::estaBajoMinimo)
      .collect(Collectors.toList());
  }

  public static List<ProductoDTO> necesitanReorden() throws PersistenciaException {
    return obtenerTodos().stream()
      .filter(ProductoDTO::necesitaReorden)
      .collect(Collectors.toList());
  }

  public static boolean verificarStock(String codigo, int cantidad) throws PersistenciaException {
    ProductoDTO p = obtenerProductoPorCodigo(codigo);
    return p != null && p.getStockActual() >= cantidad;
  }

  public static void descontarStock(String codigo, int cantidad) throws PersistenciaException {
    ProductoDTO p = obtenerProductoPorCodigo(codigo);
    if (p != null && p.getStockActual() >= cantidad) {
      p.setStockActual(p.getStockActual() - cantidad);
      
      // Convertimos de vuelta a dominio para poder actualizar en MongoDB
      entidades.Producto productoDominio = ADAPTADOR.productoADominio(p);
      PERSISTENCIA.actualizarProducto(productoDominio);
    }
  }

  public static void actualizarStock(String codigo, int nuevaCantidad) throws PersistenciaException {
    ProductoDTO p = obtenerProductoPorCodigo(codigo);
    if (p != null) {
      p.setStockActual(nuevaCantidad);
      
      entidades.Producto productoDominio = ADAPTADOR.productoADominio(p);
      PERSISTENCIA.actualizarProducto(productoDominio);
    }
  }

  public static void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws PersistenciaException {
    ProductoDTO p = obtenerProductoPorCodigo(codigo);
    if (p != null) {
      p.setStockActual(nuevoStock);
      p.setStockMinimo(nuevoMinimo);
      p.setStockMaximo(nuevoMaximo);
      
      entidades.Producto productoDominio = ADAPTADOR.productoADominio(p);
      PERSISTENCIA.actualizarProducto(productoDominio);
    }
  }

  public static void ajustarStock(String codigo, int stockFisico) throws PersistenciaException {
    ProductoDTO p = obtenerProductoPorCodigo(codigo);
    if (p != null) {
      p.setStockActual(stockFisico);
      
      entidades.Producto productoDominio = ADAPTADOR.productoADominio(p);
      PERSISTENCIA.actualizarProducto(productoDominio);
    }
  }

  public static int[] obtenerEstadisticasConteo() throws PersistenciaException {
    List<ProductoDTO> productos = obtenerTodos();
    int total = productos.size();
    int pendientes = (int) productos.stream()
      .filter(p -> p.getStockActual() < p.getStockMinimo())
      .count();
    int diferencias = productos.stream()
      .mapToInt(p -> Math.abs(p.getStockActual() - (p.getStockMinimo() + p.getStockMaximo()) / 2))
      .sum();
    return new int[]{total, pendientes, diferencias};
  }

}