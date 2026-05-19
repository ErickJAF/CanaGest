package diseñadores.negocios.dto;

public class ItemConteoDTO {

    private String codigo;
    private String comentario;
    private int diferencia;
    private int cantidadContada;
    private boolean estado;

    private UsuarioEmbedded usuario;
    private ProductoEmbedded producto;

    public ItemConteoDTO() {
        this.usuario = new UsuarioEmbedded();
        this.producto = new ProductoEmbedded();
    }

    public ItemConteoDTO(String codigoConteo, String codigoUsuario, String nombreUsuario, String rolUsuario, 
                         String productoCodigo, String productoNombre, int productoStockSistema, int productoStockFisico) {
        this.codigo = codigoConteo;
        this.usuario = new UsuarioEmbedded(codigoUsuario, nombreUsuario, rolUsuario);
        this.producto = new ProductoEmbedded(productoCodigo, productoNombre, productoStockSistema);
        this.cantidadContada = productoStockFisico;
        this.diferencia = productoStockFisico - productoStockSistema;
        this.estado = (productoStockSistema == productoStockFisico);
    }

    public int getDiferencia() { 
        return this.diferencia; 
    }
    
    public String getEstado() { 
        return this.estado ? "Verificado" : "Pendiente"; 
    }

    public String getProductoCodigo() {
        return producto != null ? producto.getIdProducto() : null;
    }
    
    public void setProductoCodigo(String productoCodigo) {
        if (this.producto == null) this.producto = new ProductoEmbedded();
        this.producto.setIdProducto(productoCodigo);
    }

    public String getProductoNombre() {
        return producto != null ? producto.getNombre() : "";
    }
    
    public void setProductoNombre(String productoNombre) {
        if (this.producto == null) this.producto = new ProductoEmbedded();
        this.producto.setNombre(productoNombre);
    }

    public int getProductoStockSistema() {
        return producto != null ? producto.getStockSistema() : 0;
    }
    
    public void setProductoStockSistema(int stockSistema) {
        if (this.producto == null) this.producto = new ProductoEmbedded();
        this.producto.setStockSistema(stockSistema);
        actualizarCalculos();
    }

    public int getProductoStockFisico() {
        return this.cantidadContada;
    }
    
    public void setProductoStockFisico(int stockFisico) {
        this.cantidadContada = stockFisico;
        actualizarCalculos();
    }

    private void actualizarCalculos() {
        this.diferencia = this.cantidadContada - getProductoStockSistema();
        this.estado = (getProductoStockSistema() == this.cantidadContada);
    }

    public boolean isVerificado() { 
        return estado; 
    }
    
    public void setVerificado(boolean verificado) { 
        this.estado = verificado; 
    }

    public String getCodigoUsuario() { 
        return usuario != null ? usuario.getIdUsuario() : null; 
    }
    
    public void setCodigoUsuario(String codigoUsuario) {
        if (this.usuario == null) this.usuario = new UsuarioEmbedded();
        this.usuario.setIdUsuario(codigoUsuario);
    }

    public String getNombreUsuario() { 
        return usuario != null ? usuario.getNombre() : null; 
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        if (this.usuario == null) this.usuario = new UsuarioEmbedded();
        this.usuario.setNombre(nombreUsuario);
    }

    public String getRolUsuario() { 
        return usuario != null ? usuario.getRol() : null; 
    }
    
    public void setRolUsuario(String rolUsuario) {
        if (this.usuario == null) this.usuario = new UsuarioEmbedded();
        this.usuario.setRol(rolUsuario);
    }

    public String getCodigoConteo() { 
        return codigo; 
    }
    
    public void setCodigoConteo(String codigoConteo) { 
        this.codigo = codigoConteo; 
    }
    
    public String getComentario() { 
        return comentario; 
    }
    
    public void setComentario(String comentario) { 
        this.comentario = comentario; 
    }

    public static class ProductoEmbedded {
        private String idProducto;
        private String nombre;
        private int stockSistema;

        public ProductoEmbedded() {}
        
        public ProductoEmbedded(String idProducto, String nombre, int stockSistema) {
            this.idProducto = idProducto;
            this.nombre = nombre;
            this.stockSistema = stockSistema;
        }
        
        public String getIdProducto() { return idProducto; }
        public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public int getStockSistema() { return stockSistema; }
        public void setStockSistema(int stockSistema) { this.stockSistema = stockSistema; }
    }

    public static class UsuarioEmbedded {
        private String idUsuario;
        private String nombre;
        private String rol;

        public UsuarioEmbedded() {}
        
        public UsuarioEmbedded(String idUsuario, String nombre, String rol) {
            this.idUsuario = idUsuario;
            this.nombre = nombre;
            this.rol = rol;
        }
        
        public String getIdUsuario() { return idUsuario; }
        public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}