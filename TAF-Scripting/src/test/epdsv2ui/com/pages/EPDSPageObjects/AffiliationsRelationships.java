package com.pages.EPDSPageObjects;

import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AffliatedTaxID;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;

public class AffiliationsRelationships {

	public static Logger LOGGER = Logger.getLogger(AffiliationsRelationships.class);

	WebDriver driver;

	@FindBy(xpath = "//input[@id='prvInfo-form:indAffiliationInd:tab0:globalFilter']")
	WebElement searchAffilTxtbox;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab0']/table/tbody")
	WebElement affiliationsTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:headerView']/following-sibling::div/div/ul/li[3]/a")
	WebElement groupingRelationsTab;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab2_data']")
	WebElement groupingRelationsTable;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:indAffiliationInd:tab2:globalFilter\"]")
	WebElement grpRelationsFilter;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:affiliations\"]")
	WebElement affiliationTab;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:providerrel\"]")
	WebElement providerTab;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabAffiliations:globalFilter']")
	WebElement grpAffFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabProvider:globalFilter']")
	WebElement provFilter;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabAffiliations_data']")
	WebElement groupingAffliationTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabProvider_data']")
	WebElement groupingProviderTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab0_paginator_bottom']/span[4]/span[1]")
	WebElement indAffiPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab0_paginator_bottom']/span[5]")
	WebElement indAffiPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab0_paginator_bottom']/span[5]/span")
	WebElement indAffiPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab2_paginator_bottom']/span[4]/span[1]")
	WebElement indGrpAffiPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab2_paginator_bottom']/span[5]")
	WebElement indGrpAffiPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:indAffiliationInd:tab2_paginator_bottom']/span[5]/span")
	WebElement indGrpAffiPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabAffiliations_paginator_bottom']/span[4]/span[1]")
	WebElement grpAffiPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabAffiliations_paginator_bottom']/span[5]")
	WebElement grpAffiPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabAffiliations_paginator_bottom']/span[5]/span")
	WebElement grpAffiPaginatorIcon;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabProvider_paginator_bottom']/span[4]/span[1]")
	WebElement grpProvPaginationFirst;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabProvider_paginator_bottom']/span[5]")
	WebElement grpProvPaginatorStatus;

	@FindBy(xpath = "//*[@id='prvInfo-form:tabProvider_paginator_bottom']/span[5]/span")
	WebElement grpProvPaginatorIcon;

	public AffiliationsRelationships(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void searchAffilEid(String providerEId) {
		WebHandlers.enterText(searchAffilTxtbox, providerEId);
	}

	public void selectGrpRelationsTab() {
		WebHandlers.click(groupingRelationsTab);
	}

	public void navToProvTab() {
		WebHandlers.click(providerTab);
	}

	public boolean searchIndAffiliation(Affiliation affiliation, Address address) throws InterruptedException {

		String expAffilLegacyID = affiliation.getAffiliatedLegacyID();
		String expAffilType = affiliation.getAffiliationType();
		String strPaginationDefaultStatus = "ui-corner-all";
		String addressLine1 = address.getAddressDetails().getAddressLine1();
		String city = address.getAddressDetails().getCity();
		String state = address.getAddressDetails().getState();
		String zipCode = address.getAddressDetails().getZip();
		String expEffDate = address.getAddressActive().getEffectiveDate();
		String expTermDate = address.getAddressActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(address.getAddressActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String expAddress = (addressLine1 + " ," + city + "," + state + "," + zipCode).replaceAll("\\s+", "");
		LOGGER.info("Searching for Address : " + expAddress);
		// WebHandlers.enterText(searchAffilTxtbox, addressLine1);

		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript("document.getElementById('prvInfo-form:indAffiliationInd:tab0:globalFilter').value='"
				+ addressLine1 + "'");

		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);
		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(affiliationsTable.getText().equals(""))) {
				WebHandlers.click(indAffiPaginationFirst);
				int affilTableRowCount = WebHandlers.getTblRowCount(affiliationsTable);
				for (int i = 0; i < affilTableRowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(affiliationsTable, i)) {
						String srchStr = WebHandlers.getTblTdVal(affiliationsTable, i, EpdsConstants.AFFIL_LEGACY_ID);
						if (srchStr.equals(expAffilLegacyID)) {
							srchStr = WebHandlers.getTblTdVal(affiliationsTable, i, EpdsConstants.AFFIL_TYPE);
							if (srchStr.equalsIgnoreCase(expAffilType)) {
								srchStr = (WebHandlers.getTblTdVal(affiliationsTable, i,
										EpdsConstants.AFFIL_PHY_ADDRESS)).replaceAll("\\s+", "");
								if (srchStr.equalsIgnoreCase(expAddress)) {
									srchStr = WebHandlers.getTblTdVal(affiliationsTable, i,
											EpdsConstants.AFFIL_EFF_DATE);
									if (srchStr.equals(expEffDate)) {
										srchStr = WebHandlers.getTblTdVal(affiliationsTable, i,
												EpdsConstants.AFFIL_TERM_DATE);
										if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
											return true;
										} else {
											if (srchStr.equals(expTermDate)) {
												srchStr = WebHandlers.getTblTdVal(affiliationsTable, i,
														EpdsConstants.AFFIL_TERM_REASON_CODE);
												if (srchStr.equalsIgnoreCase(expTermReasonCode) && srchStr != null) {
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
				strPaginationDefaultStatus = indAffiPaginatorStatus.getAttribute("class");
				WebHandlers.click(indAffiPaginatorIcon);
			}
			LOGGER.info("Matching Network record is not available.");
			return false;
		}
		return false;
	}

	public boolean searchGroupingRelations(Affiliation affilition) throws InterruptedException {

		String expReleationshipType = affilition.getAffiliationType();
		String expGroupingCode = affilition.getGroupingCode();
		String expEffDate = affilition.getAffiliationActive().getEffectiveDate();
		String expTermDate = affilition.getAffiliationActive().getTerminationDate();
		String termReasonCode = Objects.toString(affilition.getAffiliationActive().getTerminationReasonCode(), "");
		String strPaginationDefaultStatus = "ui-corner-all";
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String srchStr = null;

		List<Address> addresses = affilition.getAddresses();
		for (Address address : addresses) {
			String addressLine1 = address.getAddressDetails().getAddressLine1();
			String city = address.getAddressDetails().getCity();
			String state = address.getAddressDetails().getState();
			String zipCode = address.getAddressDetails().getZip();
			String expAddress = (addressLine1 + " ," + city + ", " + state + ", " + zipCode).replaceAll("\\s+", "");
			LOGGER.info("Searching for Address : " + expAddress);
			WebHandlers.enterText(grpRelationsFilter, expGroupingCode);
			Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

			while (!strPaginationDefaultStatus.contains("disabled")) {
				if (!(groupingRelationsTable.getText().equals(""))) {
					int affilTableRowCount = WebHandlers.getTblRowCount(groupingRelationsTable);
					LOGGER.info("Affiliation table row count : " + affilTableRowCount);
					WebHandlers.click(indGrpAffiPaginationFirst);
					for (int i = 0; i < affilTableRowCount; i++) {
						if (!WebHandlers.isGreyColorGrid(affiliationsTable, i)) {
							srchStr = WebHandlers.getTblTdVal(groupingRelationsTable, i,
									EpdsConstants.AFFIL_GRPREL_REL_TYPE);
							if (srchStr.equalsIgnoreCase(expReleationshipType)) {
								srchStr = WebHandlers.getTblTdVal(groupingRelationsTable, i,
										EpdsConstants.AFFIL_GRPREL_GRP_CODE);
								if (srchStr.equalsIgnoreCase(expGroupingCode)) {
									srchStr = (WebHandlers.getTblTdVal(groupingRelationsTable, i,
											EpdsConstants.AFFIL_GRPREL_PRAC_ADDRESS)).replaceAll("\\s", "");
									if (srchStr.equalsIgnoreCase(expAddress)) {
										srchStr = WebHandlers.getTblTdVal(groupingRelationsTable, i,
												EpdsConstants.AFFIL_GRPREL_EFF_DATE);
										if (srchStr.equals(expEffDate)) {
											srchStr = WebHandlers.getTblTdVal(groupingRelationsTable, i,
													EpdsConstants.AFFIL_GRPREL_TERM_DATE);
											if (srchStr.equals(expTermDate) && srchStr.equals(terminationDate)) {
												return true;
											} else {
												if (srchStr.equals(expTermDate)) {
													srchStr = WebHandlers.getTblTdVal(groupingRelationsTable, i,
															EpdsConstants.AFFIL_GRPREL_TERM_REASON_CODE);
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
					}
					strPaginationDefaultStatus = indGrpAffiPaginatorStatus.getAttribute("class");
					WebHandlers.click(indGrpAffiPaginatorIcon);
				}
				LOGGER.info("Matching Network record is not available.");
				return false;
			}
			return false;
		}
		return false;
	}

	public boolean selectAffliationDetails(String groupName, Affiliation affiliation, AffliatedTaxID affTax,
			Address affAddr) throws InterruptedException {

		String strPaginationDefaultStatus = "ui-corner-all";
		String expAffType = affiliation.getAffiliationType();
		String expEffDate = affiliation.getAffiliationActive().getEffectiveDate();
		String expTermDate = affiliation.getAffiliationActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(affiliation.getAffiliationActive().getTerminationReasonCode(), "");
		String expAffTax = affTax.getTaxIDValue();
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String acctualData = null;
		String addrLine1 = affAddr.getAddressDetails().getAddressLine1();
		String srchAddress = (addrLine1 + " " + affAddr.getAddressDetails().getCity() + " "
				+ affAddr.getAddressDetails().getState() + " " + affAddr.getAddressDetails().getZip())
						.replaceAll("\\s+", "");
		WebWaitHelper.waitForElementPresence(groupingAffliationTable);
		WebWaitHelper.waitForElementPresence(grpAffFilter);
		WebHandlers.enterText(grpAffFilter, expAffTax);
		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);

		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(groupingAffliationTable.getText().equals(""))) {
				int rowCount = WebHandlers.getTblRowCount(groupingAffliationTable);
				WebHandlers.click(grpAffiPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(grpAffiPaginationFirst, i)) {
						WebWaitHelper.waitForElementPresence(groupingAffliationTable);
						acctualData = (WebHandlers.getTblTdVal(groupingAffliationTable, i,
								EpdsConstants.AFFIL_PROV_NAME)).replaceAll("\\s+", "");
						if (acctualData.equalsIgnoreCase(groupName)) {
							acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
									EpdsConstants.AFFIL_GRP_CODE);
							if (acctualData.equalsIgnoreCase(expAffType)) {
								acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
										EpdsConstants.AFFIL_TAX_ID);
								if (acctualData.equals(expAffTax)) {
									acctualData = (WebHandlers.getTblTdVal(groupingAffliationTable, i,
											EpdsConstants.AFFIL_ADDRESS)).replaceAll("\\s+", "");
									if (acctualData.contains(srchAddress)) {
										acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
												EpdsConstants.AFFIL_EFFECTIVE_DATE);
										if (acctualData.equals(expEffDate)) {
											acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
													EpdsConstants.AFFIL_TERMINATION_DATE);
											if (acctualData.equals(expTermDate)
													&& acctualData.equals(terminationDate)) {
												return true;
											} else {
												if (acctualData.equals(expTermDate)) {
													acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
															EpdsConstants.AFFIL_TERMINATION_CODE);
													if (acctualData.equalsIgnoreCase(expTermReasonCode)
															&& acctualData != null) {
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
				strPaginationDefaultStatus = indGrpAffiPaginatorStatus.getAttribute("class");
				WebHandlers.click(indGrpAffiPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		return false;
	}

	public boolean selectAffliationPHODetails(String groupName, Affiliation affiliation, AffliatedTaxID affTax)
			throws InterruptedException {

		String expAffType = affiliation.getAffiliationType();
		String expEffDate = affiliation.getAffiliationActive().getEffectiveDate();
		String expTermDate = affiliation.getAffiliationActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(affiliation.getAffiliationActive().getTerminationReasonCode(), "");
		String expAffTax = affTax.getTaxIDValue();
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String acctualData = null;
		String strPaginationDefaultStatus = "ui-corner-all";
		WebWaitHelper.waitForElementPresence(groupingAffliationTable);
		WebWaitHelper.waitForElementPresence(grpAffFilter);
		WebHandlers.enterText(grpAffFilter, expAffTax);
		Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);

		while (!strPaginationDefaultStatus.contains("disabled")) {
			if (!(groupingAffliationTable.getText().equals(""))) {
				int rowCount = WebHandlers.getTblRowCount(groupingAffliationTable);
				WebHandlers.click(indGrpAffiPaginationFirst);
				for (int i = 0; i < rowCount; i++) {
					if (!WebHandlers.isGreyColorGrid(affiliationsTable, i)) {
						WebWaitHelper.waitForElementPresence(groupingAffliationTable);
						acctualData = (WebHandlers.getTblTdVal(groupingAffliationTable, i,
								EpdsConstants.AFFIL_PROV_NAME)).replaceAll("\\s+", "");
						;
						if (acctualData.equalsIgnoreCase(groupName)) {
							acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
									EpdsConstants.AFFIL_GRP_CODE);
							if (acctualData.equalsIgnoreCase(expAffType)) {
								acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
										EpdsConstants.AFFIL_TAX_ID);
								if (acctualData.equals(expAffTax)) {
									acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
											EpdsConstants.AFFIL_EFFECTIVE_DATE);
									if (acctualData.equals(expEffDate)) {
										acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
												EpdsConstants.AFFIL_TERMINATION_DATE);
										if (acctualData.equals(expTermDate) && acctualData.equals(terminationDate)) {
											return true;
										} else {
											if (acctualData.equals(expTermDate)) {
												acctualData = WebHandlers.getTblTdVal(groupingAffliationTable, i,
														EpdsConstants.AFFIL_TERMINATION_CODE);
												if (acctualData.equalsIgnoreCase(expTermReasonCode)
														&& acctualData != null) {
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
				strPaginationDefaultStatus = indGrpAffiPaginatorStatus.getAttribute("class");
				WebHandlers.click(indGrpAffiPaginatorIcon);
			}
			LOGGER.info("Matching network record is not found.");
			return false;
		}
		return false;
	}

	public boolean selectProviderDetails(String groupName, Affiliation affiliation) throws InterruptedException {

		String expAffType = (affiliation.getAffiliationType()).replaceAll("\\s+", "");
		String expEffDate = affiliation.getAffiliationActive().getEffectiveDate();
		String expTermDate = affiliation.getAffiliationActive().getTerminationDate();
		String expTermReasonCode = Objects.toString(affiliation.getAffiliationActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String acctualData = null;
		String strSrch;
		String strPaginationDefaultStatus = "ui-corner-all";
		String expPCP = affiliation.getPcpID();
		if (expPCP == "") {
			strSrch = groupName;
		} else {
			strSrch = expPCP;
		}

		List<Address> addresses = affiliation.getAddresses();
		for (Address address : addresses) {
			String addressLine1 = address.getAddressDetails().getAddressLine1();
			String city = address.getAddressDetails().getCity();
			String state = address.getAddressDetails().getState();
			String zipCode = address.getAddressDetails().getZip();
			String srchAddress = ((addressLine1 + "" + city + "" + state + "" + zipCode).replaceAll("\\s+", ""))
					.toUpperCase();
			LOGGER.info("Searching for Address : " + srchAddress);
			WebWaitHelper.waitForElementPresence(groupingProviderTable);
			WebWaitHelper.waitForElementPresence(provFilter);
			// WebHandlers.enterText(provFilter, strSrch);
			Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);

			while (!strPaginationDefaultStatus.contains("disabled")) {
				if (!(groupingProviderTable.getText().equals(""))) {
					int rowCount = WebHandlers.getTblRowCount(groupingProviderTable);
					WebHandlers.click(grpProvPaginationFirst);
					for (int i = 0; i < rowCount; i++) {
						if (!WebHandlers.isGreyColorGrid(groupingProviderTable, i)) {
							WebWaitHelper.waitForElementPresence(groupingProviderTable);
							acctualData = (WebHandlers.getTblTdVal(groupingProviderTable, i, EpdsConstants.PROV_NAME))
									.replaceAll("\\s+", "");
							if (acctualData.equalsIgnoreCase(groupName)) {
								acctualData = (WebHandlers.getTblTdVal(groupingProviderTable, i,
										EpdsConstants.PROV_CODE)).replaceAll("\\s+", "");
								if (acctualData.equalsIgnoreCase(expAffType)) {
									acctualData = WebHandlers.getTblTdVal(groupingProviderTable, i,
											EpdsConstants.PROV_PCP_ID);
									if (acctualData.equalsIgnoreCase(strSrch)) {
										acctualData = (WebHandlers.getTblTdVal(groupingProviderTable, i,
												EpdsConstants.PROV_ADDRESS)).replaceAll("\\s+", "");
										if (acctualData.contains(srchAddress)) {
											acctualData = WebHandlers.getTblTdVal(groupingProviderTable, i,
													EpdsConstants.PROV_EFFECTIVE_DATE);
											if (acctualData.equals(expEffDate)) {
												acctualData = WebHandlers.getTblTdVal(groupingProviderTable, i,
														EpdsConstants.PROV_TERMINATION_DATE);
												if (acctualData.equals(expTermDate)
														&& acctualData.equals(terminationDate)) {
													return true;
												} else {
													if (acctualData.equals(expTermDate)) {
														acctualData = WebHandlers.getTblTdVal(groupingProviderTable, i,
																EpdsConstants.PROV_TERMINATION_CODE);
														if (acctualData.equalsIgnoreCase(expTermReasonCode)
																&& acctualData != null) {
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
					strPaginationDefaultStatus = grpProvPaginatorStatus.getAttribute("class");
					WebHandlers.click(grpProvPaginatorIcon);
				} else {
					LOGGER.info("Matching network record is not found.");
				}
			}
		}
		return false;
	}

	/*
	 * public boolean searchOrgAffiliation(String expRelationType, String
	 * expPhysicalAddr) {
	 * 
	 * String srchStr = WebHandlers.getTblTdVal(affiliationsTable, 0, 0); if
	 * (!srchStr.equalsIgnoreCase("No records found.")) { int affilTableRowCount =
	 * WebHandlers.getTblRowCount(affiliationsTable);
	 * LOGGER.info("Affiliation table row count : " + affilTableRowCount);
	 * 
	 * for (int i = 0; i < affilTableRowCount; i++) { srchStr =
	 * WebHandlers.getTblTdVal(affiliationsTable, i,
	 * EpdsConstants.AFFIL_ORG_RELATION_TYPE); if
	 * (srchStr.contains(expRelationType)) { srchStr =
	 * WebHandlers.getTblTdVal(affiliationsTable, i,
	 * EpdsConstants.AFFIL_ORG_PHYSICAL_ADDR); if
	 * (srchStr.contains(expPhysicalAddr)) return true; } } }
	 * LOGGER.info("Matching Affiliation detail is not available."); return false; }
	 */

}
