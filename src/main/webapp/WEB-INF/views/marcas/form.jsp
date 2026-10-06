<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${marca.idMarca == 0 ? 'Nueva marca' : 'Editar marca'}"/>
<c:set var="seccion" value="marcas"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/marcas">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${marca.idMarca != 0}">
        <input type="hidden" name="id" value="${marca.idMarca}">
    </c:if>

    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" required maxlength="255" autofocus
           value="<c:out value='${marca.nombre}'/>">

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/marcas">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
