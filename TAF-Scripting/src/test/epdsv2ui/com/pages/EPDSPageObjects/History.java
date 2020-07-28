package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class History {

	public static Logger LOGGER = Logger.getLogger(History.class);

	WebDriver driver;

	public History(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
