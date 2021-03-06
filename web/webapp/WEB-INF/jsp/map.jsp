<t:page title="Maps Manager" css="map.css"
	js="Map.js,MapController.js,MapCoordHelper.js,MapManager.js,MapAJAX.js">
	<div id="map-manager-container">
		<div id="map-manager">
			<div id="map-creation" class="modal-dialog">
				<div id="boxSize">
					<input type="text" id="map-creation-name" placeholder="Map Name" />
					<input type="file" id="map-creation-file" /><br />
					<button type="submit" id="map-creation-create">Create</button>
					<button class="remove" id="map-creation-cancel">Cancel</button>
				</div>
			</div>
			<div id="alert-popup" class="modal-dialog">
				<p id="alert-message"></p>
				<button id="alert-ok">Ok</button>
			</div>
			<div id="modal-backdrop"></div>
			<sidebar> <a href="javascript:createMap()"><div
					class="create-button">Create a New Map</div></a> <a
				href="javascript:MapController.saveMap()"><div id="save-button"
					class="save-button">Save Changes</div></a> <a
				href="javascript:deleteMap()"><div id="del-button"
					class="del-button">Delete Map</div></a>
			<div id="map-list"></div>
			<a href="javascript:MapController.toggleAnchorMode()"><div
					id="placement-mode" class="anchor-mode-button">Anchor
					Placement mode: OFF</div></a> </sidebar>
			<div id="map-loader" class="loader-large"></div>
			<div id="map-view">
				<div id="map-message-container">
					<t:message name="MapMessage" />
				</div>
				<div id="map-popups">
					<div id="anchor-popup">
						<button class="remove" id="anchor-delete">Delete</button>
						<input type="text" id="anchor-lat" placeholder="Latitude (x x x.xx N/S)" /> <input
							type="text" id="anchor-lng" placeholder="Longitude (x x x.xx E/W)" />
						<button id="anchor-save">Save</button>
						<button class="remove" id="anchor-cancel">Cancel</button>
					</div>

					<div id="marker-popup">
						<button class="remove" id="marker-delete">Delete</button>
						<input type="text" id="marker-name" placeholder="Marker name" /><br />
						<input type="text" id="marker-lat" placeholder="Latitude (x x x.xx N/S)" /> <input
							type="text" id="marker-lng" placeholder="Longitude (x x x.xx E/W)" />
						<button id="marker-save">Save</button>
						<button class="remove" id="marker-cancel">Cancel</button>
					</div>
				</div>
				<div id="map-anchors"></div>
				<div id="map-markers"></div>
				<img id="map-image" />
			</div>
		</div>
	</div>

</t:page>
