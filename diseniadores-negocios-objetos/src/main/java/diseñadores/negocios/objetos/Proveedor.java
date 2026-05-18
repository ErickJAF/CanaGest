package diseñadores.negocios.objetos;

import adaptadores.ProductoProveedorNegocioAdapter;
import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;

/**
 * Puente de conexión (Facade) para las operaciones lógicas de Proveedores.
 * * @author icoro
 */
public class Proveedor {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final ProductoProveedorNegocioAdapter ADAPTADOR = new ProductoProveedorNegocioAdapter();

  /**
   * Recupera el listado completo de proveedores de la base de datos.
   * * @return Lista de ProveedorDTO.
   * @throws PersistenciaException En caso de error de acceso a datos.
   */
  public static List<ProveedorDTO> obtenerTodos() throws PersistenciaException {
    List<entidades.Proveedor> listaDominio = PERSISTENCIA.obtenerProveedores();
    return ADAPTADOR.listaProveedoresADTO(listaDominio);
  }

  /**
   * Localiza un proveedor específico a través de su código.
   * * @param codigo Código que identifica al proveedor.
   * @return ProveedorDTO con la información encontrada o null.
   * @throws PersistenciaException En caso de error en base de datos.
   */
  public static ProveedorDTO obtenerPorCodigo(String codigo) throws PersistenciaException {
    entidades.Proveedor proveedorDominio = PERSISTENCIA.obtenerProveedorPorCodigo(codigo);
    return ADAPTADOR.proveedorADTO(proveedorDominio);
  }

  /**
   * Registra un nuevo proveedor en el sistema.
   * * @param proveedorDTO Datos del proveedor a insertar.
   * @throws PersistenciaException Si ocurre un error al persistir.
   */
  public static void guardar(ProveedorDTO proveedorDTO) throws PersistenciaException {
    entidades.Proveedor proveedorDominio = ADAPTADOR.proveedorADominio(proveedorDTO);
    PERSISTENCIA.guardarProveedor(proveedorDominio);
  }

  /**
   * Actualiza los datos de un proveedor ya existente.
   * * @param proveedorDTO Datos modificados del proveedor.
   * @throws PersistenciaException Si la modificación falla.
   */
  public static void actualizar(ProveedorDTO proveedorDTO) throws PersistenciaException {
    entidades.Proveedor proveedorDominio = ADAPTADOR.proveedorADominio(proveedorDTO);
    PERSISTENCIA.actualizarProveedor(proveedorDominio);
  }

  /**
   * Borra a un proveedor de la base de datos.
   * * @param codigo Código identificador del proveedor.
   * @throws PersistenciaException Si el borrado falla.
   */
  public static void eliminar(String codigo) throws PersistenciaException {
    PERSISTENCIA.eliminarProveedor(codigo);
  }

}