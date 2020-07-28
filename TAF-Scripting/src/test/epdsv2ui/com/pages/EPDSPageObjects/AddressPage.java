package com.pages.EPDSPageObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.Specialty;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.RpaTestUtilities.ProviderType;

public class AddressPage {

	public static Logger LOGGER = Logger.getLogger(AddressPage.class);

	WebDriver driver;

	@FindBy(xpath = "//input[@id='prvInfo-form:dataTable_address:globalFilter']")
	WebElement addressSearchBox;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address']/table/tbody")
	WebElement practiceAddrTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_AddedAddress:globalFilter']")
	WebElement grpAddrSearchBox;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_AddedAddress_data']")
	WebElement grpAddressTable;

	// ********** Individual Tabs **********
	@FindBy(xpath = "//a[@id='prvInfo-form:contact']")
	WebElement contactTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:altIds']")
	WebElement altIdTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:areaOfFocus']")
	WebElement areaOfFocusTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:officeDetails']")
	WebElement officeDetailsTab;

	@FindBy(xpath = "// a[@id='prvInfo-form:schedule']")
	WebElement scheduleTab;

	@FindBy(xpath = "//*[@id='prvInfo-form:addrSpecialty']")
	WebElement addrSpecialtyTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:SpecialPrgm']")
	WebElement splPrgmTab;

	// ********** Organization Tabs **********

	@FindBy(xpath = "//*[@id='prvInfo-form:officeServices']")
	WebElement officeServicesTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:specialty']")
	WebElement specialtyTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:specialty']")
	WebElement orgSpecialtyTab;

	@FindBy(xpath = "//a[@id='prvInfo-form:specialProgram']")
	WebElement orgSplPrgmTab;

	@FindBy(xpath = "//*[@id='prvInfo-form:remitInfo']")
	WebElement remitInfoTab;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewStreet1']")
	WebElement addrLine1;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewStreet2']")
	WebElement addrLine2;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewStreet3']")
	WebElement addrLine3;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewcity']")
	WebElement addrCity;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewpostalcode']")
	WebElement addrZip;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewPracticeIndicator']")
	WebElement practiceInd;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewRemitIndicator']")
	WebElement remitPracticeInd;

	@FindBy(xpath = "//*[@id='prvInfo-form:vieweffectiveDate']")
	WebElement addrEffectDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewterminationDate']")
	WebElement addrTermDate;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewTremRsnCode']")
	WebElement addrTermReasonCode;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_contact_table_data']")
	WebElement indContactTable;

	// *[@id='prvInfo-form:org_contact_table_data']
	@FindBy(xpath = "//*[@id='prvInfo-form:org_contact_table']/table/tbody/tr")
	WebElement orgContactTable;

	@FindBy(xpath = "//tbody[@id='prvInfo-form:ind_address_altId_table1_data']")
	WebElement altIdTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_address_altId_table1:globalFilter']")
	WebElement searchAltIdFltr;

	@FindBy(xpath = "//*[@id='prvInfo-form:upd_ind_addr_areaOfFocus']/tbody/tr/td/table/tbody/tr/td[2]/table/tbody")
	WebElement areaOfFocusTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:org_schedule_table_data']")
	WebElement scheduleTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_spec_bill_tableView_data']")
	WebElement indSpecialtyTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_spec_bill_tableView:globalFilter']")
	WebElement specialityTblFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:Org_spec_bill_tableView_data']")
	WebElement orgSpecialtyTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:prvSpclPrgm']/tbody/tr/td/div/table/tbody")
	WebElement splPrgmTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:org_Remit_Info_table_view_data']")
	WebElement remitInfoTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_address_office_table_data']")
	WebElement officeAccessTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:org_address_office_table_data']")
	WebElement orgOfficeAccessTbl;

	@FindBy(xpath = "//*[@id='prvInfo-form:officePanel']/tbody/tr/td/table/tbody/tr/td[2]/table/tbody/tr[2]/td/textarea")
	WebElement officeServiceDetails;

	@FindBy(xpath = "//span[@id='prvInfo-form:languageDescription']")
	WebElement staffLanguage;

	@FindBy(xpath = "//span[@id='prvInfo-form:ind_contact_table:0:view_contaPhonecttype']")
	WebElement contactPhoneType;

	@FindBy(xpath = "//span[contains(text(),'Close')]")
	WebElement addressCloseBtn;

	// added prvInfo-form:dataTable_address_paginator_bottom
	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/select")
	WebElement addressDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[4]/span[1]")
	WebElement addrPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[5]")
	WebElement addrPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dataTable_address_paginator_bottom']/span[5]/span")
	WebElement addrPaginatorIcon;

	// contact ind
	@FindBy(xpath = "//*[@id='prvInfo-form:ind_contact_table_paginator_bottom']/span[4]/span[1]")
	WebElement indContactPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_contact_table_paginator_bottom']/span[5]")
	WebElement indContactPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_contact_table_paginator_bottom']/span[5]/span")
	WebElement indContactPaginatorIcon;
	// org
	@FindBy(xpath = "//*[@id='prvInfo-form:org_contact_table_paginator_bottom']/span[4]/span[1]")
	WebElement orgContactPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:org_contact_table_paginator_bottom']/span[5]")
	WebElement orgContactPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:org_contact_table_paginator_bottom']/span[5]/span")
	WebElement orgContactPaginatorIcon;

	// altId Tab
	@FindBy(xpath = "//*[@id='prvInfo-form:ind_address_altId_table1_paginator_bottom']/span[4]/span[1]")
	WebElement indAltPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_address_altId_table1_paginator_bottom']/span[5]")
	WebElement indAltPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:ind_address_altId_table1_paginator_bottom']/span[5]/span")
	WebElement indAltPaginatorIcon;

	public AddressPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean filterAndSelectIndAddress(Address address) throws InterruptedException {

		String strPaginationDefaultStatus = "ui-corner-all";
		String addrLine1 = address.getAddressDetails().getAddressLine1();
		String addrLine2 = Objects.toString(address.getAddressDetails().getAddressLine2(), "");
		String addrLine3 = Objects.toString(address.getAddressDetails().getAddressLine3(), "");

		String srchAddress = ((addrLine1 + " " + addrLine2 + " " + addrLine3 + " "
				+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState() + " "
				+ address.getAddressDetails().getZip()).replaceAll("\\s+", "")).toUpperCase();
		String expAddrType = address.getAddressDetails().getAddressType();
		String expEffdate = address.getAddressActive().getEffectiveDate();
		String expTermDate = address.getAddressActive().getTerminationDate();
		Boolean practiceIndicator = address.getAddressDetails().getPrimaryPracticeIndicator();
		// srchAddress = srchAddress.replaceAll("\\s+", "");
		String expPracticeIndicator = null;

		if (practiceIndicator) {
			expPracticeIndicator = "Y";
		} else {
			expPracticeIndicator = "N";
		}

		LOGGER.info("Searching for address : " + addrLine1);
		WebWaitHelper.waitForElement(addressSearchBox);
		WebHandlers.enterText(addressSearchBox, addrLine1);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		Select dropdown = new Select(addressDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		String srchStr = WebHandlers.getTblTdVal(practiceAddrTable, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!srchStr.equalsIgnoreCase("No records found.")) {
				WebHandlers.click(addrPaginationFirst);
				int rowCount = WebHandlers.getTblRowCount(practiceAddrTable);
				LOGGER.info("Address Details table row count after filtering : " + rowCount);

				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(practiceAddrTable, i)) {
						srchStr = ((WebHandlers.getTblTdVal(practiceAddrTable, i, EpdsConstants.ADDR_DETAILS_PRAC_ADDR))
								.replaceAll("\\s+", "")).toUpperCase();
						if (srchStr.equalsIgnoreCase(srchAddress)) {
							srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i, EpdsConstants.ADDR_DETAILS_PAC_IND);
							// if (srchStr.equalsIgnoreCase(expPracticeIndicator)) {
							if (true) {
								srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
										EpdsConstants.ADDR_DETAILS_PRAC_ADDR_TYPE);
								if (srchStr.contains(expAddrType)) {
									srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
											EpdsConstants.ADDR_DETAILS_EFF_DATE);
									if (srchStr.equals(expEffdate)) {
										srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
												EpdsConstants.ADDR_DETAILS_TERM_DATE);
										if (srchStr.equals(expTermDate)) {
											WebHandlers.clickTblVal(practiceAddrTable, i,
													EpdsConstants.ADDR_DETAILS_PRAC_ADDR);
											return true;
										}
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = addrPaginatorStatus.getAttribute("class");
				WebHandlers.click(addrPaginatorIcon);
			}
			LOGGER.info("Matching Address record is not found.");
			return false;
		}
		return false;
	}

	public boolean filterAndSelectOrgAddress(Address address) throws InterruptedException {

		String strPaginationDefaultStatus = "ui-corner-all";
		String addrLine1 = address.getAddressDetails().getAddressLine1();
		String addrLine2 = Objects.toString(address.getAddressDetails().getAddressLine2(), "");
		String addrLine3 = Objects.toString(address.getAddressDetails().getAddressLine3(), "");
		String srchAddress = ((addrLine1 + " " + addrLine2 + " " + addrLine3 + " "
				+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState() + " "
				+ address.getAddressDetails().getZip()).replaceAll("\\s+", "")).toUpperCase();
		String expAddrType = address.getAddressDetails().getAddressType();
		String expEffdate = address.getAddressActive().getEffectiveDate();
		String expTermDate = address.getAddressActive().getTerminationDate();
		Boolean practiceIndicator = address.getAddressDetails().getPrimaryPracticeIndicator();
		String expPracticeIndicator = null;

		if (practiceIndicator) {
			expPracticeIndicator = "Y";
		} else {
			expPracticeIndicator = "N";
		}

		LOGGER.info("Searching for address : " + addrLine1);
		WebWaitHelper.waitForElement(addressSearchBox);
		WebHandlers.enterText(addressSearchBox, addrLine1);
		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);

		Select dropdown = new Select(addressDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		String srchStr = WebHandlers.getTblTdVal(practiceAddrTable, 0, 0);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!srchStr.equalsIgnoreCase("No records found.")) {
				WebHandlers.click(addrPaginationFirst);
				int rowCount = WebHandlers.getTblRowCount(practiceAddrTable);
				LOGGER.info("Address Details table row count after filtering : " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(practiceAddrTable, i)) {
						srchStr = ((WebHandlers.getTblTdVal(practiceAddrTable, i, EpdsConstants.ADDR_DETAILS_PRAC_ADDR))
								.replaceAll("\\s+", "")).toUpperCase();
						if (srchStr.equalsIgnoreCase(srchAddress)) {
							srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
									EpdsConstants.ORG_ADDR_DETAILS_PAC_IND);
							// if (srchStr.equalsIgnoreCase(expPracticeIndicator)) {
							if (true) {
								srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
										EpdsConstants.ADDR_DETAILS_PRAC_ADDR_TYPE);
								if (srchStr.contains(expAddrType)) {
									srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
											EpdsConstants.ORG_ADDR_DETAILS_EFF_DATE);
									if (srchStr.equals(expEffdate)) {
										srchStr = WebHandlers.getTblTdVal(practiceAddrTable, i,
												EpdsConstants.ORG_ADDR_DETAILS_TERM_DATE);
										if (srchStr.equals(expTermDate)) {
											WebHandlers.clickTblVal(practiceAddrTable, i,
													EpdsConstants.ADDR_DETAILS_PRAC_ADDR);
											return true;
										}
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = addrPaginatorStatus.getAttribute("class");
				WebHandlers.click(addrPaginatorIcon);
			}
			LOGGER.info("Matching Address record is not found.");
			return false;
		}
		return false;
	}

	public boolean filterAndSelectGrpAddress(Address address) {

		String addrLine1 = address.getAddressDetails().getAddressLine1();
		String srchAddress = ((addrLine1 + " " + address.getAddressDetails().getCity() + " "
				+ address.getAddressDetails().getState() + " " + address.getAddressDetails().getZip())
						.replaceAll("\\s+", "")).toUpperCase();
		String expAddrType = address.getAddressDetails().getAddressType();
		String expEffdate = address.getAddressActive().getEffectiveDate();
		String expTermDate = address.getAddressActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(address.getAddressActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String srchStr = null;

		LOGGER.info("Searching for address : " + addrLine1);
		WebWaitHelper.waitForElement(grpAddrSearchBox);
		WebHandlers.enterText(grpAddrSearchBox, addrLine1);
		try {
			Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		srchStr = WebHandlers.getTblTdVal(grpAddressTable, 0, 0);
		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(grpAddressTable);
			LOGGER.info("Address Details table row count after filtering : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = ((WebHandlers.getTblTdVal(grpAddressTable, i, EpdsConstants.GRP_ADDR_DETAILS_ADDR))
						.replaceAll("\\s+", "")).toUpperCase();
				if (srchStr.contains(srchAddress)) {
					srchStr = WebHandlers.getTblTdVal(grpAddressTable, i, EpdsConstants.GRP_ADDR_DETAILS_ADDR_TYPE);
					if (srchStr.equalsIgnoreCase(expAddrType)) {
						srchStr = WebHandlers.getTblTdVal(grpAddressTable, i, EpdsConstants.GRP_ADDR_DETAILS_EFF_DATE);
						if (srchStr.equals(expEffdate)) {
							srchStr = WebHandlers.getTblTdVal(grpAddressTable, i,
									EpdsConstants.GRP_ADDR_DETAILS_TERM_DATE);
							if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
								return true;
							} else {
								if (srchStr.equals(expTermDate)) {
									srchStr = WebHandlers.getTblTdVal(grpAddressTable, i,
											EpdsConstants.GRP_ADDR_DETAILS_TERM_REASON_CODE);
									if (srchStr.equalsIgnoreCase(expTermReasonCode) && srchStr != null)
										return true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Address record is not found.");
		return false;
	}

	public void navToContactTab() {
		WebHandlers.scrollToElement(contactTab);
		WebHandlers.click(contactTab);
	}

	public void navToAltIdsTab() {
		WebHandlers.scrollToElement(altIdTab);
		WebHandlers.click(altIdTab);
	}

	public void navToAreaOfFocusTab() {
		WebHandlers.scrollToElement(areaOfFocusTab);
		WebHandlers.click(areaOfFocusTab);
	}

	public void navToOfficeDetailsTab() {
		WebHandlers.scrollToElement(officeDetailsTab);
		WebHandlers.click(officeDetailsTab);
	}

	public void navToScheduleTab() {
		WebHandlers.scrollToElement(scheduleTab);
		WebHandlers.click(scheduleTab);
	}

	public void navToSpecialPrgmTab() {
		WebHandlers.scrollToElement(splPrgmTab);
		WebHandlers.click(splPrgmTab);
	}

	public void navToOfficeServicesTab() {
		WebHandlers.scrollToElement(officeServicesTab);
		WebHandlers.click(officeServicesTab);
	}

	public void navToSpecialty() {
		WebHandlers.scrollToElement(addrSpecialtyTab);
		WebHandlers.click(addrSpecialtyTab);
	}

	public void navToOrgSpecialty() {
		WebHandlers.scrollToElement(orgSpecialtyTab);
		WebHandlers.click(orgSpecialtyTab);
	}

	public void navToOrgSpecialPrgmTab() {
		WebHandlers.scrollToElement(orgSplPrgmTab);
		WebHandlers.click(orgSplPrgmTab);
	}

	public void navToRemitInfoTab() {
		WebHandlers.scrollToElement(remitInfoTab);
		WebHandlers.click(remitInfoTab);
	}

	// Address Details
	public Map<String, String> getAddressDetails() {

		Map<String, String> address = new HashMap<String, String>();
		address.put("AddressLine2", addrLine2.getText());
		address.put("City", addrCity.getText());
		address.put("Zip", addrZip.getText());
		return address;
	}

	// Address Details
	public String getAddressLine1() {
		return (addrLine1.getText());
	}

	// Address Details
	public String getAddressLine2() {
		return (addrLine2.getText());
	}

	// Address Details
	public String getAddressLine3() {
		return (addrLine3.getText());
	}

	// Address Details
	public String getAddrZip() {
		return (addrZip.getText());
	}

	// Address Details
	public String getPracticeIndicator() {
		return (practiceInd.getText());
	}

	public String getRemitPracticeInd() {
		return (remitPracticeInd.getText());
	}

	// Address Details
	public String getAddrEffectDate() {
		return (addrEffectDate.getText());
	}

	// Address Details
	public String getAddrTermDate() {
		return (addrTermDate.getText());
	}

	// Address Details
	public String getAddrTermReasonCode() {
		return (addrTermReasonCode.getText());
	}

	// Alt Id Tab
	public boolean valAltIdTypeSource(String expAltIdType, String expAltSource) {

		String strPaginationDefaultStatus = "ui-corner-all";
		WebWaitHelper.waitForElement(searchAltIdFltr);
		WebHandlers.enterText(searchAltIdFltr, expAltIdType);
		try {
			Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String srchStr = WebHandlers.getTblTdVal(altIdTable, 0, 0);
		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(altIdTable);
			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					WebHandlers.click(indAltPaginationFirst);
					String actualAltIdType = WebHandlers.getTblTdVal(altIdTable, i, EpdsConstants.ALTID_ALTID_TYPE);
					if (actualAltIdType.equalsIgnoreCase(expAltIdType)) {
						String actualAltSource = WebHandlers.getTblTdVal(altIdTable, i, EpdsConstants.ALTID_ALTID_SRC);
						if (actualAltSource.equalsIgnoreCase(expAltSource)) {
							return true;
						}
					}
				}
				strPaginationDefaultStatus = indAltPaginatorStatus.getAttribute("class");
				WebHandlers.click(indAltPaginatorIcon);
			}
		}
		LOGGER.info("Matching Address record is not found.");
		return false;
	}

	// Area of Focus Tab
	public boolean valAreaOfFocusMatch(String expAreaOfFocus) {

		int rowCount = WebHandlers.getTblRowCount(areaOfFocusTable);
		for (int i = 0; i < rowCount; i++) {
			String actualAreaOfFocus = WebHandlers.getTblTdVal(areaOfFocusTable, i, 0);
			if (actualAreaOfFocus.contains(expAreaOfFocus)) {
				return true;
			}
		}
		LOGGER.info("Matching Area of Focus is not found.");
		return false;
	}

	// Office Details Tab
	public boolean valOfficeAccessCode(String accessibilityCode, ProviderType provType) {

		WebElement thisOfficeAccessTbl = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			thisOfficeAccessTbl = officeAccessTable;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			thisOfficeAccessTbl = orgOfficeAccessTbl;
		}

		WebHandlers.scrollToElement(thisOfficeAccessTbl);
		String srchStr = WebHandlers.getTblTdVal(thisOfficeAccessTbl, 0, 0);
		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(thisOfficeAccessTbl);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(thisOfficeAccessTbl, i,
						EpdsConstants.OFFICE_ACCESS_TABLE_ACCESS_CODE);
				if (srchStr.equalsIgnoreCase(accessibilityCode))
					return true;
			}
		}
		LOGGER.info("Matching Office Access Code is not found.");
		return false;
	}

	public String getStaffLanguage() {
		return (staffLanguage.getText());
	}

	public boolean valSchedulingDetail(SchedulingDetail schedulingDetail) {

		String expScheduleType = "Regular";
		String expScheduleDays = schedulingDetail.getDays();
		String expScheduleOpened = schedulingDetail.getOpenTime();
		String expScheduleClosed = schedulingDetail.getCloseTime();

		WebWaitHelper.waitForElement(scheduleTable);
		String srchStr = WebHandlers.getTblTdVal(scheduleTable, 0, 0);

		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(scheduleTable);
			LOGGER.info("Scheduling detail table row count : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(scheduleTable, i, EpdsConstants.SCHEDULE_TABLE_SCHEDULE_DAYS);
				if (srchStr.equalsIgnoreCase(expScheduleDays)) {
					srchStr = WebHandlers.getTblTdVal(scheduleTable, i, EpdsConstants.SCHEDULE_TABLE_SCHEDULE_OPENED);
					if (srchStr.equalsIgnoreCase(expScheduleOpened)) {
						srchStr = WebHandlers.getTblTdVal(scheduleTable, i,
								EpdsConstants.SCHEDULE_TABLE_SCHEDULE_CLOSED);
						if (srchStr.equalsIgnoreCase(expScheduleClosed)) {
							return true;
						}
					}
				}
			}
		}
		LOGGER.info("Matching Schedule record is not found.");
		return false;
	}

	public boolean valSpecialtyCount(String speciality) throws InterruptedException {
		WebWaitHelper.waitForElement(specialityTblFilter);
		WebHandlers.enterText(specialityTblFilter, speciality);
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

		WebWaitHelper.waitForElement(indSpecialtyTable);
		String srchStr = WebHandlers.getTblTdVal(indSpecialtyTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(indSpecialtyTable);
			if (rowCount == 1) {
				return true;
			}
		}
		LOGGER.info("More than one Specialty record is not found in Specialty table.");
		return false;
	}

	public boolean valSpecialty(Specialty speciality, ProviderType provType) {
		WebElement specialtyTable = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			specialtyTable = indSpecialtyTable;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			specialtyTable = orgSpecialtyTable;
		}
		String expSpecialtyCode = speciality.getSpecialtyCode();
		WebWaitHelper.waitForElement(specialtyTable);
		String srchStr = WebHandlers.getTblTdVal(specialtyTable, 0, 0);

		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(specialtyTable);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(specialtyTable, i, EpdsConstants.SPECIALITY_CODE);
				if (srchStr.equalsIgnoreCase(expSpecialtyCode)) {
					return true;
				}
			}
		}
		LOGGER.info("Matching Specialty is not found");
		return false;
	}

	// Special Program Tab
	public boolean valSplProgramsType(String expSplPrgmType, String expSplPrgmEffDate) {
		int rowCount = WebHandlers.getTblRowCount(splPrgmTable);
		LOGGER.info("Special Programs table row count : " + rowCount);
		for (int i = 0; i < rowCount; i++) {
			String srchStr = WebHandlers.getTblTdVal(splPrgmTable, i, EpdsConstants.SPL_PRGM_TABLE_SPL_PRGM_TYPE);
			if (srchStr.equalsIgnoreCase(expSplPrgmType)) {
				srchStr = WebHandlers.getTblTdVal(splPrgmTable, i, EpdsConstants.SPL_PRGM_TABLE_SPL_PRGM_EFF_DATE);
				if (srchStr.equals(expSplPrgmEffDate))
					return true;
			}
		}
		LOGGER.info("Matching Special Program is not found.");
		return false;
	}

	public boolean searchContactTable(ContactDetail contactDetail, ProviderType provType) {
		WebElement contactTable = null;
		WebElement npiFirstPage = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			contactTable = indContactTable;
			npiFirstPage = indContactPaginationFirst;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			contactTable = orgContactTable;
			npiFirstPage = orgContactPaginationFirst;
		}

		String fName = contactDetail.getFirstName() != null ? contactDetail.getFirstName() : "";
		String mName = contactDetail.getMiddleName() != null ? contactDetail.getMiddleName() : "";
		String lName = contactDetail.getLastName() != null ? contactDetail.getLastName() : "";
		String title = contactDetail.getSuffix() != null ? contactDetail.getSuffix() : "";
		String phoneNumber = contactDetail.getPhone() != null ? contactDetail.getPhone() : "";
		phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
		String faxNumber = contactDetail.getFax() != null ? contactDetail.getFax() : "";
		faxNumber = faxNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
		String url = contactDetail.getWebAddress() != null ? contactDetail.getWebAddress() : "";
		String emailAddress = contactDetail.getEmail() != null ? contactDetail.getEmail() : "";

		String phoneType = contactDetail.getPhone() != null ? "Directory Phone Number" : "";
		String faxType = contactDetail.getFax() != null ? "Fax Phone Number" : "";
		String emailType = contactDetail.getEmail() != null ? "Email Address" : "";
		String webType = contactDetail.getWebAddress() != null ? "Web Site Address" : "";
		String strPaginationDefaultStatus = "ui-corner-all";

		WebWaitHelper.waitForElement(contactTable);
		String srchStr = WebHandlers.getTblTdVal(contactTable, 0, 0);

		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!srchStr.equalsIgnoreCase("No records found.")) {
				WebHandlers.click(npiFirstPage);
				int rowCount = WebHandlers.getContactTblRowCount(contactTable);
				LOGGER.info("Contact table row count : " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					srchStr = WebHandlers.getContactTblTdValue(contactTable, i, EpdsConstants.CONTACT_LASTNAME);
					if (srchStr.equalsIgnoreCase(lName)) {
						srchStr = WebHandlers.getContactTblTdValue(contactTable, i, EpdsConstants.CONTACT_FIRSTNAME);
						if (srchStr.equalsIgnoreCase(fName)) {
							srchStr = WebHandlers.getContactTblTdValue(contactTable, i, EpdsConstants.CONTACT_MIDNAME);
							if (srchStr.equalsIgnoreCase(mName)) {
								srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
										EpdsConstants.CONTACT_TITLE);
								if (srchStr.equalsIgnoreCase(title)) {
									srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
											EpdsConstants.CONTACT_PHONE_TYPE);
									if (srchStr.equalsIgnoreCase(phoneType)) {
										srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
												EpdsConstants.CONTACT_PHONE_NUMBER);
										if (srchStr.equals(phoneNumber)) {
											srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
													EpdsConstants.CONTACT_FAX_TYPE);
											if (srchStr.equalsIgnoreCase(faxType)) {
												srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
														EpdsConstants.CONTACT_FAX_NUMBER);
												if (srchStr.equals(faxNumber)) {
													srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
															EpdsConstants.CONTACT_EMAIL_TYPE);
													if (srchStr.equalsIgnoreCase(emailType)) {
														srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
																EpdsConstants.CONTACT_EMAIL_ADDRESS);
														if (srchStr.equalsIgnoreCase(emailAddress)) {
															srchStr = WebHandlers.getContactTblTdValue(contactTable, i,
																	EpdsConstants.CONTACT_WEB_TYPE);
															if (srchStr.equalsIgnoreCase(webType)) {
																srchStr = WebHandlers.getContactTblTdValue(contactTable,
																		i, EpdsConstants.CONTACT_WEB_URL);
																if (srchStr.equalsIgnoreCase(url))
																	return true;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (provType.equals(ProviderType.INDIVIDUAL)) {
					strPaginationDefaultStatus = indContactPaginatorStatus.getAttribute("class");
					WebHandlers.click(indContactPaginatorIcon);
				} else if (provType.equals(ProviderType.ORGANIZATION)) {
					strPaginationDefaultStatus = orgContactPaginatorStatus.getAttribute("class");
					WebHandlers.click(orgContactPaginatorIcon);
				}
			}
			LOGGER.info("Matching Contact detail record is not available.");
			return false;
		}
		return false;
	}

	// Office Services Tab
	public String getOfficeServiceDetail() {
		return officeServiceDetails.getText();
	}

	// Remittance Information Tab
	public boolean searchRemitInfoTable(RemittanceDetail remitDetail) {

		String checkName = remitDetail.getCheckName();
		String effectiveDate = remitDetail.getRemittanceActive().getEffectiveDate();
		String terminationDate = remitDetail.getRemittanceActive().getTerminationDate();
		String termReasonCode = Objects.toString(remitDetail.getRemittanceActive().getTerminationReasonCode(), "");

		String npiValue = remitDetail.getRemittanceNPI().getNpiValue();
		WebWaitHelper.waitForElement(remitInfoTable);
		String srchStr = WebHandlers.getTblTdVal(remitInfoTable, 0, 0);

		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(remitInfoTable);
			LOGGER.info("Remit Info table row count : " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(remitInfoTable, i, EpdsConstants.REMIT_INFO_CHECKNAME);
				if (srchStr.equalsIgnoreCase(checkName)) {
					srchStr = WebHandlers.getTblTdVal(remitInfoTable, i, EpdsConstants.REMIT_INFO_EFF_DATE);
					if (srchStr.equals(effectiveDate)) {
						srchStr = WebHandlers.getTblTdVal(remitInfoTable, i, EpdsConstants.REMIT_INFO_TERM_DATE);
						if (srchStr.equals(terminationDate)) {
							srchStr = WebHandlers.getTblTdVal(remitInfoTable, i,
									EpdsConstants.REMIT_INFO_TERM_REASON_CODE);
							if (srchStr.equalsIgnoreCase(termReasonCode)) {
								srchStr = WebHandlers.getTblTdVal(remitInfoTable, i, EpdsConstants.REMIT_INFO_NPI);
								if (srchStr.equals(npiValue)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Remit detail record is not available.");
		return false;
	}

	public boolean searchOrgRemitAddrContactTable(ContactDetail contDetails) {

		String fName = contDetails.getFirstName() != null ? contDetails.getFirstName() : "";
		String mName = contDetails.getMiddleName() != null ? contDetails.getMiddleName() : "";
		String lName = contDetails.getLastName() != null ? contDetails.getLastName() : "";
		String title = contDetails.getSuffix() != null ? contDetails.getSuffix() : "";
		String phoneNumber = contDetails.getPhone() != null ? contDetails.getPhone() : "";
		phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
		String phoneType = "Directory Phone Number" != null ? "Directory Phone Number" : "";
		WebWaitHelper.waitForElement(orgContactTable);
		String srchStr = WebHandlers.getTblTdVal(orgContactTable, 0, 0);

		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getContactTblRowCount(orgContactTable);
			LOGGER.info("Contact table row count: " + rowCount);
			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i, EpdsConstants.CONTACT_LASTNAME);
				if (srchStr.contains(lName)) {
					srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i, EpdsConstants.CONTACT_FIRSTNAME);
					if (srchStr.contains(fName)) {
						srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i, EpdsConstants.CONTACT_MIDNAME);
						if (srchStr.contains(mName)) {
							srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i, EpdsConstants.CONTACT_TITLE);
							if (srchStr.contains(title)) {
								srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i,
										EpdsConstants.CONTACT_PHONE_TYPE);
								if (srchStr.contains(phoneType)) {
									srchStr = WebHandlers.getContactTblTdValue(orgContactTable, i,
											EpdsConstants.CONTACT_PHONE_NUMBER);
									if (srchStr.equals(phoneNumber)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Contact record is not found.");
		return false;
	}

	public void clickAddressCloseBtn() {
		WebHandlers.click(addressCloseBtn);
	}

}
