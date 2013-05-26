/**
 * Widget class of RenderableTextNote component,
 * extends custom.zk.components.quicknote.SimpleTextNote, change zclass and
 * handle client side click event
 * 
 * one new thing:
 * bind_: Callback when this widget is bound (aka., attached) to the DOM tree.
 * 
 * 
 */
custom.zk.components.quicknote.RenderableTextNote = zk.$extends(custom.zk.components.quicknote.SimpleTextNote, {
	_noteBlocks: null,
	setNoteBlocks: function (noteBlocks) {
		this._noteBlocks = noteBlocks;
		if (this.$n()) {
			this._renderNoteBlocks();
		}
	},
	bind_: function (desktop, skipper, after) {
		this.$supers(custom.zk.components.quicknote.RenderableTextNote, 'bind_', arguments);
		var noteBlocks = this._noteBlocks;
		this._renderNoteBlocks();
	},
	_renderNoteBlocks: function () {
		var noteBlocks = this._noteBlocks;

		jq(this.$n()).find('.' + this.getZclass() + '-noteblock')
			.each(function () {
				// 'this' is each block here
				this.parentNode.removeChild(this);
			});

		if (noteBlocks) {
			var datas = jq.evalJSON(noteBlocks),
				x = datas[0],
				y = datas[1],
				w = datas[2],
				h = datas[3],
				text = datas[4],
				len = x.length,
				idx = 0;
		
			for ( ; idx < len; idx++) {
				this._renderNoteBlock(x[idx], y[idx], w[idx], h[idx], text[idx]);
			}
		}
	},
	_renderNoteBlock: function (x, y, w, h, txt) {
		// note block created by _createNoteBlock defined in SimpleTextNote
		var noteBlock = this._createNoteBlock(x, y),
			textArea = noteBlock.firstChild;
		jq(textArea).css({'width': w+'px',
						'height': h+'px'});
		textArea.innerHTML = txt;
		// add note block under root element of widget
		this.$n().appendChild(noteBlock);
	}
});