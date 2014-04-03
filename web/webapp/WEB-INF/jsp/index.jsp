<t:page title="Welcome to OpticNav" js="login.js" css="front.css">
<div id="contentHolder">
 <div id="rightLogin">
         <form id="login" action="index.jsp">
             <input type="text" placeholder="Username" id="username"><br/>
             <input type="password" placeholder="Password" id="password"><br/>
             <input type="submit" value="Login" />
         </form>
         Don't have an account? <t:link href="/register">Register now.</t:link>
         <t:message name="message" />
    </div>
    <div id="leftContent">
        <h2>Welcome to OpticNav</h2>
        <ul>
            <li>Upload your own maps</li>
            <li>Create your own instances</li>
            <li>Free to use</li>
        </ul>
    </div>
</div>
</t:page>