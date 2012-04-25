package test;


import java.net.MalformedURLException;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import redstone.xmlrpc.*;

public class TestVM {
	// default request value
	private String _rpcTarget = "http://localhost:8080/CreateViewByRPCService/generateViewRpc/user/ben/page/1";
	// target address to require service
	private String _target = "http://localhost:8080/CreateViewByRPCService/generateViewRpc";
	// the key mapping to the stored components
	private String _mappingKey = null;
	// id used in zul file for timer
	private String _timerId = "timer";
	// id used in zul file for container div
	private String _containerId = "container";
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
			_cnt--;
			if (_mappingKey != null) { // has response
				Timer t;
				Div container = null;
				for (Component c : Executions.getCurrent().getDesktop().getComponents()) {
					if (_timerId.equals(c.getId()) && _cnt == 0) {
						// stop the timer
						((Timer)c).stop();
					} else if (_containerId.equals(c.getId())) {
						// get the container
						container = (Div)c;
					}
				}
				// put generated components into container
				GenerateViewService.getComponent(_mappingKey).setParent(container);
				_mappingKey = null;
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
			// store the mapping key
			_mappingKey = (String)result;
		}
	};
}