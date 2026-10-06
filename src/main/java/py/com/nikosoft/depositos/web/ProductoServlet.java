package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.MarcaDAO;
import py.com.nikosoft.depositos.boundary.ProductoDAO;
import py.com.nikosoft.depositos.entities.Marca;
import py.com.nikosoft.depositos.entities.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Formulario web de Productos.
 * La marca se asigna/remueve desde el formulario de edición.
 */
@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/productos/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/productos/form.jsp";

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                mostrarFormulario(request, response, new Producto());
            } else if ("editar".equals(accion)) {
                Producto producto = productoDAO.getById(WebUtils.paramInteger(request, "id"));
                if (producto == null) {
                    WebUtils.flash(request, "error", "El producto no existe");
                    WebUtils.redirect(request, response, "/productos");
                    return;
                }
                mostrarFormulario(request, response, producto);
            } else {
                String q = WebUtils.param(request, "q");
                request.setAttribute("q", q);
                request.setAttribute("productos", q == null ? productoDAO.getAll() : productoDAO.getByNombre(q));
                request.setAttribute("nombresMarca", nombresMarca(marcaDAO.getAll()));
                request.setAttribute("total", productoDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar productos: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = productoDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Producto eliminado" : "El producto no existe");
                WebUtils.redirect(request, response, "/productos");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            Producto producto = new Producto();
            producto.setIdProducto(id == null ? 0 : id);
            producto.setNombre(WebUtils.param(request, "nombre"));
            producto.setIdMarca(WebUtils.paramInteger(request, "idMarca"));

            String error = null;
            try {
                String precio = WebUtils.param(request, "precio");
                producto.setPrecio(precio == null ? 0 : Double.parseDouble(precio));
                if (producto.getPrecio() < 0) {
                    error = "El precio no puede ser negativo";
                }
            } catch (NumberFormatException e) {
                error = "El precio debe ser un número";
            }
            if (producto.getNombre() == null) {
                error = "El nombre es obligatorio";
            }
            if (error != null) {
                request.setAttribute("error", error);
                mostrarFormulario(request, response, producto);
                return;
            }

            if (id == null) {
                productoDAO.add(producto);
                WebUtils.flash(request, "ok", "Producto creado");
            } else {
                productoDAO.update(producto);
                WebUtils.flash(request, "ok", "Producto actualizado");
            }
            WebUtils.redirect(request, response, "/productos");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar producto: " + e.getMessage(), e);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Producto producto)
            throws ServletException, IOException, SQLException {
        request.setAttribute("producto", producto);
        request.setAttribute("marcas", marcaDAO.getAll());
        request.getRequestDispatcher(VISTA_FORM).forward(request, response);
    }

    private Map<Integer, String> nombresMarca(List<Marca> marcas) {
        Map<Integer, String> nombres = new HashMap<>();
        for (Marca m : marcas) {
            nombres.put(m.getIdMarca(), m.getNombre());
        }
        return nombres;
    }
}
