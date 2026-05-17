package diseñadores.negocios.proveedores;

import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import excepciones.NegocioException;

import java.util.List;

public interface IProveedores {

  List<ProveedorDTO> obtenerProveedores() throws NegocioException;

  ProveedorDTO obtenerProveedorPorCodigo(String codigo) throws NegocioException;

  void guardarProveedor(ProveedorDTO proveedor) throws NegocioException;

  void actualizarProveedor(ProveedorDTO proveedor) throws NegocioException;

  int contarProveedoresActivos() throws NegocioException;

  List<OrdenCompraDTO> obtenerOrdenesCompra() throws NegocioException;

  void guardarOrdenCompra(OrdenCompraDTO orden) throws NegocioException;

  void actualizarOrdenCompra(OrdenCompraDTO orden) throws NegocioException;

  void cambiarEstadoOrden(String numero, String nuevoEstado) throws NegocioException;

}