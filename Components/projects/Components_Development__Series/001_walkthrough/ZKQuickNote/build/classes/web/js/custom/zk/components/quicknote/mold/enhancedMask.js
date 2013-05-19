/**
 * redraw for enhancedmask component,
 * wrap children with a div then cover them,
 * also generate style based on attributes
 */
function (out) {
	var uuid = this.uuid,
		zcls = this.getZclass(),
		style = ' style="background-color: ' + this._maskColor
						+'; opacity:' + (this._opacity/100) + ';"';
	// output root dom element of this widget
	out.push('<div', this.domAttrs_(), '>');
	// output children
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	// output mask that cover children
	// with generated style
	out.push('<div id="', uuid, '-mask"', style, ' class="', zcls,'-cover"></div>');
	out.push('</div>');
}