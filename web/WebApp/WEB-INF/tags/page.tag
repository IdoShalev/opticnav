<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="title" required="true" %>
<%@attribute name="js" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title><c:out value="${title}" /></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/main.css" />
  <script src="${pageContext.request.contextPath}/js/jquery-2.1.0.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/message.js"></script>
  <c:forTokens items="${js}" delims="," var="jscript">
  <script src="${pageContext.request.contextPath}/js/${jscript}"></script></c:forTokens>
</head>

<div id="header">
<div class="logo"></div>
<div id="cssmenu"><ul>
    <li><span><a href="index.jsp">Home Page</a></span><ul>
        <li><span><a href="registerAccount.jsp">New Account</a></span></li>
        <li><span><a href="contact.jsp">Contact Us</a></span></li>
        <li><span><a href="about.jsp">About</a></span></li>
        <li><span><a href="help.jsp">Help</a></span></li>
        </ul></li>
        </ul>
</div>
<div class="messages"></div>
</div>
<div id="content" class="body">
<jsp:doBody />
</div>

<div id="footer"><ul class="footer">
    <li class="footer">About Us</li>
    <li class="footer">Contact Us</li>
    <li class="footer">Help</li>
    <li class="footer">Get Application</li>
    <li class="footer">Copyright © 2014, OpticNav</li>
</ul></div>
 
<%-- <body><jsp:doBody /></body> --%>


</html>