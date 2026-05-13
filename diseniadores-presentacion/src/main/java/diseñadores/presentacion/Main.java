package diseñadores.presentacion;

import diseniadores.negocios.dto.UsuarioDTO;
import diseniadores.negocios.inventario.*;
import diseniadores.negocios.proveedores.*;
import diseniadores.negocios.usuarios.IUsuarios;
import diseniadores.negocios.usuarios.UsuariosFacade;
import diseniadores.negocios.ventas.VentasFacade;
import diseniadores.presentacion.control.VentasControl;
import diseniadores.presentacion.frame.*;
import diseniadores.presentacion.utilidad.*;
import javax.swing.SwingUtilities;

public class Main {

  public static void main(String[] args) {
    Fuentes.cargar();

    InventarioControl inventarioControl = new InventarioControl();

    IUsuarios usuariosFachada = new UsuariosFacade();
    IInventario inventarioFachada = new InventarioFacade(inventarioControl);
    IProveedores proveedoresFachada = new ProveedoresFacade();

    VentasFacade ventasFachada = new VentasFacade();

    VentasControl control = new VentasControl(ventasFachada, usuariosFachada, inventarioFachada, proveedoresFachada, new UsuarioDTO());

    SwingUtilities.invokeLater(() -> {
      new PantallaAutenticacion(control).setVisible(true);
    });
  }

}