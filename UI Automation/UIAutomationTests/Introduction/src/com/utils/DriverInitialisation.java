package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;



import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverInitialisation {
    public WebDriver driver;
    public Dimension size;
    public Logger log;
    protected String browserValue;
    private ChromeOptions chromeOptions;
    @SuppressWarnings("unused")
    private SafariOptions safariOptions;
    private DesiredCapabilities desiredCapabilities;
    public static String mailReporter = "<html style=\"background-color:Azure;\"><head><style>table, td, th {border: 4px solid #ddd; text-align: center;}table {border-collapse: collapse;width: 60%;}th, td {padding: 7px;}</style></head><body><h1>Automation Test Results</h1></br> <div> <b> <i>URL :: </i> </b> ${URL}</div></br><div><b><i>Browser Name :: </i></b> ${BROWSER_NAME}</div></br><div><b><i>Environment :: </b></i> ${ENVIRONMENT}</div></br><div><b><i>Tenant :: </b></i> ${TENANT}</div></br><div><b><i>Complete Reports Path :: </b> \"\\\\10.1.177.69\\Automation Reports\\${DATE}\\${ENVIRONMENT}_${TENANT}</i></div></br></br><h3>Please Refer the Emailable Report for the Test Results</h3>";
    public String browserName = "";
   
    @Parameters(value = { "browser"})
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(String browser) throws Exception {
        try {
            browserName = browser;
            DOMConfigurator.configure(Constants.LOG4J_CONFIG_PATH);
            log = Logger.getLogger(this.getClass().getSimpleName());
            ConfigUtils.loadTenantConfig();
            ObjectRepositoryUtils.loadTenantObjectRepository();
        } catch (WebDriverException e) {
            log.info(e.getMessage());
        }
    }

    @Parameters(value = { "browser"})
    @BeforeTest(alwaysRun = true)
    public void beforeTest(String browser) {
        browserValue = browser;
        try {
            browserName = browser;
            switch (browser.toLowerCase()) {
            case "firefox":
                   break;

            case "chrome":
                chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-features=EnableEphemeralFlashPermission");
                  // system
                 System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER);
                	//WebDriverManager.chromedriver().setup();
                // adding basic desired capabilities of chrome
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_setting_values.plugins", 1);
                chromePrefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
                chromePrefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
                /*
                 * chromePrefs.put("PluginsAllowedForUrls",
                 * "https://unifiedqa.clearhub.tv/BC/Product/Modules/SignIn.aspx" );
                 */
                // Enable Flash for this site

                // Disable the content related popup blocking
                chromePrefs.put("profile.default_content_settings.popups", 1);
                // enable safe browsing
                // chromePrefs.put("safebrowsing.enabled", "true");
                // setting default download directory path
                chromePrefs.put("download.default_directory", Constants.HTTP_DOWNLOAD_PATH);
                chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
                chromePrefs.put("download.prompt_for_download", false);
                chromePrefs.put("safebrowsing.enabled", "true");
                chromePrefs.put("profile.default_content_setting_values.notifications", 1);
                chromePrefs.put("webkit.webprefs.allow_running_insecure_content", 1);
                // chromePrefs.put("excludeSwitches","disable-component-update");
                // C:\Users\BHARGH~1.KON\AppData\Local\Temp\scoped_dir2672_6708\pnacl\0.57.44.2492\_platform_specific\x86_64
                // chromeOptions.addArguments("--pnacl-dir=C:\\Users\\BHARGH~1.KON\\AppData\\Local\\Temp\\scoped_dir2672_6708\\pnacl\\0.57.44.2492\\_platform_specific\\x86_64");
                chromeOptions.setExperimentalOption("prefs", chromePrefs);
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                // To disable all the certificate errors
                chromeOptions.addArguments("-test-type");
                // To open the browser in incognito mode
                chromeOptions.addArguments("--disable-notifications");
                // chromeOptions.addArguments("--incognito");
                // openbrowser maximized state
                //chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("start-maximized");
                // Always allow the authorized plugins
                chromeOptions.addArguments("--always-authorize-plugins=true");
                // chromeOptions.addArguments("disable-extensions");
                chromeOptions.addArguments("--enable-automation");
                // enable native client
                // chromeOptions.addArguments("--enable-npapi");
                chromeOptions.addArguments("-enable-pnacl");
                // chromeOptions.addArguments("--enable-pnacl");
                // chromeOptions.addArguments("enable-pnacl");
                chromeOptions.addArguments("-enable-nacl");
                // chromeOptions.addArguments("--enable-nacl");
                // Disable popup blocking
                chromeOptions.addArguments("-disable-popup-blocking=true");
                // chromeOptions.addArguments("--disable-dev-shm-usage");
               // Don't show any infobars in the browser
                chromeOptions.addArguments("disable-infobars");
                //chromeOptions.addArguments("--incognito");
			    chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                // always accept secure socket layer alerts
                driver=new ChromeDriver(chromeOptions);
               break;

               case "edge":
            }
            
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            Assert.fail(e.getMessage());
        }

    }

    public WebDriver Driver() {
        return driver;
    }

    @AfterTest(alwaysRun = true)
    public void closeBrowser() {
        System.gc();
        try {
            driver.close();
            
        } catch (Exception e) {

        }
    }


}