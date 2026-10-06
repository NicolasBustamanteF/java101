<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${estante.idEstante == 0 ? 'Nuevo estante' : 'Editar estante'}"/>
<c:set var="seccion" value="estantes"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/estantes">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${estante.idEstante != 0}">
        <input type="hidden" name="id" value="${estante.idEstante}">
    </c:if>

    <label for="codigo">Código</label>
    <input id="codigo" name="codigo" required maxlength="255" autofocus
           value="<c:out value='${estante.codigo}'/>">

    <label for="idDeposito">Depósito</label>
    <select id="idDeposito" name="idDeposito">
        <option value="">— Sin asignar —</option>
        <c:forEach var="d" items="${depositos}">
            <option value="${d.idDeposito}" ${d.idDeposito == estante.idDeposito ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
        </c:forEach>
    </select>

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/estantes">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
