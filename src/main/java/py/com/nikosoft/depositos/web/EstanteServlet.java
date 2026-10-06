package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.DepositoDAO;
import py.com.nikosoft.depositos.boundary.EstanteDAO;
import py.com.nikosoft.depositos.entities.Deposito;
import py.com.nikosoft.depositos.entities.Estante;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Formulario web de Estantes.
 * Además de las acciones comunes, la lista se puede filtrar por depósito (?idDeposito=N)
 * y el depósito se asigna/remueve desde el formulario de edición.
 */
@WebServlet("/estantes")
public class EstanteServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/estantes/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/estantes/form.jsp";

    private final EstanteDAO estanteDAO = new EstanteDAO();
    private final DepositoDAO depositoDAO = new DepositoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                mostrarFormulario(request, response, new Estante());
            } else if ("editar".equals(accion)) {
                Estante estante = estanteDAO.getById(WebUtils.paramInteger(request, "id"));
                if (estante == null) {
                    WebUtils.flash(request, "error", "El estante no existe");
                    WebUtils.redirect(request, response, "/estantes");
                    return;
                }
                mostrarFormulario(request, response, estante);
            } else {
                String q = WebUtils.param(request, "q");
                Integer idDeposito = WebUtils.paramInteger(request, "idDeposito");
                List<Estante> estantes;
                if (idDeposito != null) {
                    estantes = estanteDAO.getByDeposito(idDeposito);
                } else if (q != null) {
                    estantes = estanteDAO.getByCodigo(q);
                } else {
                    estantes = estanteDAO.getAll();
                }
                List<Deposito> depositos = depositoDAO.getAll();
                request.setAttribute("q", q);
                request.setAttribute("idDeposito", idDeposito);
                request.setAttribute("estantes", estantes);
                request.setAttribute("depositos", depositos);
                request.setAttribute("nombresDeposito", nombresDeposito(depositos));
                request.setAttribute("total", estanteDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar estantes: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = estanteDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Estante eliminado" : "El estante no existe");
                WebUtils.redirect(request, response, "/estantes");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            Estante estante = new Estante(id == null ? 0 : id,
                    WebUtils.param(request, "codigo"),
                    WebUtils.paramInteger(request, "idDeposito"));

            if (estante.getCodigo() == null) {
                request.setAttribute("error", "El código es obligatorio");
                mostrarFormulario(request, response, estante);
                return;
            }

            if (id == null) {
                estanteDAO.add(estante);
                WebUtils.flash(request, "ok", "Estante creado");
            } else {
                estanteDAO.update(estante);
                WebUtils.flash(request, "ok", "Estante actualizado");
            }
            WebUtils.redirect(request, response, "/estantes");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar estante: " + e.getMessage(), e);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Estante estante)
            throws ServletException, IOException, SQLException {
        request.setAttribute("estante", estante);
        request.setAttribute("depositos", depositoDAO.getAll());
        request.getRequestDispatcher(VISTA_FORM).forward(request, response);
    }

    private Map<Integer, String> nombresDeposito(List<Deposito> depositos) {
        Map<Integer, String> nombres = new HashMap<>();
        for (Deposito d : depositos) {
            nombres.put(d.getIdDeposito(), d.getNombre());
        }
        return nombres;
    }
}
