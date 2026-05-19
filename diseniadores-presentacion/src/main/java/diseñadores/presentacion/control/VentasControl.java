package diseñadores.presentacion.control;

import diseñadores.negocios.conteoinventario.ConteoInventarioGeneralFacade;
import diseñadores.negocios.conteoinventario.IConteoInventarioGeneral;
import diseñadores.negocios.dto.*;
import diseñadores.negocios.inventario.IInventario;
import diseñadores.negocios.proveedores.IProveedores;
import diseñadores.negocios.usuarios.IUsuarios;
import diseñadores.negocios.ventas.IVentas;
import excepciones.NegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VentasControl {

  private final IVentas ventasFachada;
  private final IUsuarios usuariosFachada;
  private final IInventario inventarioFachada;
  private final IProveedores proveedoresFachada;
  private final UsuarioDTO usuarioActivo;
  
  // MIGRACIÓN: Se cambia el viejo control por la interfaz de la Fachada General
  private final IConteoInventarioGeneral conteoInventarioGeneralFacade;

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
    
    // Inicialización del nuevo subsistema de auditorías
    this.conteoInventarioGeneralFacade = new ConteoInventarioGeneralFacade();

    inicializarEstado();
  }

  private void inicializarEstado() {
    iniciarNuevaVenta();
    refrescarCatalogo();
  }

  private void manejarError(Exception e) {
    System.err.println("❌ ERROR EN OPERACIÓN DE NEGOCIO: " + e.getMessage());
    e.printStackTrace();
    
    // Mostrar diálogo al usuario para que vea el error
    try {
      javax.swing.JOptionPane.showMessageDialog(null,
        "Error en la operación:\n" + e.getMessage(),
        "Error del Sistema",
        javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (Exception dialogEx) {
      // Si falla el diálogo, solo imprimir
      System.err.println("No se pudo mostrar diálogo de error: " + dialogEx.getMessage());
    }
    
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

  public IVentas getVentasFachada() { return ventasFachada; }
  public IUsuarios getUsuariosFachada() { return usuariosFachada; }
  public IInventario getInventarioFachada() { return inventarioFachada; }
  public IProveedores getProveedoresFachada() { return proveedoresFachada; }
  public UsuarioDTO getUsuarioActivo() { return usuarioActivo; }

  // --- MÉTODOS DE PROVEEDORES ---
  public List<ProveedorDTO> obtenerProveedores() {
    try { return proveedoresFachada.obtenerProveedores(); } 
    catch (NegocioException e) { manejarError(e); return new ArrayList<>(); }
  }

  public int contarProveedoresActivos() {
    try { return proveedoresFachada.contarProveedoresActivos(); } 
    catch (NegocioException e) { manejarError(e); return 0; }
  }

  public void guardarProveedor(ProveedorDTO proveedor) {
    try { proveedoresFachada.guardarProveedor(proveedor); } 
    catch (NegocioException e) { manejarError(e); }
  }

  public void actualizarProveedor(ProveedorDTO proveedor) {
    try { proveedoresFachada.actualizarProveedor(proveedor); } 
    catch (NegocioException e) { manejarError(e); }
  }

  // --- MÉTODOS DE ORDENES DE COMPRA ---
  public List<OrdenCompraDTO> obtenerOrdenesCompra() {
    try { return proveedoresFachada.obtenerOrdenesCompra(); } 
    catch (NegocioException e) { manejarError(e); return new ArrayList<>(); }
  }

  public void guardarOrdenCompra(OrdenCompraDTO orden) {
    try { proveedoresFachada.guardarOrdenCompra(orden); } 
    catch (NegocioException e) { manejarError(e); }
  }

  public void cambiarEstadoOrden(String numero, String nuevoEstado) {
    try { proveedoresFachada.cambiarEstadoOrden(numero, nuevoEstado); } 
    catch (NegocioException e) { manejarError(e); }
  }

  // --- MÉTODOS DE INVENTARIO BASE ---
  public List<ProductoDTO> obtenerProductosInventario() {
    try { return inventarioFachada.obtenerTodos(); } 
    catch (NegocioException e) { manejarError(e); return new ArrayList<>(); }
  }

  public void ajustarStock(String codigo, int nuevoStockFisico) {
    try { inventarioFachada.ajustarStock(codigo, nuevoStockFisico); } 
    catch (NegocioException e) { manejarError(e); }
  }

  public void actualizarStockCompleto(String codigo, int nuevoStock, int nuevoMinimo, int nuevoMaximo) {
    try { inventarioFachada.actualizarStockCompleto(codigo, nuevoStock, nuevoMinimo, nuevoMaximo); } 
    catch (NegocioException e) { manejarError(e); }
  }

  // --- MÉTODOS DE FLUJO DE VENTAS ---
  public ResultadoEscaneo procesarEscaneo(String codigo) {
    try {
      EscanearProductoDTO dto = new EscanearProductoDTO(codigo);
      return validarYProcesarProducto(dto);
    } catch (NegocioException e) {
      manejarError(e);
      return ResultadoEscaneo.NO_EXISTE;
    }
  }

  public void guardarProducto(ProductoDTO producto) {
    try { ventasFachada.guardarProducto(producto); } 
    catch (NegocioException e) { manejarError(e); }
  }

  private ResultadoEscaneo validarYProcesarProducto(EscanearProductoDTO dto) throws NegocioException {
    if (!ventasFachada.existeProducto(dto)) return ResultadoEscaneo.NO_EXISTE;
    if (!ventasFachada.tieneStock(dto)) return ResultadoEscaneo.SIN_STOCK;
    
    ventasFachada.procesarProducto(ventaActual, dto);
    recalcularTotales();
    return ResultadoEscaneo.OK;
  }

  public void decrementerItem(ItemVentaDTO item) { decrementarItem(item); }

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

  public void iniciarNuevaVenta() { this.ventaActual = new VentaDTO(); }
  public boolean carritoVacio() { return ventaActual.getItems().isEmpty(); }

  // --- PROCESAMIENTO DE PAGOS Y TICKETS ---
  
  /**
   * Procesa el pago en efectivo y configura los datos necesarios en la venta.
   * CRÍTICO: Este método ahora configura el método de pago antes de retornar.
   */
  public ResultadoPagoDTO procesarPagoEfectivo(PagoEfectivoDTO pagoDTO) {
    try {
      System.out.println("🔵 Procesando pago en efectivo...");
      System.out.println("   Monto recibido: $" + pagoDTO.getMontoRecibido());
      System.out.println("   Total venta: $" + ventaActual.getTotal());
      
      ResultadoPagoDTO resultado = ventasFachada.procesarPagoEfectivo(ventaActual, pagoDTO);
      
      // CRÍTICO: Configurar el método de pago en la venta
      if (resultado != null && resultado.isAprobado()) {
        System.out.println("✅ Pago en efectivo aprobado");
        ventaActual.setMetodoPago("EFECTIVO");
        ventaActual.setCambio(resultado.getCambio());
        System.out.println("   Cambio: $" + resultado.getCambio());
      } else {
        System.err.println("❌ Pago en efectivo rechazado");
      }
      
      return resultado;
    } catch (NegocioException e) { 
      System.err.println("❌ Error al procesar pago en efectivo: " + e.getMessage());
      manejarError(e); 
      return null; 
    }
  }

  /**
   * Procesa el pago con tarjeta y configura los datos necesarios en la venta.
   * CRÍTICO: Este método ahora configura el método de pago y número de autorización.
   */
public ResultadoPagoDTO procesarPagoTarjeta(PagoTarjetaDTO pagoDTO) {
    try {
      System.out.println("🔵 Procesando pago con tarjeta (MOCK SIN SERVIDOR)...");
      System.out.println("   Titular: " + pagoDTO.getTitular());
      
      // 1. Fingimos la espera de conexión de 1.5 segundos
      Thread.sleep(1500); 
      
      // 2. Aprobamos la transacción
      String autorizacion = "AUTH-" + System.currentTimeMillis();
      ResultadoPagoDTO resultado = ResultadoPagoDTO.aprobado(autorizacion);
      
      System.out.println("✅ Pago aprobado por simulador interno");

      // 3. CONFIGURACIÓN DE CAMPOS
      ventaActual.setMetodoPago("TARJETA");
      ventaActual.setTipoPago(TipoPago.TARJETA); 
      // ¡ELIMINADO! ventaActual.setPagada(true); <-- El backend se encargará de esto internamente
      ventaActual.setNumeroAutorizacion(autorizacion);
      
      // Generar códigos de control si no existen
      if (ventaActual.getCodigoVenta() == null || ventaActual.getCodigoVenta().isEmpty()) {
         long time = System.currentTimeMillis();
         ventaActual.setCodigoVenta("VTA-" + time + "-" + (int)(Math.random() * 1000));
      }
      
      if (ventaActual.getFechaHora() == null || ventaActual.getFechaHora().isEmpty()) {
         ventaActual.setFechaHora(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
      }
      
      ventaActual.setCajero(usuarioActivo.getNombre());

      // 4. Guardamos directamente en la base de datos
      System.out.println("💾 Guardando venta de tarjeta en BD...");
      ventasFachada.procesarFinalizarVenta(ventaActual);
      System.out.println("✅ Venta guardada exitosamente en MongoDB como TARJETA");
      
      return resultado;
      
    } catch (Exception e) { 
      System.err.println("❌ Error al procesar tarjeta simulada: " + e.getMessage());
      return null; 
    }
  }

 

  public BigDecimal calcularCambio(BigDecimal recibido) {
    try { return ventasFachada.procesarCalcularCambio(ventaActual, recibido); } 
    catch (NegocioException e) { manejarError(e); return BigDecimal.ZERO; }
  }

  /**
   * Finaliza la venta configurando todos los datos necesarios y persistiendo en la base de datos.
   * CRÍTICO: Genera código único, timestamp y guarda en MongoDB.
   */
  public void finalizarVenta() {
    try {
      System.out.println("🔵 Iniciando finalización de venta...");
      
      // CRÍTICO: Generar código único si no existe
      if (ventaActual.getCodigoVenta() == null || ventaActual.getCodigoVenta().isEmpty()) {
        String codigoVenta = generarCodigoVentaUnico();
        ventaActual.setCodigoVenta(codigoVenta);
        System.out.println("   Código generado: " + codigoVenta);
      }
      
      // CRÍTICO: Configurar timestamp
      if (ventaActual.getFechaHora() == null || ventaActual.getFechaHora().isEmpty()) {
        String fechaHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            .format(new java.util.Date());
        ventaActual.setFechaHora(fechaHora);
        System.out.println("   Fecha/Hora: " + fechaHora);
      }
      
      // Configurar cajero
      ventaActual.setCajero(usuarioActivo.getNombre());
      System.out.println("   Cajero: " + usuarioActivo.getNombre());
      System.out.println("   Método de pago: " + ventaActual.getMetodoPago());
      System.out.println("   Total: $" + ventaActual.getTotal());
      System.out.println("   Items: " + ventaActual.getItems().size());
      
      // CRÍTICO: PERSISTIR EN BASE DE DATOS
      System.out.println("💾 Guardando venta en base de datos...");
      ventasFachada.procesarFinalizarVenta(ventaActual);
      System.out.println("✅ Venta guardada exitosamente en MongoDB");
      
    } catch (NegocioException e) { 
      System.err.println("❌ Error al finalizar venta: " + e.getMessage());
      manejarError(e); 
    }
  }
  
  /**
   * Genera un código de venta único basado en timestamp y un número aleatorio.
   */
  private String generarCodigoVentaUnico() {
    long timestamp = System.currentTimeMillis();
    int random = (int)(Math.random() * 1000);
    return String.format("VTA-%d-%03d", timestamp, random);
  }

  public TicketDTO generarTicket(BigDecimal recibido) {
    try {
      System.out.println("🎫 Generando ticket...");
      TicketDTO ticket = ventasFachada.generarTicket(ventaActual, recibido);
      System.out.println("✅ Ticket generado");
      return ticket;
    } catch (NegocioException e) { 
      System.err.println("❌ Error al generar ticket: " + e.getMessage());
      manejarError(e); 
      return null; 
    }
  }

  public TicketDTO generarTicket() { return generarTicket(BigDecimal.ZERO); }

  // --- CATÁLOGOS Y FILTROS ---
  public List<ProductoDTO> filtrarCatalogo(String query) {
    if (esQueryInvalida(query)) return new ArrayList<>(catalogoProductos);
    return ejecutarFiltro(query.toLowerCase());
  }

  private boolean esQueryInvalida(String query) { return query == null || query.isEmpty(); }

  private List<ProductoDTO> ejecutarFiltro(String q) {
    return catalogoProductos.stream()
      .filter(p -> p.getNombre().toLowerCase().contains(q) || p.getCodigo().toLowerCase().contains(q))
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

  public List<ProductoDTO> getCatalogo() { return catalogoProductos; }
  public VentaDTO getVentaActual() { return ventaActual; }

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
    return items.stream().map(ItemVentaDTO::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal calcularSubtotal(BigDecimal total) { return total.divide(BigDecimal.valueOf(1.16), 2, RoundingMode.HALF_UP); }
  private BigDecimal calcularIva(BigDecimal total, BigDecimal subtotal) { return total.subtract(subtotal).setScale(2, RoundingMode.HALF_UP); }
  private int calcularUnidades(List<ItemVentaDTO> items) { return items.stream().mapToInt(ItemVentaDTO::getCantidad).sum(); }

  public List<VentaDTO> obtenerHistorialVentas() {
    try { return ventasFachada.obtenerHistorialVentas(); } 
    catch (NegocioException e) { manejarError(e); return new ArrayList<>(); }
  }
  
  /**
   * Recupera la sesión de auditoría masiva que se encuentre abierta actualmente 
   * (cuyo flag 'verificadoGlobal' sea falso). 
   * Si no hay ninguna sesión en curso, retorna null para indicarle a la vista 
   * que debe habilitar el botón de apertura.
   */
  public ConteoInventarioGeneralDTO obtenerAuditoriaActiva() {
      try {
          List<ConteoInventarioGeneralDTO> historial = conteoInventarioGeneralFacade.obtenerHistorialSesiones();
          if (historial != null && !historial.isEmpty()) {
              for (ConteoInventarioGeneralDTO aud : historial) {
                  if (!aud.getVerificadoGlobal()) {
                      return aud; 
                  }
              }
          }
          return null;
      } catch (NegocioException e) {
          manejarError(e);
          return null;
      }
  }

  /**
   * Disparador del evento "Iniciar Nuevo Conteo". 
   * Congela el stock actual del catálogo relacional y genera un documento 
   * borrador totalmente nuevo en MongoDB con su folio único.
   */
  public ConteoInventarioGeneralDTO inicializarNuevoConteoGeneral() {
      try {
          ConteoInventarioGeneralDTO nuevaAuditoria = new ConteoInventarioGeneralDTO();
          // Genera el folio base con un prefijo y marca temporal
          nuevaAuditoria.setCodigoGeneral("AUD-" + (System.currentTimeMillis() / 1000));
          String fechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
          nuevaAuditoria.setFechaRegistro(fechaActual);
          nuevaAuditoria.setVerificadoGlobal(false);

          // Tomamos la foto del inventario en este instante
          List<ProductoDTO> productosSistema = obtenerProductosInventario();
          List<ItemConteoDTO> itemsIniciales = new ArrayList<>();

          int secuencia = 1;
          for (ProductoDTO p : productosSistema) {
              // El stock físico inicial se asume igual al del sistema.
              // Los datos de firma del usuario y comentarios se quedan vacíos/null
              // hasta que se use la ventana de ajuste.
              ItemConteoDTO item = new ItemConteoDTO(
                "ITM-" + nuevaAuditoria.getCodigoGeneral() + "-" + secuencia++,
                fechaActual,
                usuarioActivo.getNombre(),
                null, 
                p.getCodigo(), p.getNombre(), p.getStock(), p.getStock()
              );
              item.setComentario("");
              itemsIniciales.add(item);
          }

          nuevaAuditoria.setTodosLosConteos(itemsIniciales);
          nuevaAuditoria.recalcularMetricas();

          // Persiste el contenedor inicial en Mongo
          conteoInventarioGeneralFacade.crearSesionAuditoria(nuevaAuditoria);
          return nuevaAuditoria;

      } catch (NegocioException e) {
          manejarError(e);
          return null;
      }
  }

  /**
   * Vuelca las actualizaciones progresivas en la base de datos de MongoDB.
   * Se ejecuta en caliente inmediatamente después de guardar en la ventana de ajuste,
   * almacenando firmas y justificaciones de forma atómica sin alterar el stock del catálogo.
   */
  public void guardarProgresoAuditoria(ConteoInventarioGeneralDTO sesionGeneral) {
      try {
          if (sesionGeneral != null) {
              sesionGeneral.recalcularMetricas();
              // Invoca de manera segura la persistencia parcial de la fachada
              this.conteoInventarioGeneralFacade.guardarProgresoAuditoria(sesionGeneral);
          }
      } catch (NegocioException e) {
          manejarError(e);
      }
  }

  /**
   * Cierre y Consolidación Final de la auditoría.
   * Valida firmas en desajustes e impacta permanentemente el stock en el catálogo del sistema.
   */
  public void actualizarAuditoriaGeneral(ConteoInventarioGeneralDTO sesionGeneral) {
      try {
          if (sesionGeneral != null) {
              sesionGeneral.recalcularMetricas();
              // Aplica el cambio definitivo en el inventario y cierra la sesión
              this.conteoInventarioGeneralFacade.registrarYAplicarAuditoriaGlobal(sesionGeneral);
          }
      } catch (NegocioException e) {
          manejarError(e);
      }
  }

  /**
   * Busca los detalles completos de una sesión de auditoría mediante su ID/Folio.
   */
  public ConteoInventarioGeneralDTO buscarSesionAuditoriaPorCodigo(String codigoGeneral) {
      try {
          return this.conteoInventarioGeneralFacade.buscarSesionPorCodigo(codigoGeneral);
      } catch (NegocioException e) {
          manejarError(e);
          return null;
      }
  }

  /**
   * Recupera la lista con todo el historial de auditorías del sistema.
   */
  public List<ConteoInventarioGeneralDTO> obtenerHistorialSesionesAuditoria() {
      try {
          return this.conteoInventarioGeneralFacade.obtenerHistorialSesiones();
      } catch (NegocioException e) {
          manejarError(e);
          return new ArrayList<>();
      }
  }
}