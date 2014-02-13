<%@tag description="Auth page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" required="true" %>
<%@attribute name="js" required="false" %>
<t:page title="${title}" js="${js}">
<div class="logo"></div><div id="cssmenu"><ul><li><span>Menu</span><ul><li><span>Start Session</span></li><li><span>Maps</span></li><li><span>Register Device</span></li><li><span>Logout</span></li></ul></li></ul></div><div class="messages"></div>
<div class="body">
<jsp:doBody />
</div>
<div><ul class="footer">
    <li class="footer">About Us</li>
    <li class="footer">Contact Us</li>
    <li class="footer">Help</li>
    <li class="footer">Get Application</li>
    <li class="footer">Copyright © 2014, OpticNav</li>
</ul></div>
</t:page>