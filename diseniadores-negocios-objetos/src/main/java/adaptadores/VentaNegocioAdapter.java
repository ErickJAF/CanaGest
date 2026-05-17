/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import diseñadores.negocios.dto.ItemVentaDTO;
import diseñadores.negocios.dto.TipoPago;
import diseñadores.negocios.dto.VentaDTO;
import entidades.DetalleVenta;
import entidades.ProductoResumenVenta;
import entidades.Venta;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de la capa de Negocio encargado de mediar las conversiones bidireccionales
 * entre el carrito de ventas (VentaDTO) y el almacenamiento limpio (Venta),
 * mapeando de forma exacta el subdocumento ProductoResumenVenta.
 * @author ERICK
 */
public class VentaNegocioAdapter {

    /**
     * Convierte el DTO de la UI hacia la entidad pura de Dominio requerida por Mongo.
     */
    public Venta aDominio(VentaDTO dto) {
        if (dto == null) return null;

        Venta dominio = new Venta();
        dominio.setCodigoVenta(dto.getFolio());
        dominio.setFechaVenta(dto.getFecha());
        
        if (dto.getTotal() != null) {
            dominio.setTotal(dto.getTotal().doubleValue());
        }

        // Mapeo seguro del Enum TipoPago al String que espera el Dominio
        if (dto.getTipoPago() != null) {
            dominio.setMetodoPago(dto.getTipoPago().name());
        } else {
            dominio.setMetodoPago("EFECTIVO");
        }

        // Conversión en cascada usando ProductoResumenVenta con su orden de constructor exacto
        List<DetalleVenta> detallesDominio = new ArrayList<>();
        if (dto.getItems() != null) {
            for (ItemVentaDTO itemDTO : dto.getItems()) {
                DetalleVenta detalleDom = new DetalleVenta();
                detalleDom.setCantidad(itemDTO.getCantidad());
                
                if (itemDTO.getSubtotal() != null) {
                    detalleDom.setSubtotal(itemDTO.getSubtotal().doubleValue());
                }

                // Extraemos los datos del item para el constructor
                String idProd = itemDTO.getCodigo();
                double precioVenta = itemDTO.getPrecioUnitario() != null ? itemDTO.getPrecioUnitario().doubleValue() : 0.0;
                String nomProd = itemDTO.getNombre();
                
                // Constructor exacto: idProducto, precio, nombre
                ProductoResumenVenta prodResumen = new ProductoResumenVenta(idProd, precioVenta, nomProd);
                detalleDom.setProducto(prodResumen);
                
                detallesDominio.add(detalleDom);
            }
        }
        dominio.setDetalles(detallesDominio);

        return dominio;
    }

    /**
     * Convierte la entidad de Dominio recuperada de la BD a un DTO listo para la interfaz.
     */
    public VentaDTO aDTO(Venta dominio) {
        if (dominio == null) return null;

        VentaDTO dto = new VentaDTO();
        dto.setFolio(dominio.getCodigoVenta());
        dto.setFecha(dominio.getFechaVenta());
        dto.setTotal(BigDecimal.valueOf(dominio.getTotal()));
        dto.setPagada(true); 

        // Conversión inversa del Método de Pago
        if (dominio.getMetodoPago() != null) {
            try {
                dto.setTipoPago(TipoPago.valueOf(dominio.getMetodoPago().toUpperCase().trim()));
            } catch (Exception e) {
                dto.setTipoPago(TipoPago.EFECTIVO);
            }
        }

        // Reconstrucción de la lista de ítems DTO extrayendo los datos del subdocumento
        List<ItemVentaDTO> itemsDTO = new ArrayList<>();
        if (dominio.getDetalles() != null) {
            for (DetalleVenta detDom : dominio.getDetalles()) {
                
                String codigo = "";
                String nombre = "";
                BigDecimal precio = BigDecimal.ZERO;
                
                // Extraemos de forma segura del subdocumento usando sus getters reales
                if (detDom.getProducto() != null) {
                    codigo = detDom.getProducto().getIdProducto(); 
                    nombre = detDom.getProducto().getNombre();
                    precio = BigDecimal.valueOf(detDom.getProducto().getPrecio());
                }
                
                // Instanciamos usando tu constructor de ItemVentaDTO
                ItemVentaDTO itemDTO = new ItemVentaDTO(codigo, nombre, precio, detDom.getCantidad());
                itemsDTO.add(itemDTO);
            }
        }
        dto.setItems(itemsDTO);

        return dto;
    }

    /**
     * Mapea colecciones completas de historial de ventas.
     */
    public List<VentaDTO> listaADTO(List<Venta> ventasDominio) {
        List<VentaDTO> listaDTO = new ArrayList<>();
        if (ventasDominio == null) return listaDTO;
        for (Venta v : ventasDominio) {
            listaDTO.add(aDTO(v));
        }
        return listaDTO;
    }
}