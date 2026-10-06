package py.com.nikosoft.depositos.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.com.nikosoft.depositos.boundary.EstanteDAO;
import py.com.nikosoft.depositos.boundary.ProductoDAO;
import py.com.nikosoft.depositos.boundary.ProductoDetalleDAO;
import py.com.nikosoft.depositos.entities.Estante;
import py.com.nikosoft.depositos.entities.Producto;
import py.com.nikosoft.depositos.entities.ProductoDetalle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Formulario web de Stock (producto_detalle): cuántas unidades de un producto hay en cada estante.
 * La lista se puede filtrar por producto (?idProducto=N) o por estante (?idEstante=N).
 */
@WebServlet("/stock")
public class ProductoDetalleServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/views/stock/lista.jsp";
    private static final String VISTA_FORM = "/WEB-INF/views/stock/form.jsp";

    private final ProductoDetalleDAO detalleDAO = new ProductoDetalleDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final EstanteDAO estanteDAO = new EstanteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = WebUtils.param(request, "accion");
        try {
            if ("nuevo".equals(accion)) {
                mostrarFormulario(request, response, new ProductoDetalle());
            } else if ("editar".equals(accion)) {
                ProductoDetalle detalle = detalleDAO.getById(WebUtils.paramInteger(request, "id"));
                if (detalle == null) {
                    WebUtils.flash(request, "error", "El registro de stock no existe");
                    WebUtils.redirect(request, response, "/stock");
                    return;
                }
                mostrarFormulario(request, response, detalle);
            } else {
                Integer idProducto = WebUtils.paramInteger(request, "idProducto");
                Integer idEstante = WebUtils.paramInteger(request, "idEstante");
                List<ProductoDetalle> detalles;
                if (idProducto != null) {
                    detalles = detalleDAO.getByProducto(idProducto);
                } else if (idEstante != null) {
                    detalles = detalleDAO.getByEstante(idEstante);
                } else {
                    detalles = detalleDAO.getAll();
                }
                List<Producto> productos = productoDAO.getAll();
                List<Estante> estantes = estanteDAO.getAll();
                request.setAttribute("idProducto", idProducto);
                request.setAttribute("idEstante", idEstante);
                request.setAttribute("detalles", detalles);
                request.setAttribute("productos", productos);
                request.setAttribute("estantes", estantes);
                request.setAttribute("nombresProducto", nombresProducto(productos));
                request.setAttribute("codigosEstante", codigosEstante(estantes));
                request.setAttribute("total", detalleDAO.count());
                request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al procesar stock: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("eliminar".equals(WebUtils.param(request, "accion"))) {
                boolean eliminado = detalleDAO.delete(WebUtils.paramInteger(request, "id"));
                WebUtils.flash(request, eliminado ? "ok" : "error",
                        eliminado ? "Registro de stock eliminado" : "El registro de stock no existe");
                WebUtils.redirect(request, response, "/stock");
                return;
            }

            Integer id = WebUtils.paramInteger(request, "id");
            ProductoDetalle detalle = new ProductoDetalle();
            detalle.setIdProductoDetalle(id == null ? 0 : id);
            detalle.setIdProducto(WebUtils.paramInteger(request, "idProducto"));
            detalle.setIdEstante(WebUtils.paramInteger(request, "idEstante"));

            String error = null;
            try {
                Integer cantidad = WebUtils.paramInteger(request, "cantidad");
                detalle.setCantidad(cantidad == null ? 0 : cantidad);
                if (detalle.getCantidad() < 0) {
                    error = "La cantidad no puede ser negativa";
                }
            } catch (NumberFormatException e) {
                error = "La cantidad debe ser un número entero";
            }
            if (detalle.getIdProducto() == null) {
                error = "Debe seleccionar un producto";
            }
            if (error != null) {
                request.setAttribute("error", error);
                mostrarFormulario(request, response, detalle);
                return;
            }

            if (id == null) {
                detalleDAO.add(detalle);
                WebUtils.flash(request, "ok", "Stock registrado");
            } else {
                detalleDAO.update(detalle);
                WebUtils.flash(request, "ok", "Stock actualizado");
            }
            WebUtils.redirect(request, response, "/stock");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error al guardar stock: " + e.getMessage(), e);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, ProductoDetalle detalle)
            throws ServletException, IOException, SQLException {
        request.setAttribute("detalle", detalle);
        request.setAttribute("productos", productoDAO.getAll());
        request.setAttribute("estantes", estanteDAO.getAll());
        request.getRequestDispatcher(VISTA_FORM).forward(request, response);
    }

    private Map<Integer, String> nombresProducto(List<Producto> productos) {
        Map<Integer, String> nombres = new HashMap<>();
        for (Producto p : productos) {
            nombres.put(p.getIdProducto(), p.getNombre());
        }
        return nombres;
    }

    private Map<Integer, String> codigosEstante(List<Estante> estantes) {
        Map<Integer, String> codigos = new HashMap<>();
        for (Estante e : estantes) {
            codigos.put(e.getIdEstante(), e.getCodigo());
        }
        return codigos;
    }
}
