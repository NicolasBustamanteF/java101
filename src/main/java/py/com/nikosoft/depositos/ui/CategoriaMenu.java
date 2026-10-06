  package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.CategoriaDAO;
import py.com.nikosoft.depositos.entities.Categoria;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * CategoriaMenu - Interfaz de consola para gestionar categorías
 * Proporciona un menú interactivo para realizar operaciones CRUD
 */
public class CategoriaMenu {

    private CategoriaDAO dao;
    private Scanner scanner;
    private boolean ejecutando;

    /**
     * Constructor del menú
     */
    public CategoriaMenu() {
        this.dao = new CategoriaDAO();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    /**
     * Método principal para ejecutar el menú
     */
    public void ejecutar() {
        try {
            // Crear tabla si no existe
            dao.createTable();
            System.out.println("✓ Sistema de Categorías inicializado\n");

            // Mostrar menú hasta que el usuario salga
            while (ejecutando) {
                mostrarMenu();
                procesarOpcion();
            }

            System.out.println("\n✓ ¡Hasta luego!");
            scanner.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar el sistema: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GESTIÓN DE CATEGORÍAS                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1️⃣  Agregar nueva categoría");
        System.out.println("  2️⃣  Ver todas las categorías");
        System.out.println("  3️⃣  Buscar categoría por ID");
        System.out.println("  4️⃣  Buscar categoría por descripción");
        System.out.println("  5️⃣  Actualizar categoría");
        System.out.println("  6️⃣  Eliminar categoría");
        System.out.println("  7️⃣  Ver total de categorías");
        System.out.println("  0️⃣  Salir");
        System.out.println();
        System.out.print("  Seleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada por el usuario
     */
    private void procesarOpcion() {
        try {
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    agregarCategoria();
                    break;
                case "2":
                    verTodasLasCategorias();
                    break;
                case "3":
                    buscarPorId();
                    break;
                case "4":
                    buscarPorDescripcion();
                    break;
                case "5":
                    actualizarCategoria();
                    break;
                case "6":
                    eliminarCategoria();
                    break;
                case "7":
                    verTotal();
                    break;
                case "0":
                    ejecutando = false;
                    break;
                default:
                    System.out.println("\n✗ Opción no válida. Intente de nuevo.");
                    pausa();
            }
        } catch (SQLException e) {
            System.err.println("\n✗ Error en la operación: " + e.getMessage());
            pausa();
        } catch (Exception e) {
            System.err.println("\n✗ Error: " + e.getMessage());
            pausa();
        }
    }

    /**
     * Opción 1: Agregar nueva categoría
     */
    private void agregarCategoria() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  AGREGAR NUEVA CATEGORÍA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese la descripción de la categoría: ");
        String descripcion = scanner.nextLine().trim();

        if (descripcion.isEmpty()) {
            System.out.println("\n✗ La descripción no puede estar vacía.");
            pausa();
            return;
        }

        int idCategoria = dao.add(new Categoria(descripcion));

        if (idCategoria != -1) {
            System.out.println("\n✓ Categoría agregada exitosamente");
            System.out.println("  ID: " + idCategoria);
            System.out.println("  Descripción: " + descripcion);
        } else {
            System.out.println("\n✗ Error al agregar la categoría.");
        }

        pausa();
    }

    /**
     * Opción 2: Ver todas las categorías
     */
    private void verTodasLasCategorias() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TODAS LAS CATEGORÍAS");
        System.out.println("─────────────────────────────────────────────────────────\n");

        List<Categoria> categorias = dao.getAll();

        if (categorias.isEmpty()) {
            System.out.println("  ℹ No hay categorías registradas.");
        } else {
            System.out.println("  ┌─────┬──────────────────────────────────────┐");
            System.out.println("  │ ID  │ Descripción                          │");
            System.out.println("  ├─────┼──────────────────────────────────────┤");

            for (Categoria categoria : categorias) {
                String desc = categoria.getDescripcion();
                if (desc.length() > 36) {
                    desc = desc.substring(0, 33) + "...";
                }
                String linea = String.format("  │ %-3d │ %-36s │",
                    categoria.getIdCategoria(), desc);
                System.out.println(linea);
            }

            System.out.println("  └─────┴──────────────────────────────────────┘");
            System.out.println("\n  Total de categorías: " + categorias.size());
        }

        pausa();
    }

    /**
     * Opción 3: Buscar categoría por ID
     */
    private void buscarPorId() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR CATEGORÍA POR ID");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la categoría: ");

        try {
            int idCategoria = Integer.parseInt(scanner.nextLine().trim());

            Categoria categoria = dao.getById(idCategoria);

            if (categoria != null) {
                System.out.println("\n  ✓ Categoría encontrada:");
                System.out.println("    ID: " + categoria.getIdCategoria());
                System.out.println("    Descripción: " + categoria.getDescripcion());
            } else {
                System.out.println("\n  ✗ Categoría no encontrada con ID: " + idCategoria);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }

        pausa();
    }

    /**
     * Opción 4: Buscar categoría por descripción (con LIKE)
     */
    private void buscarPorDescripcion() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR CATEGORÍA POR DESCRIPCIÓN");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el texto a buscar (búsqueda parcial): ");
        String termino = scanner.nextLine().trim();

        if (termino.isEmpty()) {
            System.out.println("\n  ✗ El término de búsqueda no puede estar vacío.");
            pausa();
            return;
        }

        List<Categoria> resultados = dao.getByDescripcion(termino);

        System.out.println("\n  Búsqueda: \"" + termino + "\"");

        if (resultados.isEmpty()) {
            System.out.println("  ℹ No se encontraron categorías con ese término.");
        } else {
            System.out.println("  ✓ Se encontraron " + resultados.size() + " resultado(s):\n");
            System.out.println("  ┌─────┬──────────────────────────────────────┐");
            System.out.println("  │ ID  │ Descripción                          │");
            System.out.println("  ├─────┼──────────────────────────────────────┤");

            for (Categoria categoria : resultados) {
                String desc = categoria.getDescripcion();
                if (desc.length() > 36) {
                    desc = desc.substring(0, 33) + "...";
                }
                String linea = String.format("  │ %-3d │ %-36s │",
                    categoria.getIdCategoria(), desc);
                System.out.println(linea);
            }

            System.out.println("  └─────┴──────────────────────────────────────┘");
        }

        pausa();
    }

    /**
     * Opción 5: Actualizar categoría
     */
    private void actualizarCategoria() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ACTUALIZAR CATEGORÍA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la categoría a actualizar: ");

        try {
            int idCategoria = Integer.parseInt(scanner.nextLine().trim());

            // Verificar si existe
            Categoria actual = dao.getById(idCategoria);

            if (actual == null) {
                System.out.println("\n  ✗ Categoría no encontrada con ID: " + idCategoria);
                pausa();
                return;
            }

            System.out.println("\n  Descripción actual: " + actual.getDescripcion());
            System.out.print("  Ingrese la nueva descripción: ");
            String nuevaDescripcion = scanner.nextLine().trim();

            if (nuevaDescripcion.isEmpty()) {
                System.out.println("\n  ✗ La descripción no puede estar vacía.");
                pausa();
                return;
            }

            if (dao.update(new Categoria(idCategoria, nuevaDescripcion))) {
                System.out.println("\n  ✓ Categoría actualizada exitosamente");
                System.out.println("    ID: " + idCategoria);
                System.out.println("    Nueva descripción: " + nuevaDescripcion);
            } else {
                System.out.println("\n  ✗ Error al actualizar la categoría.");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }

        pausa();
    }

    /**
     * Opción 6: Eliminar categoría
     */
    private void eliminarCategoria() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ELIMINAR CATEGORÍA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la categoría a eliminar: ");

        try {
            int idCategoria = Integer.parseInt(scanner.nextLine().trim());

            // Verificar si existe
            Categoria categoria = dao.getById(idCategoria);

            if (categoria == null) {
                System.out.println("\n  ✗ Categoría no encontrada con ID: " + idCategoria);
                pausa();
                return;
            }

            System.out.println("\n  ¿Está seguro de que desea eliminar esta categoría?");
            System.out.println("  ID: " + categoria.getIdCategoria());
            System.out.println("  Descripción: " + categoria.getDescripcion());
            System.out.print("\n  Confirme (s/n): ");

            String confirmacion = scanner.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                if (dao.delete(idCategoria)) {
                    System.out.println("\n  ✓ Categoría eliminada exitosamente");
                } else {
                    System.out.println("\n  ✗ Error al eliminar la categoría.");
                }
            } else {
                System.out.println("\n  ℹ Operación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }

        pausa();
    }

    /**
     * Opción 7: Ver total de categorías
     */
    private void verTotal() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TOTAL DE CATEGORÍAS");
        System.out.println("─────────────────────────────────────────────────────────");

        int total = dao.count();
        System.out.println("\n  Total de categorías registradas: " + total);

        pausa();
    }

    /**
     * Limpia la pantalla
     */
    private void limpiarPantalla() {
        // Windows
        try {
            new ProcessBuilder("cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            // Linux/Mac o si falla, mostrar saltos de línea
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Pausa y espera que el usuario presione Enter
     */
    private void pausa() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Método main - Punto de entrada de la aplicación
     */
    public static void main(String[] args) {
        CategoriaMenu menu = new CategoriaMenu();
        menu.ejecutar();
    }
}

