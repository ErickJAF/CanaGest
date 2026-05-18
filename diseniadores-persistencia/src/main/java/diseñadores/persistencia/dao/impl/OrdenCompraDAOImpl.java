package diseñadores.persistencia.dao.impl;

import adaptadores.OrdenCompraPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import java.util.Arrays;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IOrdenCompraDAO;
import entidades.OrdenCompra;
import entidadesmongo.OrdenCompraMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar la colección de órdenes de compra en MongoDB.
 * Emplea el patrón Singleton para la comunicación con la base de datos y un adaptador 
 * perimetral para transformar las entidades POJO autogestionadas a objetos de dominio limpios.
 * 
 * @author icoro
 */
public class OrdenCompraDAOImpl implements IOrdenCompraDAO {

    private final MongoCollection<OrdenCompraMongoEntidad> coleccion;
    private final OrdenCompraPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la colección tipada de MongoDB utilizando
     * el Singleton de Conexión y levanta el adaptador de persistencia.
     */
    public OrdenCompraDAOImpl() {
        this.coleccion = Conexion.getInstancia().obtenerColeccionOrdenes();
        this.adaptador = new OrdenCompraPersistenciaAdapter();
    }

    @Override
    public List<OrdenCompra> obtenerTodas() throws PersistenciaException {
        try {
            List<OrdenCompraMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible obtener la lista de órdenes de compra desde MongoDB.", ex);
        }
    }

    @Override
    public OrdenCompra obtenerPorNumero(String numero) throws PersistenciaException {
        validarNumeroRequerido(numero);
        try {
            OrdenCompraMongoEntidad entidad = buscarDocumentoPorNumero(numero);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible buscar la orden de compra por su número.", ex);
        }
    }

    @Override
    public void guardar(OrdenCompra orden) throws PersistenciaException {
        validarDatosOrden(orden);
        validarNumeroDisponible(orden.getCodigoOrden());
        try {
            ejecutarInsercion(orden);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible guardar la orden de compra en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(OrdenCompra orden) throws PersistenciaException {
        validarDatosOrden(orden);
        validarOrdenExiste(orden.getCodigoOrden());
        try {
            ejecutarReemplazo(orden);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar la orden de compra seleccionada.", ex);
        }
    }

    @Override
    public void eliminar(String numero) throws PersistenciaException {
        validarNumeroRequerido(numero);
        validarOrdenExiste(numero);
        try {
            ejecutarEliminacion(numero);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar la orden de compra de MongoDB.", ex);
        }
    }

    /**
     * Valida de forma defensiva que el parámetro del número de orden no sea nulo ni esté vacío.
     */
    private void validarNumeroRequerido(String numero) throws PersistenciaException {
        if (numero == null || numero.isBlank()) {
            throw new PersistenciaException("El número de orden es obligatorio y no puede estar vacío.");
        }
    }

    /**
     * Verifica la integridad estructural de la orden de compra de dominio antes de persistirla.
     */
    private void validarDatosOrden(OrdenCompra orden) throws PersistenciaException {
        if (orden == null) {
            throw new PersistenciaException("La orden de compra no puede ser nula.");
        }
        validarNumeroRequerido(orden.getCodigoOrden());
        if (orden.getProveedor() == null) {
            throw new PersistenciaException("La orden debe tener un proveedor asignado.");
        }
        if (orden.getUsuario() == null) {
            throw new PersistenciaException("La orden debe tener un usuario comprador asociado.");
        }
    }

    /**
     * Garantiza que el número de orden asignado no esté duplicado en el sistema.
     */
    private void validarNumeroDisponible(String numero) throws PersistenciaException {
        if (buscarDocumentoPorNumero(numero) != null) {
            throw new PersistenciaException("El número de orden '" + numero + "' ya se encuentra registrado.");
        }
    }

    /**
     * Comprueba si la orden buscada existe físicamente en MongoDB previo a ediciones o bajas.
     */
    private void validarOrdenExiste(String numero) throws PersistenciaException {
        if (buscarDocumentoPorNumero(numero) == null) {
            throw new PersistenciaException("La orden de compra número '" + numero + "' no existe en el sistema.");
        }
    }

    /**
     * Realiza la consulta directa en la colección tipada mediante filtros BSON.
     */
    private OrdenCompraMongoEntidad buscarDocumentoPorNumero(String numero) {
        return coleccion.find(Filters.eq("codigoOrden", numero)).first();
    }

    /**
     * Invoca la operación atómica de inserción delegando la transformación al adapter.
     */
    private void ejecutarInsercion(OrdenCompra orden) throws PersistenciaException {
        OrdenCompraMongoEntidad entidadMongo = adaptador.convertirAMongo(orden);
        coleccion.insertOne(entidadMongo);
        System.out.println("LLEGÓ AL DAO DE MONGO");
    }

    /**
     * Invoca la sustitución completa de un documento usando los filtros de negocio de Mongo.
     */
    private void ejecutarReemplazo(OrdenCompra orden) throws PersistenciaException {
        OrdenCompraMongoEntidad documentoActual = buscarDocumentoPorNumero(orden.getCodigoOrden());
        OrdenCompraMongoEntidad entidadMongo = adaptador.convertirAMongo(orden);

        if (documentoActual != null) {
            entidadMongo.setId(documentoActual.getId());
        }
        coleccion.replaceOne(Filters.eq("codigoOrden", orden.getCodigoOrden()), entidadMongo);
    }

    /**
     * Ejecuta la eliminación física del documento que coincida con el criterio.
     */
    private void ejecutarEliminacion(String numero) {
        coleccion.deleteOne(Filters.eq("codigoOrden", numero));
    }
    @Override
    public List<Document> obtenerConteoOrdenesPorProveedor() throws PersistenciaException {
        try {
            // 1. Definir la etapa de agrupación ($group)
            // Asumiendo que tu proveedor está incrustado o tiene un campo para agrupar.
            // Si proveedor es un objeto anidado, puedes usar "$proveedor.nombre". 
            // Si solo guardas un ID o String, usa "$proveedor".
            Bson etapaAgrupacion = Aggregates.group(
                "$proveedor", // Campo por el cual vamos a agrupar
                Accumulators.sum("totalOrdenes", 1) // Acumulador: suma 1 por cada documento encontrado
            );

            // 2. Crear el pipeline (lista de etapas)
            List<Bson> pipeline = Arrays.asList(etapaAgrupacion);

            // 3. Ejecutar la agregación
            List<Document> resultados = new ArrayList<>();
            
            /* * NOTA IMPORTANTE: Tu colección es de tipo <OrdenCompraMongoEntidad>.
             * Al hacer una agregación, la forma de los datos cambia (ya no es una Orden de Compra completa, 
             * ahora es solo un _id de proveedor y un total). 
             * Por eso debemos pasar 'Document.class' como segundo parámetro a aggregate() 
             * para evitar un error de mapeo.
             */
            coleccion.aggregate(pipeline, Document.class).into(resultados);

            return resultados;

        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible generar el reporte de órdenes por proveedor mediante aggregate.", ex);
        }
    }
}