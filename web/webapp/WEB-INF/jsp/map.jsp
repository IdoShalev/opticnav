<t:page title="Maps Manager" css="map.css" js="Map.js,MapController.js,MapCoordHelper.js,MapManager.js">
<t:message name="MapMessage"/>
<div style="min-height: 560px;position: relative">
<div id="map-manager">
<div id="map-creation" class="modal-dialog">
    <input type="text" id="map-creation-name" placeholder="Map Name" />
    <input type="file" id="map-creation-file" /><br/>
    <button id="map-creation-create">Create</button>
    <button id="map-creation-cancel">Cancel</button>
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
    <a href="javascript:toggleAnchorMode()"><div class="anchor-mode-button">
    Toggle anchor placement mode
    </div></a>
</sidebar>
<div id="map-view">
    <div id="map-popups">
        <div id="marker-popup">
        <button id="delete">Delete</button>
        <input type="text" id="marker-name" placeholder="Marker name" /><br/>
        <input type="text" id="marker-lat" placeholder="Latitude" />
        <input type="text" id="marker-lng" placeholder="Longitude" />
        <button id="save">Save</button>
        </div>
    </div>
    <div id="map-markers"></div>
    <img id="map-image" />
</div>
</div>
</div>

</t:page>
