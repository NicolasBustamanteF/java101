<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Error"/>
<%@ include file="/WEB-INF/views/comun/header.jspf" %>

<h1>Ocurrió un error</h1>
<div class="alerta error">
    <c:out value="${requestScope['jakarta.servlet.error.message']}"/>
</div>
<p class="ayuda">Verifique que PostgreSQL esté corriendo y que la base <code>db_depositos</code> exista.</p>
<p><a class="boton" href="${pageContext.request.contextPath}/">Volver al inicio</a></p>

<%@ include file="/WEB-INF/views/comun/footer.jspf" %>
