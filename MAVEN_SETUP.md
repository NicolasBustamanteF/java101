# Maven Setup Guide - java101

## ¿Qué es Maven?

Maven es una herramienta de gestión de proyectos Java que automatiza:
- Descarga de dependencias
- Compilación del código
- Ejecución de tests
- Empaquetamiento de la aplicación

## 📥 Instalación de Maven

### Opción 1: Usando Chocolatey (Windows)
```powershell
choco install maven
```

### Opción 2: Descarga Manual
1. Descargar desde: https://maven.apache.org/download.cgi
2. Extraer el archivo ZIP
3. Agregar a las variables de entorno PATH

### Verificar Instalación
```powershell
mvn --version
```

Deberías ver algo como:
```
Apache Maven 3.9.x
Java version: 11.x.x
```

## 🔧 Configuración en IntelliJ IDEA

1. **Abrir el proyecto** en IntelliJ IDEA
2. IntelliJ detectará automáticamente el `pom.xml`
3. Hacer clic en **"Load Maven Changes"** cuando aparezca la notificación
4. Esperar a que descargue las dependencias (primera vez tarda más)

## 📦 Comandos Maven Básicos

### Limpiar el proyecto
```powershell
mvn clean
```

### Compilar
```powershell
mvn compile
```

### Ejecutar tests
```powershell
mvn test
```

### Empaquetar (crear JAR)
```powershell
mvn package
```

### Limpiar y compilar
```powershell
mvn clean compile
```

### Limpiar, compilar y empaquetar
```powershell
mvn clean package
```

## 🚀 Usar el ConnectionManager

### Opción 1: Desde IntelliJ IDEA
1. Abrir `ConnectionManagerExample.java`
2. Hacer clic derecho en la clase
3. Seleccionar **"Run 'ConnectionManagerExample.main()'"**

### Opción 2: Desde Terminal (PowerShell)
```powershell
# Compilar
mvn clean compile

# Ejecutar la clase
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
```

### Opción 3: Crear JAR ejecutable y ejecutar
```powershell
# Crear JAR con todas las dependencias
mvn clean package assembly:single

# Ejecutar
java -jar target/java101-1.0.0-jar-with-dependencies.jar
```

## 📋 Estructura del Proyecto con Maven

```
java101/
├── pom.xml                          # Configuración Maven
├── src/
│   ├── main/
│   │   ├── java/                   # Código fuente
│   │   │   └── py/com/nikosoft/
│   │   │       ├── utils/
│   │   │       ├── controller/
│   │   │       ├── boundary/
│   │   │       └── entities/
│   │   └── resources/              # Archivos de configuración
│   │       └── database.properties
│   └── test/
│       └── java/                   # Tests
├── target/                          # Compilados (generado por Maven)
└── java101.iml                      # Configuración IntelliJ
```

## 🐛 Troubleshooting

### Error: "Maven is not installed"
- Solución: Instalar Maven siguiendo las instrucciones de arriba
- O usar Maven wrapper: `mvnw.cmd` (si lo descargaste)

### Error: "Cannot find symbol" en la compilación
- Solución: `mvn clean compile` (limpiar y recompilar)
- Si persiste: `mvn dependency:resolve` (descargar dependencias)

### Error: "PostgreSQL driver not found"
- Solución: Las dependencias se descargan automáticamente al ejecutar:
  ```powershell
  mvn clean compile
  ```

### IntelliJ no reconoce las dependencias
- Solución: 
  1. File → Invalidate Caches → Invalidate and Restart
  2. O hacer clic en el icono "Load Maven Changes"

## 📝 Contenido del pom.xml

El archivo `pom.xml` incluye:

### Dependencias Principales:
- **PostgreSQL JDBC**: `org.postgresql:postgresql:42.6.0`
- **JUnit**: Para testing
- **SLF4J**: Para logging

### Configuración:
- Java version: 11
- Codificación: UTF-8
- Plugin de compilación Maven
- Plugin para crear JAR ejecutable

## 🔄 Actualizar Dependencias

Para actualizar a versiones más recientes:

```powershell
mvn versions:display-dependency-updates
mvn versions:use-latest-versions
mvn versions:commit
```

## 📚 Recursos Útiles

- Maven Docs: https://maven.apache.org/
- PostgreSQL JDBC: https://jdbc.postgresql.org/
- Maven Plugins: https://maven.apache.org/plugins/

---

**Próximo paso**: Ejecutar `mvn clean compile` para descargar las dependencias

