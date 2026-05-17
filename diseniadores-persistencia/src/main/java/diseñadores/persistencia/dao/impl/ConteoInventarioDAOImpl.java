/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diseñadores.persistencia.dao.impl;

import adaptadores.ConteoInventarioPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import diseñadores.persistencia.conexion.Conexion;
import diseñadores.persistencia.dao.IConteoInventarioDAO;
import entidades.ConteoInventario;
import entidadesmongo.ConteoInventarioMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación orientada a objetos para la persistencia de Conteos de Inventario.
 * Gestiona de forma directa la colección tipada de MongoDB aislando el dominio 
 * mediante su adaptador perimetral correspondiente.
 * 
 * @author ERICK
 */
public class ConteoInventarioDAOImpl implements IConteoInventarioDAO {

    private final MongoCollection<ConteoInventarioMongoEntidad> coleccion;
    private final ConteoInventarioPersistenciaAdapter adaptador;

    /**
     * Constructor por defecto que inicializa la conexión con la colección 
     * de conteos de inventario y el adaptador de persistencia.
     */
    public ConteoInventarioDAOImpl() {
        this.coleccion = Conexion.getInstancia().obtenerColeccionConteos();
        this.adaptador = new ConteoInventarioPersistenciaAdapter();
    }

    @Override
    public List<ConteoInventario> obtenerTodos() throws PersistenciaException {
        try {
            List<ConteoInventarioMongoEntidad> entidadesMongo = new ArrayList<>();
            coleccion.find().into(entidadesMongo);
            return adaptador.convertirListaADominio(entidadesMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible recuperar el historial de conteos de inventario.", ex);
        }
    }

    @Override
    public ConteoInventario obtenerPorCodigo(String codigo) throws PersistenciaException {
        validarCodigoRequerido(codigo);
        try {
            ConteoInventarioMongoEntidad entidad = buscarDocumentoPorCodigo(codigo);
            return (entidad != null) ? adaptador.convertirADominio(entidad) : null;
        } catch (MongoException ex) {
            throw new PersistenciaException("Error al buscar el conteo de inventario con código: " + codigo, ex);
        }
    }

    @Override
    public void guardar(ConteoInventario conteo) throws PersistenciaException {
        validarDatosConteo(conteo);
        validarCodigoDisponible(conteo.getCodigo());
        try {
            ConteoInventarioMongoEntidad entidadMongo = adaptador.convertirAMongo(conteo);
            coleccion.insertOne(entidadMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible registrar el conteo de inventario en MongoDB.", ex);
        }
    }

    @Override
    public void actualizar(ConteoInventario conteo) throws PersistenciaException {
        validarDatosConteo(conteo);
        validarConteoExiste(conteo.getCodigo());
        try {
            ConteoInventarioMongoEntidad documentoActual = buscarDocumentoPorCodigo(conteo.getCodigo());
            ConteoInventarioMongoEntidad entidadMongo = adaptador.convertirAMongo(conteo);

            // Resguardamos el ObjectId original para mitigar errores de mutabilidad de claves primarias
            if (documentoActual != null) {
                entidadMongo.setId(documentoActual.getId());
            }

            coleccion.replaceOne(Filters.eq("codigo", conteo.getCodigo()), entidadMongo);
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible actualizar el registro del conteo de inventario.", ex);
        }
    }

    @Override
    public void eliminar(String codigo) throws PersistenciaException {
        validarCodigoRequerido(codigo);
        validarConteoExiste(codigo);
        try {
            coleccion.deleteOne(Filters.eq("codigo", codigo));
        } catch (MongoException ex) {
            throw new PersistenciaException("No fue posible eliminar el registro de inventario de MongoDB.", ex);
        }
    }

    // --- MÉTODOS PRIVADOS DE VALIDACIÓN ---

    /**
     * Valida de manera defensiva que el código de identificación de auditoría no sea nulo o vacío.
     */
    private void validarCodigoRequerido(String codigo) throws PersistenciaException {
        if (codigo == null || codigo.isBlank()) {
            throw new PersistenciaException("El código del conteo de inventario es un dato obligatorio.");
        }
    }

    /**
     * Analiza que el objeto de negocio de conteo cumpla las reglas de integridad requeridas.
     */
    private void validarDatosConteo(ConteoInventario conteo) throws PersistenciaException {
        if (conteo == null) {
            throw new PersistenciaException("El registro de conteo de inventario no puede ser nulo.");
        }
        validarCodigoRequerido(conteo.getCodigo());
        if (conteo.getProducto() == null) {
            throw new PersistenciaException("El conteo de inventario debe estar asociado a un producto válido.");
        }
        if (conteo.getUsuario() == null) {
            throw new PersistenciaException("Debe especificarse el usuario/auditor que realizó el conteo.");
        }
        if (conteo.getFecha() == null) {
            throw new PersistenciaException("La fecha de ejecución del registro es obligatoria.");
        }
    }

    /**
     * Asegura la inexistencia del código para prevenir sobreescrituras accidentales en la inserción.
     */
    private void validarCodigoDisponible(String codigo) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigo) != null) {
            throw new PersistenciaException("El código de auditoría '" + codigo + "' ya está registrado en el sistema.");
        }
    }

    /**
     * Comprueba la existencia física del registro previo a operaciones de edición o baja.
     */
    private void validarConteoExiste(String codigo) throws PersistenciaException {
        if (buscarDocumentoPorCodigo(codigo) == null) {
            throw new PersistenciaException("El registro de inventario con código '" + codigo + "' no existe.");
        }
    }

    // --- MÉTODOS PRIVADOS DE ENLACE MONGODB ---

    /**
     * Consulta el almacén de datos buscando coincidencia exacta por el atributo comercial "codigo".
     */
    private ConteoInventarioMongoEntidad buscarDocumentoPorCodigo(String codigo) {
        return coleccion.find(Filters.eq("codigo", codigo.trim())).first();
    }
}