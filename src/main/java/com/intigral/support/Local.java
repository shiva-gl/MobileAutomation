package com.intigral.support;

import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class Local {
	Dotenv dotenv;
	Map<String, String> caps;
	
	public Local() {
		
		dotenv = Dotenv.load();
		
	}
	public Map<String, String> androidCaps() {
		caps = new HashMap<>();
		caps.put("deviceName", "Simulator");
		caps.put("platformName", "android");
		caps.put("automationName", "Appium");
		caps.put("appPackage", dotenv.get("APP_PACKAGE"));
		caps.put("appActivity", dotenv.get("APP_ACTIVITY"));
		caps.put("app", dotenv.get("local_app"));
		caps.put("uiautomator2ServerInstallTimeout", "10000");
		return caps;
	}
	
	
	public void iosCaps() {
		// TODO
	}
}