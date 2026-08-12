package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Categoria;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoriaDAO - Data Access Object para la tabla CATEGORIA
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class CategoriaDAO implements DbDaoManager<Categoria, Integer> {

    private final ConnectionManager connectionManager;

    /**
     * Constructor del DAO
     */
    public CategoriaDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    /**
     * Crea la tabla CATEGORIA si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS categoria (" +
                "id_categoria SERIAL PRIMARY KEY, " +
                "descripcion VARCHAR(255) NOT NULL" +
                ")";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("✓ Tabla 'categoria' creada/verificada exitosamente");
            statement.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tabla: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * AGREGAR (CREATE) - Inserta una nueva categoría
     * @param categoria objeto categoria
     * @return el ID de la categoría creada
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(Categoria categoria) throws SQLException {
        String insertSQL = "INSERT INTO categoria (categoria) VALUES (?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(
                    insertSQL,
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, categoria.getDescripcion());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int idCategoria = keys.getInt(1);
                    System.out.println("✓ Categoría agregada exitosamente con ID: " + idCategoria);
                    pstmt.close();
                    return idCategoria;
                }
            }
            pstmt.close();
            return -1;

        } catch (SQLException e) {
            System.err.println("✗ Error al agregar categoría: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ELIMINAR (DELETE) - Elimina una categoría por ID
     * @param idCategoria el ID de la categoría a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idCategoria) throws SQLException {
        String deleteSQL = "DELETE FROM categoria WHERE id_categoria = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);

            pstmt.setInt(1, idCategoria);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Categoría eliminada exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Categoría no encontrada (ID: " + idCategoria + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar categoría: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ACTUALIZAR (UPDATE) - Actualiza la descripción de una categoría
     * @param categoria el ID de la categoría a actualizar
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(Categoria categoria) throws SQLException {
        String updateSQL = "UPDATE categoria SET descripcion = ? WHERE id_categoria = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);

            pstmt.setString(1, categoria.getDescripcion());
            pstmt.setInt(2, categoria.getIdCategoria());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Categoría actualizada exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Categoría no encontrada (ID: " + categoria.getIdCategoria() + ")");
                pstmt.close();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar categoría: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todas las categorías
     * @return lista de todas las categorías
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<Categoria> getAll() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String selectSQL = "SELECT id_categoria, descripcion FROM categoria ORDER BY id_categoria";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            while (resultSet.next()) {
                int idCategoria = resultSet.getInt("id_categoria");
                String descripcion = resultSet.getString("descripcion");

                Categoria categoria = new Categoria(idCategoria, descripcion);
                categorias.add(categoria);
            }

            System.out.println("✓ Se consultaron " + categorias.size() + " categorías");
            resultSet.close();
            statement.close();
            return categorias;

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar categorías: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene una categoría por su ID
     * @param idCategoria el ID de la categoría a buscar
     * @return la categoría encontrada o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public Categoria getById(Integer idCategoria) throws SQLException {
        String selectSQL = "SELECT id_categoria, descripcion FROM categoria WHERE id_categoria = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);

            pstmt.setInt(1, idCategoria);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id_categoria");
                String descripcion = resultSet.getString("descripcion");

                Categoria categoria = new Categoria(id, descripcion);
                System.out.println("✓ Categoría encontrada: " + descripcion);
                pstmt.close();
                return categoria;
            } else {
                System.out.println("⚠ Categoría no encontrada (ID: " + idCategoria + ")");
                pstmt.close();
                return null;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error al consultar categoría por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }



    /**
     * SELECCIONAR POR DESCRIPCIÓN (READ BY DESCRIPTION - LIKE)
     * Busca categorías cuya descripción contenga el texto especificado
     * @param descripcion el texto a buscar (puede ser parcial)
     * @return lista de categorías que coinciden con la búsqueda
     * @throws SQLException si hay error en la operación
     */
    public List<Categoria> getByDescripcion(String descripcion) throws SQLException {
            List<Categoria> categorias = new ArrayList<>();
            String selectSQL = "SELECT id_categoria, descripcion FROM categoria " +
                    "WHERE descripcion ILIKE ? " +
                    "ORDER BY id_categoria";

            Connection connection = null;
            try {
                connection = connectionManager.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(selectSQL);

                // Agregar % para búsqueda LIKE (funciona en PostgreSQL)
                pstmt.setString(1, "%" + descripcion + "%");
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    int idCategoria = resultSet.getInt("id_categoria");
                    String desc = resultSet.getString("descripcion");

                    Categoria categoria = new Categoria(idCategoria, desc);
                    categorias.add(categoria);
                }

                System.out.println("✓ Se encontraron " + categorias.size() + " categorías con: \"" + descripcion + "\"");
                pstmt.close();
                return categorias;

            } catch (SQLException e) {
                System.err.println("✗ Error al consultar categorías por descripción: " + e.getMessage());
                throw e;
            } finally {
                if (connection != null) {
                    connectionManager.closeConnection(connection);
                }
            }
    }

    /**
     * OBTENER CANTIDAD DE CATEGORÍAS
     * @return el número total de categorías
     * @throws SQLException si hay error en la operación
     */
    public int getTotalCategorias() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM categoria";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countSQL);

            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                System.out.println("✓ Total de categorías: " + total);
                statement.close();
                return total;
            }
            statement.close();
            return 0;

        } catch (SQLException e) {
            System.err.println("✗ Error al contar categorías: " + e.getMessage());
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
    public void deleteAll() throws SQLException {
        String deleteSQL = "DELETE FROM categoria";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rowsAffected + " categorías");
            statement.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar categorías: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }
}

