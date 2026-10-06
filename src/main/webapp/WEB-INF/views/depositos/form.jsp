<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${deposito.idDeposito == 0 ? 'Nuevo depósito' : 'Editar depósito'}"/>
<c:set var="seccion" value="depositos"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/depositos">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${deposito.idDeposito != 0}">
        <input type="hidden" name="id" value="${deposito.idDeposito}">
    </c:if>

    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" required maxlength="255" autofocus
           value="<c:out value='${deposito.nombre}'/>">

    <label for="direccion">Dirección</label>
    <input id="direccion" name="direccion" maxlength="255"
           value="<c:out value='${deposito.direccion}'/>">

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/depositos">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
