package com.goxpro.xpro.web.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.compatibility.Trait;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

import com.goxpro.xpro.web.services.security.SecurityServiceLocator;

public class AppModule {

	// Add a service to those provided by Tapestry.

	public static void bind(ServiceBinder binder) {
		binder.bind(SecurityServiceLocator.class);

		// This next line addresses an issue affecting GlassFish and JBoss - see http://blog.progs.be/?p=52
		// javassist.runtime.Desc.useContextClassLoader = true;
	}

	public static void contributeCompatibility(MappedConfiguration<Trait, Boolean> configuration) {
		// Disable scriptaculous. Is this step redundant, given we're set the infrastructure provider to jquery instead
		// of prototype?
		configuration.add(Trait.SCRIPTACULOUS, false);
	}

	// Tell Tapestry how to propagate the security context to the business layer (necessary when not in an app server).
	// We do this by contributing a custom ComponentRequestFilter to Tapestry's ComponentRequestHandler service.
	// - ComponentRequestHandler is shown in
	// http://tapestry.apache.org/request-processing.html#RequestProcessing-Overview

	public void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration) {
		configuration.addInstance("SecurityContextPropagator", SecurityContextPropagator.class);
	}

	// Tell Tapestry how to handle @EJB in page and component classes.
	// We do this by contributing configuration to Tapestry's ComponentClassTransformWorker service.
	// - Based on http://wiki.apache.org/tapestry/JEE-Annotation.

	@Primary
	public static void contributeComponentClassTransformWorker(
			OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
		configuration.addInstance("EJB", EJBAnnotationWorker.class, "before:Property");
	}

	public static void contributeResteasyPackageManager(Configuration<String> configuration) {
		configuration.add("com.goxpro.xpro.web.rest");
		configuration.add("com.goxpro.xpro.web.recurly.rest");
	}

}
