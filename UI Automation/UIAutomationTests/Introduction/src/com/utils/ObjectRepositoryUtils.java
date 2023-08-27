package com.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.testng.Assert;

public class ObjectRepositoryUtils {

	private static HashMap<String, String> locators = new HashMap<>();

	static void loadTenantObjectRepository() {
		try {
					FileInputStream fis = new FileInputStream(
							Constants.OBJECTREPOSITORY_PATH );
					Properties prop = new Properties();
					prop.load(fis);
					Set<Entry<Object, Object>> properties = prop.entrySet();
					for (Entry<Object, Object> property : properties) {
						locators.put((String) property.getKey(), (String) property.getValue());
					}
					fis.close();
				
			}
		 catch (IOException e) {
			Assert.fail("Failed to load Object Repository properties file: " + e.getMessage());
		}
	}

	public static By getLocator(String strElement) {
		try {

		// retrieve the specified object from the object list
		String locator = locators.get(strElement);
		if (locator == null) {
			return By.id("No Locators are present for key :: \"" + strElement + "\"");
		}

		// extract the locator type and value from the object

		String[] locatorSplit = locator.split(":", 2);

		String locatorType = locatorSplit[0];
		String locatorValue = locatorSplit[1];

		// for testing and debugging purposes
		// System.out.println("Retrieving object of type '" + locatorType + "'
		// and value '" + locatorValue + "' from the object map");

		// return a instance of the By class based on the type of the locator
		// this By can be used by the browser object in the actual test
		if (locatorType.toLowerCase().equals("id"))
			return By.id(locatorValue);
		else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return By.tagName(locatorValue);
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return By.linkText(locatorValue);
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return By.partialLinkText(locatorValue);
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return By.cssSelector(locatorValue);
		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);
		else {
			return By.id("No Locators are present for key :: \"" + strElement + "\"");
		}
		} catch(java.lang.ArrayIndexOutOfBoundsException e) {
			return By.id("No Locator type is assigned for the key :: \"" + strElement + "\"");
		}
		
	}
}
