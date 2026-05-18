package diseñadores.negocios.proveedores;

import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import excepciones.NegocioException;

import java.util.List;

public class ProveedoresFacade implements IProveedores {

  private final ProveedoresControl control;

  public ProveedoresFacade() {
    this.control = new ProveedoresControl();
  }

  public ProveedoresFacade(ProveedoresControl control) {
    this.control = control;
  }

  @Override
  public List<ProveedorDTO> obtenerProveedores() throws NegocioException {
    return control.obtenerTodos();
  }

  @Override
  public ProveedorDTO obtenerProveedorPorCodigo(String codigo) throws NegocioException {
    return control.obtenerPorCodigo(codigo);
  }

  @Override
  public void guardarProveedor(ProveedorDTO proveedor) throws NegocioException {
    control.guardar(proveedor);
  }

  @Override
  public void actualizarProveedor(ProveedorDTO proveedor) throws NegocioException {
    control.actualizar(proveedor);
  }

  @Override
  public int contarProveedoresActivos() throws NegocioException {
    return control.contarActivos();
  }

  @Override
  public List<OrdenCompraDTO> obtenerOrdenesCompra() throws NegocioException {
    return control.obtenerOrdenesCompra();
  }

  @Override
  public void guardarOrdenCompra(OrdenCompraDTO orden) throws NegocioException {
    control.guardarOrdenCompra(orden.getProveedor(), orden.getCantidadProductos(), orden.getTotal(), orden.getProductos());
  }

  @Override
  public void actualizarOrdenCompra(OrdenCompraDTO orden) throws NegocioException {
    control.actualizarOrdenCompra(orden);
  }

  @Override
  public void cambiarEstadoOrden(String numero, String nuevoEstado) throws NegocioException {
    control.cambiarEstadoOrden(numero, nuevoEstado);
  }
}