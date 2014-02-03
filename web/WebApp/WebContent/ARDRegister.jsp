<t:page title="Register Device">
    <h1>Register Device</h1>
    Current Registered Device:<br/>
    <p style="border-style:solid; border-color:grey; background-color:light-grey;">None</p>
    <form id="RegisterARD" action="op/RegisterARD">
    Confirmation Code:<br/> <input type="text" name="code" id="code" />
    <input type="submit" value="Register" id="register" />
    <t:message name="message" />
    </form>
</t:page>