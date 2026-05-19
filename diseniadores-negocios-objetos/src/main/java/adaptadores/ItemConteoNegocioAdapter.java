package adaptadores;

import diseñadores.negocios.dto.ItemConteoDTO;
import entidades.ConteoInventario;
import entidades.ProductoResumenInventario;
import entidades.UsuarioResumen;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de la capa de Negocio encargado de las conversiones bidireccionales
 * entre el DTO de la UI (ItemConteoDTO) y la entidad pura de dominio (ConteoInventario).
 * Mapea de forma dinámica los subdocumentos UsuarioResumen y ProductoResumenInventario.
 * Sincronizado exactamente con los atributos reales del dominio.
 * 
 * @author ERICK
 */
public class ItemConteoNegocioAdapter {

    /**
     * Transforma un ItemConteoDTO de la UI a la entidad de Dominio ConteoInventario para MongoDB.
     */
    public ConteoInventario aDominio(ItemConteoDTO dto) {
        if (dto == null) return null;

        ConteoInventario dominio = new ConteoInventario();
        
        // Mapeo de Atributos Directos del Encabezado
        dominio.setCodigo(dto.getCodigoConteo());
        dominio.setFecha(dto.getFecha());
        
        // CORRECCIÓN: Se asigna el booleano al atributo correcto del dominio
        dominio.setEstado(dto.isVerificado()); 

        // 1. Instanciamos y mapeamos el subdocumento UsuarioResumen dinámicamente
        UsuarioResumen usuarioResumen = new UsuarioResumen();
        usuarioResumen.setIdUsuario(dto.getCodigoUsuario() != null ? dto.getCodigoUsuario().trim() : null);
        usuarioResumen.setNombre(dto.getNombreUsuario());
        usuarioResumen.setRol(dto.getRolUsuario() != null ? dto.getRolUsuario().toUpperCase() : "EMPLEADO"); 
        dominio.setUsuario(usuarioResumen);

        // 2. Instanciamos y mapeamos el subdocumento ProductoResumenInventario
        ProductoResumenInventario productoResumen = new ProductoResumenInventario();
        productoResumen.setIdProducto(dto.getProductoCodigo());
        productoResumen.setNombre(dto.getProductoNombre());
        productoResumen.setStockSistema(dto.getProductoStockSistema());
        dominio.setProducto(productoResumen);

        // 3. Mapeo de Métricas y Justificación del Conteo
        dominio.setCantidadContada(dto.getProductoStockFisico());
        dominio.setDiferencia(dto.getDiferencia()); 
        dominio.setComentario(dto.getComentario());

        return dominio;
    }

    /**
     * Transforma una entidad ConteoInventario del Dominio al ItemConteoDTO requerido por la UI.
     */
    public ItemConteoDTO aDTO(ConteoInventario dominio) {
        if (dominio == null) return null;

        ItemConteoDTO dto = new ItemConteoDTO();
        
        // Recuperación de los campos de control generales
        dto.setCodigoConteo(dominio.getCodigo());
        dto.setFecha(dominio.getFecha());
        dto.setComentario(dominio.getComentario());
        
        // CORRECCIÓN: Recuperación del booleano usando el método exacto del dominio
        if (dominio.getEstado() != null) {
            dto.setVerificado(dominio.getEstado());
        } else {
            dto.setVerificado(false); // Respaldo por seguridad si llega nulo de la BD
        }
        
        // Recuperación segura desde el subdocumento UsuarioResumen
        if (dominio.getUsuario() != null) {
            dto.setCodigoUsuario(dominio.getUsuario().getIdUsuario());
            dto.setNombreUsuario(dominio.getUsuario().getNombre());
            dto.setRolUsuario(dominio.getUsuario().getRol());
        }

        // Recuperación segura desde el subdocumento ProductoResumenInventario
        if (dominio.getProducto() != null) {
            dto.setProductoCodigo(dominio.getProducto().getIdProducto());
            dto.setProductoNombre(dominio.getProducto().getNombre());
            dto.setProductoStockSistema(dominio.getProducto().getStockSistema());
        }

        // Reconstrucción de métricas físicas para la UI
        dto.setProductoStockFisico(dominio.getCantidadContada());

        return dto;
    }

    /**
     * Convierte una lista completa de registros de dominio a una lista de DTOs para las tablas.
     */
    public List<ItemConteoDTO> listaADTO(List<ConteoInventario> listaDominio) {
        List<ItemConteoDTO> listaDTO = new ArrayList<>();
        if (listaDominio == null) return listaDTO;
        for (ConteoInventario c : listaDominio) {
            listaDTO.add(aDTO(c));
        }
        return listaDTO;
    }
}