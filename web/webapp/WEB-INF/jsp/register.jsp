<t:page title="New Account" js="registerAccount.js" css="register.css">
<div id=registerPage>
    <h2>Create an Account</h2>
    <p>You will need to sign up to use the application.</p>
    <h3>Get Started</h3>
    <form id= "register_Account">
        <input type="text" id="username" placeholder="Username" class="registerPage"><br/>
        <input type="password" id="password" placeholder="Password" class="registerPage"><br/>
        <input type="password" id="confirmPassword" placeholder="Confirm Password" class="registerPage"><br/>
        <input type="submit" value="Sign Up" />
    </form>
    <t:message name="message" />
</div>
</t:page>