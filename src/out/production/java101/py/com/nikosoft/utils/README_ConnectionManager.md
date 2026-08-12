# ConnectionManager - PostgreSQL Configuration Guide

## 📋 Configuración de la Base de Datos

### 1. Sentencias SQL de Configuración

Ejecuta las siguientes sentencias en PostgreSQL para configurar la base de datos:

```sql
-- Crear la base de datos
CREATE DATABASE db_depositos;

-- Crear el usuario 'develop'
CREATE USER develop WITH PASSWORD 'develop123';

-- Otorgar todos los privilegios en la base de datos
GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop;

-- Otorgar uso del esquema público
GRANT USAGE ON SCHEMA public TO develop;

-- (Opcional) Cambiar contraseña del usuario postgres
ALTER USER postgres WITH PASSWORD 'postgres';
```

### 2. Pasos de Instalación

#### A. PostgreSQL (si no está instalado)
1. Descargar desde: https://www.postgresql.org/download/
2. Instalar con la contraseña de postgres
3. Asegurarse de que el servicio está ejecutándose

#### B. Driver JDBC de PostgreSQL
Agregar la dependencia en tu `pom.xml` (Maven):

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.6.0</version>
</dependency>
```

O si usas Gradle:

```gradle
implementation 'org.postgresql:postgresql:42.6.0'
```

### 3. Estructura del ConnectionManager

```java
ConnectionManager connectionManager = ConnectionManager.getInstance();
Connection connection = connectionManager.getConnection();
// ... usar la conexión ...
connectionManager.closeConnection(connection);
```

## 🔑 Credenciales

- **Base de Datos**: db_depositos
- **Usuario**: develop
- **Contraseña**: develop123
- **Host**: localhost
- **Puerto**: 5432

## ✨ Características Principales

1. **Patrón Singleton**: Garantiza una única instancia de ConnectionManager
2. **Thread-Safe**: Implementación sincronizada para entornos multi-thread
3. **Manejo de Errores**: Captura y reporte de excepciones SQL
4. **Validación de Conexión**: Método para verificar si la conexión está activa
5. **Información de Conexión**: Método para obtener detalles de configuración

## 📖 Métodos Disponibles

### `getInstance()`
Obtiene la instancia única del ConnectionManager (Singleton Pattern)

```java
ConnectionManager manager = ConnectionManager.getInstance();
```

### `getConnection()`
Obtiene una nueva conexión con la base de datos

```java
try {
    Connection conn = connectionManager.getConnection();
    // ... usar la conexión ...
} catch (SQLException e) {
    // Manejar error
}
```

### `closeConnection(Connection)`
Cierra una conexión de manera segura

```java
connectionManager.closeConnection(connection);
```

### `isConnectionActive(Connection)`
Verifica si una conexión está activa

```java
if (connectionManager.isConnectionActive(connection)) {
    // Usar la conexión
}
```

### `getConnectionInfo()`
Obtiene información de la configuración de conexión

```java
System.out.println(connectionManager.getConnectionInfo());
```

## 🧪 Prueba de Conexión

Ejecuta `ConnectionManagerExample.java` para probar la conexión:

```bash
java py.com.nikosoft.utils.ConnectionManagerExample
```

Esto debería:
1. Conectarse a la base de datos
2. Crear una tabla de prueba
3. Insertar datos de ejemplo
4. Consultar y mostrar los datos
5. Cerrar la conexión correctamente

## ⚠️ Troubleshooting

### Error: "No se pudo cargar el driver de PostgreSQL"
- Solución: Agregar la dependencia JDBC de PostgreSQL al proyecto

### Error: "Conexión rechazada"
- Solución: Verificar que PostgreSQL está ejecutándose en localhost:5432

### Error: "Usuario o contraseña inválidos"
- Solución: Verificar las credenciales en ConnectionManager (develop/develop123)

### Error: "Base de datos db_depositos no existe"
- Solución: Ejecutar las sentencias SQL de configuración (paso 1)

## 🔐 Consideraciones de Seguridad

- **Producción**: No almacenar contraseñas en código. Usar variables de entorno o archivos de configuración externos
- **Variables de Entorno** (Recomendado para producción):

```java
String url = System.getenv("DB_URL");
String user = System.getenv("DB_USER");
String password = System.getenv("DB_PASSWORD");
```

## 📝 Ejemplo Completo de Uso

```java
ConnectionManager cm = ConnectionManager.getInstance();
Connection conn = null;

try {
    conn = cm.getConnection();
    
    if (cm.isConnectionActive(conn)) {
        // Ejecutar consultas
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM depositos");
        
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
        }
    }
    
} catch (SQLException e) {
    e.printStackTrace();
} finally {
    if (conn != null) {
        cm.closeConnection(conn);
    }
}
```

---

**Versión**: 1.0  
**Última actualización**: 2026-07-30

