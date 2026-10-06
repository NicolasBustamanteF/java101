package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Producto;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductoDAO - Data Access Object para la tabla PRODUCTO
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class ProductoDAO implements DbDaoManager<Producto, Integer> {

    private final ConnectionManager connectionManager;

    public ProductoDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
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

    /**
     * AGREGAR (CREATE) - Inserta un nuevo producto
     * @param producto objeto producto
     * @return el ID del producto creado
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(Producto producto) throws SQLException {
        String insertSQL = "INSERT INTO producto (nombre, precio, id_marca) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            if (producto.getIdMarca() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, producto.getIdMarca());
            }
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Producto agregado exitosamente con ID: " + id);
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

    /**
     * ELIMINAR (DELETE) - Elimina un producto por ID
     * @param idProducto el ID del producto a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idProducto) throws SQLException {
        String deleteSQL = "DELETE FROM producto WHERE id_producto = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, idProducto);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Producto eliminado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Producto no encontrado (ID: " + idProducto + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ACTUALIZAR (UPDATE) - Actualiza nombre, precio y marca de un producto
     * @param producto el producto con los datos actualizados (debe incluir idProducto)
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(Producto producto) throws SQLException {
        String updateSQL = "UPDATE producto SET nombre = ?, precio = ?, id_marca = ? WHERE id_producto = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecio());
            if (producto.getIdMarca() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, producto.getIdMarca());
            }
            pstmt.setInt(4, producto.getIdProducto());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Producto actualizado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Producto no encontrado (ID: " + producto.getIdProducto() + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene un producto por su ID
     * @param idProducto el ID del producto a buscar
     * @return el producto encontrado o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public Producto getById(Integer idProducto) throws SQLException {
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
                System.out.println("✓ Producto encontrado: " + p.getNombre());
                pstmt.close();
                return p;
            }
            System.out.println("⚠ Producto no encontrado (ID: " + idProducto + ")");
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

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todos los productos
     * @return lista de todos los productos
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<Producto> getAll() throws SQLException {
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

    /**
     * ASIGNAR/REMOVER MARCA - Actualiza únicamente la marca de un producto
     * @param idProducto el ID del producto
     * @param idMarca el ID de la marca a asignar, o null para remover
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
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

    /**
     * SELECCIONAR POR NOMBRE (READ BY NAME - LIKE)
     * Busca productos cuyo nombre contenga el texto especificado
     * @param nombreBusqueda el texto a buscar (puede ser parcial)
     * @return lista de productos que coinciden con la búsqueda
     * @throws SQLException si hay error en la operación
     */
    public List<Producto> getByNombre(String nombreBusqueda) throws SQLException {
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

    /**
     * OBTENER CANTIDAD DE PRODUCTOS
     * @return el número total de productos
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int count() throws SQLException {
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

    /**
     * ELIMINAR TODOS LOS DATOS (solo para pruebas)
     * @throws SQLException si hay error en la operación
     */
    @Override
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
