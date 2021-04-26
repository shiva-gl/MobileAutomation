package com.intigral.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

import com.aventstack.extentreports.Status;
import com.intigral.listeners.ScreenshotListener;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class CommonaPage {
	static AndroidDriver<AndroidElement> driver;
	private static String failedScreenShotsFolderPath = System.getProperty("user.dir") + "/target/FailedScreenShots/";

	public CommonaPage(AndroidDriver<AndroidElement> driver) {
		this.driver = driver;
	}

	public void scroll(String text) {
		driver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\""
						+ text + "\"))");

	}

	public void scroll(int times) {
		driver.findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true)).scrollForward(" + times + ")");

	}

	public static void click(AndroidElement element) {
		try {
			element.click();
		} catch (StaleElementReferenceException e) {
			element.click();
		} catch (Exception e) {

			fail(element + " cannot be clicked due to " + e.getMessage());
		}
	}

	public static String getPresentRunningTestName() {
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

	public static void fail(String failureMessage) {
		try {
			String filePath;
			String methodName = getPresentRunningTestName();
			ScreenshotListener.logger.log(Status.FAIL,
					"<b><i>Failure message is :: </b></i><textarea>" + failureMessage + "</textarea>");
			ScreenshotListener.moduleWiseLogger.log(Status.FAIL,
					"<b><i>Failure message is :: </b></i><textarea>" + failureMessage + "</textarea>");
			String scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			ScreenshotListener.logger.log(Status.INFO,
					"<img height=\"195\" width=\"195\" src='data:image/png;charset=utf-8;base64," + scrFile1
							+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"> Mouse Hover Here For Screenshot </img>"
							+ "<script> function bigImg(x) { x.style.height = \"500px\"; x.style.width = \"750px\";}  function normalImg(x)"
							+ " { x.style.height = \"195px\";  x.style.width = \"195px\";}</script>");
			ScreenshotListener.moduleWiseLogger.log(Status.INFO,
					"<img height=\"195\" width=\"195\" src='data:image/png;charset=utf-8;base64," + scrFile1
							+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"> Mouse Hover Here For Screenshot </img>"
							+ "<script> function bigImg(x) { x.style.height = \"500px\"; x.style.width = \"750px\";}  function normalImg(x)"
							+ " { x.style.height = \"195px\";  x.style.width = \"195px\";}</script>");
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			filePath = failedScreenShotsFolderPath + methodName + " " + timeStamp + ".png";
			new File(failedScreenShotsFolderPath).mkdirs(); // Ensure directory
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filePath));
			Reporter.log("Failure message is :: " + failureMessage);
			Reporter.log("<tbody><tr><td><a href='../FailedScreenShots/" + methodName + " " + timeStamp + ".png"
					+ "' title=\"Failure reference screenshot\">Screenshot</a><br></br><img height=\"42\" width=\"42\" src='data:image/png;charset=utf-8;base64,"
					+ scrFile1
					+ "'  onmouseover=\"bigImg(this)\" onmouseout=\"normalImg(this)\"><strong>Mouse Hover Here For Screenshot</strong></img>"
					+ "<script> function bigImg(x) { x.style.height = \"450px\"; x.style.width = \"1200px\";}  function normalImg(x) { x.style.height = \"20px\";  x.style.width = \"20px\";}</script>"
					+ "</td></tr>" + "<tr><td><a href='../FailedTestCaseVideos/" + methodName
					+ ".mp4' title=\"Failure reference video\">Video</a></td></tr></tbody>");
		} catch (Exception e) {
		} finally {
			org.testng.Assert.fail(failureMessage);
		}
	}
}
