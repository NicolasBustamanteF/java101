<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Depósitos"/>
<c:set var="seccion" value="depositos"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<div class="encabezado">
    <h1>Depósitos <small>(${total} en total)</small></h1>
    <a class="boton primario" href="?accion=nuevo">+ Nuevo depósito</a>
</div>

<form class="filtros" method="get">
    <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Buscar por nombre">
    <button class="boton" type="submit">Buscar</button>
    <c:if test="${not empty q}"><a class="boton" href="?">Limpiar</a></c:if>
</form>

<table>
    <thead>
    <tr><th class="num">ID</th><th>Nombre</th><th>Dirección</th><th class="acciones"></th></tr>
    </thead>
    <tbody>
    <c:forEach var="d" items="${depositos}">
        <tr>
            <td class="num">${d.idDeposito}</td>
            <td><c:out value="${d.nombre}"/></td>
            <td><c:out value="${d.direccion}"/></td>
            <td class="acciones">
                <a class="boton" href="${pageContext.request.contextPath}/estantes?idDeposito=${d.idDeposito}">Estantes</a>
                <a class="boton" href="?accion=editar&amp;id=${d.idDeposito}">Editar</a>
                <form method="post" onsubmit="return confirm('¿Eliminar este depósito? Sus estantes quedarán sin depósito.');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="${d.idDeposito}">
                    <button class="boton peligro" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty depositos}">
        <tr><td colspan="4" class="vacio">No hay depósitos para mostrar</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
