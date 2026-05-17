/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import diseñadores.negocios.dto.ProductoDTO;
import diseñadores.negocios.dto.ProveedorDTO;
import entidades.Domicilio;
import entidades.Producto;
import entidades.Proveedor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de la capa de Negocio encargado de mediar las conversiones bidireccionales
 * entre las entidades de Dominio Limpio y los DTOs para los módulos de Productos y Proveedores.
 * 
 * @author ERICK
 */
public class ProductoProveedorNegocioAdapter {

    /**
     * Transforma un ProveedorDTO de la presentación a una entidad Proveedor de Dominio.
     */
    public Proveedor proveedorADominio(ProveedorDTO dto) {
        if (dto == null) return null;

        Proveedor dominio = new Proveedor();
        dominio.setCodigo(dto.getCodigo());
        dominio.setNombre(dto.getNombre());
        dominio.setTelefono(dto.getTelefono());
        dominio.setGmail(dto.getEmail()); // Mapeo de email -> gmail
        dominio.setTerminosPago(dto.getTerminosPago());
        
        // El Dominio usa un objeto Domicilio; envolvemos la dirección String del DTO
        if (dto.getDireccion() != null) {
            Domicilio dom = new Domicilio();
            // Asumiendo que Domicilio tiene un setDirección o setCalle, ajustamos a su propiedad.
            // Si Domicilio tiene una estructura específica, aquí se parsearía. 
            // Por ahora mapeamos el texto de forma directa de manera segura:
            dom.setCalle(dto.getDireccion()); 
            dominio.setDomicilio(dom);
        }

        return dominio;
    }

    /**
     * Transforma una entidad Proveedor de Dominio a un ProveedorDTO para la vista.
     */
    public ProveedorDTO proveedorADTO(Proveedor dominio) {
        if (dominio == null) return null;

        ProveedorDTO dto = new ProveedorDTO();
        dto.setCodigo(dominio.getCodigo());
        dto.setNombre(dominio.getNombre());
        dto.setTelefono(dominio.getTelefono());
        dto.setEmail(dominio.getGmail()); // Mapeo de gmail -> email
        dto.setTerminosPago(dominio.getTerminosPago());
        dto.setActivo(true); // Valor por defecto en consistencia de negocio

        if (dominio.getDomicilio() != null) {
            // Extraemos la dirección del objeto Domicilio al String plano del DTO
            dto.setDireccion(dominio.getDomicilio().getCalle());
        }

        return dto;
    }

    /**
     * Transforma un ProductoDTO de la presentación a una entidad Producto de Dominio.
     */
    public Producto productoADominio(ProductoDTO dto) {
        if (dto == null) return null;

        Producto dominio = new Producto();
        dominio.setCodigo(dto.getCodigo());
        dominio.setNombre(dto.getNombre());
        
        // Conversión de tipos: BigDecimal -> double
        if (dto.getPrecio() != null) {
            dominio.setPrecioVenta(dto.getPrecio().doubleValue());
        }
        
        // Si el DTO trae un proveedor con precio específico de costo lo asignamos, 
        // si no, igualamos el de venta para evitar valores en cero.
        if (dto.getProveedor() != null && dto.getProveedor().getPrecioProveedor() != null) {
            dominio.setPrecioCompra(dto.getProveedor().getPrecioProveedor().doubleValue());
        } else if (dto.getPrecio() != null) {
            dominio.setPrecioCompra(dto.getPrecio().doubleValue());
        }

        dominio.setUnidades(dto.getStockActual()); // stockActual -> unidades
        dominio.setStockMinimo(dto.getStockMinimo());
        dominio.setStockMaximo(dto.getStockMaximo());
        
        // Conversión en cascada del proveedor embebido
        dominio.setProveedor(proveedorADominio(dto.getProveedor()));

        return dominio;
    }

    /**
     * Transforma una entidad Producto de Dominio a un ProductoDTO para la vista.
     */
    public ProductoDTO productoADTO(Producto dominio) {
        if (dominio == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo(dominio.getCodigo());
        dto.setNombre(dominio.getNombre());
        
        // Conversión de tipos: double -> BigDecimal
        dto.setPrecio(BigDecimal.valueOf(dominio.getPrecioVenta()));
        dto.setStockActual(dominio.getUnidades()); // unidades -> stockActual
        dto.setStockMinimo(dominio.getStockMinimo());
        dto.setStockMaximo(dominio.getStockMaximo());
        
        // Conversión en cascada del proveedor
        if (dominio.getProveedor() != null) {
            ProveedorDTO provDTO = proveedorADTO(dominio.getProveedor());
            // Seteamos el precio de costo de vuelta al DTO del proveedor
            provDTO.setPrecioProveedor(BigDecimal.valueOf(dominio.getPrecioCompra()));
            dto.setProveedor(provDTO);
        }

        return dto;
    }

    /**
     * Convierte una lista de productos de Dominio a una lista de DTOs.
     */
    public List<ProductoDTO> listaProductosADTO(List<Producto> productosDominio) {
        List<ProductoDTO> listaDTO = new ArrayList<>();
        if (productosDominio == null) return listaDTO;
        for (Producto p : productosDominio) {
            listaDTO.add(productoADTO(p));
        }
        return listaDTO;
    }

    /**
     * Convierte una lista de proveedores de Dominio a una lista de DTOs.
     */
    public List<ProveedorDTO> listaProveedoresADTO(List<Proveedor> proveedoresDominio) {
        List<ProveedorDTO> listaDTO = new ArrayList<>();
        if (proveedoresDominio == null) return listaDTO;
        for (Proveedor p : proveedoresDominio) {
            listaDTO.add(proveedorADTO(p));
        }
        return listaDTO;
    }
}