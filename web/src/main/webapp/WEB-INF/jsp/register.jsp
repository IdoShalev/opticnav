<t:page title="New Account" js="registerAccount.js" css="register.css">
	<div id=registerPage>
		<h2>Create an Account</h2>
		<p>You will need to sign up to use the application.</p>
		<h3>Get Started</h3>
		<form id="register-account">
			<input type="text" id="username" placeholder="Username"
				class="registerPage"><input type="password" id="password"
				placeholder="Password" class="registerPage"> <input
				type="password" id="confirmPassword" placeholder="Confirm Password"
				class="registerPage">
			<button type="submit">
				Sign Up
				<div id="message-loader" class="loader-adjacent"></div>
			</button>
		</form>
		<div class="messageConstraint">
			<t:message name="message" />
		</div>
	</div>
</t:page>