package com.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Listener implements ITestListener {
	private final String failedScreenShotsFolderPath = System.getProperty("user.dir") + "/target/FailedScreenShots/";
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	ExtentReports extent;
	public static ThreadLocal<ExtentReports> extentReport = new ThreadLocal<ExtentReports>();
	ExtentTest test;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	private String suiteName;

	@Override
	public void onTestStart(ITestResult result) {
		String testCaseID = "", testName = seperateStringInCamelCase(result.getName());
		String description = result.getMethod().getDescription();
		if (description != null && description.contains(":"))
			testCaseID = description.split(":", 2)[0] + " :- ";
		test = extentReport.get().createTest(testCaseID + testName);
		extentTest.set(test);
		extentTest.get().assignCategory(suiteName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = seperateStringInCamelCase(result.getName());
		log.info("\n\n TestCase: " + testName + ": --->>> PASS \n");
		extentTest.get().log(Status.INFO, "Total time taken for test case to execute :: "
				+ ((result.getEndMillis() - result.getStartMillis()) / 1000) + " Seconds");
		extentTest.get().log(Status.PASS, "Successful");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		WebDriver driver = null;
		Dimension size = null;
		extentTest.get().fail(result.getThrowable());
		Object testObject = result.getInstance();
		Class realClass = result.getTestClass().getRealClass().getSuperclass();
		try {
			driver = (WebDriver) realClass.getDeclaredField("driver").get(testObject);
			size = (Dimension) realClass.getDeclaredField("size").get(testObject);
			if (size.getWidth() > driver.manage().window().getSize().getWidth()) {
				driver = (WebDriver) result.getTestClass().getRealClass().getDeclaredField("driverForVerifyingSignalR")
						.get(testObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			extentTest.get().addScreenCaptureFromPath(getScreenShortPath(result.getMethod().getMethodName(), driver),
					result.getMethod().getMethodName());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testCaseID = "", testName = seperateStringInCamelCase(result.getName());
		String description = result.getMethod().getDescription();
		if (description != null && description.contains(":"))
			testCaseID = description.split(":", 2)[0] + " :- ";
		test = extentReport.get().createTest(testCaseID + testName);
		extentTest.set(test);
		extentTest.get().log(Status.INFO, "<b><i>Description of the test :: </b></i>\"" + description + "\"");
		extentTest.get().log(Status.SKIP, testName + " test has Skipped");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {
		suiteName = context.getName();
		System.out.println("context.getName()===\t" + context.getName());
		extentReport.set(new ExtentReports());
		extentReport.get().attachReporter(extracted(context.getName()));
	}

	@Override
	public void onFinish(ITestContext context) {
		extentReport.get().flush();
	}

	private String getScreenShortPath(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String path = failedScreenShotsFolderPath + testCaseName + " " + timeStamp + ".png";
		File destPath = new File(path);
		FileUtils.copyFile(source, destPath);
		System.out.println(path);
		return path;
	}

	private ExtentSparkReporter extracted(String suiteName) {
		System.out.println("suitName " + suiteName);
		ExtentSparkReporter reporter = new ExtentSparkReporter(
				System.getProperty("user.dir") + File.separator + "target" + File.separator
						+ "Automation ModuleWise Reports" + File.separator + suiteName + " Report.html");
		reporter.config().setReportName("Web Automation Result");
		reporter.config().setDocumentTitle(suiteName + " Automation Report");
		reporter.config().setReportName(suiteName + " Automation Report");
		reporter.config().setTheme(Theme.STANDARD);
		return reporter;
	}

	private String seperateStringInCamelCase(String string) {
		try {
			return StringUtils
					.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(string), " ").trim());
		} catch (Exception e) {
			return string;
		}
	}
}
