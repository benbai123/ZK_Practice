package test;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LastAccessFilter implements Filter {
	public void init(FilterConfig fc) {}
	public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain) {
		HttpServletRequest hreq = (HttpServletRequest)req;
		HttpSession sess = hreq.getSession();
		String checker_id = (String)sess.getAttribute("checker_id");
		String componentId = hreq.getParameter("uuid_0");

		if (componentId == null || !componentId.equals(checker_id)) {

			// only do if not request by check timer
			long last = sess.getLastAccessedTime();
			sess.setAttribute("real_last", last);
		} else if (sess.getAttribute("real_last") == null) {
			// need the initiate however
			long last = sess.getLastAccessedTime();
			sess.setAttribute("real_last", last);
		}
		try {
			chain.doFilter(req, resp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	public void destroy() {}
}
