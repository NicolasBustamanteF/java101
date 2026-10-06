package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.MarcaDAO;
import py.com.nikosoft.depositos.entities.Marca;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MarcaMenu {

    private MarcaDAO dao;
    private Scanner scanner;
    private boolean ejecutando;

    public MarcaMenu() {
        this.dao = new MarcaDAO();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    public void ejecutar() {
        try {
            dao.createTable();
            System.out.println("✓ Sistema de Marcas inicializado\n");

            while (ejecutando) {
                mostrarMenu();
                procesarOpcion();
            }

            System.out.println("\n✓ ¡Hasta luego!");
            scanner.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar el sistema de marcas: " + e.getMessage());
        }
    }

    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GESTIÓN DE MARCAS                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1️⃣  Agregar nueva marca");
        System.out.println("  2️⃣  Ver todas las marcas");
        System.out.println("  3️⃣  Buscar marca por ID");
        System.out.println("  4️⃣  Buscar marca por nombre");
        System.out.println("  5️⃣  Actualizar marca");
        System.out.println("  6️⃣  Eliminar marca");
        System.out.println("  7️⃣  Ver total de marcas");
        System.out.println("  0️⃣  Salir");
        System.out.println();
        System.out.print("  Seleccione una opción: ");
    }

    private void procesarOpcion() {
        try {
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    agregarMarca();
                    break;
                case "2":
                    verTodasLasMarcas();
                    break;
                case "3":
                    buscarPorId();
                    break;
                case "4":
                    buscarPorNombre();
                    break;
                case "5":
                    actualizarMarca();
                    break;
                case "6":
                    eliminarMarca();
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

    private void agregarMarca() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  AGREGAR NUEVA MARCA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el nombre de la marca: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("\n✗ El nombre no puede estar vacío.");
            pausa();
            return;
        }

        int id = dao.add(new Marca(0, nombre));
        if (id != -1) {
            System.out.println("\n✓ Marca agregada exitosamente");
            System.out.println("  ID: " + id);
            System.out.println("  Nombre: " + nombre);
        } else {
            System.out.println("\n✗ Error al agregar la marca.");
        }

        pausa();
    }

    private void verTodasLasMarcas() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TODAS LAS MARCAS");
        System.out.println("─────────────────────────────────────────────────────────\n");

        List<Marca> marcas = dao.getAll();
        if (marcas.isEmpty()) {
            System.out.println("  ℹ No hay marcas registradas.");
        } else {
            System.out.println("  ┌─────┬──────────────────────────────────────┐");
            System.out.println("  │ ID  │ Nombre                               │");
            System.out.println("  ├─────┼──────────────────────────────────────┤");
            for (Marca m : marcas) {
                String nombre = m.getNombre();
                if (nombre.length() > 36) {
                    nombre = nombre.substring(0, 33) + "...";
                }
                System.out.printf("  │ %-3d │ %-36s │\n", m.getIdMarca(), nombre);
            }
            System.out.println("  └─────┴──────────────────────────────────────┘");
            System.out.println("\n  Total de marcas: " + marcas.size());
        }
        pausa();
    }

    private void buscarPorId() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR MARCA POR ID");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la marca: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Marca m = dao.getById(id);
            if (m != null) {
                System.out.println("\n  ✓ Marca encontrada:");
                System.out.println("    ID: " + m.getIdMarca());
                System.out.println("    Nombre: " + m.getNombre());
            } else {
                System.out.println("\n  ✗ Marca no encontrada con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void buscarPorNombre() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR MARCA POR NOMBRE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el texto a buscar (búsqueda parcial): ");
        String termino = scanner.nextLine().trim();
        if (termino.isEmpty()) {
            System.out.println("\n  ✗ El término de búsqueda no puede estar vacío.");
            pausa();
            return;
        }
        List<Marca> resultados = dao.getByNombre(termino);
        if (resultados.isEmpty()) {
            System.out.println("\n  ℹ No se encontraron marcas con ese término.");
        } else {
            System.out.println("\n  ✓ Se encontraron " + resultados.size() + " resultado(s):\n");
            System.out.println("  ┌─────┬──────────────────────────────────────┐");
            System.out.println("  │ ID  │ Nombre                               │");
            System.out.println("  ├─────┼──────────────────────────────────────┤");
            for (Marca m : resultados) {
                String nombre = m.getNombre();
                if (nombre.length() > 36) {
                    nombre = nombre.substring(0, 33) + "...";
                }
                System.out.printf("  │ %-3d │ %-36s │\n", m.getIdMarca(), nombre);
            }
            System.out.println("  └─────┴──────────────────────────────────────┘");
        }
        pausa();
    }

    private void actualizarMarca() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ACTUALIZAR MARCA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la marca a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Marca actual = dao.getById(id);
            if (actual == null) {
                System.out.println("\n  ✗ Marca no encontrada con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  Nombre actual: " + actual.getNombre());
            System.out.print("  Ingrese el nuevo nombre: ");
            String nuevo = scanner.nextLine().trim();
            if (nuevo.isEmpty()) {
                System.out.println("\n  ✗ El nombre no puede estar vacío.");
                pausa();
                return;
            }
            if (dao.update(new Marca(id, nuevo))) {
                System.out.println("\n  ✓ Marca actualizada exitosamente");
            } else {
                System.out.println("\n  ✗ Error al actualizar la marca.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void eliminarMarca() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ELIMINAR MARCA");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID de la marca a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Marca m = dao.getById(id);
            if (m == null) {
                System.out.println("\n  ✗ Marca no encontrada con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  ¿Está seguro de que desea eliminar esta marca?");
            System.out.println("  ID: " + m.getIdMarca());
            System.out.println("  Nombre: " + m.getNombre());
            System.out.print("\n  Confirme (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("s") || confirm.equals("si")) {
                if (dao.delete(id)) {
                    System.out.println("\n  ✓ Marca eliminada exitosamente");
                } else {
                    System.out.println("\n  ✗ Error al eliminar la marca.");
                }
            } else {
                System.out.println("\n  ℹ Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void verTotal() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TOTAL DE MARCAS");
        System.out.println("─────────────────────────────────────────────────────────");

        int total = dao.count();
        System.out.println("\n  Total de marcas registradas: " + total);
        pausa();
    }

    private void limpiarPantalla() {
        try {
            new ProcessBuilder("cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private void pausa() {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        MarcaMenu menu = new MarcaMenu();
        menu.ejecutar();
    }
}

