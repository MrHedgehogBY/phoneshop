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
    <table class="no-border-table">
        <thead>
            <tr>
                <td class="no-border-td">Product code</td>
                <td class="no-border-td">Quantity</td>
            </tr>
        </thead>
        <c:forEach var="id" items="${addedToCartProductIds}">
            <div id="success-result">
                ${id} product added successfully
            </div>
        </c:forEach>
        <br>
        <form:form id="b2b-form" method="post" action="${pageContext.request.contextPath}/b2b" commandName="b2bCartDTO">
            <c:forEach var="i" begin="0" end="7">
                <tr>
                    <td class="no-border-td">
                        <form:input path="b2bCartItems[${i}].id" />
                        <div>
                            <form:errors path="b2bCartItems[${i}].id" cssClass="result-for-item"/>
                        </div>
                    </td>
                    <td class="no-border-td">
                        <form:input path="b2bCartItems[${i}].quantity" />
                        <div>
                            <form:errors path="b2bCartItems[${i}].quantity" cssClass="result-for-item"/>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="no-border-td">
                    <button class="buttons" form="b2b-form">
                        Add 2 cart
                    </button>
                </td>
            </tr>
        </form:form>
    </table>
</body>
</html>
