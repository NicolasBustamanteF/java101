package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Producto;
import py.com.nikosoft.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private ConnectionManager connectionManager;

    public ProductoDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    public void createTable() throws SQLException {
        String createMarcaTable = "CREATE TABLE IF NOT EXISTS marca (" +
                "id_marca SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(255) NOT NULL" +
                ")";

        String createProductoTable = "CREATE TABLE IF NOT EXISTS producto (" +
                "id_producto SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(255) NOT NULL, " +
                "precio NUMERIC(12,2) DEFAULT 0, " +
                "id_marca INTEGER REFERENCES marca(id_marca) ON DELETE SET NULL" +
                ")";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            // Asegurar existencia de marca (en caso de que se ejecute independiente)
            stmt.execute(createMarcaTable);
            stmt.execute(createProductoTable);
            System.out.println("✓ Tablas 'marca' y 'producto' creadas/verificadas exitosamente");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tablas producto/marca: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public int add(String nombre, double precio, Integer idMarca) throws SQLException {
        String insertSQL = "INSERT INTO producto (nombre, precio, id_marca) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            if (idMarca == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, idMarca);
            }
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Producto agregado con ID: " + id);
                    pstmt.close();
                    return id;
                }
            }
            pstmt.close();
            return -1;
        } catch (SQLException e) {
            System.err.println("✗ Error al agregar producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public boolean assignMarca(int idProducto, Integer idMarca) throws SQLException {
        String updateSQL = "UPDATE producto SET id_marca = ? WHERE id_producto = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            if (idMarca == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, idMarca);
            }
            pstmt.setInt(2, idProducto);
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al asignar marca al producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public boolean delete(int idProducto) throws SQLException {
        String deleteSQL = "DELETE FROM producto WHERE id_producto = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, idProducto);
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public boolean update(int idProducto, String nombre, double precio, Integer idMarca) throws SQLException {
        String updateSQL = "UPDATE producto SET nombre = ?, precio = ?, id_marca = ? WHERE id_producto = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            if (idMarca == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, idMarca);
            }
            pstmt.setInt(4, idProducto);
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public List<Producto> select() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String selectSQL = "SELECT id_producto, nombre, precio, id_marca FROM producto ORDER BY id_producto";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getObject("id_marca") == null ? null : rs.getInt("id_marca")
                );
                productos.add(p);
            }
            stmt.close();
            System.out.println("✓ Se consultaron " + productos.size() + " productos");
            return productos;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar productos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public Producto selectById(int idProducto) throws SQLException {
        String selectSQL = "SELECT id_producto, nombre, precio, id_marca FROM producto WHERE id_producto = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getObject("id_marca") == null ? null : rs.getInt("id_marca")
                );
                pstmt.close();
                return p;
            }
            pstmt.close();
            return null;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar producto por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public List<Producto> selectByNombre(String nombreBusqueda) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String selectSQL = "SELECT id_producto, nombre, precio, id_marca FROM producto WHERE nombre ILIKE ? ORDER BY id_producto";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setString(1, "%" + nombreBusqueda + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getObject("id_marca") == null ? null : rs.getInt("id_marca")
                ));
            }
            pstmt.close();
            System.out.println("✓ Se encontraron " + productos.size() + " productos con: '" + nombreBusqueda + "'");
            return productos;
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar productos por nombre: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public int getTotalProductos() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM producto";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(countSQL);
            if (rs.next()) {
                int total = rs.getInt("total");
                stmt.close();
                System.out.println("✓ Total de productos: " + total);
                return total;
            }
            stmt.close();
            return 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al contar productos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    public void deleteAll() throws SQLException {
        String deleteSQL = "DELETE FROM producto";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            int rows = stmt.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rows + " productos");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar productos: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }
}

