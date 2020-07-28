package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Specialty;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;

public class SpecialityTaxonomy {

	public static Logger LOGGER = Logger.getLogger(SpecialityTaxonomy.class);

	WebDriver driver;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_specEdit']/table/tbody")
	WebElement specialtyDetailsTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_specEdit:globalFilter\"]")
	WebElement specialtySrchbox;

	// Added
	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_specEdit_paginator_bottom']/select")
	WebElement specialtyDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_specEdit_paginator_bottom']/span[4]/span[1]")
	WebElement specialtyPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_specEdit_paginator_bottom']/span[5]")
	WebElement specialtyPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_specEdit_paginator_bottom']/span[5]/span")
	WebElement specialtyPaginatorIcon;

	public SpecialityTaxonomy(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean searchSpecialtyDetailsTable(Specialty specialty) {

		String spltyCode = specialty.getSpecialtyCode();
		boolean spltyInd = specialty.getPrimarySpecialtyIndicator();
		String boardCertifAgencyName = Objects.toString(specialty.getBoardCertificationAgencyName(), "");
		String boardCertifRecertifDate = Objects.toString(specialty.getBoardCertificationRecertificationDate(), "");
		String boardCertifEffDate = Objects.toString(specialty.getBoardCertificationDate(), "");
		String boardCertifExpDate = Objects.toString(specialty.getBoardCertificationExpirationDate(), "");
		String spltyEffDate = specialty.getSpecialtyActive().getEffectiveDate();
		String spltyTermDate = specialty.getSpecialtyActive().getTerminationDate();
		String spltyTermReasonCode = Objects.toString(specialty.getSpecialtyActive().getTerminationReasonCode(), "");
		String strPaginationDefaultStatus = "ui-corner-all";

		WebWaitHelper.waitForElement(specialtySrchbox);
		WebHandlers.enterText(specialtySrchbox, spltyCode);
		try {
			Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Select dropdown = new Select(specialtyDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		String srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			WebHandlers.click(specialtyPaginationFirst);
			int rowCount = WebHandlers.getTblRowCount(specialtyDetailsTable);
			LOGGER.info("Specialty table row count : " + rowCount);
			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(specialtyDetailsTable, i)) {
						String address = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
								EpdsConstants.SPECIALTY_ADDRESS);
						String addrType = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
								EpdsConstants.SPECIALTY_ADDRESS_TYPE);
						String termReasonCode = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
								EpdsConstants.SPECIALTY_TERM_REASON_CODE);

						if (address.equals("") && addrType.equals("") && !(termReasonCode.equals("Created in Error"))) {
							srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
									EpdsConstants.SPECIALTY_PRIMARY_SPL_IND);
							Boolean specialtyFlag = false;
							if (spltyInd && srchStr.equals("Primary")) {
								specialtyFlag = true;
							}
							if (!spltyInd && srchStr.equals("Secondary")) {
								specialtyFlag = true;
							}

							if (specialtyFlag) {
								srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
										EpdsConstants.SPECIALTY_AGENCY_NAME);
								if (srchStr.equalsIgnoreCase(boardCertifAgencyName)) {
									srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
											EpdsConstants.SPECIALTY_RECERTIF_DATE);
									if (srchStr.equals(boardCertifRecertifDate)) {
										srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
												EpdsConstants.SPECIALTY_BOARD_CERTIF_EFF_DATE);
										if (srchStr.equals(boardCertifEffDate)) {
											srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
													EpdsConstants.SPECIALTY_BOARD_CERTIF_EXP_DATE);
											if (srchStr.equals(boardCertifExpDate)) {
												srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
														EpdsConstants.SPECIALTY_EFF_DATE);
												if (srchStr.equals(spltyEffDate)) {
													srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
															EpdsConstants.SPECIALTY_TERM_DATE);
													if (srchStr.equals(spltyTermDate)) {
														srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
																EpdsConstants.SPECIALTY_TERM_REASON_CODE);
														if (srchStr.equalsIgnoreCase(spltyTermReasonCode)) {
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
				strPaginationDefaultStatus = specialtyPaginatorStatus.getAttribute("class");
				WebHandlers.click(specialtyPaginatorIcon);
			}
			return false;
		}
		LOGGER.info("Matching Specialty detail record is not found.");
		return false;
	}

	public boolean searchSpecialityDetails(Address splAddress, Specialty specialty) throws InterruptedException {

		String spltyIndStr = null;
		String strPaginationDefaultStatus = "ui-corner-all";
		String addrLine1 = splAddress.getAddressDetails().getAddressLine1();
		String srchAddress = addrLine1 + " " + splAddress.getAddressDetails().getCity() + " "
				+ splAddress.getAddressDetails().getState() + " " + splAddress.getAddressDetails().getZip();
		String strAddressType = splAddress.getAddressDetails().getAddressType();

		String spltyCode = specialty.getSpecialtyCode();
		boolean spltyInd = specialty.getPrimarySpecialtyIndicator();
		if (spltyInd) {
			spltyIndStr = "Primary";
		} else {
			spltyIndStr = "Secondary";
		}

		String boardCertifAgencyName = specialty.getBoardCertificationAgencyName() != null
				? specialty.getBoardCertificationAgencyName()
				: "";
		String boardCertifRecertifDate = specialty.getBoardCertificationRecertificationDate() != null
				? specialty.getBoardCertificationRecertificationDate()
				: "";
		String boardCertifEffDate = specialty.getBoardCertificationDate() != null
				? specialty.getBoardCertificationDate()
				: "";
		String boardCertifExpDate = specialty.getBoardCertificationExpirationDate() != null
				? specialty.getBoardCertificationExpirationDate()
				: "";
		String spltyEffDate = specialty.getSpecialtyActive().getEffectiveDate();
		String spltyTermDate = specialty.getSpecialtyActive().getTerminationDate();

		WebWaitHelper.waitForElement(specialtySrchbox);
		WebHandlers.enterText(specialtySrchbox, spltyCode);
		try {
			Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Select dropdown = new Select(specialtyDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		String srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			WebHandlers.click(specialtyPaginationFirst);
			int rowCount = WebHandlers.getTblRowCount(specialtyDetailsTable);
			LOGGER.info("Specialty table row count after filtering : " + rowCount);

			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(specialtyDetailsTable, i)) {
						WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
						srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
								EpdsConstants.SPECIALTY_TABLE_SPECIALTY);
						if (srchStr.equalsIgnoreCase(spltyCode)) {
							srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
									EpdsConstants.SPECIALTY_AGENCY_NAME);
							if (srchStr.equalsIgnoreCase(boardCertifAgencyName)) {
								WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
								srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
										EpdsConstants.SPECIALTY_PRIMARY_SPL_IND);
								if (srchStr.equalsIgnoreCase(spltyIndStr)) {
									WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
									srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
											EpdsConstants.SPECIALTY_RECERTIF_DATE);
									if (srchStr.equals(boardCertifRecertifDate)) {
										WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
										srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
												EpdsConstants.SPECIALTY_BOARD_CERTIF_EFF_DATE);
										if (srchStr.equals(boardCertifEffDate)) {
											WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
											srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
													EpdsConstants.SPECIALTY_BOARD_CERTIF_EXP_DATE);
											if (srchStr.equals(boardCertifExpDate)) {
												WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
												srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
														EpdsConstants.SPECIALTY_EFF_DATE);
												if (srchStr.equals(spltyEffDate)) {
													WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
													srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
															EpdsConstants.SPECIALTY_TERM_DATE);
													if (srchStr.equals(spltyTermDate)) {
														WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
														srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
																EpdsConstants.SPECIALTY_ADDRESS);
														if (srchStr.equalsIgnoreCase(srchAddress)) {
															WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
															srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
																	EpdsConstants.SPECIALTY_ADDRESS_TYPE);
															if (srchStr.equalsIgnoreCase(strAddressType)) {
																WebWaitHelper
																		.waitForElementPresence(specialtyDetailsTable);
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
				strPaginationDefaultStatus = specialtyPaginatorStatus.getAttribute("class");
				WebHandlers.click(specialtyPaginatorIcon);
			}
			return false;
		}
		LOGGER.info("Matching Specialty detail record is not found.");
		return false;
	}

	public boolean searchProfileSpeciality(Specialty specialty) throws InterruptedException {

		String spltyIndStr = null;
		String strPaginationDefaultStatus = "ui-corner-all";
		String spltyBillingFrom = specialty.getBillingform();
		boolean spltyInd = specialty.getPrimarySpecialtyIndicator();
		if (spltyInd) {
			spltyIndStr = "Yes";
		} else {
			spltyIndStr = "No";
		}
		String spltyCode = specialty.getSpecialtyCode();
		String spltyEffDate = specialty.getSpecialtyActive().getEffectiveDate();
		String spltyTermDate = specialty.getSpecialtyActive().getTerminationDate();
		String spltyTermReason = Objects.toString(specialty.getSpecialtyActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;

		WebWaitHelper.waitForElement(specialtySrchbox);
		specialtySrchbox.click();
		specialtySrchbox.clear();
		try {
			Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		WebHandlers.enterText(specialtySrchbox, spltyCode);
		try {
			Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Select dropdown = new Select(specialtyDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		String srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			WebHandlers.click(specialtyPaginationFirst);

			int rowCount = WebHandlers.getTblRowCount(specialtyDetailsTable);
			LOGGER.info("Specialty table row count after filtering : " + rowCount);
			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(specialtyDetailsTable, i)) {
						WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
						srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
								EpdsConstants.SPECIALTY_PROFILE_BILLINGFROM);
						if (srchStr.equalsIgnoreCase(spltyBillingFrom)) {
							srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
									EpdsConstants.SPECIALTY_PROFILE_PRIMARY_SPL_INDICATOR);
							if (srchStr.equalsIgnoreCase(spltyIndStr)) {
								WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
								srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
										EpdsConstants.SPECIALTY_PROFILE_SPECIALITY);
								if (srchStr.equalsIgnoreCase(spltyCode)) {
									WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
									srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
											EpdsConstants.SPECIALTY_PROFILE_EFF_DATE);
									if (srchStr.equals(spltyEffDate)) {
										WebWaitHelper.waitForElementPresence(specialtyDetailsTable);
										srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
												EpdsConstants.SPECIALTY_PROFILE_TERM_DATE);
										if (srchStr.equals(spltyTermDate) && srchStr.equals(terminationDate)) {
											return true;
										} else {
											if (srchStr.equals(spltyTermDate)) {
												srchStr = WebHandlers.getTblTdVal(specialtyDetailsTable, i,
														EpdsConstants.SPECIALTY_PROFILE_TERM_REASON_CODE);
												if (srchStr.equalsIgnoreCase(spltyTermReason) && srchStr != null) {
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
				strPaginationDefaultStatus = specialtyPaginatorStatus.getAttribute("class");
				WebHandlers.click(specialtyPaginatorIcon);
			}
			return false;
		}
		LOGGER.info("Matching Specialty detail record is not found.");
		return false;
	}
}
