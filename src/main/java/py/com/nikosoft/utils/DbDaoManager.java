package py.com.nikosoft.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DbDaoManager<K ,ID> {
     void createTable() throws SQLException;
     int add(K k) throws SQLException;
     boolean delete(ID id) throws SQLException;
     boolean update(K k) throws SQLException;
     K getById(ID id) throws SQLException;
     List<K> getAll() throws SQLException;
 }
