<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Inicio"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1>Sistema de Gestión de Depósitos</h1>

<div class="tarjetas">
    <a class="tarjeta" href="${pageContext.request.contextPath}/depositos">
        <strong>Depósitos</strong><span>Locales donde se guarda la mercadería</span>
    </a>
    <a class="tarjeta" href="${pageContext.request.contextPath}/estantes">
        <strong>Estantes</strong><span>Ubicaciones dentro de cada depósito</span>
    </a>
    <a class="tarjeta" href="${pageContext.request.contextPath}/productos">
        <strong>Productos</strong><span>Artículos, precios y marca</span>
    </a>
    <a class="tarjeta" href="${pageContext.request.contextPath}/marcas">
        <strong>Marcas</strong><span>Fabricantes de los productos</span>
    </a>
    <a class="tarjeta" href="${pageContext.request.contextPath}/categorias">
        <strong>Categorías</strong><span>Clasificación de productos</span>
    </a>
    <a class="tarjeta" href="${pageContext.request.contextPath}/stock">
        <strong>Stock</strong><span>Cantidad de cada producto por estante</span>
    </a>
</div>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
