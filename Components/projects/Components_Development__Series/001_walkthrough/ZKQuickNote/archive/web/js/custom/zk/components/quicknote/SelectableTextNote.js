/**
 * Widget class of SelectableTextNote component,
 * extends custom.zk.components.quicknote.RenderableTextNote
 * 
 * One new thing:
 * fire: fire event to server,
 * 		widget.fire('EVENT_NAME', {DATA}, {OPTIONS}, TIMEOUT);
 * 		refer to http://www.zkoss.org/javadoc/latest/jsdoc/zk/Widget.html#fire(_global_.String, zk.Object, _global_.Map, int)
 * 
 */
custom.zk.components.quicknote.SelectableTextNote = zk.$extends(custom.zk.components.quicknote.RenderableTextNote, {
	// called while onclick of any dom elements
	// under root element is triggered
	doClick_: function (evt) {
		// call super at first
		this.$supers('doClick_', arguments);
		// cls: css class of textarea within text note block
		// target: the clicked dom element
		var cls = this.getZclass() + '-noteblock-textarea',
			scls = this.getZclass() + '-noteblock-selected',
			target = evt.domTarget;

		// clicked in textarea in text note block
		if (jq(target).hasClass(cls)) {
			// firstTextNote: the first text note block
			// current: a copy of text note block for while loop
			// idx: index of current text note block
			var firstTextNote = this.$n('mask').nextSibling,
				current = firstTextNote,
				idx = 0;
			// clear style and class of old selected block
			jq('.' + scls).each(function () {
				jq(this).css('z-index', '999999').removeClass(scls);
			});
			// for each text note block
			while (current) {
				// found clicked block
				if (jq(current).find('.'+cls)[0] == target) {
					jq(current).css('z-index', '1000000') // make clicked block top most
						.addClass(scls); // add a specific so can find it easily next time
					// fire event to update index to server side
					this.fire('onTextNoteBlockSelect', {index: idx});
					break;
				}
				current = current.nextSibling;
				idx++;
			}
		}
	}
});