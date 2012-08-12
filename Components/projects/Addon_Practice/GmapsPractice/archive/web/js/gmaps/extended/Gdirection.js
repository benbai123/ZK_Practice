gmaps.extended.Gdirection = zk.$extends(zul.Widget, {
	$define: {
		/**
		 * sets the mapId of the map for direction display
		 * @param v
		 */
		mapId: function (v) {
			var map,
				service,
				direction;
			map = this._map = zk.Widget.$('#' + v);
			if (service = this._directionsService) {
				// do route if ready
				if (direction = this._direction)
					this.setDirection(direction, {force: true});
			} else
				this._init();
		},
		/**
		 * sets the panelId of the panel for direction display
		 * @param v
		 */
		panelId: function (v) {
			var panel,
				service,
				direction;
			panel = this._panel = jq('#' + v)[0];
			if (service = this._directionsService) {
				// do route if ready
				if (direction = this._direction)
					this.setDirection(direction, {force: true});
			} else
				this._init();
		},
		/**
		 * sets the direction to route
		 * @param v
		 */
		direction: function (v) {
			var display = this._directionsDisplay;

			if (display = this._directionsDisplay) {
				var s = v? $eval(v) : null,
						service;
				// wrong arguments or not binded
				if (s.length != 2 || !(service = this._directionsService)) return;
				var start = s[0],
					end = s[1],
					request = {
						origin: start,
						destination: end,
						travelMode: google.maps.DirectionsTravelMode.DRIVING
					};

				service.route(request, function(response, status) {
					if (status == google.maps.DirectionsStatus.OK) {
						display.setDirections(response);
					}
				});
			}
		}
	},
	bind_: function () {
		this.$supers(gmaps.extended.Gdirection, 'bind_', arguments);
		this._tryBind();
	},
	
	_tryBind: function () {
		var mapId, panelId;
		// init if google api, mapId and panelId are ready
		if (window.google && window.google.maps
			&& (mapId = this._mapId)
			&& (panelId = this._panelId))
			this._init();
		else if ((mapId = this._mapId)
				&& (panelId = this._panelId)) {
			// retry if the info for init is ready
			var wgt = this;
			setTimeout (function () {wgt._tryBind()}, 100);
		}
	},
	_init: function () {
		var mapId = this._mapId,
			panelId = this._panelId,
			map,
			panel,
			direction,
			directionsDisplay;

		if (!(map = this._map))
			map = this._map = zk.Widget.$('#' + mapId);
		if (!(panel = this._panel))
			panel = this._panel = jq('#' + panelId)[0];
		// prevent multiple init
		if (directionsDisplay = this._directionsDisplay)
			return;

		// while map and panel are ready
		if (map && map._gmaps && panel) {
			this._directionsService = new google.maps.DirectionsService();
			this._directionsDisplay = directionsDisplay = new google.maps.DirectionsRenderer();

			if ((map = this._map)
				&& (map = map._gmaps))
				directionsDisplay.setMap(map);
			if (panel = this._panel)
				directionsDisplay.setPanel(panel);

			if (map
				&& panel
				&& (direction = this._direction))
				this.setDirection(direction, {force: true});
		} else if (mapId
				&& panelId) {
			// retry if the info for routing is ready
			var wgt = this,
				timer;
			if (timer = this._initTimer) clearTimeout(timer);
			this._initTimer = setTimeout (function () {wgt._init()}, 100);
		}
	},
	getZclass: function () {
		return 'z-gdirection';
	}
});