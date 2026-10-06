package py.com.nikosoft.depositos.boundary;

import py.com.nikosoft.depositos.entities.Estante;
import py.com.nikosoft.utils.ConnectionManager;
import py.com.nikosoft.utils.DbDaoManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EstanteDAO - Data Access Object para la tabla ESTANTE
 * Implementa operaciones CRUD (Create, Read, Update, Delete)
 * Utiliza ConnectionManager para gestionar conexiones
 */
public class EstanteDAO implements DbDaoManager<Estante, Integer> {

    private final ConnectionManager connectionManager;

    public EstanteDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public void createTable() throws SQLException {
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

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            // Asegurar existencia de deposito (en caso de que se ejecute independiente)
            stmt.execute(createDepositoTable);
            stmt.execute(createEstanteTable);
            System.out.println("✓ Tablas 'deposito' y 'estante' creadas/verificadas exitosamente");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al crear tablas estante/deposito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * AGREGAR (CREATE) - Inserta un nuevo estante
     * @param estante objeto estante
     * @return el ID del estante creado
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int add(Estante estante) throws SQLException {
        String insertSQL = "INSERT INTO estante (codigo, id_deposito) VALUES (?, ?)";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, estante.getCodigo());
            if (estante.getIdDeposito() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, estante.getIdDeposito());
            }
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("✓ Estante agregado exitosamente con ID: " + id);
                    pstmt.close();
                    return id;
                }
            }
            pstmt.close();
            return -1;
        } catch (SQLException e) {
            System.err.println("✗ Error al agregar estante: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ELIMINAR (DELETE) - Elimina un estante por ID
     * @param idEstante el ID del estante a eliminar
     * @return true si se eliminó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean delete(Integer idEstante) throws SQLException {
        String deleteSQL = "DELETE FROM estante WHERE id_estante = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, idEstante);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Estante eliminado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Estante no encontrado (ID: " + idEstante + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar estante: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ACTUALIZAR (UPDATE) - Actualiza código y depósito de un estante
     * @param estante el estante con los datos actualizados (debe incluir idEstante)
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    @Override
    public boolean update(Estante estante) throws SQLException {
        String updateSQL = "UPDATE estante SET codigo = ?, id_deposito = ? WHERE id_estante = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            pstmt.setString(1, estante.getCodigo());
            if (estante.getIdDeposito() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, estante.getIdDeposito());
            }
            pstmt.setInt(3, estante.getIdEstante());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Estante actualizado exitosamente");
                pstmt.close();
                return true;
            } else {
                System.out.println("⚠ Estante no encontrado (ID: " + estante.getIdEstante() + ")");
                pstmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al actualizar estante: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR ID (READ BY ID) - Obtiene un estante por su ID
     * @param idEstante el ID del estante a buscar
     * @return el estante encontrado o null si no existe
     * @throws SQLException si hay error en la operación
     */
    @Override
    public Estante getById(Integer idEstante) throws SQLException {
        String selectSQL = "SELECT id_estante, codigo, id_deposito FROM estante WHERE id_estante = ?";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idEstante);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Estante e = new Estante(
                        rs.getInt("id_estante"),
                        rs.getString("codigo"),
                        rs.getObject("id_deposito") == null ? null : rs.getInt("id_deposito")
                );
                System.out.println("✓ Estante encontrado: " + e.getCodigo());
                pstmt.close();
                return e;
            }
            System.out.println("⚠ Estante no encontrado (ID: " + idEstante + ")");
            pstmt.close();
            return null;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar estante por ID: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR TODOS (READ ALL) - Obtiene todos los estantes
     * @return lista de todos los estantes
     * @throws SQLException si hay error en la operación
     */
    @Override
    public List<Estante> getAll() throws SQLException {
        List<Estante> estantes = new ArrayList<>();
        String selectSQL = "SELECT id_estante, codigo, id_deposito FROM estante ORDER BY id_estante";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                Estante e = new Estante(
                        rs.getInt("id_estante"),
                        rs.getString("codigo"),
                        rs.getObject("id_deposito") == null ? null : rs.getInt("id_deposito")
                );
                estantes.add(e);
            }
            stmt.close();
            System.out.println("✓ Se consultaron " + estantes.size() + " estantes");
            return estantes;
        } catch (SQLException e) {
            System.err.println("✗ Error al consultar estantes: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * ASIGNAR/REMOVER DEPÓSITO - Actualiza únicamente el depósito de un estante
     * @param idEstante el ID del estante
     * @param idDeposito el ID del depósito a asignar, o null para remover
     * @return true si se actualizó, false si no se encontró
     * @throws SQLException si hay error en la operación
     */
    public boolean assignDeposito(int idEstante, Integer idDeposito) throws SQLException {
        String updateSQL = "UPDATE estante SET id_deposito = ? WHERE id_estante = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            if (idDeposito == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, idDeposito);
            }
            pstmt.setInt(2, idEstante);
            int rows = pstmt.executeUpdate();
            pstmt.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al asignar depósito al estante: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR CÓDIGO (READ BY CODE - LIKE)
     * Busca estantes cuyo código contenga el texto especificado
     * @param codigoBusqueda el texto a buscar (puede ser parcial)
     * @return lista de estantes que coinciden con la búsqueda
     * @throws SQLException si hay error en la operación
     */
    public List<Estante> getByCodigo(String codigoBusqueda) throws SQLException {
        List<Estante> estantes = new ArrayList<>();
        String selectSQL = "SELECT id_estante, codigo, id_deposito FROM estante WHERE codigo ILIKE ? ORDER BY id_estante";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setString(1, "%" + codigoBusqueda + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                estantes.add(new Estante(
                        rs.getInt("id_estante"),
                        rs.getString("codigo"),
                        rs.getObject("id_deposito") == null ? null : rs.getInt("id_deposito")
                ));
            }
            pstmt.close();
            System.out.println("✓ Se encontraron " + estantes.size() + " estantes con: '" + codigoBusqueda + "'");
            return estantes;
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar estantes por código: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * SELECCIONAR POR DEPÓSITO - Obtiene todos los estantes de un depósito
     * @param idDeposito el ID del depósito
     * @return lista de estantes que pertenecen al depósito
     * @throws SQLException si hay error en la operación
     */
    public List<Estante> getByDeposito(int idDeposito) throws SQLException {
        List<Estante> estantes = new ArrayList<>();
        String selectSQL = "SELECT id_estante, codigo, id_deposito FROM estante WHERE id_deposito = ? ORDER BY id_estante";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, idDeposito);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                estantes.add(new Estante(
                        rs.getInt("id_estante"),
                        rs.getString("codigo"),
                        rs.getObject("id_deposito") == null ? null : rs.getInt("id_deposito")
                ));
            }
            pstmt.close();
            System.out.println("✓ Se encontraron " + estantes.size() + " estantes en el depósito ID: " + idDeposito);
            return estantes;
        } catch (SQLException e) {
            System.err.println("✗ Error al buscar estantes por depósito: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }

    /**
     * OBTENER CANTIDAD DE ESTANTES
     * @return el número total de estantes
     * @throws SQLException si hay error en la operación
     */
    @Override
    public int count() throws SQLException {
        String countSQL = "SELECT COUNT(*) as total FROM estante";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(countSQL);
            if (rs.next()) {
                int total = rs.getInt("total");
                stmt.close();
                System.out.println("✓ Total de estantes: " + total);
                return total;
            }
            stmt.close();
            return 0;
        } catch (SQLException e) {
            System.err.println("✗ Error al contar estantes: " + e.getMessage());
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
        String deleteSQL = "DELETE FROM estante";
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            Statement stmt = connection.createStatement();
            int rows = stmt.executeUpdate(deleteSQL);
            System.out.println("✓ Se eliminaron " + rows + " estantes");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("✗ Error al eliminar estantes: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connectionManager.closeConnection(connection);
            }
        }
    }
}
