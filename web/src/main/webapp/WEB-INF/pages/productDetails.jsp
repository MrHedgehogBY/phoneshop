<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><c:out value="${phone.model}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
    <tags:header cart="${cart}"/>
    <input class="buttons" type="button" onclick="history.go(-1);" value="<spring:theme code="backToPdp"/>"/>
    <div id="success-result">
    </div>
    <div id="pdp-content">
        <div id="left-part">
            <h3>
                <c:out value="${phone.model}"/>
            </h3>
            <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"/>
            <p>
                <c:out value="${phone.description}"/>
            </p>
            <div class="price-n-button-add">
                <h4>
                    <spring:theme code="pricePdp"/> <c:out value="${phone.price}"/> <spring:theme code="usd"/>
                </h4>
                <form:form id="addToCartForm" method="post" modelAttribute="phoneDTO">
                    <input class="quantity-input" type="text" id="quantity" name="quantity" value="1"/>
                    <button class="buttons">
                        <spring:theme code="addToCart" />
                    </button>
                    <input id="phoneId" name="phoneId" type="hidden" value="${phone.id}"/>
                    <div id="result">
                    </div>
                </form:form>
            </div>
        </div>
        <div id="right-part">
            <div>
                <h4><spring:theme code="displayPdp"/></h4>
                <table>
                    <tr>
                        <td><spring:theme code="sizePdp"/></td>
                        <td><c:out value="${phone.displaySizeInches}"/>"</td>
                    </tr>
                    <tr>
                        <td><spring:theme code="resolutionPdp"/></td>
                        <td><c:out value="${phone.displayResolution}"/></td>
                    </tr>
                    <tr>
                        <td><spring:theme code="technologyPdp"/></td>
                        <td><c:out value="${phone.displayTechnology}"/></td>
                    </tr>
                    <tr>
                        <td><spring:theme code="pixelDensityPdp"/></td>
                        <td><c:out value="${phone.pixelDensity}"/></td>
                    </tr>
                </table>
            </div>
            <div>
                <h4><spring:theme code="dimensionsWeightPdp"/></h4>
                <table>
                    <tr>
                        <td><spring:theme code="lengthPdp"/></td>
                        <td><c:out value="${phone.lengthMm} "/>
                            <c:if test="${not empty phone.lengthMm}">
                                <spring:theme code="mmPdp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="widthPdp"/></td>
                        <td><c:out value="${phone.widthMm} "/>
                            <c:if test="${not empty phone.lengthMm}">
                                <spring:theme code="mmPdp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="weightPdp"/></td>
                        <td><c:out value="${phone.weightGr}"/></td>
                    </tr>
                </table>
            </div>
            <div>
                <h4><spring:theme code="cameraPdp"/></h4>
                <table>
                    <tr>
                        <td><spring:theme code="frontPdp"/></td>
                        <td><c:out value="${phone.frontCameraMegapixels} "/>
                            <c:if test="${not empty phone.frontCameraMegapixels}">
                                <spring:theme code="mpxPdp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="backPdp"/></td>
                        <td><c:out value="${phone.backCameraMegapixels} "/>
                            <c:if test="${not empty phone.backCameraMegapixels}">
                                <spring:theme code="mpxPdp"/>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <h4><spring:theme code="batteryPdp"/></h4>
                <table>
                    <tr>
                        <td><spring:theme code="talkTimePdp"/></td>
                        <td><c:out value="${phone.talkTimeHours} "/>
                            <c:if test="${not empty phone.talkTimeHours}">
                                <spring:theme code="hoursPdp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="standByTimePdp"/></td>
                        <td><c:out value="${phone.standByTimeHours} "/>
                            <c:if test="${not empty phone.standByTimeHours}">
                                <spring:theme code="hoursPdp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="batteryCapacityPdp"/></td>
                        <td><c:out value="${phone.batteryCapacityMah} "/>
                            <c:if test="${not empty phone.batteryCapacityMah}">
                                <spring:theme code="mAhPdp"/>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <h4><spring:theme code="otherPdp"/></h4>
                <table>
                    <tr>
                        <td><spring:theme code="colorPdp"/></td>
                        <td>
                            <c:forEach var="color" items="${phone.colors}">
                                <c:out value="${color.code}"/><br>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td><spring:theme code="deviceTypePdp"/></td>
                        <td><c:out value="${phone.deviceType}"/></td>
                    </tr>
                    <tr>
                        <td><spring:theme code="bluetoothPdp"/></td>
                        <td><c:out value="${phone.bluetooth}"/></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</body>

<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
<script>
    jQuery(document).ready(function ($) {
        $("#addToCartForm").submit(function (event) {
            event.preventDefault();
            addToCart();
        });
    })

    function addToCart() {

        var id = $("#phoneId").val();
        var quantity = $("#quantity").val();

        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/ajaxCart',
            data: 'id=' + id + '&quantity=' + quantity,
            success: function (message) {
                $('#result').text('');
                var json = JSON.stringify(message);
                var jsonObject = JSON.parse(json);
                $('#cart-quantity').text(jsonObject.totalQuantity);
                $('#cart-cost').text(jsonObject.totalCost);
                $('#success-result').text('Product added to cart successfully');
            },
            error: function (message) {
                $('#success-result').text('');
                var json = JSON.stringify(message);
                var jsonObject = JSON.parse(json);
                var responseTextObject = JSON.parse(jsonObject.responseText);
                if (responseTextObject.errorsMessage !== undefined) {
                    $('#result').text(responseTextObject.errorsMessage);
                } else {
                    $('#result').text(responseTextObject.errors[0].code);
                }
            }
        });
    }
</script>

</html>
