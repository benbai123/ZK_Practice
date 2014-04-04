/** Utils for AngularJS-ZK Component
 * declare component module, register directive,
 * bootstrap AngularJS,
 * bind AngularJS object with ZK widget,
 * fire event by ZK au engine
 * 
 */

// used to handle template and components
zk.ngcomponents = {
	// declare AngularJS module
	components: angular.module('components', []),
	// store registered directive
	registered: {},
	zkcomps: {}, // temporary store zk components
	// whether a directive name is already registered
	isRegistered: function (name) {
		return this.registered[name];
	},
	// to register a directive
	directive: function (name, func) {
		if (!this.isRegistered(name)) {
			this.components.directive(name, func);
			this.registered[name] = true;
		}
	},
	// bootstrap a dom element of component manually
	bootstrap: function (comp) {
		var $n = jq('#'+comp.uuid);

		if (!$n.parents('.ngRoot')[0]) { // no ng parents
			$n.addClass('ngRoot'); // mark self as ng parent then bootstrap
			angular.bootstrap($n[0], ['components']);
		}
	},
	// called by link function of directive
	// do before bind_ function of ZK component
	bindng_: function (scope, element, attrs) {
		// get saved widget instance
		var wgt = scope.wgt = this.removeComponent(scope.id);
		wgt.ngElement = element;
		// store AngularJS scope to ZK widget
		wgt.ngScope = element.ngScope;
		// store AngularJS compile to ZK widget
		wgt.ngCompile = element.ngCompile;
		// store AngularJS controller to ZK widget
		wgt.ngCtrl = element.ngCtrl;
		// copy widget's content into scope
		wgt.copyDataToAngular(scope);
		// marked as ready to do real bind task
		wgt.angReady = true;
		// call widget bind of ZK
		wgt.bind();
		// just need to wait angularjs finish its task
		// resize after children linked
		wgt.ngFireSized();
	},
	/** to override, register AngularJS directive
	 * 
	 */
	registerTemplate: function () {
		
	},
	/** store AngularJS Objects to element for ZK widget
	 * to access in bindng_
	 * 
	 * Must be called in controller of directive
	 * 
	 * @param element
	 * @param scope
	 * @param compile
	 * @param ctrl
	 */
	storeNgObjects: function (element, scope, compile, ctrl) {
		element.ngScope = scope;
		element.ngCompile = compile;
		element.ngCtrl = ctrl;
	},
	/** update field of ZK Widget
	 * 
	 * @param id widget id
	 * @param field field name
	 * @param value value to update
	 */
	updateWidgetValue: function (id, field, value) {
		var wgt = zk.Widget.$('#'+id);
		if (wgt) {
			wgt['_'+field] = value;
		}
	},
	/** fire event to server side
	 * id: component id
	 * eventName: event name to fire
	 * eventData: data to pass with event
	 */
	fireEventToZK: function (id, eventName, eventData) {
		// find client widget by id
		var wgt = zk.Widget.$('#'+id);
		if (wgt) {// if widget exists
			// fire event with data
			wgt.fire(eventName, eventData);
		}
	},
	// store ZK component to zkcomps
	addComponent: function (comp) {
		this.zkcomps[comp.uuid] = comp;
	},
	// return ZK component from zkcomps
	getComponent: function (id) {
		return this.zkcomps[id];
	},
	// remove and return ZK component from zkcomps
	removeComponent: function (id) {
		var comp = this.zkcomps[id];
		delete this.zkcomps[id];
		return comp;
	}
};