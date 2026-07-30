# UCOM - Seminario 1 #java101 📚

En esta sección estaremos realizando un breve repaso de Java

## Partes del Curso

* **Parte1**: Introducción a las aplicaciones en Java
* **Parte2**: Introducción a las clases / objetos
* **Parte3**: Instrucciones de control
* **Parte4**: Métodos - Funciones
* **Parte5**: Arreglos y Objetos
* **Parte6**: Clases y Objetos, POO - Herencia / Polimorfismo
* **Parte7**: Interfaces
* **Parte8**: Manejo de Excepciones
* **Parte9**: Patrones de Diseño
* **Parte10**: Proyecto - Sistema de Gestión VET

## 🚀 Inicio Rápido - Conexión a PostgreSQL

### 📋 Requisitos
- Java 11 o superior
- PostgreSQL 12 o superior
- Maven 3.6+ (opcional, para gestionar dependencias)

### 🔧 Configuración de Base de Datos

Ejecutar en PostgreSQL (pgAdmin o psql):
```sql
CREATE DATABASE db_depositos;
CREATE USER develop WITH PASSWORD 'develop123';
GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop;
GRANT USAGE ON SCHEMA public TO develop;
```

### 📦 Instalar Maven

Ver archivos:
- `MANUAL_MAVEN_INSTALL.md` - Instrucciones detalladas
- `Install-Maven.ps1` - Script automático (PowerShell)

Comando rápido (si tienes Chocolatey):
```powershell
choco install maven -y
```

### 💻 Compilar y Ejecutar

```powershell
# Compilar
mvn clean compile

# Ejecutar ejemplo
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
```

## 📚 Documentación

- **`py/com/nikosoft/utils/README_ConnectionManager.md`** - Doc técnica del ConnectionManager
- **`MAVEN_SETUP.md`** - Guía de comandos Maven
- **`MANUAL_MAVEN_INSTALL.md`** - Instalación manual de Maven

## 📁 Estructura del Proyecto

```
java101/
├── pom.xml                    ← Configuración Maven (nuevo)
├── py/
│   └── com/nikosoft/
│       ├── utils/
│       │   ├── ConnectionManager.java           (nuevo)
│       │   ├── ConnectionManagerExample.java    (nuevo)
│       │   └── database.properties              (nuevo)
│       ├── controller/
│       ├── boundary/
│       └── entities/
├── parte1-9/
└── README.md (este archivo)
```

## 🎯 Objetivos Principales

1. ✅ Aprender conceptos fundamentales de Java
2. ✅ Implementar Programación Orientada a Objetos (POO)
3. ✅ Trabajar con patrones de diseño
4. ✅ Conectar aplicaciones Java con bases de datos PostgreSQL
5. ✅ Gestionar dependencias con Maven

---

**Última actualización:** Julio 2026

