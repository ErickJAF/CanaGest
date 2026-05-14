package diseñadores.negocios.objetos;

import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.persistencia.dao.IOrdenCompraDAO;
import diseñadores.persistencia.dao.impl.OrdenCompraDAOImpl;

import java.util.List;

public class OrdenCompra {

    private static final IOrdenCompraDAO ORDEN_COMPRA_DAO = new OrdenCompraDAOImpl();

    public static List<OrdenCompraDTO> obtenerTodas() {
        return ORDEN_COMPRA_DAO.obtenerTodas();
    }

    public static OrdenCompraDTO obtenerPorNumero(String numero) {
        return ORDEN_COMPRA_DAO.obtenerPorNumero(numero);
    }

    public static void guardar(OrdenCompraDTO orden) {
        ORDEN_COMPRA_DAO.guardar(orden);
    }

    public static void actualizar(OrdenCompraDTO orden) {
        ORDEN_COMPRA_DAO.actualizar(orden);
    }

    public static void eliminar(String numero) {
        ORDEN_COMPRA_DAO.eliminar(numero);
    }
}