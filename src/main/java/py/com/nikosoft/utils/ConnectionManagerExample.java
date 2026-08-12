package py.com.nikosoft.utils;

import py.com.nikosoft.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Ejemplo de uso del ConnectionManager
 * Demuestra cómo conectarse a la base de datos db_depositos
 */
public class ConnectionManagerExample {

    public static void main(String[] args) {
        // Obtener la instancia del ConnectionManager (Singleton)
        ConnectionManager connectionManager = ConnectionManager.getInstance();

        // Mostrar información de la conexión
        System.out.println("=== Información de Conexión ===");
        System.out.println(connectionManager.getConnectionInfo());
        System.out.println();

        // Obtener una conexión
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();

            // Ejemplo: Crear una tabla de prueba
            createSampleTable(connection);

            // Ejemplo: Insertar datos
            insertSampleData(connection);

            // Ejemplo: Consultar datos
            querySampleData(connection);

        } catch (SQLException e) {
            System.err.println("Error en la operación: " + e.getMessage());
        } finally {
            // Cerrar la conexión
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * Crea una tabla de ejemplo
     */
    private static void createSampleTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS test_tabla (" +
                                "id SERIAL PRIMARY KEY, " +
                                "nombre VARCHAR(100) NOT NULL, " +
                                "correo VARCHAR(100)" +
                                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("✓ Tabla 'test_tabla' creada/verificada exitosamente");
        }
    }

    /**
     * Inserta datos de ejemplo
     */
    private static void insertSampleData(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO test_tabla (nombre, correo) VALUES " +
                           "('Juan Pérez', 'juan@example.com'), " +
                           "('María García', 'maria@example.com')";

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(insertSQL);
            System.out.println("✓ Se insertaron " + rowsAffected + " registros");
        } catch (SQLException e) {
            // Si ya existen los registros, ignorar el error
            System.out.println("Nota: Registro ya existe o error al insertar");
        }
    }

    /**
     * Consulta datos de ejemplo
     */
    private static void querySampleData(Connection connection) throws SQLException {
        String querySQL = "SELECT id, nombre, correo FROM test_tabla";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(querySQL)) {

            System.out.println("\n=== Datos en test_tabla ===");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                                 " | Nombre: " + resultSet.getString("nombre") +
                                 " | Correo: " + resultSet.getString("correo"));
            }
        }
    }
}

