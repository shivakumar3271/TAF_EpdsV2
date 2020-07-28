package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.web.WebHandlers;

public class LegacyInfo {

	public static Logger LOGGER = Logger.getLogger(LegacyInfo.class);

	WebDriver driver;

	@FindBy(xpath = "//input[@name='legacyInfoform:legacyValues_editableInput']")
	WebElement legacyFieldsSelect;

	@FindBy(xpath = "//*[@class='ui-selectonemenu-items ui-selectonemenu-list ui-widget-content ui-widget ui-corner-all ui-helper-reset']")
	WebElement legacyDropdownliValues;

	@FindBy(xpath = "//span[@id='legacyInfoform:cntrctInd']")
	WebElement legacyContractInd;

	@FindBy(xpath = "//span[@id='legacyInfoform:nwmInd']")
	WebElement nwmIndicater;

	public LegacyInfo(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void selectLegacyFieldDropdown() throws InterruptedException {
		WebHandlers.click(legacyFieldsSelect);
		String dropDownValue = "QCARE";
		WebHandlers.selectDropdownValue(legacyDropdownliValues, dropDownValue);
	}

	public String legacyContractIndicator() {
		return legacyContractInd.getText();
	}

	public String nwmInd() {
		return nwmIndicater.getText();
	}
}
