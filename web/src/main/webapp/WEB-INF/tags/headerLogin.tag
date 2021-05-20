<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="header-part">
    <a href="${pageContext.request.contextPath}/productList">
        <h2>
            <spring:theme code="helloMessage" />
        </h2>
    </a>
</div>
<hr>
