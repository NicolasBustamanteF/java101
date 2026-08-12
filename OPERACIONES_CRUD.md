# 🗄️ OPERACIONES CRUD - CategoriaDAO

## 📋 Descripción

Se han implementado todas las operaciones CRUD (Create, Read, Update, Delete) en el archivo `CategoriaDAO.java`.

Este DAO (Data Access Object) utiliza `ConnectionManager` para manejar conexiones a la base de datos PostgreSQL y proporciona métodos para:
- **ADD** (CREATE) - Insertar nuevas categorías
- **SELECT** (READ) - Obtener todas las categorías
- **SELECT BY ID** (READ) - Obtener una categoría específica
- **SELECT BY DESCRIPCIÓN** (READ) - Buscar categorías por descripción (con LIKE)
- **UPDATE** - Actualizar categorías existentes
- **DELETE** - Eliminar categorías

---

## 📁 Archivos Creados

### 1. **CategoriaDAO.java**
   - Ubicación: `py/com/nikosoft/depositos/boundary/CategoriaDAO.java`
   - Clase que implementa todas las operaciones CRUD
   - Usa `ConnectionManager` para obtener conexiones
   - Métodos documentados y seguros (PreparedStatements)

### 2. **CategoriaCRUDExample.java**
   - Ubicación: `py/com/nikosoft/depositos/CategoriaCRUDExample.java`
   - Ejemplo completo de uso de todas las operaciones CRUD
   - Demostraciones prácticas de cada operación
   - Listo para ejecutar

---

## 🔧 MÉTODOS IMPLEMENTADOS

### 1. `createTable()` - Crear Tabla
```java
public void createTable() throws SQLException
```
**Descripción:** Crea la tabla `categoria` si no existe

**Parámetros:** Ninguno

**Retorna:** void

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
dao.createTable();
```

**SQL generado:**
```sql
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria SERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
)
```

---

### 2. `add()` - Agregar Nueva Categoría (CREATE)
```java
public int add(String descripcion) throws SQLException
```
**Descripción:** Inserta una nueva categoría en la base de datos

**Parámetros:**
- `descripcion` (String) - La descripción de la categoría

**Retorna:** int - El ID de la categoría creada, o -1 si falló

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
int idCategoria = dao.add("Electrónica");
System.out.println("Categoría creada con ID: " + idCategoria);
```

**SQL generado:**
```sql
INSERT INTO categoria (descripcion) VALUES (?)
```

---

### 3. `delete()` - Eliminar Categoría (DELETE)
```java
public boolean delete(int idCategoria) throws SQLException
```
**Descripción:** Elimina una categoría por su ID

**Parámetros:**
- `idCategoria` (int) - El ID de la categoría a eliminar

**Retorna:** boolean - true si se eliminó, false si no se encontró

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
boolean eliminado = dao.delete(1);
if (eliminado) {
    System.out.println("Categoría eliminada");
}
```

**SQL generado:**
```sql
DELETE FROM categoria WHERE id_categoria = ?
```

---

### 4. `update()` - Actualizar Categoría (UPDATE)
```java
public boolean update(int idCategoria, String descripcion) throws SQLException
```
**Descripción:** Actualiza la descripción de una categoría

**Parámetros:**
- `idCategoria` (int) - El ID de la categoría a actualizar
- `descripcion` (String) - La nueva descripción

**Retorna:** boolean - true si se actualizó, false si no se encontró

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
boolean actualizado = dao.update(1, "Electrónica y Gadgets");
if (actualizado) {
    System.out.println("Categoría actualizada");
}
```

**SQL generado:**
```sql
UPDATE categoria SET descripcion = ? WHERE id_categoria = ?
```

---

### 5. `select()` - Obtener Todas las Categorías (READ ALL)
```java
public List<Categoria> select() throws SQLException
```
**Descripción:** Obtiene todas las categorías de la base de datos

**Parámetros:** Ninguno

**Retorna:** List<Categoria> - Lista de todas las categorías

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
List<Categoria> categorias = dao.select();
for (Categoria cat : categorias) {
    System.out.println("[" + cat.getIdCategoria() + "] " + cat.getDescripcion());
}
```

**SQL generado:**
```sql
SELECT id_categoria, descripcion FROM categoria ORDER BY id_categoria
```

---

### 6. `selectById()` - Obtener Categoría por ID (READ BY ID)
```java
public Categoria selectById(int idCategoria) throws SQLException
```
**Descripción:** Obtiene una categoría específica por su ID

**Parámetros:**
- `idCategoria` (int) - El ID de la categoría a buscar

**Retorna:** Categoria - La categoría encontrada, o null si no existe

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
Categoria categoria = dao.selectById(1);
if (categoria != null) {
    System.out.println("Encontrada: " + categoria.getDescripcion());
}
```

**SQL generado:**
```sql
SELECT id_categoria, descripcion FROM categoria WHERE id_categoria = ?
```

---

### 7. `selectByDescripcion()` - Buscar por Descripción con LIKE (READ BY DESCRIPTION)
```java
public List<Categoria> selectByDescripcion(String descripcion) throws SQLException
```
**Descripción:** Busca categorías cuya descripción contenga el texto especificado (búsqueda parcial con LIKE)

**Parámetros:**
- `descripcion` (String) - El texto a buscar (puede ser parcial)

**Retorna:** List<Categoria> - Lista de categorías que coinciden con la búsqueda

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();

// Búsqueda por palabra completa
List<Categoria> electronica = dao.selectByDescripcion("Electrónica");

// Búsqueda parcial
List<Categoria> ropa = dao.selectByDescripcion("Ropa");

// Búsqueda por letra
List<Categoria> conY = dao.selectByDescripcion("y");

for (Categoria cat : electronica) {
    System.out.println("[" + cat.getIdCategoria() + "] " + cat.getDescripcion());
}
```

**SQL generado:**
```sql
SELECT id_categoria, descripcion FROM categoria 
WHERE descripcion ILIKE ? 
ORDER BY id_categoria
```

**Nota:** Usa `ILIKE` en PostgreSQL (búsqueda insensible a mayúsculas/minúsculas)

---

### 8. `getTotalCategorias()` - Contar Categorías
```java
public int getTotalCategorias() throws SQLException
```
**Descripción:** Obtiene el número total de categorías en la base de datos

**Parámetros:** Ninguno

**Retorna:** int - El número total de categorías

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
int total = dao.getTotalCategorias();
System.out.println("Total de categorías: " + total);
```

**SQL generado:**
```sql
SELECT COUNT(*) as total FROM categoria
```

---

### 9. `deleteAll()` - Eliminar Todas las Categorías
```java
public void deleteAll() throws SQLException
```
**Descripción:** Elimina todas las categorías (útil para pruebas)

**Parámetros:** Ninguno

**Retorna:** void

**Ejemplo:**
```java
CategoriaDAO dao = new CategoriaDAO();
dao.deleteAll();
System.out.println("Todas las categorías han sido eliminadas");
```

**SQL generado:**
```sql
DELETE FROM categoria
```

---

## 🚀 CÓMO USAR

### Paso 1: Ejecutar el Ejemplo Completo
```powershell
cd C:\Users\nicob\OneDrive\Documentos\repositorio\java101

# Compilar
mvn clean compile

# Ejecutar el ejemplo
mvn exec:java -Dexec.mainClass="py.com.nikosoft.depositos.CategoriaCRUDExample"
```

### Paso 2: Usar en tu Propio Código
```java
import py.com.nikosoft.depositos.boundary.CategoriaDAO;
import py.com.nikosoft.depositos.entities.Categoria;
import java.sql.SQLException;

public class MiClase {
    public static void main(String[] args) {
        try {
            CategoriaDAO dao = new CategoriaDAO();
            
            // Crear tabla
            dao.createTable();
            
            // Agregar categoría
            int id = dao.add("Mi Categoría");
            
            // Consultar por ID
            Categoria cat = dao.selectById(id);
            
            // Actualizar
            dao.update(id, "Categoría Actualizada");
            
            // Buscar
            List<Categoria> resultados = dao.selectByDescripcion("Actualizada");
            
            // Eliminar
            dao.delete(id);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 📊 ESTRUCTURA DE DATOS

### Tabla: `categoria`
```
┌─────────────────┬──────────────┬─────────────┐
│ Columna         │ Tipo         │ Restricción │
├─────────────────┼──────────────┼─────────────┤
│ id_categoria    │ SERIAL       │ PRIMARY KEY │
│ descripcion     │ VARCHAR(255) │ NOT NULL    │
└─────────────────┴──────────────┴─────────────┘
```

### Clase: `Categoria`
```java
public class Categoria {
    private int idCategoria;           // ID único
    private String descripcion;        // Descripción de la categoría
}
```

---

## ✨ CARACTERÍSTICAS PRINCIPALES

✅ **Seguridad SQL** - Usa PreparedStatements para evitar inyección SQL
✅ **Gestión de Conexiones** - Usa ConnectionManager para conexiones seguras
✅ **Manejo de Errores** - Try-catch y mensajes descriptivos
✅ **Logging** - Mensajes informativos en cada operación
✅ **Búsqueda Flexible** - LIKE para búsquedas parciales
✅ **Documentación Completa** - Javadoc en todos los métodos

---

## 🔒 BUENAS PRÁCTICAS IMPLEMENTADAS

1. **PreparedStatements** - Previene inyección SQL
   ```java
   PreparedStatement pstmt = connection.prepareStatement(selectSQL);
   pstmt.setString(1, descripcion);  // Valor escapado
   ```

2. **Manejo de Recursos** - Cierra conexiones correctamente
   ```java
   try {
       // operación
   } finally {
       if (connection != null) {
           connectionManager.closeConnection(connection);
       }
   }
   ```

3. **Mensajes Informativos** - Feedback visual de operaciones
   ```java
   System.out.println("✓ Categoría agregada exitosamente con ID: " + idCategoria);
   System.err.println("✗ Error al agregar categoría: " + e.getMessage());
   ```

4. **Retorno de Valores Significativos** - Facilita validación
   ```java
   // Retorna el ID si se creó, -1 si falló
   int id = dao.add("Nueva Categoría");
   
   // Retorna true/false para actualizar/eliminar
   boolean resultado = dao.delete(1);
   ```

---

## 📝 NOTAS IMPORTANTES

- La tabla se crea automáticamente en la primera llamada a `createTable()`
- Todos los métodos lanzan `SQLException` - debe ser manejada con try-catch
- El campo `id_categoria` se auto-incrementa automáticamente
- Las búsquedas por descripción son insensibles a mayúsculas/minúsculas (ILIKE)
- ConnectionManager maneja la conexión a PostgreSQL automáticamente

---

## 🔄 FLUJO TÍPICO DE USO

```
1. Crear instancia del DAO
   └─> CategoriaDAO dao = new CategoriaDAO();

2. Crear tabla (primera vez)
   └─> dao.createTable();

3. Insertar datos (ADD)
   └─> int id = dao.add("Nueva Categoría");

4. Consultar datos (SELECT)
   └─> List<Categoria> categorias = dao.select();

5. Buscar específico (SELECT BY ID)
   └─> Categoria cat = dao.selectById(id);

6. Buscar por criterio (SELECT BY DESCRIPCION)
   └─> List<Categoria> resultados = dao.selectByDescripcion("palabra");

7. Actualizar datos (UPDATE)
   └─> dao.update(id, "Nueva descripción");

8. Eliminar datos (DELETE)
   └─> dao.delete(id);
```

---

**Versión:** 1.0.0  
**Última actualización:** Julio 2026  
**Estado:** ✅ Completo y listo para usar

