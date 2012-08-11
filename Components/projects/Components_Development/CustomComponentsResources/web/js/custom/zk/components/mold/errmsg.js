//error message html renderer
//this file will be loaded because
//the package of widget-class --  'custom.zk.components'
//and the mold-uri of mold -- 'mold/errmsg.js'
//specified in lang-addon.xml
function (out) {
	// the rendered msg
	var msg = this._msg;

	// this.uuid is the default attribute that
	// will assigned by ZK framework
	
	// this.getZclass() is overridden in Errmsg.js
	
	// after this line, the tmp result is
	// <span id="xxxxx" class="z-errmsn">
	out.push('<span id="', this.uuid,
			'" class="', this.getZclass(), '">');

	// output msg if exists
	if (msg)
		out.push(msg);
	// output end tag of span
	out.push('</span>');

	// finally, the result will be
	// <span id="xxxxx" class="z-errmsn">some message</span>
	// or
	// <span id="xxxxx" class="z-errmsn"></span>
}