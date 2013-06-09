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
	_selectedTextNoteIndex: -1,
	// called while onclick of any dom elements
	// under root element is triggered
	doClick_: function (evt) {
		// call super at first
		this.$supers('doClick_', arguments);
		var target = evt.domTarget;
		// clicked in textarea in text note block
		if (jq(target).hasClass(this.getZclass() + '-noteblock-textarea')) {
			this._doTextNoteBlockClick(evt);
		}
	},
	/** processing onclick of textarea in note block
	 * pass event into this function (instead of just pass target)
	 * since we probably need some information (e.g., pageX/Y, etc) in
	 * the future
	 * 
	 * @param evt
	 */
	_doTextNoteBlockClick: function (evt) {
		// cls: css class of textarea within text note block
		// target: the clicked dom element
		var scls = this.getZclass() + '-noteblock-selected',
			target = evt.domTarget,
			idx;

		// clear selected class of old selected block
		jq('.' + scls).each(function () {
			jq(this).removeClass(scls);
		});
		// add class to make it become top most note block
		jq(target.parentNode).addClass(scls);
		// fire event to update index to server side
		if ((idx = this.getTextBlockIndex(target)) >= 0) {
			this._selectedTextNoteIndex = idx;
			this.fire('onTextNoteBlockSelect', {index: idx});
		}
	},
	getTextBlockIndex: function (textarea) {
		var cls = this.getZclass() + '-noteblock-textarea';
		// current: a copy of text note block for while loop
		// idx: index of current text note block
		var current = this.$n('mask').nextSibling,
			idx = 0;

		// for each text note block
		while (current) {
			// found clicked block
			if (jq(current).find('.'+cls)[0] == textarea) {
				// return index
				return idx;
			}
			current = current.nextSibling;
			idx++;
		}
		return -1;
	},
	// override with new css class name
	getZclass: function () {
		var zcls = this._zclass;
		return zcls? zcls : 'z-selectabletextnote';
	}
});