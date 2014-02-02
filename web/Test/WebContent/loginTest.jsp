<t:page title="Login Test" js="loginTest.js">
	<t:message name="message" />
	<form id="login" action="Login">
	<fmt:message key="login.username"/>: <input type="text" name="user" id="user" /><br />
	<fmt:message key="login.password"/>: <input type="password" name="pass" id="pass" /><br />
	<input type="submit" value="<fmt:message key="login" />" />
	</form>
</t:page>