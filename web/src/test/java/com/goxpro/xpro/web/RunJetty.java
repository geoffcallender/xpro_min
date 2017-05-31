package com.goxpro.xpro.web;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.ajp.Ajp13SocketConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class RunJetty {

	public static void main(String[] args) throws Exception {
		String jetty_home = System.getProperty("jetty.home");

		// Create and configure a Jetty web server.

		Server server = new Server();
		XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(jetty_home + "/etc/jetty.xml"));
		configuration.configure(server);

		// Normally we'd develop without AJP, ie. -Dgoxpro.jetty.use.ajp=false and -Dtapestry.secure-enabled=false .
		//
		// To test without AJP:
		// * Run with "-Dgoxpro.jetty.use.ajp=false" and "-Dtapestry.secure-enabled=false".
		// * Browse "http://localhost:8080/xpro" or from LAN use "http://<computer name>:8080/xpro".
		//
		// To test with AJP, using HTTP requests:
		// * Configure Apache port 80 to proxy for AJP on 8009.
		// * Run with "-Dgoxpro.jetty.use.ajp=true" and "-Dtapestry.secure-enabled=false".
		// * Browse "http://localhost/xpro" or from LAN use "http://<computer name>/xpro".
		//
		// To test with AJP, using HTTPS requests:
		// * Configure Apache port 443 to proxy for AJP on 8009.
		// * Run with "-Dgoxpro.jetty.use.ajp=true" and "-Dtapestry.secure-enabled=false".
		// * Else if you want HTTP requests redirected to HTTPS, use "-Dtapestry.secure-enabled=true".
		// * Browse "https://localhost/xpro" or from LAN use "https://<computer name>/xpro".

		List<Connector> connectors = new ArrayList<>();

		if (isUseAjp()) {
			Connector ajpConnector = new Ajp13SocketConnector();
			ajpConnector.setPort(Integer.getInteger("jetty.port", 8009));
			connectors.add(ajpConnector);
		}
		else {
			Connector httpConnector = new SelectChannelConnector();
			// ServerConnector httpConnector = new ServerConnector(server);
			httpConnector.setPort(Integer.getInteger("jetty.port", 28080));
			connectors.add(httpConnector);

			// ServerConnector httpsConnector = new ServerConnector(server);
			// httpsConnector.setPort(Integer.getInteger("jetty.port", 8443));
			// httpsConnector.setDefaultProtocol("https");
			// connectors.add(httpsConnector);
		}

		server.setConnectors(connectors.toArray(new Connector[0]));

		// Describe our web app

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/xpro");
		webAppContext.setWar("collapsed/xpro.war");
		// webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		webAppContext.setParentLoaderPriority(true);

		// Give the web app some JAAS security

		// JAASUserRealm userRealm = new JAASUserRealm("XproRealm");
		// userRealm.setLoginModuleName("JDBCLoginModule");
		// webapp.getSecurityHandler().setUserRealm(userRealm);

		// Replace the web app's list of classes with one that excludes slf4j and jaas

		// webapp.setServerClasses(new String[] { "org.mortbay.jetty." });
		// webapp.setServerClasses(new String[] { "-org.mortbay.jetty.plus.jaas.", "org.mortbay.jetty." });
		// webAppContext.setServerClasses(new String[] { "-org.eclipse.jetty.plus.jaas.", "org.eclipse.jetty." });

		// Tell our Jetty web server to handle our web app

		server.setHandler(webAppContext);

		// Start the server then wait until it stops.

		server.start();
		server.join();
	}

	private static boolean isUseAjp() {
		final String useAjpStr = System.getProperty("goxpro.jetty.use.ajp");
		return useAjpStr != null && useAjpStr.equals("true");
	}

}
