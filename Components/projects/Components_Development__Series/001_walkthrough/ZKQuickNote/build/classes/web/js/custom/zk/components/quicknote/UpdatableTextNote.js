/** UpdatableTextNote
 * 
 * Extends RecordableTextNote and add functions to update, add or remove
 * a specific text note block
 * 
 * No new thing
 * 
 */
custom.zk.components.quicknote.UpdatableTextNote = zk.$extends(custom.zk.components.quicknote.RecordableTextNote, {
	// update a specific text note block
	setTextNoteBlockToUpdate: function (blockInfo) {
		this._updateNoteBlock(jq.evalJSON(blockInfo));
	},
	// add a specific text note block
	setTextNoteBlockToAdd: function (blockInfo) {
		var data = jq.evalJSON(blockInfo),
			index = data.index,
			blocks = jq(this.$n()).find('.' + this.getZclass() + '-noteblock'),
			len = blocks.length; // keep length before the new one is added
		// add to the tail at first
		this._renderNoteBlock(data.left, data.top, data.width, data.height, data.text);

		// insert case,
		// the specified index is smaller than the length
		// of text note blocks
		if (index < len) { // insert
			// newDom: last block
			// ref: the block at the specified index
			var newDom = this._getTextNoteBlockByIndex(len),
				ref = this._getTextNoteBlockByIndex(index);
			// insert newDom before ref
			ref.parentNode.insertBefore(newDom, ref);
		}
	},
	// remove a specific text note block
	setTextNoteBlockToRemove: function (blockInfo) {
		// find block by specified index
		var block = this._getTextNoteBlockByIndex(jq.evalJSON(blockInfo).index);
		if (block) {
			// remove it
			block.parentNode.removeChild(block);
		}
	},
	// update a specific note block
	_updateNoteBlock: function (data) {
		// get attributes from data
		// find the block by specified index
		var index = data.index,
			block = this._getTextNoteBlockByIndex(index),
			textarea = block.firstChild,
			bstyle = block.style,
			tstyle = textarea.style,
			zattr = textarea.zkAttributes;

		// update block,
		// also update the stored attributes of block
		zattr.left = bstyle.left = data.left + 'px';
		zattr.top = bstyle.top = data.top + 'px';
		zattr.width = tstyle.width = data.width + 'px';
		zattr.height = tstyle.height = data.height + 'px';
		zattr.text = data.text;
		jq(textarea).val(data.text);
	},
	// find text note block by the given index
	_getTextNoteBlockByIndex: function (index) {
		var current = this.$n('mask').nextSibling,
			idx = 0;
	
		// for each text note block
		while (current) {
			if (idx == index) {
				return current;
			}
			current = current.nextSibling;
			idx++;
		}
		return null;
	}
});