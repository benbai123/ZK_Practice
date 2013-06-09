/**
 * Widget class of RecordableTextNote component,
 * extends custom.zk.components.quicknote.SelectableTextNote
 * 
 * actually no new thing, just handle more events and do more
 * works to make it recordable
 * 
 */
custom.zk.components.quicknote.RecordableTextNote = zk.$extends(custom.zk.components.quicknote.SelectableTextNote, {
	_createNoteBlock: function (x, y) {
		// call super
		var noteBlock = this.$supers('_createNoteBlock', arguments),
			textarea = noteBlock.firstChild,
			wgt = this;
		// add event listener for onfocus and onblur of textarea
		textarea.onfocus = function () {
			wgt.doTextNoteBlockFocus(textarea);
		};
		textarea.onblur = function () {
			wgt.doTextNoteBlockBlur(textarea);
		};
		// define an object to hold attributes of text note block
		// and store it at textarea
		textarea.zkAttributes = {};
		this.storeTextBlockAttributes(textarea);
		return noteBlock;
	},
	// rewrite _renderNoteBlock to add _afterRenderNoteBlock
	// at the tail
	_renderNoteBlock: function (x, y, w, h, txt) {
		var noteBlock = this._createNoteBlock(x, y),
			textarea = noteBlock.firstChild;
		jq(textarea).css({'width': w+'px',
						'height': h+'px'});
		textarea.innerHTML = txt;
		this.$n().appendChild(noteBlock);
		// add _afterRenderNoteBlock
		this._afterRenderNoteBlock(noteBlock);
	},
	// called after _renderNoteBlock
	_afterRenderNoteBlock: function (noteBlock) {
		var idx = this._selectedTextNoteIndex;
		if (idx >= 0
			&& idx == this.getTextBlockIndex(noteBlock.firstChild))
			jq(noteBlock).addClass(this.getZclass() + '-noteblock-selected');
		// simply update textarea.zkAttributes
		this.storeTextBlockAttributes(noteBlock.firstChild);
	},
	// store/update attributes of text note block
	// to textarea.zkAttributes
	storeTextBlockAttributes: function (textarea) {
		var $textarea = jq(textarea),
			$div = jq(textarea.parentNode),
			zattr = textarea.zkAttributes;
		// store current value
		zattr.left = $div.css('left');
		zattr.top = $div.css('top');
		zattr.width = $textarea.css('width');
		zattr.height = $textarea.css('height');
		zattr.text = $textarea.val();
	},
	// called while onfocus of textarea in text note block
	doTextNoteBlockFocus: function (textarea) {
		// store changed attributes
		// and fire event as needed
		this.recordTextBlock(textarea);
	},
	// called while onblur of textarea in text note block
	doTextNoteBlockBlur: function (textarea) {
		this.recordTextBlock(textarea);
	},
	// update changed attributes and fire event as needed
	recordTextBlock: function (textarea) {
		// update attributes and fire event
		// if attributes are changed
		if (this.isTextNoteBlockChanged(textarea)) {
			this.storeTextBlockAttributes(textarea);
			this.fireOnTextNoteBlockChanged(textarea);
		}
	},
	// check whether attributes of a text note block
	// are changed
	isTextNoteBlockChanged: function (textarea) {
		var $textarea = jq(textarea),
			$div = jq(textarea.parentNode),
			zattr = textarea.zkAttributes;
		// store current value
		return zattr.left != $div.css('left')
			||	zattr.top != $div.css('top')
			|| zattr.width != $textarea.css('width')
			|| zattr.height != $textarea.css('height')
			|| zattr.text != $textarea.val();
	},
	// fire text note block changed event
	fireOnTextNoteBlockChanged: function (textarea) {
		var zattr = textarea.zkAttributes,
			idx = this.getTextBlockIndex(textarea),
			selected = jq('.' + this.getZclass() + '-noteblock-selected')[0];
		// has attributes object and index exists
		if (zattr
			&& idx >= 0) {
			// create data
			var data = {index: idx,
					left: parseInt(zattr.left),
					top: parseInt(zattr.top),
					width: parseInt(zattr.width),
					height: parseInt(zattr.height),
					text: zattr.text
			};
			// always fire onTextNoteBlockChanged
			this.fire('onTextNoteBlockChanged', data);
			// also fire onSelectedTextNoteBlockChanged if
			// changed text note block is selected one
			if (selected 
				&& this.getTextBlockIndex(selected.firstChild) == idx) {
				this.fire('onSelectedTextNoteBlockChanged', data);
			}
		}
	}
});