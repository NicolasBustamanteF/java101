package py.com.nikosoft.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionManager: Gestiona la conexión con la base de datos PostgreSQL
 * Utiliza el patrón Singleton para garantizar una única instancia
 *
 * Configuración de la BD:
 * - Base de datos: db_depositos
 * - Usuario: develop
 * - Contraseña: develop123
 * - Host: localhost
 * - Puerto: 5432
 */
public class ConnectionManager {

    private static ConnectionManager instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/db_depositos";
    private static final String USER = "develop";
    private static final String PASSWORD = "develop123";
    private static final String DRIVER = "org.postgresql.Driver";

    // Bloque estático para cargar el driver
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el driver de PostgreSQL");
            e.printStackTrace();
        }
    }

    /**
     * Constructor privado para el patrón Singleton
     */
    private ConnectionManager() {
    }

    /**
     * Obtiene la instancia única de ConnectionManager
     * @return instancia única de ConnectionManager
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Obtiene una conexión con la base de datos
     * @return objeto Connection configurado
     * @throws SQLException si hay error en la conexión
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("✓ Conexión exitosa a la base de datos db_depositos");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cierra una conexión con la base de datos
     * @param connection la conexión a cerrar
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("✗ Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si la conexión está activa
     * @param connection la conexión a verificar
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConnectionActive(Connection connection) {
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene información de la conexión (para debugging)
     * @return String con la información de la conexión
     */
    public String getConnectionInfo() {
        return "URL: " + URL + "\n" +
               "Usuario: " + USER + "\n" +
               "Base de datos: db_depositos";
    }
}
