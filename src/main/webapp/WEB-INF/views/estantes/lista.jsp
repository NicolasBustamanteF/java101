<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Estantes"/>
<c:set var="seccion" value="estantes"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<div class="encabezado">
    <h1>Estantes <small>(${total} en total)</small></h1>
    <a class="boton primario" href="?accion=nuevo">+ Nuevo estante</a>
</div>

<form class="filtros" method="get">
    <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Buscar por código">
    <select name="idDeposito" onchange="this.form.submit()">
        <option value="">Todos los depósitos</option>
        <c:forEach var="d" items="${depositos}">
            <option value="${d.idDeposito}" ${d.idDeposito == idDeposito ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
        </c:forEach>
    </select>
    <button class="boton" type="submit">Buscar</button>
    <c:if test="${not empty q or not empty idDeposito}"><a class="boton" href="?">Limpiar</a></c:if>
</form>

<table>
    <thead>
    <tr><th class="num">ID</th><th>Código</th><th>Depósito</th><th class="acciones"></th></tr>
    </thead>
    <tbody>
    <c:forEach var="e" items="${estantes}">
        <tr>
            <td class="num">${e.idEstante}</td>
            <td><c:out value="${e.codigo}"/></td>
            <td>
                <c:choose>
                    <c:when test="${empty e.idDeposito}"><span class="sin-dato">Sin asignar</span></c:when>
                    <c:otherwise><c:out value="${nombresDeposito[e.idDeposito]}"/></c:otherwise>
                </c:choose>
            </td>
            <td class="acciones">
                <a class="boton" href="${pageContext.request.contextPath}/stock?idEstante=${e.idEstante}">Stock</a>
                <a class="boton" href="?accion=editar&amp;id=${e.idEstante}">Editar</a>
                <form method="post" onsubmit="return confirm('¿Eliminar este estante?');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="${e.idEstante}">
                    <button class="boton peligro" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty estantes}">
        <tr><td colspan="4" class="vacio">No hay estantes para mostrar</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
