<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="item" required="true" %>

<tr>
    <td class="customer-td"><span><spring:theme code="${label}"/></span></td>
    <td class="customer-td"><span><c:out value="${item}"/></span></td>
</tr>