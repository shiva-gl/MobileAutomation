package com.intigral.support;

import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class Browserstack {
	
	Dotenv dotenv;
	Map<String, String> caps;
	
	public Browserstack() {
		
		dotenv = Dotenv.load();
		
	}
	public Map<String, String> androidCaps() {
		caps = new HashMap<>();
		caps.put("project", dotenv.get("project"));
		caps.put("build", dotenv.get("build"));
		caps.put("browserstack.debug", "true");
		caps.put("app", dotenv.get("browser_stack_app_id"));
		caps.put("device", dotenv.get("android_device"));
		caps.put("os_version", dotenv.get("android_os_version"));
		return caps;
	}
	
	public void iosCaps() {
		// TODO
	}

}
