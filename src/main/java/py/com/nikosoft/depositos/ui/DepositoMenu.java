package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.DepositoDAO;
import py.com.nikosoft.depositos.entities.Deposito;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DepositoMenu {

    private DepositoDAO dao;
    private Scanner scanner;
    private boolean ejecutando;

    public DepositoMenu() {
        this.dao = new DepositoDAO();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    public void ejecutar() {
        try {
            dao.createTable();
            System.out.println("✓ Sistema de Depósitos inicializado\n");

            while (ejecutando) {
                mostrarMenu();
                procesarOpcion();
            }

            System.out.println("\n✓ ¡Hasta luego!");
            scanner.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar el sistema de depósitos: " + e.getMessage());
        }
    }

    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GESTIÓN DE DEPÓSITOS                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1️⃣  Agregar nuevo depósito");
        System.out.println("  2️⃣  Ver todos los depósitos");
        System.out.println("  3️⃣  Buscar depósito por ID");
        System.out.println("  4️⃣  Buscar depósito por nombre");
        System.out.println("  5️⃣  Actualizar depósito");
        System.out.println("  6️⃣  Eliminar depósito");
        System.out.println("  7️⃣  Ver total de depósitos");
        System.out.println("  0️⃣  Salir");
        System.out.println();
        System.out.print("  Seleccione una opción: ");
    }

    private void procesarOpcion() {
        try {
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    agregarDeposito();
                    break;
                case "2":
                    verTodosLosDepositos();
                    break;
                case "3":
                    buscarPorId();
                    break;
                case "4":
                    buscarPorNombre();
                    break;
                case "5":
                    actualizarDeposito();
                    break;
                case "6":
                    eliminarDeposito();
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

    private void agregarDeposito() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  AGREGAR NUEVO DEPÓSITO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el nombre del depósito: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("\n✗ El nombre no puede estar vacío.");
            pausa();
            return;
        }

        System.out.print("  Ingrese la dirección (ENTER para omitir): ");
        String direccion = scanner.nextLine().trim();
        if (direccion.isEmpty()) {
            direccion = null;
        }

        int id = dao.add(new Deposito(0, nombre, direccion));
        if (id != -1) {
            System.out.println("\n✓ Depósito agregado exitosamente");
            System.out.println("  ID: " + id);
            System.out.println("  Nombre: " + nombre);
        } else {
            System.out.println("\n✗ Error al agregar el depósito.");
        }

        pausa();
    }

    private void verTodosLosDepositos() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TODOS LOS DEPÓSITOS");
        System.out.println("─────────────────────────────────────────────────────────\n");

        List<Deposito> depositos = dao.getAll();
        if (depositos.isEmpty()) {
            System.out.println("  ℹ No hay depósitos registrados.");
        } else {
            System.out.println("  ┌─────┬────────────────────────┬────────────────────────┐");
            System.out.println("  │ ID  │ Nombre                 │ Dirección               │");
            System.out.println("  ├─────┼────────────────────────┼────────────────────────┤");
            for (Deposito d : depositos) {
                String nombre = d.getNombre();
                if (nombre.length() > 22) nombre = nombre.substring(0, 19) + "...";
                String direccion = d.getDireccion() == null ? "-" : d.getDireccion();
                if (direccion.length() > 22) direccion = direccion.substring(0, 19) + "...";
                System.out.printf("  │ %-3d │ %-22s │ %-22s │\n", d.getIdDeposito(), nombre, direccion);
            }
            System.out.println("  └─────┴────────────────────────┴────────────────────────┘");
            System.out.println("\n  Total de depósitos: " + depositos.size());
        }
        pausa();
    }

    private void buscarPorId() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR DEPÓSITO POR ID");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del depósito: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Deposito d = dao.getById(id);
            if (d != null) {
                System.out.println("\n  ✓ Depósito encontrado:");
                System.out.println("    ID: " + d.getIdDeposito());
                System.out.println("    Nombre: " + d.getNombre());
                System.out.println("    Dirección: " + (d.getDireccion() == null ? "(sin especificar)" : d.getDireccion()));
            } else {
                System.out.println("\n  ✗ Depósito no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void buscarPorNombre() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR DEPÓSITO POR NOMBRE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el texto a buscar (búsqueda parcial): ");
        String termino = scanner.nextLine().trim();
        if (termino.isEmpty()) {
            System.out.println("\n  ✗ El término de búsqueda no puede estar vacío.");
            pausa();
            return;
        }
        List<Deposito> resultados = dao.getByNombre(termino);
        if (resultados.isEmpty()) {
            System.out.println("\n  ℹ No se encontraron depósitos con ese término.");
        } else {
            System.out.println("\n  ✓ Se encontraron " + resultados.size() + " resultado(s):\n");
            System.out.println("  ┌─────┬────────────────────────┬────────────────────────┐");
            System.out.println("  │ ID  │ Nombre                 │ Dirección               │");
            System.out.println("  ├─────┼────────────────────────┼────────────────────────┤");
            for (Deposito d : resultados) {
                String nombre = d.getNombre();
                if (nombre.length() > 22) nombre = nombre.substring(0, 19) + "...";
                String direccion = d.getDireccion() == null ? "-" : d.getDireccion();
                if (direccion.length() > 22) direccion = direccion.substring(0, 19) + "...";
                System.out.printf("  │ %-3d │ %-22s │ %-22s │\n", d.getIdDeposito(), nombre, direccion);
            }
            System.out.println("  └─────┴────────────────────────┴────────────────────────┘");
        }
        pausa();
    }

    private void actualizarDeposito() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ACTUALIZAR DEPÓSITO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del depósito a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Deposito actual = dao.getById(id);
            if (actual == null) {
                System.out.println("\n  ✗ Depósito no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  Nombre actual: " + actual.getNombre());
            System.out.print("  Nuevo nombre (ENTER para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (nuevoNombre.isEmpty()) nuevoNombre = actual.getNombre();

            System.out.println("  Dirección actual: " + (actual.getDireccion() == null ? "(sin especificar)" : actual.getDireccion()));
            System.out.print("  Nueva dirección (ENTER para mantener): ");
            String nuevaDireccion = scanner.nextLine().trim();
            if (nuevaDireccion.isEmpty()) nuevaDireccion = actual.getDireccion();

            if (dao.update(new Deposito(id, nuevoNombre, nuevaDireccion))) {
                System.out.println("\n  ✓ Depósito actualizado exitosamente");
            } else {
                System.out.println("\n  ✗ Error al actualizar el depósito.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void eliminarDeposito() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ELIMINAR DEPÓSITO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del depósito a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Deposito d = dao.getById(id);
            if (d == null) {
                System.out.println("\n  ✗ Depósito no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  ¿Está seguro de que desea eliminar este depósito?");
            System.out.println("  ID: " + d.getIdDeposito());
            System.out.println("  Nombre: " + d.getNombre());
            System.out.print("\n  Confirme (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("s") || confirm.equals("si")) {
                if (dao.delete(id)) {
                    System.out.println("\n  ✓ Depósito eliminado exitosamente");
                } else {
                    System.out.println("\n  ✗ Error al eliminar el depósito.");
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
        System.out.println("  TOTAL DE DEPÓSITOS");
        System.out.println("─────────────────────────────────────────────────────────");

        int total = dao.count();
        System.out.println("\n  Total de depósitos registrados: " + total);
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
        DepositoMenu menu = new DepositoMenu();
        menu.ejecutar();
    }
}
