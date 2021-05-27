<%@ tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="name" required="true" %>

<tr>
    <td class="no-border-td"><label for="${id}"><spring:theme code="${label}"/></label><span class="required">*</span></td>
    <td class="no-border-td">
        <form:input id="${id}" path="${name}"/>
        <br>
        <form:errors path="${name}" cssClass="result-for-item"/>
    </td>
</tr>