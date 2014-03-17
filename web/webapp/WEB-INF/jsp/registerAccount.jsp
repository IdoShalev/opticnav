<t:page title="New Account" js="registerAccount.js">
<div id=center>
    <div id=content>
        <div id=registerPage>
            <t:message name="message" />
            Welcoming new user text and stuff.
            <form id= "register_Account" action="registerAccount.jsp">
                <input type="text" id="username" placeholder="Username"><br/>
                <input type="password" id="password" placeholder="Password"><br/>
                <input type="password" id="password" placeholder="Confirm Password"><br/>
                <input type="submit" value="Register" />
            </form>
        </div>
    </div>
</div>
</t:page>