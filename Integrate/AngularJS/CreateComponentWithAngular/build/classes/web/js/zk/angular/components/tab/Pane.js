zk.angular.components.tab.Pane = zk.$extends(zk.angular.components.NgComp, {
	$define: {
		title: function (v) {
			// apply change
			this.ngApply('title', v);
		}
	},
	// output title attribute
	_ngTagAttr: function (out) {
		return 'title="'+this._title+'"';
	},

	registerTemplate: function () {
		var ngTemplateUrl = this._ngTemplateUrl,
			ngTag = this._ngTag,
			parentNgTag = this.parent._ngTag;
		if (!zk.ngcomponents.isRegistered(ngTag)) {
			zk.ngcomponents.directive(ngTag, function ($compile) {
				return {
					require: '^'+parentNgTag,
					restrict: 'E',
					transclude: true,
					scope: { id: '@' },
					link: function(scope, element, attrs, tabsCtrl) {
						tabsCtrl.addPane(scope);
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
	},
	copyDataToAngular: function (scope) {
		scope.title = this._title;
	}
});