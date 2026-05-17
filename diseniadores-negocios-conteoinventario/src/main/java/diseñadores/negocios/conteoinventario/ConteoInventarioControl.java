/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diseñadores.negocios.conteoinventario;

import diseñadores.negocios.dto.ItemConteoDTO;
import diseñadores.negocios.inventario.IInventario;
import diseñadores.negocios.inventario.InventarioFacade;
import diseñadores.negocios.objetos.ConteoInventario;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author ERICK
 */
public class ConteoInventarioControl {

  private final IInventario serviciosInventario;

  public ConteoInventarioControl() {
    this.serviciosInventario = new InventarioFacade();
  }

  public ConteoInventarioControl(IInventario serviciosInventario) {
    this.serviciosInventario = serviciosInventario;
  }

  public void registrarYAplicarConteo(ItemConteoDTO item) throws NegocioException {
    validarItemNoNulo(item);
    validarDatosObligatorios(item);

    // Si la interfaz no le generó un código de lote/conteo, se lo asignamos aquí
    if (item.getCodigoConteo() == null || item.getCodigoConteo().isBlank()) {
      item.setCodigoConteo(generarCodigoConteo());
    }

    // 1. Persistir el registro del conteo físico
    ejecutarGuardadoItemConteo(item);

    // 2. Si hay discrepancia o se requiere asentar el stock físico, impactamos el inventario
    if (item.getDiferencia() != 0) {
      try {
        serviciosInventario.actualizarStock(item.getCodigo(), item.getStockFisico());
      } catch (NegocioException e) {
        throw new NegocioException("El conteo se registró, pero falló la actualización del stock real para el producto: " + item.getCodigo(), e);
      }
    }
  }

  public List<ItemConteoDTO> obtenerHistorialConteos() throws NegocioException {
    try {
      return ConteoInventario.obtenerTodos();
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al recuperar el historial de conteos físicos de inventario", e);
    }
  }

  public List<ItemConteoDTO> obtenerConteosConDiscrepancia() throws NegocioException {
    return obtenerHistorialConteos().stream()
        .filter(item -> item.getDiferencia() != 0)
        .collect(Collectors.toList());
  }

  // --- Métodos de Validación y Soporte ---

  private void validarItemNoNulo(ItemConteoDTO item) {
    if (item == null) {
      throw new IllegalArgumentException("El registro de conteo no puede ser nulo.");
    }
  }

  private void validarDatosObligatorios(ItemConteoDTO item) {
    if (item.getAuditor() == null || item.getAuditor().isBlank()) {
      throw new IllegalArgumentException("El nombre del auditor es obligatorio.");
    }
    if (item.getCodigo() == null || item.getCodigo().isBlank()) {
      throw new IllegalArgumentException("El código del producto es obligatorio.");
    }
    if (item.getStockFisico() < 0) {
      throw new IllegalArgumentException("El stock físico no puede ser un valor negativo.");
    }
  }

  private String generarCodigoConteo() {
    return "CNT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  private void ejecutarGuardadoItemConteo(ItemConteoDTO item) throws NegocioException {
    try {
      ConteoInventario.guardar(item);
    } catch (PersistenciaException e) {
      throw new NegocioException("Error al almacenar el registro de conteo físico en la base de datos", e);
    }
  }
}
