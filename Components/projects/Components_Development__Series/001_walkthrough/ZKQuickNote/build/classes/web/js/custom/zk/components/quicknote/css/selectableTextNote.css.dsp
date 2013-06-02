<%--// ------------------------------------------- --%>
<%--//                                             --%>
<%--//            SelectableTextNote component           --%>
<%--//                                             --%>
<%--// ------------------------------------------- --%>
<%--// root element --%>
.z-simpletextnote {
	<%--// be the anchor of absolute positioned children --%>
	position: relative;
	overflow: hidden;
}
<%--// the mask that cover whole element --%>
<%--// no background-color and opacity specified --%>
<%--// since we specified them in widget class --%>
.z-selectabletextnote-cover {
	<%--// absoluted positioned --%>
	position: absolute;
	<%--// align the left-top corner of parent (root) element --%>
	left: 0px;
	top: 0px;
	<%--// cover whole root element --%>
	height: 100%;
	width: 100%;
	<%--// make it the top most element under root element --%>
	z-index: 99999;
}

.z-selectabletextnote .z-selectabletextnote-noteblock {
	<%--// absoluted positioned --%>
	position: absolute;
	<%--// in front of mask --%>
	z-index: 999999;
}

.z-selectabletextnote .z-selectabletextnote-noteblock-textarea {
	<%--// h/v resizable --%>
	resize: both;
	<%--// default width and height --%>
	<%--// NOTE: the specified value of width/height will  --%>
	<%--// be the minimum value, you cannot shrink textarea --%>
	<%--// smaller than these values (at least on chrome) --%>
	width: 50px;
	height: 30px;
}
.z-selectabletextnote .z-selectabletextnote-noteblock-selected {
	<%--// in front other text note blocks --%>
	z-index: 1000000;
}