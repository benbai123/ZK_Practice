//
// Tested with ZK 6.0.2
//
(function () {
	function customizeCalendarPop (pop) {
		// store original function
		var oldRedraw = pop.redraw,
			oldOpen = pop.open;
		pop.redraw = function (out) {
			// call original function
			oldRedraw.apply(pop, arguments);
			// update view
			pop._customChangeView ();
		};
		pop.open = function (silent) {
			// call original function
			oldOpen.apply(pop, arguments);
			// update view
			pop._customChangeView ();
		};
		pop._customChangeView = function () {
			var view = pop._view;
			// switch to year view as needed
			if (view == 'month' || view == 'day')
				pop._setView("year");
		};
		// do customize chooseDate
		pop._chooseDate = function (target, val) {
			var view = pop._view;
			// set date value
			pop._setTime(val, null, 1);
			if (view == 'decade') {
				// switch to year view if at decade view
				pop._setView("year");
			} else {
					pop.close();
					pop.parent.updateChange_();
			}
		};
	}
	custom.zk.components.Yearbox = zk.$extends(zul.db.Datebox, {
		bind_: function () {
			// call original function
			this.$supers('bind_', arguments);
			// customize calendar pop
			customizeCalendarPop(this._pop);
		}
	});
})();