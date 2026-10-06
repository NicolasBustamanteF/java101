package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.DepositoDAO;
import py.com.nikosoft.depositos.entities.Deposito;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Formulario web de Depósitos (mismas acciones que CategoriaServlet).
 */
@WebServlet("/depositos")
public class DepositoServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/depositos/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/depositos/form.jsp";

    private final DepositoDAO depositoDAO = new DepositoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                request.setAttribute("deposito", new Deposito());
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else if ("editar".equals(accion)) {
                Deposito deposito = depositoDAO.getById(WebUtils.paramInteger(request, "id"));
                if (deposito == null) {
                    WebUtils.flash(request, "error", "El depósito no existe");
                    WebUtils.redirect(request, response, "/depositos");
                    return;
                }
                request.setAttribute("deposito", deposito);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            } else {
                String q = WebUtils.param(request, "q");
                request.setAttribute("q", q);
                request.setAttribute("depositos", q == null ? depositoDAO.getAll() : depositoDAO.getByNombre(q));
                request.setAttribute("total", depositoDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar depósitos: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = depositoDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Depósito eliminado" : "El depósito no existe");
                WebUtils.redirect(request, response, "/depositos");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            Deposito deposito = new Deposito(id == null ? 0 : id,
                    WebUtils.param(request, "nombre"),
                    WebUtils.param(request, "direccion"));

            if (deposito.getNombre() == null) {
                request.setAttribute("error", "El nombre es obligatorio");
                request.setAttribute("deposito", deposito);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
                return;
            }

            if (id == null) {
                depositoDAO.add(deposito);
                WebUtils.flash(request, "ok", "Depósito creado");
            } else {
                depositoDAO.update(deposito);
                WebUtils.flash(request, "ok", "Depósito actualizado");
            }
            WebUtils.redirect(request, response, "/depositos");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar depósito: " + e.getMessage(), e);
        }
    }
}
