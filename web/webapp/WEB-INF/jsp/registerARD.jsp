<t:page title="Register Device" js="registerARD.js">
    <h2>Device Manager</h2>
    <p>To register a device follow the instruction on your Device.</p>
    <p>For more help please refer to the FAQ in the Help page or contact us.</p>
    <t:message name="message" />
    <button id="device-remove">Remove</button>
    
    <form onsubmit="return false">
    <input type="text" name="code" id="code" placeholder="Confirmation code" autocomplete="off" />
    <button id="device-register" type="submit">Register device</button>
    </form>
</t:page>