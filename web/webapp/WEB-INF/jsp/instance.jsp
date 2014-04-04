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
    <h2>Instance Controller</h2>
        <div id="leftControl">
        <div id="map-container">
        Select a map
        <div id="map-list"></div>
        </div>
        </div>
        
        <div id="rightControl">
        <div id="rightForm">
        Invite to instance<br/>
        <input type="text" id="invite-to-inst"><br/>
        <input type="button" value="Invite" id="inst-invite"><br/>
        <div id="selected-list">
        <ul id="inst-select-users">
            
        </ul></div><br/>
        <input type="submit" value="Start Instance" id="instaB">
        </div>
        <div id="messageConstraint">
        <t:message name="message" />
        </div>
        </div>
    </div>
</div>
</t:page>