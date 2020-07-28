package com.pages.EPDSPageObjects;

import java.awt.AWTException;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.Specialty;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;

public class ReimbusermentNetworks {

	public static Logger LOGGER = Logger.getLogger(ReimbusermentNetworks.class);

	WebDriver driver;

	@FindBy(xpath = "//a[@id='prvInfo-form:dtble_qcareRmbList:0:netID']")
	WebElement netwkId;

	@FindBy(xpath = "//input[@id='prvInfo-form:dataTable_relatedAff:globalFilter']")
	WebElement affilSrchbox;

	@FindBy(xpath = "//tbody[@id='prvInfo-form:dtble_rmbDisplay_data']//td[1]")
	WebElement associatedReimbIdLink;

	@FindBy(xpath = "//input[@id='prvInfo-form:dataTable_relatedAff:globalFilter']")
	WebElement searchAffiTxtBox;

	/****** Final list *****************/

	/*** Individual ****/
	@FindBy(xpath = "//input[@id='prvInfo-form:dataTable_address:globalFilter']")
	WebElement addrSrchbox;

	@FindBy(xpath = "//table/tbody[@id='prvInfo-form:dataTable_address_data']")
	WebElement avlAddrTableBody;

	@FindBy(xpath = "//*[@id='prvInfo-form:relProviderPanel_end']/tbody/tr/td/div/div[2]/table/tbody/tr/td/div/div/table/tbody")
	WebElement affiAddrTableBody;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_qcareRmbList']/table/tbody")
	WebElement indNetworkTableBody;

	@FindBy(xpath = "//input[@id='prvInfo-form:dtble_qcareRmbList:filterForRmbNet']")
	WebElement searchIndNetIdTxtBox;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:editNetCancel\"]/span")
	WebElement indCancelBtn;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[8]/td[4]")
	WebElement indTimlyfiling;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[8]/td[2]")
	WebElement indAcceptPatientsIndicator;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[5]/td[2]/label")
	WebElement indNetwkTermDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[2]/td[2]")
	WebElement indNetwkId;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[8]/td[3]")
	WebElement indPARInd;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[5]/td[1]/label")
	WebElement indNetwkEffDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[5]/td[3]")
	WebElement indNetTermReasonCode;

	@FindBy(xpath = "//*[@id='prvInfo-form:qcareNeworkManagePanel']/table/tbody/tr[3]/td/table/tbody/tr[8]/td[1]")
	WebElement indNetDirIndicator;

	@FindBy(xpath = "//*[@id='indReimbDiv']/span/table/tbody/tr[9]/td/table/tbody/tr/td[1]/button/span")
	WebElement indNetwrkcancelBtn;

	@FindBy(xpath = "//table[@id='prvInfo-form:rmbDetails_add1']/tbody/tr[2]/td[2]/table/tbody/tr/td/label")
	WebElement indReimbValue;

	@FindBy(xpath = "//table[@id='prvInfo-form:rmbDetails_add1']/tbody/tr[8]/td[1]/table/tbody/tr/td[1]/label")
	WebElement indReimbEffDate;

	@FindBy(xpath = "//table[@id='prvInfo-form:rmbDetails_add1']/tbody/tr[8]/td[2]/table/tbody/tr/td[1]/label")
	WebElement indReimbTermDate;

	@FindBy(xpath = "//table[@id='prvInfo-form:rmbDetails_add1']/tbody/tr[8]/td[3]/label")
	WebElement indReimbTermReasonCode;

	@FindBy(xpath = "//*[@id='prvInfo-form:cancelQcareRmbButton1']/span")
	WebElement indReimbCancelBtn;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[8]/td/div/table/tbody")
	WebElement indAssociateReimbTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[3]/td/table/tbody/tr[16]/td[1]")
	WebElement ageFromTo;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[3]/td/table/tbody/tr[16]/td[2]")
	WebElement indPatientGender;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[3]/td/table/tbody/tr[16]/td[4]")
	WebElement indMemberCapacity;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:reimbursementQcareSourceSystem1\"]")
	WebElement indReimSourceValue;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[4]/td/table/tbody/tr/td/div/table/tbody")
	WebElement indSpecialityTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:indMainReimbDiv']/tbody/tr/td/div/span/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]")
	WebElement indNetwrkSourceSystem;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:j_idt4583\"]/tbody/tr[2]/td[1]")
	WebElement indNetSourceSys;

	public ReimbusermentNetworks(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void indNetwrkClickCancel() {
		WebHandlers.click(indNetwrkcancelBtn);
	}

	public String getIndNetwrkId() {
		return (indNetwkId.getText());
	}

	public String getIndPARInd() {
		return (indPARInd.getText());
	}

	public String getIndNetwrkEffDate() {
		return (indNetwkEffDate.getText());
	}

	public String getIndNetwrkTermDate() {
		return (indNetwkTermDate.getText());
	}

	public String getIndNetwrkTermReasonCode() {
		return (indNetTermReasonCode.getText());
	}

	public String getIndNetDirIndicator() {
		return (indNetDirIndicator.getText());
	}

	public String getIndTimlyFiling() {
		return (indTimlyfiling.getText());
	}

	public String getIndAcceptPatientsIndicator() {
		return (indAcceptPatientsIndicator.getText());
	}

	public void indClickCancel() {
		WebHandlers.click(indCancelBtn);
	}

	public String getIndReimbValue() {
		return (indReimbValue.getText());
	}

	public String getIndReimbEffDate() {
		return (indReimbEffDate.getText());
	}

	public String getIndReimbTermDate() {
		return (indReimbTermDate.getText());
	}

	public String getIndReimbTermReasonCode() {
		return (indReimbTermReasonCode.getText());
	}

	public void indReimbClickCancel() {
		WebHandlers.click(indReimbCancelBtn);
	}

	public String getIndAgeFrom() {

		String age = ageFromTo.getText();
		int firstIndex = age.indexOf('-');
		String ageFromTxt = age.substring(0, firstIndex).trim();
		return ageFromTxt;
	}

	public String getIndAgeTo() {

		String age = ageFromTo.getText();
		int length = age.length();
		int firstIndex = age.indexOf('-');
		String ageToTxt = age.substring(firstIndex + 1, length).trim();
		return ageToTxt;
	}

	public String getIndPatientGender() {
		return (indPatientGender.getText());
	}

	public String getIndMemberCapacity() {
		return (indMemberCapacity.getText());
	}

	public String getIndNetwrkSourceSystem() {
		return (indNetwrkSourceSystem.getText());
	}

	public String getindNetSourceSys() {
		return (indNetSourceSys.getText());
	}

	public String getindReimSourceValue() {
		return (indReimSourceValue.getText());
	}

	/*** Organization ****/

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_rmbList']/table/tbody")
	WebElement orgNetworkTableBody;

	@FindBy(xpath = "//input[@id='prvInfo-form:dtble_rmbList:globalFilter']")
	WebElement orgNetwkSearchBox;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[1]/label")
	WebElement orgRembSourceSystem;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/label")
	WebElement orgRembValue;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[3]/td/table/tbody/tr[2]/td[2]/label")
	WebElement orgRembEffDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[3]/td/table/tbody/tr[2]/td[3]/label")
	WebElement orgRembTerminationDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[3]/td/table/tbody/tr[2]/td[4]/label")
	WebElement orgRembTerminationCode;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[1]/tbody/tr[2]/td[1]/label")
	WebElement orgNetwrkSourceSystem;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[1]/tbody/tr[2]/td[5]/label")
	WebElement orgNetwrkEffDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[2]/tbody/tr[2]/td[1]/label")
	WebElement orgNetwrkTermDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[2]/tbody/tr[2]/td[2]/label")
	WebElement orgNetwrkTermCode;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[1]/tbody/tr[2]/td[2]/label")
	WebElement orgNetwrkId;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[4]/tbody/tr[2]/td[3]/label")
	WebElement orgDirectoryIndicator;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[4]/tbody/tr[2]/td[1]/label")
	WebElement orgAcceptingPatientsIndicator;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[4]/tbody/tr[2]/td[2]/label")
	WebElement orgPARIndicator;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[1]/td[2]/table[4]/tbody/tr[2]/td[4]/label")
	WebElement orgTimlyFiling;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[1]/label")
	WebElement orgAgeFrom;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[2]/label")
	WebElement orgAgeTo;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[3]/label")
	WebElement orgPatGender;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNetworkDetails']/tbody/tr/td/table[1]/tbody/tr[3]/td[2]/table/tbody/tr[2]/td[2]/label")
	WebElement orgMemCapacity;

	@FindBy(xpath = "//*[@id='prvInfo-form:netViewPanel_content']/table/tbody/tr/td/table[3]/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div/table/thead/tr/th/span/span/input")
	WebElement specialtySrchbox;

	@FindBy(xpath = "//*[@id='prvInfo-form:netViewPanel_content']/table/tbody/tr/td/table[3]/tbody/tr/td[2]/div/div/table/tbody/tr/td[2]/div/table/tbody")
	WebElement specialtyDetailsTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:closeViewButton']/span")
	WebElement orgNetworkDetailsCloseBtn;

	@FindBy(xpath = "//label[contains(text(),'View Network')]")
	WebElement orgViewNetworkActive;

	@FindBy(xpath = "//*[@id='prvInfo-form:orgMainRmbPanel']/tbody/tr/td/div/span/div/div/a")
	WebElement orgSearchMaxMinBtn;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewRmbActive' or @id='prvInfo-form:viewRmbInActive']/label")
	WebElement orgViewReimb;

	@FindBy(xpath = "//*[@id='prvInfo-form:rmbViewPanel_content']/table/tbody/tr/td/table[1]/tbody/tr/td[2]/div/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/label")
	WebElement orgReimbursementValue;

	public String getOrgReimbursementSource() {
		return orgRembSourceSystem.getText();
	}

	public String getOrgReimbursementValue() {
		return orgRembValue.getText();
	}

	public String getOrgReimbursementEffdate() {
		return orgRembEffDate.getText();
	}

	public String getOrgReimbursementTermDate() {
		return (orgRembTerminationDate.getText());
	}

	public String getOrgReimbursementTermCode() {
		return (orgRembTerminationCode.getText());
	}

	public void navToOrgViewNetworkTab() {
		WebHandlers.click(orgViewNetworkActive);
	}

	public void orgNetworkDetailsClose() {
		WebHandlers.click(orgNetworkDetailsCloseBtn);
	}

	public String getOrgNetwrkSourceSystem() {
		return orgNetwrkSourceSystem.getText();
	}

	public String getOrgNetwrkEffDate() {
		return (orgNetwrkEffDate.getText());
	}

	public String getOrgNetwkTermdate() {
		return orgNetwrkTermDate.getText();
	}

	public String getOrgNetwrkTermCode() {
		return (orgNetwrkTermCode.getText());
	}

	public String getOrgNetId() {
		return (orgNetwrkId.getText());
	}

	public String getOrgDirIndicator() {
		return (orgDirectoryIndicator.getText());
	}

	public String getOrgAcceptingPatientsIndicator() {
		return (orgAcceptingPatientsIndicator.getText());
	}

	public String getOrgPARIndicator() {
		return (orgPARIndicator.getText());
	}

	public String getOrgTimlyFiling() {
		return (orgTimlyFiling.getText());
	}

	public String getOrgAgeFrom() {
		return (orgAgeFrom.getText());
	}

	public String getOrgAgeTo() {
		return (orgAgeTo.getText());
	}

	public String getOrgPatGender() {
		return (orgPatGender.getText());
	}

	public String getOrgMemCapacity() {
		return (orgMemCapacity.getText());
	}

	public void navOrgViewReimursementTab() {
		WebHandlers.click(orgViewReimb);
	}

	public String ReimbursementValue() {
		return orgReimbursementValue.getText();
	}

	// Added for handling pagination

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_rmbList_paginator_bottom']/select")
	WebElement orgNetwrkRemDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_rmbList_paginator_bottom']/span[4]/span[1]")
	WebElement orgNetwrkRemPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_rmbList_paginator_bottom']/span[5]")
	WebElement orgNetwrkRemPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_rmbList_paginator_bottom']/span[5]/span")
	WebElement orgNetwrkRemPaginatorIcon;

	@FindBy(xpath = "//label[contains(text(),'View Reimbursement')]")
	WebElement orgReimbursementTabText;

	// selectAvailableAddrCheckbox
	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/select")
	WebElement addressDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[4]/span[1]")
	WebElement addrPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[5]")
	WebElement addrPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[5]/span")
	WebElement addrPaginatorIcon;

	// selectAffiliationAddrCheckbox
	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_relatedAff_paginator_bottom']/span[4]/span[1]")
	WebElement affiAddrPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_relatedAff_paginator_bottom']/span[5]")
	WebElement affiAddrPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_relatedAff_paginator_bottom']/span[5]/span")
	WebElement affiAddrPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_relatedAff_paginator_bottom']/select")
	WebElement affiAddrDropDown;

	// filterAndSelectIndNetId
	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_qcareRmbList_paginator_bottom']/span[4]/span[1]")
	WebElement indNetwkPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_qcareRmbList_paginator_bottom']/span[5]")
	WebElement indNetwrkPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_qcareRmbList_paginator_bottom']/span[5]/span")
	WebElement indNetwrkPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_qcareRmbList_paginator_bottom']/select")
	WebElement indNetwrkDropDown;

	public void reimbursementTab() {
		WebHandlers.click(orgReimbursementTabText);
	}

	public boolean selectAvailableAddrCheckbox(Address address) throws InterruptedException {
		String addressLine1 = address.getAddressDetails().getAddressLine1();
		String city = address.getAddressDetails().getCity();
		String state = address.getAddressDetails().getState();
		String zipCode = address.getAddressDetails().getZip();
		String effDate = address.getAddressActive().getEffectiveDate();
		String termDate = address.getAddressActive().getTerminationDate();
		String expAddress = ((addressLine1 + " " + city + " " + state + " " + zipCode).replaceAll("\\s+", ""))
				.toUpperCase();
		String actData = null;
		String strPaginationDefaultStatus = "ui-corner-all";

		WebWaitHelper.waitForElement(addrSrchbox);

		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript(
				"document.getElementById('prvInfo-form:dataTable_address:globalFilter').value='" + addressLine1 + "'");
		addrSrchbox.sendKeys(Keys.TAB);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		Select dropdown = new Select(addressDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		actData = WebHandlers.getTblTdVal(avlAddrTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(actData.equalsIgnoreCase("No records found."))) {
				WebHandlers.click(addrPaginationFirst);
				int rowCount = WebHandlers.getTblRowCount(avlAddrTableBody);
				LOGGER.info("Address Details table row count after filtering : " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					WebWaitHelper.waitForElementPresence(avlAddrTableBody);
					actData = ((WebHandlers.getTblTdVal(avlAddrTableBody, i,
							EpdsConstants.AVALABLE_ADDR_TABLE_ADDR_COL)).replaceAll("\\s+", "")).toUpperCase();
					// actData = actData.replaceAll("\\s+", "");
					// actData = actData.toUpperCase();
					Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
					if (actData.contains(expAddress) & actData.contains(effDate) & actData.contains(termDate)) {
						WebHandlers.selectTblCelChkbox(avlAddrTableBody, i,
								EpdsConstants.AVALABLE_ADDR_TABLE_CHECKBOX_COL);
						Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
						return true;
					}
				}
				strPaginationDefaultStatus = addrPaginatorStatus.getAttribute("class");
				WebHandlers.click(addrPaginatorIcon);
			}
			LOGGER.info("No Matching Address records found");
			return false;
		}
		return false;
	}

	public boolean selectAffiliationAddrCheckbox(Affiliation affiliation) throws InterruptedException {

		String providerEid = affiliation.getAffiliatedLegacyID();
		String effDate = affiliation.getAffiliationActive().getEffectiveDate();
		String termDate = affiliation.getAffiliationActive().getTerminationDate();
		String actData = null;
		String strPaginationDefaultStatus = "ui-corner-all";

		WebWaitHelper.waitForElement(searchAffiTxtBox);

		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript("document.getElementById('prvInfo-form:dataTable_relatedAff:globalFilter').value='"
				+ providerEid + "'");
		addrSrchbox.sendKeys(Keys.TAB);
		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);

		Select dropdown = new Select(affiAddrDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		actData = WebHandlers.getTblTdVal(affiAddrTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(actData.equalsIgnoreCase("No records found."))) {
				WebHandlers.click(affiAddrPaginationFirst);
				int rowCount = WebHandlers.getTblRowCount(affiAddrTableBody);
				LOGGER.info("Address Details table row count after filtering : " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					WebWaitHelper.waitForElementPresence(affiAddrTableBody);
					actData = WebHandlers.getTblTdVal(affiAddrTableBody, i,
							EpdsConstants.AFFILITION_ADDR_TABLE_ADDR_COL);
					if (actData.contains(effDate) & actData.contains(termDate)) {
						WebHandlers.selectTblCelChkbox(affiAddrTableBody, i,
								EpdsConstants.AFFI_ADDR_TABLE_CHECKBOX_COL);
						Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
						return true;
					}
				}
				strPaginationDefaultStatus = affiAddrPaginatorStatus.getAttribute("class");
				WebHandlers.click(affiAddrPaginatorIcon);
			}
			LOGGER.info("No Matching Affiliation records found");
			return false;
		}
		return false;
	}

	public boolean filterAndSelectIndNetId(NetworkAffiliation networkAffiliation) throws InterruptedException {
		String expNetId = networkAffiliation.getNetworkID();
		String expEffDate = networkAffiliation.getNetworkActive().getEffectiveDate();
		String expTermDate = networkAffiliation.getNetworkActive().getTerminationDate();
		String strPaginationDefaultStatus = "ui-corner-all";
		String actualData = null;

		WebWaitHelper.waitForElementPresence(searchIndNetIdTxtBox);
		WebWaitHelper.waitForElementPresence(indNetworkTableBody);

		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);// change 4 to 10
		searchIndNetIdTxtBox.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		Actions actions = new Actions(driver);
		actions.moveToElement(searchIndNetIdTxtBox).click().perform();

		searchIndNetIdTxtBox.sendKeys(expNetId);
		searchIndNetIdTxtBox.sendKeys(Keys.TAB);
		searchIndNetIdTxtBox.sendKeys(Keys.ENTER);

		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

		actualData = WebHandlers.getTblTdVal(indNetworkTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(actualData.equalsIgnoreCase("No records found."))) {
				actions.moveToElement(indNetwkPaginationFirst).click().perform();
				int rowCount = WebHandlers.getTblRowCount(indNetworkTableBody);
				LOGGER.info("Address Details table row count after filtering : " + rowCount);
				WebWaitHelper.waitForElementPresence(indNetworkTableBody);

				for (int i = 0; i < rowCount; i++) {
					actualData = WebHandlers.getTblTdVal(indNetworkTableBody, i, EpdsConstants.NETWORK_TABLE_NETID_COL);
					if (actualData.equalsIgnoreCase(expNetId)) {
						actualData = WebHandlers.getTblTdVal(indNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_EFFDATE_COL);
						if (actualData.contains(expEffDate)) {
							actualData = WebHandlers.getTblTdVal(indNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_TERMDATE_COL);
							if (actualData.contains(expTermDate)) {
								WebHandlers.clickTblLinkValue(indNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_NETID_COL);
								return true;
							}
						}
					}
				}
				strPaginationDefaultStatus = indNetwrkPaginatorStatus.getAttribute("class");
				actions.moveToElement(indNetwkPaginationFirst).click().perform();
			}
			LOGGER.info("No Matching networkId found.");
			return false;
		}
		return false;
	}

	public boolean valIndAssociatedReimbTable(Reimbursement reimbursement) {
		String expReimbValue = reimbursement.getReimbursementValue();
		String expReimbEffDate = reimbursement.getReimbursementActive().getEffectiveDate();
		String expReimTermDate = reimbursement.getReimbursementActive().getTerminationDate();
		String expData = "";

		WebHandlers.scrollToElement(indAssociateReimbTable);
		WebWaitHelper.waitForElementPresence(indAssociateReimbTable);
		int rowCount = WebHandlers.getTblRowCount(indAssociateReimbTable);

		for (int i = 0; i < rowCount; i++) {
			expData = WebHandlers.getTblTdVal(indAssociateReimbTable, i,
					EpdsConstants.REIMBNETWK_ASSOCIATE_TABLE_RMBID_VALUE);
			if (expData.equalsIgnoreCase(expReimbValue)) {
				expData = WebHandlers.getTblTdVal(indAssociateReimbTable, i,
						EpdsConstants.REIMBNETWK_ASSOCIATE_TABLE_RMB_EFFDATE);
				if (expData.equals(expReimbEffDate)) {
					expData = WebHandlers.getTblTdVal(indAssociateReimbTable, i,
							EpdsConstants.REIMBNETWK_ASSOCIATE_TABLE_RMB_TERMDATE);
					if (expData.equals(expReimTermDate)) {
						WebHandlers.clickTblLinkValue(indAssociateReimbTable, i,
								EpdsConstants.REIMBNETWK_ASSOCIATE_TABLE_RMBID_VALUE);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean searchSpeciality(Specialty specialty) throws InterruptedException {

		String acctualData = null;
		String spltyCode = specialty.getSpecialtyCode();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebWaitHelper.waitForElement(specialtySrchbox);

		specialtySrchbox.click();
		specialtySrchbox.clear();

		WebHandlers.enterText(specialtySrchbox, spltyCode);

		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebWaitHelper.waitForElement(specialtyDetailsTable);
		int rowCount = WebHandlers.getTblRowCount(specialtyDetailsTable);
		System.out.println("Specialty table row count after filtering : " + rowCount);
		String rowsNull = "No records found.";
		WebWaitHelper.waitForElement(specialtyDetailsTable);
		acctualData = WebHandlers.getTblTdVal(specialtyDetailsTable, 0, 0);
		if (!(acctualData.equalsIgnoreCase(rowsNull))) {
			for (int i = 0; i < rowCount; i++) {
				String srchStr = null;
				WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
				srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i, EpdsConstants.NETWORK_TABLE_ORG_SPECIALITY);
				System.out.println("The search string value :" + srchStr);
				if (srchStr.equalsIgnoreCase(spltyCode)) {
					return true;
				}
			}
			LOGGER.info("Matching Speciality record is not found.");
			return false;
		}
		return false;
	}

	public boolean filterAndSelectReimbIdWithNetID(Address netwrkAddr, Reimbursement reimbursement)
			throws InterruptedException, AWTException {

		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "ui-corner-all";
		String expNetId = reimbursement.getNetworkID();
		String expRemVal = reimbursement.getReimbursementValue();
		String expEffDate = reimbursement.getReimbursementActive().getEffectiveDate();
		String expTermDate = reimbursement.getReimbursementActive().getTerminationDate();
		String acctualData = null;
		String addrLine1 = netwrkAddr.getAddressDetails().getAddressLine1();
		String srchAddress = addrLine1 + " " + netwrkAddr.getAddressDetails().getCity() + " "
				+ netwrkAddr.getAddressDetails().getState() + " " + netwrkAddr.getAddressDetails().getZip();

		WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
		WebWaitHelper.waitForElementPresence(orgNetwkSearchBox);

		// WebHandlers.searchText(orgNetwkSearchBox, expRemVal);
		orgNetwkSearchBox.clear();
		orgNetwkSearchBox.click();
		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript(
				"document.getElementById('prvInfo-form:dtble_rmbList:globalFilter').value='" + expRemVal + "'");
		orgNetwkSearchBox.sendKeys(Keys.TAB);
		orgNetwkSearchBox.sendKeys(Keys.ENTER);

		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		// orgNetwkSearchBox.sendKeys(Keys.TAB);

		dropdown = new Select(orgNetwrkRemDropDown);
		selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);
		int rowCount = WebHandlers.getTblRowCount(orgNetworkTableBody);
		System.out.println(rowCount);
		acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(acctualData.equalsIgnoreCase("No records found."))) {
				WebHandlers.click(orgNetwrkRemPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
							EpdsConstants.NETWORK_TABLE_ORG_NETPHYADDRS_COL);
					if (acctualData.substring(0, acctualData.length() - 3).equalsIgnoreCase(srchAddress)) {
						acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
						if (acctualData.equalsIgnoreCase(expNetId)) {
							acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_ORG_REMEFFDATE_COL);
							if (acctualData.equals(expEffDate)) {
								acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_ORG_REMTERDATE_COL);
								if (acctualData.equals(expTermDate)) {
									acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
											EpdsConstants.NETWORK_TABLE_ORG_REMBIDVALUE_COL);
									if (acctualData.contains(expRemVal)) {
										WebHandlers.clickTblLinkValue(orgNetworkTableBody, i,
												EpdsConstants.NETWORK_TABLE_ORG_REMBIDTYPE_COL);
										return true;
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = orgNetwrkRemPaginatorStatus.getAttribute("class");
				WebHandlers.click(orgNetwrkRemPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		LOGGER.info("Matching network record is not available.");
		return false;
	}

	public boolean filterAndSelectOrgNetId(Address netwrkAddr, NetworkAffiliation networkAff)
			throws InterruptedException, AWTException {
		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "ui-corner-all";
		String expNetId = networkAff.getNetworkID();
		String expEffDate = networkAff.getNetworkActive().getEffectiveDate();
		String expTermDate = networkAff.getNetworkActive().getTerminationDate();
		String acctualData = null;
		String addrLine1 = netwrkAddr.getAddressDetails().getAddressLine1();
		String srchAddress = addrLine1 + " " + netwrkAddr.getAddressDetails().getCity() + " "
				+ netwrkAddr.getAddressDetails().getState() + " " + netwrkAddr.getAddressDetails().getZip();
		WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
		WebWaitHelper.waitForElementPresence(orgNetwkSearchBox);

		orgNetwkSearchBox.clear();
		orgNetwkSearchBox.click();
		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript(
				"document.getElementById('prvInfo-form:dtble_rmbList:globalFilter').value='" + expNetId + "'");
		orgNetwkSearchBox.sendKeys(Keys.TAB);
		orgNetwkSearchBox.sendKeys(Keys.ENTER);

		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		dropdown = new Select(orgNetwrkRemDropDown);
		selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);
		int rowCount = WebHandlers.getTblRowCount(orgNetworkTableBody);
		acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(acctualData.equalsIgnoreCase("No records found."))) {
				WebHandlers.click(orgNetwrkRemPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
							EpdsConstants.NETWORK_TABLE_ORG_NETPHYADDRS_COL);
					if (acctualData.substring(0, acctualData.length() - 3).equalsIgnoreCase(srchAddress)) {
						acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
						if (acctualData.equalsIgnoreCase(expNetId)) {
							acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_ORG_NETEFFDATE_COL);
							if (acctualData.equals(expEffDate)) {
								acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_ORG_NETTERDATE_COL);
								if (acctualData.equals(expTermDate)) {
									WebHandlers.clickTblLinkValue(orgNetworkTableBody, i,
											EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
									return true;
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = orgNetwrkRemPaginatorStatus.getAttribute("class");
				WebHandlers.click(orgNetwrkRemPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		return false;
	}

	public boolean SelectNetwkIdWithAddress(Address netwrkAddr, NetworkAffiliation networkAff)
			throws InterruptedException {

		String expNetId = networkAff.getNetworkID();
		String expEffDate = networkAff.getNetworkActive().getEffectiveDate();
		String expTermDate = networkAff.getNetworkActive().getTerminationDate();
		String acctualData = null;

		String addrLine1 = netwrkAddr.getAddressDetails().getAddressLine1();
		String srchAddress = addrLine1 + " " + netwrkAddr.getAddressDetails().getCity() + " "
				+ netwrkAddr.getAddressDetails().getState() + " " + netwrkAddr.getAddressDetails().getZip();

		WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
		int rowCount = WebHandlers.getTblRowCount(orgNetworkTableBody);
		System.out.println(rowCount);
		acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, 0, 0);
		if (!(acctualData.equalsIgnoreCase("No records found."))) {
			for (int i = 0; i < rowCount; i++) {
				WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
				acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
						EpdsConstants.NETWORK_TABLE_ORG_NETPHYADDRS_COL);
				System.out.println(acctualData.substring(0, acctualData.length() - 3));
				if (acctualData.substring(0, acctualData.length() - 3).equalsIgnoreCase(srchAddress)) {
					WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
					acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
							EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
					if (acctualData.equalsIgnoreCase(expNetId)) {
						WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
						acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_ORG_NETEFFDATE_COL);
						if (acctualData.equals(expEffDate)) {
							WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
							acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_ORG_NETTERDATE_COL);
							if (acctualData.equals(expTermDate)) {
								WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
								WebHandlers.clickTblLinkValue(orgNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
								return true;
							}
						}
					}
				}
			}
		} else {
			LOGGER.info("Matching network record is not found.");
		}
		return false;
	}

	public boolean filterAndSelectRembIdWithNetID(Address netwrkAddr, NetworkAffiliation networkAffliations,
			Reimbursement netwrkreimbrsmnt) throws InterruptedException {

		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "ui-corner-all";

		String expNetId = networkAffliations.getNetworkID();
		String expNetEffDate = networkAffliations.getNetworkActive().getEffectiveDate();
		String expNetTermDate = networkAffliations.getNetworkActive().getTerminationDate();

		String expRemEffDate = netwrkreimbrsmnt.getReimbursementActive().getEffectiveDate();
		String expTermDate = netwrkreimbrsmnt.getReimbursementActive().getTerminationDate();
		String expRemValue = netwrkreimbrsmnt.getReimbursementValue();

		String addrLine1 = netwrkAddr.getAddressDetails().getAddressLine1();
		String srchAddress = (addrLine1 + " " + netwrkAddr.getAddressDetails().getCity() + " "
				+ netwrkAddr.getAddressDetails().getState() + " " + netwrkAddr.getAddressDetails().getZip())
						.replaceAll("\\s+", "");

		String acctualData = null;
		String rowsNull = "No records found.";
		WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
		WebWaitHelper.waitForElementPresence(orgNetwkSearchBox);
		WebHandlers.enterText(orgNetwkSearchBox, expNetId);

		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
		dropdown = new Select(orgNetwrkRemDropDown);
		selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);
		int rowCount = WebHandlers.getTblRowCount(orgNetworkTableBody);
		System.out.println(rowCount);
		acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(acctualData.equalsIgnoreCase(rowsNull))) {
				for (int i = 0; i < rowCount; i++) {
					acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
							EpdsConstants.NETWORK_TABLE_ORG_REMBIDVALUE_COL);
					if (acctualData.contains(expRemValue)) {
						acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_ORG_REMEFFDATE_COL);
						if (acctualData.equals(expRemEffDate)) {
							acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_ORG_REMTERDATE_COL);
							if (acctualData.equals(expTermDate)) {
								acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
								if (acctualData.equalsIgnoreCase(expNetId)) {
									acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
											EpdsConstants.NETWORK_TABLE_ORG_NETEFFDATE_COL);
									if (acctualData.equals(expNetEffDate)) {
										acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
												EpdsConstants.NETWORK_TABLE_ORG_NETTERDATE_COL);
										if (acctualData.equals(expNetTermDate)) {
											acctualData = (WebHandlers.getTblTdVal(orgNetworkTableBody, i,
													EpdsConstants.NETWORK_TABLE_ORG_NETPHYADDRS_COL)).replaceAll("\\s+",
															"");
											if (acctualData.equalsIgnoreCase(srchAddress)) {
												WebHandlers.clickTblVal(orgNetworkTableBody, i,
														EpdsConstants.NETWORK_TABLE_ORG_REMBIDTYPE_COL);
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = orgNetwrkRemPaginatorStatus.getAttribute("class");
				WebHandlers.click(orgNetwrkRemPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		return false;
	}

	public void searchAddressline1(String addrLine1) throws InterruptedException {

		System.out.println("Searching for address : " + addrLine1);
		WebWaitHelper.waitForElement(orgNetwkSearchBox);
		WebHandlers.enterText(orgNetwkSearchBox, addrLine1);

		WebHandlers.click(orgSearchMaxMinBtn);
		System.out.println("Minimize button clicked succesfully");
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebHandlers.click(orgSearchMaxMinBtn);
		System.out.println("Maximize button clicked succesfully");
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
	}

	public boolean indValidateSpeciality(Specialty specialty) {
		String spltyCode = specialty.getSpecialtyCode();
		String rowsNull = "No records found.";
		int rowCount = WebHandlers.getTblRowCount(indSpecialityTable);
		String acctualData = WebHandlers.getTblTdVal(indSpecialityTable, 0, 0);
		if (!(acctualData.equalsIgnoreCase(rowsNull))) {
			for (int i = 0; i < rowCount; i++) {
				String srcStr = WebHandlers.getTblTdVal(indSpecialityTable, i, 0);
				if (srcStr.equalsIgnoreCase(spltyCode)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean selectReimbId(Address netwrkAddr, Reimbursement reimbursement) throws InterruptedException {

		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "ui-corner-all";

		String expNetId = reimbursement.getNetworkID();
		String expRemVal = reimbursement.getReimbursementValue();
		String expEffDate = reimbursement.getReimbursementActive().getEffectiveDate();
		String expTermDate = reimbursement.getReimbursementActive().getTerminationDate();
		String acctualData = null;

		String addrLine1 = netwrkAddr.getAddressDetails().getAddressLine1();
		String srchAddress = addrLine1 + " " + netwrkAddr.getAddressDetails().getCity() + " "
				+ netwrkAddr.getAddressDetails().getState() + " " + netwrkAddr.getAddressDetails().getZip();

		WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
		WebWaitHelper.waitForElementPresence(orgNetwkSearchBox);

		dropdown = new Select(orgNetwrkRemDropDown);
		selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		int rowCount = WebHandlers.getTblRowCount(orgNetworkTableBody);
		System.out.println(rowCount);
		acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(acctualData.equalsIgnoreCase("No records found."))) {
				WebHandlers.click(orgNetwrkRemPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
					acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
							EpdsConstants.NETWORK_TABLE_ORG_NETPHYADDRS_COL);
					System.out.println(acctualData.substring(0, acctualData.length() - 3));
					if (acctualData.substring(0, acctualData.length() - 3).equalsIgnoreCase(srchAddress)) {
						WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
						acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
								EpdsConstants.NETWORK_TABLE_ORG_NETID_COL);
						if (acctualData.equalsIgnoreCase(expNetId)) {
							WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
							acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
									EpdsConstants.NETWORK_TABLE_ORG_REMEFFDATE_COL);
							if (acctualData.equals(expEffDate)) {
								WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
								acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
										EpdsConstants.NETWORK_TABLE_ORG_REMTERDATE_COL);
								if (acctualData.equals(expTermDate)) {
									WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
									acctualData = WebHandlers.getTblTdVal(orgNetworkTableBody, i,
											EpdsConstants.NETWORK_TABLE_ORG_REMBIDVALUE_COL);
									if (acctualData.contains(expRemVal)) {
										WebWaitHelper.waitForElementPresence(orgNetworkTableBody);
										WebHandlers.clickTblLinkValue(orgNetworkTableBody, i,
												EpdsConstants.NETWORK_TABLE_ORG_REMBIDTYPE_COL);
										return true;
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = orgNetwrkRemPaginatorStatus.getAttribute("class");
				WebHandlers.click(orgNetwrkRemPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		return false;
	}
}
