/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * Mapea fielmente los subdocumentos UsuarioResumen y ProductoResumenInventario.
 * * @author ERICK
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
        dominio.setEstado("PROCESADO"); // Estado operativo general de la auditoría

        // 1. Instanciamos y mapeamos el subdocumento UsuarioResumen (Auditor)
        UsuarioResumen usuarioResumen = new UsuarioResumen();
        usuarioResumen.setNombre(dto.getAuditor());
        usuarioResumen.setRol("EMPLEADO"); // Rol base por defecto
        if (dto.getAuditor() != null) {
            usuarioResumen.setIdUsuario(dto.getAuditor().toLowerCase().trim());
        }
        dominio.setUsuario(usuarioResumen);

        // 2. Instanciamos y mapeamos el subdocumento ProductoResumenInventario
        ProductoResumenInventario productoResumen = new ProductoResumenInventario();
        productoResumen.setIdProducto(dto.getCodigo());
        productoResumen.setNombre(dto.getNombre());
        productoResumen.setStockSistema(dto.getStockSistema());
        dominio.setProducto(productoResumen);

        // 3. Mapeo de Métricas y Detalles del Conteo
        dominio.setCantidadContada(dto.getStockFisico());
        dominio.setDiferencia(dto.getDiferencia()); // Método matemático calculado en tu DTO
        dominio.setEstadoConteo(dto.getEstado());   // "Verificado" o "Pendiente"
        dominio.setDetailleConteo("Auditoría física realizada en pasillo.");

        return dominio;
    }

    /**
     * Transforma una entidad ConteoInventario del Dominio al ItemConteoDTO requerido por la UI.
     */
    public ItemConteoDTO aDTO(ConteoInventario dominio) {
        if (dominio == null) return null;

        ItemConteoDTO dto = new ItemConteoDTO();
        
        // Recuperación de los campos de control
        dto.setCodigoConteo(dominio.getCodigo());
        dto.setFecha(dominio.getFecha());
        
        // Recuperación segura desde el subdocumento UsuarioResumen
        if (dominio.getUsuario() != null) {
            dto.setAuditor(dominio.getUsuario().getNombre());
        }

        // Recuperación segura desde el subdocumento ProductoResumenInventario
        if (dominio.getProducto() != null) {
            dto.setCodigo(dominio.getProducto().getIdProducto());
            dto.setNombre(dominio.getProducto().getNombre());
            dto.setStockSistema(dominio.getProducto().getStockSistema());
        }

        // Reconstrucción del Stock Físico y actualización de la bandera de verificación
        dto.setStockFisico(dominio.getCantidadContada());
        dto.setVerificado(dto.getStockSistema() == dto.getStockFisico());

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