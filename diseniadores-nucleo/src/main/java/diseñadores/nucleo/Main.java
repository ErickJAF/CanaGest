package diseñadores.nucleo;

import diseñadores.negocios.dto.*;
import diseñadores.negocios.productos.IProductos;
import diseñadores.negocios.productos.ProductosFacade;
import diseñadores.negocios.proveedores.IProveedores;
import diseñadores.negocios.proveedores.ProveedoresFacade;
import diseñadores.negocios.usuarios.IUsuarios;
import diseñadores.negocios.usuarios.UsuariosFacade;
import diseñadores.negocios.ventas.IVentas;
import diseñadores.negocios.ventas.VentasFacade;
import diseñadores.persistencia.PersistenciaFacade;
import excepciones.NegocioException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de ejecución encargada de inicializar el ecosistema CanaGest.
 * Actúa como un módulo de carga inicial (seeding) que puebla de forma controlada
 * colecciones lógicas en MongoDB a través de Fachadas de negocio.
 * * @author icoro
 * @version 1.0
 */
public class Main {

  private static final IProveedores fachadaProveedores = new ProveedoresFacade();
  private static final IProductos fachadaProductos = new ProductosFacade();
  private static final IUsuarios fachadaUsuarios = new UsuariosFacade();
  private static final IVentas fachadaVentas = new VentasFacade();

  /**
   * Punto de entrada de la aplicación de carga y verificación del sistema.
   * * @param args Argumentos de la línea de comandos de la JVM (no utilizados).
   */
  public static void main(String[] args) {
    System.out.println("═══════════════════════════════════════════════");
    System.out.println("        SISTEMA CANAGEST - CARGA INICIAL         ");
    System.out.println("═══════════════════════════════════════════════\n");

    try {
      // 1. Insertar productos y proveedores
      insertarProductos();
      
      // 2. Insertar usuarios (los requerimos para las órdenes de compra)
      insertarUsuarios();
      
      // 3. Insertar órdenes de compra asociadas a un usuario legal
      insertarOrdenesCompra();
      
      // 4. Registrar historial de ventas
      insertarVentas();

      System.out.println("\n═══════════════════════════════════════════════");
      System.out.println("        VERIFICACIÓN DE DATOS                    ");
      System.out.println("═══════════════════════════════════════════════\n");

      verificarProveedores();
      verificarProductos();
      verificarUsuarios();
      verificarOrdenesCompra();
      verificarVentas();

      PersistenciaFacade.getInstancia();
      System.out.println("\n✔ Carga inicial completada con éxito.");

    } catch (NegocioException e) {
      System.err.println("\n❌ ERROR CRÍTICO EN LA CARGA DE NEGOCIO: " + e.getMessage());
      if (e.getCause() != null) {
        System.err.println("Causa subyacente: " + e.getCause().getMessage());
      }
    }
  }

  // ─────────────────────── INSERCIÓN VIA FACADES ───────────────────────────────

  private static void insertarProductos() throws NegocioException {
    System.out.println("── Insertando productos (y registrando proveedores de forma implícita)...");

    ProveedorDTO provGranos = new ProveedorDTO(
      "Abarrotes del Mayo", "PROV-001", "Juan Pérez",
      "555-0101", "ventas@mayo.com",
      "Av. Mayo 123", "30 días", true
    );
    ProveedorDTO provAceites = new ProveedorDTO(
      "Distribuidora Sonora", "PROV-002", "María López",
      "555-0202", "contacto@distsonora.com",
      "Calle Sonora 456", "15 días", true
    );
    ProveedorDTO provLacteos = new ProveedorDTO(
      "Lácteos Premium", "PROV-003", "Carlos Ramírez",
      "555-0303", "info@lacteospremium.com",
      "Blvd. Lácteo 789", "45 días", true
    );

    List<ProductoDTO> productos = List.of(
      new ProductoDTO("7501001000011", "Arroz", BigDecimal.valueOf(28.00), 50, 10, 100, provGranos),
      new ProductoDTO("7501001000028", "Frijol", BigDecimal.valueOf(32.00), 30, 5, 80, provGranos),
      new ProductoDTO("7501001000035", "Azúcar", BigDecimal.valueOf(26.00), 20, 5, 60, provGranos),
      new ProductoDTO("7501002000010", "Aceite", BigDecimal.valueOf(48.00), 15, 3, 50, provAceites),
      new ProductoDTO("7501002000027", "Atún", BigDecimal.valueOf(18.00), 40, 10, 100, provAceites),
      new ProductoDTO("7501003000019", "Leche", BigDecimal.valueOf(30.00), 35, 8, 90, provLacteos),
      new ProductoDTO("7501001000042", "Sal", BigDecimal.valueOf(8.00), 60, 15, 150, provGranos),
      new ProductoDTO("7501004000018", "Café", BigDecimal.valueOf(55.00), 25, 5, 70, provGranos),
      new ProductoDTO("7501005000017", "Jabón", BigDecimal.valueOf(22.00), 45, 10, 120, provAceites),
      new ProductoDTO("7501003000026", "Mantequilla", BigDecimal.valueOf(42.00), 20, 5, 60, provLacteos),
      new ProductoDTO("7501003000033", "Crema", BigDecimal.valueOf(24.00), 18, 5, 50, provLacteos),
      new ProductoDTO("7501001000059", "Maíz", BigDecimal.valueOf(20.00), 40, 10, 90, provGranos)
    );

    for (ProductoDTO p : productos) {
      fachadaProductos.guardarProducto(p);
    }

    System.out.println("   ✔ " + productos.size() + " productos insertados junto a sus proveedores.\n");
  }

  private static void insertarUsuarios() throws NegocioException {
    System.out.println("── Insertando usuarios...");

    List<UsuarioDTO> usuarios = List.of(
      new UsuarioDTO("erick armenta", "1234", UsuarioRol.ADMINISTRADOR),
      new UsuarioDTO("isaias coronado", "1234", UsuarioRol.CAJERO),
      new UsuarioDTO("miguel angel", "1234", UsuarioRol.ENCARGADO_ALMACEN)
    );

    for (UsuarioDTO u : usuarios) {
      fachadaUsuarios.guardarUsuario(u);
    }

    System.out.println("   ✔ " + usuarios.size() + " usuarios insertados.\n");
  }

  private static void insertarOrdenesCompra() throws NegocioException {
    System.out.println("── Insertando órdenes de compra...");

    ProveedorDTO prov1 = fachadaProveedores.obtenerProveedorPorCodigo("PROV-001");
    ProveedorDTO prov2 = fachadaProveedores.obtenerProveedorPorCodigo("PROV-002");
    ProveedorDTO prov3 = fachadaProveedores.obtenerProveedorPorCodigo("PROV-003");

    List<UsuarioDTO> todosLosUsuarios = fachadaUsuarios.obtenerTodos();
    UsuarioDTO comprador = todosLosUsuarios.stream()
      .filter(u -> u.getRol() == UsuarioRol.ENCARGADO_ALMACEN || u.getRol() == UsuarioRol.ADMINISTRADOR)
      .findFirst()
      .orElse(new UsuarioDTO("sistema", "1234", UsuarioRol.ADMINISTRADOR));

    // CORRECCIÓN: Instanciación parametrizada de productos reales para evitar ítems vacíos (null)
    DetalleOrdenCompraDTO dArroz = new DetalleOrdenCompraDTO("7501001000011", "Arroz", 100, BigDecimal.valueOf(28.00));
    DetalleOrdenCompraDTO dAceite = new DetalleOrdenCompraDTO("7501002000010", "Aceite", 50, BigDecimal.valueOf(48.00));
    DetalleOrdenCompraDTO dLeche = new DetalleOrdenCompraDTO("7501003000019", "Leche", 80, BigDecimal.valueOf(30.00));

    List<DetalleOrdenCompraDTO> lista1 = new ArrayList<>(List.of(dArroz));
    List<DetalleOrdenCompraDTO> lista2 = new ArrayList<>(List.of(dAceite));
    List<DetalleOrdenCompraDTO> lista3 = new ArrayList<>(List.of(dLeche));

    // ---- Orden de Compra 1 ----
    OrdenCompraDTO oc1 = new OrdenCompraDTO(
      "OC-2026-001",
      LocalDate.now().toString(),
      "Pendiente",
      BigDecimal.valueOf(2800.00),
      prov1,
      comprador,
      lista1
    );

    // ---- Orden de Compra 2 ----
    OrdenCompraDTO oc2 = new OrdenCompraDTO(
      "OC-2026-002",
      LocalDate.now().minusDays(2).toString(),
      "Aprobada",
      BigDecimal.valueOf(2400.00),
      prov2,
      comprador,
      lista2
    );

    // ---- Orden de Compra 3 ----
    OrdenCompraDTO oc3 = new OrdenCompraDTO(
      "OC-2026-003",
      LocalDate.now().minusDays(5).toString(),
      "Recibida",
      BigDecimal.valueOf(2400.00),
      prov3,
      comprador,
      lista3
    );

    fachadaProveedores.guardarOrdenCompra(oc1);
    fachadaProveedores.guardarOrdenCompra(oc2);
    fachadaProveedores.guardarOrdenCompra(oc3);

    System.out.println("   ✔ 3 órdenes de compra (con productos) insertadas con éxito.\n");
  }

  private static void insertarVentas() throws NegocioException {
    System.out.println("── Insertando ventas...");

    ProductoDTO arroz = fachadaProductos.buscarProductoPorCodigo(new EscanearProductoDTO("7501001000011"));
    ProductoDTO frijol = fachadaProductos.buscarProductoPorCodigo(new EscanearProductoDTO("7501001000028"));
    ProductoDTO leche = fachadaProductos.buscarProductoPorCodigo(new EscanearProductoDTO("7501003000019"));
    ProductoDTO aceite = fachadaProductos.buscarProductoPorCodigo(new EscanearProductoDTO("7501002000010"));
    ProductoDTO cafe = fachadaProductos.buscarProductoPorCodigo(new EscanearProductoDTO("7501004000018"));

    // Venta 1
    VentaDTO venta1 = new VentaDTO();
    venta1.agregarProducto(arroz);
    venta1.agregarProducto(arroz);
    venta1.agregarProducto(frijol);
    venta1.setCajero("erick armenta");
    venta1.setFolio("TK-" + System.currentTimeMillis());

    // Venta 2
    VentaDTO venta2 = new VentaDTO();
    venta2.agregarProducto(leche);
    venta2.agregarProducto(aceite);
    venta2.agregarProducto(cafe);
    venta2.setCajero("isaias coronado");
    venta2.setFolio("TK-" + (System.currentTimeMillis() + 1));

    // Venta 3
    VentaDTO venta3 = new VentaDTO();
    venta3.agregarProducto(arroz);
    venta3.agregarProducto(leche);
    venta3.agregarProducto(leche);
    venta3.agregarProducto(frijol);
    venta3.setCajero("erick armenta");
    venta3.setFolio("TK-" + (System.currentTimeMillis() + 2));

    fachadaVentas.procesarFinalizarVenta(venta1);
    fachadaVentas.procesarFinalizarVenta(venta2);
    fachadaVentas.procesarFinalizarVenta(venta3);

    System.out.println("   ✔ 3 ventas insertadas y procesadas con inventario.\n");
  }

  // ─────────────────────── VERIFICACIÓN VIA FACADES ────────────────────────────
  
  private static void verificarProveedores() throws NegocioException {
    System.out.println("── Proveedores en base de datos:");
    List<ProveedorDTO> proveedores = fachadaProveedores.obtenerProveedores();
    for (ProveedorDTO p : proveedores) {
      System.out.printf("   [%s] %-25s | Contacto: %-18s | Activo: %s%n",
        p.getCodigo(), p.getNombre(), p.getContacto(), p.isActivo() ? "Sí" : "No");
    }
    System.out.println("   Total: " + proveedores.size() + "\n");
  }

  private static void verificarProductos() throws NegocioException {
    System.out.println("── Productos en base de datos:");
    List<ProductoDTO> productos = fachadaProductos.obtenerCatalogo();
    for (ProductoDTO p : productos) {
      System.out.printf("   [%s] %-15s | Precio: $%6.2f | Stock: %3d | Proveedor: %s%n",
        p.getCodigo(), p.getNombre(), p.getPrecio(),
        p.getStock(), p.getProveedor() != null ? p.getProveedor().getNombre() : "N/A");
    }
    System.out.println("   Total: " + productos.size() + "\n");
  }

  private static void verificarUsuarios() throws NegocioException {
    System.out.println("── Usuarios en base de datos:");
    List<UsuarioDTO> usuarios = fachadaUsuarios.obtenerTodos();
    for (UsuarioDTO u : usuarios) {
      System.out.printf("   %-10s | Rol: %s%n", u.getNombre(), u.getRol());
    }
    System.out.println("   Total: " + usuarios.size() + "\n");
  }

  private static void verificarOrdenesCompra() throws NegocioException {
    System.out.println("── Órdenes de compra en base de datos:");
    List<OrdenCompraDTO> ordenes = fachadaProveedores.obtenerOrdenesCompra();
    for (OrdenCompraDTO o : ordenes) {
      System.out.printf("   [%s] Fecha: %s | Estado: %-10s | Total: $%8.2f | Proveedor: %s | Solicitó: %s%n",
        o.getNumero(), o.getFecha(), o.getEstado(), o.getTotal(),
        o.getProveedor() != null ? o.getProveedor().getNombre() : "N/A",
        o.getUsuario() != null ? o.getUsuario().getNombre() : "N/A");
    }
    System.out.println("   Total: " + ordenes.size() + "\n");
  }

  private static void verificarVentas() throws NegocioException {
    System.out.println("── Ventas en base de datos:");
    List<VentaDTO> ventas = fachadaVentas.obtenerHistorialVentas();
    for (VentaDTO v : ventas) {
      System.out.printf("   [%s] Unidades: %2d | Subtotal: $%7.2f | IVA: $%6.2f | Total: $%7.2f | Pagada: %s%n",
        v.getFolio(), v.getTotalUnidades(),
        v.getSubtotal(), v.getIva(), v.getTotal(),
        v.isPagada() ? "Sí" : "No");
      for (ItemVentaDTO item : v.getItems()) {
        System.out.printf("        · %-15s x%d  $%.2f%n",
          item.getNombre(), item.getCantidad(), item.getSubtotal());
      }
    }
    System.out.println("   Total: " + ventas.size() + "\n");
  }
}