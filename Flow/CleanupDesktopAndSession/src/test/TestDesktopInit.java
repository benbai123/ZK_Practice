package test;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.Desktop;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class TestDesktopInit implements DesktopInit {
	private static Map<String, Object> MapOfVeryBigDataPerDesktop = new HashMap<String, Object>();
	// the init function that will be called by ZK
	// while desktop initiated
	public void init(Desktop desktop, java.lang.Object request) {
		System.out.println(" desktop init " + desktop.getId());
		MapOfVeryBigDataPerDesktop.put(desktop.getId(), desktop.getId());
		System.out.println(" put data into MapOfVeryBigDataPerDesktop, size = " + MapOfVeryBigDataPerDesktop.size());
		System.out.println();
	}
	public static void removeDataFromMapOfVeryBigDataPerDesktop (String key) {
		MapOfVeryBigDataPerDesktop.remove(key);
	}
	public static int getSizeOfMapOfVeryBigDataPerDesktop () {
		return MapOfVeryBigDataPerDesktop.size();
	}
}
