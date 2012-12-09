package test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.zkoss.zk.ui.http.HttpSessionListener;

/**
 * Tested with ZK 6.0.2
 * @author benbai
 *
 */
public class TestSessionCleaner extends HttpSessionListener {

	private Map<String, Object> MapOfVeryBigDataPerSession = new HashMap<String, Object>();
	// called while session created
	public void sessionCreated(HttpSessionEvent evt) {
		super.sessionCreated(evt);
		HttpSession session = evt.getSession();
		System.out.println(" session created " + session.getId());
		MapOfVeryBigDataPerSession.put(session.getId(), session.getId());
		System.out.println(" put data into MapOfVeryBigDataPerSession, size = " + MapOfVeryBigDataPerSession.size());
		System.out.println();
	}
	// called while session destroy
	public void sessionDestroyed(HttpSessionEvent evt) {
		super.sessionDestroyed(evt);
		HttpSession session = evt.getSession();
		System.out.println(" session destroyed " + session.getId());
		MapOfVeryBigDataPerSession.remove(session.getId());
		System.out.println(" remove data from MapOfVeryBigDataPerSession, size = " + MapOfVeryBigDataPerSession.size());
		System.out.println();
	}
}
