package diseñadores.negocios.dto;

import java.math.BigDecimal;

/**
 * Objeto de Transferencia de Datos (DTO) con la información de un proveedor.
 * * @author icoro
 */
public class ProveedorDTO {

  private String nombre;
  private String codigo;
  private String contacto;
  private String telefono;
  private String email;
  private String direccion;
  private String terminosPago;
  private boolean activo;
  private BigDecimal precioProveedor;
  private String tiempoEntregaProveedor;

  /**
   * Constructor vacío por defecto.
   */
  public ProveedorDTO() {
  }

  /**
   * Constructor estándar para instanciar a un proveedor.
   * * @param nombre       Nombre de la empresa proveedora.
   * @param codigo       Código de identificación.
   * @param contacto     Nombre de la persona de contacto.
   * @param telefono     Teléfono de contacto.
   * @param email        Correo electrónico corporativo.
   * @param direccion    Dirección física.
   * @param terminosPago Términos y condiciones de pago.
   * @param activo       Indica si el proveedor está operando actualmente.
   */
  public ProveedorDTO(String nombre, String codigo, String contacto, String telefono,
    String email, String direccion, String terminosPago, boolean activo) {
    this.nombre = nombre;
    this.codigo = codigo;
    this.contacto = contacto;
    this.telefono = telefono;
    this.email = email;
    this.direccion = direccion;
    this.terminosPago = terminosPago;
    this.activo = activo;
  }

  /** @return El nombre del proveedor. */
  public String getNombre() { return nombre; }

  /** @param nombre El nombre a establecer. */
  public void setNombre(String nombre) { this.nombre = nombre; }

  /** @return El código identificador del proveedor. */
  public String getCodigo() { return codigo; }

  /** @param codigo El código a establecer. */
  public void setCodigo(String codigo) { this.codigo = codigo; }

  /** @return El nombre del contacto principal. */
  public String getContacto() { return contacto; }

  /** @param contacto El nuevo contacto a establecer. */
  public void setContacto(String contacto) { this.contacto = contacto; }

  /** @return El número de teléfono. */
  public String getTelefono() { return telefono; }

  /** @param telefono El nuevo número de teléfono. */
  public void setTelefono(String telefono) { this.telefono = telefono; }

  /** @return El correo electrónico. */
  public String getEmail() { return email; }

  /** @param email El nuevo correo electrónico. */
  public void setEmail(String email) { this.email = email; }

  /** @return La dirección del proveedor. */
  public String getDireccion() { return direccion; }

  /** @param direccion La nueva dirección. */
  public void setDireccion(String direccion) { this.direccion = direccion; }

  /** @return Las condiciones de pago pactadas. */
  public String getTerminosPago() { return terminosPago; }

  /** @param terminosPago Las nuevas condiciones de pago. */
  public void setTerminosPago(String terminosPago) { this.terminosPago = terminosPago; }

  /** @return True si el proveedor está marcado como activo. */
  public boolean isActivo() { return activo; }

  /** @param activo El estado de actividad a establecer. */
  public void setActivo(boolean activo) { this.activo = activo; }

  /** @return El coste estándar acordado con este proveedor. */
  public BigDecimal getPrecioProveedor() { return precioProveedor; }

  /** @param precioProveedor El nuevo coste estándar. */
  public void setPrecioProveedor(BigDecimal precioProveedor) { this.precioProveedor = precioProveedor; }

  /** @return El tiempo estimado en que el proveedor entrega pedidos. */
  public String getTiempoEntregaProveedor() { return tiempoEntregaProveedor; }

  /** @param tiempoEntregaProveedor El nuevo tiempo estimado de entrega. */
  public void setTiempoEntregaProveedor(String tiempoEntregaProveedor) { this.tiempoEntregaProveedor = tiempoEntregaProveedor; }

}