# Instalación Manual de Maven (Sin Chocolatey)

## Opción 1: Usar Chocolatey (Recomendado - Lo más fácil)

### Paso 1: Instalar Chocolatey
Si no tienes Chocolatey, abre PowerShell **como administrador** y ejecuta:

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### Paso 2: Instalar Maven
Una vez que tengas Chocolatey, ejecuta:

```powershell
choco install maven -y
```

### Paso 3: Verificar instalación
```powershell
mvn --version
```

---

## Opción 2: Instalación Manual de Maven

### Paso 1: Descargar Maven
1. Ir a: https://maven.apache.org/download.cgi
2. Descargar: **Binary zip archive** (apache-maven-3.9.x-bin.zip)
3. Extraer en una carpeta, por ejemplo: `C:\tools\maven`

### Paso 2: Configurar Variables de Entorno

#### En Windows 10/11:
1. Presionar `Win + X` → **Sistema**
2. Ir a **Configuración avanzada del sistema**
3. Hacer clic en **Variables de entorno**
4. En **Variables del sistema**, hacer clic en **Nueva**
5. Agregar:
   - **Nombre**: `MAVEN_HOME`
   - **Valor**: `C:\tools\maven` (o donde extrajiste Maven)

6. Editar la variable `Path`:
   - Hacer clic en **Path** (en Variables del sistema)
   - Hacer clic en **Editar**
   - Hacer clic en **Nuevo**
   - Agregar: `%MAVEN_HOME%\bin`
   - Hacer clic en **Aceptar**

### Paso 3: Verificar en PowerShell
Abre una **nueva ventana de PowerShell** y ejecuta:

```powershell
mvn --version
```

---

## Opción 3: Usar Maven Wrapper (Sin instalación global)

Si no quieres instalar Maven globalmente, puedes usar Maven Wrapper:

### Paso 1: Descargar Maven Wrapper
Desde la carpeta del proyecto, ejecuta en PowerShell:

```powershell
curl -o wrapper.zip https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar
```

O descarga manualmente: https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar

### Paso 2: Usar los scripts de Maven Wrapper
En lugar de `mvn`, usar:

```powershell
# En Windows (PowerShell)
.\mvnw.cmd clean compile

# O en PowerShell moderno
.\mvnw clean compile
```

---

## Después de Instalar Maven

Una vez que Maven esté instalado, vuelve a la carpeta del proyecto y ejecuta:

```powershell
cd C:\Users\nicob\OneDrive\Documentos\repositorio\java101

# Descargar dependencias y compilar
mvn clean compile

# Ejecutar el ejemplo
mvn exec:java -Dexec.mainClass="py.com.nikosoft.utils.ConnectionManagerExample"
```

---

## ¿Cuál elegir?

| Opción | Ventaja | Desventaja |
|--------|---------|-----------|
| **Chocolatey** | Lo más fácil, Maven disponible globalmente | Requiere instalar Chocolatey primero |
| **Manual** | Control total | Pasos manuales, requiere configurar variables de entorno |
| **Maven Wrapper** | No requiere instalación global | Solo para este proyecto |

**Recomendación**: Usa **Chocolatey** si es la primera vez que instalas Maven.

---

¿Necesitas ayuda con alguna opción?

