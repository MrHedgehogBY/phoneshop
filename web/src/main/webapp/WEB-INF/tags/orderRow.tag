<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="errors" required="true" type="org.springframework.validation.BindingResult" %>

<tr>
    <td class="customer-td"><label for="${id}"><spring:theme code="${label}"/></label><span class="required">*</span></td>
    <td class="customer-td">
        <input id="${id}" type="text" name="${name}" value="${param.get(name)}"/><br>
        <div class="result-for-item">
            <c:if test="${not empty errors.getFieldErrors(name)}">
                <c:if test="${errors.getFieldErrors(name)[0].code eq 'emptyField'}">
                    <spring:theme code="valueRequired"/>
                </c:if>
                <c:if test="${errors.getFieldErrors(name)[0].code ne 'emptyField'}">
                    <spring:theme code="wrongInput"/>
                </c:if>
            </c:if>
        </div>
    </td>
</tr>