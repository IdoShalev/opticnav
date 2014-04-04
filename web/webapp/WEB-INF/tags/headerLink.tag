<%@attribute name="name" required="true" %>
<%@attribute name="href" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<li><a href="${pageContext.request.contextPath}${href}"><div class="buttons <c:if test="${name==currentPage}">
currentPage
</c:if>">
<jsp:doBody />
</div></a></li>