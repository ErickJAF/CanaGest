package diseñadores.negocios.productos;

import diseñadores.negocios.dto.EscanearProductoDTO;
import diseñadores.negocios.dto.ProductoDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.List;

public interface IProductos {

    List<ProductoDTO> obtenerCatalogo() throws NegocioException;

    ProductoDTO buscarProductoPorCodigo(EscanearProductoDTO dto) throws NegocioException;

    boolean existeProducto(EscanearProductoDTO dto) throws NegocioException;

    boolean tieneStock(EscanearProductoDTO dto, int cantidad) throws NegocioException;

    void descontarStock(String codigo, int cantidad) throws NegocioException;

    void guardarProducto(ProductoDTO producto) throws NegocioException;

    void actualizarProducto(ProductoDTO producto) throws NegocioException;

    void eliminarProducto(String codigo) throws NegocioException;

}