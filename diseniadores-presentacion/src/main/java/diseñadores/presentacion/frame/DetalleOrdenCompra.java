package diseñadores.presentacion.frame;

import diseñadores.negocios.dto.DetalleOrdenCompraDTO;
import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.presentacion.utilidad.Colores;
import diseñadores.presentacion.utilidad.Fuentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Cuadro de diálogo que muestra el detalle completo de una orden de compra.
 * Permite visualizar el estado, la fecha, el proveedor y los productos asociados.
 * * @author icoro
 */
public class DetalleOrdenCompra extends JDialog {

  private final OrdenCompraDTO orden;

  /**
   * Constructor de la vista de detalle de la orden.
   * * @param parent Ventana padre que invoca el diálogo.
   * @param orden  Objeto DTO con la información de la orden a mostrar.
   */
  public DetalleOrdenCompra(JFrame parent, OrdenCompraDTO orden) {
    super(parent, "Detalle de Orden de Compra", true);
    this.orden = orden;

    setSize(600, 500);
    setLocationRelativeTo(parent);
    setResizable(true);

    construirContenido();
  }

  /**
   * Construye y ensambla los componentes gráficos del panel principal.
   */
  private void construirContenido() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(28, 32, 28, 32));
    panel.setBackground(Colores.BLANCO);

    panel.add(crearEncabezado());
    panel.add(Box.createVerticalStrut(6));
    panel.add(crearLabelFecha());
    panel.add(Box.createVerticalStrut(24));

    panel.add(crearSeccion("PROVEEDOR"));
    panel.add(Box.createVerticalStrut(12));
    panel.add(crearFilaInfo("Nombre", orden.getProveedor() != null ? orden.getProveedor().getNombre() : "N/A"));
    panel.add(Box.createVerticalStrut(20));

    panel.add(crearSeccion("DETALLE DE LA ORDEN"));
    panel.add(Box.createVerticalStrut(12));

    // Extraer nombres de los productos y sumar cantidades
    StringBuilder nombresProductos = new StringBuilder();
    int cantidadTotal = 0;
    if (orden.getProductos() != null && !orden.getProductos().isEmpty()) {
      for (DetalleOrdenCompraDTO detalle : orden.getProductos()) {
        nombresProductos.append(detalle.getNombreProducto()).append(", ");
        cantidadTotal += detalle.getCantidad();
      }
      if (nombresProductos.length() > 0) {
        nombresProductos.setLength(nombresProductos.length() - 2); // Remover la última coma
      }
    } else {
      nombresProductos.append("Sin productos registrados");
    }

    panel.add(crearFilaInfo("Producto(s)", nombresProductos.toString()));
    panel.add(Box.createVerticalStrut(8));
    panel.add(crearFilaInfo("Cantidad total", cantidadTotal + " unidades"));
    panel.add(Box.createVerticalStrut(8));
    panel.add(crearFilaInfo("Total", String.format("$%,.2f", orden.getTotal().doubleValue())));
    panel.add(Box.createVerticalStrut(8));
    panel.add(crearFilaInfo("Estado", orden.getEstado()));

    setContentPane(panel);
  }

  /**
   * Genera el encabezado con el número de orden y el badge de estado.
   * * @return JPanel configurado como encabezado.
   */
  private JPanel crearEncabezado() {
    JPanel headerRow = new JPanel(new BorderLayout(10, 0));
    headerRow.setOpaque(false);

    JLabel lblNum = new JLabel(orden.getNumero() != null ? orden.getNumero() : "N/A");
    lblNum.setFont(Fuentes.b(22));
    lblNum.setForeground(Colores.TEXTO_OSCURO);

    JLabel badge = crearBadgeEstado();
    headerRow.add(lblNum, BorderLayout.WEST);
    headerRow.add(badge, BorderLayout.EAST);
    return headerRow;
  }

  /**
   * Crea la etiqueta para la fecha de la orden.
   * * @return JLabel con la fecha formateada.
   */
  private JLabel crearLabelFecha() {
    JLabel lblFecha = new JLabel("Fecha: " + (orden.getFecha() != null ? orden.getFecha() : "N/A"));
    lblFecha.setFont(Fuentes.r(13));
    lblFecha.setForeground(Colores.GRIS_TEXTO);
    return lblFecha;
  }

  /**
   * Crea un indicador visual (badge) según el estado de la orden de compra.
   * * @return JLabel estilizado que representa el estado.
   */
  private JLabel crearBadgeEstado() {
    Color badgeColor, badgeBg;
    String estado = orden.getEstado() != null ? orden.getEstado() : "Desconocido";
    
    switch (estado) {
      case "Pendiente" -> {
        badgeColor = new Color(161, 110, 0);
        badgeBg = new Color(254, 243, 199);
      }
      case "Aprobada" -> {
        badgeColor = new Color(30, 80, 180);
        badgeBg = new Color(219, 234, 254);
      }
      default -> {
        badgeColor = new Color(21, 128, 61);
        badgeBg = new Color(220, 252, 231);
      }
    }
    JLabel badge = new JLabel(estado, SwingConstants.CENTER);
    badge.setFont(Fuentes.b(11));
    badge.setForeground(badgeColor);
    badge.setOpaque(true);
    badge.setBackground(badgeBg);
    badge.setBorder(new EmptyBorder(4, 12, 4, 12));
    return badge;
  }

  /**
   * Genera un título de sección para agrupar la información.
   * * @param texto Título de la sección.
   * @return JLabel estilizado como título de sección.
   */
  private JLabel crearSeccion(String texto) {
    JLabel label = new JLabel(texto);
    label.setFont(Fuentes.b(12));
    label.setForeground(Colores.GRIS_TEXTO);
    label.setAlignmentX(LEFT_ALIGNMENT);
    return label;
  }

  /**
   * Crea un contenedor horizontal para mostrar un par clave-valor de información.
   * * @param label Etiqueta descriptiva (ej. "Nombre").
   * @param valor Valor asociado a la etiqueta.
   * @return JPanel con el diseño de la fila de información.
   */
  private JPanel crearFilaInfo(String label, String valor) {
    JPanel row = new JPanel(new BorderLayout(10, 0)) {
      @Override
      protected void paintComponent(Graphics g2d) {
        Graphics2D g = (Graphics2D) g2d;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Colores.FONDO_GRIS_CLARO);
        g.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
        super.paintComponent(g2d);
      }
    };
    row.setOpaque(false);
    row.setBorder(new EmptyBorder(12, 14, 12, 14));
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    row.setAlignmentX(LEFT_ALIGNMENT);

    JLabel l = new JLabel(label);
    l.setFont(Fuentes.r(12));
    l.setForeground(Colores.GRIS_TEXTO);

    JLabel v = new JLabel(valor != null ? valor : "-");
    v.setFont(Fuentes.r(14));
    v.setForeground(Colores.TEXTO_OSCURO);

    row.add(l, BorderLayout.WEST);
    row.add(v, BorderLayout.EAST);
    return row;
  }
}