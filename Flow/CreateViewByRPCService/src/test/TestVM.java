package test;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import redstone.xmlrpc.*;

public class TestVM {
	// default request value
	private String _rpcTarget = "http://localhost:8080/CreateViewByRPCService/generateViewRpc/user/ben/page/1";
	// target address to require service
	private String _target = "http://localhost:8080/CreateViewByRPCService/generateViewRpc";
	// id used in zul file for timer
	private String _timerId = "timer";
	// id used in zul file for container div
	private String _containerId = "container";
	// the list to store generated components
	private List<Component> components = new ArrayList<Component>();
	// for prevent race
	private Integer _lock = 0;
	// request counter
	private int _cnt = 0;

	public void setRpcTarget (String rpcTarget) {
		_rpcTarget = rpcTarget;
	}
	public String getRpcTarget () {
		return _rpcTarget;
	}
	public String getTimerId () {
		return _timerId;
	}
	public String getContainerId () {
		return _containerId;
	}

	/**
	 * Make a request to require service that generate components
	 */
	@Command
	public void generateComponent () {
		synchronized (_lock) {
			_cnt++;
		}
		// start the timer to check the result
		for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
			if (_timerId.equals(c.getId()))
				((Timer)c).start();
		}
		try {
			// get param from rpc target
			String param = _rpcTarget.replace("http://localhost:8080/CreateViewByRPCService/generateViewRpc", "");
			// create rpc client
			XmlRpcClient client = new XmlRpcClient( _target, true);
			// invoke RPC method with param
			client.invokeAsynchronously( "ComponentGenerateService.generateComponent", new Object[] {param}, xrc);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * check whether the components are generated
	 */
	@Command
	public void checkGenerate () {
		synchronized (_lock) {
			if (components.size() > 0) { // has response
				Timer t = null;
				Div container = null;
				for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
					if (_timerId.equals(c.getId())) {
						// get the timer
						t = ((Timer)c);
					} else if (_containerId.equals(c.getId())) {
						// get the container
						container = (Div)c;
					}
				}
				// put generated components into container
				while (components.size() > 0) {
					_cnt--;
					components.remove(0).setParent(container);
				}
				// stop the timer if no more component
				if (_cnt == 0)
					t.stop();
			}
		}
	}
	// the callback for async RPC
	private XmlRpcCallback xrc = new XmlRpcCallback() {
		public void onException(XmlRpcException exception) {
			exception.printStackTrace();
		}
		public void onFault(int faultCode, java.lang.String faultMessage) {}
		// response
		public void onResult(java.lang.Object result) {
			// store the component
			components.add(GenerateViewService.getComponent((String)result));
		}
	};
}