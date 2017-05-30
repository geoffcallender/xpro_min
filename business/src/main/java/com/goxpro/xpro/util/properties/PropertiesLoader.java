/*
 * Created on May 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.goxpro.xpro.util.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goxpro.xpro.util.StringUtil;

public class PropertiesLoader {
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private String resourceName;
	private URL resourceURL;
	private File file;
	private Properties properties;
	InputStream inputStream;

	public PropertiesLoader(String resourceName) {
		this.resourceName = resourceName;
		this.resourceURL = null;
		this.properties = new Properties();

		// try classpath
		this.inputStream = this.getClass().getResourceAsStream("/" + resourceName);

		if (inputStream != null) {
			resourceURL = this.getClass().getResource("/" + resourceName);
		}
		else {
			// try local
			inputStream = this.getClass().getResourceAsStream(resourceName);

			if (inputStream != null) {
				resourceURL = this.getClass().getResource(resourceName);
			}
			else {
				// TODO - improve this message, esp. since JBoss 7 doesn't have
				// a dir on the classpath.

				throw new IllegalStateException("Failed to load properties resource \"" + resourceName
						+ "\". It should be on the classpath.  If using JBoss, put the \"" + resourceName
						+ "\" file in the server's conf/ directory.");
			}
		}
		try {
			properties.load(inputStream);
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to load properties resource \"" + resourceName
					+ "\". It should be on the classpath.  If using JBoss, put the \"" + resourceName
					+ "\" file in the server's conf/ directory.", e);
		}
	}

	public PropertiesLoader(File file) {
		this.resourceURL = null;
		this.file = file;
		this.properties = new Properties();

		String filePath = file.getAbsolutePath();

		try {
			inputStream = new FileInputStream(file);
		}
		catch (IOException e) {
			// TODO - improve this message - it was never intended for a File.

			throw new IllegalStateException("Failed to load properties file \"" + filePath + "\".", e);
		}

		try {
			properties.load(inputStream);
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to load properties file \"" + filePath + "\".", e);
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public String getOptionalProperty(String propertyName) {
		String propertyValue = properties.getProperty(propertyName);
		return propertyValue;
	}

	public String getMandatoryProperty(String propertyName) {
		String propertyValue = getOptionalStringProperty(propertyName);

		if (propertyValue == null) {
			if (resourceName != null) {
				throw new PropertyMissingException(propertyName, resourceName);
			}
			else if (file != null) {
				throw new PropertyMissingException(propertyName, file);
			}
			else {
				throw new IllegalStateException();
			}
		}

		if (propertyValue.trim().equals("")) {
			throw new PropertyEmptyException(propertyName, resourceName);
		}

		return propertyValue;
	}

	public <T extends Enum<T>> T getMandatoryEnumProperty(String propertyName, Class<T> enumClass) {
		T propertyValue = null;

		String propertyValueAsString = getMandatoryStringProperty(propertyName);

		try {
			propertyValue = Enum.valueOf(enumClass, propertyValueAsString);
		}
		catch (IllegalArgumentException e) {
			T[] allowableValues = enumClass.getEnumConstants();
			String[] allowableValuesAsStrings = new String[allowableValues.length];

			try {
				Method accessor = enumClass.getMethod("toString");

				for (int i = 0; i < allowableValues.length; i++) {
					allowableValuesAsStrings[i] = accessor.invoke(allowableValues[i]).toString();
				}
			}
			catch (Exception e1) {
				throw new IllegalStateException(e1);
			}

			throw new PropertyValueException(propertyName, this.resourceName, propertyValueAsString,
					allowableValuesAsStrings);
		}

		return propertyValue;
	}

	public String getOptionalStringProperty(String propertyName) {
		return getOptionalProperty(propertyName);
	}

	public String getOptionalStringProperty(String propertyName, String defaultValue) {
		String propertyValue = getOptionalProperty(propertyName);

		if (StringUtil.isEmpty(propertyValue)) {
			return defaultValue;
		}
		else {
			return propertyValue;
		}
	}

	public String getMandatoryStringProperty(String propertyName) {
		return getMandatoryProperty(propertyName);
	}

	public String getMandatoryStringProperty(String propertyName, String[] allowableValues) {
		String propertyValue = getMandatoryProperty(propertyName);

		boolean valid = false;
		for (int i = 0; i < allowableValues.length; i++) {
			if (propertyValue.equals(allowableValues[i])) {
				valid = true;
				break;
			}
		}

		if (!valid) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, allowableValues);
		}

		return propertyValue;
	}

	public boolean getOptionalBooleanProperty(String propertyName, boolean defaultValue) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return defaultValue;
		}
		else if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue,
					new String[] { "", "true", "false" });
		}
	}

	public boolean getMandatoryBooleanProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue,
					new String[] { "true", "false" });
		}
	}

	public int getMandatoryIntProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			int i = Integer.parseInt(propertyValue);
			return i;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " an integer.");
		}
	}

	public Integer getOptionalIntegerProperty(String propertyName) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}
		try {
			Integer i = Integer.valueOf(propertyValue);
			return i;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " an Integer.");
		}
	}

	public short getMandatoryShortProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			short s = Short.parseShort(propertyValue);
			return s;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " a short.");
		}
	}

	public Short getOptionalShortProperty(String propertyName) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}
		try {
			Short s = Short.valueOf(propertyValue);
			return s;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " a Short.");
		}
	}

	public double getMandatoryDoubleProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			double d = Double.parseDouble(propertyValue);
			return d;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " a double.");
		}
	}

	public Double getOptionalDoubleProperty(String propertyName) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}
		try {
			Double d = Double.valueOf(propertyValue);
			return d;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " a Double.");
		}
	}

	public Period getMandatoryPeriodProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			Period p = Period.parse(propertyValue);
			return p;
		}
		catch (DateTimeParseException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue,
					" a period. For valid formats see https://docs.oracle.com/javase/8/docs/api/java/time/Period.html#parse-java.lang.CharSequence- .");
		}
	}

	public void store(String headers) throws IOException {
		try {
			FileOutputStream out = new FileOutputStream(resourceURL.getFile());
			properties.store(out, headers);
		}
		catch (IOException e) {
			LOGGER.error("Could not store properties to resource \"" + resourceURL + "\" because: " + e);
			throw e;
		}
	}

	public Object setProperty(String key, String value) {
		return properties.setProperty(key, value);
	}

	public Object setProperty(String key, int value) {
		final DecimalFormat df = new DecimalFormat();
		return properties.setProperty(key, df.format(value));
	}

}
