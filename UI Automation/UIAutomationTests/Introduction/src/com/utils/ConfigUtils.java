package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;

public class ConfigUtils {
	private static Properties properties;

	static void loadTenantConfig() {
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					Constants.CONFIG_PATH + File.separator +"TestData.properties");
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			Assert.fail("Failed to load Config properties file: " + e.getMessage());
		}
	}

	public static String getConfigData(String key) {
		String propertyValue = null;
		try {
			propertyValue = properties.getProperty(key);
			if (propertyValue == null) {
				System.out.println("Unable to find the key value for the given key \"" + key + "\"");
				Assert.fail("Unable to find the key value for the given key \"" + key + "\"");
			}
		} catch (Exception e) {
			propertyValue = "";
			e.printStackTrace();
		}
		return propertyValue.trim();
	}
}
