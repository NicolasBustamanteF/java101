package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.MarcaDAO;
import py.com.nikosoft.depositos.boundary.ProductoDAO;
import py.com.nikosoft.depositos.entities.Marca;
import py.com.nikosoft.depositos.entities.Producto;

import java.sql.SQLException;
import java.util.List;

public class MarcaProductoCRUDExample {

    public static void main(String[] args) {
        MarcaDAO marcaDAO = new MarcaDAO();
        ProductoDAO productoDAO = new ProductoDAO();

        try {
            System.out.println("═════════════════════════════════════════════════════════");
            System.out.println("       EJEMPLO DE MARCAS Y PRODUCTOS");
            System.out.println("═════════════════════════════════════════════════════════\n");

            // Crear tablas
            marcaDAO.createTable();
            productoDAO.createTable();

            // Limpiar datos previos
            marcaDAO.deleteAll();
            productoDAO.deleteAll();

            // Crear marcas
            int m1 = marcaDAO.add(new Marca(0, "Acme"));
            int m2 = marcaDAO.add(new Marca(0, "Globex"));

            // Crear productos
            int p1 = productoDAO.add(new Producto(0, "Cámara", 299.99, m1));
            int p2 = productoDAO.add(new Producto(0, "Camiseta", 19.90, null));
            int p3 = productoDAO.add(new Producto(0, "Auriculares", 49.50, m2));

            // Listar productos
            List<Producto> productos = productoDAO.getAll();
            System.out.println("Productos creados:");
            for (Producto p : productos) {
                System.out.println("  [" + p.getIdProducto() + "] " + p.getNombre() + " - Precio: " + p.getPrecio() + " - ID Marca: " + (p.getIdMarca() == null ? "(sin asignar)" : p.getIdMarca()));
            }

            // Asignar marca a producto existente
            System.out.println("\nAsignando marca al producto Camiseta...");
            productoDAO.assignMarca(p2, m2);

            Producto camis = productoDAO.getById(p2);
            System.out.println("Producto actualizado: [" + camis.getIdProducto() + "] " + camis.getNombre() + " - ID Marca: " + camis.getIdMarca());

            System.out.println("\n═════════════════════════════════════════════════════════");
            System.out.println("         ✓ EJEMPLO COMPLETADO EXITOSAMENTE");
            System.out.println("═════════════════════════════════════════════════════════");

        } catch (SQLException e) {
            System.err.println("✗ Error en la operación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

