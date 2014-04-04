zk.angular.components.wgt.SimpleButton = zk.$extends(zk.angular.components.NgComp, {
	$define: {
		// button attributes
		title: null,
		label: null,
		disabled: function (v) {
			if (this.$n())
				this.$n().disabled = v;
		}
	},
	getZclass: function () {
		return 'z-button';
	},
	bind_: function (desktop, skipper, after) {
		this.$supers(zk.angular.components.wgt.SimpleButton, 'bind_', arguments);
	},
	_ngTagAttr: function (out) {
		return this._disabled? 'disabled="disabled"' : '';
	},
	copyDataToAngular: function (scope) {
		scope.style = this.domStyle_();
		scope.zclass = this.getZclass();
		scope.sclass = this.getSclass();
		scope.title = zUtl.encodeXML(this.domTooltiptext_());
		scope.tabindex = this._tabindex;
		scope.label = this._label;
	},
	registerTemplate: function () {
		var ngTemplateUrl = this._ngTemplateUrl,
			ngTag = this._ngTag;
		if (!zk.ngcomponents.isRegistered(ngTag)) {
			zk.ngcomponents.directive(ngTag, function ($compile) {
				return {
					restrict: 'E',
					transclude: true,
					scope: { id: '@'},
					link: function(scope, element, attrs, ctrl) {
						zk.ngcomponents.bindng_(scope, element, attrs);
					},
					controller: function($scope, $element) {
						// store AngularJS objects for bindng_
						zk.ngcomponents.storeNgObjects($element, $scope, $compile, this);
					},
					templateUrl: ngTemplateUrl,
					replace: true
				};
			});
		}
	}
});