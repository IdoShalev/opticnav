<t:page title="Register Device" js="registerARD.js">
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