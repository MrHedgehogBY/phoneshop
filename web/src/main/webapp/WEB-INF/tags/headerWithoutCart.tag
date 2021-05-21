<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div class="header-part">
    <a href="${pageContext.request.contextPath}/productList">
        <h2>
            <spring:theme code="helloMessage" />
        </h2>
    </a>
    <tags:authContent/>
</div>
<hr>
