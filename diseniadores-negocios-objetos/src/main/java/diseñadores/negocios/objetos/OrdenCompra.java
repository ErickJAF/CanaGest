package diseñadores.negocios.objetos;

import adaptadores.OrdenCompraNegocioAdapter;
import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;

/**
 * Puente de conexión que adapta los DTOs de la UI hacia el dominio limpio
 * requerido por la capa de persistencia.
 * @author ERICK
 */
public class OrdenCompra {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final OrdenCompraNegocioAdapter ADAPTADOR = new OrdenCompraNegocioAdapter();

  public static List<OrdenCompraDTO> obtenerTodas() throws PersistenciaException {
    // 1. Recupera la lista de entidades de dominio puro
    List<entidades.OrdenCompra> listaDominio = PERSISTENCIA.obtenerOrdenesCompra();
    // 2. Las convierte a DTOs para la interfaz gráfica
    return ADAPTADOR.listaADTO(listaDominio);
  }

  public static OrdenCompraDTO obtenerPorNumero(String numero) throws PersistenciaException {
    // 1. Busca la entidad de dominio por número
    entidades.OrdenCompra ordenDominio = PERSISTENCIA.obtenerOrdenCompraPorNumero(numero);
    // 2. La mapea a DTO
    return ADAPTADOR.aDTO(ordenDominio);
  }

  public static void guardar(OrdenCompraDTO ordenDTO) throws PersistenciaException {
    // 1. Transforma el DTO de la UI al dominio limpio de Mongo
    entidades.OrdenCompra ordenDominio = ADAPTADOR.aDominio(ordenDTO);
    // 2. Lo manda a guardar a la base de datos
    PERSISTENCIA.guardarOrdenCompra(ordenDominio);
  }

  public static void actualizar(OrdenCompraDTO ordenDTO) throws PersistenciaException {
    // 1. Transforma a dominio
    entidades.OrdenCompra ordenDominio = ADAPTADOR.aDominio(ordenDTO);
    // 2. Actualiza en la persistencia
    PERSISTENCIA.actualizarOrdenCompra(ordenDominio);
  }

  public static void eliminar(String numero) throws PersistenciaException {
    // Pasa directo ya que requiere un String plano
    PERSISTENCIA.eliminarOrdenCompra(numero);
  }

}