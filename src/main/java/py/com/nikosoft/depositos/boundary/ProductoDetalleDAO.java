package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.ProductoDetalle;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductoDetalleDAO - Data Access Object para la tabla PRODUCTO_DETALLE
 * Representa el stock de un producto ubicado en un estante.
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class ProductoDetalleDAO implements DbDaoManager<ProductoDetalle, Integer> {

    private final ConnectionManager connectionManager;

    public ProductoDetalleDAO() {
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

        String createDepositoTable = "CREATE TABLE IF NOT EXISTS deposito (" +
                "id_deposito SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(255) NOT NULL, " +
                "direccion VARCHAR(255)" +
                ")";

        String createEstanteTable = "CREATE TABLE IF NOT EXISTS estante (" +
                "id_estante SERIAL PRIMARY KEY, " +
                "codigo VARCHAR(255) NOT NULL, " +
                "id_deposito INTEGER REFERENCES deposito(id_deposito) ON DELETE SET NULL" +
                ")";

        String createProductoDetalleTable = "CREATE TABLE IF NOT EXISTS producto_detalle (" +
                "id_producto_detalle SERIAL PRIMARY KEY, " +
                "id_producto INTEGER REFERENCES producto(id_producto) ON DELETE SET NULL, " +
                "id_estante INTEGER REFERENCES estante(id_estante) ON DELETE SET NULL, " +
                "cantidad INTEGER NOT NULL DEFAULT 0" +
                ")";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            // Asegurar existencia de las tablas referenciadas (en caso de que se ejecute independiente)
            stmt.execute(createMarcaTable);
            stmt.execute(createProductoTable);
            stmt.execute(createDepositoTable);
            stmt.execute(createEstanteTable);
            stmt.execute(createProductoDetalleTable);
            System.out.println("✓ Tabla 'producto_detalle' creada/verificada exitosamente");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tabla producto_detalle: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * AGREGAR (CREATE) - Inserta un nuevo detalle de producto (stock en un estante)
     * @param detalle objeto productoDetalle
     * @return el ID del detalle creado
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(ProductoDetalle detalle) throws SQLException {
        String insertSQL = "INSERT INTO producto_detalle (id_producto, id_estante, cantidad) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            if (detalle.getIdProducto() == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, detalle.getIdProducto());
            }
            if (detalle.getIdEstante() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, detalle.getIdEstante());
            }
            pstmt.setInt(3, detalle.getCantidad());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Detalle de producto agregado exitosamente con ID: " + id);
                    pstmt.close();
                    return id;
                }
            }
            pstmt.close();
            return -1;
        } catch (SQLException e) {
            System.err.println("✗ Error al agregar detalle de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ELIMINAR (DELETE) - Elimina un detalle de producto por ID
     * @param idProductoDetalle el ID del detalle a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idProductoDetalle) throws SQLException {
        String deleteSQL = "DELETE FROM producto_detalle WHERE id_producto_detalle = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, idProductoDetalle);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Detalle de producto eliminado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Detalle de producto no encontrado (ID: " + idProductoDetalle + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar detalle de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ACTUALIZAR (UPDATE) - Actualiza producto, estante y cantidad de un detalle
     * @param detalle el detalle con los datos actualizados (debe incluir idProductoDetalle)
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(ProductoDetalle detalle) throws SQLException {
        String updateSQL = "UPDATE producto_detalle SET id_producto = ?, id_estante = ?, cantidad = ? WHERE id_producto_detalle = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            if (detalle.getIdProducto() == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, detalle.getIdProducto());
            }
            if (detalle.getIdEstante() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, detalle.getIdEstante());
            }
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setInt(4, detalle.getIdProductoDetalle());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Detalle de producto actualizado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Detalle de producto no encontrado (ID: " + detalle.getIdProductoDetalle() + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar detalle de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene un detalle de producto por su ID
     * @param idProductoDetalle el ID del detalle a buscar
     * @return el detalle encontrado o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public ProductoDetalle getById(Integer idProductoDetalle) throws SQLException {
        String selectSQL = "SELECT id_producto_detalle, id_producto, id_estante, cantidad FROM producto_detalle WHERE id_producto_detalle = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idProductoDetalle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ProductoDetalle detalle = mapRow(rs);
                System.out.println("✓ Detalle de producto encontrado (ID: " + detalle.getIdProductoDetalle() + ")");
                pstmt.close();
                return detalle;
            }
            System.out.println("⚠ Detalle de producto no encontrado (ID: " + idProductoDetalle + ")");
            pstmt.close();
            return null;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar detalle de producto por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todos los detalles de producto
     * @return lista de todos los detalles de producto
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<ProductoDetalle> getAll() throws SQLException {
        List<ProductoDetalle> detalles = new ArrayList<>();
        String selectSQL = "SELECT id_producto_detalle, id_producto, id_estante, cantidad FROM producto_detalle ORDER BY id_producto_detalle";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                detalles.add(mapRow(rs));
            }
            stmt.close();
            System.out.println("✓ Se consultaron " + detalles.size() + " detalles de producto");
            return detalles;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar detalles de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR PRODUCTO - Obtiene todos los detalles (ubicaciones/stock) de un producto
     * @param idProducto el ID del producto
     * @return lista de detalles asociados al producto
     * @throws SQLException si hay error en la operación
     */
    public List<ProductoDetalle> getByProducto(int idProducto) throws SQLException {
        List<ProductoDetalle> detalles = new ArrayList<>();
        String selectSQL = "SELECT id_producto_detalle, id_producto, id_estante, cantidad FROM producto_detalle WHERE id_producto = ? ORDER BY id_producto_detalle";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                detalles.add(mapRow(rs));
            }
            pstmt.close();
            System.out.println("✓ Se encontraron " + detalles.size() + " detalles para el producto ID: " + idProducto);
            return detalles;
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar detalles por producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ESTANTE - Obtiene todos los detalles (stock) ubicados en un estante
     * @param idEstante el ID del estante
     * @return lista de detalles ubicados en el estante
     * @throws SQLException si hay error en la operación
     */
    public List<ProductoDetalle> getByEstante(int idEstante) throws SQLException {
        List<ProductoDetalle> detalles = new ArrayList<>();
        String selectSQL = "SELECT id_producto_detalle, id_producto, id_estante, cantidad FROM producto_detalle WHERE id_estante = ? ORDER BY id_producto_detalle";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idEstante);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                detalles.add(mapRow(rs));
            }
            pstmt.close();
            System.out.println("✓ Se encontraron " + detalles.size() + " detalles en el estante ID: " + idEstante);
            return detalles;
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar detalles por estante: " + e.getMessage());
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
        String deleteSQL = "DELETE FROM producto_detalle";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            int rows = stmt.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rows + " detalles de producto");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar detalles de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * OBTENER CANTIDAD DE DETALLES DE PRODUCTO
     * @return el número total de registros de detalle de producto
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int count() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM producto_detalle";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(countSQL);
            if (rs.next()) {
                int total = rs.getInt("total");
                stmt.close();
                System.out.println("✓ Total de detalles de producto: " + total);
                return total;
            }
            stmt.close();
            return 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al contar detalles de producto: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    private ProductoDetalle mapRow(ResultSet rs) throws SQLException {
        return new ProductoDetalle(
                rs.getInt("id_producto_detalle"),
                rs.getObject("id_producto") == null ? null : rs.getInt("id_producto"),
                rs.getObject("id_estante") == null ? null : rs.getInt("id_estante"),
                rs.getInt("cantidad")
        );
    }
}
