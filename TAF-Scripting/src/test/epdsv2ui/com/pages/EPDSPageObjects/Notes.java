package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class Notes {

	public static Logger LOGGER = Logger.getLogger(Notes.class);

	WebDriver driver;

	public Notes(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
