package diseñadores.negocios.dto;

public class ItemConteoDTO {

  private String codigoConteo;
  private String fecha = java.time.LocalDateTime.now()
    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  private String auditor; 

  private String codigo; 
  private String nombre;
  private int stockSistema;
  private int stockFisico;
  private boolean verificado;

  public ItemConteoDTO() {
  }

  public ItemConteoDTO(String codigoConteo, String auditor, String codigo, String nombre, int stockSistema, int stockFisico) {
    this.codigoConteo = codigoConteo;
    this.auditor = auditor;
    this.codigo = codigo;
    this.nombre = nombre;
    this.stockSistema = stockSistema;
    this.stockFisico = stockFisico;
    this.verificado = (stockSistema == stockFisico);
  }

  public int getDiferencia() {
    return stockFisico - stockSistema;
  }

  public String getEstado() {
    return verificado ? "Verificado" : "Pendiente";
  }

  // --- Getters y Setters ---

  public String getCodigoConteo() {
    return codigoConteo;
  }

  public void setCodigoConteo(String codigoConteo) {
    this.codigoConteo = codigoConteo;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getAuditor() {
    return auditor;
  }

  public void setAuditor(String auditor) {
    this.auditor = auditor;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getStockSistema() {
    return stockSistema;
  }

  public void setStockSistema(int stockSistema) {
    this.stockSistema = stockSistema;
    this.verificado = (this.stockSistema == this.stockFisico);
  }

  public int getStockFisico() {
    return stockFisico;
  }

  public void setStockFisico(int stockFisico) {
    this.stockFisico = stockFisico;
    this.verificado = (this.stockSistema == this.stockFisico);
  }

  public boolean isVerificado() {
    return verificado;
  }

  public void setVerificado(boolean verificado) {
    this.verificado = verificado;
  }
}