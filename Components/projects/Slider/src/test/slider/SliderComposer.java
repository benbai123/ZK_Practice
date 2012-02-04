package test.slider;

import java.util.*;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

public class SliderComposer extends GenericForwardComposer {

	Image imgOne;
	Image imgTwo;
	Image imgThree;
	Intbox interval;
	Button start;
	Button stop;
	List<String> contentList = null;
	int length;
	int index = 0;

	public void doAfterCompose (Component comp) throws Exception {
		super.doAfterCompose(comp);
		// init first part image
		imgOne.setSrc(getSrc());
		imgTwo.setSrc(getSrc());
		imgThree.setSrc(getSrc());
		Clients.evalJavaScript("setContent('"+getContentString()+"')");
	}
	public void onClick$start () {
		int delay = interval.getValue();
		String command = "startSlideShow(" + delay*1000 + ")";
		System.out.println(command);
		Clients.evalJavaScript(command);
	}
	public void onClick$stop () {
		String command = "stopSlideShow()";
		Clients.evalJavaScript(command);
	}
	private String getContentString() {
		StringBuilder sb = new StringBuilder();
		List l = getContentList();
		for (int i = 0; i < l.size(); i++) {
			if (i > 0)
				sb.append(",");
			sb.append(l.get(i));
		}
		return sb.toString();
	}
	private List<String> getContentList () {
		if (contentList == null) {
			// modify here for dynamic assign images
			contentList = new ArrayList<String>();
			for (int i = 0; i < 1000; i++)
				contentList.add("img/my_draw_"+i+".png");
		}
		length = contentList.size();
		return contentList;
	}
	private String getSrc () {
		String src = getContentList().get(index);
		index = (index+1) % length;
		return src;
	}
}
