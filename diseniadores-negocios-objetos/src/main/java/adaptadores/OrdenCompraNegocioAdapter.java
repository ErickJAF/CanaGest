package adaptadores;

import diseñadores.negocios.dto.DetalleOrdenCompraDTO;
import diseñadores.negocios.dto.OrdenCompraDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import diseñadores.negocios.dto.UsuarioDTO;
import entidades.DetalleOrdenCompra;
import entidades.OrdenCompra;
import entidades.ProductoResumenCompra;
import entidades.ProveedorResumenOrden;
import entidades.UsuarioResumen;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de negocio de alta fidelidad para la gestión de Órdenes de Compra.
 * Facilita la conversión bidireccional entre los objetos de transferencia de datos (DTO)
 * utilizados en la interfaz y las entidades puros del modelo de dominio.
 * * @author ERICK
 * @version 1.0
 */
public class OrdenCompraNegocioAdapter {
    
    /**
     * Convierte un objeto estructurado de tipo OrdenCompraDTO en una entidad
     * pura del modelo de dominio.
     * * @param dto El objeto OrdenCompraDTO proveniente de las capas superiores.
     * @return Una entidad de dominio de tipo OrdenCompra mapeada, o null si el DTO es nulo.
     */
    public OrdenCompra aDominio(OrdenCompraDTO dto) {
        if (dto == null) return null;

        OrdenCompra dominio = new OrdenCompra();
        dominio.setCodigoOrden(dto.getNumero());
        dominio.setFecha(dto.getFecha());
        dominio.setEstado(dto.getEstado());
        
        if (dto.getTotal() != null) {
            dominio.setTotal(dto.getTotal().doubleValue());
        }

        // 1. Mapeo del Proveedor embebido desde el DTO
        if (dto.getProveedor() != null) {
            String idProv = dto.getProveedor().getCodigo();
            String nomProv = dto.getProveedor().getNombre();
            String telProv = dto.getProveedor().getTelefono();
            String mailProv = dto.getProveedor().getEmail();
            
            ProveedorResumenOrden provRes = new ProveedorResumenOrden(idProv, nomProv, telProv, mailProv);
            dominio.setProveedor(provRes);
        }

        // 2. Mapeo del Usuario embebido desde el DTO
        if (dto.getUsuario() != null) {
            String idUsuario = dto.getUsuario().getNombre().toLowerCase().trim();
            String nombreUsuario = dto.getUsuario().getNombre();
            
            String rolTexto = "EMPLEADO"; 
            if (dto.getUsuario().getRol() != null) {
                rolTexto = dto.getUsuario().getRol().name();
            }

            UsuarioResumen usrRes = new UsuarioResumen(idUsuario, nombreUsuario, rolTexto);
            dominio.setUsuario(usrRes);
        }

        // 3. Mapeo de la lista de Productos en cascada desde el DTO
        List<DetalleOrdenCompra> detallesDominio = new ArrayList<>();
        if (dto.getProductos() != null) {
            for (DetalleOrdenCompraDTO detDTO : dto.getProductos()) {
                DetalleOrdenCompra detDom = new DetalleOrdenCompra();
                detDom.setCantidad(detDTO.getCantidad()); // Línea corregida (sin propiedades fantasmas)
                
                if (detDTO.getSubtotal() != null) {
                    detDom.setSubtotal(detDTO.getSubtotal().doubleValue());
                }

                // Armamos el ProductoResumenCompra que va incrustado en el detalle
                String idProd = detDTO.getIdProducto();
                String nomProd = detDTO.getNombreProducto();
                double precioComp = detDTO.getPrecioCosto() != null ? detDTO.getPrecioCosto().doubleValue() : 0.0;
                
                ProductoResumenCompra prodRes = new ProductoResumenCompra(idProd, nomProd, precioComp);
                detDom.setProducto(prodRes);
                
                detallesDominio.add(detDom);
            }
        }
        dominio.setProductos(detallesDominio);

        return dominio;
    }

    /**
     * Convierte una entidad pura del dominio en un objeto desacoplado de transferencia
     * de datos (DTO) apto para su consumo en las pantallas gráficas.
     * * @param dominio La entidad pura de negocio de tipo OrdenCompra.
     * @return Un DTO estructurado de tipo OrdenCompraDTO, o null si la entidad es nula.
     */
    public OrdenCompraDTO aDTO(OrdenCompra dominio) {
        if (dominio == null) return null;

        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setNumero(dominio.getCodigoOrden());
        dto.setFecha(dominio.getFecha());
        dto.setEstado(dominio.getEstado());
        dto.setTotal(BigDecimal.valueOf(dominio.getTotal()));

        // 1. Reconstrucción de ProveedorDTO
        if (dominio.getProveedor() != null) {
            ProveedorDTO provDTO = new ProveedorDTO();
            provDTO.setCodigo(dominio.getProveedor().getIdProveedor());
            provDTO.setNombre(dominio.getProveedor().getNombre());
            provDTO.setTelefono(dominio.getProveedor().getTelefono());
            provDTO.setEmail(dominio.getProveedor().getGmail());
            provDTO.setActivo(true);
            dto.setProveedor(provDTO);
        }

        // 2. Reconstrucción de UsuarioDTO
        if (dominio.getUsuario() != null) {
            UsuarioDTO usrDTO = new UsuarioDTO();
            usrDTO.setNombre(dominio.getUsuario().getNombre());
            try {
                usrDTO.setRol(diseñadores.negocios.dto.UsuarioRol.valueOf(dominio.getUsuario().getRol()));
            } catch (Exception e) {
                // Resguardo silencioso en caso de inconsistencia con Enums
            }
            dto.setUsuario(usrDTO);
        }

        // 3. Reconstrucción de la Lista de Productos DTO
        List<DetalleOrdenCompraDTO> detallesDTO = new ArrayList<>();
        if (dominio.getProductos() != null) {
            for (DetalleOrdenCompra detDom : dominio.getProductos()) {
                DetalleOrdenCompraDTO detDTO = new DetalleOrdenCompraDTO();
                detDTO.setCantidad(detDom.getCantidad());
                detDTO.setSubtotal(BigDecimal.valueOf(detDom.getSubtotal()));

                if (detDom.getProducto() != null) {
                    detDTO.setIdProducto(detDom.getProducto().getIdProducto());
                    detDTO.setNombreProducto(detDom.getProducto().getNombre());
                    detDTO.setPrecioCosto(BigDecimal.valueOf(detDom.getProducto().getPrecioCompra()));
                }
                
                detallesDTO.add(detDTO);
            }
        }
        dto.setProductos(detallesDTO);

        return dto;
    }
    
    /**
     * Transforma una lista de entidades de dominio puro en una nueva lista
     * equivalente de objetos de transferencia de datos (DTO).
     * * @param ordenesDominio Lista de entidades puras de tipo OrdenCompra.
     * @return Una lista procesada que contiene objetos OrdenCompraDTO.
     */
    public List<OrdenCompraDTO> listaADTO(List<OrdenCompra> ordenesDominio) {
        List<OrdenCompraDTO> listaDTO = new ArrayList<>();
        if (ordenesDominio == null) return listaDTO;
        for (OrdenCompra o : ordenesDominio) {
            listaDTO.add(aDTO(o));
        }
        return listaDTO;
    }
}