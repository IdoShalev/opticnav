<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" required="true" %>
<%@attribute name="css" required="false" %>
<%@attribute name="js" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:page_base
    title="${title}"
    css="main.css,${css}"
    js="jquery-2.1.0.min.js,message.js,${js}">
    <link rel="icon" type="image/png" href="./css/images/Weblogo.png"/>
<header>
<div id="loggedin">
        <c:if test="${!empty user}">Welcome, <c:out value="${user.username}"/>
        <br/><t:link href="/logout">Logout</t:link></c:if></div>
        <div class="logodiv"><t:link href="/"><img class="logo" src="${pageContext.request.contextPath}/css/images/OpticNavLogo.png"></t:link></div>
        <div id="headContainer">
    <div id="nav">
		<ul>
		    <c:if test="${!empty user}">
		    <li><t:link href="/"><div class="buttonsS" id="${cssId}">Hub</div></t:link></li>
            <li><t:link href="/registerDevice"><div class="buttonsS" id="${cssId}">Device Manager</div></t:link></li>
            <li><t:link href="/map"><div class="buttonsS" id="${cssId}">Maps Manager</div></t:link></li>
            <li><t:link href="/instance"><div class="buttonsS" id="${cssId}">Instance Manager</div></t:link></li>
            </c:if>
            <c:if test="${empty user}">
            <li><t:link href="/"><div class="buttons" id="${cssId}">Home</div></t:link></li>
		    <li><t:link href="/register"><div class="buttons"id="${cssId}">Register</div></t:link></li>
		    </c:if>
		    <li><t:link href="/download"><div class="buttons"id="${cssId}">Get Application</div></t:link></li>
		    <li><t:link href="/support"><div class="buttons"id="${cssId}">Support</div></t:link></li>
		</ul></div></div>
</header>

<div id="content">
<jsp:doBody />
</div>

<div id=footer>

</div>

</t:page_base>