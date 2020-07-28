package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.AlternateIDActive;
import com.beans.mdxjson.NpiDetail;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.RpaTestUtilities.ProviderType;

public class AlternateId {

	public static Logger LOGGER = Logger.getLogger(AlternateId.class);
	public String npiEligibile = null;
	public String npiExcempt = null;
	WebDriver driver;

	@FindBy(xpath = "//*[@id='prvInfo-form:viewNpiType']")
	WebElement eligibilityType;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_update_npi_data\"]")
	WebElement indNpiDetailsTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_npi_data\"]")
	WebElement orgNpiDetailsTable;

	@FindBy(xpath = " //*[@id='prvInfo-form:dtble_update_npi:npiFilter']")
	WebElement npiIndTblFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_npi:npiFilter']")
	WebElement npiOrgTblFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId:alternateIdFilter']")
	WebElement altIdFilterTxt;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId_data']")
	WebElement altIdDetailsTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_altIdQcare:globalFilter\"]")
	WebElement grpAltIdFilterTxt;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:dtble_altIdQcare_data\"]")
	WebElement grpAltIdDetailsTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId_paginator_bottom']/select")
	WebElement indOrgAltDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_altIdQcare_paginator_bottom']/select")
	WebElement grpAltDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId_paginator_bottom']/span[4]/span[1]")
	WebElement indOrgAltPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_altIdQcare_paginator_bottom']/span[4]/span[1]")
	WebElement grpAltPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId_paginator_bottom']/span[5]")
	WebElement indOrgAltPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_altIdQcare_paginator_bottom']/span[5]")
	WebElement grpAltPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_updateAltId_paginator_bottom']/span[5]/span")
	WebElement indOrgAltPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_altIdQcare_paginator_bottom']/span[5]/span")
	WebElement grpAltPaginatorIcon;

	// NPI
	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_update_npi_paginator_bottom']/select")
	WebElement indNpiDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_npi_paginator_bottom']/select")
	WebElement orgNpiDropDown;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_update_npi_paginator_bottom']/span[4]/span[1]")
	WebElement indNpiPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_npi_paginator_bottom']/span[4]/span[1]")
	WebElement orgNpiPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_update_npi_paginator_bottom']/span[5]/span")
	WebElement indNpiPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_npi_paginator_bottom']/span[5]/span")
	WebElement orgNpiPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_update_npi_paginator_bottom']/span[5]")
	WebElement indNpiPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:dtble_npi_paginator_bottom']/span[5]")
	WebElement orgNpiPaginatorStatus;

	public AlternateId(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String getNpiEligibilityType() {
		return (eligibilityType.getText());
	}

	// eligibility type
	public boolean getEligibilityType(ProviderType provType) throws InterruptedException {

		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "enable";

		WebElement npiDetailsTbl = null;
		WebElement npiTblFilter = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			npiDetailsTbl = indNpiDetailsTable;
			npiTblFilter = npiIndTblFilter;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			npiDetailsTbl = orgNpiDetailsTable;
			npiTblFilter = npiOrgTblFilter;
		}

		//WebWaitHelper.waitForElement(npiDetailsTbl);
		WebElement npiFirstPage = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			dropdown = new Select(indNpiDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = indNpiPaginationFirst;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			dropdown = new Select(orgNpiDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = orgNpiPaginationFirst;
		}

		String expData = WebHandlers.getTblTdVal(npiDetailsTbl, 0, 0);
		int rowCount = WebHandlers.getTblRowCount(npiDetailsTbl);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(expData.equals("No records found."))) {
				WebHandlers.click(npiFirstPage);
				if (rowCount != 1)
					rowCount = (WebHandlers.getTblRowCount(npiDetailsTbl) / 2);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(npiDetailsTbl, i)) {
						String actTermDate = WebHandlers.getNPITblVal(npiDetailsTbl, i,
								EpdsConstants.NPI_TERM_REASON_CODE);

						if (actTermDate.equals("")) {
							npiEligibile = "Eligible";
							return true;
						} else {
							npiExcempt = "Exempt";
							continue;
						}
					}
				}
				if (provType.equals(ProviderType.INDIVIDUAL)) {
					strPaginationDefaultStatus = indNpiPaginatorStatus.getAttribute("class");
					WebHandlers.click(indNpiPaginatorIcon);
				} else if (provType.equals(ProviderType.ORGANIZATION)) {
					strPaginationDefaultStatus = orgNpiPaginatorStatus.getAttribute("class");
					WebHandlers.click(orgNpiPaginatorIcon);
				}
				return false;
			}
			LOGGER.info("No Records found");
			return false;
		}
		return false;
	}

	public boolean searchNpiDetails(NpiDetail npiDetail, ProviderType provType) throws InterruptedException {

		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "enable";

		WebElement npiDetailsTbl = null;
		WebElement npiTblFilter = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			npiDetailsTbl = indNpiDetailsTable;
			npiTblFilter = npiIndTblFilter;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			npiDetailsTbl = orgNpiDetailsTable;
			npiTblFilter = npiOrgTblFilter;
		}

		String expNpiType = npiDetail.getNpiType();
		String expNpiValue = npiDetail.getNpiValue();
		String expEffDate = npiDetail.getNpiActive().getEffectiveDate();
		String expTermDate = npiDetail.getNpiActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(npiDetail.getNpiActive().getTerminationReasonCode(), "");

		WebWaitHelper.waitForElement(npiDetailsTbl);
		npiTblFilter.clear();
		npiTblFilter.click();
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		WebElement npiFirstPage = null;
		if (provType.equals(ProviderType.INDIVIDUAL)) {
			dropdown = new Select(indNpiDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = indNpiPaginationFirst;
		} else if (provType.equals(ProviderType.ORGANIZATION)) {
			dropdown = new Select(orgNpiDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = orgNpiPaginationFirst;
		}

		WebHandlers.enterText(npiTblFilter, expNpiType);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		boolean flag = true;
		String expData = WebHandlers.getTblTdVal(npiDetailsTbl, 0, 0);
		int rowCount = WebHandlers.getTblRowCount(npiDetailsTbl);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(expData.equals("No records found."))) {
				WebHandlers.click(npiFirstPage);
				if (rowCount != 1)
					rowCount = (WebHandlers.getTblRowCount(npiDetailsTbl) / 2);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(npiDetailsTbl, i)) {
						String actNpiValue = WebHandlers.getNPITblVal(npiDetailsTbl, i, EpdsConstants.NPI_VALUE);
						String actEffDate = WebHandlers.getNPITblVal(npiDetailsTbl, i, EpdsConstants.NPI_EFF_DATE);
						String actTermDate = WebHandlers.getNPITblVal(npiDetailsTbl, i, EpdsConstants.NPI_TERM_DATE);
						String actReasonCode = WebHandlers.getNPITblVal(npiDetailsTbl, i,
								EpdsConstants.NPI_TERM_REASON_CODE);
						String terminationDate = EpdsConstants.TERMINATION_DATE;
						if (actEffDate.equals(expEffDate) & actEffDate.equals(expTermDate)
								& actReasonCode.equalsIgnoreCase(expTermReasonCode)) {
							LOGGER.info(actNpiValue + " : " + expNpiValue + " || " + actEffDate + " : " + expEffDate
									+ " || " + actTermDate + " : " + expTermDate + " || " + actReasonCode + " : "
									+ expTermReasonCode);
							continue;
						}
						if (actEffDate.equals(expEffDate)
								& (actTermDate.equals(expTermDate) & actReasonCode.equalsIgnoreCase(expTermReasonCode)
										|| expTermDate.equals(terminationDate)
										|| terminationDate.equals(actTermDate))) {
							LOGGER.info(actEffDate + " : " + expEffDate + " || " + actTermDate + " : " + expTermDate);
							continue;
						} else {
							flag = false;
							continue;
						}
					}
				}
				if (provType.equals(ProviderType.INDIVIDUAL)) {
					strPaginationDefaultStatus = indNpiPaginatorStatus.getAttribute("class");
					WebHandlers.click(indNpiPaginatorIcon);
				} else if (provType.equals(ProviderType.ORGANIZATION)) {
					strPaginationDefaultStatus = orgNpiPaginatorStatus.getAttribute("class");
					WebHandlers.click(orgNpiPaginatorIcon);
				}
				return flag;
			}
			LOGGER.info("No records found.");
			return false;
		}
		return false;
	}

	public boolean searchAltIdDetails(AltIdDetail altIdDetail) throws InterruptedException {
		String altIdSrc = altIdDetail.getAltIDSource();
		String altIdType = altIdDetail.getAltIDType();
		String altIdVal = altIdDetail.getAltIDValue();
		String altIdEffDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
		String altIdTermDate = altIdDetail.getAlternateIDActive().getTerminationDate();
		String termReasonCode = Objects.toString(altIdDetail.getAlternateIDActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		Select dropdown = null;
		int selectOptions;
		String strPaginationDefaultStatus = "ui-corner-all";
		WebWaitHelper.waitForElement(altIdFilterTxt);
		/*
		 * altIdFilterTxt.click(); altIdFilterTxt.clear();
		 * Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
		 * WebHandlers.enterText(altIdFilterTxt, altIdVal);
		 */
		WebHandlers.enterText(altIdFilterTxt, altIdVal);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		dropdown = new Select(indOrgAltDropDown);
		selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		String srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, 0, 0);
		if (!(srchStr.equals("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(altIdDetailsTable);
			LOGGER.info("Alt ID details table row count : " + rowCount);
			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					WebHandlers.click(indOrgAltPaginationFirst);
					srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i, EpdsConstants.ALTID_TYPE);
					if (srchStr.contains(altIdType)) {
						srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i, EpdsConstants.ALTID_SRC);
						if (srchStr.contains(altIdSrc)) {
							srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i, EpdsConstants.ALTID_VALUE);
							if (srchStr.contains(altIdVal)) {
								srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i, EpdsConstants.ALTID_EFF_DATE);
								if (srchStr.contains(altIdEffDate)) {
									srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i,
											EpdsConstants.ALTID_TERM_DATE);
									if (srchStr.equals(altIdTermDate) && srchStr.equals(terminationDate)) {
										return true;
									} else {
										if (srchStr.equals(altIdTermDate)) {
											srchStr = WebHandlers.getTblTdVal(altIdDetailsTable, i,
													EpdsConstants.ALTID_TERM_REASON_CODE);
											if (srchStr.equalsIgnoreCase(termReasonCode) && srchStr != null) {
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
				strPaginationDefaultStatus = indOrgAltPaginatorStatus.getAttribute("class");
				WebHandlers.click(indOrgAltPaginatorIcon);
			}
			return false;
		}
		LOGGER.info("Matching Alt Id detail record is not available.");
		return false;
	}

	public AltIdDetail getAltIdDetails(String altIdSrc, String altIdType, ProviderType provType)
			throws InterruptedException {

		WebElement thisAltIdTable = null;
		WebElement thisAltIdFilter = null;
		Select dropdown = null;
		int selectOptions;
		WebElement npiFirstPage = null;
		String strPaginationDefaultStatus = "ui-corner-all";
		if (provType.equals(ProviderType.INDIVIDUAL) || provType.equals(ProviderType.ORGANIZATION)) {
			thisAltIdTable = altIdDetailsTable;
			thisAltIdFilter = altIdFilterTxt;
		} else if (provType.equals(ProviderType.GROUP)) {
			thisAltIdTable = grpAltIdDetailsTable;
			thisAltIdFilter = grpAltIdFilterTxt;
		}

		AltIdDetail altIdDetail = new AltIdDetail();
		AlternateIDActive alternateIDActive = new AlternateIDActive();
		String altIdValue, altIdEffDate, altIdTermDate, altIdTermCode;
		WebWaitHelper.waitForElement(thisAltIdFilter);

		thisAltIdFilter.clear();
		thisAltIdFilter.click();

		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
		WebHandlers.enterText(thisAltIdFilter, altIdType);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		if (provType.equals(ProviderType.INDIVIDUAL) || provType.equals(ProviderType.ORGANIZATION)) {
			dropdown = new Select(indOrgAltDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = indOrgAltPaginationFirst;

		} else if (provType.equals(ProviderType.GROUP)) {
			dropdown = new Select(grpAltDropDown);
			selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
			npiFirstPage = grpAltPaginationFirst;
		}
		String srchStr = WebHandlers.getTblTdVal(thisAltIdTable, 0, 0);
		if (!(srchStr.equals("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(thisAltIdTable);
			LOGGER.info("Alt ID details table row count : " + rowCount);

			while (!strPaginationDefaultStatus.contains("disabled")) {
				for (int i = 0; i < rowCount; i++) {
					WebHandlers.click(npiFirstPage);
					if (!WebHandlers.isGreyColorGrid(grpAltIdDetailsTable, i)) {
						srchStr = WebHandlers.getTblTdVal(thisAltIdTable, i, EpdsConstants.ALTID_TYPE);
						if (srchStr.equalsIgnoreCase(altIdType)) {
							srchStr = WebHandlers.getTblTdVal(thisAltIdTable, i, EpdsConstants.ALTID_SRC);
							if (srchStr.contains(altIdSrc)) {
								altIdValue = WebHandlers.getTblTdVal(thisAltIdTable, i, EpdsConstants.ALTID_VALUE);
								altIdEffDate = WebHandlers.getTblTdVal(thisAltIdTable, i, EpdsConstants.ALTID_EFF_DATE);
								altIdTermDate = WebHandlers.getTblTdVal(thisAltIdTable, i,
										EpdsConstants.ALTID_TERM_DATE);
								altIdTermCode = WebHandlers.getTblTdVal(thisAltIdTable, i,
										EpdsConstants.ALTID_TERM_REASON_CODE);
								altIdDetail.setAltIDValue(altIdValue);
								alternateIDActive.setEffectiveDate(altIdEffDate);
								alternateIDActive.setTerminationDate(altIdTermDate);
								alternateIDActive.setTerminationReasonCode(altIdTermCode);
								altIdDetail.setAlternateIDActive(alternateIDActive);
								return altIdDetail;
							}
						}
					}
				}
				if (provType.equals(ProviderType.INDIVIDUAL) || provType.equals(ProviderType.ORGANIZATION)) {
					strPaginationDefaultStatus = indOrgAltPaginatorStatus.getAttribute("class");
					WebHandlers.click(indOrgAltPaginatorIcon);
				} else if (provType.equals(ProviderType.GROUP)) {
					strPaginationDefaultStatus = grpAltPaginatorStatus.getAttribute("class");
					WebHandlers.click(grpAltPaginatorIcon);
				}
			}
			return null;
		}
		LOGGER.info("Matching Alt ID record is not available.");
		return null;
	}

	public boolean searchGrpAltIdDetails(AltIdDetail altIdDetail) {

		String strPaginationDefaultStatus = "ui-corner-all";
		String expAltIDType = altIdDetail.getAltIDType();
		String expAltIDSource = altIdDetail.getAltIDSource();
		String expAltIDValue = altIdDetail.getAltIDValue();
		String expEffectiveDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
		String expTerminationDate = altIdDetail.getAlternateIDActive().getTerminationDate();
		String expTerminationReasonCode = Objects.toString(altIdDetail.getAlternateIDActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String srchStr = null;
		WebWaitHelper.waitForElement(grpAltIdDetailsTable);
		srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, 0, 0);
		int rowCount = WebHandlers.getTblRowCount(grpAltIdDetailsTable);

		Select dropdown = new Select(grpAltDropDown);
		int selectOptions = dropdown.getOptions().size();
		dropdown.selectByIndex(selectOptions - 1);

		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!srchStr.equalsIgnoreCase("No records found.")) {
				WebHandlers.click(grpAltPaginationFirst);
				LOGGER.info("Alt ID details table rowCount: " + rowCount);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(grpAltIdDetailsTable, i)) {
						srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i, EpdsConstants.ALTID_TYPE);
						if (srchStr.equalsIgnoreCase(expAltIDType)) {
							srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i, EpdsConstants.ALTID_SRC);
							if (srchStr.equalsIgnoreCase(expAltIDSource)) {
								srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i, EpdsConstants.ALTID_VALUE);
								if (srchStr.equalsIgnoreCase(expAltIDValue)) {
									srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i,
											EpdsConstants.ALTID_EFF_DATE);
									if (srchStr.equals(expEffectiveDate)) {
										srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i,
												EpdsConstants.ALTID_TERM_DATE);
										if (srchStr.equals(expTerminationDate) && srchStr.equals(terminationDate)) {
											return true;
										} else {
											if (srchStr.equalsIgnoreCase(expTerminationDate)) {
												srchStr = WebHandlers.getTblTdVal(grpAltIdDetailsTable, i,
														EpdsConstants.ALTID_TERM_REASON_CODE);
												if (srchStr.equalsIgnoreCase(expTerminationReasonCode) && srchStr != null) {
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
				strPaginationDefaultStatus = grpAltPaginatorStatus.getAttribute("class");
				WebHandlers.click(grpAltPaginatorIcon);
			}
			LOGGER.info("No Alt ID record found");
			return false;
		}
		return false;
	}
}
//	public boolean searchIndNpiDetails(NpiDetail npiDetail) {
//
//		String expNpiType = npiDetail.getNpiType();
//		String expNpiValue = npiDetail.getNpiValue();
//		String expEffDate = npiDetail.getNpiActive().getEffectiveDate();
//		String expTermDate = npiDetail.getNpiActive().getTerminationDate();
//		String expTermReasonCode = npiDetail.getNpiActive().getTerminationReasonCode();
//		String terminationDate = EpdsConstants.TERMINATION_DATE;
//
//		WebWaitHelper.waitForElement(indNpiFilter);
//		indNpiFilter.click();
//		indNpiFilter.clear();
//		WebHandlers.enterText(indNpiFilter, expNpiType);
//		try {
//			Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		String srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, 0, 0);
//		if (!(srchStr.equals("No records found."))) {
//			int rowCount = WebHandlers.getTblRowCount(indNpiDetailsTable);
//			LOGGER.info("NPI details table row count : " + rowCount);
//
//			for (int i = 0; i < rowCount; i++) {
//				srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, i, EpdsConstants.NPI_TYPE);
//				if (srchStr.contains(expNpiType)) {
//					srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, i, EpdsConstants.NPI_VALUE);
//					if (srchStr.equals(expNpiValue)) {
//						srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, i, EpdsConstants.IND_NPI_EFF_DATE);
//						if (srchStr.equals(expEffDate)) {
//							srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, i, EpdsConstants.IND_NPI_TERM_DATE);
//							if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
//								return true;
//							} else {
//								if (srchStr.equals(expTermDate)) {
//									srchStr = WebHandlers.getTblTdVal(indNpiDetailsTable, i,
//											EpdsConstants.IND_NPI_TERM_REASON_CODE);
//									if (srchStr.equals(expTermReasonCode) && srchStr != null) {
//										return true;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		LOGGER.info("Matching NPI detail record is not available.");
//		return false;
//	}

//	public boolean searchOrgNpiDetails(NpiDetail npiDetail) {
//
//		String expNpiType = npiDetail.getNpiType();
//		String expNpiValue = npiDetail.getNpiValue();
//		String expEffDate = npiDetail.getNpiActive().getEffectiveDate();
//		String expTermDate = npiDetail.getNpiActive().getTerminationDate();
//		String expTermReasonCode = npiDetail.getNpiActive().getTerminationReasonCode();
//		String terminationDate = EpdsConstants.TERMINATION_DATE;
//
//		WebWaitHelper.waitForElement(orgNpiFilter);
//		orgNpiFilter.click();
//		orgNpiFilter.clear();
//		try {
//			Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		WebHandlers.enterText(orgNpiFilter, expNpiType);
//		try {
//			Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		String srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, 0, 0);
//		if (!(srchStr.equals("No records found."))) {
//			int rowCount = WebHandlers.getTblRowCount(orgNpiDetailsTable);
//			LOGGER.info("NPI details table row count : " + rowCount);
//
//			for (int i = 0; i < rowCount; i++) {
//				srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, i, EpdsConstants.NPI_TYPE);
//				if (srchStr.contains(expNpiType) && srchStr != "") {
//					srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, i, EpdsConstants.NPI_VALUE);
//					if ((srchStr.equals(expNpiValue)) && srchStr != "") {
//						srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, i, EpdsConstants.ORG_NPI_EFF_DATE);
//						if ((srchStr.equals(expEffDate)) && expEffDate != "") {
//							srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, i, EpdsConstants.ORG_NPI_TERM_DATE);
//							if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
//								return true;
//							} else {
//								if (srchStr.equals(expTermDate)) {
//									srchStr = WebHandlers.getTblTdVal(orgNpiDetailsTable, i,
//											EpdsConstants.ORG_NPI_TERM_REASON_CODE);
//									if (srchStr.equals(expTermReasonCode) && srchStr != null) {
//										return true;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		LOGGER.info("Matching NPI detail record is not available.");
//		return false;
//	}
