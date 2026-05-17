package diseñadores.persistencia.dao.impl;

import adaptadores.VentaPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IVentaDAO;
import entidades.Venta;
import entidadesmongo.VentaMongoEntidad;
import excepciones.PersistenciaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar la persistencia de las Notas de Venta en MongoDB.
 * Utiliza el patrón Singleton para la comunicación con la base de datos y
 * aísla el acoplamiento estructural por medio de un adaptador perimetral de dominio.
 * 
 * @author ERICK
 */
public class VentaDAOImpl implements IVentaDAO {

    private final MongoCollection<VentaMongoEntidad> coleccion;
    private final VentaPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la colección tipada de MongoDB para Ventas
     * y asocia el adaptador de persistencia correspondiente.
     */
    public VentaDAOImpl() {
        this.coleccion = Conexion.getInstancia().obtenerColeccionVentas();
        this.adaptador = new VentaPersistenciaAdapter();
    }

    @Override
    public List<Venta> obtenerTodas() throws PersistenciaException {
        try {
            List<VentaMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible recuperar el historial de ventas desde MongoDB.", ex);
        }
    }

    @Override
    public Venta obtenerPorCodigoVenta(String codigoVenta) throws PersistenciaException {
        validarCodigoVentaRequerido(codigoVenta);
        try {
            VentaMongoEntidad entidad = buscarDocumentoPorCodigo(codigoVenta);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible buscar la venta solicitada por su código único.", ex);
        }
    }

    @Override
    public void guardar(Venta venta) throws PersistenciaException {
        validarDatosVenta(venta);
        validarCodigoVentaDisponible(venta.getCodigoVenta());
        try {
            ejecutarInsercion(venta);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible registrar la transacción de venta en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(Venta venta) throws PersistenciaException {
        validarDatosVenta(venta);
        validarVentaExiste(venta.getCodigoVenta());
        try {
            ejecutarReemplazo(venta);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar los datos de la venta especificada.", ex);
        }
    }

    @Override
    public void eliminar(String codigoVenta) throws PersistenciaException {
        validarCodigoVentaRequerido(codigoVenta);
        validarVentaExiste(codigoVenta);
        try {
            ejecutarEliminacion(codigoVenta);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar la nota de venta de MongoDB.", ex);
        }
    }

    /**
     * Valida de manera defensiva que el código único comercial no sea nulo o inválido.
     */
    private void validarCodigoVentaRequerido(String codigoVenta) throws PersistenciaException {
        if (codigoVenta == null || codigoVenta.isBlank()) {
            throw new PersistenciaException("El código de la venta es un dato obligatorio.");
        }
    }

    /**
     * Analiza la consistencia básica de la venta de dominio antes de enviarla a la base de datos.
     */
    private void validarDatosVenta(Venta venta) throws PersistenciaException {
        if (venta == null) {
            throw new PersistenciaException("La entidad de venta a procesar no puede ser nula.");
        }
        validarCodigoVentaRequerido(venta.getCodigoVenta());
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new PersistenciaException("No se puede procesar una venta sin artículos en su desglose de detalles.");
        }
        if (venta.getTotal() < 0) {
            throw new PersistenciaException("El valor total de la venta debe ser una cantidad positiva.");
        }
    }

    /**
     * Asegura la inexistencia del código de venta para prevenir sobreescrituras en inserciones.
     */
    private void validarCodigoVentaDisponible(String codigoVenta) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigoVenta) != null) {
            throw new PersistenciaException("El código de venta '" + codigoVenta + "' ya se encuentra registrado.");
        }
    }

    /**
     * Comprueba la existencia física de la venta para poder dar paso a modificaciones o bajas.
     */
    private void validarVentaExiste(String codigoVenta) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigoVenta) == null) {
            throw new PersistenciaException("La nota de venta con código '" + codigoVenta + "' no existe en el sistema.");
        }
    }

    /**
     * Consulta el almacén de datos buscando coincidencia exacta por el atributo de negocio "codigoVenta".
     */
    private VentaMongoEntidad buscarDocumentoPorCodigo(String codigoVenta) {
        return coleccion.find(Filters.eq("codigoVenta", codigoVenta.trim())).first();
    }

    /**
     * Transforma el objeto de dominio e inyecta el nuevo documento de venta de forma atómica.
     */
    private void ejecutarInsercion(Venta venta) throws PersistenciaException {
        VentaMongoEntidad entidadMongo = adaptador.convertirAMongo(venta);
        coleccion.insertOne(entidadMongo);
    }

    /**
     * Reemplaza el documento en cascada conservando el ciclo de vida del ObjectId técnico.
     */
    private void ejecutarReemplazo(Venta venta) throws PersistenciaException {
        VentaMongoEntidad documentoActual = buscarDocumentoPorCodigo(venta.getCodigoVenta());
        VentaMongoEntidad entidadMongo = adaptador.convertirAMongo(venta);

        // Clave: Conservamos el _id hexadecimal original para no violar restricciones de inmutabilidad
        if (documentoActual != null) {
            entidadMongo.setId(documentoActual.getId());
        }

        coleccion.replaceOne(Filters.eq("codigoVenta", venta.getCodigoVenta()), entidadMongo);
    }

    /**
     * Remueve físicamente el documento mapeado de la colección por medio de filtros BSON.
     */
    private void ejecutarEliminacion(String codigoVenta) {
        coleccion.deleteOne(Filters.eq("codigoVenta", codigoVenta));
    }
}