package com.intigral.utils;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import com.intigral.support.Browserstack;
import com.intigral.support.Local;

import io.appium.java_client.android.AndroidDriver;
import io.github.cdimascio.dotenv.Dotenv;

public class DriverInit {

	
	public AndroidDriver driverinvoke() {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		@SuppressWarnings("rawtypes")
		AndroidDriver  driver = null;
		Dotenv dotenv = Dotenv.load();
		String  url;
		Map<String, String> caps = dotenv.get("EXECUTION_TYPE").equals("browser_stack")
				? new Browserstack().androidCaps()
				: new Local().androidCaps();

		Iterator<Entry<String, String>> it = caps.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (capabilities.getCapability(pair.getKey().toString()) == null) {
				capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
			}
		}


		url = dotenv.get("EXECUTION_TYPE").equals("browser_stack")
				? "http://" + dotenv.get("browser_stack_user") + ":" + dotenv.get("browser_stack_key") + "@hub-cloud.browserstack.com/wd/hub"
				: "http://127.0.0.1:4723/wd/hub";

		try {
			driver = new AndroidDriver(new URL(url), capabilities);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		} catch (Exception e) {
			System.out.println(e);
		}

		return driver;
	}
}
