<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title><spring:theme code="titleOrder"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:headerWithoutCart/>
    <form method="get" action="${pageContext.request.contextPath}/cart">
        <button class="buttons">
            <spring:theme code="backToCart"/>
        </button>
    </form>
    <h3>
        <spring:theme code="titleOrder"/>
    </h3>
    <div id="error-result">
        <c:if test="${not empty error}">
            <c:out value="${error}"/>
        </c:if>
    </div>
    <br>
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
        <c:forEach var="cartItem" items="${cart.cartItems}">
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
                    <c:out value="${cartItem.quantity}"/>
                </td>
                <td>
                    <c:out value="${cartItem.phone.price}"/> <spring:theme code="usd"/>
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
                <c:out value="${cart.totalCost}"/> <spring:theme code="usd"/>
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
                <c:out value="${deliveryPrice}"/> <spring:theme code="usd"/>
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
                <c:out value="${totalPrice}"/> <spring:theme code="usd"/>
            </td>
        </tr>
    </table>
    <br>
    <form:form method="post" action="${pageContext.request.contextPath}/order" modelAttribute="orderDataDTO">
        <table class="no-border-table">
            <tags:orderRow label="firstNameOrder" id="first-name" name="firstName" errors="${errors}"/>
            <tags:orderRow label="lastNameOrder" id="last-name" name="lastName" errors="${errors}"/>
            <tags:orderRow label="addressOrder" id="address" name="address" errors="${errors}"/>
            <tags:orderRow label="phoneOrder" id="phone" name="phone" errors="${errors}"/>
        </table>
        <textarea id="add-info-to-order" name="additionalInformation" placeholder="Additional information" maxlength="500">
            <c:out value="${param.get('additionalInformation')}"/>
        </textarea>
        <br>
        <button class="buttons">
            <spring:theme code="order"/>
        </button>
    </form:form>
</body>
</html>
