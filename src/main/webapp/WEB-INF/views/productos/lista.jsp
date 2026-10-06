<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="titulo" value="Productos"/>
<c:set var="seccion" value="productos"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<div class="encabezado">
    <h1>Productos <small>(${total} en total)</small></h1>
    <a class="boton primario" href="?accion=nuevo">+ Nuevo producto</a>
</div>

<form class="filtros" method="get">
    <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Buscar por nombre">
    <button class="boton" type="submit">Buscar</button>
    <c:if test="${not empty q}"><a class="boton" href="?">Limpiar</a></c:if>
</form>

<table>
    <thead>
    <tr><th class="num">ID</th><th>Nombre</th><th class="num">Precio</th><th>Marca</th><th class="acciones"></th></tr>
    </thead>
    <tbody>
    <c:forEach var="p" items="${productos}">
        <tr>
            <td class="num">${p.idProducto}</td>
            <td><c:out value="${p.nombre}"/></td>
            <td class="num"><fmt:formatNumber value="${p.precio}" minFractionDigits="2" maxFractionDigits="2"/></td>
            <td>
                <c:choose>
                    <c:when test="${empty p.idMarca}"><span class="sin-dato">Sin marca</span></c:when>
                    <c:otherwise><c:out value="${nombresMarca[p.idMarca]}"/></c:otherwise>
                </c:choose>
            </td>
            <td class="acciones">
                <a class="boton" href="${pageContext.request.contextPath}/stock?idProducto=${p.idProducto}">Stock</a>
                <a class="boton" href="?accion=editar&amp;id=${p.idProducto}">Editar</a>
                <form method="post" onsubmit="return confirm('¿Eliminar este producto?');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="${p.idProducto}">
                    <button class="boton peligro" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty productos}">
        <tr><td colspan="5" class="vacio">No hay productos para mostrar</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
