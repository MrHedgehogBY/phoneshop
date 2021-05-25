<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>B2B</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <table>
        <thead>
            <tr>
                <td>Product code</td>
                <td>Quantity</td>
            </tr>
        </thead>
        <c:forEach var="id" items="${successMessage}">
            <div id="success-result">
                ${id} product added successfully
            </div>
        </c:forEach>
        <br>
        <form:form id="b2b-form" method="post" action="${pageContext.request.contextPath}/b2b" modelAttribute="b2bCartDTO">
            <c:forEach var="i" begin="0" end="7" varStatus="status">
                <tr>
                    <td>
                        <input id="code" name="code" value="${fn:contains(errorsCode, i) or fn:contains(errorsQuantity, i) ? paramValues['code'][status.index] : ""}">
                        <c:if test="${fn:contains(errorsCode, i)}">
                            <div class="result-for-item">
                                Product not found
                            </div>
                        </c:if>
                    </td>
                    <td>
                        <input id="quantity" name="quantity" value="${fn:contains(errorsCode, i) or fn:contains(errorsQuantity, i) ? paramValues['quantity'][status.index] : ""}">
                        <c:if test="${fn:contains(errorsQuantity, i)}">
                            <div class="result-for-item">
                                Not a number or out of stock
                            </div>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <button class="buttons" form="b2b-form">
                        Add 2 cart
                    </button>
                </td>
            </tr>
        </form:form>
    </table>
</body>
</html>
