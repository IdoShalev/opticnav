<t:page title="Register Device" js="registerARD.js"
	css="registerDevice.css">
	<div id="registerDeviceContainer">
		<h2>Device Manager</h2>
		<p>To register a device follow the instruction on your Device.</p>
		<p>For more help please refer to the FAQ in the Help page or
			contact us.</p>
		<div id="messageConstraint">
			<t:message name="message" />
			<input type="button" id="device-remove" value="Remove">
		</div>

		<div>
			<form onsubmit="return false" id="registerARD-form">
				<input type="text" name="code" id="code"
					placeholder="Confirmation code" autocomplete="off" />
				<button id="device-register" type="submit">
					Register
					<div id="message-loader" class="loader-adjacent"></div>
				</button>
			</form>
		</div>
	</div>
</t:page>