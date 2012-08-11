//error message widget class
//this file will be loaded because
//the widget-class --  'custom.zk.components.Errmsg'
//specified in lang-addon.xml and zk.wpd
custom.zk.components.Errmsg = zk.$extends(zul.Widget, {
//	block for define attributes
//	where 'anAttr: null' in this block will
//	generate 3 items in this widget
//	1. this._anAttr
//	2. this.setAnAttr(value)
//	3. this.getAnAttr(value)
	
//	'anAttr: function (v)' will also generate the 3 items above,
//	the function will be called by setter,
//	i.e., setAnAttr(value) {
//		...
//		function(v)
//		...
//	}
//	so you can specify what you want to do while the setter is called

//	for more information, please refer to
//	http://www.zkoss.org/javadoc/latest/jsdoc/_global_/zk.html#$extends(zk.Class, _global_.Map, _global_.Map)
//	http://www.zkoss.org/javadoc/latest/jsdoc/_global_/zk.html#define(zk.Class, _global_.Map)
	$define: {
		msg: function (v) {
			var n;
			if (v
				&& (n = this.$n()))
				n.innerHTML = v;
		},
		description: null
	},
	// function to provide css class
	getZclass: function () {
		var zcls = this._zclass;
		return zcls? zcls : 'z-errmsg';
	}
});