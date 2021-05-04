<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><spring:theme code="error404title"/></title>
</head>
<body>
    <h3>
        <spring:theme code="error404message"/>
    </h3>
    <h2>
        <c:if test="${not empty param.message}">
            <c:out value="${param.message}"/>
        </c:if>
    </h2>
</body>
</html>
