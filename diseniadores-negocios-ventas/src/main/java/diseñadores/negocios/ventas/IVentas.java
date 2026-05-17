package diseñadores.negocios.ventas;

import diseñadores.negocios.dto.*;
import excepciones.NegocioException;
import java.math.BigDecimal;
import java.util.List;

public interface IVentas {

  List<ProductoDTO> obtenerCatalogo() throws NegocioException;

  boolean existeProducto(EscanearProductoDTO dto) throws NegocioException;

  boolean tieneStock(EscanearProductoDTO dto) throws NegocioException;

  ProductoDTO procesarProducto(VentaDTO venta, EscanearProductoDTO dto) throws NegocioException;

  ResultadoPagoDTO procesarPagoEfectivo(VentaDTO venta, PagoEfectivoDTO dto) throws NegocioException;

  ResultadoPagoDTO procesarPagoTarjeta(VentaDTO venta, PagoTarjetaDTO dto) throws NegocioException;

  ResultadoPagoDTO procesarPagoTransferencia(VentaDTO venta, PagoTransferenciaDTO dto) throws NegocioException;

  ResultadoPagoDTO procesarPagoQr(VentaDTO venta, PagoQrDTO dto) throws NegocioException;

  BigDecimal procesarCalcularCambio(VentaDTO venta, BigDecimal efectivo) throws NegocioException;

  void procesarFinalizarVenta(VentaDTO venta) throws NegocioException;

  TicketDTO generarTicket(VentaDTO venta, BigDecimal efectivoRecibido) throws NegocioException;

  void guardarProducto(ProductoDTO producto) throws NegocioException;

  void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) throws NegocioException;

  List<VentaDTO> obtenerHistorialVentas() throws NegocioException;

}