package py.com.nikosoft.depositos.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Utilidades compartidas por los servlets: lectura de parámetros,
 * mensajes "flash" (sobreviven a un redirect) y redirecciones.
 */
public final class WebUtils {

    private WebUtils() {
    }

    /**
     * Devuelve el parámetro sin espacios, o null si viene vacío o no existe
     */
    public static String param(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    /**
     * Convierte el parámetro a Integer; null si está vacío
     * @throws NumberFormatException si no es un número válido
     */
    public static Integer paramInteger(HttpServletRequest request, String nombre) {
        String valor = param(request, nombre);
        return valor == null ? null : Integer.valueOf(valor);
    }

    /**
     * Guarda un mensaje en sesión para mostrarlo en la próxima página
     * @param tipo "ok" o "error"
     */
    public static void flash(HttpServletRequest request, String tipo, String mensaje) {
        HttpSession session = request.getSession();
        session.setAttribute("flashTipo", tipo);
        session.setAttribute("flashMensaje", mensaje);
    }

    /**
     * Redirige a una ruta relativa al contexto de la aplicación (ej: "/categorias")
     */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String ruta)
            throws IOException {
        response.sendRedirect(request.getContextPath() + ruta);
    }
}
