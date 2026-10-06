package py.com.nikosoft.depositos.ui;

import py.com.nikosoft.depositos.boundary.MarcaDAO;
import py.com.nikosoft.depositos.boundary.ProductoDAO;
import py.com.nikosoft.depositos.entities.Marca;
import py.com.nikosoft.depositos.entities.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductoMenu {

    private ProductoDAO productoDAO;
    private MarcaDAO marcaDAO;
    private Scanner scanner;
    private boolean ejecutando;

    public ProductoMenu() {
        this.productoDAO = new ProductoDAO();
        this.marcaDAO = new MarcaDAO();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    public void ejecutar() {
        try {
            // Crear tablas si no existen
            productoDAO.createTable();
            System.out.println("✓ Sistema de Productos inicializado\n");

            while (ejecutando) {
                mostrarMenu();
                procesarOpcion();
            }

            System.out.println("\n✓ ¡Hasta luego!");
            scanner.close();

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar el sistema de productos: " + e.getMessage());
        }
    }

    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE GESTIÓN DE PRODUCTOS                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1️⃣  Agregar nuevo producto");
        System.out.println("  2️⃣  Ver todos los productos");
        System.out.println("  3️⃣  Buscar producto por ID");
        System.out.println("  4️⃣  Buscar producto por nombre");
        System.out.println("  5️⃣  Actualizar producto");
        System.out.println("  6️⃣  Eliminar producto");
        System.out.println("  7️⃣  Asignar/Remover marca a producto");
        System.out.println("  8️⃣  Ver total de productos");
        System.out.println("  0️⃣  Salir");
        System.out.println();
        System.out.print("  Seleccione una opción: ");
    }

    private void procesarOpcion() {
        try {
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1":
                    agregarProducto();
                    break;
                case "2":
                    verTodosLosProductos();
                    break;
                case "3":
                    buscarPorId();
                    break;
                case "4":
                    buscarPorNombre();
                    break;
                case "5":
                    actualizarProducto();
                    break;
                case "6":
                    eliminarProducto();
                    break;
                case "7":
                    asignarMarca();
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

    private void agregarProducto() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  AGREGAR NUEVO PRODUCTO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Nombre del producto: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("\n✗ El nombre no puede estar vacío.");
            pausa();
            return;
        }

        System.out.print("  Precio: ");
        double precio = 0;
        try {
            precio = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\n✗ Precio inválido. Use un número válido.");
            pausa();
            return;
        }

        System.out.println("\n  ¿Desea asignar una marca ahora? (s/n): ");
        String want = scanner.nextLine().trim().toLowerCase();
        Integer idMarca = null;
        if (want.equals("s") || want.equals("si")) {
            List<Marca> marcas = marcaDAO.getAll();
            if (marcas.isEmpty()) {
                System.out.println("\n  ℹ No hay marcas disponibles. Cree una marca primero.");
                pausa();
            } else {
                System.out.println("\n  Marcas disponibles:");
                for (Marca m : marcas) {
                    System.out.println("    [" + m.getIdMarca() + "] " + m.getNombre());
                }
                System.out.print("\n  Ingrese ID de la marca a asignar (o ENTER para omitir): ");
                String idStr = scanner.nextLine().trim();
                if (!idStr.isEmpty()) {
                    try {
                        idMarca = Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        System.out.println("\n✗ ID inválido. No se asignará marca.");
                        idMarca = null;
                    }
                }
            }
        }

        int idProd = productoDAO.add(new Producto(0, nombre, precio, idMarca));
        if (idProd != -1) {
            System.out.println("\n✓ Producto agregado exitosamente (ID: " + idProd + ")");
        } else {
            System.out.println("\n✗ Error al agregar producto.");
        }
        pausa();
    }

    private void verTodosLosProductos() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TODOS LOS PRODUCTOS");
        System.out.println("─────────────────────────────────────────────────────────\n");

        List<Producto> productos = productoDAO.getAll();
        if (productos.isEmpty()) {
            System.out.println("  ℹ No hay productos registrados.");
        } else {
            System.out.println("  ┌─────┬────────────────────────────┬──────────┬──────┐");
            System.out.println("  │ ID  │ Nombre                     │ Precio   │ Marca│");
            System.out.println("  ├─────┼────────────────────────────┼──────────┼──────┤");
            for (Producto p : productos) {
                String nombre = p.getNombre();
                if (nombre.length() > 28) nombre = nombre.substring(0, 25) + "...";
                String marcaStr = p.getIdMarca() == null ? "-" : String.valueOf(p.getIdMarca());
                System.out.printf("  │ %-3d │ %-26s │ %-8.2f │ %-4s │\n", p.getIdProducto(), nombre, p.getPrecio(), marcaStr);
            }
            System.out.println("  └─────┴────────────────────────────┴──────────┴──────┘");
            System.out.println("\n  Total de productos: " + productos.size());
        }
        pausa();
    }

    private void buscarPorId() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR PRODUCTO POR ID");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del producto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto p = productoDAO.getById(id);
            if (p != null) {
                System.out.println("\n  ✓ Producto encontrado:");
                System.out.println("    ID: " + p.getIdProducto());
                System.out.println("    Nombre: " + p.getNombre());
                System.out.println("    Precio: " + p.getPrecio());
                System.out.println("    ID Marca: " + (p.getIdMarca() == null ? "(sin asignar)" : p.getIdMarca()));
            } else {
                System.out.println("\n  ✗ Producto no encontrado con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void buscarPorNombre() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  BUSCAR PRODUCTO POR NOMBRE");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el texto a buscar (búsqueda parcial): ");
        String termino = scanner.nextLine().trim();
        if (termino.isEmpty()) {
            System.out.println("\n  ✗ El término de búsqueda no puede estar vacío.");
            pausa();
            return;
        }
        List<Producto> resultados = productoDAO.getByNombre(termino);
        if (resultados.isEmpty()) {
            System.out.println("\n  ℹ No se encontraron productos con ese término.");
        } else {
            System.out.println("\n  ✓ Se encontraron " + resultados.size() + " resultado(s):\n");
            System.out.println("  ┌─────┬────────────────────────────┬──────────┬──────┐");
            System.out.println("  │ ID  │ Nombre                     │ Precio   │ Marca│");
            System.out.println("  ├─────┼────────────────────────────┼──────────┼──────┤");
            for (Producto p : resultados) {
                String nombre = p.getNombre();
                if (nombre.length() > 28) nombre = nombre.substring(0, 25) + "...";
                String marcaStr = p.getIdMarca() == null ? "-" : String.valueOf(p.getIdMarca());
                System.out.printf("  │ %-3d │ %-26s │ %-8.2f │ %-4s │\n", p.getIdProducto(), nombre, p.getPrecio(), marcaStr);
            }
            System.out.println("  └─────┴────────────────────────────┴──────────┴──────┘");
        }
        pausa();
    }

    private void actualizarProducto() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ACTUALIZAR PRODUCTO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del producto a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto actual = productoDAO.getById(id);
            if (actual == null) {
                System.out.println("\n  ✗ Producto no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  Nombre actual: " + actual.getNombre());
            System.out.print("  Nuevo nombre (ENTER para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (nuevoNombre.isEmpty()) nuevoNombre = actual.getNombre();

            System.out.println("  Precio actual: " + actual.getPrecio());
            System.out.print("  Nuevo precio (ENTER para mantener): ");
            String precioStr = scanner.nextLine().trim();
            double nuevoPrecio = actual.getPrecio();
            if (!precioStr.isEmpty()) {
                try {
                    nuevoPrecio = Double.parseDouble(precioStr);
                } catch (NumberFormatException e) {
                    System.out.println("\n✗ Precio inválido. Operación cancelada.");
                    pausa();
                    return;
                }
            }

            Integer idMarca = actual.getIdMarca();
            System.out.println("  ID Marca actual: " + (idMarca == null ? "(sin asignar)" : idMarca));
            System.out.print("  Nuevo ID Marca (ENTER para mantener / 0 para remover): ");
            String marcaInput = scanner.nextLine().trim();
            if (!marcaInput.isEmpty()) {
                try {
                    int m = Integer.parseInt(marcaInput);
                    if (m == 0) idMarca = null;
                    else idMarca = m;
                } catch (NumberFormatException e) {
                    System.out.println("\n✗ ID Marca inválido. Operación cancelada.");
                    pausa();
                    return;
                }
            }

            if (productoDAO.update(new Producto(id, nuevoNombre, nuevoPrecio, idMarca))) {
                System.out.println("\n  ✓ Producto actualizado exitosamente");
            } else {
                System.out.println("\n  ✗ Error al actualizar el producto.");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void eliminarProducto() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ELIMINAR PRODUCTO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del producto a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Producto p = productoDAO.getById(id);
            if (p == null) {
                System.out.println("\n  ✗ Producto no encontrado con ID: " + id);
                pausa();
                return;
            }
            System.out.println("\n  ¿Está seguro de que desea eliminar este producto?");
            System.out.println("  ID: " + p.getIdProducto());
            System.out.println("  Nombre: " + p.getNombre());
            System.out.print("\n  Confirme (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("s") || confirm.equals("si")) {
                if (productoDAO.delete(id)) {
                    System.out.println("\n  ✓ Producto eliminado exitosamente");
                } else {
                    System.out.println("\n  ✗ Error al eliminar el producto.");
                }
            } else {
                System.out.println("\n  ℹ Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void asignarMarca() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  ASIGNAR/REMOVER MARCA A PRODUCTO");
        System.out.println("─────────────────────────────────────────────────────────");

        System.out.print("\n  Ingrese el ID del producto: ");
        try {
            int idProd = Integer.parseInt(scanner.nextLine().trim());
            Producto p = productoDAO.getById(idProd);
            if (p == null) {
                System.out.println("\n  ✗ Producto no encontrado con ID: " + idProd);
                pausa();
                return;
            }

            System.out.println("\n  Producto: " + p.getNombre() + " (ID: " + p.getIdProducto() + ")");
            List<Marca> marcas = marcaDAO.getAll();
            if (marcas.isEmpty()) {
                System.out.println("\n  ℹ No hay marcas disponibles. Cree una marca primero.");
                pausa();
                return;
            }

            System.out.println("\n  Marcas disponibles:");
            for (Marca m : marcas) {
                System.out.println("    [" + m.getIdMarca() + "] " + m.getNombre());
            }
            System.out.print("\n  Ingrese ID de la marca a asignar (0 para remover): ");
            int idMarca = Integer.parseInt(scanner.nextLine().trim());
            Integer idMarcaObj = idMarca == 0 ? null : idMarca;
            if (productoDAO.assignMarca(idProd, idMarcaObj)) {
                System.out.println("\n  ✓ Marca asignada/removida correctamente");
            } else {
                System.out.println("\n  ✗ Error al asignar/remover marca.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  ✗ ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private void verTotal() throws SQLException {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("  TOTAL DE PRODUCTOS");
        System.out.println("─────────────────────────────────────────────────────────");

        int total = productoDAO.count();
        System.out.println("\n  Total de productos registrados: " + total);
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
        ProductoMenu menu = new ProductoMenu();
        menu.ejecutar();
    }
}

