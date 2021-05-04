<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:theme code="titleCart"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:header cart="${cart}"/>
    <form method="get" action="${pageContext.request.contextPath}/productList">
        <button class="buttons">
            <spring:theme code="backToPdp"/>
        </button>
    </form>
    <h3>
        <spring:theme code="titleCart"/>
    </h3>
    <div id="error-result">
        <c:if test="${not empty error}">
            <c:out value="${error}"/>
        </c:if>
    </div>
    <div id="success-result">
        <c:if test="${not empty message}">
            <c:out value="${message}"/>
        </c:if>
    </div>
    <form:form id="update-form" method="post" action="${pageContext.request.contextPath}/cart" modelAttribute="phoneArrayDTO">
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
                    <spring:theme code="price" />
                </td>
                <td>
                    <spring:theme code="quantity" />
                </td>
                <td>
                    <spring:theme code="action" />
                </td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
                <tr>
                    <td>
                        <c:out value="${cartItem.phone.brand}"/>
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.model}"/>
                    </td>
                    <td>
                        <c:forEach var="color" items="${cartItem.phone.colors}">
                            <c:out value="${color.code}"/>
                        </c:forEach>
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.displaySizeInches}"/>"
                    </td>
                    <td>
                        <c:out value="${cartItem.phone.price}"/><spring:theme code="usd"/>
                    </td>
                    <td>
                        <input class="quantity-input" type="text" id="quantity"
                               name="quantity" value="${(not empty errorsId or not empty outOfStockId) ?
                               paramValues['quantity'][status.index] : cartItem.quantity}"/>
                        <div class="result-error" id="result">
                            <c:if test="${fn:contains(errorsId, cartItem.phone.id)}">
                                <spring:theme code="wrongInput"/>
                            </c:if>
                            <c:if test="${fn:contains(outOfStockId, cartItem.phone.id)}">
                                <spring:theme code="outOfStock"/>
                            </c:if>
                        </div>
                        <input id="phoneId" name="phoneId" type="hidden" value="${cartItem.phone.id}"/>
                    </td>
                    <td>
                        <button class="buttons" formmethod="post" formaction="${pageContext.request.contextPath}/cart/${cartItem.phone.id}">
                            <spring:theme code="deleteFromCart"/>
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form:form>
    <div id="cart-buttons">
        <button class="buttons" id="update-cart" form="update-form">
            <spring:theme code="updateCart" />
        </button>
        <button class="buttons" id="order-cart">
            <spring:theme code="orderCart" />
        </button>
    </div>
</body>
</html>
