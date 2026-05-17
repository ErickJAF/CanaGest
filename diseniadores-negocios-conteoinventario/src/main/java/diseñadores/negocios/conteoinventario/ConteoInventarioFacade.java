/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diseñadores.negocios.conteoinventario;

import diseñadores.negocios.dto.ItemConteoDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author ERICK
 */
public class ConteoInventarioFacade implements IConteoInventario {

  private final ConteoInventarioControl control;

  public ConteoInventarioFacade() {
    this.control = new ConteoInventarioControl();
  }

  public ConteoInventarioFacade(ConteoInventarioControl control) {
    this.control = control;
  }

  @Override
  public void registrarYAplicarConteo(ItemConteoDTO item) throws NegocioException {
    control.registrarYAplicarConteo(item);
  }

  @Override
  public List<ItemConteoDTO> obtenerHistorialConteos() throws NegocioException {
    return control.obtenerHistorialConteos();
  }

  @Override
  public List<ItemConteoDTO> obtenerConteosConDiscrepancia() throws NegocioException {
    return control.obtenerConteosConDiscrepancia();
  }
}
