package composer;

import java.util.List;

import javax.servlet.ServletException;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;

import zk.angular.components.Pane;
import zk.angular.components.SimpleButton;
import zk.angular.components.Tabs;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/** Tested with ZK 7.0.1 CE
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TestComposer extends SelectorComposer {
	private static final long serialVersionUID = 4267444195827296296L;
	@Wire
	Tabs tbx;
	@Wire
	Tabs tbx2;
	@Wire
	SimpleButton cbtn;
	// show info
	@Listen("onClick = #btn0")
	public void test () {
		Clients.showNotification("test");
	}
	// select second pane
	@Listen("onClick = #btn1")
	public void select () {
		tbx.setSelectedIndex(1);
	}
	// show selected index of tbx2
	@Listen("onSelect = #tbx2")
	public void showSelected () {
		Pane p = (Pane)tbx2.getChildren().get(tbx2.getSelectedIndex());
		Clients.showNotification("select " + p.getTitle());
	}
	// add pane to second tabs
	@Listen("onClick = #btn")
	public void add () {
		Pane p = new Pane();
		String lb = "Pane " + (tbx2.getChildren().size()+1);
		SimpleButton b = new SimpleButton(lb);
		p.setTitle(lb);
		b.addEventListener("onClick", new EventListener() {
			public void onEvent (Event event) {
				Clients.showNotification(((SimpleButton)event.getTarget()).getLabel());
			}
		});
		b.setParent(p);
		p.setParent(tbx2);
	}
	// remove 3rd or later pane from second tabs
	@Listen("onClick = #rbtn")
	public void remove () {
		List c = tbx2.getChildren();
		if (c.size() > 2) {
			c.remove(c.size()-1);
		}
	}
	// change title of first pane of second tabs
	@Listen("onClick = #cbtn")
	public void change () {
		((Pane)tbx2.getFirstChild()).setTitle("Changed title");
	}
	// update the style of 'change title' button
	@Listen("onClick = #ubtn")
	public void changeStyle () {
		try {
			cbtn.setNgTemplateUrl("/bsbutton.html");
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cbtn.setSclass("btn-success");
	}
	// hide first tabs
	@Listen("onClick = #hbtn")
	public void hideFirst () {
		tbx.setVisible(false);
	}
	// show first tabs
	@Listen("onClick = #sbtn")
	public void showFirst () {
		tbx.setVisible(true);
	}
}
