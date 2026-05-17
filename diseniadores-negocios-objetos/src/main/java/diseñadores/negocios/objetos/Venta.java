package diseñadores.negocios.objetos;

import adaptadores.VentaNegocioAdapter;
import diseñadores.negocios.dto.VentaDTO;
import diseñadores.persistencia.IPersistencia;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.PersistenciaException;

import java.util.List;

public class Venta {

  private static final IPersistencia PERSISTENCIA = PersistenciaFacade.getInstancia();
  private static final VentaNegocioAdapter ADAPTADOR = new VentaNegocioAdapter();

  public static List<VentaDTO> obtenerTodas() throws PersistenciaException {
    // 1. Recupera la lista de entidades de dominio limpio
    List<entidades.Venta> listaDominio = PERSISTENCIA.obtenerVentas();
    // 2. Las adapta a DTOs legibles para la interfaz
    return ADAPTADOR.listaADTO(listaDominio);
  }

  public static VentaDTO obtenerPorFolio(String folio) throws PersistenciaException {
    // 1. Busca en la persistencia usando el método real por Código de Venta
    entidades.Venta ventaDominio = PERSISTENCIA.obtenerVentaPorCodigoVenta(folio);
    // 2. La mapea a DTO de forma segura
    return ADAPTADOR.aDTO(ventaDominio);
  }

  public static void guardar(VentaDTO venta) throws PersistenciaException {
    // 1. Transforma el DTO al dominio limpio de Mongo
    entidades.Venta ventaDominio = ADAPTADOR.aDominio(venta);
    // 2. Ejecuta el guardado en la persistencia
    PERSISTENCIA.guardarVenta(ventaDominio);
  }

  public static void actualizar(VentaDTO venta) throws PersistenciaException {
    // 1. Convierte el DTO al dominio limpio
    entidades.Venta ventaDominio = ADAPTADOR.aDominio(venta);
    // 2. Actualiza el documento en la base de datos
    PERSISTENCIA.actualizarVenta(ventaDominio);
  }

  public static void eliminar(String folio) throws PersistenciaException {
    // Pasa directo el String del folio al método de eliminación real
    PERSISTENCIA.eliminarVenta(folio);
  }

}