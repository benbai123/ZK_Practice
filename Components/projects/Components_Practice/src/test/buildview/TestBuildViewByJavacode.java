package test.buildview;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class TestBuildViewByJavacode extends GenericForwardComposer {
	/** While doAfterCompose, all components in zul page under
	 * the div that apply this composer (including the div itself)
	 * is ready and can be accessed in java code
	 *
	 */
	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		// the comp here is the div which apply this
		// composer in build_view_by_javacode_in_java_file.zul
		System.out.println(comp);

		// below is the equivalent code of
		// <window title="test window" border="normal">
		//     <attribute name="onClick"><![CDATA[
		//         alert("test window is clicked !");
		//     ]]></attribute>
		//     <label value="test label" style="color: green;">
		//         <attribute name="onClick"><![CDATA[
		//             alert("test label is clicked !");
		//         ]]></attribute>
		//     </label>
		// </window>
		Window win = new Window(); // create window component
		win.setTitle("test window"); // set properties
		win.setBorder("normal");
		// add event listener for onClick event of window
		win.addEventListener(Events.ON_CLICK, new EventListener () {
			public void onEvent (Event event) {
				alert("test window is clicked !");
			}
		});
		win.setParent(comp); // append it to parent div

		Label lb = new Label(); // create label component
		lb.setValue("test label"); // set properties
		lb.setStyle("color: green;");
		// add event listener for onClick event of label
		lb.addEventListener(Events.ON_CLICK, new EventListener () {
			public void onEvent (Event event) {
				alert("test label is clicked !");
			}
		});
		lb.setParent(win); // append it to parent window
	}
}