package diseñadores.negocios.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class VentaDTO {

  private List<ItemVentaDTO> items;
  private BigDecimal subtotal;
  private BigDecimal iva;
  private BigDecimal total;
  private int totalUnidades;
  private boolean pagada;
  private String folio;
  private TipoPago tipoPago;
  private String fecha = java.time.LocalDateTime.now()
    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  private String cajero;

  // ====================================================================
  // NUEVOS ATRIBUTOS FALTANTES (Requeridos por VentasControl)
  // ====================================================================
  private String codigoVenta;
  private String fechaHora;
  private String metodoPago;
  private String numeroAutorizacion;
  private BigDecimal cambio;

  public VentaDTO() {
    this.items = new ArrayList<>();
    this.pagada = false;
    this.total = BigDecimal.ZERO;
    this.subtotal = BigDecimal.ZERO;
    this.iva = BigDecimal.ZERO;
    this.cambio = BigDecimal.ZERO; // Inicializado por defecto
    this.folio = null;
    this.fechaHora = this.fecha; // Sincronizado con tu formato de fecha original
  }

  public void agregarProducto(ProductoDTO producto) {
    ItemVentaDTO itemExistente = items.stream()
      .filter(i -> i.getCodigo().equals(producto.getCodigo()))
      .findFirst()
      .orElse(null);

    if (itemExistente != null) {
      int index = items.indexOf(itemExistente);
      items.set(index, itemExistente.conCantidad(itemExistente.getCantidad() + 1));
    } else {
      items.add(new ItemVentaDTO(producto.getCodigo(), producto.getNombre(), producto.getPrecio(), 1));
    }

    recalcularTotales();
  }

  private void recalcularTotales() {
    this.total = items.stream()
      .map(ItemVentaDTO::getSubtotal)
      .reduce(BigDecimal.ZERO, BigDecimal::add)
      .setScale(2, RoundingMode.HALF_UP);
    this.subtotal = this.total.divide(BigDecimal.valueOf(1.16), 2, RoundingMode.HALF_UP);
    this.iva = this.total.subtract(this.subtotal).setScale(2, RoundingMode.HALF_UP);
    this.totalUnidades = items.stream().mapToInt(ItemVentaDTO::getCantidad).sum();
  }

  // ====================================================================
  // GETTERS Y SETTERS ORIGINALES
  // ====================================================================

  public List<ItemVentaDTO> getItems() {
    return items;
  }

  public void setItems(List<ItemVentaDTO> items) {
    this.items = items;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getIva() {
    return iva;
  }

  public void setIva(BigDecimal iva) {
    this.iva = iva;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public int getTotalUnidades() {
    return totalUnidades;
  }

  public void setTotalUnidades(int totalUnidades) {
    this.totalUnidades = totalUnidades;
  }

  public boolean isPagada() {
    return pagada;
  }

  public void setPagada(boolean pagada) {
    this.pagada = pagada;
  }

  public String getFolio() {
    return folio;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public TipoPago getTipoPago() {
    return tipoPago;
  }

  public void setTipoPago(TipoPago tipoPago) {
    this.tipoPago = tipoPago;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getCajero() {
    return cajero;
  }

  public void setCajero(String cajero) {
    this.cajero = cajero;
  }



  public String getCodigoVenta() {
    return codigoVenta;
  }

  public void setCodigoVenta(String codigoVenta) {
    this.codigoVenta = codigoVenta;
  }

  public String getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(String fechaHora) {
    this.fechaHora = fechaHora;
  }

  public String getMetodoPago() {
    return metodoPago;
  }

  public void setMetodoPago(String metodoPago) {
    this.metodoPago = metodoPago;
  }

  public String getNumeroAutorizacion() {
    return numeroAutorizacion;
  }

  public void setNumeroAutorizacion(String numeroAutorizacion) {
    this.numeroAutorizacion = numeroAutorizacion;
  }

  public BigDecimal getCambio() {
    return cambio;
  }

  public void setCambio(BigDecimal cambio) {
    this.cambio = cambio;
  }
}