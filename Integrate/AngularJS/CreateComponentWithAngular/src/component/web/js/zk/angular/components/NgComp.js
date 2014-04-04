/** APIs for AngularJS-ZK Component
 */

zk.angular.components.NgComp = zk.$extends(zul.Widget, {
	$define: {
		/** Tag to output AngularJS directive
		 * 
		 * @param v tag name
		 */
		ngTag: function (v) {
			// rerender and re-apply angularjs if already
			// rendered
			if (this.$n())
				this.rerenderNg(this.$n());
		},
		/** Template url to render AngularJS directive
		 * 
		 * @param v template url
		 */
		ngTemplateUrl: function (v) {
			// rerender and re-apply angularjs if already
			// rendered
			if (this.$n())
				this.rerenderNg(this.$n());
		},
		/** Whether prevent blinking of AngularJS by fake dom element
		 * 
		 * Note: This will affect client side performance
		 */
		preventBlink: null
	},
	// ZK redraw
	redraw: function (out) {
		// pre check
		var parent = this.parent,
			$n = parent? jq(parent.$n()) : null;

		// need to rerender if under a rendered AngularJS fragment 
		if ($n[0]
			&& ($n.hasClass('ngRoot')
				|| $n.parents('.ngRoot')[0]
			)) {
			if (!$n.hasClass('ngRoot')) // find root of AngularJS fragment
				$n = jq($n.parents('.ngRoot')[0]);
			if (!$n.hasClass('ngToRerender')) { // marke ngRoot as need to rerender
				// avoid rerender multiple times
				$n.addClass('ngToRerender');
				// store rerender info to self
				this.ngRerender = true;
			} else {
				// store skip info if ngRoot already marked
				this.ngSkip = true;
			}
			return; // skip redraw, wait for rerender
		}

		// redraw start
		var w = this.firstChild;
		// simply output ngTag, attrs and id
		out.push('<', this._ngTag, ' ', this._ngTagAttr(), ' id="', this.uuid, '">');

		// output children
		for ( ; w; w = w.nextSibling)
			w.redraw(out);
		out.push('</', this._ngTag, '>');
		// store component for bindng_
		zk.ngcomponents.addComponent(this);
		// register template for AngularJS directive
		this.registerTemplate();
	},
	/** to override
	 * output attributes for ngTag
	 */
	_ngTagAttr: function () {
		
	},
	bind_: function (desktop, skipper, after) {
		if (!this.angReady) { // not ready for ZK bind
			if (this.ngSkip) { // will be rerender, skip
				this.ngSkip = null
				return;
			} if (this.ngRerender) { // need to do rerender
				this.ngRerender = null;
				// rerender ngRoot
				this.parent.rerenderNg(this.parent.$n());
			} else { // apply AngularJS
				var $n = jq('#'+this.uuid);
				// mark self as not ready for parent to check
				// so parent will not call flex
				// until mark removed
				$n.addClass('ngNotReady');
				if ($n[0]) { // dom found
					// try to find root of AngularJS fragment
					var ngRoot = $n[0]? $n.parents('.ngRoot')[0] : null;
					if (!ngRoot) { // not found
						// mark self as ngRoot
						$n.addClass('ngRoot');
						this.isNgRoot = true;
						// switch _visible status to avoid hflex/vflex
						this._visibleBkup = this._visible;
						this._visible = false;
						// bootstrap manually
						zk.ngcomponents.bootstrap(this);
					} // else wait ngRoot to apply angular
				}
			}
		} else {
			// call ZK bind
			this.$supers(zk.angular.components.NgComp, 'bind_', arguments);
			// remove 'not ready' mark
			jq(this.$n()).removeClass('ngNotReady');
		}
	},
	setVisible: function (v) {
		this.$supers(zk.angular.components.NgComp, 'setVisible', arguments);
		if (this.$n()) {
			zUtl.fireSized(this.parent);
		}
	},
	detach: function () {
		var ngScope = this.ngScope,
			w = this.firstChild,
			parent = this.parent;
		// detach children first
		while (w) {
			w.detach();
			w = w.nextSibling;
		}
		// destroy scope
		ngScope.$destroy();
		this.parent.onNgChildRemoved_(this);
		// call removeChild with ignoreDom=true
		// dom will be handled by AngularJS
		if (parent)
			parent.removeChild(this, true);
	},
	/** to override
	 * destroy AngularJS child
	 * 
	 * @param child AngularJS child
	 */
	onNgChildRemoved_: function (child) {
		
	},
	/** rerender ngRoot and apply angularjs
	 * 
	 * AngularJS still has problem of dynamically insert
	 * a fragment of complex dom element so simply
	 * rerender ngRoot in this POC
	 * 
	 */ 
	rerenderNg: function (n) {
		// try to find ngRoot
		var $n = jq(n),
			ngRoot = this.isNgRoot? $n[0] : $n.parents('.ngRoot')[0];

		if (ngRoot) {
			// get ZK widgets
			var wgt = zk.Widget.$('#' + ngRoot.id);
			if (wgt && wgt.$n() && wgt.angReady) { // already rendered
				wgt.angReady = null;
				// need to prevent blinking
				if (wgt._preventBlink)
					wgt.createFake(); // create fake
				wgt.rerender();
			}
		}
	},
	/** apply change to AngularJS scope
	 * 
	 * @param field field to update
	 * @param value value to update
	 */
	ngApply: function (field, value) {
		// get scope
		var scope = this.ngScope;
		if (scope) {
			// update field value if needed
			if (field) {
				scope[field] = value;
			}
			// apply change
			scope.$apply();
		}
	},
	/** create fake dom element to prevent blinking of
	 * AngularJS 
	 */
	createFake: function () {
		var n = this.$n(),
			tmp = jq(n).clone()[0],
			parent = n.parentNode;

		if (n == parent.lastChild)
			parent.appendChild(tmp);
		else
			parent.insertBefore(tmp, n.nextSibling);
		jq(n).css({'position': 'absolute',
			'left': '-9999px'
		});
		if (this.fakeToRemove) this.removeFake();
		this.fakeToRemove = tmp;
	},
	/** remove fake dom element
	 * 
	 */
	removeFake: function () {
		var tmp = this.fakeToRemove;
		if (tmp) {
			tmp.parentNode.removeChild(tmp);
			this.fakeToRemove = null;
		}
	},
	/** to override
	 * copy data of ZK Widget to AngularJS scope
	 * 
	 * @param scope AngularJS scope
	 */
	copyDataToAngular: function (scope) {

	},

	/** trigger resize for the parent dom of ngRoot
	 * 
	 */
	ngFireSized: function () {
		var n = this.$n(),
			$n = jq(n);
		if (!n) return;
		// find ngRoot
		if (!$n.hasClass('ngRoot')) {
			n = $n.parents('.ngRoot')[0];
		}
		if (n) {
			$n = jq(n);
			// all children are ready
			if (!$n.find('.ngNotReady')[0]) {
				var wgt = zk.Widget.$('#'+n.id);
				if (wgt) {
					// remove fake dom
					wgt.removeFake();
					// restore visible attribute
					wgt._visible = wgt._visibleBkup;
					// trigger resize
					zUtl.fireSized(wgt.parent);
				}
			}
		}
	}
});