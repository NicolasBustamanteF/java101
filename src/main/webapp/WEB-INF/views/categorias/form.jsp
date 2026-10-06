<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${categoria.idCategoria == 0 ? 'Nueva categoría' : 'Editar categoría'}"/>
<c:set var="seccion" value="categorias"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/categorias">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${categoria.idCategoria != 0}">
        <input type="hidden" name="id" value="${categoria.idCategoria}">
    </c:if>

    <label for="descripcion">Descripción</label>
    <input id="descripcion" name="descripcion" required maxlength="255" autofocus
           value="<c:out value='${categoria.descripcion}'/>">

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/categorias">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
