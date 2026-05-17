/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import diseñadores.negocios.dto.UsuarioDTO;
import diseñadores.negocios.dto.UsuarioRol;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de la capa de Negocio encargado de mediar las conversiones bidireccionales
 * entre la entidad de Dominio Limpio Usuario y su respectivo DTO de presentación.
 * * @author ERICK
 */
public class UsuarioNegocioAdapter {

    /**
     * Transforma un UsuarioDTO de la presentación a una entidad Usuario de Dominio.
     */
    public Usuario aDominio(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario dominio = new Usuario();
        dominio.setNombre(dto.getNombre());
        dominio.setContraseña(dto.getContrasena()); // Mapeo de contrasena -> contraseña
        
        // Generamos un idUsuario seguro basándonos en el nombre si viene disponible
        if (dto.getNombre() != null) {
            dominio.setIdUsuario(dto.getNombre().toLowerCase().trim());
        }

        // Conversión segura del Enum de la UI al String que requiere el Dominio
        if (dto.getRol() != null) {
            dominio.setRol(dto.getRol().name()); // "ADMINISTRADOR", "EMPLEADO", etc.
        } else {
            dominio.setRol("EMPLEADO"); // Valor por defecto preventivo
        }

        return dominio;
    }

    /**
     * Transforma una entidad Usuario de Dominio a un UsuarioDTO para la vista.
     */
    public UsuarioDTO aDTO(Usuario dominio) {
        if (dominio == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre(dominio.getNombre());
        dto.setContrasena(dominio.getContraseña());

        // Conversión inversa: String del Dominio al Enum tipado de la UI
        if (dominio.getRol() != null) {
            try {
                dto.setRol(UsuarioRol.valueOf(dominio.getRol().toUpperCase().trim()));
            } catch (Exception e) {
                // En caso de discrepancia en el String, asignamos un rol base para evitar caídas en cascada
                dto.setRol(UsuarioRol.ADMINISTRADOR); 
            }
        }

        return dto;
    }

    /**
     * Convierte una lista de usuarios de Dominio a una lista de DTOs listos para la UI.
     */
    public List<UsuarioDTO> listaADTO(List<Usuario> usuariosDominio) {
        List<UsuarioDTO> listaDTO = new ArrayList<>();
        if (usuariosDominio == null) return listaDTO;
        for (Usuario u : usuariosDominio) {
            listaDTO.add(aDTO(u));
        }
        return listaDTO;
    }
}
