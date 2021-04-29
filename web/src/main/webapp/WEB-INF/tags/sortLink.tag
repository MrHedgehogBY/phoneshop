<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?field=${field}&order=${order}&search=${param.search}"
style="${field eq param.field and order eq param.order
? 'font-weight: bold' : ''}">
    <c:if test="${order eq 'asc'}">
        ${'🠗'}
    </c:if>
    <c:if test="${order eq 'desc'}">
        ${'🠕'}
    </c:if>
</a>