package diseñadores.persistencia.dao.impl;

import adaptadores.ConteoInventarioGeneralPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IConteoInventarioGeneralDAO;
import entidades.ConteoInventarioGeneral;
import entidadesmongo.ConteoInventarioGeneralMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación orientada a objetos para la persistencia de Conteos Generales de Inventario.
 * Centraliza la administración y el resguardo analítico de las sesiones globales de auditoría.
 * 
 * Gstiona de forma directa la colección tipada de MongoDB aislando el dominio 
 * mediante su adaptador perimetral correspondiente.
 * 
 * @author ERICK
 */
public class ConteoInventarioGeneralDAOImpl implements IConteoInventarioGeneralDAO {

    private final MongoCollection<ConteoInventarioGeneralMongoEntidad> coleccion;
    private final ConteoInventarioGeneralPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la conexión con la colección 
     * unificada de conteos generales y el adaptador de persistencia.
     */
    public ConteoInventarioGeneralDAOImpl() {
        // NOTA: Asegúrate de que tu clase Conexion tenga el método 'obtenerColeccionConteosGenerales'
        // configurado para retornar la colección "conteosInventarioGeneral" mapeada con su PojoCodec.
        this.coleccion = Conexion.getInstancia().obtenerColeccionConteosGenerales();
        this.adaptador = new ConteoInventarioGeneralPersistenciaAdapter();
    }

    @Override
    public List<ConteoInventarioGeneral> obtenerTodos() throws PersistenciaException {
        try {
            List<ConteoInventarioGeneralMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible recuperar el historial de conteos generales de inventario.", ex);
        }
    }

    @Override
    public ConteoInventarioGeneral obtenerPorCodigoGeneral(String codigoGeneral) throws PersistenciaException {
        validarCodigoRequerido(codigoGeneral);
        try {
            ConteoInventarioGeneralMongoEntidad entidad = buscarDocumentoPorCodigoGeneral(codigoGeneral);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al buscar el conteo general de inventario con código: " + codigoGeneral, ex);
        }
    }

    @Override
    public void guardar(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException {
        validarDatosConteoGeneral(conteoGeneral);
        validarCodigoDisponible(conteoGeneral.getCodigoGeneral());
        try {
            ConteoInventarioGeneralMongoEntidad entidadMongo = adaptador.convertirAMongo(conteoGeneral);
            coleccion.insertOne(entidadMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible registrar el conteo general de inventario en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException {
        validarDatosConteoGeneral(conteoGeneral);
        
        // Comprobamos existencia comercial previa antes de intentar mutar
        ConteoInventarioGeneralMongoEntidad documentoActual = buscarDocumentoPorCodigoGeneral(conteoGeneral.getCodigoGeneral());
        if (documentoActual == null) {
            throw new PersistenciaException("El registro de inventario general con código '" + conteoGeneral.getCodigoGeneral() + "' no existe.");
        }
        
        try {
            ConteoInventarioGeneralMongoEntidad entidadMongo = adaptador.convertirAMongo(conteoGeneral);

            // Resguardamos rigurosamente el ObjectId original del documento raíz para evitar duplicidad de llaves
            entidadMongo.setId(documentoActual.getId());

            // Reemplazo atómico del documento maestro indexado por el código comercial
            coleccion.replaceOne(Filters.eq("codigoGeneral", conteoGeneral.getCodigoGeneral()), entidadMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar el registro unificado del conteo general.", ex);
        }
    }

    @Override
    public void eliminar(String codigoGeneral) throws PersistenciaException {
        validarCodigoRequerido(codigoGeneral);
        validarConteoGeneralExiste(codigoGeneral);
        try {
            coleccion.deleteOne(Filters.eq("codigoGeneral", codigoGeneral));
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar el registro de inventario general de MongoDB.", ex);
        }
    }

    /**
     * Valida que el código comercial descriptor de la auditoría no sea nulo ni vacío.
     */
    private void validarCodigoRequerido(String codigoGeneral) throws PersistenciaException {
        if (codigoGeneral == null || codigoGeneral.isBlank()) {
            throw new PersistenciaException("El código del conteo general es un dato obligatorio requerido.");
        }
    }

    /**
     * Analiza exhaustivamente la estructura de dominio para asegurar la integridad de datos del negocio.
     */
    private void validarDatosConteoGeneral(ConteoInventarioGeneral conteoGeneral) throws PersistenciaException {
        if (conteoGeneral == null) {
            throw new PersistenciaException("El objeto de registro general no puede ser nulo.");
        }
        validarCodigoRequerido(conteoGeneral.getCodigoGeneral());
        if (conteoGeneral.getFechaRegistro() == null) {
            throw new PersistenciaException("La fecha de captura de la auditoría general es obligatoria.");
        }
        if (conteoGeneral.getTodosLosConteos() == null) {
            throw new PersistenciaException("La lista interna de sub-conteos individuales no puede estar vacía o nula.");
        }
    }

    /**
     * Previene la sobreescritura accidental asegurando que el código comercial esté disponible para inserción.
     */
    private void validarCodigoDisponible(String codigoGeneral) throws PersistenciaException {
        if (buscarDocumentoPorCodigoGeneral(codigoGeneral) != null) {
            throw new PersistenciaException("El código de auditoría general '" + codigoGeneral + "' ya se encuentra registrado en el sistema.");
        }
    }

    /**
     * Comprueba la existencia del registro maestro previo a operaciones críticas de eliminación.
     */
    private void validarConteoGeneralExiste(String codigoGeneral) throws PersistenciaException {
        if (buscarDocumentoPorCodigoGeneral(codigoGeneral) == null) {
            throw new PersistenciaException("El registro de auditoría general con código '" + codigoGeneral + "' no existe en el sistema.");
        }
    }

    /**
     * Realiza una consulta directa a la base de datos MongoDB filtrando de forma 
     * exacta por el atributo de cabecera "codigoGeneral".
     */
    private ConteoInventarioGeneralMongoEntidad buscarDocumentoPorCodigoGeneral(String codigoGeneral) {
        return coleccion.find(Filters.eq("codigoGeneral", codigoGeneral.trim())).first();
    }
}