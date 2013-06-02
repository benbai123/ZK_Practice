/**
 * Widget class of SimpleTextNote component,
 * extends custom.zk.components.quicknote.EnhancedMask, change zclass and
 * handle client side click event
 * 
 * Several new things:
 * 
 * extends another widget class:
 * 			this is not that new, we have extended from
 * 			zk.Widget in mask and enhancedmask components.
 * 			but in this widget class we can see that we do not define the
 * 			getZclass, setOpacity and setMaskColor methods again, we can
 * 			use them directly since they are defined in EnhancedMask.js
 * 			(The super widget class of this widget)
 * handling client side event (doClick_):
 * 			the doClick_ is a predefined API
 * 			that will be called automatically with an onclick event in ZK
 * 			There are lots of such API in ZK, e.g., doClick_, doDoubleClick_,
 * 			doRightClick_, doMouseOver_, doMouseOut_, etc, etc
 * 
 * 			currently they are defined in widget.js under 'zk' project,
 * 			you can override it if you want to process a click event of this widget
 * 
 * evt:
 * 			the ZK wrapped event that will be passed into event-handling API automatically
 * 			two major content:
 * 				evt.target: the widget that received this event
 * 				evt.domTarget: the dom element that received this event
 * 
 * override widget function:
 * 			you can override a widget function that has been defined in
 * 			ancestor widget class by define it again in widget class
 * 			e.g., the doClick_ is defined in zk.Widget,
 * 				and we override it by define it again in this widget class
 * 
 * call ancestor's widget function:
 * 			use this.$supers('functionName', arguments)
 * 			to call the mathod defined in ancestor's widget class
 * 			where functionName is the method to call, arguments is the parameters
 * 			to pass
 * 
 */
custom.zk.components.quicknote.SimpleTextNote = zk.$extends(custom.zk.components.quicknote.EnhancedMask, {
	doClick_: function (evt) {
		// call doClick_ method in super widget class
		// (i.e., bubble up)
		// the click event will not be fired to server side
		// if this line is not added (or you need to fire it manually)
		this.$supers('doClick_', arguments);
		// add note block if click on mask element
		if (evt.domTarget == this.$n('mask'))
			this._addNoteBlock(evt);
	},
	_addNoteBlock: function (evt) {
		var n = this.$n(),
			ofs = jq(n).offset(), // offset
			x = evt.pageX - ofs.left, // left for note block
			y = evt.pageY - ofs.top, // top for note block
			noteBlock = this._createNoteBlock(x, y); // note block

		// add note block under root element of widget
		n.appendChild(noteBlock);
		// focus textarea
		jq(noteBlock.firstChild).focus();
	},
	_createNoteBlock: function (x, y) {
		var div = document.createElement('div'), // note block div
			textarea = document.createElement('textarea'), // note block textarea
			tstyle = textarea.style,
			zcls = this.getZclass();
		// add styles,
		// also add a class name to apply the style defined
		// with selector '.z-simpletextnote .z-simpletextnote-noteblock'
		// in simpleTextNote.css.dsp
		jq(div).css({'left': x+'px', // position left and top
					'top': y+'px'})
			.addClass(zcls + '-noteblock');

		// set cols of textarea to 1 so
		// its width can be shrinked
		// also add a class name to apply the style defined
		// with selector '.z-simpletextnote .z-simpletextnote-noteblock-textarea'
		// in simpleTextNote.css.dsp
		jq(textarea).prop('cols', '1')
			.addClass(zcls + '-noteblock-textarea');

		// append textarea to div
		div.appendChild(textarea);
		return div;
	},
	// override with new css class name
	getZclass: function () {
		var zcls = this._zclass;
		return zcls? zcls : 'z-simpletextnote';
	}
});