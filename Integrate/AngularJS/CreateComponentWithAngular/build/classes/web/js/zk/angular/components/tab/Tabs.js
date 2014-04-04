zk.angular.components.tab.Tabs = zk.$extends(zk.angular.components.NgComp, {
	$define: {
		/** index of selected pane */
		selectedIndex: function (v) {
			if (this.ngCtrl) {
				// set to AngularJS controller
				this.ngCtrl.setSelectedIndex(v);
				// apply change
				this.ngApply();
			}
		}
	},
	onNgChildRemoved_: function (child) {
		// remove from parent scope
		this.removePane(child.uuid);
		// apply change
		this.ngApply();
	},
	/** Remove pane from AngularJS scope by
	 * AngularJS controller
	 * 
	 * @param id pane id
	 */
	removePane: function (id) {
		this.ngCtrl.removePane(id);
	},
	copyDataToAngular: function (scope) {
		scope.selectedIndex = this._selectedIndex;
	},

	/** register AngularJS directive
	 * 
	 */
	registerTemplate: function () {
		var ngTemplateUrl = this._ngTemplateUrl,
			ngTag = this._ngTag;
		if (!zk.ngcomponents.isRegistered(ngTag)) { // register once only
			zk.ngcomponents.directive(ngTag, function ($compile) {
				return {
					restrict: 'E',
					transclude: true,
					scope: { id: '@'},
					link: function(scope, element, attrs, ctrl) {
						// bind with ZK widget
						zk.ngcomponents.bindng_(scope, element, attrs);
						var idx = scope.selectedIndex;
						// update selected index to AngularJS
						if (idx)
							ctrl.setSelectedIndex(idx);
					},
					controller: function($scope, $element) {
						var panes = $scope.panes = [];
						// select method
						// defined in scope so can
						// call it with ng-click
						$scope.select = function(pane, opts) {
							var idx = 0;
							// for each pane
							angular.forEach(panes, function(p) {
								if (pane == p) { // found pane to select
									pane.selected = true; // select it
									// fire select event and
									// update widget value
									if (!opts || !opts.ignoreOnSelect) {
										if ($scope.selectedIndex != idx) {
											var id = $scope.id;
											zk.ngcomponents.fireEventToZK(id, "onSelect", {index: idx});
											$scope.selectedIndex = idx;
											zk.ngcomponents.updateWidgetValue(id, 'selectedIndex', idx);
										}
									}
								} else
									p.selected = false; // deselect it
								idx++; // increase index
							});
						};
						// set selected index
						this.setSelectedIndex = function (idx) {
							var cnt = 0;
							$scope.selectedIndex = idx;
							// for each pane
							angular.forEach(panes, function(p) {
								if (cnt == idx) {// found
									p.selected = true; // select it
								} else // deselect it otherwise
									p.selected = false;
								cnt++;
							});
						};
						// add pane
						this.addPane = function(pane) {
							// push to panes array
							panes.push(pane);
							// select if no others
							if (panes.length == 1)
								$scope.select(pane, {ignoreOnSelect: true});
							else {
								var idx = $scope.selectedIndex;
								if (idx && idx == panes.length-1) {
									this.setSelectedIndex(idx);
								}
							}
						};
						// remove pane from panes array
						this.removePane = function (id) {
							angular.forEach(panes, function(pane) {
								// found
								if (pane.id == id) {
									// remove it
									var idx = panes.indexOf(pane);
									panes.splice(idx, 1);
								}
							});
						}
						// store AngularJS objects for bindng_
						zk.ngcomponents.storeNgObjects($element, $scope, $compile, this);
					},
					// assigned template url
					templateUrl: ngTemplateUrl,
					replace: true
				};
			});
		}
	}
});