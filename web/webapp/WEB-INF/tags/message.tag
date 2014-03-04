<%@attribute name="name" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
<c:when test="${empty param[name]}">
<div id="${name}" class="message"></div>
</c:when>
<c:otherwise>
<c:set var="message_ok" value="${name}_ok" />
<div id="${name}" class="message ${param[message_ok]!='0'?'message_ok':'message_error'}">
<c:out value="${param[name]}"/>
</div>
</c:otherwise>
</c:choose>