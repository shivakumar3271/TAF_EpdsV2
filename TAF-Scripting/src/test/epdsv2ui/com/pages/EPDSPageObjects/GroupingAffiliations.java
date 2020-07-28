package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class GroupingAffiliations {

	public static Logger LOGGER = Logger.getLogger(GroupingAffiliations.class);

	WebDriver driver;

	public GroupingAffiliations(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
