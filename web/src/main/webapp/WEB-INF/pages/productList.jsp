<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <title><spring:theme code="titlePlp" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:header cart="${cart}"/>
    <p>
    <div>
        <spring:theme code="found" />
        <c:out value="${phoneQuantity}"/> <spring:theme code="phones" />
    </div>
    <form method="get">
        <input name="search" value="${not empty param.search ? param.search : ''}"/>
        <button class="buttons"><spring:theme code="search" /></button>
    </form>
    <div id="success-result">
    </div>
    <table border="1px">
        <thead>
        <tr>
            <td><spring:theme code="image" /></td>
            <td>
                <spring:theme code="brand" />
                <tags:sortLink field="brand" order="asc"/>
                <tags:sortLink field="brand" order="desc"/>
            </td>
            <td>
                <spring:theme code="model" />
                <tags:sortLink field="model" order="asc"/>
                <tags:sortLink field="model" order="desc"/>
            </td>
            <td><spring:theme code="color" /></td>
            <td>
                <spring:theme code="displaySize" />
                <tags:sortLink field="displaySizeInches" order="asc"/>
                <tags:sortLink field="displaySizeInches" order="desc"/>
            </td>
            <td>
                <spring:theme code="price" />
                <tags:sortLink field="price" order="asc"/>
                <tags:sortLink field="price" order="desc"/>
            </td>
            <td><spring:theme code="quantity" /></td>
            <td><spring:theme code="action" /></td>
        </tr>
        </thead>
        <c:forEach var="phone" items="${phones}">
            <form:form method="post" id="${phone.id}" modelAttribute="phoneDTO">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td><c:out value="${phone.brand}"/></td>
                    <td><a href="${pageContext.request.contextPath}/productDetails/${phone.id}"><c:out value="${phone.model}"/></a></td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            <c:out value="${color.code}"/><br>
                        </c:forEach>
                    </td>
                    <td><c:out value="${phone.displaySizeInches}"/>"</td>
                    <td>$ <c:out value="${phone.price}"/></td>
                    <td>
                        <input class="quantity-input" type="text" id="quantity${phone.id}" name="quantity" value="1"/>
                        <div class="result-for-item" id="result${phone.id}">
                        </div>
                        <input id="phoneId${phone.id}" name="phoneId" type="hidden" value="${phone.id}"/>
                    </td>
                    <td>
                        <button class="buttons">
                            <spring:theme code="addToCart" />
                        </button>
                    </td>
                </tr>
            </form:form>
        </c:forEach>
    </table>
    </p>
    <div class="pages-links">
        <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : null}&order=${not empty param.order ? param.order : null}&search=${not empty param.search ? param.search : null}&page=${empty param.page ? 1 : (param.page > 1 ? param.page - 1 : 1)}"><spring:theme code="previousPage"/></a>
        <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : null}&order=${not empty param.order ? param.order : null}&search=${not empty param.search ? param.search : null}&page=${empty param.page ? (pages eq 1 ? 1 : 2) : (param.page < pages ? param.page + 1 : pages)}"><spring:theme code="nextPage"/></a>
    </div>
</body>

<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
<script>
    <c:forEach var="phone" items="${phones}">
    jQuery(document).ready(function ($) {
        $("#${phone.id}").submit(function (event) {
            event.preventDefault();
            addToCart(${phone.id});
        });
    })
    </c:forEach>

    function addToCart(phoneId) {

        var id = $("#phoneId" + phoneId).val();
        var quantity = $("#quantity" + phoneId).val();

        $.ajax({
            type: 'POST',
            url: 'ajaxCart',
            data: 'id=' + id + '&quantity=' + quantity,
            success: function (message) {
                $('.result-for-item').text('');
                var json = JSON.stringify(message);
                var jsonObject = JSON.parse(json);
                $('#cart-quantity').text( jsonObject.totalQuantity);
                $('#cart-cost').text( jsonObject.totalCost);
                $('#success-result').text('Product added to cart successfully');
            },
            error: function (message) {
                $('#success-result').text('');
                var json = JSON.stringify(message);
                var jsonObject = JSON.parse(json);
                var responseTextObject = JSON.parse(jsonObject.responseText);
                if (responseTextObject.errorsMessage !== undefined) {
                    $('#result' + phoneId).text(responseTextObject.errorsMessage);
                } else {
                    $('#result' + phoneId).text(responseTextObject.errors[0].code);
                }
            }
        });
    }
</script>
