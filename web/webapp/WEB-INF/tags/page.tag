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
<div id="nav">
<ul>
    <li><a href="index.jsp"><span>Home Page</span></a></li>
    <li><a href="registerAccount.jsp"><span>New Account</span></a></li>
    <li><a href="download.jsp"><span>Download</span></a></li>
    <%--DO NOT DELETE thank you.<li><div id="logo"><a href="index.jsp"><span></span></a></div></li> --%>
    <li><a href="contact.jsp"><span>Contact Us</span></a></li>
    <li><a href="about.jsp"><span>About</span></a></li>
    <li><a href="help.jsp"><span>Help</span></a></li>
</ul>
</div>
</header>

<div id="content">
<jsp:doBody />
</div>

<footer>
<ul class="footer">
    <li class="footer"><span>About Us</span></li>
    <li class="footer"><span>Contact Us</span></li>
    <li class="footer"><span>Help</span></li>
    <li class="footer"><span>Get Application</span></li>
    <li class="footer"><span>Copyright &copy; 2014</span></li>
</ul>
</footer>

</t:page_base>