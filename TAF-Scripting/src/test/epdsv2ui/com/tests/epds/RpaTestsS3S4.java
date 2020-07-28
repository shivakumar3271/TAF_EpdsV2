package com.tests.epds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.AcceptingPatientsInd;
import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.ClaimActionSanction;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.TimelyFilingInd;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AffiliationsRelationships;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.ClaimActionSanctionPage;
import com.pages.EPDSPageObjects.LegacyInfo;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.pages.EPDSPageObjects.ReimbusermentNetworks;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class RpaTestsS3S4 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaTestsS3S4.class);

	@Test(description = "SPDS-132988 - Individual - Term and cascade practice address", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132988", author = "Reshmi C", transactionId = "IND_TERM_CASCADE_PRACTICE_ADDR", sprint = "Sprint 3")
	public void IND_TERM_CASCADE_PRACTICE_ADDR() {

		LOGGER.info("Inside test IND_TERM_CASCADE_PRACTICE_ADDR");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String expAltId = "Q/Care License ID";
		String expAltSource = "QCARE";
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AddressPage addressPage = new AddressPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					for (Address address : json.getAddresses()) {

						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectIndAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available" : "";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						String expAddrTermDate = address.getAddressActive().getTerminationDate();
						String expTermReasonCode = address.getAddressActive().getTerminationReasonCode();
						if (expTermReasonCode == null)
							expTermReasonCode = "";

						String addrTermDate = addressPage.getAddrTermDate();
						String termReasonCode = addressPage.getAddrTermReasonCode();

						result = addrTermDate.equals(expAddrTermDate) ? PASSED : FAILED;
						resultSet.add(new ResultTuple("Address Term Date", addrTermDate, expAddrTermDate, result, ""));

						result = termReasonCode.equals(expTermReasonCode) ? PASSED : FAILED;
						resultSet.add(new ResultTuple("Address Term Reason Code", termReasonCode, expTermReasonCode,
								result, ""));

						addressPage.navToAltIdsTab();
						result = addressPage.valAltIdTypeSource(expAltId, expAltSource) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Alt ID record is not available" : "";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltSource, result, comment));
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133010 - Edit Organization - Edit/Term Alternate ID", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133010", author = "Shiva", transactionId = "ORG_EDIT_TERM_ALTERNATE_ID", sprint = "Sprint 4")
	public void ORG_EDIT_TERM_ALTERNATE_ID() {

		LOGGER.info("Inside test ORG_EDIT_TERM_ALTERNATE_ID");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AlternateId altIdPage = new AlternateId(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Organization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Organization provider is present...");

					List<AltIdDetail> altIdDetails = json.getAlternateIDs().getAltIdDetails();
					if (altIdDetails != null) {

						loginPage.navToAltIdTab();
						for (AltIdDetail altIdDetail : altIdDetails) {
							result = altIdPage.searchAltIdDetails(altIdDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Organization PDM Indicators record is not available"
									: "";

							resultSet.add(new ResultTuple("Alt ID", "",
									altIdDetail.getAltIDType() + " : " + altIdDetail.getAltIDSource() + " : "
											+ altIdDetail.getAltIDValue() + " : "
											+ altIdDetail.getAlternateIDActive().getEffectiveDate() + " ; "
											+ altIdDetail.getAlternateIDActive().getTerminationDate() + " ; "
											+ altIdDetail.getAlternateIDActive().getTerminationReasonCode(),
									result, comment));
							if (result == FAILED)
								continue;
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133023 - Edit Organization - Edit/Add/Terminate NPI Details", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "PDS-133023", author = "Murali", transactionId = "ORG_EDIT_ADD_TERM_NPI_DETAILS", sprint = "Sprint 3")

	public void ORG_EDIT_ADD_TERM_NPI_DETAILS() {

		LOGGER.info("Inside test ORG_EDIT_ADD_TERM_NPI_DETAILS");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AlternateId altIdPage = new AlternateId(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("validating NPI details in Alternate ID page...");

					/******* NPI Details */
					loginPage.navToAltIdTab();
					List<NpiDetail> npiDetails = json.getAlternateIDs().getNpiDetails();
					if (npiDetails != null) {
						String actEgilibilityType = altIdPage.getNpiEligibilityType();

						result = altIdPage.getEligibilityType(ProviderType.ORGANIZATION) ? PASSED : FAILED;
						String expEgilibilityType = altIdPage.npiEligibile;
						String expEgilibilityType1 = altIdPage.npiExcempt;
						if (result.equalsIgnoreCase(PASSED) && expEgilibilityType.equalsIgnoreCase("Eligible")
								&& actEgilibilityType.equalsIgnoreCase("Eligible")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
									PASSED, " Eligibility Type : Eligible"));
						} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType1.equalsIgnoreCase("Exempt")
								&& actEgilibilityType.equalsIgnoreCase("Exempt")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType1,
									PASSED, " Eligibility Type : Exempt"));
						} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType1.equalsIgnoreCase("Exempt")
								&& actEgilibilityType.equalsIgnoreCase("Eligible")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
									FAILED, " Eligibility Type not matching"));
						} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType.equalsIgnoreCase("Eligible")
								&& actEgilibilityType.equalsIgnoreCase("Exempt")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
									FAILED, " Eligibility Type not matching"));
						}

						for (NpiDetail npiDetail : npiDetails) {
							/*
							 * String expEgilibilityType = npiDetail.getNpiEligibilityType(); String
							 * actEgilibilityType = altIdPage.getNpiEligibilityType(); if
							 * (!actEgilibilityType.equalsIgnoreCase(expEgilibilityType)) {
							 * resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType,
							 * expEgilibilityType, FAILED, "Addition failed")); }
							 */

							result = altIdPage.searchNpiDetails(npiDetail, ProviderType.ORGANIZATION) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "NPI record is not available" : "";
							resultSet.add(new ResultTuple("NPI details", "",
									npiDetail.getNpiType() + " " + npiDetail.getNpiValue() + " "
											+ npiDetail.getNpiActive().getEffectiveDate() + " "
											+ npiDetail.getNpiActive().getTerminationDate() + " "
											+ npiDetail.getNpiActive().getTerminationReasonCode(),
									result, comment));
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS - 133009 - Edit Organization - Add Alternate ID", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133009", author = "Murali", transactionId = "ORG_ADD_ALT_ID", sprint = "Sprint 4")
	public void ORG_ADD_ALT_ID() {

		LOGGER.info("Inside test ORG_ADD_ALT_ID");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AlternateId altIdPage = new AlternateId(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Orgnization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}

					LOGGER.info("Validating addition of ALT ID details ...");
					List<AltIdDetail> altIdDetails = json.getAlternateIDs().getAltIdDetails();
					if (altIdDetails != null) {

						loginPage.navToAltIdTab();
						for (AltIdDetail altIdDetail : altIdDetails) {

							result = altIdPage.searchAltIdDetails(altIdDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "ALT record is not available" : "";
							resultSet.add(new ResultTuple("ALT details", "",
									altIdDetail.getAltIDSource() + " : " + altIdDetail.getAltIDType() + " : "
											+ altIdDetail.getAltIDValue() + " : "
											+ altIdDetail.getAlternateIDActive().getEffectiveDate() + " : "
											+ altIdDetail.getAlternateIDActive().getTerminationDate() + " : "
											+ altIdDetail.getAlternateIDActive().getTerminationReasonCode(),
									result, comment));
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133011 - Edit Organization - Add Claim Action-Sanction", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133011", author = "Murali", transactionId = "ORG_ADD_CLAIM_ACTION_SANCTION", sprint = "Sprint 4")
	public void ORG_ADD_CLAIM_ACTION_SANCTION() {

		LOGGER.info("Inside test ORG_ADD_CLAIM_ACTION_SANCTION");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ClaimActionSanctionPage claimActnSnctnPage = new ClaimActionSanctionPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Orgnization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}

					LOGGER.info("validating addition of Claim Action details ...");
					List<ClaimActionSanction> claimActionDetails = json.getClaimActionSanctions();
					if (claimActionDetails != null) {

						loginPage.navToClaimActSanctTab();
						for (ClaimActionSanction claimDetail : claimActionDetails) {

							result = claimActnSnctnPage.searchClaimActions(claimDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "ClaimActions record is not available" : "";
							resultSet.add(new ResultTuple("Claim Action-Sanction", "",
									claimDetail.getClaimActionSanctionType() + " ; "
											+ claimDetail.getClaimActionSanctionValue() + " : "
											+ claimDetail.getClaimActionSanctionCriteria() + " : "
											+ claimDetail.getLowRange() + " : " + claimDetail.getHighRange() + " : "
											+ claimDetail.getClaimActionSanctionActive().getEffectiveDate() + " : "
											+ claimDetail.getClaimActionSanctionActive().getTerminationDate() + " : "
											+ claimDetail.getClaimActionSanctionActive().getTerminationReasonCode(),
									result, comment));
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133012 - Edit Organization - Edit/Term Claim Action-Sanction", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133012", author = "Murali", transactionId = "ORG_EDIT_TERM_CLAIM_ACTION_SANCTION", sprint = "Sprint 4")

	public void ORG_EDIT_TERM_CLAIM_ACTION_SANCTION() {

		LOGGER.info("Inside test ORG_EDIT_TERM_CLAIM_ACTION_SANCTION");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ClaimActionSanctionPage claimActnSnctnPage = new ClaimActionSanctionPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Orgnization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}

					LOGGER.info("validating Edit/Term Claim Action details ...");
					List<ClaimActionSanction> claimActionDetails = json.getClaimActionSanctions();
					if (claimActionDetails != null) {

						loginPage.navToClaimActSanctTab();
						for (ClaimActionSanction claimDetail : claimActionDetails) {

							result = claimActnSnctnPage.searchEditClaimActions(claimDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "ClaimActions record is not available" : "";
							resultSet.add(new ResultTuple("Claim Action-Sanction", "",
									claimDetail.getClaimActionSanctionType() + " : "
											+ claimDetail.getClaimActionSanctionValue() + " : "
											+ claimDetail.getClaimActionSanctionActive().getEffectiveDate() + " : "
											+ claimDetail.getClaimActionSanctionActive().getTerminationDate() + " : "
											+ claimDetail.getClaimActionSanctionActive().getTerminationReasonCode(),
									result, comment));
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-132970 - Add/Edit/Term Organization PDM Indicators", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-132970", author = "Shiva", transactionId = "ORG_ADD_EDIT_TERM_PDM_INDS", sprint = "Sprint 4")

	public void ORG_ADD_EDIT_TERM_PDM_INDS() {

		LOGGER.info("Inside test ORG_ADD_EDIT_TERM_PDM_INDS");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ProfilePage profilePage = new ProfilePage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Orgnization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}

					LOGGER.info("Provider Organization is present...");

					List<PdmIndicatorDetail> pdmIndDetails = json.getProfileAdditionalDetails()
							.getPdmIndicatorDetails();
					if (pdmIndDetails != null) {
						for (PdmIndicatorDetail pdmIndDetail : pdmIndDetails) {
							result = profilePage.searchPDMIndicators(pdmIndDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "PdmIndicatorDetail record is not available" : "";
							resultSet.add(new ResultTuple("PdmIndicatorDetail", "",
									pdmIndDetail.getPdmIndicator() + " : "
											+ pdmIndDetail.getPdmIndicatorActive().getEffectiveDate() + " "
											+ pdmIndDetail.getPdmIndicatorActive().getTerminationDate() + " "
											+ pdmIndDetail.getPdmIndicatorActive().getTerminationReasonCode(),
									result, comment));
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133331 - Individual - terminate network and reimbursements - no replacements", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133331", author = "Shiva", transactionId = "IND_TERM_NTWK_REIMB_NO_REPLACEMENTS", sprint = "Sprint 4")

	public void IND_TERM_NTWK_REIMB_NO_REPLACEMENTS() {

		LOGGER.info("Inside test IND_TERM_NTWK_REIMB_NO_REPLACEMENTS");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Provider Individual is present...");
					List<Affiliation> affiliations = json.getAffiliations();
					for (Affiliation affiliation : affiliations) {

						List<Address> addr = affiliation.getAddresses();
						for (Address address : addr) {

							loginPage.navToReimbNtwrkTab();

							result = reimbNtwrkPage.selectAvailableAddrCheckbox(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Available Address record is not available" : "";
							resultSet.add(new ResultTuple("Available Address", "",
									address.getAddressDetails().getAddressLine1() + " : "
											+ address.getAddressDetails().getCity() + " "
											+ address.getAddressDetails().getCounty() + " "
											+ address.getAddressDetails().getZip(),
									result, comment));
							if (result == FAILED)
								continue;

							Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
							result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation Address record is not available" : "";
							resultSet.add(new ResultTuple("Affiliation Address", "",
									affiliation.getAffiliatedLegacyID() + " : "
											+ affiliation.getAffiliationActive().getEffectiveDate() + " "
											+ affiliation.getAffiliationActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
							for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
								result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "NetworkAffiliation record is not available" : "";
								resultSet.add(new ResultTuple("NetworkAffiliation ", "",
										ntwrkAffil.getNetworkID() + " : " + ntwrkAffil.getNetworkSourceSystem() + " "
												+ ntwrkAffil.getNetworkActive().getEffectiveDate() + " "
												+ ntwrkAffil.getNetworkActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								String actNtwrkTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
								String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();

								if (!actNtwrkTermDate.equalsIgnoreCase(expNtwrkTermDate)) {
									resultSet.add(new ResultTuple("NetwrkTermDate", actNtwrkTermDate, expNtwrkTermDate,
											FAILED, "NetwrkTermDate is not matching"));
								} else {
									resultSet.add(new ResultTuple("NetwrkTermDate", actNtwrkTermDate, expNtwrkTermDate,
											PASSED, "NetwrkTermDate is matched"));
								}

								String actNtwrkTermReasonCode = reimbNtwrkPage.getIndNetwrkTermReasonCode();
								String expNtwrkTermReasonCode = ntwrkAffil.getNetworkActive().getEffectiveDate();

								if (!actNtwrkTermReasonCode.equalsIgnoreCase(expNtwrkTermReasonCode)) {
									resultSet.add(new ResultTuple("NetwrkTermReasonCode", actNtwrkTermReasonCode,
											expNtwrkTermReasonCode, FAILED, "NetwrkTermReasonCode is not matching"));
								} else {
									resultSet.add(new ResultTuple("NetwrkTermReasonCode", actNtwrkTermReasonCode,
											expNtwrkTermReasonCode, PASSED, "NetwrkTermReasonCode is matched"));
								}
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
								reimbNtwrkPage.indNetwrkClickCancel();
							}
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133082 - Edit Individual - Network - Term/Add Directory Indicator", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133082", author = "Shiva", transactionId = "IND_NTWK_ADD_TERM_DIRECTORY_IND", sprint = "Sprint 3")

	public void IND_NTWK_ADD_TERM_DIRECTORY_IND() {

		LOGGER.info("Inside test IND_NTWK_ADD_TERM_DIRECTORY_IND");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AffiliationsRelationships affilRelationPage = new AffiliationsRelationships(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					loginPage.navToAffilRelationTab();

					List<Affiliation> affiliations = json.getAffiliations();
					for (Affiliation affiliation : affiliations) {
						String providerEId = affiliation.getAffiliatedLegacyID();
						affilRelationPage.searchAffilEid(providerEId);

						List<Address> addresses = affiliation.getAddresses();
						for (Address address : addresses) {

							result = affilRelationPage.searchIndAffiliation(affiliation, address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation  details is not available" : "";
							resultSet.add(new ResultTuple("Affiliation ", "",
									address.getAddressActive().getEffectiveDate() + " "
											+ address.getAddressActive().getTerminationDate() + " "
											+ address.getAddressActive().getTerminationReasonCode(),
									result, comment));
							if (result == FAILED)
								continue;

							loginPage.navToReimbNtwrkTab();

							result = reimbNtwrkPage.selectAvailableAddrCheckbox(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Record is not available with the matching address" : "";
							resultSet.add(new ResultTuple("AvailableAddrCheckbox", "", "Expected address: "
									+ address.getAddressDetails().getAddressLine1() + "Expected Effective Date: "
									+ address.getAddressActive().getEffectiveDate() + "Expected Termination  date: "
									+ address.getAddressActive().getTerminationDate(), result, comment));

							if (result == FAILED)
								continue;

							Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
							result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED)
									? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
									: "";
							resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
									"Expected ID: " + affiliation.getAffiliatedLegacyID() + "Expected Effective Date: "
											+ affiliation.getAffiliationActive().getEffectiveDate()
											+ "Expected Termination  date: "
											+ affiliation.getAffiliationActive().getTerminationDate(),
									result, comment));

							if (result == FAILED)
								continue;

							List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
							for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

								result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Network ID, effective date , termination date"
										: "";
								resultSet.add(new ResultTuple("Network ID", "",
										"Expected ID: " + ntwrkAffil.getNetworkID() + "Expected Effective Date: "
												+ ntwrkAffil.getNetworkSourceSystem() + "Expected Termination  date: "
												+ ntwrkAffil.getNetworkActive().getEffectiveDate()
												+ ntwrkAffil.getNetworkActive().getTerminationDate()
												+ ntwrkAffil.getNetworkActive().getTerminationReasonCode(),
										result, comment));
								if (result == FAILED)
									continue;

								String actNtwrkTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
								String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
								if (!actNtwrkTermDate.contains(expNtwrkTermDate)) {
									resultSet.add(new ResultTuple("IndNetwrkTermDate", actNtwrkTermDate,
											expNtwrkTermDate, FAILED, "IndNetwrkTermDate not matching"));
								} else {
									resultSet.add(new ResultTuple("IndNetwrkTermDate", actNtwrkTermDate,
											expNtwrkTermDate, PASSED, "IndNetwrkTermDate matching"));
								}

								String actNetDirInd = reimbNtwrkPage.getIndNetDirIndicator();
								String expNetDirInd = ntwrkAffil.getDirectoryIndicator();
								if (!actNetDirInd.contains(expNetDirInd)) {
									resultSet.add(new ResultTuple("IndDirectoryindicator", actNetDirInd, expNetDirInd,
											FAILED, "actIndNetwkEffDate not matching"));
								} else {
									resultSet.add(new ResultTuple("IndDirectoryindicator", actNetDirInd, expNetDirInd,
											PASSED, "actIndNetwkEffDate matching"));
								}
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
								reimbNtwrkPage.indNetwrkClickCancel();
							}
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133208 - Organization - Edit Legacy Fields", enabled = true, groups = { "org" })
	@TestDetails(userstory = "SPDS-133208", author = "Shiva", transactionId = "ORG_EDIT_LEGACY_FIELDS", sprint = "Sprint 4")
	public void ORG_EDIT_LEGACY_FIELDS() {

		LOGGER.info("Inside test ORG_EDIT_LEGACY_FIELDS");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					LegacyInfo legacyInfoPage = new LegacyInfo(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Orgnization Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Organization Provider is present...");

					loginPage.navToLegacyInfoTab();
					legacyInfoPage.selectLegacyFieldDropdown();

					String expContractInd = json.getLegacy().getContractIndicator();
					String actContractInd = legacyInfoPage.legacyContractIndicator();
					if (!actContractInd.contains(expContractInd)) {
						resultSet.add(new ResultTuple("IndNetwrkTermDate", actContractInd, expContractInd, FAILED,
								"ContractIndicator not matching"));
					} else {
						resultSet.add(new ResultTuple("ContractIndicator", actContractInd, expContractInd, PASSED,
								"ContractIndicator matching"));
					}

					String expNWMIndicator = json.getLegacy().getNwm();
					String actNWMIndicator = legacyInfoPage.nwmInd();
					if (!actContractInd.contains(expNWMIndicator)) {
						resultSet.add(new ResultTuple("NWMIndicator", actNWMIndicator, expNWMIndicator, FAILED,
								"NWMIndicator not matching"));
					} else {
						resultSet.add(new ResultTuple("NWMIndicator", actNWMIndicator, expNWMIndicator, PASSED,
								"NWMIndicator matching"));
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-133081 - Edit Individual - Network - Term/Add Timely Filing", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133081", author = "Murali", transactionId = "IND_NTWK_ADD_TERM_TIMELY_FILING", sprint = "Sprint 3")
	public void IND_NTWK_ADD_TERM_TIMELY_FILING() {

		LOGGER.info("Inside test IND_NTWK_ADD_TERM_TIMELY_FILING");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Provider Individual is present...");

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();

					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {

								result = reimbNtwrkPage.selectAvailableAddrCheckbox(ntwrkAddr) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Record is not available with the matching address"
										: "";
								resultSet.add(new ResultTuple("AvailableAddrCheckbox", "",
										"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
												+ "Expected Effective Date: "
												+ affiliation.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affiliation.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
										: "";
								resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
										"Expected ID: " + affiliation.getAffiliatedLegacyID()
												+ "Expected Effective Date: "
												+ affiliation.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affiliation.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();

								for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

									result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "Record is not available with the matching Network ID, effective date , termination date"
											: "";
									resultSet.add(new ResultTuple("Network ID", "",
											"Expected ID: " + affiliation.getAffiliatedLegacyID()
													+ "Expected Effective Date: "
													+ affiliation.getAffiliationActive().getEffectiveDate()
													+ "Expected Termination  date: "
													+ affiliation.getAffiliationActive().getTerminationDate(),
											result, comment));
									if (result == FAILED)
										continue;

									String actTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
									String actTimelyFiling = reimbNtwrkPage.getIndTimlyFiling();
									LOGGER.info("Actual termination date: " + actTermDate);
									LOGGER.info("Actual timily filing value: " + actTimelyFiling);

									List<TimelyFilingInd> timelyFilingIndList = ntwrkAffil.getTimelyFilingInds();
									if (timelyFilingIndList != null) {
										for (TimelyFilingInd expTimelyFilingInd : timelyFilingIndList) {
											LOGGER.info("Timely Filing Indicators: "
													+ expTimelyFilingInd.getTimelyFiling());

											result = expTimelyFilingInd.getTimelyFiling()
													.equalsIgnoreCase(actTimelyFiling) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The timily filing value is not matching with the actual value"
													: "";
											resultSet.add(new ResultTuple("Timily filing value ",
													"Actual value: " + actTimelyFiling,
													"Expected : " + expTimelyFilingInd.getTimelyFiling(), result,
													comment));

											String expTermDate = expTimelyFilingInd.getTimelyFilingActive()
													.getTerminationDate();

											result = actTermDate.equalsIgnoreCase(expTermDate) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The Network Termination date is not matching with the actual value"
													: "";
											resultSet.add(new ResultTuple("Network termination date ",
													"Actual value: " + actTermDate, " Expected : " + expTermDate,
													result, comment));
										}
									}
									reimbNtwrkPage.indClickCancel();
									Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
								}
							}
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	@Test(description = "SPDS-132989 - Edit Individual - existing practice address and affiliation - add new networks/reimbursements", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132989", author = "Murali", transactionId = "IND_EXIST_ADDR_AFF_ADD_NTWK_REIMB", sprint = "Sprint 3")
	public void IND_EXIST_ADDR_AFF_ADD_NTWK_REIMB() {

		LOGGER.info("Inside test IND_EXIST_ADDR_AFF_ADD_NTWK_REIMB");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String expPARIndicator = "Yes";
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Provider Individual is present...");

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();

					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {

								result = reimbNtwrkPage.selectAvailableAddrCheckbox(ntwrkAddr) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Record is not available with the matching address"
										: "";
								resultSet.add(new ResultTuple("AvailableAddrCheckbox", "",
										"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
												+ "Expected Effective Date: "
												+ affiliation.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affiliation.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
										: "";
								resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
										"Expected ID: " + affiliation.getAffiliatedLegacyID()
												+ "Expected Effective Date: "
												+ affiliation.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affiliation.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
								for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

									result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "Record is not available with the matching Network ID, effective date , termination date"
											: "";
									resultSet.add(new ResultTuple("Network ID", "",
											"Expected ID: " + affiliation.getAffiliatedLegacyID()
													+ "Expected Effective Date: "
													+ affiliation.getAffiliationActive().getEffectiveDate()
													+ "Expected Termination  date: "
													+ affiliation.getAffiliationActive().getTerminationDate(),
											result, comment));
									if (result == FAILED)
										continue;

									String actNetId = reimbNtwrkPage.getIndNetwrkId();
									String actPARInd = reimbNtwrkPage.getIndPARInd();
									String actNetEffDate = reimbNtwrkPage.getIndNetwrkEffDate();
									String actTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
									String actTermReasonCode = reimbNtwrkPage.getIndNetwrkTermReasonCode();
									String actTimelyfiling = reimbNtwrkPage.getIndTimlyFiling();
									String accPatientsInd = reimbNtwrkPage.getIndAcceptPatientsIndicator();
									String actNetDirInd = reimbNtwrkPage.getIndNetDirIndicator();

									LOGGER.info("Actual Network Id" + actNetId);
									LOGGER.info("Actual Network Effective Date" + actNetEffDate);
									LOGGER.info("Actual termination date: " + actTermDate);
									LOGGER.info("Actual Termination reason code : " + actTermReasonCode);
									LOGGER.info("Actual patients indicator: " + actTimelyfiling);
									LOGGER.info("Actual accept patients indicator: " + accPatientsInd);
									LOGGER.info("Actual Network Directory Indicator: " + actNetDirInd);

									String expNetId = ntwrkAffil.getNetworkID();
									String expEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
									String expTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
									String expNetTermReasonCode = ntwrkAffil.getNetworkActive()
											.getTerminationReasonCode() != null
													? ntwrkAffil.getNetworkActive().getTerminationReasonCode()
													: "";
									String expNetDirInd = ntwrkAffil.getDirectoryIndicator() != null
											? ntwrkAffil.getDirectoryIndicator()
											: "";

									result = actNetId.equalsIgnoreCase(expNetId) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "The Network ID is not matching with the actual value"
											: "";
									resultSet.add(new ResultTuple("Network ID ", "Actual value: " + actNetId,
											"Expected : " + expNetId, result, comment));

									result = actPARInd.equalsIgnoreCase(expPARIndicator) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "PAR Indicator is not matching with the actual value"
											: "";
									resultSet.add(new ResultTuple("PAR Indicator", "Actual value: " + actPARInd,
											"Expected : " + expPARIndicator, result, comment));

									result = actNetEffDate.equalsIgnoreCase(expEffDate) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "The Network effective date is not matching with the actual value"
											: "";
									resultSet.add(
											new ResultTuple("Network effective date ", "Actual value: " + actNetEffDate,
													"Expected : " + expEffDate, result, comment));

									result = actTermDate.equalsIgnoreCase(expTermDate) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "The Network termination date is not matching with the actual value"
											: "";
									resultSet.add(
											new ResultTuple("Network termination date ", "Actual value: " + actTermDate,
													"Expected : " + expTermDate, result, comment));

									result = actTermReasonCode.equalsIgnoreCase(expNetTermReasonCode) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "The termination reason code is not matching with the actual value"
											: "";
									resultSet.add(new ResultTuple("Termination reason code",
											"Actual value: " + actTermReasonCode, " Expected : " + expNetTermReasonCode,
											result, comment));

									result = actNetDirInd.equalsIgnoreCase(expNetDirInd) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "The Networking directory indicator is not matching with the actual value"
											: "";
									resultSet.add(new ResultTuple("Networking directory indicator ",
											"Actual value: " + actNetDirInd, " Expected : " + expNetDirInd, result,
											comment));

									List<TimelyFilingInd> timelyFilingIndList = ntwrkAffil.getTimelyFilingInds();
									if (timelyFilingIndList != null) {
										for (TimelyFilingInd expTimelyFilingInd : timelyFilingIndList) {
											LOGGER.info("Timely Filing Indicators: "
													+ expTimelyFilingInd.getTimelyFiling());

											result = expTimelyFilingInd.getTimelyFiling()
													.equalsIgnoreCase(actTimelyfiling) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The Timily filing value is not matching with the actual value"
													: "";
											resultSet.add(new ResultTuple(" Timily filing value ",
													"Actual value: " + actTimelyfiling,
													"Expected : " + expTimelyFilingInd.getTimelyFiling(), result,
													comment));
										}
									}

									List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
											.getAcceptingPatientsInds();
									if (accPatientsIndList != null) {
										for (AcceptingPatientsInd expAccPatientsInd : accPatientsIndList) {
											LOGGER.info("Accepting patient indicators: "
													+ expAccPatientsInd.getAcceptingPatientsIndicator());

											result = expAccPatientsInd.getAcceptingPatientsIndicator()
													.equalsIgnoreCase(accPatientsInd) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The Accepting patients indicator is not matching with the actual value"
													: "";
											resultSet.add(new ResultTuple("Accepting patients indicator ",
													"Actual value: " + accPatientsInd,
													"Expected : " + expAccPatientsInd.getAcceptingPatientsIndicator(),
													result, comment));
										}
									}

									List<Reimbursement> reimbList = affiliation.getReimbursements();
									if (reimbList != null) {
										for (Reimbursement ntwrkReimb : reimbList) {
											if (ntwrkReimb.getNetworkID().equals(ntwrkAffil.getNetworkID())) {
												LOGGER.info(
														"Reimbursement Network ID and Network affiliation network ID matches");

												result = reimbNtwrkPage.valIndAssociatedReimbTable(ntwrkReimb) ? PASSED
														: FAILED;
												comment = result.equals(FAILED)
														? "Record is not available with the matching Reimbursement value, effective date , termination date"
														: "";
												resultSet.add(new ResultTuple("Reimbursement value", "",
														"Expected reimbursement value: "
																+ ntwrkReimb.getReimbursementValue()
																+ "Expected Effective Date: "
																+ ntwrkReimb.getReimbursementActive().getEffectiveDate()
																+ "Expected Termination  date: " + ntwrkReimb
																		.getReimbursementActive().getTerminationDate(),
														result, comment));
												if (result == FAILED)
													continue;

												LOGGER.info("Inside Individual reimbursement page");
												String indExpReimbValue = ntwrkReimb.getReimbursementValue();
												String indExpReimbEffDate = ntwrkReimb.getReimbursementActive()
														.getEffectiveDate();
												String indExpReimbTermDate = ntwrkReimb.getReimbursementActive()
														.getTerminationDate();
												String indExpReimbTermCode = ntwrkReimb.getReimbursementActive()
														.getTerminationReasonCode() != null
																? ntwrkReimb.getReimbursementActive()
																		.getTerminationReasonCode()
																: "";

												String actReimbValue = reimbNtwrkPage.getIndReimbValue();
												String actReimbEffDate = reimbNtwrkPage.getIndReimbEffDate();
												String actReimbTermDate = reimbNtwrkPage.getIndReimbTermDate();
												String actReimbTermReasonCode = reimbNtwrkPage
														.getIndReimbTermReasonCode();

												LOGGER.info("Actual reimbursement value: " + actReimbValue);
												LOGGER.info("Actual Effective Date : " + actReimbEffDate);
												LOGGER.info("Actual Termination Date: " + actReimbTermDate);
												LOGGER.info("Actual Termination Code : " + actReimbTermReasonCode);

												result = actReimbValue.equalsIgnoreCase(indExpReimbValue) ? PASSED
														: FAILED;
												comment = result.equals(FAILED)
														? "The Reimbursement  value is not matching with the actual value"
														: "";
												resultSet.add(new ResultTuple("Reimbursement  value ",
														"Actual value: " + actReimbValue,
														" Expected : " + indExpReimbValue, result, comment));

												result = actReimbEffDate.equalsIgnoreCase(indExpReimbEffDate) ? PASSED
														: FAILED;
												comment = result.equals(FAILED)
														? "The Reimbursement effective date is not matching with the actual value"
														: "";
												resultSet.add(new ResultTuple("Reimbursement effective date ",
														"Actual value: " + actReimbEffDate,
														"Expected : " + indExpReimbEffDate, result, comment));

												result = actReimbTermDate.equalsIgnoreCase(indExpReimbTermDate) ? PASSED
														: FAILED;
												comment = result.equals(FAILED)
														? "The Reimbursement termination date is not matching with the actual value"
														: "";
												resultSet.add(new ResultTuple("Reimbursement effective date ",
														"Actual value: " + actReimbTermDate,
														"Expected : " + indExpReimbTermDate, result, comment));

												result = actReimbTermReasonCode.equalsIgnoreCase(indExpReimbTermCode)
														? PASSED
														: FAILED;
												comment = result.equals(FAILED)
														? "The Reimbursement Termination code is not matching with the actual value"
														: "";
												resultSet.add(new ResultTuple("Reimbursement termination code",
														"Actual value: " + actReimbTermReasonCode,
														"Expected : " + indExpReimbTermCode, result, comment));
											}
										}
										reimbNtwrkPage.indReimbClickCancel();
										Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
									}
								}
								reimbNtwrkPage.indNetwrkClickCancel();
							}
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

}
