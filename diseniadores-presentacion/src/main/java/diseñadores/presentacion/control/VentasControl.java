package diseñadores.presentacion.control;

import diseñadores.negocios.dto.*;
import diseñadores.negocios.inventario.IInventario;
import diseñadores.negocios.proveedores.IProveedores;
import diseñadores.negocios.usuarios.IUsuarios;
import diseñadores.negocios.ventas.IVentas;
import excepciones.NegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane; // Por si deseas alertar directo, o puedes quitarlo

public class VentasControl {

  private final IVentas ventasFachada;
  private final IUsuarios usuariosFachada;
  private final IInventario inventarioFachada;
  private final IProveedores proveedoresFachada;
  private final UsuarioDTO usuarioActivo;

  private VentaDTO ventaActual;
  private List<ProductoDTO> catalogoProductos;

  public VentasControl(IVentas ventasFachada, IUsuarios usuariosFachada,
    IInventario inventarioFachada, IProveedores proveedoresFachada,
    UsuarioDTO usuarioActivo) {
    this.ventasFachada = ventasFachada;
    this.usuariosFachada = usuariosFachada;
    this.inventarioFachada = inventarioFachada;
    this.proveedoresFachada = proveedoresFachada;
    this.usuarioActivo = usuarioActivo;

    inicializarEstado();
  }

  private void inicializarEstado() {
    iniciarNuevaVenta();
    refrescarCatalogo();
  }

  // Envoltura para lanzar RuntimeException si algo falla en el negocio
  private void manejarError(Exception e) {
    System.err.println("Error en operación de negocio: " + e.getMessage());
    // Lanzamos RuntimeException para que pase el compilador sin pedir try-catch en las vistas
    throw new RuntimeException(e.getMessage(), e);
  }

  public Optional<UsuarioDTO> autenticar(String nombre, String contrasena) {
    try {
      return usuariosFachada.autenticarse(nombre, contrasena);
    } catch (NegocioException e) {
      manejarError(e);
      return Optional.empty();
    }
  }

  public IVentas getVentasFachada() {
    return ventasFachada;
  }

  public IUsuarios getUsuariosFachada() {
    return usuariosFachada;
  }

  public IInventario getInventarioFachada() {
    return inventarioFachada;
  }

  public IProveedores getProveedoresFachada() {
    return proveedoresFachada;
  }

  public UsuarioDTO getUsuarioActivo() {
    return usuarioActivo;
  }

  public List<ProveedorDTO> obtenerProveedores() {
    try {
      return proveedoresFachada.obtenerProveedores();
    } catch (NegocioException e) {
      manejarError(e);
      return new ArrayList<>();
    }
  }

  public int contarProveedoresActivos() {
    try {
      return proveedoresFachada.contarProveedoresActivos();
    } catch (NegocioException e) {
      manejarError(e);
      return 0;
    }
  }

  public void guardarProveedor(ProveedorDTO proveedor) {
    try {
      proveedoresFachada.guardarProveedor(proveedor);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public void actualizarProveedor(ProveedorDTO proveedor) {
    try {
      proveedoresFachada.actualizarProveedor(proveedor);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public List<OrdenCompraDTO> obtenerOrdenesCompra() {
    try {
      return proveedoresFachada.obtenerOrdenesCompra();
    } catch (NegocioException e) {
      manejarError(e);
      return new ArrayList<>();
    }
  }

  public void guardarOrdenCompra(OrdenCompraDTO orden) {
    try {
      proveedoresFachada.guardarOrdenCompra(orden);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public void cambiarEstadoOrden(String numero, String nuevoEstado) {
    try {
      proveedoresFachada.cambiarEstadoOrden(numero, nuevoEstado);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public List<ProductoDTO> obtenerProductosInventario() {
    try {
      return inventarioFachada.obtenerTodos();
    } catch (NegocioException e) {
      manejarError(e);
      return new ArrayList<>();
    }
  }

  public void ajustarStock(String codigo, int nuevoStockFisico) {
    try {
      inventarioFachada.ajustarStock(codigo, nuevoStockFisico);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public ResultadoEscaneo procesarEscaneo(String codigo) {
    try {
      EscanearProductoDTO dto = new EscanearProductoDTO(codigo);
      return validarYProcesarProducto(dto);
    } catch (NegocioException e) {
      manejarError(e);
      return ResultadoEscaneo.NO_EXISTE;
    }
  }

  public void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) {
    try {
      inventarioFachada.actualizarStockCompleto(codigo, nuevoStock, nuevoMinimo, nuevoMaximo);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public void guardarProducto(ProductoDTO producto) {
    try {
      ventasFachada.guardarProducto(producto);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  private ResultadoEscaneo validarYProcesarProducto(EscanearProductoDTO dto) throws NegocioException {
    if (!ventasFachada.existeProducto(dto)) {
      return ResultadoEscaneo.NO_EXISTE;
    }
    if (!ventasFachada.tieneStock(dto)) {
      return ResultadoEscaneo.SIN_STOCK;
    }
    ventasFachada.procesarProducto(ventaActual, dto);
    recalcularTotales();
    return ResultadoEscaneo.OK;
  }

  public void decrementerItem(ItemVentaDTO item) { // Mantenido nombre original
    decrementarItem(item);
  }

  public void decrementarItem(ItemVentaDTO item) {
    if (item.getCantidad() > 1) {
      ajustarCantidadItem(item, item.getCantidad() - 1);
    } else {
      eliminarItem(item);
    }
  }

  private void ajustarCantidadItem(ItemVentaDTO item, int nuevaCantidad) {
    List<ItemVentaDTO> items = ventaActual.getItems();
    int idx = items.indexOf(item);
    if (idx >= 0) {
      items.set(idx, item.conCantidad(nuevaCantidad));
      recalcularTotales();
    }
  }

  public void eliminarItem(ItemVentaDTO item) {
    ventaActual.getItems().removeIf(i -> i.getCodigo().equalsIgnoreCase(item.getCodigo()));
    recalcularTotales();
  }

  public void cancelarVenta() {
    ventaActual.getItems().clear();
    recalcularTotales();
  }

  public void iniciarNuevaVenta() {
    this.ventaActual = new VentaDTO();
  }

  public boolean carritoVacio() {
    return ventaActual.getItems().isEmpty();
  }

  public ResultadoPagoDTO procesarPagoEfectivo(PagoEfectivoDTO pagoDTO) {
    try {
      return ventasFachada.procesarPagoEfectivo(ventaActual, pagoDTO);
    } catch (NegocioException e) {
      manejarError(e);
      return null;
    }
  }

  public ResultadoPagoDTO procesarPagoTarjeta(PagoTarjetaDTO pagoDTO) {
    try {
      return ventasFachada.procesarPagoTarjeta(ventaActual, pagoDTO);
    } catch (NegocioException e) {
      manejarError(e);
      return null;
    }
  }

  public ResultadoPagoDTO procesarPagoCoDi(PagoQrDTO pagoDTO) {
    try {
      return ventasFachada.procesarPagoQr(ventaActual, pagoDTO);
    } catch (NegocioException e) {
      manejarError(e);
      return null;
    }
  }

  public ResultadoPagoDTO procesarPagoTransferencia(PagoTransferenciaDTO pagoDTO) {
    try {
      return ventasFachada.procesarPagoTransferencia(ventaActual, pagoDTO);
    } catch (NegocioException e) {
      manejarError(e);
      return null;
    }
  }

  public BigDecimal calcularCambio(BigDecimal recibido) {
    try {
      return ventasFachada.procesarCalcularCambio(ventaActual, recibido);
    } catch (NegocioException e) {
      manejarError(e);
      return BigDecimal.ZERO;
    }
  }

  public void finalizarVenta() {
    try {
      ventaActual.setCajero(usuarioActivo.getNombre());
      ventasFachada.procesarFinalizarVenta(ventaActual);
    } catch (NegocioException e) {
      manejarError(e);
    }
  }

  public TicketDTO generarTicket(BigDecimal recibido) {
    try {
      return ventasFachada.generarTicket(ventaActual, recibido);
    } catch (NegocioException e) {
      manejarError(e);
      return null;
    }
  }

  public TicketDTO generarTicket() {
    return generarTicket(BigDecimal.ZERO);
  }

  public List<ProductoDTO> filtrarCatalogo(String query) {
    if (esQueryInvalida(query)) {
      return new ArrayList<>(catalogoProductos);
    }
    return ejecutarFiltro(query.toLowerCase());
  }

  private boolean esQueryInvalida(String query) {
    return query == null || query.isEmpty();
  }

  private List<ProductoDTO> ejecutarFiltro(String q) {
    return catalogoProductos.stream()
      .filter(p -> p.getNombre().toLowerCase().contains(q)
      || p.getCodigo().toLowerCase().contains(q))
      .collect(Collectors.toList());
  }

  public List<ProductoDTO> refrescarCatalogo() {
    try {
      this.catalogoProductos = ventasFachada.obtenerCatalogo();
      return catalogoProductos;
    } catch (NegocioException e) {
      this.catalogoProductos = new ArrayList<>();
      return catalogoProductos;
    }
  }

  public List<ProductoDTO> getCatalogo() {
    return catalogoProductos;
  }

  public VentaDTO getVentaActual() {
    return ventaActual;
  }

  private void recalcularTotales() {
    List<ItemVentaDTO> items = ventaActual.getItems();
    BigDecimal total = calcularTotal(items);
    BigDecimal subtotal = calcularSubtotal(total);
    BigDecimal iva = calcularIva(total, subtotal);
    int totalUnidades = calcularUnidades(items);
    actualizarDatosVenta(total, subtotal, iva, totalUnidades);
  }

  private void actualizarDatosVenta(BigDecimal total, BigDecimal subtotal, BigDecimal iva, int unidades) {
    ventaActual.setTotal(total);
    ventaActual.setSubtotal(subtotal);
    ventaActual.setIva(iva);
    ventaActual.setTotalUnidades(unidades);
  }

  private BigDecimal calcularTotal(List<ItemVentaDTO> items) {
    return items.stream()
      .map(ItemVentaDTO::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add)
      .setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal calcularSubtotal(BigDecimal total) {
    return total.divide(BigDecimal.valueOf(1.16), 2, RoundingMode.HALF_UP);
  }

  private BigDecimal calcularIva(BigDecimal total, BigDecimal subtotal) {
    return total.subtract(subtotal).setScale(2, RoundingMode.HALF_UP);
  }

  private int calcularUnidades(List<ItemVentaDTO> items) {
    return items.stream().mapToInt(ItemVentaDTO::getCantidad).sum();
  }

  public List<VentaDTO> obtenerHistorialVentas() {
    try {
      return ventasFachada.obtenerHistorialVentas();
    } catch (NegocioException e) {
      manejarError(e);
      return new ArrayList<>();
    }
  }
}