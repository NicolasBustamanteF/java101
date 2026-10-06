package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Marca;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MarcaDAO - Data Access Object para la tabla MARCA
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class MarcaDAO implements DbDaoManager<Marca, Integer> {

    private final ConnectionManager connectionManager;

    public MarcaDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
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

    /**
     * AGREGAR (CREATE) - Inserta una nueva marca
     * @param marca objeto marca
     * @return el ID de la marca creada
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(Marca marca) throws SQLException {
        String insertSQL = "INSERT INTO marca (nombre) VALUES (?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    insertSQL,
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, marca.getNombre());
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

    /**
     * ELIMINAR (DELETE) - Elimina una marca por ID
     * @param idMarca el ID de la marca a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idMarca) throws SQLException {
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

    /**
     * ACTUALIZAR (UPDATE) - Actualiza el nombre de una marca
     * @param marca la marca con los datos actualizados (debe incluir idMarca)
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(Marca marca) throws SQLException {
        String updateSQL = "UPDATE marca SET nombre = ? WHERE id_marca = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);

            pstmt.setString(1, marca.getNombre());
            pstmt.setInt(2, marca.getIdMarca());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Marca actualizada exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Marca no encontrada (ID: " + marca.getIdMarca() + ")");
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

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene una marca por su ID
     * @param idMarca el ID de la marca a buscar
     * @return la marca encontrada o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public Marca getById(Integer idMarca) throws SQLException {
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

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todas las marcas
     * @return lista de todas las marcas
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<Marca> getAll() throws SQLException {
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

    /**
     * SELECCIONAR POR NOMBRE (READ BY NAME - LIKE)
     * Busca marcas cuyo nombre contenga el texto especificado
     * @param nombreBusqueda el texto a buscar (puede ser parcial)
     * @return lista de marcas que coinciden con la búsqueda
     * @throws SQLException si hay error en la operación
     */
    public List<Marca> getByNombre(String nombreBusqueda) throws SQLException {
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

    /**
     * OBTENER CANTIDAD DE MARCAS
     * @return el número total de marcas
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int count() throws SQLException {
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

    /**
     * ELIMINAR TODOS LOS DATOS (solo para pruebas)
     * @throws SQLException si hay error en la operación
     */
    @Override
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
