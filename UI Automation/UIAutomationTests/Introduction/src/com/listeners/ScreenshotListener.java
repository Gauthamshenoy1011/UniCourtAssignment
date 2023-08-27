package com.listeners;


import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.utils.Constants;
import com.utils.DriverInitialisation;
import com.utils.seleniumUtils;

public class ScreenshotListener implements ITestListener {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private static ScreenRecorder screenRecorder;
	public String filename;
	private static ExtentHtmlReporter htmlreporter = new ExtentHtmlReporter(Constants.EXTENT_REPORT_FILE);
	private static ExtentHtmlReporter moduleWiseHTMLReporter;
	private ExtentReports extent = new ExtentReports();
	private ExtentReports moduleWiseExtent;
	public static ExtentTest logger;
	public static ExtentTest moduleWiseLogger;
	private String suiteName;
	private String methodParameters;
	public static int passed, failed, skipped;
	//Date startTime, endTime;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	
	@Override
	public void onTestFailure(ITestResult result) {
		try {
			WebDriver driver = null;
			Object testobject = result.getInstance();
			Class variablesOfTest = result.getTestClass().getRealClass();
			try {
				driver = (WebDriver)variablesOfTest.getDeclaredField("driver").get(testobject);
				} catch (Exception e) {
				e.printStackTrace();
			}
			//String videoFileName = "";
			//endTime = new Date();
			//List<File> files = stopRecording();
			String testName = seperateStringInCamelCase(result.getName());
			//videoFileName = (testName + methodParameters).contains("[Lcom") ? testName : (testName + methodParameters);
			failed++;
			/*for (File fil : files) {
				if (fil.exists()) {
					File destination = new File(Constants.FAILED_TEST_VIDEOS + File.separator + videoFileName + ".avi");
					if (destination.exists()) {
						destination.delete();
					}
					fil.renameTo(destination);
					log.info(testName + " has failed and video file is saved as " + destination);
				}
			}*/
			//convertToMp4(videoFileName);
			//String fileForURL = videoFileName.replace(" ", "%20").trim();
			//log.info("fileForURL:" + fileForURL);
			/*ScreenshotListener.logger.log(Status.INFO,
					"<a href=" + Constants.MP4_VIDEI_DESTINATIONLINK + File.separator
							+ DriverInitialisation.failedTCMP4VideoFolder + File.separator + fileForURL + ".mp4"
							+ " target=\"_blank\">click here to see video</a>");
			ScreenshotListener.extentTest.get().log(Status.INFO,
					"<a href=" + Constants.MP4_VIDEI_DESTINATIONLINK + File.separator
							+ DriverInitialisation.failedTCMP4VideoFolder + File.separator + fileForURL + ".mp4"
							+ " target=\"_blank\">click here to see video</a>");*/
			log.error("\n\n " + testName + methodParameters + " test has failed \n\n");
			logger.log(Status.FAIL, testName + " failed");
			extentTest.get().log(Status.FAIL, testName + " failed");
			try {
				extentTest.get().addScreenCaptureFromPath(seleniumUtils.getScreenshotPath(result.getMethod().getMethodName(),driver),result.getMethod().getMethodName());
				} catch (Exception e) {
				e.printStackTrace();
			}
			Throwable throwable = result.getThrowable();
            String failureMessage = throwable.getMessage();
            extentTest.get().log(Status.FAIL,
					"<b><i>Failure message is :: </b></i><textarea>" + failureMessage + "</textarea>");
			extentTest.get().log(Status.INFO, "Total time taken for test case to execute :: "
					+ ((result.getEndMillis() - result.getStartMillis()) / 1000) + " Seconds");
			extent.flush();
			moduleWiseExtent.flush();
		} catch (Exception e) {
			log.error("After test failure things are getting failed due to  :: " + e.getMessage());
		}
	}
;
	/*
	 * private String takeScreenShot(String className) { String folderPath =
	 * System.getProperty("user.dir")+"/target/FailedScreenShots"; String filePath;
	 * String timeStamp = new
	 * SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime
	 * ()); filePath = folderPath + className + " " + timeStamp + ".png"; new
	 * File(folderPath).mkdirs(); // Insure directory is there File scrFile =
	 * ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); try {
	 * FileUtils.copyFile(scrFile, new File(filePath)); } catch (IOException e) {
	 * e.printStackTrace(); } log.info(" Placed screen shot in " + filePath); return
	 * filePath; }
	 */
	public void onFinish(ITestContext context) {
		log.info("----------------------------------------------------------------------------------------\n");
		log.info("****************************************************************************************\n");
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = seperateStringInCamelCase(result.getName());
		//String suiteName = seperateStringInCamelCase(result.getTestContext().getName());
		Object[] params = result.getParameters();
		methodParameters = "";
		if (params.length > 0) {
			methodParameters += " ( ";
			for (int i = 0; i < params.length; i++) {
				if (i == 0) {
					methodParameters += params[i].toString();
				} else {
					methodParameters += " , " + params[i].toString();
				}
			}
			methodParameters += " ) ";
			methodParameters = methodParameters.replace("*", "");
		}
		log.info("\n\n" + "<< --- TestCase START --->> " + testName + methodParameters + "\n");
		logger = extent.createTest(testName + methodParameters);
		logger.assignCategory(suiteName);
		logger.log(Status.INFO, "<b><i>Test Case Name :: </b></i>\"" + testName + "\"");
		logger.log(Status.INFO,
				"<b><i>Description of the test :: </b></i> \"" + result.getMethod().getDescription() + "\"");
		moduleWiseLogger = moduleWiseExtent.createTest(result.getTestClass().getName().split("\\.")[4] + " - " + testName + methodParameters);
		extentTest.set(moduleWiseLogger);
		extentTest.get().assignCategory(suiteName);
		extentTest.get().log(Status.INFO, "<b><i>Test Case Name :: </b></i>\"" + testName + "\"");
		extentTest.get().log(Status.INFO,
				"<b><i>Description of the test :: </b></i> \"" + result.getMethod().getDescription() + "\"");
		//startTime = new Date();
		//startRecording();
		filename = testName;
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		passed++;
		//endTime = new Date();
		String testName = seperateStringInCamelCase(result.getName());
		List<File> files = null;
		log.info("\n\n TestCase: " + testName + methodParameters + ": --->>> PASS \n");
		logger.log(Status.PASS, testName + methodParameters + " test has passed");
		extentTest.get().log(Status.INFO, "Total time taken for test case to execute :: "
				+ ((result.getEndMillis() - result.getStartMillis()) / 1000) + " Seconds");
		extentTest.get().log(Status.PASS, testName + methodParameters + " test has passed");
		extent.flush();
		moduleWiseExtent.flush();
		//files = stopRecording();
		/*for (File fil : files) {
			if (fil.exists()) {
				fil.delete();
				log.info("onTestSuccess file deleted");
			}
		}*/
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		skipped++;
		String testName = seperateStringInCamelCase(result.getName());
		List<File> files = null;
		logger.log(Status.INFO,
				"<b><i>Description of the test :: </b></i>\"" + result.getMethod().getDescription() + "\"");
		logger.log(Status.SKIP, testName + methodParameters + " test has Skipped");
		extentTest.get().log(Status.INFO,
				"<b><i>Description of the test :: </b></i>\"" + result.getMethod().getDescription() + "\"");
		extentTest.get().log(Status.SKIP, testName + methodParameters + " test has Skipped");
		log.info("\n\n TestCase: " + testName + methodParameters + ": --->>> SKIPPED");
		//files = stopRecording();
		/*for (File fil : files) {
			if (fil.exists()) {
				fil.delete();
				log.info("file deleted");
			}
		}*/
		extent.flush();
		moduleWiseExtent.flush();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		log.info("\n\n TestCase: " + result.getName() + ": --->>> FAILED With percentage");
	}

	@Override
	public void onStart(ITestContext context) {
		log.info("****************************************************************************************");
		log.info("                                " + context.getName() + "       ");
		log.info("----------------------------------------------------------------------------------------");
		File extentReports = new File(Constants.EXTENT_REPORT_PATH);
		if (!extentReports.exists()) {
			extentReports.mkdirs();
		}
		extent.attachReporter(htmlreporter);
		htmlreporter.config().setDocumentTitle("Automation Report");
		htmlreporter.config().setAutoCreateRelativePathMedia(true);
		htmlreporter.config().setReportName("Automation Report");
		htmlreporter.config().setTheme(Theme.DARK);
		suiteName = seperateStringInCamelCase(context.getName());
		moduleWiseHTMLReporter = null;
		moduleWiseExtent = null;
		moduleWiseExtent = new ExtentReports();
		moduleWiseHTMLReporter = new ExtentHtmlReporter(
				Constants.EXTENT_REPORT_PATH + File.separator + "Automation Report.html");
		moduleWiseExtent.attachReporter(moduleWiseHTMLReporter);
		moduleWiseHTMLReporter.config().setDocumentTitle("Automation Report");
		moduleWiseHTMLReporter.config().setAutoCreateRelativePathMedia(true);
		moduleWiseHTMLReporter.config().setReportName("Automation Report");
		moduleWiseHTMLReporter.config().setTheme(Theme.STANDARD);

		passed = 0;
		failed = 0;
		skipped = 0;
	}

	

	public String seperateStringInCamelCase(String string) {
		try {
			return capitalizeTheFirstWord(
					StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(string), " ").trim());
		} catch (Exception e) {
			return string;
		}
	}

	public String capitalizeTheFirstWord(String name) {
		String s1 = name.substring(0, 1).toUpperCase();
		String nameCapitalized = s1 + name.substring(1);
		return nameCapitalized;
	}


}