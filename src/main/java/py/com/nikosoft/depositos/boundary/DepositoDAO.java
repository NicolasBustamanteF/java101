package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Deposito;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DepositoDAO - Data Access Object para la tabla DEPOSITO
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class DepositoDAO implements DbDaoManager<Deposito, Integer> {

    private final ConnectionManager connectionManager;

    public DepositoDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS deposito (" +
                "id_deposito SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(255) NOT NULL, " +
                "direccion VARCHAR(255)" +
                ")";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("✓ Tabla 'deposito' creada/verificada exitosamente");
            statement.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tabla deposito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * AGREGAR (CREATE) - Inserta un nuevo depósito
     * @param deposito objeto deposito
     * @return el ID del depósito creado
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(Deposito deposito) throws SQLException {
        String insertSQL = "INSERT INTO deposito (nombre, direccion) VALUES (?, ?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    insertSQL,
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, deposito.getNombre());
            pstmt.setString(2, deposito.getDireccion());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Depósito agregado exitosamente con ID: " + id);
                    pstmt.close();
                    return id;
                }
            }
            pstmt.close();
            return -1;

        } catch (SQLException e) {
            System.err.println("✗ Error al agregar depósito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ELIMINAR (DELETE) - Elimina un depósito por ID
     * @param idDeposito el ID del depósito a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idDeposito) throws SQLException {
        String deleteSQL = "DELETE FROM deposito WHERE id_deposito = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);

            pstmt.setInt(1, idDeposito);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Depósito eliminado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Depósito no encontrado (ID: " + idDeposito + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar depósito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ACTUALIZAR (UPDATE) - Actualiza nombre y dirección de un depósito
     * @param deposito el depósito con los datos actualizados (debe incluir idDeposito)
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(Deposito deposito) throws SQLException {
        String updateSQL = "UPDATE deposito SET nombre = ?, direccion = ? WHERE id_deposito = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);

            pstmt.setString(1, deposito.getNombre());
            pstmt.setString(2, deposito.getDireccion());
            pstmt.setInt(3, deposito.getIdDeposito());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Depósito actualizado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Depósito no encontrado (ID: " + deposito.getIdDeposito() + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar depósito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene un depósito por su ID
     * @param idDeposito el ID del depósito a buscar
     * @return el depósito encontrado o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public Deposito getById(Integer idDeposito) throws SQLException {
        String selectSQL = "SELECT id_deposito, nombre, direccion FROM deposito WHERE id_deposito = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idDeposito);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Deposito deposito = new Deposito(
                        rs.getInt("id_deposito"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );
                System.out.println("✓ Depósito encontrado: " + deposito.getNombre());
                pstmt.close();
                return deposito;
            } else {
                System.out.println("⚠ Depósito no encontrado (ID: " + idDeposito + ")");
                pstmt.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar depósito por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todos los depósitos
     * @return lista de todos los depósitos
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<Deposito> getAll() throws SQLException {
        List<Deposito> depositos = new ArrayList<>();
        String selectSQL = "SELECT id_deposito, nombre, direccion FROM deposito ORDER BY id_deposito";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            while (resultSet.next()) {
                Deposito deposito = new Deposito(
                        resultSet.getInt("id_deposito"),
                        resultSet.getString("nombre"),
                        resultSet.getString("direccion")
                );
                depositos.add(deposito);
            }

            System.out.println("✓ Se consultaron " + depositos.size() + " depósitos");
            resultSet.close();
            statement.close();
            return depositos;

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar depósitos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR NOMBRE (READ BY NAME - LIKE)
     * Busca depósitos cuyo nombre contenga el texto especificado
     * @param nombreBusqueda el texto a buscar (puede ser parcial)
     * @return lista de depósitos que coinciden con la búsqueda
     * @throws SQLException si hay error en la operación
     */
    public List<Deposito> getByNombre(String nombreBusqueda) throws SQLException {
        List<Deposito> depositos = new ArrayList<>();
        String selectSQL = "SELECT id_deposito, nombre, direccion FROM deposito WHERE nombre ILIKE ? ORDER BY id_deposito";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setString(1, "%" + nombreBusqueda + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                depositos.add(new Deposito(
                        rs.getInt("id_deposito"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                ));
            }

            System.out.println("✓ Se encontraron " + depositos.size() + " depósitos con: '" + nombreBusqueda + "'");
            pstmt.close();
            return depositos;

        } catch (SQLException e) {
            System.err.println("✗ Error al buscar depósitos por nombre: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * OBTENER CANTIDAD DE DEPÓSITOS
     * @return el número total de depósitos
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int count() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM deposito";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(countSQL);
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✓ Total de depósitos: " + total);
                statement.close();
                return total;
            }
            statement.close();
            return 0;

        } catch (SQLException e) {
            System.err.println("✗ Error al contar depósitos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ELIMINAR TODOS LOS DATOS (solo para pruebas)
     * @throws SQLException si hay error en la operación
     */
    @Override
    public void deleteAll() throws SQLException {
        String deleteSQL = "DELETE FROM deposito";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rows + " depósitos");
            statement.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar depósitos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }
}
