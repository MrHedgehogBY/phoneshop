<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<head>
    <title>Product list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <p>
        Hello from product list!
    </p>
    <p>
        Found
        <c:out value="${phones.size()}"/> phones.
    </p>
    <hr>
    <p>
    <div class="under-head">
        <form method="get">
            <input name="search" value="${not empty param.search ? param.search : ''}"/>
            <button>Search</button>
        </form>
        <div id="cart-div">
            Cart: ${cart.totalQuantity} items ${cart.totalCost} $
        </div>
    </div>
    <div id="success-result">
    </div>
    <table border="1px">
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Brand
                <tags:sortLink field="brand" order="asc"/>
                <tags:sortLink field="brand" order="desc"/>
            </td>
            <td>
                Model
                <tags:sortLink field="model" order="asc"/>
                <tags:sortLink field="model" order="desc"/>
            </td>
            <td>Color</td>
            <td>
                Display size
                <tags:sortLink field="displaySize" order="asc"/>
                <tags:sortLink field="displaySize" order="desc"/>
            </td>
            <td>
                Price
                <tags:sortLink field="price" order="asc"/>
                <tags:sortLink field="price" order="desc"/>
            </td>
            <td>Quantity</td>
            <td>Action</td>
        </tr>
        </thead>
        <c:forEach var="phone" items="${phones}">
            <form:form method="post" id="${phone.id}" modelAttribute="phoneDataHolder">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td>${phone.brand}</td>
                    <td>${phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}<br>
                        </c:forEach>
                    </td>
                    <td>${phone.displaySizeInches}"</td>
                    <td>$ ${phone.price}</td>
                    <td>
                        <input class="quantity-input" type="text" id="quantity${phone.id}" name="quantity" value="1"/>
                        <div class="result-error" id="result${phone.id}">

                        </div>
                        <input id="phoneId${phone.id}" name="phoneId" type="hidden" value="${phone.id}"/>
                    </td>
                    <td>
                        <button>
                            Add to cart
                        </button>
                    </td>
                </tr>
            </form:form>
        </c:forEach>
    </table>
    </p>
    <div class="pages-links">
        <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : ''}&order=${not empty param.order ? param.order : ''}&search=${not empty param.search ? param.search : ''}&page=${empty param.page ? 1 : (param.page > 1 ? param.page - 1 : 1)}"><<< Previous page</a>
        <a href="${pageContext.request.contextPath}/productList?field=${not empty param.field ? param.field : ''}&order=${not empty param.order ? param.order : ''}&search=${not empty param.search ? param.search : ''}&page=${empty param.page ? 2 : (param.page < pages ? param.page + 1 : pages)}">Next page >>></a>
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
            success: function () {
                $('#result' + phoneId).text('');
                $('cart-div').text('Cart: ${cart.totalQuantity} items')
                $('#success-result').text('Product added to cart successfully');
            },
            error: function () {
                $('#success-result').text('');
                $('#result' + phoneId).text('Wrong input');
            }
        });
    }
</script>
