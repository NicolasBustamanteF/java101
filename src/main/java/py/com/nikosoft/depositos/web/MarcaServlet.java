package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.MarcaDAO;
import py.com.nikosoft.depositos.entities.Marca;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Formulario web de Marcas (mismas acciones que CategoriaServlet).
 */
@WebServlet("/marcas")
public class MarcaServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/marcas/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/marcas/form.jsp";

    private final MarcaDAO marcaDAO = new MarcaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                request.setAttribute("marca", new Marca());
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else if ("editar".equals(accion)) {
                Marca marca = marcaDAO.getById(WebUtils.paramInteger(request, "id"));
                if (marca == null) {
                    WebUtils.flash(request, "error", "La marca no existe");
                    WebUtils.redirect(request, response, "/marcas");
                    return;
                }
                request.setAttribute("marca", marca);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else {
                String q = WebUtils.param(request, "q");
                request.setAttribute("q", q);
                request.setAttribute("marcas", q == null ? marcaDAO.getAll() : marcaDAO.getByNombre(q));
                request.setAttribute("total", marcaDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar marcas: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = marcaDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Marca eliminada" : "La marca no existe");
                WebUtils.redirect(request, response, "/marcas");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            Marca marca = new Marca(id == null ? 0 : id, WebUtils.param(request, "nombre"));

            if (marca.getNombre() == null) {
                request.setAttribute("error", "El nombre es obligatorio");
                request.setAttribute("marca", marca);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
                return;
            }

            if (id == null) {
                marcaDAO.add(marca);
                WebUtils.flash(request, "ok", "Marca creada");
            } else {
                marcaDAO.update(marca);
                WebUtils.flash(request, "ok", "Marca actualizada");
            }
            WebUtils.redirect(request, response, "/marcas");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar marca: " + e.getMessage(), e);
        }
    }
}
