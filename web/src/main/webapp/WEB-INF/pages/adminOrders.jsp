<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title><spring:theme code="titleAdminOrders"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:headerWithoutCart/>
    <h3>
        <spring:theme code="titleAdminOrders"/>
    </h3>
    <c:if test="${empty orders}">
        <h2><spring:theme code="ordersEmpty"/></h2>
    </c:if>
    <c:if test="${not empty orders}">
        <table>
            <thead>
            <tr>
                <td>
                    <spring:theme code="adminOrderNumber"/>
                </td>
                <td>
                    <spring:theme code="adminCustomer"/>
                </td>
                <td>
                    <spring:theme code="adminPhone"/>
                </td>
                <td>
                    <spring:theme code="adminAddress"/>
                </td>
                <td>
                    <spring:theme code="adminDate"/>
                </td>
                <td>
                    <spring:theme code="adminTotalPrice"/>
                </td>
                <td>
                    <spring:theme code="adminStatus"/>
                </td>
            </tr>
            </thead>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/orders/${order.id}"><c:out value="${order.id}"/></a>
                    </td>
                    <td>
                        <c:out value="${order.firstName}"/> <c:out value="${order.lastName}"/>
                    </td>
                    <td>
                        <c:out value="${order.contactPhoneNo}"/>
                    </td>
                    <td>
                        <c:out value="${order.deliveryAddress}"/>
                    </td>
                    <td>
                        <c:out value="${order.orderPlacingDate}"/>
                    </td>
                    <td>
                        <c:out value="${order.totalPrice}"/> <spring:theme code="usd"/>
                    </td>
                    <td>
                        <c:out value="${order.status.toString()}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <div class="pages-links">
            <a href="${pageContext.request.contextPath}/admin/orders?page=${empty param.page ? 1 : (param.page > 1 ? param.page - 1 : 1)}"><spring:theme code="previousPage"/></a>
            <a href="${pageContext.request.contextPath}/admin/orders?page=${empty param.page ? (lastPage eq 1 ? 1 : 2) : (param.page < lastPage ? param.page + 1 : lastPage)}"><spring:theme code="nextPage"/></a>
        </div>
    </c:if>
</body>
</html>
