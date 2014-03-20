<t:page title="Maps Manager" css="map.css" js="Map.js,MapController.js,MapCoordHelper.js,MapManager.js,jquery.ba-resize.min.js">
<t:message name="MapMessage"/>
<div id="map-manager-container">
<div id="map-manager">
<div id="map-creation" class="modal-dialog">
    <input type="text" id="map-creation-name" placeholder="Map Name" />
    <input type="file" id="map-creation-file" /><br/>
    <button id="map-creation-create">Create</button>
    <button id="map-creation-cancel" style="margin-left:250px">Cancel</button>
</div>
<div id="modal-backdrop"></div>
<sidebar>
    <a href="javascript:createMap()"><div class="create-button">
    Create a New Map
    </div></a>
    <a href="javascript:MapController.saveMap()"><div id="save-button" class="save-button">
    Save Changes
    </div></a>
    <div id="map-list"></div>
    <a href="javascript:MapController.toggleAnchorMode()"><div id="placement-mode" class="anchor-mode-button">
    Anchor Placement mode: OFF
    </div></a>
</sidebar>
<div id="map-view">
    <div id="map-popups">
        <div id="anchor-popup">
        <button id="anchor-delete">Delete</button>
        <input type="text" id="anchor-lat" placeholder="Latitude" />
        <input type="text" id="anchor-lng" placeholder="Longitude" />
        <button id="anchor-save">Save</button>
        </div>
        
        <div id="marker-popup">
        <button id="marker-delete">Delete</button>
        <input type="text" id="marker-name" placeholder="Marker name" /><br/>
        <input type="text" id="marker-lat" placeholder="Latitude" />
        <input type="text" id="marker-lng" placeholder="Longitude" />
        <button id="marker-save">Save</button>
        </div>
    </div>
    <div id="map-anchors"></div>
    <div id="map-markers"></div>
    <img id="map-image" />
</div>
</div>
</div>

</t:page>
