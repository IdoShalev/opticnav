<t:page title="Welcome to OpticNav" js="login.js" css="front.css">
<div id="contentHolder">
<h1>Welcome to OpticNav</h1>
<hr></hr>
 <div id="rightLogin">
         <form id="login" action="index.jsp">
             <input type="text" placeholder="Username" id="username"><br/>
             <input type="password" placeholder="Password" id="password"><br/>
             <input type="submit" value="Login" />
         </form>
         Don't have an account? <t:link href="/register">Register now.</t:link>
         <div id="messageConstraint">
         <t:message name="message"/>
         </div>
    </div>
    <div id="leftContent">
        <div id="slideShow">
            <div id="img1"></div>
            <div id="img2"></div>
            <div id="img3"></div>
            <div id="img4"></div>
        </div>
    </div>
</div>
</t:page>