package py.com.nikosoft.depositos.entities;

public class ProductoDetalle {

    private int idProductoDetalle;
    private Integer idProducto; // FK -> producto
    private Integer idEstante;  // FK -> estante, puede ser null si no está ubicado
    private int cantidad;

    public ProductoDetalle() {
    }

    public ProductoDetalle(int idProductoDetalle, Integer idProducto, Integer idEstante, int cantidad) {
        this.idProductoDetalle = idProductoDetalle;
        this.idProducto = idProducto;
        this.idEstante = idEstante;
        this.cantidad = cantidad;
    }

    public int getIdProductoDetalle() {
        return idProductoDetalle;
    }

    public void setIdProductoDetalle(int idProductoDetalle) {
        this.idProductoDetalle = idProductoDetalle;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdEstante() {
        return idEstante;
    }

    public void setIdEstante(Integer idEstante) {
        this.idEstante = idEstante;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
