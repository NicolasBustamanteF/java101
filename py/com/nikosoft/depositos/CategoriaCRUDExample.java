package py.com.nikosoft.depositos;

import py.com.nikosoft.depositos.boundary.CategoriaDAO;
import py.com.nikosoft.depositos.entities.Categoria;

import java.sql.SQLException;
import java.util.List;

/**
 * CategoriaCRUDExample - Ejemplo de uso del CategoriaDAO
 * Demuestra todas las operaciones CRUD (Create, Read, Update, Delete)
 */
public class CategoriaCRUDExample {

    public static void main(String[] args) {
        CategoriaDAO dao = new CategoriaDAO();

        try {
            System.out.println("═════════════════════════════════════════════════════════");
            System.out.println("       EJEMPLO DE OPERACIONES CRUD CON CATEGORÍAS");
            System.out.println("═════════════════════════════════════════════════════════\n");

            // 1. CREAR TABLA
            System.out.println("1️⃣ CREAR TABLA (si no existe)");
            System.out.println("─────────────────────────────────────────────────────────");
            dao.createTable();
            System.out.println();

            // 2. LIMPIAR DATOS PREVIOS (para ejemplo limpio)
            System.out.println("2️⃣ LIMPIAR DATOS PREVIOS");
            System.out.println("─────────────────────────────────────────────────────────");
            dao.deleteAll();
            System.out.println();

            // 3. ADD - AGREGAR NUEVAS CATEGORÍAS
            System.out.println("3️⃣ AGREGAR (ADD) - Insertar nuevas categorías");
            System.out.println("─────────────────────────────────────────────────────────");
            int id1 = dao.add("Electrónica");
            int id2 = dao.add("Ropa y Accesorios");
            int id3 = dao.add("Hogar y Decoración");
            int id4 = dao.add("Libros y Material Educativo");
            int id5 = dao.add("Deportes y Recreación");
            System.out.println();

            // 4. SELECT - OBTENER TODAS LAS CATEGORÍAS
            System.out.println("4️⃣ CONSULTAR (SELECT) - Obtener todas las categorías");
            System.out.println("─────────────────────────────────────────────────────────");
            List<Categoria> todasLasCategorias = dao.select();
            System.out.println("Categorías encontradas:");
            for (Categoria cat : todasLasCategorias) {
                System.out.println("  [" + cat.getIdCategoria() + "] " + cat.getDescripcion());
            }
            System.out.println();

            // 5. SELECT BY ID - OBTENER UNA CATEGORÍA ESPECÍFICA
            System.out.println("5️⃣ CONSULTAR POR ID (SELECT BY ID)");
            System.out.println("─────────────────────────────────────────────────────────");
            Categoria categoria = dao.selectById(id1);
            if (categoria != null) {
                System.out.println("Categoría encontrada: [" + categoria.getIdCategoria() + "] " +
                                 categoria.getDescripcion());
            }
            System.out.println();

            // 6. SELECT BY DESCRIPCION - BÚSQUEDA CON LIKE
            System.out.println("6️⃣ BÚSQUEDA POR DESCRIPCIÓN (SELECT BY DESCRIPCION - LIKE)");
            System.out.println("─────────────────────────────────────────────────────────");
            System.out.println("Búsqueda: 'Electró' (búsqueda parcial)");
            List<Categoria> resultadosElectro = dao.selectByDescripcion("Electró");
            for (Categoria cat : resultadosElectro) {
                System.out.println("  [" + cat.getIdCategoria() + "] " + cat.getDescripcion());
            }
            System.out.println();

            System.out.println("Búsqueda: 'Material' (búsqueda parcial)");
            List<Categoria> resultadosMaterial = dao.selectByDescripcion("Material");
            for (Categoria cat : resultadosMaterial) {
                System.out.println("  [" + cat.getIdCategoria() + "] " + cat.getDescripcion());
            }
            System.out.println();

            // 7. UPDATE - ACTUALIZAR UNA CATEGORÍA
            System.out.println("7️⃣ ACTUALIZAR (UPDATE) - Modificar una categoría");
            System.out.println("─────────────────────────────────────────────────────────");
            System.out.println("Actualizando categoría " + id2 + " de: 'Ropa y Accesorios'");
            boolean actualizado = dao.update(id2, "Vestuario y Accesorios Personales");
            System.out.println("Nueva descripción: 'Vestuario y Accesorios Personales'");
            System.out.println();

            // 8. VERIFICAR LA ACTUALIZACIÓN
            System.out.println("8️⃣ VERIFICAR ACTUALIZACIÓN");
            System.out.println("─────────────────────────────────────────────────────────");
            Categoria categoriaActualizada = dao.selectById(id2);
            if (categoriaActualizada != null) {
                System.out.println("Categoría actualizada: [" + categoriaActualizada.getIdCategoria() + "] " +
                                 categoriaActualizada.getDescripcion());
            }
            System.out.println();

            // 9. DELETE - ELIMINAR UNA CATEGORÍA
            System.out.println("9️⃣ ELIMINAR (DELETE) - Remover una categoría");
            System.out.println("─────────────────────────────────────────────────────────");
            System.out.println("Eliminando categoría con ID: " + id5);
            boolean eliminado = dao.delete(id5);
            System.out.println();

            // 10. VERIFICAR LA ELIMINACIÓN
            System.out.println("🔟 VERIFICAR ELIMINACIÓN");
            System.out.println("─────────────────────────────────────────────────────────");
            System.out.println("Intentando obtener categoría eliminada (ID: " + id5 + ")");
            Categoria categoriaEliminada = dao.selectById(id5);
            System.out.println();

            // 11. ESTADO FINAL
            System.out.println("1️⃣1️⃣ ESTADO FINAL");
            System.out.println("─────────────────────────────────────────────────────────");
            int total = dao.getTotalCategorias();
            System.out.println("Categorías restantes: " + total);
            System.out.println();

            // 12. LISTAR TODAS LAS CATEGORÍAS FINALES
            System.out.println("1️⃣2️⃣ LISTADO FINAL DE CATEGORÍAS");
            System.out.println("─────────────────────────────────────────────────────────");
            List<Categoria> categoriasFinales = dao.select();
            for (Categoria cat : categoriasFinales) {
                System.out.println("  [" + cat.getIdCategoria() + "] " + cat.getDescripcion());
            }
            System.out.println();

            // 13. PRUEBA DE BÚSQUEDA AVANZADA
            System.out.println("1️⃣3️⃣ BÚSQUEDAS ADICIONALES (LIKE)");
            System.out.println("─────────────────────────────────────────────────────────");
            System.out.println("Búsqueda: 'y' (letra 'y')");
            List<Categoria> resultadosY = dao.selectByDescripcion("y");
            for (Categoria cat : resultadosY) {
                System.out.println("  [" + cat.getIdCategoria() + "] " + cat.getDescripcion());
            }
            System.out.println();

            System.out.println("═════════════════════════════════════════════════════════");
            System.out.println("         ✓ EJEMPLO COMPLETADO EXITOSAMENTE");
            System.out.println("═════════════════════════════════════════════════════════");

        } catch (SQLException e) {
            System.err.println("✗ Error en la operación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

