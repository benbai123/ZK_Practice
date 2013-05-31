/**
 * Widget class of RecordableTextNote component,
 * extends custom.zk.components.quicknote.SelectableTextNote
 * 
 * one new thing:
 * bind_: Callback when this widget is bound (aka., attached) to the DOM tree.
 * 
 * 
 */
custom.zk.components.quicknote.RecordableTextNote = zk.$extends(custom.zk.components.quicknote.SelectableTextNote, {
	doClick_: function (evt) {
		this.$supers('doClick_', arguments);
		zk.log(evt.domTarget);
	},
	_createNoteBlock: function (x, y) {
		// call super
		var noteBlock = this.$supers('_createNoteBlock', arguments),
			textArea = noteBlock.firstChild,
			wgt = this;
		textArea.onfocus = function () {
			wgt._storeOldText(textArea);
		};
		// bind event
		textArea.onblur = function () {
			wgt._checkUpdate(textArea);
		};
		textArea.onmousemove = function () {
			wgt._storeOldSize(textArea);
		};
		textArea.onmouseup = function () {
			var textAreas = jq(wgt.$n()).find('textarea'),
				len = textAreas.length,
				idx = 0;
			for ( ; idx < len; idx++ ) {
				if (textAreas[idx] == textArea) {
					zk.log(idx);
					break;
				}
			}
		};
		return noteBlock;
	},
	_storeOldText: function (textArea) {
		textArea._oldText = jq(textArea).val();
		zk.log(textArea._oldText);
	},
	_checkUpdate: function (textArea) {
		var old = textArea._oldText,
			newText = jq(textArea).val();
		zk.log(old);
		zk.log(newText);
	},
	_storeOldSize: function (textArea) {
		var $textArea = jq(textArea);
		textArea._oldWidth = $textArea.width();
		textArea._oldHeight = $textArea.height();

		zk.log(textArea._oldWidth);
		zk.log(textArea._oldHeight);
	}
});