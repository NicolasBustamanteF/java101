<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Marcas"/>
<c:set var="seccion" value="marcas"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<div class="encabezado">
    <h1>Marcas <small>(${total} en total)</small></h1>
    <a class="boton primario" href="?accion=nuevo">+ Nueva marca</a>
</div>

<form class="filtros" method="get">
    <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Buscar por nombre">
    <button class="boton" type="submit">Buscar</button>
    <c:if test="${not empty q}"><a class="boton" href="?">Limpiar</a></c:if>
</form>

<table>
    <thead>
    <tr><th class="num">ID</th><th>Nombre</th><th class="acciones"></th></tr>
    </thead>
    <tbody>
    <c:forEach var="m" items="${marcas}">
        <tr>
            <td class="num">${m.idMarca}</td>
            <td><c:out value="${m.nombre}"/></td>
            <td class="acciones">
                <a class="boton" href="?accion=editar&amp;id=${m.idMarca}">Editar</a>
                <form method="post" onsubmit="return confirm('¿Eliminar esta marca? Los productos quedarán sin marca.');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="${m.idMarca}">
                    <button class="boton peligro" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty marcas}">
        <tr><td colspan="3" class="vacio">No hay marcas para mostrar</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
