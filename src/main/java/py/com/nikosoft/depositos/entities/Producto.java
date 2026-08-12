package py.com.nikosoft.depositos.entities;

public class Producto {

    private int idProducto;
    private String nombre;
    private double precio;
    private Integer idMarca; // puede ser null si no está asignado

    public Producto() {
    }

    public Producto(int idProducto, String nombre, double precio, Integer idMarca) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.idMarca = idMarca;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }
}

