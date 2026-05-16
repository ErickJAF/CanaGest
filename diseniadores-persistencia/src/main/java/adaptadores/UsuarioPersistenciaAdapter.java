/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import entidades.Usuario;
import entidadesmongo.UsuarioMongoEntidad;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Adapter encargado de convertir objetos del dominio limpio de Usuarios a entidades
 * de persistencia de MongoDB y viceversa.
 * Su objetivo es aislar el manejo de ObjectId dentro de la capa de persistencia.
 * 
 * @author ERICK
 */
public class UsuarioPersistenciaAdapter {

    /**
     * Convierte un usuario del dominio limpio a una entidad de MongoDB.
     *
     * @param usuario usuario del dominio limpio.
     * @return entidad de usuario lista para persistirse en MongoDB.
     * @throws PersistenciaException si el id no tiene formato válido de ObjectId.
     */
    public UsuarioMongoEntidad convertirAMongo(Usuario usuario) throws PersistenciaException {
        if (usuario == null) {
            return null;
        }

        UsuarioMongoEntidad entidadMongo = new UsuarioMongoEntidad();
        entidadMongo.setId(convertirStringAObjectId(usuario.getId()));
        entidadMongo.setIdUsuario(usuario.getIdUsuario());
        entidadMongo.setNombre(usuario.getNombre());
        entidadMongo.setContraseña(usuario.getContraseña());
        entidadMongo.setRol(usuario.getRol());

        return entidadMongo;
    }

    /**
     * Convierte una entidad de MongoDB a un usuario del dominio limpio.
     *
     * @param entidadMongo entidad recuperada de MongoDB.
     * @return usuario del dominio limpio.
     */
    public Usuario convertirADominio(UsuarioMongoEntidad entidadMongo) {
        if (entidadMongo == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(convertirObjectIdAString(entidadMongo.getId()));
        usuario.setIdUsuario(entidadMongo.getIdUsuario());
        usuario.setNombre(entidadMongo.getNombre());
        usuario.setContraseña(entidadMongo.getContraseña());
        usuario.setRol(entidadMongo.getRol());

        return usuario;
    }

    /**
     * Convierte una lista de entidades MongoDB a una lista de usuarios del dominio limpio.
     *
     * @param entidadesMongo lista de entidades de MongoDB.
     * @return lista de usuarios limpios.
     */
    public List<Usuario> convertirListaADominio(List<UsuarioMongoEntidad> entidadesMongo) {
        List<Usuario> usuarios = new ArrayList<>();
        if (entidadesMongo == null) {
            return usuarios;
        }
        for (UsuarioMongoEntidad entidadMongo : entidadesMongo) {
            usuarios.add(convertirADominio(entidadMongo));
        }
        return usuarios;
    }

    /**
     * Convierte un id de texto a ObjectId.
     *
     * @param id identificador como texto.
     * @return ObjectId correspondiente o null si está vacío.
     * @throws PersistenciaException si el id no tiene formato válido.
     */
    public ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) {
            return null;
        }
        if (!ObjectId.isValid(id)) {
            throw new PersistenciaException("El id recibido no tiene formato válido de ObjectId.");
        }
        return new ObjectId(id);
    }

    /**
     * Convierte un ObjectId a String hexadecimal.
     *
     * @param id identificador ObjectId.
     * @return identificador como texto.
     */
    public String convertirObjectIdAString(ObjectId id) {
        return (id == null) ? null : id.toHexString();
    }
}
