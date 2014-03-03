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
<c:if test="true">

</c:if>
<c:if test="false">
    <div id="nav">
		<ul>
		    <li><t:link href="/"><div id="buttons">Home Page</div></t:link></li>
		    <li><t:link href="/register"><div id="buttons">New Account</div></t:link></li>
		    <li><t:link href="/download"><div id="buttons">Get Application</div></t:link></li>
		    <li><t:link href="/"><div id="logo"></div></t:link></li>
		    <li><t:link href="/contact"><div id="buttons">Contact Us</div></t:link></li>
		    <li><t:link href="/about"><div id="buttons">About</div></t:link></li>
		    <li><t:link href="/help"><div id="buttons">Help</div></t:link></li>
		</ul>
	</div>
</c:if>

</header>

<div id="content">
<jsp:doBody />
</div>

<div id=footer>
<ul>
    <li><t:link href="/about"><div id="buttons">About</div></t:link></li>
    <li><t:link href="/contact"><div id="buttons">Contact Us</div></t:link></li>
    <li><t:link href="/help"><div id="buttons">Help</div></t:link></li>
    <li><t:link href="/download"><div id="buttons">Get Application</div></t:link></li>
</ul>
</div>

</t:page_base>