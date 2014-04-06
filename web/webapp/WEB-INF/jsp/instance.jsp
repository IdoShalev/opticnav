<t:page title="Instance" css="instance.css"
	js="MapAJAX.js,instanceManager.js,date.format.js">
	<div id="instanceContainer">
		<div id="info">
			<div id="info-modal"></div>
			Instance Information <br /> <br /> Map: <span id="inst-map-name"></span>
			<br /> Start Time: <span id="inst-start-time"></span> <br /> People
			in the instance<br />
			<ul id="inst-selected-users">

			</ul>
			<br />
			<button type="submit" id="stop-instance">Stop Instance</button>
		</div>
		<div id="controller">
			<div id="controller-modal"></div>
			<div class="messageConstraint">
				<t:message name="message" />
			</div>
			<div>
				<div id="leftControl">
					<div id="map-container">
						Maps
						<div id="map-list"></div>
					</div>
				</div>

				<div id="rightControl">
					<div id="section-invite">
						<input type="text" id="invite-to-inst" placeholder="Username">
						<button id="inst-invite">
							Invite
							<div id="invite-loader" class="loader-adjacent"></div>
						</button>
						<div id="selected-list">
							<ul id="inst-select-users">
							</ul>
						</div>
						<button type="submit" id="instaB">
							Start Instance
							<div id="start-instance-loader" class="loader-adjacent"></div>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</t:page>