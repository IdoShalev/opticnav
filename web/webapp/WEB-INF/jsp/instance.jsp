<t:page title="Instance" css="instance.css"
	js="MapAJAX.js,instanceManager.js,date.format.js">

	<div class="messageConstraint">
		<t:message name="generic-message" />
	</div>
	<div id="instanceContainer">
		<div id="instance-wrapper">
			<div id="controller">
				<h1>Start an instance</h1>
				<div id="controlCenter">
                    <div id="instControlBox">
					<table>
					   <tr><td><b>Select a map</b></td><td><b>Invite list:</b></td></tr>
					   <tr><td><div id="map-container">
                                <div id="map-list"></div>
                            </div></td><td><div id="section-invite">
                                <input type="text" id="invite-to-inst" placeholder="Username">
                                <button id="inst-invite">
                                    Invite
                                    <div id="invite-loader" class="loader-adjacent"></div>
                                </button><br/><button type="submit" id="instaB">
                                    Start Instance
                                    <div id="start-instance-loader" class="loader-adjacent"></div>
                                </button>
                                <div id="selected-list">
                                    <ul id="inst-select-users">
                                    </ul>
                                </div></div>
                              </td></tr><tr><td><div class="messageConstraint">
                                <t:message name="start-instance-message" />
                            </div></td></tr>
					   </table>
                    </div>
                 </div>
            </div>
			<div id="info">
				<h1>Current instance information</h1>
				<div id="infoCenter">
					<div id="instInfoBox">
	                    <table id="instanceInfo">
	                    <tr><td><b>Map:</b></td><td><span id="inst-map-name"></span></td></tr>
	                    <tr><td><b>Start Time:</b></td><td><span id="inst-start-time"></span></td></tr>
	                    <tr><td><b>In the instance:</b></td></tr>
						</table>
						<div id="selected-list">
						<ul id="inst-selected-users">
	
						</ul>
						</div>
						<br />
						<button type="submit" id="stop-instance">
							Stop Instance
							<div id="stop-instance-loader" class="loader-adjacent"></div>
						</button>
						<div class="messageConstraint">
                            <t:message name="instance-info-message" />
                        </div>
					</div>
				</div>
			</div>
			</div>
		</div>
	
</t:page>