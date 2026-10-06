<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${producto.idProducto == 0 ? 'Nuevo producto' : 'Editar producto'}"/>
<c:set var="seccion" value="productos"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/productos">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${producto.idProducto != 0}">
        <input type="hidden" name="id" value="${producto.idProducto}">
    </c:if>

    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" required maxlength="255" autofocus
           value="<c:out value='${producto.nombre}'/>">

    <label for="precio">Precio</label>
    <input id="precio" name="precio" type="number" min="0" step="0.01" value="${producto.precio}">

    <label for="idMarca">Marca</label>
    <select id="idMarca" name="idMarca">
        <option value="">— Sin marca —</option>
        <c:forEach var="m" items="${marcas}">
            <option value="${m.idMarca}" ${m.idMarca == producto.idMarca ? 'selected' : ''}><c:out value="${m.nombre}"/></option>
        </c:forEach>
    </select>

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/productos">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
