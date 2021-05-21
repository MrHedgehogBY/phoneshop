<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div id="auth-header-part">
    <security:authorize access="isAuthenticated()">
        <span id="nickname-span"><security:authentication property="name"/></span>
    </security:authorize>
    <security:authorize access="hasRole('ROLE_ADMIN')">
        <a id="admin-orders-link" href="${pageContext.request.contextPath}/admin/orders"><spring:theme code="adminPage"/></a>
    </security:authorize>
    <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/j_spring_security_logout"/>"><spring:theme code="logout"/></a>
    </security:authorize>
    <security:authorize access="!isAuthenticated()">
        <a href="${pageContext.request.contextPath}/login"><spring:theme code="login"/></a>
    </security:authorize>
</div>
