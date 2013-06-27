test.components.Decimalbox = zk.$extends(zul.inp.Decimalbox, {
	_textAlign: 'left',
	setTextAlign: function (v) {
		if (v != this._textAlign) {
			this._textAlign = v;
			var inp = this.getInputNode();
			if (inp)
				jq(inp).css('text-align', this._textAlign);
		}
	},
	bind_: function () {
		this.$supers('bind_', arguments);
		jq(this.getInputNode()).css('text-align', this._textAlign);
	},
	getAllowedKeys_: function () {
		// add 'E' into allowed keys
		return this.$supers('getAllowedKeys_', arguments) + 'E';
	},
	_markError: function (msg, val, noOnError, doDirectly) {
		if (doDirectly) {
			// do it without server confirmation
			this.$supers('_markError', arguments);
		} else {
			// store info only, waiting for server confirmation
			this._markErrorInfo = {msg: msg, val: val, noOnError: noOnError};
			// ask server side custom validation
			var wgt = this,
				timer = this._onCheckErrorValueTimer;
			// prevent duplicated event
			if (timer)
				clearTimeout(timer);
			this._onCheckErrorValueTimer = setTimeout (function () {
				wgt.fire('onCheckErrorValue', {value: jq(wgt.getInputNode()).val()});
			}, 300);
		}
	},
	// called by server
	// do _markError directly with the stored information
	_realMarkError: function () {
		var info = this._markErrorInfo;
		if (info) {
			this._markError(info.msg, info.val, info.noOnError, true);
			this._markErrorInfo = null;
		}
	}
});