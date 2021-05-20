<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title><spring:theme code="titleOrderOverview"/><c:out value="${order.id}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:headerWithoutCart/>
    <h3>
        <spring:theme code="titleDownOrderOverview"/>
    </h3>
    <h4>
        <spring:theme code="orderNumber"/> <c:out value="${order.id}"/>
    </h4>
    <table>
        <thead>
        <tr>
            <td>
                <spring:theme code="brand" />
            </td>
            <td>
                <spring:theme code="model" />
            </td>
            <td>
                <spring:theme code="color" />
            </td>
            <td>
                <spring:theme code="displaySize" />
            </td>
            <td>
                <spring:theme code="quantity" />
            </td>
            <td>
                <spring:theme code="price" />
            </td>
        </tr>
        </thead>
        <c:forEach var="orderItem" items="${order.orderItems}">
            <tr>
                <td>
                    <c:out value="${orderItem.phone.brand}"/>
                </td>
                <td>
                    <c:out value="${orderItem.phone.model}"/>
                </td>
                <td>
                    <c:forEach var="color" items="${orderItem.phone.colors}">
                        <c:out value="${color.code}"/>
                    </c:forEach>
                </td>
                <td>
                    <c:out value="${orderItem.phone.displaySizeInches}"/>"
                </td>
                <td>
                    <c:out value="${orderItem.quantity}"/>
                </td>
                <td>
                    <c:out value="${orderItem.phone.price}"/> <spring:theme code="usd"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <spring:theme code="subtotalOrder"/>
            </td>
            <td>
                <c:out value="${order.subtotal}"/> <spring:theme code="usd"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <spring:theme code="deliveryOrder"/>
            </td>
            <td>
                <c:out value="${order.deliveryPrice}"/> <spring:theme code="usd"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <spring:theme code="totalOrder"/>
            </td>
            <td>
                <c:out value="${order.totalPrice}"/> <spring:theme code="usd"/>
            </td>
        </tr>
    </table>
    <br>
    <table class="no-border-table">
        <tags:orderOverviewRow label="firstNameOrder" item="${order.firstName}"/>
        <tags:orderOverviewRow label="lastNameOrder" item="${order.lastName}"/>
        <tags:orderOverviewRow label="addressOrder" item="${order.deliveryAddress}"/>
        <tags:orderOverviewRow label="phoneOrder" item="${order.contactPhoneNo}"/>
    </table>
    <p id="add-info-overview">
        <c:out value="${order.additionalInformation}"/>
    </p>
    <br>
    <form method="get" action="${pageContext.request.contextPath}/productList">
        <button class="buttons">
            <spring:theme code="orderOverviewBack"/>
        </button>
    </form>
</body>
</html>
