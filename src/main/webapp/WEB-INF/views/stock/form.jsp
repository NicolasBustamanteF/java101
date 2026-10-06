<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${detalle.idProductoDetalle == 0 ? 'Registrar stock' : 'Editar stock'}"/>
<c:set var="seccion" value="stock"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<form class="formulario" method="post" action="${pageContext.request.contextPath}/stock">
    <input type="hidden" name="accion" value="guardar">
    <c:if test="${detalle.idProductoDetalle != 0}">
        <input type="hidden" name="id" value="${detalle.idProductoDetalle}">
    </c:if>

    <label for="idProducto">Producto</label>
    <select id="idProducto" name="idProducto" required>
        <option value="">— Seleccione —</option>
        <c:forEach var="p" items="${productos}">
            <option value="${p.idProducto}" ${p.idProducto == detalle.idProducto ? 'selected' : ''}><c:out value="${p.nombre}"/></option>
        </c:forEach>
    </select>

    <label for="idEstante">Estante</label>
    <select id="idEstante" name="idEstante">
        <option value="">— Sin ubicar —</option>
        <c:forEach var="e" items="${estantes}">
            <option value="${e.idEstante}" ${e.idEstante == detalle.idEstante ? 'selected' : ''}><c:out value="${e.codigo}"/></option>
        </c:forEach>
    </select>

    <label for="cantidad">Cantidad</label>
    <input id="cantidad" name="cantidad" type="number" min="0" step="1" required value="${detalle.cantidad}">

    <div class="botones">
        <button class="boton primario" type="submit">Guardar</button>
        <a class="boton" href="${pageContext.request.contextPath}/stock">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
