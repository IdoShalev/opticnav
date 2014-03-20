<t:page title="New Account" js="registerAccount.js">
<div id=center>
    <div id=content>
        <div id=registerPage>
            <h2>Creating Account</h2>
            <p>Please fill in the fields below.</p>
            <p>All fields must be filled in correctly to make a new account.</p>
            <form id= "register_Account">
                <input type="text" id="username" placeholder="Username"><br/>
                <input type="password" id="password" placeholder="Password"><br/>
                <input type="password" id="confirmPassword" placeholder="Confirm Password"><br/>
                <input type="submit" value="Register" />
            </form>
            <t:message name="message" />
        </div>
    </div>
</div>
</t:page>