/**
 * Widget class of EnhancedMask component,
 * extends zul.Widget, change zclass and add setters for
 * attributes opacity and maskColor
 * 
 * Two new things:
 * setters: the setSomething methods are mapping to 'something' at
 * 			server side when render or smartUpdate is called.
 * 			e.g., setOpacity (in widget class) is called if executes
 * 				smartUpdate("opacity", _opacity);
 * 				or
 * 				render(renderer, "opacity", _opacity);
 * 				at server side
 * 
 * $n():	This is the API to get dom elements by specified id.
 * 			ZK will assign each component an ID (say uuid) (is unique within an ID Space),
 * 			we usually assign the uuid to root dom element,
 * 			and assign uuid+'-'+suffix to child elements
 * 
 * 			In this case, this api will return the root element when you call this.$n(),
 * 			and return the child element with specific suffix when you call this.$n(suffix)
 * 
 *  		e.g., We specified uuid+'-mask' as the ID of child mask element in enhancedMask.js,
 *  			so we can get the mask dom element by this.$n('mask');
 *  
 *  
 *  NOTE: It is important to specify the uuid to root dom element of a widget,
 *  		or ZK can not find it while detaching it,
 *  		i.e., will leave the element in dom tree after the component is detached,
 *  		and causes weird bug or memory leak in browser.
 *  
 *  		In most cases, you can call this.domAttrs_() directly when you output the html
 *  		of root dom element then it should work fine.
 * 
 */
custom.zk.components.quicknote.EnhancedMask = zk.$extends(zul.Widget, {
	/**
	 * default values: set default value in both java class and
	 * widget class, so do not need transfer them via network if
	 * we just want to use the default values
	 */
	_opacity: 35,
	_maskColor: '#ccc',
	setOpacity: function (opacity) {
		// update if value is changed
		if (this._opacity != opacity) {
			// update value
			this._opacity = opacity;
			// try to get mask element
			var mask = this.$n('mask');
			if (mask) {
				// apply opacity to mask if it is available
				jq(mask).css('opacity', (opacity/100));
			}
		}
	},
	setMaskColor: function (maskColor) {
		// update if value is changed
		if (this._maskColor != maskColor) {
			// update value
			this._maskColor = maskColor;
			// try to get mask element
			var mask = this.$n('mask');
			if (mask) {
				// apply background-color to mask if it is available
				jq(mask).css('background-color', maskColor);
			}
		}
	},
	// override with new css class name
	getZclass: function () {
		var zcls = this._zclass;
		return zcls? zcls : 'z-enhancedmask';
	}
});