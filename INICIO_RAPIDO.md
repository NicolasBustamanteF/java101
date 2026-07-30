# ⚡ INICIO RÁPIDO - 5 Pasos

## 📌 PASO 1: Instalar Maven (5 minutos)

### Opción A: Con Chocolatey (La más fácil)
```powershell
# Abre PowerShell como ADMINISTRADOR y ejecuta esto:
choco install maven -y
```

### Opción B: Script automático
```powershell
# En PowerShell como ADMINISTRADOR:
C:\Users\nicob\OneDrive\Documentos\repositorio\java101\Install-Maven.ps1
```

### Opción C: Instalación manual
Ver: `MANUAL_MAVEN_INSTALL.md`

**✓ Verificar que funcionó:**
```powershell
mvn --version
```

---

## 📌 PASO 2: Configurar PostgreSQL (2 minutos)

### Opción A: Desde pgAdmin (interfaz gráfica)
1. Abre pgAdmin
2. Hacer clic derecho en "Databases" → "Create" → "Database"
3. Nombre: `db_depositos`
4. Crear usuario `develop` con contraseña `develop123`
5. Darle permisos

### Opción B: Desde psql (terminal)
```sql
-- En psql o cualquier cliente SQL:
CREATE DATABASE db_depositos;
CREATE USER develop WITH PASSWORD 'develop123';
GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop;
GRANT USAGE ON SCHEMA public TO develop;
```

**✓ Verificar que funcionó:**
```powershell
# Conectarse a la BD desde PowerShell:
psql -h localhost -U develop -d db_depositos
```

---

## 📌 PASO 3: Compilar el Proyecto (1 minuto)

```powershell
# Navega a la carpeta del proyecto:
cd C:\Users\nicob\OneDrive\Documentos\repositorio\java101

# Compila:
mvn clean compile
```

**Esto va a:**
1. Descargar el driver de PostgreSQL
2. Compilar todo el código Java
3. Crear la carpeta `target/` con los archivos compilados

La primera vez tarda más (descarga dependencias). Las siguientes son más rápidas.

---

## 📌 PASO 4: Ejecutar el Ejemplo (1 minuto)

```powershell
# Aún en la carpeta del proyecto:
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
```

**Si ve este mensaje, ¡TODO FUNCIONA! ✓**
```
✓ Conexión exitosa a la base de datos db_depositos
✓ Tabla 'test_tabla' creada/verificada exitosamente
✓ Se insertaron X registros
=== Datos en test_tabla ===
ID: 1 | Nombre: Juan Pérez | Correo: juan@example.com
ID: 2 | Nombre: María García | Correo: maria@example.com
✓ Conexión cerrada correctamente
```

---

## 📌 PASO 5: Usar ConnectionManager en tu código (2 minutos)

```java
// En cualquier clase Java:

import py.com.nikosoft.utils.ConnectionManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class MiClase {
    public static void main(String[] args) {
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn = null;
        
        try {
            // Obtener conexión
            conn = cm.getConnection();
            
            // Usar la conexión
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mi_tabla");
            
            while (rs.next()) {
                System.out.println(rs.getString("columna"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Cerrar conexión
            if (conn != null) {
                cm.closeConnection(conn);
            }
        }
    }
}
```

---

## ✅ Checklist de Verificación

- [ ] Maven instalado: `mvn --version` (muestra versión)
- [ ] PostgreSQL ejecutándose: `psql -V` (muestra versión)
- [ ] Base de datos creada: Desde pgAdmin o psql
- [ ] Usuario 'develop' creado: Con contraseña 'develop123'
- [ ] Proyecto compilado: `mvn clean compile` (sin errores)
- [ ] Ejemplo ejecutado: Muestra datos de la BD
- [ ] ConnectionManager funciona: Puedes usarlo en tu código

---

## 🆘 Problemas Comunes y Soluciones

### ❌ "Maven no está instalado"
**Solución:** Ejecutar los scripts del PASO 1

### ❌ "Cannot find symbol - ConnectionManager"
**Solución:** Ejecutar `mvn clean compile` nuevamente

### ❌ "Error al conectar a la base de datos"
**Solución:** 
1. Verificar PostgreSQL está ejecutándose
2. Verificar que la BD y usuario existen
3. Probar conexión: `psql -h localhost -U develop -d db_depositos`

### ❌ "Connection refused"
**Solución:** PostgreSQL no está ejecutándose
- En Windows: Buscar "Servicios" → Iniciar "PostgreSQL"

### ❌ "usuario o contraseña inválida"
**Solución:** Cambiar en `ConnectionManager.java` líneas 19-20

---

## 📚 Documentación Completa

- `README.md` - Descripción general del proyecto
- `MANUAL_MAVEN_INSTALL.md` - Formas alternativas de instalar Maven
- `MAVEN_SETUP.md` - Comandos y configuración de Maven
- `py/com/nikosoft/utils/README_ConnectionManager.md` - Doc técnica

---

## 🎯 Próximos Pasos

Después de completar estos 5 pasos:

1. Crear las tablas de la base de datos (Categoria, Deposito, etc.)
2. Implementar DAO (Data Access Objects) para cada tabla
3. Crear Manager classes para la lógica de negocio
4. Desarrollar la interfaz gráfica o REST API
5. Escribir tests con JUnit

---

**¿Listo? ¡Comienza con el PASO 1!** 🚀


