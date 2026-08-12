# 🖥️ INTERFAZ DE CONSOLA - CategoriaMenu

## 📋 Descripción

Se ha implementado una interfaz de consola interactiva (`CategoriaMenu`) que proporciona un menú visual para gestionar categorías sin necesidad de escribir código Java.

**Archivo:** `py/com/nikosoft/depositos/controller/CategoriaMenu.java`

---

## 🚀 CÓMO EJECUTAR

### Opción 1: Desde Maven
```powershell
cd C:\Users\nicob\OneDrive\Documentos\repositorio\java101
mvn clean compile
mvn exec:java -Dexec.mainClass="py.com.nikosoft.depositos.controller.CategoriaMenu"
```

### Opción 2: Directamente desde IntelliJ IDEA
1. Abre `CategoriaMenu.java`
2. Haz clic derecho en la clase
3. Selecciona "Run 'CategoriaMenu.main()'"

### Opción 3: Desde línea de comandos (Java)
```powershell
# Compilar manualmente
javac -cp "lib/postgresql-42.7.13.jar" py/com/nikosoft/depositos/controller/CategoriaMenu.java

# Ejecutar
java -cp "lib/postgresql-42.7.13.jar;." py.com.nikosoft.depositos.controller.CategoriaMenu
```

---

## 📊 MENÚ PRINCIPAL

Al ejecutar, verás un menú como este:

```
╔════════════════════════════════════════════════════════════╗
║       SISTEMA DE GESTIÓN DE CATEGORÍAS                     ║
╚════════════════════════════════════════════════════════════╝

  1️⃣  Agregar nueva categoría
  2️⃣  Ver todas las categorías
  3️⃣  Buscar categoría por ID
  4️⃣  Buscar categoría por descripción
  5️⃣  Actualizar categoría
  6️⃣  Eliminar categoría
  7️⃣  Ver total de categorías
  0️⃣  Salir

  Seleccione una opción: 
```

---

## 🔧 OPCIONES DEL MENÚ

### 1️⃣ AGREGAR NUEVA CATEGORÍA

**Descripción:** Inserta una nueva categoría en la base de datos

**Pasos:**
1. Selecciona opción `1`
2. Ingresa la descripción de la categoría
3. Se muestra el ID asignado

**Ejemplo:**
```
  AGREGAR NUEVA CATEGORÍA
─────────────────────────────────────────────────────────

  Ingrese la descripción de la categoría: Electrónica

✓ Categoría agregada exitosamente
  ID: 1
  Descripción: Electrónica
```

---

### 2️⃣ VER TODAS LAS CATEGORÍAS

**Descripción:** Muestra un listado de todas las categorías registradas

**Pasos:**
1. Selecciona opción `2`
2. Se muestra una tabla con todas las categorías

**Ejemplo:**
```
  TODAS LAS CATEGORÍAS
─────────────────────────────────────────────────────────

  ┌─────┬──────────────────────────────────────┐
  │ ID  │ Descripción                          │
  ├─────┼──────────────────────────────────────┤
  │ 1   │ Electrónica                          │
  │ 2   │ Ropa y Accesorios                    │
  │ 3   │ Hogar y Decoración                   │
  └─────┴──────────────────────────────────────┘

  Total de categorías: 3
```

---

### 3️⃣ BUSCAR CATEGORÍA POR ID

**Descripción:** Busca una categoría específica usando su ID

**Pasos:**
1. Selecciona opción `3`
2. Ingresa el ID de la categoría
3. Se muestra la información si existe

**Ejemplo:**
```
  BUSCAR CATEGORÍA POR ID
─────────────────────────────────────────────────────────

  Ingrese el ID de la categoría: 1

  ✓ Categoría encontrada:
    ID: 1
    Descripción: Electrónica
```

---

### 4️⃣ BUSCAR CATEGORÍA POR DESCRIPCIÓN

**Descripción:** Busca categorías usando búsqueda parcial (LIKE)

**Pasos:**
1. Selecciona opción `4`
2. Ingresa el texto a buscar (puede ser parcial)
3. Se muestran todas las categorías que contienen el texto

**Ejemplos de búsqueda:**

```
Búsqueda 1: "Electró"
  ✓ Se encontraron 1 resultado(s):
  ┌─────┬──────────────────────────────────────┐
  │ ID  │ Descripción                          │
  ├─────┼──────────────────────────────────────┤
  │ 1   │ Electrónica                          │
  └─────┴──────────────────────────────────────┘

Búsqueda 2: "y"
  ✓ Se encontraron 3 resultado(s):
  ┌─────┬──────────────────────────────────────┐
  │ ID  │ Descripción                          │
  ├─────┼──────────────────────────────────────┤
  │ 2   │ Ropa y Accesorios                    │
  │ 3   │ Hogar y Decoración                   │
  │ 4   │ Deportes y Recreación                │
  └─────┴──────────────────────────────────────┘
```

---

### 5️⃣ ACTUALIZAR CATEGORÍA

**Descripción:** Modifica la descripción de una categoría existente

**Pasos:**
1. Selecciona opción `5`
2. Ingresa el ID de la categoría a actualizar
3. Ingresa la nueva descripción
4. Confirma la actualización

**Ejemplo:**
```
  ACTUALIZAR CATEGORÍA
─────────────────────────────────────────────────────────

  Ingrese el ID de la categoría a actualizar: 2

  Descripción actual: Ropa y Accesorios
  Ingrese la nueva descripción: Vestuario y Accesorios

  ✓ Categoría actualizada exitosamente
    ID: 2
    Nueva descripción: Vestuario y Accesorios
```

---

### 6️⃣ ELIMINAR CATEGORÍA

**Descripción:** Elimina una categoría de la base de datos (con confirmación)

**Pasos:**
1. Selecciona opción `6`
2. Ingresa el ID de la categoría a eliminar
3. Se muestra la información de la categoría
4. Confirma la eliminación (s/n)
5. Se elimina si confirmas

**Ejemplo:**
```
  ELIMINAR CATEGORÍA
─────────────────────────────────────────────────────────

  Ingrese el ID de la categoría a eliminar: 3

  ¿Está seguro de que desea eliminar esta categoría?
  ID: 3
  Descripción: Hogar y Decoración

  Confirme (s/n): s

  ✓ Categoría eliminada exitosamente
```

---

### 7️⃣ VER TOTAL DE CATEGORÍAS

**Descripción:** Muestra el número total de categorías registradas

**Pasos:**
1. Selecciona opción `7`
2. Se muestra el total

**Ejemplo:**
```
  TOTAL DE CATEGORÍAS
─────────────────────────────────────────────────────────

  Total de categorías registradas: 5
```

---

### 0️⃣ SALIR

**Descripción:** Cierra la aplicación

**Pasos:**
1. Selecciona opción `0`
2. Se muestra mensaje de despedida
3. Se cierra el programa

---

## ✨ CARACTERÍSTICAS

✅ **Interfaz clara y intuitiva** - Menú visual con emojis y bordes
✅ **Validación de entrada** - Verifica IDs numéricos y descripciones no vacías
✅ **Confirmación de eliminar** - Pide confirmación antes de eliminar
✅ **Búsqueda flexible** - Búsqueda parcial con LIKE
✅ **Tabla formateada** - Muestra resultados en tabla ordenada
✅ **Manejo de errores** - Mensajes claros de error
✅ **Limpieza de pantalla** - Interfaz limpia en cada operación

---

## 🔍 FLUJO DE TRABAJO TÍPICO

```
1. Ejecutar el programa
   └─> mvn exec:java -Dexec.mainClass="py.com.nikosoft.depositos.controller.CategoriaMenu"

2. Ver menú principal
   └─> Selecciona una opción (1-7 o 0)

3. Realizar operación
   ├─ Agregar: Ingresa descripción
   ├─ Ver todas: Se muestra tabla
   ├─ Buscar: Ingresa criterio
   ├─ Actualizar: Ingresa ID y nueva descripción
   └─ Eliminar: Ingresa ID y confirma

4. Vuelve al menú
   └─> Selecciona otra opción o presiona 0 para salir
```

---

## 💻 TECNOLOGÍA UTILIZADA

- **Java 11** - Lenguaje
- **Scanner** - Captura de entrada del usuario
- **CategoriaDAO** - Operaciones con base de datos
- **PostgreSQL** - Base de datos
- **ProcessBuilder** - Limpieza de pantalla

---

## 📝 NOTAS IMPORTANTES

- El programa se conecta automáticamente a PostgreSQL
- Crea la tabla `categoria` si no existe
- Todos los cambios se guardan en la base de datos
- La búsqueda por descripción es **insensible a mayúsculas/minúsculas**
- Presiona ENTER después de cada operación para continuar

---

## 🆘 ERRORES COMUNES

### "Error al inicializar el sistema"
**Causa:** PostgreSQL no está ejecutándose o credenciales incorrectas
**Solución:** Verificar que PostgreSQL esté ejecutándose en localhost:5432

### "ID inválido. Debe ser un número"
**Causa:** Se ingresó un valor no numérico
**Solución:** Ingresar solo números para el ID

### "La descripción no puede estar vacía"
**Causa:** Se intentó ingresar una descripción vacía
**Solución:** Ingresar una descripción con al menos un carácter

### "Categoría no encontrada"
**Causa:** El ID ingresado no existe
**Solución:** Verificar el ID o usar opción 2 para ver todas las categorías

---

## 📊 EJEMPLO COMPLETO DE SESIÓN

```
Paso 1: Ejecutar programa
$ mvn exec:java -Dexec.mainClass="py.com.nikosoft.depositos.controller.CategoriaMenu"

Paso 2: Seleccionar opción 1 (Agregar)
Opción: 1
Descripción: Electrónica
✓ Categoría agregada exitosamente (ID: 1)

Paso 3: Seleccionar opción 1 (Agregar otra)
Opción: 1
Descripción: Ropa
✓ Categoría agregada exitosamente (ID: 2)

Paso 4: Seleccionar opción 2 (Ver todas)
Opción: 2
[Se muestra tabla con 2 categorías]

Paso 5: Seleccionar opción 4 (Buscar)
Opción: 4
Término: Electró
[Se encuentra y muestra la categoría Electrónica]

Paso 6: Seleccionar opción 5 (Actualizar)
Opción: 5
ID: 1
Nueva descripción: Electrónica y Gadgets
✓ Categoría actualizada exitosamente

Paso 7: Seleccionar opción 0 (Salir)
Opción: 0
✓ ¡Hasta luego!
```

---

## 🔄 PARÁMETROS DE ENTRADA

| Opción | Parámetro | Tipo | Requerido |
|--------|-----------|------|----------|
| 1 | Descripción | String | Sí |
| 3 | ID | Integer | Sí |
| 4 | Término búsqueda | String | Sí |
| 5 | ID + Nueva descripción | Integer + String | Sí |
| 6 | ID + Confirmación | Integer + Char | Sí |

---

**Versión:** 1.0.0  
**Última actualización:** Julio 2026  
**Estado:** ✅ Completo y listo para usar

