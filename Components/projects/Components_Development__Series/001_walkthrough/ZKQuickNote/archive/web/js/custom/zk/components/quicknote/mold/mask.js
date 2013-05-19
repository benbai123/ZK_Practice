/**
 * redraw for mask component,
 * wrap children with a div then cover them
 */
function (out) {
	var uuid = this.uuid,
		zcls = this.getZclass();
	// output root dom element of this widget
	out.push('<div', this.domAttrs_(), '>');
	// output children
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	// output mask that cover children
	out.push('<div id="', uuid, '-mask" class="', zcls,'-cover"></div>');
	out.push('</div>');
}