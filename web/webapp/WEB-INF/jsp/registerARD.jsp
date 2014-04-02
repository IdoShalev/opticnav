<t:page title="Register Device" js="registerARD.js">
    <h2>Device Manager</h2>
    <p>To register a device follow the instruction on your Device.</p>
    <p>For more help please refer to the FAQ in the Help page or contact us.</p>
    <div id="messageConstraint"><t:message name="message" /></div>
    
    <div id="registerARD-form">
    <input type="button" id="device-remove" value="Remove">
    <form onsubmit="return false">
    <input type="text" name="code" id="code" placeholder="Confirmation code" autocomplete="off" />
    <input id="device-register" type="submit" value="Register">
    </form>
    </div>
</t:page>