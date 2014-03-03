<t:page title="New Account" js="registerAccount.js">
<div id=center>
    <div id=content>
        <div id=registerPage>
            <t:message name="message" />
            Welcoming new user text and stuff.
            <form id= "register_Account" action="registerAccount.jsp">
                Username: <input type="text" id="username"><br/>
                Password: <input type="password" id="password"><br/>
                Confirm Password: <input type="password" id="password"><br/>
                <input type="submit" value="Register" />
            </form>
        </div>
    </div>
</div>
</t:page>