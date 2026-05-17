package diseñadores.persistencia.conexion;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidadesmongo.ConteoInventarioMongoEntidad;
import entidadesmongo.OrdenCompraMongoEntidad;
import entidadesmongo.ProductoMongoEntidad;
import entidadesmongo.UsuarioMongoEntidad;
import entidadesmongo.VentaMongoEntidad;
import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Clase encargada de administrar la conexión con MongoDB.
 * Esta clase pertenece exclusivamente a la capa de persistencia y configura 
 * el registro de Codecs POJO para permitir el mapeo automático entre objetos 
 * Java y documentos BSON.
 * 
 * @autor Erick
 */
public class Conexion {

    private static final String URL = "mongodb://localhost:27017";
    private static final String NOMBRE_BASE_DATOS = "canagest";
    
    private static final String COLECCION_USUARIOS = "usuarios";
    private static final String COLECCION_PRODUCTOS = "productos";
    private static final String COLECCION_VENTAS = "ventas";
    private static final String COLECCION_ORDENES = "ordenesCompra";
    private static final String COLECCION_CONTEOS = "conteosInventario";

    private static Conexion instancia;
    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Constructor privado que inicializa el cliente de MongoDB configurando
     * el CodecRegistry para POJOs y la base de datos central.
     */
    private Conexion() {
        try {
            // Permite que MongoDB convierta automáticamente tus clases a documentos BSON y viceversa
            CodecProvider proveedorPojo = PojoCodecProvider.builder()
                    .automatic(true)
                    .build();

            // Combina los códecs por defecto de Mongo con el proveedor automático de POJOs
            CodecRegistry registroCodecs = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(proveedorPojo)
            );

            // Aplica la configuración con la URL y los Codecs unificados
            MongoClientSettings configuracion = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URL))
                    .codecRegistry(registroCodecs)
                    .build();

            // Guardamos las instancias en los atributos del objeto
            this.mongoClient = MongoClients.create(configuracion);
            this.database = this.mongoClient.getDatabase(NOMBRE_BASE_DATOS);
        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB: " + e.getMessage());
        }
    }

    /**
     * Obtiene de manera única y sincronizada la instancia de la clase Conexion.
     * @return Instancia única de Conexion.
     */
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Obtiene la base de datos centralizada del sistema.
     * * @return MongoDatabase con los códecs mapeados.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Cierra de forma segura el cliente de MongoDB y destruye la instancia única.
     */
    public void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            instancia = null;
        }
    }
    
    /**
     * Centraliza el acceso a la colección de Usuarios mapeada con su entidad de persistencia.
     * @return MongoCollection tipada para Usuarios.
     */
    public MongoCollection<UsuarioMongoEntidad> obtenerColeccionUsuarios() {
        return getDatabase().getCollection(COLECCION_USUARIOS, UsuarioMongoEntidad.class);
    }

    /**
     * Centraliza el acceso a la colección de Productos mapeada con su entidad de persistencia.
     * @return MongoCollection tipada para Productos.
     */
    public MongoCollection<ProductoMongoEntidad> obtenerColeccionProductos() {
        return getDatabase().getCollection(COLECCION_PRODUCTOS, ProductoMongoEntidad.class);
    }

    /**
     * Centraliza el acceso a la colección de Ventas mapeada con su entidad de persistencia.
     * @return MongoCollection tipada para Ventas.
     */
    public MongoCollection<VentaMongoEntidad> obtenerColeccionVentas() {
        return getDatabase().getCollection(COLECCION_VENTAS, VentaMongoEntidad.class);
    }

    /**
     * Centraliza el acceso a la colección de Órdenes de Compra mapeada con su entidad de persistencia.
     * @return MongoCollection tipada para Órdenes de Compra.
     */
    public MongoCollection<OrdenCompraMongoEntidad> obtenerColeccionOrdenes() {
        return getDatabase().getCollection(COLECCION_ORDENES, OrdenCompraMongoEntidad.class);
    }

    /**
     * Centraliza el acceso a la colección de Conteos de Inventario mapeada con su entidad de persistencia.
     * @return MongoCollection tipada para Conteos de Inventario.
     */
    public MongoCollection<ConteoInventarioMongoEntidad> obtenerColeccionConteos() {
        return getDatabase().getCollection(COLECCION_CONTEOS, ConteoInventarioMongoEntidad.class);
    }
}