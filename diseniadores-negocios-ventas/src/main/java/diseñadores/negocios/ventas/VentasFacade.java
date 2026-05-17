package diseñadores.negocios.ventas;

import diseñadores.negocios.dto.*;
import excepciones.NegocioException;
import java.math.BigDecimal;
import java.util.List;

public class VentasFacade implements IVentas {

  private final VentasControl control;

  public VentasFacade() {
    this.control = new VentasControl();
  }

  public VentasFacade(VentasControl control) {
    this.control = control;
  }

  @Override
  public List<ProductoDTO> obtenerCatalogo() throws NegocioException {
    return control.obtenerCatalogo();
  }

  @Override
  public boolean existeProducto(EscanearProductoDTO d) throws NegocioException {
    return control.existeProducto(d);
  }

  @Override
  public boolean tieneStock(EscanearProductoDTO d) throws NegocioException {
    return control.tieneStock(d);
  }

  @Override
  public ProductoDTO procesarProducto(VentaDTO v, EscanearProductoDTO d) throws NegocioException {
    return control.procesarProducto(v, d);
  }

  @Override
  public ResultadoPagoDTO procesarPagoEfectivo(VentaDTO v, PagoEfectivoDTO d) throws NegocioException {
    return control.procesarPagoEfectivo(v, d);
  }

  @Override
  public ResultadoPagoDTO procesarPagoTarjeta(VentaDTO v, PagoTarjetaDTO d) throws NegocioException {
    return control.procesarPagoTarjeta(v, d);
  }

  @Override
  public ResultadoPagoDTO procesarPagoTransferencia(VentaDTO v, PagoTransferenciaDTO d) throws NegocioException {
    return control.procesarPagoTransferencia(v, d);
  }

  @Override
  public ResultadoPagoDTO procesarPagoQr(VentaDTO v, PagoQrDTO d) throws NegocioException {
    return control.procesarPagoQr(v, d);
  }

  @Override
  public BigDecimal procesarCalcularCambio(VentaDTO v, BigDecimal ef) throws NegocioException {
    return control.procesarCalcularCambio(v, ef);
  }

  @Override
  public void procesarFinalizarVenta(VentaDTO v) throws NegocioException {
    control.procesarFinalizarVenta(v);
  }

  @Override
  public TicketDTO generarTicket(VentaDTO v, BigDecimal ef) throws NegocioException {
    return control.generarTicket(v, ef);
  }

  @Override
  public void guardarProducto(ProductoDTO producto) throws NegocioException {
    control.guardarProducto(producto);
  }

  @Override
  public void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws NegocioException {
    control.actualizarStockCompleto(codigo, nuevoStock, nuevoMinimo, nuevoMaximo);
  }

  @Override
  public List<VentaDTO> obtenerHistorialVentas() throws NegocioException {
    return control.obtenerHistorialVentas();
  }
}