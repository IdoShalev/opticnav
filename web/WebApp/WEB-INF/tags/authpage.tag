<%@tag description="Auth page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" required="true" %>
<%@attribute name="js" required="false" %>
<t:page title="${title}" js="${js}">
<div class="logo"></div><div class="menu"></div><div class="messages"></div>
<div class="body">
<jsp:doBody />
</div>
<div class="footer"></div>
</t:page>