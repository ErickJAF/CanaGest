package diseñadores.persistencia.dao.impl;

import adaptadores.UsuarioPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IUsuarioDAO;
import entidades.Usuario;
import entidadesmongo.UsuarioMongoEntidad;
import excepciones.PersistenciaException;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar la colección de usuarios en MongoDB.
 * Emplea la conexión centralizada Singleton y utiliza un adaptador perimetral
 * para transformar las entidades de persistencia POJO a objetos de negocio puros.
 * * @author ERICK
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    private final MongoCollection<UsuarioMongoEntidad> coleccion;
    private final UsuarioPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la colección tipada de MongoDB 
     * mediante la instancia del Singleton de Conexión y levanta el adaptador.
     */
    public UsuarioDAOImpl() {
        this.coleccion = Conexion.getInstancia().obtenerColeccionUsuarios();
        this.adaptador = new UsuarioPersistenciaAdapter();
    }

    @Override
    public List<Usuario> obtenerTodos() throws PersistenciaException {
        try {
            List<UsuarioMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible obtener la lista de usuarios desde MongoDB.", ex);
        }
    }

    @Override
    public Usuario obtenerPorIdUsuario(String idUsuario) throws PersistenciaException {
        validarIdUsuarioRequerido(idUsuario);
        try {
            UsuarioMongoEntidad entidad = buscarDocumentoPorIdUsuario(idUsuario);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible buscar el usuario por su identificador.", ex);
        }
    }

    @Override
    public void guardar(Usuario usuario) throws PersistenciaException {
        validarDatosUsuario(usuario);
        validarIdUsuarioDisponible(usuario.getIdUsuario());
        try {
            ejecutarInsercion(usuario);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible registrar al usuario en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(Usuario usuario) throws PersistenciaException {
        validarDatosUsuario(usuario);
        validarUsuarioExiste(usuario.getIdUsuario());
        try {
            ejecutarReemplazo(usuario);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar los datos del usuario seleccionado.", ex);
        }
    }

    @Override
    public void eliminar(String idUsuario) throws PersistenciaException {
        validarIdUsuarioRequerido(idUsuario);
        validarUsuarioExiste(idUsuario);
        try {
            ejecutarEliminacion(idUsuario);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar al usuario de MongoDB.", ex);
        }
    }

    // --- MÉTODOS PRIVADOS DE VALIDACIÓN ---

    /**
     * Valida de forma defensiva que el parámetro del identificador no sea nulo ni esté vacío.
     */
    private void validarIdUsuarioRequerido(String idUsuario) throws PersistenciaException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new PersistenciaException("El identificador (idUsuario) es obligatorio y no puede estar vacío.");
        }
    }

    /**
     * Verifica la integridad estructural del objeto de dominio antes de operar sobre la base de datos.
     */
    private void validarDatosUsuario(Usuario usuario) throws PersistenciaException {
        if (usuario == null) {
            throw new PersistenciaException("El usuario no puede ser nulo.");
        }
        validarIdUsuarioRequerido(usuario.getIdUsuario());
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new PersistenciaException("El nombre del usuario es obligatorio.");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().isBlank()) {
            throw new PersistenciaException("La contraseña del usuario no puede estar vacía.");
        }
        if (usuario.getRol() == null) {
            throw new PersistenciaException("El usuario debe tener asignado un rol válido.");
        }
    }

    /**
     * Asegura que el identificador propuesto no esté registrado previamente.
     */
    private void validarIdUsuarioDisponible(String idUsuario) throws PersistenciaException {
        if (buscarDocumentoPorIdUsuario(idUsuario) != null) {
            throw new PersistenciaException("El identificador de usuario '" + idUsuario + "' ya se encuentra registrado.");
        }
    }

    /**
     * Comprueba la existencia real del usuario previo a operaciones de actualización o baja.
     */
    private void validarUsuarioExiste(String idUsuario) throws PersistenciaException {
        if (buscarDocumentoPorIdUsuario(idUsuario) == null) {
            throw new PersistenciaException("El usuario con el identificador '" + idUsuario + "' no existe en el sistema.");
        }
    }

    // --- MÉTODOS PRIVADOS DE PROCESAMIENTO ATÓMICO MONGODB ---

    /**
     * Realiza la consulta directa en la colección utilizando filtros BSON.
     * Aplica limpieza de cadenas (trim) para evitar fallos de espacios en blanco.
     */
    private UsuarioMongoEntidad buscarDocumentoPorIdUsuario(String idUsuario) {
        String idLimpio = (idUsuario != null) ? idUsuario.trim() : "";
        return coleccion.find(Filters.eq("idUsuario", idLimpio)).first();
    }

    /**
     * Invoca la operación atómica de inserción delegando la transformación al adapter.
     */
    private void ejecutarInsercion(Usuario usuario) throws PersistenciaException {
        UsuarioMongoEntidad entidadMongo = adaptador.convertirAMongo(usuario);
        coleccion.insertOne(entidadMongo);
    }

    /**
     * Ejecuta el reemplazo integral del documento garantizando que se respete el _id nativo de Mongo.
     */
    private void ejecutarReemplazo(Usuario usuario) throws PersistenciaException {
        UsuarioMongoEntidad documentoActual = buscarDocumentoPorIdUsuario(usuario.getIdUsuario());
        UsuarioMongoEntidad entidadMongo = adaptador.convertirAMongo(usuario);

        // Clave: Resguardamos el ObjectId original para mitigar el error de inmutabilidad del _id
        if (documentoActual != null) {
            entidadMongo.setId(documentoActual.getId());
        }

        coleccion.replaceOne(Filters.eq("idUsuario", usuario.getIdUsuario()), entidadMongo);
    }

    /**
     * Remueve físicamente el documento correspondiente de la colección.
     */
    private void ejecutarEliminacion(String idUsuario) {
        coleccion.deleteOne(Filters.eq("idUsuario", idUsuario));
    }
}