# 🎯 FLUJO DE TRABAJO - ConnectionManager + Maven

## 📊 Diagrama del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                    APLICACIÓN JAVA                              │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              Tu Código Java                             │   │
│  │  (MiClase.java, CategoriaManager.java, etc.)           │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              ↓                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │   ConnectionManager (Singleton)                         │   │
│  │   ├─ getInstance()                                      │   │
│  │   ├─ getConnection()        ←─── Crea conexión         │   │
│  │   ├─ closeConnection()      ←─── Cierra conexión       │   │
│  │   └─ isConnectionActive()   ←─── Valida conexión       │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              ↓                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │   PostgreSQL JDBC Driver (42.6.0)                      │   │
│  │   ├─ Protocolo: JDBC                                   │   │
│  │   ├─ URL: jdbc:postgresql://localhost:5432             │   │
│  │   └─ Pool de conexiones                                │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              ↓
        ┌─────────────────────────────────────────┐
        │        PostgreSQL Server                │
        │                                         │
        │  Base de Datos: db_depositos           │
        │  Usuario: develop                      │
        │  Contraseña: develop123               │
        │  Host: localhost:5432                 │
        └─────────────────────────────────────────┘
```

---

## 🔄 Ciclo de Vida de una Conexión

```
1. INICIALIZACIÓN
   └─> Carga del driver PostgreSQL
       └─> Instancia de ConnectionManager (Singleton)

2. OBTENER CONEXIÓN
   └─> ConnectionManager.getInstance()
       └─> getConnection()
           └─> DriverManager.getConnection(URL, USER, PASSWORD)
               └─> Conexión activa a PostgreSQL

3. USAR LA CONEXIÓN
   └─> Crear Statement
       └─> Ejecutar SQL (query, insert, update, delete)
           └─> Procesar ResultSet

4. CERRAR CONEXIÓN
   └─> closeConnection(connection)
       └─> connection.close()
           └─> Liberar recursos
               └─> Conexión cerrada
```

---

## 🛠️ Proceso de Compilación y Ejecución

```
CÓDIGO FUENTE (.java)
     ↓
[mvn compile] ←─── Maven Compiler Plugin
     ↓
Verifica dependencias en pom.xml
     ↓
Descarga archivos necesarios (JDBC, etc.)
     ↓
Compila archivos .java → .class
     ↓
ARCHIVOS COMPILADOS (target/classes/)
     ↓
[mvn exec:java] ←─── Maven Exec Plugin
     ↓
Ejecuta JVM con classpath configurado
     ↓
APLICACIÓN EJECUTÁNDOSE
     ↓
Conecta a PostgreSQL
     ↓
Ejecuta operaciones en BD
     ↓
Cierra conexiones
     ↓
FIN
```

---

## 📦 Estructura de Dependencias en Maven

```
pom.xml
├── Project Info
│   ├─ groupId: py.com.nikosoft
│   ├─ artifactId: java101
│   ├─ version: 1.0.0
│   └─ packaging: jar
│
├── Properties
│   ├─ Java version: 11
│   └─ Encoding: UTF-8
│
├── Dependencies
│   ├─ PostgreSQL JDBC 42.6.0
│   │  └─ org.postgresql:postgresql
│   ├─ JUnit 4.13.2
│   │  └─ junit:junit
│   └─ SLF4J 2.0.7
│      ├─ org.slf4j:slf4j-api
│      └─ org.slf4j:slf4j-simple
│
└── Build Plugins
    ├─ Maven Compiler (Java 11)
    ├─ Maven Surefire (Tests)
    ├─ Maven Assembly (JAR)
    └─ Maven Exec (Ejecutar clases)
```

---

## 🚀 Flujo de Inicio Rápido (5 Pasos)

```
PASO 1: INSTALAR MAVEN
   └─> choco install maven -y
       └─> Descarga e instala Maven 3.9+
           └─> Agrega a variables de entorno

PASO 2: CONFIGURAR PostgreSQL
   └─> CREATE DATABASE db_depositos
       └─> CREATE USER develop
           └─> GRANT PRIVILEGES
               └─> Base de datos lista

PASO 3: COMPILAR PROYECTO
   └─> cd java101
       └─> mvn clean compile
           └─> Descarga dependencias (JDBC)
               └─> Compila código Java
                   └─> Genera target/classes/

PASO 4: EJECUTAR EJEMPLO
   └─> mvn exec:java -Dexec.mainClass="...ConnectionManagerExample"
       └─> Ejecuta aplicación
           └─> Conecta a PostgreSQL
               └─> Crea tabla de prueba
                   └─> Inserta y consulta datos
                       └─> Cierra conexión

PASO 5: USAR EN TU CÓDIGO
   └─> Importa ConnectionManager
       └─> Crea instance singleton
           └─> Obtén conexión
               └─> Ejecuta SQL
                   └─> Cierra conexión
```

---

## 🔐 Seguridad de Conexión

```
Variables Críticas:
┌──────────────────────────────┐
│  URL    │ jdbc:postgresql://localhost:5432/db_depositos
│  USER   │ develop
│  PASS   │ develop123
│  DRIVER │ org.postgresql.Driver
└──────────────────────────────┘
         ↓
    ConnectionManager.java
         ↓
  Carga del Driver JDBC
         ↓
DriverManager.getConnection()
         ↓
Establece conexión TCP/IP
         ↓
PostgreSQL Server
```

---

## 📈 Escalabilidad Futura

```
Aplicación Actual (ConnectionManager básico)
    ↓
Posibles Mejoras:
├─ Connection Pooling (HikariCP, C3P0)
├─ ORM (Hibernate, JPA)
├─ DAO Pattern (Data Access Objects)
├─ Repository Pattern
├─ Transactions Management
├─ Logging (SLF4J + Logback)
├─ Configuration Management (Spring Boot)
└─ REST API (Spring Web)
```

---

## 🧪 Flujo de Testing

```
ConnectionManagerExample.java
    ↓
1. getInstance() - Obtiene singleton
    ↓
2. getConnection() - Conecta a BD
    ↓
3. createSampleTable() - CREATE TABLE
    ↓
4. insertSampleData() - INSERT INTO
    ↓
5. querySampleData() - SELECT
    ↓
6. closeConnection() - CLOSE
    ↓
✓ Test completado exitosamente
```

---

## 📊 Estado de Archivos

```
ANTES                          DESPUÉS
═══════════════════════════════════════════════════════════════

java101.iml          ✓ (sin cambios)
README.md            ✓ ACTUALIZADO
└─ Contenido previo   └─ + Info Maven + ConnectionManager

py/com/nikosoft/utils/
│
├─ ConnectionManager.java     ✓ ACTUALIZADO
│  └─ Contenido vacío         └─ Implementación completa
│
├─ (nuevo) ConnectionManagerExample.java
├─ (nuevo) database.properties
└─ (nuevo) README_ConnectionManager.md

NUEVOS ARCHIVOS EN RAÍZ:
├─ pom.xml
├─ MAVEN_SETUP.md
├─ MANUAL_MAVEN_INSTALL.md
├─ INICIO_RAPIDO.md
├─ RESUMEN_CAMBIOS.txt
├─ Install-Maven.ps1
└─ install-maven.bat
```

---

## ✅ Checklist de Verificación

```
┌─ PASO 1: MAVEN
│ ├─ ✓ pom.xml creado
│ ├─ ✓ Maven instalable
│ ├─ ✓ Scripts de instalación listos
│ └─ ✓ Documentación completa
│
├─ PASO 2: DATABASE
│ ├─ ✓ Sentencias SQL documentadas
│ ├─ ✓ Credenciales configuradas
│ └─ ✓ URL de conexión lista
│
├─ PASO 3: CODE
│ ├─ ✓ ConnectionManager implementado
│ ├─ ✓ Singleton pattern aplicado
│ ├─ ✓ Thread-safe
│ └─ ✓ Manejo de errores
│
├─ PASO 4: EXAMPLE
│ ├─ ✓ ConnectionManagerExample creado
│ ├─ ✓ Demuestra CREATE TABLE
│ ├─ ✓ Demuestra INSERT
│ └─ ✓ Demuestra SELECT
│
└─ PASO 5: DOCUMENTATION
  ├─ ✓ README.md
  ├─ ✓ MAVEN_SETUP.md
  ├─ ✓ MANUAL_MAVEN_INSTALL.md
  ├─ ✓ INICIO_RAPIDO.md
  ├─ ✓ README_ConnectionManager.md
  └─ ✓ RESUMEN_CAMBIOS.txt
```

---

**Estado Final: ✅ COMPLETADO Y LISTO PARA USAR**


