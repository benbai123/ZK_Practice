package test;

import java.util.Random;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * Provide the service that generate components with respect to the param
 * @author ben
 *
 */
public class ComponentGenerator {
	private static Integer lock = 0;
	public String generateComponent (String param) {
		// just simulate a long processing time
		// can be removed
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// for key generation
		Random random = new Random();
		String key = random.nextInt() + "";

		// make sure generate a unique key
		synchronized (lock) {
			while (GenerateViewService.getInngerMap().containsKey(key))
				key = random.nextInt() + "";
		}
		Div outer = new Div();
		Window w = new Window();
		Label l = new Label(param);
		w.setBorder("normal");
		w.setTitle("Hello ZK!");
		l.setParent(w);
		w.setParent(outer);
		GenerateViewService.getInngerMap().put(key, outer);
		return key;
	}
}