package diseñadores.persistencia;

import entidades.ConteoInventario;
import entidades.OrdenCompra;
import entidades.Producto;
import entidades.Proveedor;
import entidades.Usuario;
import entidades.Venta;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define el contrato único de acceso a la capa de persistencia (Fachada).
 * Despacha operaciones utilizando exclusivamente las entidades del dominio limpio.
 * 
 * @author ERICK
 */
public interface IPersistencia {

    List<Producto> obtenerProductos() throws PersistenciaException;
    Producto obtenerProductoPorCodigo(String codigo) throws PersistenciaException;
    void guardarProducto(Producto producto) throws PersistenciaException;
    void actualizarProducto(Producto producto) throws PersistenciaException;
    void eliminarProducto(String codigo) throws PersistenciaException;

    List<Venta> obtenerVentas() throws PersistenciaException;
    Venta obtenerVentaPorCodigoVenta(String codigoVenta) throws PersistenciaException;
    void guardarVenta(Venta venta) throws PersistenciaException;
    void actualizarVenta(Venta venta) throws PersistenciaException;
    void eliminarVenta(String codigoVenta) throws PersistenciaException;

    List<Usuario> obtenerUsuarios() throws PersistenciaException;
    Optional<Usuario> obtenerUsuarioPorIdUsuario(String idUsuario) throws PersistenciaException;
    void guardarUsuario(Usuario usuario) throws PersistenciaException;
    void actualizarUsuario(Usuario usuario) throws PersistenciaException;
    void eliminarUsuario(String idUsuario) throws PersistenciaException;

    List<Proveedor> obtenerProveedores() throws PersistenciaException;
    Proveedor obtenerProveedorPorCodigo(String codigo) throws PersistenciaException;
    void guardarProveedor(Proveedor proveedor) throws PersistenciaException;
    void actualizarProveedor(Proveedor proveedor) throws PersistenciaException;
    void eliminarProveedor(String codigo) throws PersistenciaException;

    List<OrdenCompra> obtenerOrdenesCompra() throws PersistenciaException;
    OrdenCompra obtenerOrdenCompraPorNumero(String numero) throws PersistenciaException;
    void guardarOrdenCompra(OrdenCompra orden) throws PersistenciaException;
    void actualizarOrdenCompra(OrdenCompra orden) throws PersistenciaException;
    void eliminarOrdenCompra(String numero) throws PersistenciaException;

    List<ConteoInventario> obtenerConteosInventario() throws PersistenciaException;
    ConteoInventario obtenerConteoInventarioPorCodigo(String codigo) throws PersistenciaException;
    void guardarConteoInventario(ConteoInventario conteo) throws PersistenciaException;
    void actualizarConteoInventario(ConteoInventario conteo) throws PersistenciaException;
    void eliminarConteoInventario(String codigo) throws PersistenciaException;
}