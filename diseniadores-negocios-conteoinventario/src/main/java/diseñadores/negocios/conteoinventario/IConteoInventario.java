/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package diseñadores.negocios.conteoinventario;

import diseñadores.negocios.dto.ItemConteoDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author ERICK
 */
public interface IConteoInventario {

  void registrarYAplicarConteo(ItemConteoDTO item) throws NegocioException;

  List<ItemConteoDTO> obtenerHistorialConteos() throws NegocioException;

  List<ItemConteoDTO> obtenerConteosConDiscrepancia() throws NegocioException;

}