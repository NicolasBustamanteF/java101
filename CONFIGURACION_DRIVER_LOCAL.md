# ⚙️ CONFIGURACIÓN ACTUALIZADA - Driver Local PostgreSQL

## 📝 CAMBIOS REALIZADOS

✅ **pom.xml actualizado**
   - Removido: Descarga automática de PostgreSQL JDBC
   - Agregado: Referencia al JAR local (`lib/postgresql-42.7.13.jar`)
   - Removida: Dependencia de SLF4J (logging)
   - Mantenido: JUnit para testing
   - Simplificado: Menos dependencias externas

✅ **ConnectionManager.java**
   - Sin cambios (funciona con el driver en el classpath)
   - Continúa cargando el driver automáticamente

---

## 📁 ESTRUCTURA DE CARPETAS REQUERIDA

```
java101/
├── lib/
│   └── postgresql-42.7.13.jar          ← Debe estar aquí
├── py/
│   └── com/nikosoft/utils/
│       ├── ConnectionManager.java
│       └── ConnectionManagerExample.java
├── pom.xml                             (Actualizado)
└── [otros archivos]
```

**IMPORTANTE:** El archivo `lib/postgresql-42.7.13.jar` debe estar en la carpeta `lib/` en la raíz del proyecto.

---

## 🚀 CÓMO COMPILAR Y EJECUTAR

### Opción 1: Compilar con Maven (recomendado)

```powershell
cd C:\Users\nicob\OneDrive\Documentos\repositorio\java101
mvn clean compile
```

Maven automáticamente incluirá el JAR local en el classpath.

### Opción 2: Ejecutar el ejemplo con Maven

```powershell
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
```

### Opción 3: Compilar manualmente (sin Maven)

```powershell
# Crear carpeta de compilación
mkdir bin

# Compilar con el driver en el classpath
javac -cp "lib/postgresql-42.7.13.jar" -d bin py/com/nikosoft/utils/ConnectionManager.java
javac -cp "lib/postgresql-42.7.13.jar:bin" -d bin py/com/nikosoft/utils/ConnectionManagerExample.java

# Ejecutar
java -cp "bin;lib/postgresql-42.7.13.jar" py.com.nikosoft.utils.ConnectionManagerExample
```

---

## 📋 CONFIGURACIÓN BASE DE DATOS

Las credenciales sigue siendo las mismas:

```
Host:         localhost
Puerto:       5432
Base de datos: db_depositos
Usuario:      develop
Contraseña:   develop123
```

Ejecutar estas sentencias SQL en PostgreSQL (si no lo hiciste):

```sql
CREATE DATABASE db_depositos;
CREATE USER develop WITH PASSWORD 'develop123';
GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop;
GRANT USAGE ON SCHEMA public TO develop;
```

---

## ✅ VERIFICAR QUE TODO FUNCIONA

```powershell
# 1. Verifica que el JAR existe
Test-Path "lib/postgresql-42.7.13.jar"
# Debe mostrar: True

# 2. Compila
mvn clean compile
# Debe mostrar: BUILD SUCCESS

# 3. Ejecuta el ejemplo
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
# Debe mostrar datos de la base de datos
```

---

## 🔧 pom.xml - Cambios Realizados

El archivo `pom.xml` ahora:

✓ Referencia el JAR local en lugar de descargarlo
✓ Usa scope `system` para la dependencia
✓ Especifica la ruta: `${project.basedir}/lib/postgresql-42.7.13.jar`

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.13</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/postgresql-42.7.13.jar</systemPath>
</dependency>
```

---

## 💡 VENTAJAS DEL CAMBIO

✅ No necesita descargar de internet
✅ Más rápido (no depende de Maven central)
✅ Control total sobre la versión del driver
✅ Funciona sin conexión a internet
✅ Tamaño de pom.xml reducido

---

## ⚠️ IMPORTANTE

- El archivo `postgresql-42.7.13.jar` DEBE estar en `lib/` (raíz del proyecto)
- Si el JAR no existe, Maven mostrará error
- El nombre exacto del archivo debe ser `postgresql-42.7.13.jar`

---

## 📝 PRÓXIMOS PASOS

1. ✅ Verifica que `lib/postgresql-42.7.13.jar` existe
2. ✅ Ejecuta: `mvn clean compile`
3. ✅ Ejecuta: `mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"`
4. ✅ ¡Listo!

---

**Actualizado:** Julio 2026
**Versión:** 1.0.1 (Driver Local)

