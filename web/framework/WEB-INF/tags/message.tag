<%--
The message tag expands to a div that can be used with and without JS.
If no request attribute is set, the div is initially invisible.
--%>
<%@attribute name="name" required="true" %><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="<c:out value="${name}" />" class="message <c:if test="${empty requestScope[name]}">invisible</c:if>"><c:out value="${requestScope[name]}" /></div>
