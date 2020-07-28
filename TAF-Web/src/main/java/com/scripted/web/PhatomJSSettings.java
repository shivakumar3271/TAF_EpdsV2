package com.scripted.web;

import java.io.File;

import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhatomJSSettings {
	private DesiredCapabilities phantomCapabilities = null;
	
	public DesiredCapabilities setByPhatomJSSettings(File fileName) {
		 phantomCapabilities = new DesiredCapabilities();
		 phantomCapabilities.setJavascriptEnabled(true);
		 phantomCapabilities.setCapability("takesScreenshot", true);
		 phantomCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					WebDriverPathUtil.getPhatomJSDriverPath());
		 return phantomCapabilities;
	}
	
	
	

	
}
