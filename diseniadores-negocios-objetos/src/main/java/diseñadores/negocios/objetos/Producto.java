package diseñadores.negocios.objetos;

import adaptadores.ProductoProveedorNegocioAdapter;
import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Puente de conexión directo para operaciones de Productos.
 * @author ERICK
 */
public class Producto {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final ProductoProveedorNegocioAdapter ADAPTADOR = new ProductoProveedorNegocioAdapter();

  public static List<ProductoDTO> obtenerTodos() throws PersistenciaException {
    // 1. Recupera la lista de entidades de dominio limpio
    List<entidades.Producto> listaDominio = PERSISTENCIA.obtenerProductos();
    // 2. Las mapea a la lista de DTOs para la UI
    return ADAPTADOR.listaProductosADTO(listaDominio);
  }

  public static ProductoDTO obtenerPorCodigo(String codigo) throws PersistenciaException {
    // 1. Busca la entidad de dominio por su código
    entidades.Producto productoDominio = PERSISTENCIA.obtenerProductoPorCodigo(codigo);
    // 2. La transforma a DTO
    return ADAPTADOR.productoADTO(productoDominio);
  }

  public static void guardar(ProductoDTO productoDTO) throws PersistenciaException {
    // 1. Transforma el DTO de la UI al dominio limpio de Mongo
    entidades.Producto productoDominio = ADAPTADOR.productoADominio(productoDTO);
    // 2. Lo manda a guardar a la base de datos
    PERSISTENCIA.guardarProducto(productoDominio);
  }

  public static void actualizar(ProductoDTO productoDTO) throws PersistenciaException {
    // 1. Transforma a dominio limpio
    entidades.Producto productoDominio = ADAPTADOR.productoADominio(productoDTO);
    // 2. Ejecuta la actualización en la persistencia
    PERSISTENCIA.actualizarProducto(productoDominio);
  }

  public static void eliminar(String codigo) throws PersistenciaException {
    // Pasa directo ya que la persistencia requiere el String plano del código
    PERSISTENCIA.eliminarProducto(codigo);
  }

}