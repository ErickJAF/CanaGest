package diseñadores.presentacion.frame;

import diseñadores.presentacion.control.VentasControl;
import diseñadores.presentacion.utilidad.Colores;
import diseñadores.presentacion.utilidad.Componentes;
import diseñadores.presentacion.utilidad.Fuentes;
import org.bson.Document;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de Dashboard rediseñada para coincidir con la estética de "La Canasta".
 * Utiliza tarjetas de estilo material y componentes unificados.
 */
public class PantallaReporteProveedores extends JFrame {
    private final VentasControl control;

    public PantallaReporteProveedores(VentasControl control) {
        super("Dashboard de Estadísticas");
        this.control = control;
        initUI();
    }

    private void initUI() {
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 1. Fondo Amarillo unificado
        JPanel root = Componentes.fondoAmarillo();
        root.setLayout(new BorderLayout());

        // 2. Contenedor principal con margen
        JPanel contenido = new JPanel(new BorderLayout(0, 20));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 3. Título estilizado
        JLabel lblTitulo = new JLabel("Estadísticas por Proveedor");
        lblTitulo.setFont(Fuentes.b(24));
        lblTitulo.setForeground(Colores.TEXTO_OSCURO);
        contenido.add(lblTitulo, BorderLayout.NORTH);

        // 4. Tarjeta Blanca para la tabla (Estilo Card)
        JPanel card = Componentes.tarjetaBlanca(20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Configuración de la Tabla
        String[] columnas = {"Proveedor", "Total de Órdenes"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setFont(Fuentes.r(14));
        tabla.getTableHeader().setFont(Fuentes.b(14));
        tabla.getTableHeader().setBackground(Colores.AZUL);
        tabla.getTableHeader().setForeground(Colores.BLANCO);
        tabla.setGridColor(Colores.BORDE_GRIS);
        tabla.setSelectionBackground(Colores.AZUL_CLARO);

        // Carga de datos
        List<Document> stats = control.consultarReporteOrdenes();
        for (Document doc : stats) {
            Object idField = doc.get("_id");
            String nombreProv = (idField instanceof Document) ? ((Document) idField).getString("nombre") : idField.toString();
            modelo.addRow(new Object[]{nombreProv, doc.getInteger("totalOrdenes") + " órdenes"});
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        contenido.add(card, BorderLayout.CENTER);

        // 5. Botón de cierre estilizado
        JButton btnCerrar = new JButton("Cerrar Dashboard");
        btnCerrar.setFont(Fuentes.b(14));
        btnCerrar.setBackground(Colores.GRIS_BTN);
        btnCerrar.setForeground(Colores.TEXTO_OSCURO);
        btnCerrar.setPreferredSize(new Dimension(0, 45));
        btnCerrar.addActionListener(e -> dispose());
        
        contenido.add(btnCerrar, BorderLayout.SOUTH);

        root.add(contenido, BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }
}