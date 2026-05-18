package diseñadores.infraestructura.pagos;

import diseñadores.infraestructura.dto.RespuestaPagoDTO;
import diseñadores.infraestructura.dto.TipoPago;
import java.math.BigDecimal;

/**
 * Fachada limpia de infraestructura. Delega el flujo natural al control 
 * sin generar objetos mutables o clases anónimas complejas.
 * * @author ERICK
 * @version 1.2
 */
public class PagosFacade implements IPagos {

  private final PagosControl control;

  public PagosFacade() {
    this.control = new PagosControl();
  }

  public PagosFacade(PagosControl control) {
    this.control = control;
  }

  @Override
  public RespuestaPagoDTO procesarPago(TipoPago tipo, BigDecimal monto, String referencia, String datos) {
    try {
        return control.procesarPago(tipo, monto, referencia, datos);
    } catch (Exception e) {
        return null; // Si truena o no está implementado, devolvemos null de seguridad
    }
  }
}