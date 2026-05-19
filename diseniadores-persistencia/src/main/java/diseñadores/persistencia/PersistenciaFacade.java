package diseñadores.persistencia;

import diseñadores.persistencia.dao.IConteoInventarioGeneralDAO; // NUEVO: Interfaz general
import diseñadores.persistencia.dao.IOrdenCompraDAO;
import diseñadores.persistencia.dao.IProductoDAO;
import diseñadores.persistencia.dao.IProveedorDAO;
import diseñadores.persistencia.dao.IUsuarioDAO;
import diseñadores.persistencia.dao.IVentaDAO;
import diseñadores.persistencia.dao.impl.ConteoInventarioGeneralDAOImpl; // NUEVO: Implementación general
import diseñadores.persistencia.dao.impl.OrdenCompraDAOImpl;
import diseñadores.persistencia.dao.impl.ProductoDAOImpl;
import diseñadores.persistencia.dao.impl.ProveedorDAOImpl;
import diseñadores.persistencia.dao.impl.UsuarioDAOImpl;
import diseñadores.persistencia.dao.impl.VentaDAOImpl;
import entidades.ConteoInventarioGeneral; // NUEVO: Entidad de dominio raíz correcta
import entidades.OrdenCompra;
import entidades.Producto;
import entidades.Proveedor;
import entidades.Usuario;
import entidades.Venta;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.Optional;

/**
 * Fachada única de Persistencia que centraliza y delega el acceso a los subsistemas DAO.
 * Trabaja enteramente con entidades de dominio limpio, aislando la lógica de datos.
 * 
 * @author ERICK
 */
public class PersistenciaFacade implements IPersistencia {

    private static PersistenciaFacade instancia;

    private final IProductoDAO productoDAO;
    private final IVentaDAO ventaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IProveedorDAO proveedorDAO;
    private final IOrdenCompraDAO ordenCompraDAO;
    private final IConteoInventarioGeneralDAO conteoInventarioGeneralDAO; // MODIFICADO

    /**
     * Constructor privado para aplicar el patrón de diseño Singleton.
     */
    private PersistenciaFacade() {
        this.productoDAO = new ProductoDAOImpl();
        this.ventaDAO = new VentaDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
        this.proveedorDAO = new ProveedorDAOImpl();
        this.ordenCompraDAO = new OrdenCompraDAOImpl();
        this.conteoInventarioGeneralDAO = new ConteoInventarioGeneralDAOImpl(); // MODIFICADO
    }

    /**
     * Obtiene de forma sincronizada la instancia única de la Fachada.
     * 
     * @return Instancia global de PersistenciaFacade.
     */
    public static synchronized PersistenciaFacade getInstancia() {
        if (instancia == null) {
            instancia = new PersistenciaFacade();
        }
        return instancia;
    }

    @Override
    public List<Producto> obtenerProductos() throws PersistenciaException {
        return productoDAO.obtenerTodos();
    }

    @Override
    public Producto obtenerProductoPorCodigo(String codigo) throws PersistenciaException {
        return productoDAO.obtenerPorCodigo(codigo);
    }

    @Override
    public void guardarProducto(Producto producto) throws PersistenciaException {
        productoDAO.guardar(producto);
    }

    @Override
    public void actualizarProducto(Producto producto) throws PersistenciaException {
        productoDAO.actualizar(producto);
    }

    @Override
    public void eliminarProducto(String codigo) throws PersistenciaException {
        productoDAO.eliminar(codigo);
    }

    @Override
    public List<Venta> obtenerVentas() throws PersistenciaException {
        return ventaDAO.obtenerTodas();
    }

    @Override
    public Venta obtenerVentaPorCodigoVenta(String codigoVenta) throws PersistenciaException {
        return ventaDAO.obtenerPorCodigoVenta(codigoVenta);
    }

    @Override
    public void guardarVenta(Venta venta) throws PersistenciaException {
        ventaDAO.guardar(venta);
    }

    @Override
    public void actualizarVenta(Venta venta) throws PersistenciaException {
        ventaDAO.actualizar(venta);
    }

    @Override
    public void eliminarVenta(String codigoVenta) throws PersistenciaException {
        ventaDAO.eliminar(codigoVenta);
    }

    @Override
    public List<Usuario> obtenerUsuarios() throws PersistenciaException {
        return usuarioDAO.obtenerTodos();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorIdUsuario(String idUsuario) throws PersistenciaException {
        Usuario usuario = usuarioDAO.obtenerPorIdUsuario(idUsuario);
        return Optional.ofNullable(usuario);
    }

    @Override
    public void guardarUsuario(Usuario usuario) throws PersistenciaException {
        usuarioDAO.guardar(usuario);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws PersistenciaException {
        usuarioDAO.actualizar(usuario);
    }

    @Override
    public void eliminarUsuario(String idUsuario) throws PersistenciaException {
        usuarioDAO.eliminar(idUsuario);
    }

    @Override
    public List<Proveedor> obtenerProveedores() throws PersistenciaException {
        return proveedorDAO.obtenerTodos();
    }

    @Override
    public Proveedor obtenerProveedorPorCodigo(String codigo) throws PersistenciaException {
        return proveedorDAO.obtenerPorCodigo(codigo);
    }

    @Override
    public void guardarProveedor(Proveedor proveedor) throws PersistenciaException {
        proveedorDAO.guardar(proveedor);
    }

    @Override
    public void actualizarProveedor(Proveedor proveedor) throws PersistenciaException {
        proveedorDAO.actualizar(proveedor);
    }

    @Override
    public void eliminarProveedor(String codigo) throws PersistenciaException {
        proveedorDAO.eliminar(codigo);
    }

    @Override
    public List<OrdenCompra> obtenerOrdenesCompra() throws PersistenciaException {
        return ordenCompraDAO.obtenerTodas();
    }

    @Override
    public OrdenCompra obtenerOrdenCompraPorNumero(String numero) throws PersistenciaException {
        return ordenCompraDAO.obtenerPorNumero(numero);
    }

    @Override
    public void guardarOrdenCompra(OrdenCompra orden) throws PersistenciaException {
        ordenCompraDAO.guardar(orden);
    }

    @Override
    public void actualizarOrdenCompra(OrdenCompra orden) throws PersistenciaException {
        ordenCompraDAO.actualizar(orden);
    }

    @Override
    public void eliminarOrdenCompra(String numero) throws PersistenciaException {
        ordenCompraDAO.eliminar(numero);
    }

    @Override
    public List<ConteoInventarioGeneral> obtenerConteosInventarioGenerales() throws PersistenciaException {
        return conteoInventarioGeneralDAO.obtenerTodos();
    }

    @Override
    public ConteoInventarioGeneral obtenerConteoInventarioGeneralPorCodigo(String codigoGeneral) throws PersistenciaException {
        return conteoInventarioGeneralDAO.obtenerPorCodigoGeneral(codigoGeneral);
    }

    @Override
    public void guardarConteoInventarioGeneral(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException {
        conteoInventarioGeneralDAO.guardar(conteoGeneral);
    }

    @Override
    public void actualizarConteoInventarioGeneral(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException {
        conteoInventarioGeneralDAO.actualizar(conteoGeneral);
    }

    @Override
    public void eliminarConteoInventarioGeneral(String codigoGeneral) throws PersistenciaException {
        conteoInventarioGeneralDAO.eliminar(codigoGeneral);
    }
}