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
        <t:link href="/logout">Logout</t:link></c:if></div>
        <div class="logodiv"><t:link href="/"><img class="logo" src="${pageContext.request.contextPath}/css/images/OpticNavLogo.png"></t:link></div>
        <div id="headContainer">
    <div id="nav">
		<ul>
		    <c:if test="${!empty user}">
		    <t:headerLink name="hub" href="/">Hub</t:headerLink>
            <t:headerLink name="registerDevice" href="/registerDevice">Device Manager</t:headerLink>
            <t:headerLink name="map" href="/map">Maps Manager</t:headerLink>
            <t:headerLink name="instance" href="/instance">Instance Manager</t:headerLink>
            </c:if>
            <c:if test="${empty user}">
            <li><t:link href="/"><div class="buttons">Home</div></t:link></li>
		    <li><t:link href="/register"><div class="buttons">Register</div></t:link></li>
		    </c:if>
		    <li><t:link href="/download"><div class="buttons">Get Application</div></t:link></li>
		    <li><t:link href="/support"><div class="buttons">Support</div></t:link></li>
		</ul></div></div>
</header>

<div id="content">
<jsp:doBody />
</div>

<div id=footer>

</div>

</t:page_base>