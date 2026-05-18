package diseñadores.presentacion.frame;

import diseñadores.negocios.dto.DetalleOrdenCompraDTO;
import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.negocios.objetos.Producto;
import diseñadores.presentacion.control.VentasControl;
import diseñadores.presentacion.utilidad.Bordes;
import diseñadores.presentacion.utilidad.Colores;
import diseñadores.presentacion.utilidad.Fuentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cuadro de diálogo para registrar una nueva orden de compra en el sistema.
 * Calcula de forma automática el total en base a la cantidad y el producto del proveedor seleccionado.
 * * @author icoro
 */
public class RegistrarNuevaOrdenCompra extends JDialog {

  private final VentasControl control;
  private final Runnable onSuccess;
  private List<ProductoDTO> productosDisponibles;
  private ProductoDTO productoSeleccionado;

  private JTextField tfCant;
  private JTextField tfTotal;
  private JLabel lblProductoNombreValor;
  private JLabel lblPrecioValor;

  /**
   * Constructor del diálogo para nueva orden de compra.
   * * @param parent    Ventana padre que invoca el diálogo.
   * @param control   Controlador principal para gestionar las persistencias y lógica.
   * @param onSuccess Callback a ejecutar si la orden se guarda exitosamente.
   */
  public RegistrarNuevaOrdenCompra(JFrame parent, VentasControl control, Runnable onSuccess) {
    super(parent, "Nueva Orden de Compra", true);
    this.control = control;
    this.onSuccess = onSuccess;
    
    // Cargar productos previamente para asociarlos al proveedor
    try {
        this.productosDisponibles = Producto.obtenerTodos();
    } catch (Exception e) {
        this.productosDisponibles = new ArrayList<>();
    }

    setSize(520, 620);
    setLocationRelativeTo(parent);
    construirContenido();
  }

  /**
   * Construye e inicializa los componentes de la interfaz de usuario.
   */
  private void construirContenido() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(28, 32, 28, 32));
    panel.setBackground(Colores.BLANCO);

    JLabel titulo = new JLabel("Nueva Orden de Compra");
    titulo.setFont(Fuentes.b(20));
    titulo.setForeground(Colores.TEXTO_OSCURO);
    titulo.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(titulo);
    panel.add(Box.createVerticalStrut(20));

    List<ProveedorDTO> proveedores = control.obtenerProveedores();
    List<ProveedorDTO> activos = proveedores.stream()
      .filter(ProveedorDTO::isActivo)
      .toList();

    String[] nombresProveedores = activos.stream()
      .map(ProveedorDTO::getNombre)
      .toArray(String[]::new);

    JComboBox<String> comboProveedor = new JComboBox<>(nombresProveedores);
    comboProveedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    comboProveedor.setAlignmentX(LEFT_ALIGNMENT);

    // Labels dinámicos para el producto
    lblProductoNombreValor = new JLabel("-");
    lblProductoNombreValor.setFont(Fuentes.r(13));
    lblProductoNombreValor.setForeground(Colores.AZUL);
    
    lblPrecioValor = new JLabel("0.00");
    lblPrecioValor.setFont(Fuentes.r(13));
    lblPrecioValor.setForeground(Colores.AZUL);

    panel.add(crearEtiqueta("Proveedor"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(comboProveedor);
    panel.add(Box.createVerticalStrut(10));
    
    panel.add(crearEtiqueta("Producto asociado:"));
    panel.add(lblProductoNombreValor);
    panel.add(Box.createVerticalStrut(10));
    
    panel.add(crearEtiqueta("Precio Unitario ($):"));
    panel.add(lblPrecioValor);
    panel.add(Box.createVerticalStrut(10));

    tfCant = crearCampo();
    tfTotal = crearCampo();
    tfTotal.setEditable(false); // El total se calcula automáticamente
    tfTotal.setBackground(Colores.FONDO_GRIS_CLARO);

    panel.add(crearEtiqueta("Cantidad de productos"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(tfCant);
    panel.add(Box.createVerticalStrut(10));
    panel.add(crearEtiqueta("Total Automático ($)"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(tfTotal);
    panel.add(Box.createVerticalStrut(20));

    // Listeners para actualizar dinámicamente los datos
    comboProveedor.addActionListener(e -> actualizarDatosProducto((String) comboProveedor.getSelectedItem()));
    
    tfCant.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { recalcularTotal(); }
        public void removeUpdate(DocumentEvent e) { recalcularTotal(); }
        public void changedUpdate(DocumentEvent e) { recalcularTotal(); }
    });

    // Disparar evento inicial si hay proveedores
    if (nombresProveedores.length > 0) {
        actualizarDatosProducto(nombresProveedores[0]);
    }

    JButton btnCrear = crearBoton("Crear Orden");
    btnCrear.setAlignmentX(LEFT_ALIGNMENT);
    btnCrear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

    btnCrear.addActionListener(e -> guardarOrden(activos, (String) comboProveedor.getSelectedItem()));

    panel.add(btnCrear);

    JScrollPane sp = new JScrollPane(panel);
    sp.setBorder(BorderFactory.createEmptyBorder());
    setContentPane(sp);
  }
  
  /**
   * Busca el producto asociado al proveedor seleccionado y actualiza la UI.
   * * @param nombreProv Nombre del proveedor seleccionado en el JComboBox.
   */
  private void actualizarDatosProducto(String nombreProv) {
      productoSeleccionado = productosDisponibles.stream()
        .filter(p -> p.getProveedor() != null && p.getProveedor().getNombre().equals(nombreProv))
        .findFirst().orElse(null);
        
      if (productoSeleccionado != null) {
          lblProductoNombreValor.setText(productoSeleccionado.getNombre());
          lblPrecioValor.setText(productoSeleccionado.getPrecio().toString());
      } else {
          lblProductoNombreValor.setText("No hay productos asociados");
          lblPrecioValor.setText("0.00");
      }
      recalcularTotal();
  }
  
  /**
   * Recalcula el campo del total basado en el precio unitario y la cantidad ingresada.
   */
  private void recalcularTotal() {
      try {
          if (tfCant.getText().isBlank()) {
              tfTotal.setText("0.00");
              return;
          }
          int cantidad = Integer.parseInt(tfCant.getText().trim());
          BigDecimal precio = new BigDecimal(lblPrecioValor.getText());
          tfTotal.setText(precio.multiply(BigDecimal.valueOf(cantidad)).toString());
      } catch (NumberFormatException ex) {
          tfTotal.setText("0.00");
      }
  }

  /**
   * Ejecuta la lógica para validar y persistir la nueva orden de compra.
   * * @param activos    Lista de proveedores activos.
   * @param nombreProv Nombre del proveedor seleccionado al momento de guardar.
   */
  private void guardarOrden(List<ProveedorDTO> activos, String nombreProv) {
      if (nombreProv == null || activos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay proveedores activos disponibles.",
          "Error", JOptionPane.WARNING_MESSAGE);
        return;
      }

      ProveedorDTO prov = activos.stream()
        .filter(p -> p.getNombre().equals(nombreProv))
        .findFirst().orElse(null);

      if (prov == null || productoSeleccionado == null) {
        JOptionPane.showMessageDialog(this, "Proveedor inválido o sin productos asociados.",
          "Error", JOptionPane.WARNING_MESSAGE);
        return;
      }

      try {
        int cant = Integer.parseInt(tfCant.getText().trim());
        BigDecimal tot = new BigDecimal(tfTotal.getText().trim());

        if (cant <= 0 || tot.compareTo(BigDecimal.ZERO) <= 0) {
          JOptionPane.showMessageDialog(this, "La cantidad y el total deben ser mayores a cero.",
            "Error", JOptionPane.WARNING_MESSAGE);
          return;
        }

        // Crear el detalle del producto para agregarlo a la orden
        DetalleOrdenCompraDTO detalle = new DetalleOrdenCompraDTO(
                productoSeleccionado.getCodigo(), 
                productoSeleccionado.getNombre(), 
                cant, 
                productoSeleccionado.getPrecio()
        );
        List<DetalleOrdenCompraDTO> listaDetalle = new ArrayList<>();
        listaDetalle.add(detalle);

        // Generar la orden con los detalles asociados
        OrdenCompraDTO nueva = new OrdenCompraDTO(null, null, "Pendiente", tot, prov, null, listaDetalle);
        
        control.guardarOrdenCompra(nueva);
        if (onSuccess != null) {
          onSuccess.run();
        }
        dispose();

      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Ingrese números válidos en cantidad.",
          "Error", JOptionPane.WARNING_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
      }
  }

  /**
   * Crea una etiqueta estandarizada para el formulario.
   * * @param texto Texto de la etiqueta.
   * @return JLabel configurado.
   */
  private JLabel crearEtiqueta(String texto) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(Fuentes.b(12));
    lbl.setForeground(Colores.TEXTO_OSCURO);
    lbl.setAlignmentX(LEFT_ALIGNMENT);
    return lbl;
  }

  /**
   * Crea un campo de texto estandarizado para el formulario.
   * * @return JTextField configurado.
   */
  private JTextField crearCampo() {
    JTextField tf = new JTextField();
    tf.setFont(Fuentes.r(13));
    tf.setBorder(BorderFactory.createCompoundBorder(
      new Bordes(Colores.BORDE_GRIS, 1, 8),
      new EmptyBorder(8, 12, 8, 12)));
    tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    tf.setAlignmentX(LEFT_ALIGNMENT);
    return tf;
  }

  /**
   * Crea un botón estilizado con efectos hover.
   * * @param texto Texto del botón.
   * @return JButton configurado.
   */
  private JButton crearBoton(String texto) {
    JButton btn = new JButton(texto) {
      boolean ov = false;

      {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            ov = true;
            repaint();
          }

          @Override
          public void mouseExited(MouseEvent e) {
            ov = false;
            repaint();
          }
        });
      }

      @Override
      protected void paintComponent(Graphics g2d) {
        Graphics2D g = (Graphics2D) g2d;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(ov ? Colores.AZUL_HOVER : Colores.AZUL);
        g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
        super.paintComponent(g2d);
      }

    };
    btn.setForeground(Colores.BLANCO);
    btn.setFont(Fuentes.b(14));
    return btn;
  }
}