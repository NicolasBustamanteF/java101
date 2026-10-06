package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.DepositoDAO;
import py.com.nikosoft.depositos.boundary.EstanteDAO;
import py.com.nikosoft.depositos.entities.Deposito;
import py.com.nikosoft.depositos.entities.Estante;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EstanteMenu {

    private EstanteDAO estanteDAO;
    private DepositoDAO depositoDAO;
    private Scanner scanner;
    private boolean ejecutando;

    public EstanteMenu() {
        this.estanteDAO = new EstanteDAO();
        this.depositoDAO = new DepositoDAO();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    public void ejecutar() {
        try {
            estanteDAO.createTable();
            System.out.println("✓ Sistema de Estantes inicializado\n");

            while (ejecutando) {
                mostrarMenu();
                procesarOpcion();
            }

            System.out.println("\n✓ ¡Hasta luego!");
            scanner.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar el sistema de estantes: " + e.getMessage());
        }
    }

    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GESTIÓN DE ESTANTES                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1️⃣  Agregar nuevo estante");
        System.out.println("  2️⃣  Ver todos los estantes");
        System.out.println("  3️⃣  Buscar estante por ID");
        System.out.println("  4️⃣  Buscar estante por código");
        System.out.println("  5️⃣  Actualizar estante");
        System.out.println("  6️⃣  Eliminar estante");
        System.out.println("  7️⃣  Asignar/Remover depósito a estante");
        System.out.println("  8️⃣  Ver total de estantes");
        System.out.println("  0️⃣  Salir");
        System.out.println();
        System.out.print("  Seleccione una opción: ");
    }

    private void procesarOpcion() {
        try {
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1":
                    agregarEstante();
                    break;
                case "2":
                    verTodosLosEstantes();
                    break;
                case "3":
                    buscarPorId();
                    break;
                case "4":
                    buscarPorCodigo();
                    break;
                case "5":
                    actualizarEstante();
                    break;
                case "6":
                    eliminarEstante();
                    break;
                case "7":
                    asignarDeposito();
                    break;
                case "8":
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

    private void agregarEstante() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  AGREGAR NUEVO ESTANTE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Código del estante: ");
        String codigo = scanner.nextLine().trim();
        if (codigo.isEmpty()) {
            System.out.println("\n✗ El código no puede estar vacío.");
            pausa();
            return;
        }

        System.out.println("\n  ¿Desea asignar un depósito ahora? (s/n): ");
        String want = scanner.nextLine().trim().toLowerCase();
        Integer idDeposito = null;
        if (want.equals("s") || want.equals("si")) {
            List<Deposito> depositos = depositoDAO.getAll();
            if (depositos.isEmpty()) {
                System.out.println("\n  ℹ No hay depósitos disponibles. Cree un depósito primero.");
                pausa();
            } else {
                System.out.println("\n  Depósitos disponibles:");
                for (Deposito d : depositos) {
                    System.out.println("    [" + d.getIdDeposito() + "] " + d.getNombre());
                }
                System.out.print("\n  Ingrese ID del depósito a asignar (o ENTER para omitir): ");
                String idStr = scanner.nextLine().trim();
                if (!idStr.isEmpty()) {
                    try {
                        idDeposito = Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        System.out.println("\n✗ ID inválido. No se asignará depósito.");
                        idDeposito = null;
                    }
                }
            }
        }

        int idEst = estanteDAO.add(new Estante(0, codigo, idDeposito));
        if (idEst != -1) {
            System.out.println("\n✓ Estante agregado exitosamente (ID: " + idEst + ")");
        } else {
            System.out.println("\n✗ Error al agregar estante.");
        }
        pausa();
    }

    private void verTodosLosEstantes() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TODOS LOS ESTANTES");
        System.out.println("─────────────────────────────────────────────────────────\n");

        List<Estante> estantes = estanteDAO.getAll();
        if (estantes.isEmpty()) {
            System.out.println("  ℹ No hay estantes registrados.");
        } else {
            System.out.println("  ┌─────┬────────────────────────────┬──────────┐");
            System.out.println("  │ ID  │ Código                     │ Depósito │");
            System.out.println("  ├─────┼────────────────────────────┼──────────┤");
            for (Estante e : estantes) {
                String codigo = e.getCodigo();
                if (codigo.length() > 28) codigo = codigo.substring(0, 25) + "...";
                String depositoStr = e.getIdDeposito() == null ? "-" : String.valueOf(e.getIdDeposito());
                System.out.printf("  │ %-3d │ %-26s │ %-8s │\n", e.getIdEstante(), codigo, depositoStr);
            }
            System.out.println("  └─────┴────────────────────────────┴──────────┘");
            System.out.println("\n  Total de estantes: " + estantes.size());
        }
        pausa();
    }

    private void buscarPorId() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR ESTANTE POR ID");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del estante: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Estante e = estanteDAO.getById(id);
            if (e != null) {
                System.out.println("\n  ✓ Estante encontrado:");
                System.out.println("    ID: " + e.getIdEstante());
                System.out.println("    Código: " + e.getCodigo());
                System.out.println("    ID Depósito: " + (e.getIdDeposito() == null ? "(sin asignar)" : e.getIdDeposito()));
            } else {
                System.out.println("\n  ✗ Estante no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void buscarPorCodigo() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR ESTANTE POR CÓDIGO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el texto a buscar (búsqueda parcial): ");
        String termino = scanner.nextLine().trim();
        if (termino.isEmpty()) {
            System.out.println("\n  ✗ El término de búsqueda no puede estar vacío.");
            pausa();
            return;
        }
        List<Estante> resultados = estanteDAO.getByCodigo(termino);
        if (resultados.isEmpty()) {
            System.out.println("\n  ℹ No se encontraron estantes con ese término.");
        } else {
            System.out.println("\n  ✓ Se encontraron " + resultados.size() + " resultado(s):\n");
            System.out.println("  ┌─────┬────────────────────────────┬──────────┐");
            System.out.println("  │ ID  │ Código                     │ Depósito │");
            System.out.println("  ├─────┼────────────────────────────┼──────────┤");
            for (Estante e : resultados) {
                String codigo = e.getCodigo();
                if (codigo.length() > 28) codigo = codigo.substring(0, 25) + "...";
                String depositoStr = e.getIdDeposito() == null ? "-" : String.valueOf(e.getIdDeposito());
                System.out.printf("  │ %-3d │ %-26s │ %-8s │\n", e.getIdEstante(), codigo, depositoStr);
            }
            System.out.println("  └─────┴────────────────────────────┴──────────┘");
        }
        pausa();
    }

    private void actualizarEstante() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ACTUALIZAR ESTANTE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del estante a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Estante actual = estanteDAO.getById(id);
            if (actual == null) {
                System.out.println("\n  ✗ Estante no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  Código actual: " + actual.getCodigo());
            System.out.print("  Nuevo código (ENTER para mantener): ");
            String nuevoCodigo = scanner.nextLine().trim();
            if (nuevoCodigo.isEmpty()) nuevoCodigo = actual.getCodigo();

            Integer idDeposito = actual.getIdDeposito();
            System.out.println("  ID Depósito actual: " + (idDeposito == null ? "(sin asignar)" : idDeposito));
            System.out.print("  Nuevo ID Depósito (ENTER para mantener / 0 para remover): ");
            String depositoInput = scanner.nextLine().trim();
            if (!depositoInput.isEmpty()) {
                try {
                    int d = Integer.parseInt(depositoInput);
                    if (d == 0) idDeposito = null;
                    else idDeposito = d;
                } catch (NumberFormatException e) {
                    System.out.println("\n✗ ID Depósito inválido. Operación cancelada.");
                    pausa();
                    return;
                }
            }

            if (estanteDAO.update(new Estante(id, nuevoCodigo, idDeposito))) {
                System.out.println("\n  ✓ Estante actualizado exitosamente");
            } else {
                System.out.println("\n  ✗ Error al actualizar el estante.");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void eliminarEstante() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ELIMINAR ESTANTE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del estante a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Estante e = estanteDAO.getById(id);
            if (e == null) {
                System.out.println("\n  ✗ Estante no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  ¿Está seguro de que desea eliminar este estante?");
            System.out.println("  ID: " + e.getIdEstante());
            System.out.println("  Código: " + e.getCodigo());
            System.out.print("\n  Confirme (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("s") || confirm.equals("si")) {
                if (estanteDAO.delete(id)) {
                    System.out.println("\n  ✓ Estante eliminado exitosamente");
                } else {
                    System.out.println("\n  ✗ Error al eliminar el estante.");
                }
            } else {
                System.out.println("\n  ℹ Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void asignarDeposito() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ASIGNAR/REMOVER DEPÓSITO A ESTANTE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del estante: ");
        try {
            int idEst = Integer.parseInt(scanner.nextLine().trim());
            Estante e = estanteDAO.getById(idEst);
            if (e == null) {
                System.out.println("\n  ✗ Estante no encontrado con ID: " + idEst);
                pausa();
                return;
            }

            System.out.println("\n  Estante: " + e.getCodigo() + " (ID: " + e.getIdEstante() + ")");
            List<Deposito> depositos = depositoDAO.getAll();
            if (depositos.isEmpty()) {
                System.out.println("\n  ℹ No hay depósitos disponibles. Cree un depósito primero.");
                pausa();
                return;
            }

            System.out.println("\n  Depósitos disponibles:");
            for (Deposito d : depositos) {
                System.out.println("    [" + d.getIdDeposito() + "] " + d.getNombre());
            }
            System.out.print("\n  Ingrese ID del depósito a asignar (0 para remover): ");
            int idDeposito = Integer.parseInt(scanner.nextLine().trim());
            Integer idDepositoObj = idDeposito == 0 ? null : idDeposito;
            if (estanteDAO.assignDeposito(idEst, idDepositoObj)) {
                System.out.println("\n  ✓ Depósito asignado/removido correctamente");
            } else {
                System.out.println("\n  ✗ Error al asignar/remover depósito.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void verTotal() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TOTAL DE ESTANTES");
        System.out.println("─────────────────────────────────────────────────────────");

        int total = estanteDAO.count();
        System.out.println("\n  Total de estantes registrados: " + total);
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
        EstanteMenu menu = new EstanteMenu();
        menu.ejecutar();
    }
}
