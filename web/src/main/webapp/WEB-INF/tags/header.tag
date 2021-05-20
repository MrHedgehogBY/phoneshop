<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="cart" type="com.es.core.cart.Cart" required="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div class="header-part">
    <a href="${pageContext.request.contextPath}/productList">
        <h2>
            <spring:theme code="helloMessage" />
        </h2>
    </a>
    <div id="auth-and-cart">
        <tags:authContent/>
        <div id="form-for-cart">
            <form action="${pageContext.request.contextPath}/cart" method="get">
                <button id="cart-button" class="buttons">
                    <spring:theme code="cart"/>&nbsp
                    <span id="cart-quantity"><c:out value="${cart.totalQuantity}"/></span>
                    <spring:theme code="items"/>&nbsp
                    <span id="cart-cost"><c:out value="${cart.totalCost}"/></span>
                    <spring:theme code="usd"/>
                </button>
            </form>
        </div>
    </div>
</div>
<hr>
