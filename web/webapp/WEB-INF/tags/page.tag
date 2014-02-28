<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" required="true" %>
<%@attribute name="css" required="false" %>
<%@attribute name="js" required="false" %>
<t:page_base
    title="${title}"
    css="main.css,${css}"
    js="jquery-2.1.0.min.js,message.js,${js}">
    <link rel="icon" type="image/png" href="./css/images/Weblogo.png"/>
<header>
<div id="nav">
<ul>
    <li><a href="index.jsp"><div id="buttons">Home Page</div></a></li>
    <li><a href="registerAccount.jsp"><div id="buttons">New Account</div></a></li>
    <li><a href="download.jsp"><div id="buttons">Download</div></a></li>
    <li><a href="index.jsp"><div id="logo"></div></a></li>
    <li><a href="contact.jsp"><div id="buttons">Contact Us</div></a></li>
    <li><a href="about.jsp"><div id="buttons">About</div></a></li>
    <li><a href="help.jsp"><div id="buttons">Help</div></a></li>
</ul>
</div>
</header>

<div id="content">
<jsp:doBody />
</div>

<div id=footer>
<ul>
    <li><span>About Us</span></li>
    <li><span>Contact Us</span></li>
    <li><span>Help</span></li>
    <li><span>Get Application</span></li>
    <li><span>OpticNav &copy; 2014</span></li>
</ul>
</div>

</t:page_base>