package com.pages.EPDSPageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.tests.epds.RpaTestBaseClass;
import com.utilities.epds.EpdsConstants;

public class ProviderSearch extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(ProviderSearch.class);

	WebDriver driver;

	@FindBy(xpath = "//a[contains(text(),'Individual Search')]")
	WebElement individualTab;

	@FindBy(xpath = "//input[@id='tabz:ind_search-form:eid']")
	WebElement indEIdTxt;

	@FindBy(xpath = "//button[@id='tabz:ind_search-form:searchBtn']//span[@class='ui-button-text']")
	WebElement indSearchButton;

	@FindBy(xpath = "//*[@id='formSearch1:search-result:dataTableForInd_data']/tr/td[2]/div/div/a[@class='ui-commandlink' or @class='ui-commandlink inactive']")
	WebElement indProvName;

	@FindBy(xpath = "//a[contains(text(),'Organization Search')]")
	WebElement organizationTab;

	@FindBy(xpath = "//input[@id='tabz:orgSearchForm:eid']")
	WebElement orgEIdTxt;

	@FindBy(xpath = "//button[@id='tabz:orgSearchForm:orgSrchBtn']//span[@class='ui-button-text'][contains(text(),'Search')]")
	WebElement orgSearchBtn;

	@FindBy(xpath = "//*[@id='formSearch1:search-result:dataTableForOrg_data']/tr/td[2]/div/div/a[@class='ui-commandlink inactive' or @class='ui-commandlink']")
	WebElement orgProvName;

	@FindBy(xpath = "//a[contains(text(),'Grouping Search')]")
	WebElement groupingTab;

	@FindBy(xpath = "//input[@id='tabz:provGroupSearchForm:groupingCode']")
	WebElement grpCodeTxt;

	@FindBy(xpath = "//*[@id=\"tabz:provGroupSearchForm:groupingName\"]")
	WebElement grpName;

	@FindBy(xpath = "//button[@id='tabz:provGroupSearchForm:provGrpSrchBtn']")
	WebElement grpSearchBtn;

	@FindBy(xpath = "//*[@id=\"formSearch1:search-result:dataTableForQcareGrp_data\"]")
	WebElement groupingTable;

	@FindBy(xpath = "//*[@id=\"formSearch1:search-result:dataTableForQcareGrp:globalFilter\"]")
	WebElement groupingFilter;

	@FindBy(xpath = "//*[@id='formSearch1:search-result:dataTableForQcareGrp_paginator_bottom']/span[4]/span[1]")
	WebElement groupingPaginationFirst;

	@FindBy(xpath = "//*[@id='formSearch1:search-result:dataTableForQcareGrp_paginator_bottom']/span[5]")
	WebElement groupingPovPaginatorStatus;

	@FindBy(xpath = "//*[@id='formSearch1:search-result:dataTableForQcareGrp_paginator_bottom']/span[5]/span")
	WebElement groupingPaginatorIcon;

	String searchTableRow = "//table[@role='grid']/tbody[@id ='formSearch1:search-result:dataTableForQcareGrp_data']/tr[@data-ri='<<rowindex>>']";

	public ProviderSearch(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean searchAndSelectIndividual(String enterpriseId) {
		WebHandlers.click(individualTab);
		WebHandlers.enterText(indEIdTxt, enterpriseId);
		WebHandlers.click(indSearchButton);
		Boolean iselementpresent = driver
				.findElements(By.xpath("//*[@id='tabz:ind_search-form:ind-input-errors']/div/ul/li/span")).size() != 0;
		if (iselementpresent == true) {
			LOGGER.info("Invalid Individual provider ...");
		} else if (indProvName.isDisplayed()) {
			WebHandlers.click(indProvName);
			return true;
		}
		return false;
	}

	public boolean searchAndSelectOrganization(String enterpriseId) {
		WebHandlers.click(organizationTab);
		WebHandlers.enterText(orgEIdTxt, enterpriseId);
		WebHandlers.click(orgSearchBtn);
		Boolean iselementpresent = driver
				.findElements(By.xpath("//*[@id='tabz:orgSearchForm:org-input-errors']/div/ul/li/span")).size() != 0;
		if (iselementpresent == true) {
			LOGGER.info("Invalid Organizational provider ...");
		} else if (orgProvName.isDisplayed()) {
			WebHandlers.click(orgProvName);
			return true;
		}
		return false;
	}

	public boolean searchAndSelectGrouping(String groupingName, String groupingCode, String groupingType)
			throws InterruptedException {

		WebHandlers.click(groupingTab);
		WebHandlers.enterText(grpCodeTxt, groupingCode);
		WebHandlers.click(grpSearchBtn);
		String strPaginationDefaultStatus = "ui-corner-all";
		Boolean iselementpresent = driver
				.findElements(By.xpath("//*[@id='tabz:provGroupSearchForm:prvGrp-input-errors']/div/ul/li/span"))
				.size() != 0;
		if (iselementpresent == true) {
			LOGGER.info("Invalid Grouping provider ...");
			return false;
		}
		WebWaitHelper.waitForElement(groupingFilter);
		WebHandlers.enterText(groupingFilter, groupingCode);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		while (!strPaginationDefaultStatus.contains("disabled")) {
			String srchStr = WebHandlers.getTblTdVal(groupingTable, 0, 0);
			if (!srchStr.equalsIgnoreCase("No records found.")) {
				int rowCount = WebHandlers.getTblRowCount(groupingTable);
				LOGGER.info("Grouping table row count : " + rowCount);
				WebHandlers.click(groupingPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(groupingTable, i)) {
						srchStr = WebHandlers.getTblTdVal(groupingTable, i, EpdsConstants.PROFILE_GROUPING_NAME);
						if (srchStr.equalsIgnoreCase(groupingName)) {
							srchStr = WebHandlers.getTblTdVal(groupingTable, i, EpdsConstants.PROFILE_GROUPING_CODE);
							if (srchStr.equalsIgnoreCase(groupingCode)) {
								srchStr = WebHandlers.getTblTdVal(groupingTable, i,
										EpdsConstants.PROFILE_GROUPING_TYPE);
								if (srchStr.equalsIgnoreCase(groupingType)) {
									WebHandlers.clickGroupTblValue(groupingTable, i,
											EpdsConstants.PROFILE_GROUPING_NAME);
									return true;
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = groupingPovPaginatorStatus.getAttribute("class");
				WebHandlers.click(groupingPaginatorIcon);
			}
			LOGGER.info("Could not find a matching Grouping record.");
			return false;
		}

		return false;
	}

	public boolean searchAndSelectGroupingByGrpName(String grpNameValue, String grpTypeValue)
			throws InterruptedException {

		WebHandlers.click(groupingTab);
		WebHandlers.enterText(grpName, grpNameValue);
		WebHandlers.click(grpSearchBtn);
		Boolean iselementpresent = driver
				.findElements(By.xpath("//*[@id='tabz:provGroupSearchForm:prvGrp-input-errors']/div/ul/li/span"))
				.size() != 0;
		if (iselementpresent == true) {
			LOGGER.info("Invalid Grouping provider ...");
			return false;
		} else {
			WebWaitHelper.waitForElement(groupingFilter);
			WebHandlers.enterText(groupingFilter, grpTypeValue);
			try {
				Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String srchStr = WebHandlers.getTblTdVal(groupingTable, 0, 0);

			if (!srchStr.equalsIgnoreCase("No records found.")) {
				int rowCount = WebHandlers.getTblRowCount(groupingTable);
				LOGGER.info("Grouping table row count : " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					if (!this.isGreyColorGrid(i)) {
						srchStr = WebHandlers.getTblTdVal(groupingTable, i, EpdsConstants.PROFILE_GROUPING_NAME);
						if (srchStr.equalsIgnoreCase(grpNameValue)) {
							srchStr = WebHandlers.getTblTdVal(groupingTable, i, EpdsConstants.PROFILE_GROUPING_TYPE);
							if (srchStr.equalsIgnoreCase(grpTypeValue)) {
								WebHandlers.clickGroupTblValue(groupingTable, i, EpdsConstants.PROFILE_GROUPING_NAME);
								return true;
							}
						}
					}
				}
			}
		}
		return false;

	}

	public boolean isGreyColorGrid(int rowIndex) {
		String dynamicRowXpath = searchTableRow.replace("<<rowindex>>", "" + rowIndex);
		WebElement row = WebHandlers.getElement(driver, By.xpath(dynamicRowXpath));
		String colorClass = row.getAttribute("class");
		if (colorClass.contains("colored")) {
			return true;
		} else {
			return false;
		}

	}

}
