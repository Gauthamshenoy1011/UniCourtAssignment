package com.utils;

import java.io.File;
import java.util.HashMap;

public class Constants {
	// Set your Project Folder here
	public static final String PROJECT_HOME = System.getProperty("user.dir");

	
	public static final int IMPLICIT_WAIT = 60;
	public static final int IMPLICIT_WAIT_DEFAULT = 0;

	/* Config Paths */

	public final static HashMap<String, String> argumentsForExecution = new HashMap<String, String>();

	public static final String LOG4J_CONFIG_PATH = PROJECT_HOME + File.separator + "Config" + File.separator
			+ "log4j.xml";
	public static final String LOGIN_CONFIG_PATH = PROJECT_HOME + File.separator + "Config" + File.separator;
	public static final String LOGIN = PROJECT_HOME + File.separator + "Config" + File.separator + "testfile.xml";
	public static final String LOGIN_CONFIG_XML_PATH = PROJECT_HOME + File.separator + "Config" + File.separator
			+ "Login.xml";
	public static final String CONFIG_PATH = PROJECT_HOME + File.separator + "Config" + File.separator;

	public static final String TESTDATA_PATH = PROJECT_HOME + File.separator + "TestData" + File.separator;
	public static final String HTTPHEADER_CHROME = PROJECT_HOME + File.separator + "ExternalJARs" + File.separator
			+ "HttpHeaderChrome.crx";
	public static final String CATALOG_DETAILS = PROJECT_HOME + File.separator + "TestData" + File.separator
			+ "catalog.xml";
	public static final String OBJECTREPOSITORY_PATH = PROJECT_HOME + File.separator +"Config" +File.separator + "ObjectRepository.properties";
	public static final String DOWNLOAD_PATH = PROJECT_HOME + File.separator + "DownloadFile";
	public static final String EXTERNAL_JARS_PATH = PROJECT_HOME + File.separator + "ExternalJARs" + File.separator;

	public static final String HTTP_DOWNLOAD_PATH = "D:" + File.separator
			+ "Automation Downloads" + File.separator;
	public static final String CHROME_DRIVER = PROJECT_HOME + File.separator + "Drivers" + File.separator
			+ "chromedriver.exe";

	public static final String ASPERA_DOWNLOAD_PATH = HTTP_DOWNLOAD_PATH + File.separator + "AsperaDownload"
			+ File.separator;

	public static final String EXTENT_CONFIG_XML_FILE = PROJECT_HOME + File.separator + "ExternalJARs" + File.separator
			+ "ExtentConfig.xml";
	public static final String EXTENT_REPORT_FILE = PROJECT_HOME + File.separator + "target" + File.separator
			+ "AutomationReport.html";
	public static final String MAIL_REPORTER_FILE = PROJECT_HOME + File.separator + "target" + File.separator
			+ "MailReporter.html";
	public static final String USERDATA_TEMP_FOLDER_PATH = PROJECT_HOME + File.separator + "Temp" + File.separator;

	public static final String EXTENT_REPORT_PATH = PROJECT_HOME + File.separator + "target" + File.separator
			+ "Automation ModuleWise Reports";
}