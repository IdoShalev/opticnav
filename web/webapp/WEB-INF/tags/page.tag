<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" required="true" %>
<%@attribute name="css" required="false" %>
<%@attribute name="js" required="false" %>
<t:page_base
    title="${title}"
    css="main.css,${css}"
    js="jquery-2.1.0.min.js,message.js,${js}">
<header>
<div id="cssmenu">
<ul>
    <li>
    <span><a href="index.jsp">Home Page</a></span>
    <ul>
        <li><span><a href="registerAccount.jsp">New Account</a></span></li>
        <li><span><a href="contact.jsp">Contact Us</a></span></li>
        <li><span><a href="about.jsp">About</a></span></li>
        <li><span><a href="help.jsp">Help</a></span></li>
    </ul>
    </li>
</ul>
</div>
</header>

<div id="content">
<jsp:doBody />
</div>

<footer>
<ul class="footer">
    <li class="footer">About Us</li>
    <li class="footer">Contact Us</li>
    <li class="footer">Help</li>
    <li class="footer">Get Application</li>
    <li class="footer">Copyright &copy; 2014</li>
</ul>
</footer>

</t:page_base>