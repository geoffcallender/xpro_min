/*
 * Created on Jul 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.goxpro.xpro.util.properties;

import java.io.File;

/**
 * @author geocal
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class PropertyMissingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String propertyName;
	private String resourceName;
	private File file;

	PropertyMissingException(String propertyName, String resourceName) {
		this.propertyName = propertyName;
		this.resourceName = resourceName;
		this.file = null;
	}

	PropertyMissingException(String propertyName, File file) {
		this.propertyName = propertyName;
		this.resourceName = null;
		this.file = file;
	}

	@Override
	public String getMessage() {
		if (resourceName != null) {
			return "Property \"" + propertyName + "\" is missing from resource \"" + resourceName + "\".";
		}
		else if (file != null) {
			return "Property \"" + propertyName + "\" is missing from file \"" + file.getAbsolutePath() + "\".";
		}
		else {
			throw new IllegalStateException();
		}
	}
}
