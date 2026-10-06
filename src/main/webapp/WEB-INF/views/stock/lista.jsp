<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Stock"/>
<c:set var="seccion" value="stock"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<div class="encabezado">
    <h1>Stock por estante <small>(${total} registros)</small></h1>
    <a class="boton primario" href="?accion=nuevo">+ Registrar stock</a>
</div>

<form class="filtros" method="get">
    <select name="idProducto" onchange="this.form.idEstante.value=''; this.form.submit()">
        <option value="">Todos los productos</option>
        <c:forEach var="p" items="${productos}">
            <option value="${p.idProducto}" ${p.idProducto == idProducto ? 'selected' : ''}><c:out value="${p.nombre}"/></option>
        </c:forEach>
    </select>
    <select name="idEstante" onchange="this.form.idProducto.value=''; this.form.submit()">
        <option value="">Todos los estantes</option>
        <c:forEach var="e" items="${estantes}">
            <option value="${e.idEstante}" ${e.idEstante == idEstante ? 'selected' : ''}><c:out value="${e.codigo}"/></option>
        </c:forEach>
    </select>
    <c:if test="${not empty idProducto or not empty idEstante}"><a class="boton" href="?">Limpiar</a></c:if>
</form>

<table>
    <thead>
    <tr><th class="num">ID</th><th>Producto</th><th>Estante</th><th class="num">Cantidad</th><th class="acciones"></th></tr>
    </thead>
    <tbody>
    <c:forEach var="d" items="${detalles}">
        <tr>
            <td class="num">${d.idProductoDetalle}</td>
            <td>
                <c:choose>
                    <c:when test="${empty d.idProducto}"><span class="sin-dato">Producto eliminado</span></c:when>
                    <c:otherwise><c:out value="${nombresProducto[d.idProducto]}"/></c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${empty d.idEstante}"><span class="sin-dato">Sin ubicar</span></c:when>
                    <c:otherwise><c:out value="${codigosEstante[d.idEstante]}"/></c:otherwise>
                </c:choose>
            </td>
            <td class="num">${d.cantidad}</td>
            <td class="acciones">
                <a class="boton" href="?accion=editar&amp;id=${d.idProductoDetalle}">Editar</a>
                <form method="post" onsubmit="return confirm('¿Eliminar este registro de stock?');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="${d.idProductoDetalle}">
                    <button class="boton peligro" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty detalles}">
        <tr><td colspan="5" class="vacio">No hay registros de stock para mostrar</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
