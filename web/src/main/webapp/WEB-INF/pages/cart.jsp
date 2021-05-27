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
    <c:if test="${not empty isEmpty}">
        <h2><c:out value="${isEmpty}"/></h2>
    </c:if>
    <c:if test="${empty isEmpty}">
        <form:form id="update-form" method="post" action="${pageContext.request.contextPath}/cart" commandName="phoneArrayDTO">
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
                <c:forEach var="i" begin="0" end="${cart.cartItems.size() - 1}">
                    <tr>
                        <td>
                            <c:out value="${cart.cartItems.get(i).phone.brand}"/>
                        </td>
                        <td>
                            <c:out value="${cart.cartItems.get(i).phone.model}"/>
                        </td>
                        <td>
                            <c:forEach var="color" items="${cart.cartItems.get(i).phone.colors}">
                                <c:out value="${color.code}"/>
                            </c:forEach>
                        </td>
                        <td>
                            <c:out value="${cart.cartItems.get(i).phone.displaySizeInches}"/>"
                        </td>
                        <td>
                            <c:out value="${cart.cartItems.get(i).phone.price}"/><spring:theme code="usd"/>
                        </td>
                        <td>
                            <form:input path="phoneDTOItems[${i}].id" type="hidden"/>
                            <form:input path="phoneDTOItems[${i}].quantity"/>
                            <br>
                            <form:errors path="phoneDTOItems[${i}].quantity" cssClass="result-for-item"/>
                            <span id="success-result"><c:out value="${fn:contains(updatedPhoneIds, cart.cartItems.get(i).phone.id) ? successfulUpdateMessage : ''}"/></span>
                        </td>
                        <td>
                            <button class="buttons" formmethod="post" formaction="${pageContext.request.contextPath}/cart/${cart.cartItems.get(i).phone.id}">
                                <spring:theme code="deleteFromCart"/>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </form:form>
        <form id="to-order-form" action="${pageContext.request.contextPath}/order">
        </form>
        <div id="cart-buttons">
            <button class="buttons" id="update-cart" form="update-form">
                <spring:theme code="updateCart" />
            </button>
            <button class="buttons" id="order-cart" form="to-order-form">
                <spring:theme code="orderCart" />
            </button>
        </div>
    </c:if>

</body>
</html>
