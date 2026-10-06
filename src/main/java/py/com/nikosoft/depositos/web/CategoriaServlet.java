package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.CategoriaDAO;
import py.com.nikosoft.depositos.entities.Categoria;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Formulario web de Categorías.
 * GET  /categorias                      -> lista (con búsqueda ?q=)
 * GET  /categorias?accion=nuevo         -> formulario vacío
 * GET  /categorias?accion=editar&id=N   -> formulario con datos
 * POST /categorias (accion=guardar)     -> alta o modificación
 * POST /categorias (accion=eliminar)    -> baja
 */
@WebServlet("/categorias")
public class CategoriaServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/categorias/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/categorias/form.jsp";

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                request.setAttribute("categoria", new Categoria());
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else if ("editar".equals(accion)) {
                Categoria categoria = categoriaDAO.getById(WebUtils.paramInteger(request, "id"));
                if (categoria == null) {
                    WebUtils.flash(request, "error", "La categoría no existe");
                    WebUtils.redirect(request, response, "/categorias");
                    return;
                }
                request.setAttribute("categoria", categoria);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else {
                String q = WebUtils.param(request, "q");
                request.setAttribute("q", q);
                request.setAttribute("categorias",
                        q == null ? categoriaDAO.getAll() : categoriaDAO.getByDescripcion(q));
                request.setAttribute("total", categoriaDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar categorías: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = categoriaDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Categoría eliminada" : "La categoría no existe");
                WebUtils.redirect(request, response, "/categorias");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            Categoria categoria = new Categoria(id == null ? 0 : id, WebUtils.param(request, "descripcion"));

            if (categoria.getDescripcion() == null) {
                request.setAttribute("error", "La descripción es obligatoria");
                request.setAttribute("categoria", categoria);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
                return;
            }

            if (id == null) {
                categoriaDAO.add(categoria);
                WebUtils.flash(request, "ok", "Categoría creada");
            } else {
                categoriaDAO.update(categoria);
                WebUtils.flash(request, "ok", "Categoría actualizada");
            }
            WebUtils.redirect(request, response, "/categorias");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar categoría: " + e.getMessage(), e);
        }
    }
}
