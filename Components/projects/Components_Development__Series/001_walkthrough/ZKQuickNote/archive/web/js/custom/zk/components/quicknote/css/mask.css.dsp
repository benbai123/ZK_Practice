<%--// ------------------------------------------- --%>
<%--//                                             --%>
<%--//                Mask component               --%>
<%--//                                             --%>
<%--// ------------------------------------------- --%>
<%--// root element --%>
.z-mask {
	<%--// be the anchor of absolute positioned children --%>
	position: relative;
	overflow: hidden;
}
<%--// the mask that cover whole element --%>
.z-mask-cover {
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
	<%--// make this mask a little bit visible --%>
	background-color: #ccc;
	opacity: 0.35;
}