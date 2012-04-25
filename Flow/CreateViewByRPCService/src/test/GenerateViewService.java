package test;

import java.util.*;
import javax.servlet.*;

import org.zkoss.zk.ui.Component;

import redstone.xmlrpc.XmlRpcServlet;

public class GenerateViewService extends XmlRpcServlet {
	/**
	* auto-generated serial version UID
	*/
	private static final long serialVersionUID = 7764199825847356985L;

	// the map that store the generated components
	private static Map<String, Component> generatedComponents = new HashMap<String, Component>();
	// getter of inner map
	public static Map<String, Component> getInngerMap () {
		return generatedComponents;
	}
	public void init( ServletConfig servletConfig )
		throws ServletException {
		// init
		super.init( servletConfig );
		// regist the Service
		// ComponentGenerateService is the service name for client to invoke
		// new ComponentGenerator() is the instance that provide this service
		getXmlRpcServer().addInvocationHandler( "ComponentGenerateService", new ComponentGenerator());
	}
	// get component from inner map by key
	public static Component getComponent (String key) {
		return generatedComponents.remove(key); // also remove it
	}
}
