/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.ConteoInventario;
import entidades.ProductoResumenInventario;
import entidades.UsuarioResumen;
import entidadesmongo.ConteoInventarioMongoEntidad;
import entidadesmongo.ProductoResumenInventarioMongoEntidad;
import entidadesmongo.UsuarioResumenMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adapter encargado de mediar las conversiones de los registros de auditorías físicas 
 * de inventarios entre la capa de negocio y el driver nativo de MongoDB.
 *
 * @author ERICK
 */
public class ConteoInventarioPersistenciaAdapter {

    /**
     * Convierte un conteo de inventario de dominio a entidad MongoDB.
     *
     * @param conteo objeto auditoría de dominio.
     * @return entidad con estructura mongo.
     * @throws PersistenciaException si el id string falla las reglas de ObjectId.
     */
    public ConteoInventarioMongoEntidad convertirAMongo(ConteoInventario conteo) throws PersistenciaException {
        if (conteo == null) return null;

        ConteoInventarioMongoEntidad mongo = new ConteoInventarioMongoEntidad();
        mongo.setId(convertirStringAObjectId(conteo.getId()));
        mongo.setCodigo(conteo.getCodigo());
        mongo.setFecha(conteo.getFecha());
        mongo.setEstado(conteo.getEstado());
        mongo.setCantidadContada(conteo.getCantidadContada());
        mongo.setDiferencia(conteo.getDiferencia());
        mongo.setEstadoConteo(conteo.getEstadoConteo());
        mongo.setDetalleConteo(conteo.getDetailleConteo());
        mongo.setUsuario(convertirUsuarioAMongo(conteo.getUsuario()));
        mongo.setProducto(convertirProductoAMongo(conteo.getProducto()));

        return mongo;
    }

    /**
     * Convierte una entidad recuperada de Mongo a un objeto limpio de dominio de auditoría.
     *
     * @param mongo entidad de la base de datos.
     * @return objeto de dominio limpio.
     */
    public ConteoInventario convertirADominio(ConteoInventarioMongoEntidad mongo) {
        if (mongo == null) return null;

        ConteoInventario conteo = new ConteoInventario();
        conteo.setId(convertirObjectIdAString(mongo.getId()));
        conteo.setCodigo(mongo.getCodigo());
        conteo.setFecha(mongo.getFecha());
        conteo.setEstado(mongo.getEstado());
        conteo.setCantidadContada(mongo.getCantidadContada());
        conteo.setDiferencia(mongo.getDiferencia());
        conteo.setEstadoConteo(mongo.getEstadoConteo());
        conteo.setDetailleConteo(mongo.getDetalleConteo());
        conteo.setUsuario(convertirUsuarioADominio(mongo.getUsuario()));
        conteo.setProducto(convertirProductoADominio(mongo.getProducto()));

        return conteo;
    }

    /**
     * Convierte colecciones completas de documentos mongo a listas limpias de auditoría.
     *
     * @param entidadesMongo lista de entidades de base de datos.
     * @return lista de dominio.
     */
    public List<ConteoInventario> convertirListaADominio(List<ConteoInventarioMongoEntidad> entidadesMongo) {
        List<ConteoInventario> lista = new ArrayList<>();
        if (entidadesMongo == null) return lista;
        for (ConteoInventarioMongoEntidad m : entidadesMongo) {
            lista.add(convertirADominio(m));
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

    /**
     * Transforma texto String en una clase ObjectId técnica.
     *
     * @param id el identificador textual.
     * @return ObjectId.
     * @throws PersistenciaException error de conversión.
     */
    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) return null;
        if (!ObjectId.isValid(id)) throw new PersistenciaException("Formato incorrecto de id para Conteo.");
        return new ObjectId(id);
    }

    /**
     * Transforma un ObjectId técnico a String plano.
     *
     * @param id identificador de base de datos.
     * @return String legible.
     */
    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}
