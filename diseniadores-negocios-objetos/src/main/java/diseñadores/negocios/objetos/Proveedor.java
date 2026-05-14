package diseñadores.negocios.objetos;

import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.persistencia.dao.IProveedorDAO;
import diseñadores.persistencia.dao.impl.ProveedorDAOImpl;

import java.util.List;

public class Proveedor {

    private static final IProveedorDAO PROVEEDOR_DAO = new ProveedorDAOImpl();

    public static List<ProveedorDTO> obtenerTodos() {
        return PROVEEDOR_DAO.obtenerTodos();
    }

    public static ProveedorDTO obtenerPorCodigo(String codigo) {
        return PROVEEDOR_DAO.obtenerPorCodigo(codigo);
    }

    public static void guardar(ProveedorDTO proveedor) {
        PROVEEDOR_DAO.guardar(proveedor);
    }

    public static void actualizar(ProveedorDTO proveedor) {
        PROVEEDOR_DAO.actualizar(proveedor);
    }

    public static void eliminar(String codigo) {
        PROVEEDOR_DAO.eliminar(codigo);
    }
}