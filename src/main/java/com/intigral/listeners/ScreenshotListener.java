package com.intigral.listeners;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.intigral.utils.Constants;


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
	Date startTime, endTime;
	public static String testName = "";

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			String videoFileName = "";
			endTime = new Date();
			failed++;
			List<File> files = null;
			String testName = seperateStringInCamelCase(result.getName());
			log.error("\n\n " + testName + methodParameters + " test has failed \n\n");
			logger.log(Status.FAIL, testName + " failed");
			moduleWiseLogger.log(Status.FAIL, testName + " failed");
			logger.log(Status.INFO, "Total time taken for test case to execute :: "
					+ ((endTime.getTime() - startTime.getTime()) / 1000) + " Seconds");
			moduleWiseLogger.log(Status.INFO, "Total time taken for test case to execute :: "
					+ ((endTime.getTime() - startTime.getTime()) / 1000) + " Seconds");
			files = stopRecording();
			videoFileName = (testName + methodParameters).contains("[Lcom") ? testName : (testName + methodParameters);
			for (File fil : files) {
				if (fil.exists()) {
					File destination = new File(Constants.FAILED_TEST_VIDEOS + File.separator + videoFileName + ".avi");
					if (destination.exists()) {
						destination.delete();
					}
					fil.renameTo(destination);
					log.info(testName + " has failed and video file is saved as " + destination);
				}
			}
			extent.flush();
			moduleWiseExtent.flush();
		} catch (Exception e) {
			log.error("After test failure things are getting failed due to  :: " + e.getMessage());
		} finally {
			logger = null;
		}
	}
	
	@Override
	public void onTestStart(ITestResult result) {
		String testName = seperateStringInCamelCase(result.getName());
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
		moduleWiseLogger = moduleWiseExtent.createTest(testName + methodParameters);
		moduleWiseLogger.assignCategory(suiteName);
		moduleWiseLogger.log(Status.INFO, "<b><i>Test Case Name :: </b></i>\"" + testName + "\"");
		moduleWiseLogger.log(Status.INFO,
				"<b><i>Description of the test :: </b></i> \"" + result.getMethod().getDescription() + "\"");
		startTime = new Date();
		startRecording();
		filename = testName;
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			passed++;
			endTime = new Date();
			String testName = seperateStringInCamelCase(result.getName());
			List<File> files = null;
			log.info("\n\n TestCase: " + testName + methodParameters + ": --->>> PASS \n");
			logger.log(Status.INFO, "Total time taken for test case to execute :: "
					+ ((endTime.getTime() - startTime.getTime()) / 1000) + " Seconds");
			logger.log(Status.PASS, testName + methodParameters + " test has passed");
			moduleWiseLogger.log(Status.INFO, "Total time taken for test case to execute :: "
					+ ((endTime.getTime() - startTime.getTime()) / 1000) + " Seconds");
			moduleWiseLogger.log(Status.PASS, testName + methodParameters + " test has passed");
			extent.flush();
			moduleWiseExtent.flush();
			files = stopRecording();
			for (File fil : files) {
				if (fil.exists()) {
					fil.delete();
					log.info("onTestSuccess file deleted");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger = null;
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		try {
		skipped++;
		String testName = seperateStringInCamelCase(result.getName())+ methodParameters;
		if(logger == null) {
			logger = extent.createTest(testName);
		}
		List<File> files = null;
		logger.log(Status.INFO,
				"<b><i>Description of the test :: </b></i>\"" + result.getMethod().getDescription() + "\"");
		logger.log(Status.SKIP, testName + " test has Skipped");
		moduleWiseLogger.log(Status.INFO,
				"<b><i>Description of the test :: </b></i>\"" + result.getMethod().getDescription() + "\"");
		moduleWiseLogger.log(Status.SKIP, testName + " test has Skipped");
		log.info("\n\n TestCase: " + testName + ": --->>> SKIPPED");
		files = stopRecording();
		for (File fil : files) {
			if (fil.exists()) {
				fil.delete();
				log.info("file deleted");
			}
		}
		extent.flush();
		moduleWiseExtent.flush();
		} finally {
			logger = null;
		}
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
				Constants.EXTENT_REPORT_PATH + File.separator + suiteName + " Report.html");
		moduleWiseExtent.attachReporter(moduleWiseHTMLReporter);
		moduleWiseHTMLReporter.config().setDocumentTitle(suiteName + " Automation Report");
		moduleWiseHTMLReporter.config().setAutoCreateRelativePathMedia(true);
		moduleWiseHTMLReporter.config().setReportName(suiteName + " Automation Report");
		moduleWiseHTMLReporter.config().setTheme(Theme.STANDARD);

		passed = 0;
		failed = 0;
		skipped = 0;
	}

	private void startRecording() {
		try {
			File file = new File(Constants.FAILED_TEST_VIDEOS);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;
			Rectangle captureSize = new Rectangle(0, 0, width, height);
			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();
			screenRecorder = new ScreenRecorder(gc, captureSize,
					new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
							CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
							Rational.valueOf(5), QualityKey, 1.0f, KeyFrameIntervalKey, 5 * 60),
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "blue", FrameRateKey, Rational.valueOf(5)),
					null, file);
			screenRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<File> stopRecording() {
		try {
			screenRecorder.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screenRecorder.getCreatedMovieFiles();
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

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
}