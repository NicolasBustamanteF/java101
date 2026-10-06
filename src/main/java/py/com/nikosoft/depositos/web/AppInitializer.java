package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import py.com.nikosoft.depositos.boundary.CategoriaDAO;
import py.com.nikosoft.depositos.boundary.ProductoDetalleDAO;

import java.sql.SQLException;

/**
 * Se ejecuta al iniciar la aplicación web: crea las tablas si no existen
 * (lo que antes hacía cada menú de consola en su método ejecutar()).
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // ProductoDetalleDAO crea marca, producto, deposito, estante y producto_detalle
            new ProductoDetalleDAO().createTable();
            new CategoriaDAO().createTable();
            System.out.println("✓ Aplicación web de depósitos inicializada");
        } catch (SQLException e) {
            System.err.println("✗ No se pudieron crear las tablas: " + e.getMessage());
        }
    }
}
