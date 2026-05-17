package diseñadores.negocios.objetos;

import adaptadores.ProductoProveedorNegocioAdapter;
import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;

/**
 * Puente de conexión directo para operaciones de Proveedores.
 * @author ERICK
 */
public class Proveedor {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final ProductoProveedorNegocioAdapter ADAPTADOR = new ProductoProveedorNegocioAdapter();

  public static List<ProveedorDTO> obtenerTodos() throws PersistenciaException {
    // 1. Recupera la lista de entidades de dominio limpio
    List<entidades.Proveedor> listaDominio = PERSISTENCIA.obtenerProveedores();
    // 2. Las mapea a la lista de DTOs para la UI
    return ADAPTADOR.listaProveedoresADTO(listaDominio);
  }

  public static ProveedorDTO obtenerPorCodigo(String codigo) throws PersistenciaException {
    // 1. Busca la entidad de dominio por su código
    entidades.Proveedor proveedorDominio = PERSISTENCIA.obtenerProveedorPorCodigo(codigo);
    // 2. La transforma a DTO
    return ADAPTADOR.proveedorADTO(proveedorDominio);
  }

  public static void guardar(ProveedorDTO proveedorDTO) throws PersistenciaException {
    // 1. Transforma el DTO de la UI al dominio de Mongo
    entidades.Proveedor proveedorDominio = ADAPTADOR.proveedorADominio(proveedorDTO);
    // 2. Lo guarda en la persistencia
    PERSISTENCIA.guardarProveedor(proveedorDominio);
  }

  public static void actualizar(ProveedorDTO proveedorDTO) throws PersistenciaException {
    // 1. Transforma a dominio limpio
    entidades.Proveedor proveedorDominio = ADAPTADOR.proveedorADominio(proveedorDTO);
    // 2. Actualiza en la base de datos
    PERSISTENCIA.actualizarProveedor(proveedorDominio);
  }

  public static void eliminar(String codigo) throws PersistenciaException {
    // Pasa directo usando el String plano del código
    PERSISTENCIA.eliminarProveedor(codigo);
  }

}