package com.intigral.utils;

import java.io.File;

public class Constants {
	// Set your Project Folder here
	public static final String PROJECT_HOME = System.getProperty("user.dir");

	public static final String FAILED_TEST_VIDEOS = PROJECT_HOME + File.separator + "target" + File.separator
			+ "FailedTestCaseVideos";
	
	public static final String EXTENT_REPORT_PATH = PROJECT_HOME + File.separator + "target" + File.separator
			+ "Automation ModuleWise Reports";
	
	public static final String EXTENT_REPORT_FILE = PROJECT_HOME + File.separator + "target" + File.separator
			+ "AutomationReport.html";
}