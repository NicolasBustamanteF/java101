package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Marca;
import py.com.nikosoft.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    private ConnectionManager connectionManager;

    public MarcaDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    public void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS marca (" +
                "id_marca SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(255) NOT NULL" +
                ")";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("✓ Tabla 'marca' creada/verificada exitosamente");
            statement.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tabla marca: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public int add(String nombre) throws SQLException {
        String insertSQL = "INSERT INTO marca (nombre) VALUES (?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    insertSQL,
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, nombre);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Marca agregada exitosamente con ID: " + id);
                    pstmt.close();
                    return id;
                }
            }
            pstmt.close();
            return -1;

        } catch (SQLException e) {
            System.err.println("✗ Error al agregar marca: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public boolean delete(int idMarca) throws SQLException {
        String deleteSQL = "DELETE FROM marca WHERE id_marca = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);

            pstmt.setInt(1, idMarca);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Marca eliminada exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Marca no encontrada (ID: " + idMarca + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar marca: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public boolean update(int idMarca, String nombre) throws SQLException {
        String updateSQL = "UPDATE marca SET nombre = ? WHERE id_marca = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);

            pstmt.setString(1, nombre);
            pstmt.setInt(2, idMarca);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Marca actualizada exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Marca no encontrada (ID: " + idMarca + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar marca: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public List<Marca> select() throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String selectSQL = "SELECT id_marca, nombre FROM marca ORDER BY id_marca";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_marca");
                String nombre = resultSet.getString("nombre");
                marcas.add(new Marca(id, nombre));
            }

            System.out.println("✓ Se consultaron " + marcas.size() + " marcas");
            resultSet.close();
            statement.close();
            return marcas;

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar marcas: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public Marca selectById(int idMarca) throws SQLException {
        String selectSQL = "SELECT id_marca, nombre FROM marca WHERE id_marca = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idMarca);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Marca marca = new Marca(rs.getInt("id_marca"), rs.getString("nombre"));
                System.out.println("✓ Marca encontrada: " + marca.getNombre());
                pstmt.close();
                return marca;
            } else {
                System.out.println("⚠ Marca no encontrada (ID: " + idMarca + ")");
                pstmt.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar marca por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public List<Marca> selectByNombre(String nombreBusqueda) throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String selectSQL = "SELECT id_marca, nombre FROM marca WHERE nombre ILIKE ? ORDER BY id_marca";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setString(1, "%" + nombreBusqueda + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                marcas.add(new Marca(rs.getInt("id_marca"), rs.getString("nombre")));
            }

            System.out.println("✓ Se encontraron " + marcas.size() + " marcas con: '" + nombreBusqueda + "'");
            pstmt.close();
            return marcas;

        } catch (SQLException e) {
            System.err.println("✗ Error al buscar marcas por nombre: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public int getTotalMarcas() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM marca";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(countSQL);
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✓ Total de marcas: " + total);
                statement.close();
                return total;
            }
            statement.close();
            return 0;

        } catch (SQLException e) {
            System.err.println("✗ Error al contar marcas: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public void deleteAll() throws SQLException {
        String deleteSQL = "DELETE FROM marca";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rows + " marcas");
            statement.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar todas las marcas: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }
}

