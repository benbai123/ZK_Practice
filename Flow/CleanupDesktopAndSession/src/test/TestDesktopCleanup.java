package test;

import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.Desktop;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class TestDesktopCleanup implements DesktopCleanup {
	// the cleanup function that will be called by ZK
	// while desktop destroy
	public void cleanup (Desktop desktop) {
		System.out.println(" desktop cleanup " + desktop.getId());
		TestDesktopInit.removeDataFromMapOfVeryBigDataPerDesktop(desktop.getId());
		System.out.println(" remove data from MapOfVeryBigDataPerDesktop, size = "
					+ TestDesktopInit.getSizeOfMapOfVeryBigDataPerDesktop());
		System.out.println();
	}
}
