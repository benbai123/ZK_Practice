zk.afterLoad("zul.db", function () {
	// Datebox Calendar Renderer
	var _Cwgt = {};
	zk.override(zul.db.CalendarPop.prototype, _Cwgt, {
		// switch the view after redraw or open as needed
		redraw: function (out) {
			_Cwgt.redraw.apply(this, arguments); //call the original method
			this._customChangeView ();
		},
		open: function (silent) {
			_Cwgt.open.apply(this, arguments); //call the original method
			this._customChangeView ();
		},
		_customChangeView: function () {
			// cannot show month/day
			if (this._isYearboxCalPop()) {
				var view = this._view;
				// switch to year view as needed
				if (view == 'month' || view == 'day')
					this._setView("year");
			} else if (this._isMonthboxCalPop()) {
				// cannot show day view
				// switch to month view as needed
				if (this._view == 'day')
					this._setView("month");
			}
		},
		// customize the chooseDate function
		_chooseDate: function (target, val) {
			var view = this._view;
			if (this._isYearboxCalPop()
				|| this._isMonthboxCalPop()) {
				// do customize chooseDate if the parent (datebox)
				// has specific class
				var date = this.getTime(),
					year = (view == 'decade' || view == 'year')? val : date.getFullYear(),
					month = view == 'month'? val : 0;
				// set date value
				this._setTime(year, month, 1);
				if (view == 'decade') {
					// switch to year view if at decade view
					this._setView("year");
				} else if (this._isMonthboxCalPop()
					&& view == 'year') {
					// switch to month view if at year view and the month view is allowed
					this._setView("month");
				} else if (this._isMonthboxCalPop() && view == 'month'
					|| this._isYearboxCalPop() && view == 'year') {
					// close calendar and update value if already at the smallest allowed view
					this.close();
					this.parent.updateChange_();
				}
			} else {
				_Cwgt._chooseDate.apply(this, arguments); //call the original method
			}
		},
		_isYearboxCalPop: function () {
			return this.parent.$instanceof(custom.zk.components.Yearbox);
		},
		_isMonthboxCalPop: function () {
			return this.parent.$instanceof(custom.zk.components.Monthbox);
		}
	});
});