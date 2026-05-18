package diseñadores.negocios.objetos;

import adaptadores.OrdenCompraNegocioAdapter;
import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;

/**
 * Puente de conexión (Facade) para las operaciones lógicas de Órdenes de Compra.
 * Adapta los DTOs provenientes de la Interfaz de Usuario hacia entidades
 * de dominio puro requeridas por la capa de persistencia.
 * * @author icoro
 */
public class OrdenCompra {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final OrdenCompraNegocioAdapter ADAPTADOR = new OrdenCompraNegocioAdapter();

  /**
   * Recupera todas las órdenes de compra de la base de datos y las transforma a DTOs.
   * * @return Lista de OrdenCompraDTO listas para la capa de presentación.
   * @throws PersistenciaException Si ocurre un error en la base de datos.
   */
  public static List<OrdenCompraDTO> obtenerTodas() throws PersistenciaException {
    List<entidades.OrdenCompra> listaDominio = PERSISTENCIA.obtenerOrdenesCompra();
    return ADAPTADOR.listaADTO(listaDominio);
  }

  /**
   * Busca una orden de compra específica mediante su número único.
   * * @param numero Código único de la orden a buscar.
   * @return Objeto OrdenCompraDTO con los datos, o null si no se encuentra.
   * @throws PersistenciaException Si ocurre un error en la base de datos.
   */
  public static OrdenCompraDTO obtenerPorNumero(String numero) throws PersistenciaException {
    entidades.OrdenCompra ordenDominio = PERSISTENCIA.obtenerOrdenCompraPorNumero(numero);
    return ADAPTADOR.aDTO(ordenDominio);
  }

  /**
   * Registra una nueva orden de compra en la persistencia.
   * * @param ordenDTO El DTO con la información recabada desde la interfaz.
   * @throws PersistenciaException Si fallan las validaciones o la inserción.
   */
  public static void guardar(OrdenCompraDTO ordenDTO) throws PersistenciaException {
    entidades.OrdenCompra ordenDominio = ADAPTADOR.aDominio(ordenDTO);
    PERSISTENCIA.guardarOrdenCompra(ordenDominio);
  }

  /**
   * Actualiza la información de una orden de compra existente.
   * * @param ordenDTO DTO con los datos modificados.
   * @throws PersistenciaException Si la orden no existe o la actualización falla.
   */
  public static void actualizar(OrdenCompraDTO ordenDTO) throws PersistenciaException {
    entidades.OrdenCompra ordenDominio = ADAPTADOR.aDominio(ordenDTO);
    PERSISTENCIA.actualizarOrdenCompra(ordenDominio);
  }

  /**
   * Elimina del sistema una orden de compra mediante su identificador.
   * * @param numero Código de la orden a remover.
   * @throws PersistenciaException Si la operación de borrado falla.
   */
  public static void eliminar(String numero) throws PersistenciaException {
    PERSISTENCIA.eliminarOrdenCompra(numero);
  }

}