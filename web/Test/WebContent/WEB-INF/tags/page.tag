<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="title" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
  <title><c:out value="${title}" /></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/style/main.css" />
</head>

<body><jsp:doBody /></body>

</html>