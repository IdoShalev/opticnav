<t:page title="Instance" css="instance.css" js="MapAJAX.js,instanceManager.js">    
<div id="instanceContainer">
    <div id="info">
    <div id="info-modal"></div>
        Instance Information <br/>
        <br/>
        Map: <span id="inst-map-name"></span>
        <br/>
        Start Time: <span id="inst-start-time"></span>
        <br/>
        People in the instance<br/>
        <ul id="inst-selected-users">
            
        </ul><br/>
        <input type="submit" value="Stop Instance" id="stop-instance">
    </div>
    <div id="controller">
    <div id="controller-modal"></div>
        <div id="leftControl">
        Instance Controller<br/>
        <br/>
        <div id="map-container">
        <div id="map-list"></div>
        </div>
        <br/>
        </div>
        
        <div id="rightControl">
        Invite<br/>
        <input type="text" id="invite-to-inst"><br/>
        <input type="button" value="Invite" id="inst-invite"><br/>
        <ul id="inst-select-users">
            
        </ul><br/>
        <input type="submit" value="Start Instance" id="instaB">
        <t:message name="message" />
        </div>
    </div>
</div>
</t:page>