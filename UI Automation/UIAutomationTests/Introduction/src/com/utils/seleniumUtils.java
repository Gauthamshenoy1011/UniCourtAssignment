package com.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;

import com.aventstack.extentreports.Status;
import com.listeners.Listener;
import com.listeners.ScreenshotListener;

public class seleniumUtils extends StringUtils {

	private final static String failedScreenShotsFolderPath = System.getProperty("user.dir") + "/target/FailedScreenShots/";
	private final int counter = 60;
	private final int counter1 = 180;
	protected WebDriver driver;
	private Logger logger;
	int i = 0;

	public seleniumUtils(WebDriver driver) {
		this.driver = driver;
		logger = LogManager.getLogger(this.getClass().getSimpleName());
	}

	/**
	 * Waits for element to display in web page and fails the test case if
	 * element is not displayed in specific time
	 * 
	 * @param=By element locator for which you are waiting for it to become
	 *           visible
	 * @author Anonymous
	 */
	final public void WaitUntilElementIsVisible(By element) {
		try {
			scroll(element);
			implicitlyWait(0);
			int i = 0;
			for (; i < counter; i++) {
				try {
					if (driver.findElement(element).isDisplayed())
						break;
					else {
						logger.info("Waiting for element to be visible !!! " + i + " seconds :: " + element);
						waitFor(1);
					}

				} catch (NoSuchElementException | StaleElementReferenceException e) {
					logger.info("Waiting for element to be visible !!! " + i + " seconds :: " + element);
					waitFor(1);
				} catch (Exception e) {
					logger.error("cannot wait untill element is visible due to " + e.getMessage());
					fail("cannot wait untill element is visible due to " + e.getMessage());
				}
			}
			if (i >= counter) {
				logger.error("Element is not displayed after " + i + " seconds element is :: " + element);
				fail("Element is not displayed after " + i + " seconds element is :: " + element);
			}
		} catch (Exception e) {
		} finally {
			implicitlyWait(60);
		}
	}

	/**
	 * waits for web element to become invisible for given time and fails the
	 * test case if element is displayed after defined time
	 * 
	 * @param=By element locator in the form of By
	 * @author Anonymous
	 */
	final public void WaitUntilElementIsInVisible(By element) {
		try {
			implicitlyWait(0);
			int i;
			for (i = 0; i < counter; i++) {
				try {
					boolean flag = driver.findElement(element).isDisplayed();
					if (!flag) {
						break;
					} else {
						logger.info("Waiting for element to be Invisible !!! " + i + " seconds " + element);
						waitFor(1);
					}
				} catch (StaleElementReferenceException r) {
					logger.info("Waiting for element to be Invisible !!! " + i + " seconds " + element);
					waitFor(1);
				} catch (NoSuchElementException e) {
					break;
				} catch (Exception e) {
					logger.error("Cannot Verify Whether element is displayed or not due to " + e.getMessage());
					fail("Cannot Verify Whether element is displayed or not due to " + e.getMessage());
				}
			}
			if (i >= counter) {
				logger.error(element + " Element is  displayed after " + i + " seconds");
				fail(element + " Element is  displayed after " + i + " seconds");
			}
		} catch (Exception e) {
		} finally {
			implicitlyWait(60);
		}

	}

	final public void WaitUntilElementIsPassed(By element) {
		try {
			implicitlyWait(0);
			int i;
			for (i = 0; i < counter1; i++) {
				try {
					String result = driver.findElement(element).getText();
					System.out.println(result);
					if (result.equalsIgnoreCase("Failed")) {
						break;
					} else {
						logger.info("Waiting for element to be Invisible !!! " + i + " seconds " + element);
						waitFor(1);
					}
				} catch (StaleElementReferenceException r) {
					logger.info("Waiting for element to be Invisible !!! " + i + " seconds " + element);
					waitFor(1);
				} catch (NoSuchElementException e) {
					break;
				} catch (Exception e) {
					logger.error("Cannot Verify Whether element is displayed or not due to " + e.getMessage());
					fail("Cannot Verify Whether element is displayed or not due to " + e.getMessage());
				}
			}
			if (i >= counter1) {
				logger.error(element + " Element is  displayed after " + i + " seconds");
				fail(element + " Element is  displayed after " + i + " seconds");
			}
		} catch (Exception e) {
		} finally {
			implicitlyWait(60);
		}

	}

	/**
	 * To check whether element is displayed or not
	 * 
	 * @return=true if element is displayed
	 * @return=false if element is not displayed
	 * @param=By element locator in the form of By
	 * @author Anonymous
	 */
	final public boolean isDisplayed(By element) {
		boolean flag = false;
		int counter = 05;
		if (driver instanceof InternetExplorerDriver)
			counter = 10;
		implicitlyWait(0);
		try {
			for (int i = 0; i < counter; i++) {
				try {
					flag = driver.findElement(element).isDisplayed();
					if (flag) {
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (Exception e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return flag;
	}

	
	
	final public boolean isDisplayedText(By element, int count) {
		boolean flag = false;
		int counter = 05;
		if (driver instanceof InternetExplorerDriver)
			counter = 10;
		implicitlyWait(0);
		try {
			for (int i = 0; i < counter; i++) {
				try {
					flag = driver.findElement(element).isDisplayed();
					if (flag) {
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (Exception e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return flag;
	}
	/**
	 * To check whether element is displayed or not
	 * 
	 * @return=true if element is displayed
	 * @return=false if element is not displayed
	 * @param=By element locator in the form of By
	 * @author Anonymous
	 */
	final public boolean isDisplayed(By element, int WaitTime) {
		boolean flag = false;
		try {
			implicitlyWait(0);
			for (int i = 0; i < WaitTime; i++) {
				try {
					flag = driver.findElement(element).isDisplayed();
					if (flag) {
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (Exception e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return flag;
	}

	/**
	 * To check whether element is present or not
	 * 
	 * @return=true if element is displayed
	 * @return=false if element is not displayed
	 * @param=By element locator in the form of By
	 * @author Anonymous
	 */
	final public boolean isPresent(By element) {
		int counter = 05;
		try {
			implicitlyWait(0);
			for (int i = 0; i < counter; i++) {
				try {
					driver.findElement(element);
					return true;
				} catch (Exception e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return false;
	}

	/**
	 * select element in dropDown folder
	 * 
	 * @param element      drop down locator along with option tag;
	 * @param SelectOption text in dropDown
	 * @see #WaitUntilElementIsVisible(By)
	 * @author Anonymous
	 */
	final public boolean Select(By element, String SelectOption) {
		boolean flag = false;
		try {
			WaitUntilElementIsVisible(element);
			scrollIntoView(element);
			//List<WebElement> SelectBoxOptions = findElements(element);
			Select se = new Select(findElement(element));
			List<WebElement> SelectBoxOptions = se.getOptions();
			for (WebElement Option : SelectBoxOptions) {
				String OptionsText = Option.getText().trim();
				if (OptionsText.equals(SelectOption.trim())) {
					Option.click();
					flag = true;
					break;
				}
			}
			if (flag) {
				logger.info("successfully selected option : " + SelectOption);
			} else {
				logger.error("cannot Select the option : " + SelectOption + " in the element :: " + element);
				fail("cannot Select the option : " + SelectOption + " in the element :: " + element);
			}
		} catch (Exception e) {
			logger.error("cannot Select the option : " + SelectOption + " in the element :: " + element);
			fail("cannot Select the option : " + SelectOption + " in the element :: " + element);
		}
		return flag;
	}
	
	/**
	 * select element in dropDown folder
	 * 
	 * @param element      drop down locator along with option tag;
	 * @param SelectOption text in dropDown
	 * @see #WaitUntilElementIsVisible(By)
	 * @author Anonymous
	 */
	final public boolean Select(WebElement element, String SelectOption) {
		boolean flag = false;
		try {
			waituntilWebElementIsvisible(element);
			scrollIntoView(element);
			//List<WebElement> SelectBoxOptions = findElements(element);
			Select se = new Select(element);
			List<WebElement> SelectBoxOptions = se.getOptions();
			for (WebElement Option : SelectBoxOptions) {
				String OptionsText = Option.getText().trim();
				if (OptionsText.equals(SelectOption.trim())) {
					Option.click();
					flag = true;
					break;
				}
			}
			if (flag) {
				logger.info("successfully selected option : " + SelectOption);
			} else {
				logger.error("cannot Select the option : " + SelectOption + " in the element :: " + element);
				fail("cannot Select the option : " + SelectOption + " in the element :: " + element);
			}
		} catch (Exception e) {
			logger.error("cannot Select the option : " + SelectOption + " in the element :: " + element);
			fail("cannot Select the option : " + SelectOption + " in the element :: " + element);
		}
		return flag;
	}

	/**
	 * wait until element is present in document object model (DOM)
	 * 
	 * @param=By element you want to wait for till that element loads in the DOM
	 * @author Anonymous
	 */
	final public void WaitUntilElementIsPresent(By element) {
		try {
			boolean flag = false;
			logger.info("Waiting for the element :: \"" + element + "\"");
			implicitlyWait(0);
			for (int i = 0; i < counter; i++) {
				try {
					driver.findElement(element);
					flag = true;
					break;
				} catch (NoSuchElementException | StaleElementReferenceException e) {
					logger.info("Waiting for element to be present in DOM " + i + " seconds element:: " + element);
					waitFor(1);
				} catch (Exception e) {
					e.getStackTrace();
					break;
				}
			}
			if (flag) {
				logger.info("Element :: \"" + element + "\" is present in the DOM");
			} else {
				logger.error("Element is not present in DOM element:: " + element);
				fail("Element is not present in DOM element:: " + element);
			}
		} catch (Exception e) {
		} finally {
			implicitlyWait(60);
		}
	}

	/**
	 * wait until element is displayed in browser and element is enabled for
	 * clicking
	 * 
	 * @param=By element you want to wait for till it is clickable
	 * @author Anonymous
	 */
	final public void WaitUntilElementIsClickable(By element) {
		try {
			boolean flag = false;
			String message = "Element is disabled";
			WaitUntilElementIsVisible(element);
			implicitlyWait(0);
			for (int i = 0; i < counter; i++) {
				try {
					if (driver.findElement(element).isEnabled()) {
						flag = true;
						break;
					} else {
						logger.info("Waiting for element to be clickable " + i + " seconds");
						waitFor(1);
					}
				} catch (Exception e) {
					logger.info("Waiting for element to be clickable " + i + " seconds");
					waitFor(1);
					message = e.getMessage();
				}
			}
			if (flag) {
			} else {
				logger.error(element + " is not clickable till " + counter + " seconds due to " + message);
				fail(element + " is not clickable till " + counter + " seconds due to " + message);
			}
		} catch (Exception e) {
		} finally {
			implicitlyWait(60);
		}
	}

	/**
	 * scroll to the required element in web page using BY element
	 * 
	 * @param=By element you want to scroll to
	 * @author Anonymous
	 */
	final public void scroll(By element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			WaitUntilElementIsPresent(element);
			if (!isDisplayedNow(element)) {
				js.executeScript("arguments[0].scrollIntoView(false);", driver.findElement(element));
			}
			logger.info("Succesfully scrolled to element :: \"" + element + "\"");
		} catch (Exception e) {
			logger.error("Failed to scroll to " + element + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	/**
	 * scroll to the given web element
	 * 
	 * @param=WebElement web element you want to scroll to
	 * @author Anonymous
	 */
	final public void scroll(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			if (!element.isDisplayed()) {
				js.executeScript("arguments[0].scrollIntoView(false)", element);
			}
		} catch (Exception e) {
			logger.error("Failed to scroll to " + element + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	/**
	 * clicks on given element using driver
	 * 
	 * @param=By element locator you want to click using driver
	 * @author Anonymous
	 */
	final public void click(By element) {
		try {
			scroll(element);
			WaitUntilElementIsClickable(element);
			highlightAnElement(element);
			if (driver instanceof InternetExplorerDriver) {
				pageLoadTimeout(0);
				try {
					driver.findElement(element).click();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			} else {
				driver.findElement(element).click();
			}
			logger.info("successfully clicked on element " + element);
		} catch (StaleElementReferenceException e) {
			waitFor(1);
			driver.findElement(element).click();
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			unHighlightAnElement(element);
		}
	}

	/**
	 * clicks on given element using driver
	 * 
	 * @param=WebElement element locator you want to click using driver
	 * @author Anonymous
	 */
	final public void click(WebElement element) {
		try {
			scroll(element);
			waitFor(1);
			waituntilWebElementIsvisible(element);
			highlightAnElement(element);
			if (driver instanceof InternetExplorerDriver) {
				pageLoadTimeout(0);
				try {
					element.click();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			} else {
				element.click();
			}
			logger.info("successfully clicked on element " + element);
		} catch (TimeoutException e) {
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			unHighlightAnElement(element);
		}
	}

	/**
	 * fails the test case and takes the screenshot with the test name and adds the
	 * link to the report
	 * 
	 * @param=String reason of the failure
	 * @author Anonymous
	 */
	final public void fail(String failureMessage) {
		try {
			String filePath;
			String methodName = getPresentRunningTestName();
		} catch (Exception e) {
		} finally {
			logger.error(failureMessage,new Exception());
			org.testng.Assert.fail(failureMessage);
		}

	}
	
	/**
	 * Takes screenshot of the particular driver
	 * link to the report
	 * 
	 * @param=String reason of the failure
	 * @author vishal.s
	 */
	public static String getScreenshotPath(String TestCaseName,WebDriver driver) throws IOException
	{
		String filePath = null;
		//String methodName = getPresentRunningTestName();
		/*ScreenshotListener.logger.log(Status.FAIL,
				"<b><i>Failure message is :: </b></i><textarea>" + failureMessage + "</textarea>");
		ScreenshotListener.moduleWiseLogger.log(Status.FAIL,
				"<b><i>Failure message is :: </b></i><textarea>" + failureMessage + "</textarea>");*/
		String scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		ScreenshotListener.extentTest.get().log(Status.INFO,
				"<img height=\"195\" width=\"195\" src='data:image/png;charset=utf-8;base64," + scrFile1
						+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"> Mouse Hover Here For Screenshot </img>"
						+ "<script> function bigImg(x) { x.style.height = \"500px\"; x.style.width = \"750px\";}  function normalImg(x)"
						+ " { x.style.height = \"195px\";  x.style.width = \"195px\";}</script>");
		/*ScreenshotListener.extentTest.get().log(Status.INFO,
				"<img height=\"195\" width=\"195\" src='data:image/png;charset=utf-8;base64," + scrFile1
				+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"> Mouse Hover Here For Screenshot </img>"
				+ "<script> function bigImg(x) { x.style.height = \"500px\"; x.style.width = \"750px\";}  function normalImg(x)"
				+ " { x.style.height = \"195px\";  x.style.width = \"195px\";}</script>");*/
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		filePath = failedScreenShotsFolderPath + TestCaseName + " " + timeStamp + ".png";
		new File(failedScreenShotsFolderPath).mkdirs(); // Ensure directory
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(filePath));
		//Reporter.log("Failure message is :: " + failureMessage);
		Reporter.log("<tbody><tr><td><a href='../FailedScreenShots/" + TestCaseName + " " + timeStamp + ".png"
				+ "' title=\"Failure reference screenshot\">Screenshot</a><br></br><img height=\"42\" width=\"42\" src='data:image/png;charset=utf-8;base64,"
				+ scrFile1
				+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"><strong>Mouse Hover Here For Screenshot</strong></img>"
				+ "<script> function bigImg(x) { x.style.height = \"450px\"; x.style.width = \"1200px\";}  function normalImg(x) { x.style.height = \"20px\";  x.style.width = \"20px\";}</script>"
				+ "</td></tr>" + "<tr><td><a href='../FailedTestCaseVideos/" + TestCaseName
				+ ".mp4' title=\"Failure reference video\">Video</a></td></tr></tbody>");
		return filePath;
	}
	
	/**
	 * Takes screenshot of the particular driver
	 * link to the report
	 * 
	 * @param=String reason of the failure
	 * @author vishal.s
	 */
	public static String getScreenshotPathWithBase64(String TestCaseName,WebDriver driver) throws IOException
	{
		String scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		return scrFile1;
	}

	/**
	 * waits until web element is visible
	 * 
	 * @param=WebElement web element you want to wait for till that is visible
	 * @author Anonymous
	 */
	final public void waituntilWebElementIsvisible(WebElement element) {
		try {
			int counter = 0;
			while (!element.isDisplayed()) {
				if (counter > this.counter) {
					logger.error("Failed to Display Element  " + element + " in " + counter + " secs");
					fail("Failed to Display Element  " + element + " in " + counter + " secs");
					break;
				} else {
					counter++;
					waitFor(1);
					logger.info("Loading...!!!" + counter + " secs over element :: " + element);
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Locates the elements present in the portal with given locator
	 * 
	 * @param=By Locator of the elements you want to find
	 * @return=List<WebElement> returns the list of web elements
	 * @author Anonymous
	 */
	final public List<WebElement> findElements(By elements) {
		List<WebElement> element = null;
		try {
			// WaitUntilElementIsPresent(elements);
			element = driver.findElements(elements);
		} catch (Exception e) {
			logger.error("unable to find elements due to " + e.getMessage());
			fail("unable to find elements due to " + e.getMessage());
		}
		return element;
	}

	/**
	 * Locates the element present in the portal with given locator
	 * 
	 * @param=By Locator of the element you want to find
	 * @return=WebElement returns the web element
	 * @author Anonymous
	 */
	final public WebElement findElement(By element) {
		WebElement webElement = null;
		try {
			WaitUntilElementIsPresent(element);
			webElement = driver.findElement(element);
		} catch (Exception e) {
			logger.error("unable to find the element " + element + " due to " + e.getMessage());
			fail("unable to find the element " + element + " due to " + e.getMessage());
		}
		return webElement;
	}

	/**
	 * clears the text present in the input box
	 * 
	 * @param=By locator of the input box
	 * @author Anonymous
	 */
	final public void clear(By element) {
		try {
			click(element);
			findElement(element).clear();
		} catch (Exception e) {
			logger.error(
					"unable to clear data present in input box for element " + element + " due to " + e.getMessage());
			fail("unable to clear data present in input box for element " + element + " due to " + e.getMessage());
		}
	}

	/**
	 * clears the text present in the input box
	 * 
	 * @param=WebElement web element of the input box
	 * @author Anonymous
	 */
	final public void clear(WebElement element) {
		try {
			click(element);
			element.clear();
		} catch (Exception e) {
			logger.error(
					"unable to clear data present in input box for element " + element + " due to " + e.getMessage());
			fail("unable to clear data present in input box for element " + element + " due to " + e.getMessage());
		}
	}

	/**
	 * keys required to be entered in the input box
	 * 
	 * @param=By locator of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void sendKeys(By element, String keys) {
		try {
			clear(element);
			findElement(element).sendKeys("");
			findElement(element).sendKeys(keys.trim());
			logger.info("succesfully sent the keys:: \"" + keys + "\" to the input box of element:: " + element);
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
		}
	}

	/**
	 * keys required to be entered in the input box
	 * 
	 * @param=WebElement web element of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void sendKeys(WebElement element, String keys) {
		try {
			clear(element);
			element.sendKeys("");
			element.sendKeys(keys);
			logger.info("succesfully sent the keys:: \"" + keys + "\" to the input box of element:: " + element);
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
		}
	}
	
	/**
	 * keys required to be entered in the input box
	 * 
	 * @param=WebElement web element of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void sendKeysEnter(By by, Keys keys) {
		try {
			findElement(by).sendKeys(keys);
			logger.info("succesfully sent the keys:: \"" + keys + "\" to the input box of element:: " + by);
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys to input box " + by + " due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys to input box " + by + " due to " + e.getMessage());
		}
	}

	/**
	 * returns the given attributes value for the given element
	 * 
	 * @param=By locator from which you want to get the attribute
	 * @param=String attribute name
	 * @return=String returns the value of the attribute
	 * @author Anonymous
	 */
	final public String getAttribute(By element, String attribute) {
		String value = null;
		try {
			if (isDisplayedNow(element)) {
				moveToElement(element);
			}
			logger.info("Getting the attribute :: \"" + attribute + "\" from element :: \"" + element + "\"");
			value = findElement(element).getAttribute(attribute);
			logger.info("The " + attribute + " value of element :: \"" + element + "\" is " + value);
		} catch (Exception e) {
			logger.error("Cannot get the Attribute :: " + attribute + " value of element " + element + " due to "
					+ e.getMessage());
			fail("Cannot get the Attribute :: " + attribute + " value of element " + element + " due to "
					+ e.getMessage());
		}
		return value;
	}

	/**
	 * returns the given attributes value for the given element
	 * 
	 * @param=WebElement web element from which you want to get the attribute
	 * @param=String attribute name
	 * @return=String returns the value of the attribute
	 * @author Anonymous
	 */
	final public String getAttribute(WebElement element, String attribute) {
		String value = null;
		try {
			value = element.getAttribute(attribute);
		} catch (Exception e) {
			logger.error("Cannot get the Attribute :: " + attribute + " value of element " + element + " due to "
					+ e.getMessage());
			fail("Cannot get the Attribute :: " + attribute + " value of element " + element + " due to "
					+ e.getMessage());
		}
		return value;
	}

	/**
	 * returns the text of the given element
	 * 
	 * @param=By locator from which you want the text
	 * @return=String returns the text of that locator
	 * @author Anonymous
	 */
	final public String getText(By element) {
		String value = null;
		try {
			scroll(element);
			WaitUntilElementIsVisible(element);
			value = driver.findElement(element).getText();
			logger.info(" Text of the element :: " + element + " is ::" + value);
		} catch (StaleElementReferenceException e) {
			waitFor(1);
			value = driver.findElement(element).getText();
		} catch (Exception e) {
			fail("Cannot get the text of element " + element + " due to " + e.getMessage());
		}
		return value.trim();
	}

	/**
	 * returns the text of the given element
	 * 
	 * @param=WebElement web element from which you want the text
	 * @return=String returns the text of that element
	 * @author Anonymous
	 */
	final public String getText(WebElement element) {
		String value = null;
		try {
			scroll(element);
			value = element.getText();
		} catch (Exception e) {
			fail("Cannot get the text of element " + element + " due to " + e.toString());
		}
		return value.trim();
	}

	/**
	 * check whether element is selected or not
	 * 
	 * @param=By locator of the check box
	 * @return=true if check box is selected
	 * @return=false if check box is not selected
	 * @author Anonymous
	 */
	final public boolean isSelected(By element) {
		WaitUntilElementIsPresent(element);
		boolean status = false;
		try {
			status = findElement(element).isSelected();
			logger.info("Status of the checkbox of element ::" + element + " is:: " + status);
		} catch (Exception e) {
			logger.error("Unable to get the status for the element:: " + element + " due to " + e.getMessage());
		}
		return status;
	}

	/**
	 * check whether element is selected or not
	 * 
	 * @param=WebElement web element of the check box
	 * @return=true if check box is selected
	 * @return=false if check box is not selected
	 * @author Anonymous
	 */
	final public boolean isSelected(WebElement element) {
		boolean status = false;
		try {
			status = element.isSelected();
			logger.info("Status of the checkbox of element ::" + element + " is:: " + status);
		} catch (Exception e) {
			logger.error("Unable to get the status for the element:: " + element + " due to " + e.getMessage());
		}
		return status;
	}

	/**
	 * wait for particular amount of time
	 * 
	 * @param=float seconds you want to wait for
	 * @author Anonymous
	 */
	final public void waitFor(float timeToWait) {
		try {
			Thread.sleep((long) (timeToWait * 1000));
		} catch (InterruptedException e) {
		}
	}

	/**
	 * clicks on given element using action class
	 * 
	 * @param=By element locator you want to click using action class
	 * @author Anonymous
	 */
	final public void actionClick(By element) {
		Actions act = new Actions(driver);
		try {
			WaitUntilElementIsClickable(element);
			scroll(element);
			act.moveToElement(findElement(element)).click().build().perform();
			logger.info("successfully clicked on element " + element);
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * clicks on given element using action class
	 * 
	 * @param=WebElement element locator you want to click using action class
	 * @author Anonymous
	 */
	final public void actionClick(WebElement element) {
		Actions act = new Actions(driver);
		try {
			waituntilWebElementIsvisible(element);
			scroll(element);
			act.moveToElement(element).click().build().perform();
			logger.info("successfully clicked on element " + element);
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * double clicks on given element using action class
	 * 
	 * @param=By element locator you want to double click using action class
	 * @author Anonymous
	 */
	final public void doubleClick(By element) {
		Actions act = new Actions(driver);
		try {
			WaitUntilElementIsClickable(element);
			scroll(element);
			act.moveToElement(findElement(element)).doubleClick().build().perform();
			logger.info("successfully double clicked on element " + element);
		} catch (Exception e) {
			logger.error("cannot double click on element :: " + element + " due to " + e.getMessage());
			fail("cannot double click on element :: " + element + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * double clicks on given element using action class
	 * 
	 * @param=WebElement element locator you want to double click using action
	 *                   class
	 * @author Anonymous
	 */
	final public void doubleClick(WebElement element) {
		Actions act = new Actions(driver);
		try {
			waituntilWebElementIsvisible(element);
			scroll(element);
			act.moveToElement(element).doubleClick().build().perform();
			logger.info("successfully double clicked on element " + element);
		} catch (Exception e) {
			logger.error("cannot double click on element :: " + element + " due to " + e.getMessage());
			fail("cannot double click on element :: " + element + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * mouse hover to given element using action class
	 * 
	 * @param=By element locator you want to mouse hover to using action class
	 * @author Anonymous
	 */
	final public void moveToElement(By element) {
		Actions act = new Actions(driver);
		try {
			scroll(element);
			act.moveToElement(findElement(element)).build().perform();
			logger.info("successfully mouse hovered to element " + element);
		} catch (StaleElementReferenceException e) {
			try {
				act.moveToElement(findElement(element)).build().perform();
			} catch (Exception e1) {
				logger.error(element + " cannot be mouse hovered due to " + e.getMessage());
				fail(element + " cannot be mouse hovered due to " + e.getMessage());
			}
		} catch (Exception e) {
			logger.error(element + " cannot be mouse hovered due to " + e.getMessage());
			fail(element + " cannot be mouse hovered due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * mouse hover to given element using action class
	 * 
	 * @param=WebElement element locator you want to mouse hover to using action
	 *                   class
	 * @author Anonymous
	 */
	final public void moveToElement(WebElement element) {
		Actions act = new Actions(driver);
		try {
			scroll(element);
			act.moveToElement(element).build().perform();
			logger.info("successfully mouse hovered on element " + element);
		} catch (Exception e) {
			logger.error(element + " cannot be mouse hovered due to " + e.getMessage());
			fail(element + " cannot be mouse hovered due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * drag and drop an element from one place to another
	 * 
	 * @param=By element locator you want to drag from using action class
	 * @param=By element locator you want to drop to using action class
	 * @author Anonymous
	 */
	final public void dragAndDrop(By fromElement, By toElement) {
		Actions act = new Actions(driver);
		try {
			WaitUntilElementIsVisible(fromElement);
			WaitUntilElementIsPresent(toElement);
			scroll(fromElement);
			act.dragAndDrop(findElement(fromElement), findElement(toElement)).build().perform();
			logger.info("successfully dragged from element " + fromElement + " to element " + toElement);
		} catch (Exception e) {
			logger.error("unable to perform drag and drop operation due to " + e.getMessage());
			fail("unable to perform drag and drop operation due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * drag and drop an element from one place to another
	 * 
	 * @param=WebElement web element you want to drag from using action class
	 * @param=WebElement web element you want to drop to using action class
	 * @author Anonymous
	 */
	final public void dragAndDrop(WebElement fromElement, WebElement toElement) {
		Actions act = new Actions(driver);
		try {
			waituntilWebElementIsvisible(fromElement);
			scroll(fromElement);
			act.dragAndDrop(fromElement, toElement).build().perform();
			logger.info("successfully dragged from element " + fromElement + " to element " + toElement);
		} catch (Exception e) {
			logger.error("unable to perform drag and drop operation due to " + e.getMessage());
			fail("unable to perform drag and drop operation due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * keys required to be entered in the input box using action class
	 * 
	 * @param=By locator of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void actionSendKeys(By element, String keys) {
		Actions act = new Actions(driver);
		try {
			act.moveToElement(driver.findElement(element)).sendKeys(keys).build().perform();
			logger.info("succesfully sent the keys:: " + keys + "to the input box of element:: " + element);
			act.release();
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	final public void actionSendKeys(String keys) {
		Actions act = new Actions(driver);
		try {
			act.sendKeys(keys).build().perform();
			logger.info("succesfully sent the keys:: " + keys);
			act.release();
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + " due to " + e.getMessage());
			fail("unable to send \"" + keys + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * keys required to be entered in the input box using action class
	 * 
	 * @param=WebElement web element of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void actionSendKeys(WebElement element, String keys) {
		Actions act = new Actions(driver);
		try {
			act.moveToElement(element).sendKeys(keys).build().perform();
			logger.info("succesfully sent the keys :: " + keys + " to the input box of element:: " + element);
			act.release();
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys to input box " + element + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * perform right click on the particular element
	 * 
	 * @param=WebElement web element where right click operation needs to be
	 *                   performed
	 * @author Anonymous
	 */
	final public void contextClick(WebElement element) {
		Actions act = new Actions(driver);
		try {
			act.moveToElement(element).contextClick(element).build().perform();
			waitFor(1);
			logger.info("succesfully right clicked on given element:: " + element);
		} catch (Exception e) {
			logger.error("unable to perform right click on element:: " + element + " due to " + e.getMessage());
			fail("unable to perform right click on element:: " + element + " due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * refreshes the web page where driver is having the focus now
	 * 
	 * @author Anonymous
	 */
	final public void refresh() {
		try {
			driver.navigate().refresh();
		} catch (Exception e) {
			refreshPageUsingJavaScript();
			logger.error("Cannot refresh the page due to " + e.getMessage());
		}
	}

	/**
	 * navigates the browser to given url
	 * 
	 * @param=String required url you want to navigate to
	 * @author Anonymous
	 */
	final public void get(String url) {
		try {
			driver.get(url);
			logger.info("navigated to url :: " + url);
		} catch (TimeoutException e) {
		} catch (Exception e) {
			logger.error("Unable to navigate to url :: " + url + " due to " + e.getMessage());
			fail("Unable to navigate to url :: " + url + " due to " + e.getMessage());
		}
	}

	/**
	 * clicks on given element using driver
	 * 
	 * @param=By element locator you want to click using Javascript
	 * @author Anonymous
	 */
	final public void clickUsingJavascript(By element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// WaitUntilElementIsClickable(element);
			scroll(element);
			js.executeScript("arguments[0].click();", findElement(element));
			logger.info("successfully clicked on element " + element);
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	/**
	 * clicks on given element using driver
	 * 
	 * @param=WebElement element locator you want to click using Javascript
	 * @author Anonymous
	 */
	final public void clickUsingJavascript(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			waituntilWebElementIsvisible(element);
			scroll(element);
			js.executeScript("arguments[0].click();", element);
			logger.info("successfully clicked on element " + element);
		} catch (Exception e) {
			logger.error(element + " cannot be clicked due to " + e.getMessage());
			fail(element + " cannot be clicked due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	/**
	 * runs the garbage collector and removes all objects which will referring
	 * null
	 */
	final private void gc() {
		Runtime.getRuntime().gc();
	}

	final public Object executeScript(String Script) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object obj = null;
		try {
			obj = js.executeScript(Script);
		} catch (Exception e) {
			logger.error("Cannot execute javascript :: " + Script + " due to " + e.getMessage());
			fail("Cannot execute javascript :: " + Script + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
		return obj;
	}

	final public void executeScript(String Script, WebElement Element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript(Script, Element);
		} catch (Exception e) {
			logger.error("Cannot execute javascript :: " + Script + " due to " + e.getMessage());
			fail("Cannot execute javascript :: " + Script + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	final public void deleteAllCookies() {
		try {
			driver.manage().deleteAllCookies();
		} catch (Exception e) {
			logger.error("unable to delete all cookies due to " + e.getMessage());
			fail("unable to delete all cookies due to " + e.getMessage());
		}
	}

	final public void implicitlyWait(int time) {
		try {
			driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @return=String returns the present URL */
	final public String getCurrentUrl() {
		String url = null;
		try {
			url = driver.getCurrentUrl();
		} catch (Exception e) {
		}
		return url;
	}

	/**
	 * @return=Set set of the window handles driver has access
	 */
	final public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	/** @return=String window handle */
	final public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * Switch's the driver focus to given required window
	 * 
	 * @param=String window handle of the tab you want to switch to
	 */
	final public void switchTo(String window) {
		try {
			driver.switchTo().window(window);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final public void switchTo(WebElement frame) {
		try {
			driver.switchTo().frame(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** quits the driver and closes the window */
	final public void close() {
		driver.close();
	}

	/**
	 * keys required to be entered in the input box
	 * 
	 * @param=By locator of the input box
	 * @param=String keys required to send
	 * @author Anonymous
	 */
	final public void actionSendShortcutKeys(String keys) {
		Actions act = new Actions(driver);
		try {
			act.sendKeys(keys).build().perform();
			waitFor(0.5f);
			act.release();
			logger.info("succesfully sent shortcut keys:: " + keys);
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys  due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * Open new browser tab with given URL*
	 * 
	 * @param=String URL which you want to open in new tab
	 * @author bharghav.kongara
	 */
	final public void openurlInNewTab(String url) {
		try {
			String firstWindow = getWindowHandle();
			logger.info("CurrentWindow : " + firstWindow);
			executeScript("window.open()");
			Set<String> windows = getWindowHandles();
			logger.info("Window size: " + windows.size());
			for (String window : windows) {
				if (!window.equals(firstWindow)) {
					String secondWindow = window;
					switchTo(secondWindow);
					get(url);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * closes all the browser tabs except tab given tab
	 * 
	 * @param=windowHandle window handle of the tab which you don't want to
	 *                     close
	 * @author bharghav.kongara
	 */
	final public void CloseAllTheTabsExceptRequiredTab(String MainWindow) {
		try {
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(MainWindow)) {
					switchTo(handle);
					close();
				}
			}
			switchTo(MainWindow);
		} catch (Exception e) {
		}
	}

	/**
	 * @return=String Returns the domain name of present web site
	 * @author bharghav.kongara
	 */
	final public String getDomainName() {
		String domainName = null;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			domainName = (String) js.executeScript("return document.domain");
		} catch (Exception e) {
			logger.error("getDomainName :: " + e.getMessage());
			fail("getDomainName :: " + e.getMessage());
		}
		return domainName;
	}

	/**
	 * Opens new browser tab and switches the driver focus to newly opened tab
	 * 
	 * @return=String window handle of newly opened tab
	 * @author bharghav.kongara
	 */
	final public String openNewTab() {
		String newWindowHandle = null;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			String firstWindow = getWindowHandle();
			logger.info("CurrentWindow : " + firstWindow);
			js.executeScript("window.open()");
			Set<String> windows = getWindowHandles();
			logger.info("Window size: " + windows.size());
			for (String window : windows) {
				if (!window.equals(firstWindow)) {
					String secondWindow = window;
					switchTo(secondWindow);
					newWindowHandle = driver.getWindowHandle();
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return newWindowHandle;
	}

	/**
	 * returns the size of the given element
	 * 
	 * @param=By locator of the elements you want the size
	 * @return size of the elements of the given locator
	 * @author sourav.malik
	 */
	final public int getElementCount(By element) {
		try {
			implicitlyWait(Constants.IMPLICIT_WAIT_DEFAULT);
			return driver.findElements(element).size();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			implicitlyWait(Constants.IMPLICIT_WAIT);
		}
		return 0;
	}

	/**
	 * To check whether element is enabled or not
	 * 
	 * @return=true if element is enabled
	 * @return=false if element is not enabled
	 * @param=By element locator in the form of By
	 * @author soumya.das
	 */
	final public boolean isEnabled(By element) {
		boolean flag = false;
		try {
			for (int i = 0; i < 05; i++) {
				try {
					implicitlyWait(0);
					flag = driver.findElement(element).isEnabled();
					if (flag) {
						logger.info("element is enabled");
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (NoSuchElementException e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return flag;
	}

	/**
	 * To check whether element is enabled or not
	 * 
	 * @return=true if element is enabled
	 * @return=false if element is not enabled
	 * @param=WebElement element
	 */
	final public boolean isEnabled(WebElement element) {
		boolean flag = false;
		try {
			for (int i = 0; i < 05; i++) {
				try {
					flag = element.isEnabled();
					if (flag) {
						logger.info("element is enabled");
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (NoSuchElementException e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return flag;
	}

	/**
	 * To check whether element is displayed or not
	 * 
	 * @return=true if element is displayed
	 * @return=false if element is not displayed
	 * @param=WebElement element
	 * @author Anonymous
	 */
	final public boolean isDisplayed(WebElement element) {
		boolean flag = false;
		try {
			for (int i = 0; i < 05; i++) {
				try {
					flag = element.isDisplayed();
					if (flag) {
						flag = true;
						break;
					} else {
						waitFor(1);
					}
				} catch (NoSuchElementException e) {
					waitFor(1);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return flag;
	}

	/**
	 * wait until given element is not visible
	 * 
	 * @param=WebElement element you want to wait until it becomes invisible
	 * @author Anonymous
	 */
	final public void waituntilWebElementIsInvisible(WebElement element) {
		try {
			int counter = 0;
			while (element.isDisplayed()) {
				if (counter > 180) {
					logger.error("Failed to Close the Element  " + element + " in " + counter + "secs");
					fail("Failed to Close the Element  " + element + " in " + counter + "secs");
					break;
				} else {
					counter++;
					waitFor(1);
					logger.info("Loading...!!!" + counter + "secs over");
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * match text at given location
	 * 
	 * @param=By locator where text is or will be displayed
	 * @param=Pattern pattern/text you want to wait for or match
	 * @author Anonymous
	 */
	@SuppressWarnings("unlikely-arg-type")
	final public boolean textMatches(By locator, Pattern textToMatch) {
		try {
			implicitlyWait(0);
			for (int i = 0; i < 60; i++) {
				String elementText = driver.findElement(locator).getText().trim();
				logger.info("Element text :: " + elementText + ", Text not to match :: " + textToMatch);
				if (elementText.equals(textToMatch) && !elementText.isEmpty()) {
					return true;
				}
				waitFor(1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return true;
	}

	/**
	 * match text at given location
	 * 
	 * @param=By locator where text is or will be displayed
	 * @param=Pattern pattern/text you want to wait for or match
	 * @author Anonymous
	 */
	final public boolean textDoesNotMatch(By locator, String textToMatch) {
		try {
			implicitlyWait(0);
			for (int i = 0; i < 60; i++) {
				String elementText = driver.findElement(locator).getText().trim();
				logger.info("Element text :: " + elementText + ", Text not to match :: " + textToMatch);
				if (!elementText.equals(textToMatch) && !elementText.isEmpty()) {
					return true;
				}
				waitFor(1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			implicitlyWait(60);
		}
		return false;
	}

	/**
	 * drag and drop a By element from a specific x offset to another y offset
	 * 
	 * @param=By element locator you want to drag from using action class
	 * @param=x offset
	 * @param=y offset
	 * @author soumya.das
	 */
	final public void dragAndDropBy(By Element, int xOffset, int yOffset) {
		Actions act = new Actions(driver);
		try {
			WaitUntilElementIsVisible(Element);
			scroll(Element);
			act.dragAndDropBy(findElement(Element), xOffset, yOffset).build().perform();
			logger.info(
					"successfully dragged element " + Element + " from postion " + xOffset + " to position " + yOffset);
		} catch (Exception e) {
			logger.error("unable to perform drag and dropBy operation due to " + e.getMessage());
			fail("unable to perform drag and dropBy operation due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * drag and drop a Web element from a specific x offset to another y offset
	 * 
	 * @param=By element locator you want to drag from using action class
	 * @param=x offset
	 * @param=y offset
	 * @author soumya.das
	 */
	final public void dragAndDropBy(WebElement Element, int xOffset, int yOffset) {
		Actions act = new Actions(driver);
		try {
			waituntilWebElementIsvisible(Element);
			scroll(Element);
			act.dragAndDropBy(Element, xOffset, yOffset).build().perform();
			logger.info(
					"successfully dragged element " + Element + " from postion " + xOffset + " to position " + yOffset);
		} catch (Exception e) {
			logger.error("unable to perform drag and dropBy operation due to " + e.getMessage());
			fail("unable to perform drag and dropBy operation due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * keys required to be entered in the input box
	 * 
	 * @param=WebElement of the input box
	 * @param=String keys required to send
	 * @author soumya.das
	 */
	final public void actionSendShortcutKeys(WebElement element, Keys keys) {
		Actions act = new Actions(driver);
		try {
			act.moveToElement(element).keyDown(keys).click().keyUp(keys).build().perform();
			waitFor(0.5f);
			act.release();
			logger.info("succesfully sent shortcut keys:: " + keys);
		} catch (Exception e) {
			logger.error("unable to send \"" + keys + "\" keys due to " + e.getMessage());
			fail("unable to send \"" + keys + "\" keys  due to " + e.getMessage());
		} finally {
			act = null;
			gc();
		}
	}

	/**
	 * scroll to the required element in web page using BY element
	 * 
	 * @param=By element you want to scroll to
	 * @author Anonymous
	 */
	final public void scrollIntoView(By element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			WaitUntilElementIsPresent(element);
			js.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(element));
		} catch (Exception e) {
			logger.error("Failed to scroll to " + element + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	/**
	 * scroll to the given web element
	 * 
	 * @param=WebElement web element you want to scroll to
	 * @author Anonymous
	 */
	final public void scrollIntoView(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("arguments[0].scrollIntoView(true)", element);
		} catch (Exception e) {
			logger.error("Failed to scroll to " + element + " due to " + e.getMessage());
		} finally {
			js = null;
			gc();
		}
	}

	final public void refreshPageUsingJavaScript() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("window.location.reload();");
		} catch (Exception e) {
		} finally {
			js = null;
			gc();
		}
	}

	final public void pageLoadTimeout(int time) {
		if (!(driver instanceof FirefoxDriver)) {
			driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
		}
	}

	/**
	 * return's the name of the present running test case
	 * 
	 * @return the name of the test case which is running
	 * 
	 * @author bharghav.kongara
	 */
	final private String getPresentRunningTestName() {
		String testName = null;
		StackTraceElement[] stack = new Exception().getStackTrace();
		for (int i = 0; i < stack.length; i++) {
			if (stack[i].getMethodName().contains("invoke0")) {
				testName = stack[i - 1].getMethodName();
				break;
			}
		}
		return testName;
	}

	/**
	 * Highlight's the given element in <B>cyan</B> color.
	 * 
	 * @param element
	 */
	private void highlightAnElement(By element) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3.5px solid cyan'",
					driver.findElement(element));
		} catch (Exception e) {
		}
	}

	/**
	 * Highlight's the given element in <B>cyan</B> color.
	 * 
	 * @param element
	 */
	private void highlightAnElement(WebElement element) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3.5px solid cyan'", element);
		} catch (Exception e) {
		}
	}

	/**
	 * unhighlight's the given element
	 * 
	 * @param element
	 */
	private void unHighlightAnElement(By element) {
		try {
			if (getElementCount(element) > 0)
				((JavascriptExecutor) driver).executeScript("arguments[0].style.border=''",
						driver.findElement(element));
		} catch (Exception e) {
		}
	}

	/**
	 * unhighlight's the given element
	 * 
	 * @param element
	 */
	private void unHighlightAnElement(WebElement element) {
		try {
			if (element.isDisplayed())
				((JavascriptExecutor) driver).executeScript("arguments[0].style.border=''", element);
		} catch (Exception e) {
		}
	}

	/**
	 * set's the attribute value of the given element </br>
	 * <b style='color:red'><i> Use it carefully </br>
	 * not recommended </b ></i>
	 * 
	 * @param element
	 * @param attribute
	 * @param value
	 */
	public void setAttributeUsingJavaScript(By element, String attribute, String value) {
		try {
			if (getElementCount(element) > 0)
				((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
						driver.findElement(element), attribute, value);
		} catch (Exception e) {
		}
	}

	/**
	 * return's the css value for the given attribute for given element
	 * 
	 * @param locator
	 * @param attribute
	 * @return
	 * @author bharghav.kongara
	 */
	public String getCSSValue(By locator, String attribute) {
		try {
			return findElement(locator).getCssValue(attribute).trim();
		} catch (Exception e) {
			logger.error("Unable to get the css value for the given locator :: \"" + locator + "\" and attribute :: \""
					+ attribute + "\"");
			return "";
		}
	}

	/**
	 * return's the css value for the given attribute for given element
	 * 
	 * @param locator
	 * @param attribute
	 * @return
	 * @author bharghav.kongara
	 */
	public String getCSSValue(WebElement locator, String attribute) {
		try {
			return locator.getCssValue(attribute).trim();
		} catch (Exception e) {
			logger.error("Unable to get the css value for the given locator :: \"" + locator + "\" and attribute :: \""
					+ attribute + "\"");
			return "";
		}
	}

	/**
	 * @param locator
	 * @return
	 * @author bharghav.kongara
	 */
	public boolean isPresentNow(By locator) {
		try {
			implicitlyWait(0);
			driver.findElement(locator);
			implicitlyWait(60);
			return true;
		} catch (Exception e) {
			implicitlyWait(60);
			return false;
		}
	}

	/**
	 * @param locator
	 * @return
	 * @author bharghav.kongara
	 */
	public boolean isDisplayedNow(By locator) {
		try {
			implicitlyWait(0);
			boolean status = driver.findElement(locator).isDisplayed();
			implicitlyWait(60);
			if (status)
				return true;
		} catch (Exception e) {
			implicitlyWait(60);
			return false;
		}
		return false;
	}

	@SuppressWarnings("finally")
	public void skip(String skipMessage) {
		try {
			Listener.extentTest.get().log(Status.SKIP,
					"<b><i>Skip message is :: </b></i><textarea>" + skipMessage + "</textarea>");
			// Reporter.log("Skip message is :: " + skipMessage);
		} catch (Exception e) {
		} finally {
			logger.error(skipMessage, new Exception());
			throw new SkipException(skipMessage);
		}

	}
	
	public void scrollToBottom() {
		try {
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
		} catch (Exception e) {
			logger.error("Cannot be scrolled due to " + e.getMessage());
		}
	}
}
