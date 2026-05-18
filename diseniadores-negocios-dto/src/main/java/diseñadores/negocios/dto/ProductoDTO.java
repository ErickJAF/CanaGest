package diseñadores.negocios.dto;

import java.math.BigDecimal;

/**
 * Objeto de Transferencia de Datos (DTO) para la gestión de productos en inventario.
 * * @author icoro
 */
public class ProductoDTO {

  private String codigo;
  private String nombre;
  private BigDecimal precio;
  private int stockActual;
  private int stockMinimo;
  private int stockMaximo;
  private String fechaModificacion;
  private ProveedorDTO proveedor;

  /**
   * Constructor vacío por defecto.
   */
  public ProductoDTO() {
  }

  /**
   * Constructor completo para instanciar un producto con todos sus atributos.
   * La fecha de modificación se inicializa automáticamente al día actual.
   * * @param codigo      Código único del producto.
   * @param nombre      Nombre comercial del producto.
   * @param precio      Precio unitario actual.
   * @param stockActual Unidades en existencia.
   * @param stockMinimo Límite inferior permitido de stock.
   * @param stockMaximo Límite superior permitido de stock.
   * @param proveedor   Proveedor predeterminado del producto.
   */
  public ProductoDTO(String codigo, String nombre, BigDecimal precio, int stockActual, int stockMinimo, int stockMaximo, ProveedorDTO proveedor) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.precio = precio;
    this.stockActual = stockActual;
    this.stockMinimo = stockMinimo;
    this.stockMaximo = stockMaximo;
    this.fechaModificacion = java.time.LocalDate.now().toString();
    this.proveedor = proveedor;
  }

  /** @return El código del producto. */
  public String getCodigo() { return codigo; }

  /** @param codigo El nuevo código. */
  public void setCodigo(String codigo) { this.codigo = codigo; }

  /** @return El nombre del producto. */
  public String getNombre() { return nombre; }

  /** @param nombre El nuevo nombre. */
  public void setNombre(String nombre) { this.nombre = nombre; }

  /** @return El precio del producto. */
  public BigDecimal getPrecio() { return precio; }

  /** @param precio El nuevo precio. */
  public void setPrecio(BigDecimal precio) { this.precio = precio; }

  /** @return Alias para obtener el stock actual. */
  public int getStock() { return stockActual; }

  /** @return El stock actual del producto. */
  public int getStockActual() { return stockActual; }

  /** * @param stockActual El nuevo stock a establecer. 
   * Actualiza automáticamente la fecha de modificación. 
   */
  public void setStockActual(int stockActual) {
    this.stockActual = stockActual;
    this.fechaModificacion = java.time.LocalDate.now().toString();
  }

  /** @return El stock mínimo requerido. */
  public int getStockMinimo() { return stockMinimo; }

  /** * @param stockMinimo El nuevo stock mínimo. 
   * Actualiza automáticamente la fecha de modificación. 
   */
  public void setStockMinimo(int stockMinimo) {
    this.stockMinimo = stockMinimo;
    this.fechaModificacion = java.time.LocalDate.now().toString();
  }

  /** @return El stock máximo permitido. */
  public int getStockMaximo() { return stockMaximo; }

  /** * @param stockMaximo El nuevo stock máximo. 
   * Actualiza automáticamente la fecha de modificación. 
   */
  public void setStockMaximo(int stockMaximo) {
    this.stockMaximo = stockMaximo;
    this.fechaModificacion = java.time.LocalDate.now().toString();
  }

  /** @return La fecha de la última alteración de inventario. */
  public String getFechaModificacion() { return fechaModificacion; }

  /** @param fechaModificacion La nueva fecha a establecer manualmente. */
  public void setFechaModificacion(String fechaModificacion) { this.fechaModificacion = fechaModificacion; }

  /** * Alias para establecer el stock actual. 
   * Actualiza automáticamente la fecha de modificación. 
   * * @param stock El nuevo stock. 
   */
  public void setStock(int stock) {
    this.stockActual = stock;
    this.fechaModificacion = java.time.LocalDate.now().toString();
  }

  /** @return El proveedor asignado a este producto. */
  public ProveedorDTO getProveedor() { return proveedor; }

  /** @param proveedor El nuevo proveedor a asignar. */
  public void setProveedor(ProveedorDTO proveedor) { this.proveedor = proveedor; }

  /** @return True si el stock actual es menor al mínimo requerido. */
  public boolean estaBajoMinimo() { return stockActual < stockMinimo; }

  /** @return True si el stock actual excede el límite máximo. */
  public boolean estaSobreMaximo() { return stockActual > stockMaximo; }

  /** @return True si el stock actual es menor o igual al mínimo, indicando necesidad de reorden. */
  public boolean necesitaReorden() { return stockActual <= stockMinimo; }

  @Override
  public String toString() {
    return "ProductoDTO{" + "codigo=" + codigo + ", nombre=" + nombre + ", precio=" + precio + ", stockActual=" + stockActual + ", proveedor=" + proveedor + '}';
  }
}