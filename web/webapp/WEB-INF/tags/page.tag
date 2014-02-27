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
    <li><div id="buttons"><a href="index.jsp">Home Page</a></div></li>
    <li><div id="buttons"><a href="registerAccount.jsp">New Account</a></div></li>
    <li><div id="buttons"><a href="download.jsp">Download</a></div></li>
    <li><a href="index.jsp"><div id="logo"></div></a></li>
    <li><div id="buttons"><a href="contact.jsp">Contact Us</a></div></li>
    <li><div id="buttons"><a href="about.jsp">About</a></div></li>
    <li><div id="buttons"><a href="help.jsp">Help</a></div></li>
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