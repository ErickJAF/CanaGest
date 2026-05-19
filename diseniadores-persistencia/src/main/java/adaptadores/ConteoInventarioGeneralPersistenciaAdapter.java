package adaptadores;

import entidades.ConteoInventario;
import entidades.ConteoInventarioGeneral;
import entidades.ProductoResumenInventario;
import entidades.UsuarioResumen;
import entidadesmongo.ConteoInventarioGeneralMongoEntidad;
import entidadesmongo.ConteoInventarioMongoEntidad;
import entidadesmongo.ProductoResumenInventarioMongoEntidad;
import entidadesmongo.UsuarioResumenMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adapter encargado de mediar las conversiones de las sesiones globales de auditoría
 * de inventarios entre la capa de negocio (Dominio) y el driver nativo de MongoDB.
 * 
 * @author ERICK
 */
public class ConteoInventarioGeneralPersistenciaAdapter {

    /**
     * Convierte una sesión global de auditoría de dominio a una entidad raíz para MongoDB.
     */
    public ConteoInventarioGeneralMongoEntidad convertirAMongo(ConteoInventarioGeneral dominio) throws PersistenciaException {
        if (dominio == null) return null;

        ConteoInventarioGeneralMongoEntidad mongo = new ConteoInventarioGeneralMongoEntidad();
        
        mongo.setId(convertirStringAObjectId(dominio.getId()));
        mongo.setCodigoGeneral(dominio.getCodigoGeneral());
        mongo.setFechaRegistro(dominio.getFechaRegistro());
        mongo.setVerificadoGlobal(dominio.getVerificadoGlobal());
        
        mongo.setCantidadVerificados(dominio.getCantidadVerificados());
        mongo.setCantidadNoVerificados(dominio.getCantidadNoVerificados());
        mongo.setDiferenciasTotales(dominio.getDiferenciasTotales());
        
        // Cambiado a 'convertirListaItemsAMongo' para evitar duplicidad de firma
        mongo.setTodosLosConteos(convertirListaItemsAMongo(dominio.getTodosLosConteos()));

        return mongo;
    }

    /**
     * Convierte un documento raíz recuperado de MongoDB a un objeto limpio de dominio.
     */
    public ConteoInventarioGeneral convertirADominio(ConteoInventarioGeneralMongoEntidad mongo) {
        if (mongo == null) return null;

        ConteoInventarioGeneral dominio = new ConteoInventarioGeneral();
        
        dominio.setId(convertirObjectIdAString(mongo.getId()));
        dominio.setCodigoGeneral(mongo.getCodigoGeneral());
        dominio.setFechaRegistro(mongo.getFechaRegistro());
        dominio.setVerificadoGlobal(mongo.getVerificadoGlobal());
        
        dominio.setCantidadVerificados(mongo.getCantidadVerificados());
        dominio.setCantidadNoVerificados(mongo.getCantidadNoVerificados());
        dominio.setDiferenciasTotales(mongo.getDiferenciasTotales());
        
        // Cambiado a 'convertirListaItemsADominio' para evitar duplicidad de firma
        dominio.setTodosLosConteos(convertirListaItemsADominio(mongo.getTodosLosConteos()));

        return dominio;
    }

    /**
     * Convierte colecciones completas de documentos raíz (Cabeceras) a listas de dominio.
     */
    public List<ConteoInventarioGeneral> convertirListaADominio(List<ConteoInventarioGeneralMongoEntidad> entidadesMongo) {
        List<ConteoInventarioGeneral> lista = new ArrayList<>();
        if (entidadesMongo == null) return lista;
        for (ConteoInventarioGeneralMongoEntidad m : entidadesMongo) {
            lista.add(convertirADominio(m));
        }
        return lista;
    }

    private ConteoInventarioMongoEntidad convertirItemAMongo(ConteoInventario item) {
        if (item == null) return null;

        ConteoInventarioMongoEntidad mongo = new ConteoInventarioMongoEntidad();
        mongo.setCodigo(item.getCodigo());
        mongo.setFecha(item.getFecha());
        mongo.setEstado(item.getEstado());
        mongo.setCantidadContada(item.getCantidadContada());
        mongo.setDiferencia(item.getDiferencia());
        mongo.setComentario(item.getComentario());
        
        mongo.setUsuario(convertirUsuarioAMongo(item.getUsuario()));
        mongo.setProducto(convertirProductoAMongo(item.getProducto()));

        return mongo;
    }

    private ConteoInventario convertirItemADominio(ConteoInventarioMongoEntidad mongo) {
        if (mongo == null) return null;

        ConteoInventario item = new ConteoInventario();
        item.setId(null); 
        item.setCodigo(mongo.getCodigo());
        item.setFecha(mongo.getFecha());
        item.setEstado(mongo.getEstado());
        item.setCantidadContada(mongo.getCantidadContada());
        item.setDiferencia(mongo.getDiferencia());
        item.setComentario(mongo.getComentario());
        
        item.setUsuario(convertirUsuarioADominio(mongo.getUsuario()));
        item.setProducto(convertirProductoADominio(mongo.getProducto()));

        return item;
    }

    // NOMBRE CORREGIDO: Especifica que procesa sub-elementos "Items"
    private List<ConteoInventarioMongoEntidad> convertirListaItemsAMongo(List<ConteoInventario> listaDominio) {
        List<ConteoInventarioMongoEntidad> lista = new ArrayList<>();
        if (listaDominio == null) return lista;
        for (ConteoInventario c : listaDominio) {
            lista.add(convertirItemAMongo(c));
        }
        return lista;
    }

    // NOMBRE CORREGIDO: Especifica que procesa sub-elementos "Items"
    private List<ConteoInventario> convertirListaItemsADominio(List<ConteoInventarioMongoEntidad> listaMongo) {
        List<ConteoInventario> lista = new ArrayList<>();
        if (listaMongo == null) return lista;
        for (ConteoInventarioMongoEntidad m : listaMongo) {
            lista.add(convertirItemADominio(m));
        }
        return lista;
    }

    private UsuarioResumenMongoEntidad convertirUsuarioAMongo(UsuarioResumen u) {
        if (u == null) return null;
        UsuarioResumenMongoEntidad m = new UsuarioResumenMongoEntidad();
        m.setIdUsuario(u.getIdUsuario());
        m.setNombre(u.getNombre());
        m.setRol(u.getRol());
        return m;
    }

    private UsuarioResumen convertirUsuarioADominio(UsuarioResumenMongoEntidad m) {
        if (m == null) return null;
        UsuarioResumen u = new UsuarioResumen();
        u.setIdUsuario(m.getIdUsuario());
        u.setNombre(m.getNombre());
        u.setRol(m.getRol());
        return u;
    }

    private ProductoResumenInventarioMongoEntidad convertirProductoAMongo(ProductoResumenInventario p) {
        if (p == null) return null;
        ProductoResumenInventarioMongoEntidad m = new ProductoResumenInventarioMongoEntidad();
        m.setIdProducto(p.getIdProducto());
        m.setNombre(p.getNombre());
        m.setStockSistema(p.getStockSistema());
        return m;
    }

    private ProductoResumenInventario convertirProductoADominio(ProductoResumenInventarioMongoEntidad m) {
        if (m == null) return null;
        ProductoResumenInventario p = new ProductoResumenInventario();
        p.setIdProducto(m.getIdProducto());
        p.setNombre(m.getNombre());
        p.setStockSistema(m.getStockSistema());
        return p;
    }

    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) return null;
        if (!ObjectId.isValid(id)) throw new PersistenciaException("Formato hex-textual incorrecto de ID para la cabecera de Conteo General.");
        return new ObjectId(id);
    }

    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}