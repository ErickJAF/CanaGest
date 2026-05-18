package diseñadores.negocios.objetos;

import adaptadores.ProductoProveedorNegocioAdapter;
import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;
import java.util.List;

/**
 * Puente de conexión (Facade) para las operaciones lógicas de Productos.
 * Gestiona la transformación entre entidades de persistencia y DTOs de vista.
 * * @author icoro
 */
public class Producto {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final ProductoProveedorNegocioAdapter ADAPTADOR = new ProductoProveedorNegocioAdapter();

  /**
   * Recupera todo el catálogo de productos disponibles.
   * * @return Lista de ProductoDTO.
   * @throws PersistenciaException Si la consulta a la base de datos falla.
   */
  public static List<ProductoDTO> obtenerTodos() throws PersistenciaException {
    List<entidades.Producto> listaDominio = PERSISTENCIA.obtenerProductos();
    return ADAPTADOR.listaProductosADTO(listaDominio);
  }

  /**
   * Busca la información de un producto específico mediante su código.
   * * @param codigo El código del producto a buscar.
   * @return DTO del producto encontrado o null.
   * @throws PersistenciaException Si la consulta falla.
   */
  public static ProductoDTO obtenerPorCodigo(String codigo) throws PersistenciaException {
    entidades.Producto productoDominio = PERSISTENCIA.obtenerProductoPorCodigo(codigo);
    return ADAPTADOR.productoADTO(productoDominio);
  }

  /**
   * Inserta un nuevo producto en el catálogo del sistema.
   * * @param productoDTO DTO con la información capturada.
   * @throws PersistenciaException Si el guardado falla.
   */
  public static void guardar(ProductoDTO productoDTO) throws PersistenciaException {
    entidades.Producto productoDominio = ADAPTADOR.productoADominio(productoDTO);
    PERSISTENCIA.guardarProducto(productoDominio);
  }

  /**
   * Modifica los datos de un producto previamente registrado.
   * * @param productoDTO DTO con la información actualizada.
   * @throws PersistenciaException Si la actualización falla en persistencia.
   */
  public static void actualizar(ProductoDTO productoDTO) throws PersistenciaException {
    entidades.Producto productoDominio = ADAPTADOR.productoADominio(productoDTO);
    PERSISTENCIA.actualizarProducto(productoDominio);
  }

  /**
   * Elimina un producto del catálogo permanentemente.
   * * @param codigo Código identificador del producto a remover.
   * @throws PersistenciaException Si el borrado falla.
   */
  public static void eliminar(String codigo) throws PersistenciaException {
    PERSISTENCIA.eliminarProducto(codigo);
  }

}