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
	/** setter for noteBLocks,
	 * will render note blocks if dom element exists
	 * 
	 * @param noteBlocks information of note blocks from server
	 */
	setNoteBlocks: function (noteBlocks) {
		this._noteBlocks = noteBlocks;
		if (this.$n()) {
			// dom exists, render note blocks
			this._renderNoteBlocks();
		}
	},
	bind_: function (desktop, skipper, after) {
		this.$supers(custom.zk.components.quicknote.RenderableTextNote, 'bind_', arguments);
		var noteBlocks = this._noteBlocks;
		this._renderNoteBlocks();
	},
	// render note blocks
	_renderNoteBlocks: function () {
		var noteBlocks = this._noteBlocks;

		// clear all old note blocks
		// so we do not need to care about the mapping (keep, rerender, add, etc)
		jq(this.$n()).find('.' + this.getZclass() + '-noteblock')
			.each(function () {
				// 'this' is each block here
				this.parentNode.removeChild(this);
			});

		// has note blocks
		if (noteBlocks) {
			var datas = jq.evalJSON(noteBlocks), // eval to get a 2-D array
				x = datas[0], // left array
				y = datas[1], // top array
				w = datas[2], // width array
				h = datas[3], // height array
				text = datas[4], // text array
				len = x.length, // amount of note blocks
				idx = 0; // index

			// render each note block
			for ( ; idx < len; idx++) {
				this._renderNoteBlock(x[idx], y[idx], w[idx], h[idx], text[idx]);
			}
		}
	},
	// create dom element of note block
	_renderNoteBlock: function (x, y, w, h, txt) {
		// note block created by _createNoteBlock defined in SimpleTextNote
		var noteBlock = this._createNoteBlock(x, y),
			textArea = noteBlock.firstChild;
		// add width and height, insert text
		jq(textArea).css({'width': w+'px',
						'height': h+'px'});
		textArea.innerHTML = txt;
		// add note block under root element of widget
		this.$n().appendChild(noteBlock);
	}
});