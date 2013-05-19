/**
 * Widget class of mask component,
 * do nothing, just provide the zclass
 */
custom.zk.components.quicknote.Mask = zk.$extends(zul.Widget, {
	getZclass: function () {
		var zcls = this._zclass;
		return zcls? zcls : 'z-mask';
	}
});