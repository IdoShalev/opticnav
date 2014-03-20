<t:page title="Register Device" js="registerARD.js">
    <h2>Device Manager</h2>
    <p>To register a device follow the instruction on your Device.</p>
    <p>For more help please refer to the FAQ in the Help page or contact us.</p>
    <form>
    <div class="registered_ard"><span id="ard_id"></span><span id="ard_name"></span></div>
    <input type="submit" value="Remove">
    </form>
    <form id="RegisterARD" action="op/RegisterARD">
    <input type="text" name="code" id="code" placeholder="Confirmation code" autocomplete="off" />
    <input type="submit" value="Register" id="register" />
    <t:message name="message" />
    </form>
</t:page>