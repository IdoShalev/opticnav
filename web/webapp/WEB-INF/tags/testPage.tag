<%@tag description="Page template tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="script" fragment="true" %>
<%@ attribute name="content" fragment="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:page_base
    title="Test"
    css="main.css"
    js="jquery-2.1.0.min.js,message.js">
<script>
<jsp:invoke fragment="script" />
</script>
<div id="content">
<jsp:invoke fragment="content" />
</div>
</t:page_base>