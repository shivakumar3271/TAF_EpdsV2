package com.tests.epds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.ClaimActionSanction;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.ClaimActionSanctionPage;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class Day_03Run extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaOrgTestsS192.class);

	// 27072282 27069499
	@Test(description = "SPDS-133011 - Edit Organization - Add Claim Action-Sanction", enabled = false, groups = {
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

	@Test(description = "SPDS-133022 - Edit Individual - Term/Add NPI", enabled = false, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133022", author = "Shiva", transactionId = "IND_ADD_TERM_NPI", sprint = "Sprint 192")
	public void IND_ADD_TERM_NPI() {

		LOGGER.info("Inside test IND_ADD_TERM_NPI");
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
					AlternateId altIdPage = new AlternateId(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID();
					if (!new ProviderSearch(driver).searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					List<NpiDetail> npiDetails = json.getAlternateIDs().getNpiDetails();
					if (npiDetails != null) {
						loginPage.navToAltIdTab();

						String actEgilibilityType = altIdPage.getNpiEligibilityType();

						result = altIdPage.getEligibilityType(ProviderType.INDIVIDUAL) ? PASSED : FAILED;
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
							 * actEgilibilityType = altIdPage.getNpiEligibilityType();
							 * 
							 * if (!actEgilibilityType.equalsIgnoreCase(expEgilibilityType)) {
							 * resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType,
							 * expEgilibilityType, FAILED, "EgilibilityType is not matching")); } else {
							 * resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType,
							 * expEgilibilityType, PASSED, "EgilibilityType is matching")); }
							 */

							result = altIdPage.searchNpiDetails(npiDetail, ProviderType.INDIVIDUAL) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "NPI Detail record is not available"
									: "NPI Detail record is available";
							resultSet.add(new ResultTuple("NPI Details", "",
									npiDetail.getNpiType() + " : " + npiDetail.getNpiValue() + " : "
											+ npiDetail.getNpiActive().getEffectiveDate() + " ; "
											+ npiDetail.getNpiActive().getTerminationDate() + " : "
											+ npiDetail.getNpiActive().getTerminationReasonCode(),
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

	@Test(description = "SPDS-132976 - Edit Individual - Add/Remove provider Ethnicity", enabled = false, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-132976", author = "Reshmi C", transactionId = "IND_ADD_REMOVE_PROV_ETHNICITY", sprint = "Sprint 192")
	public void IND_ADD_REMOVE_PROV_ETHNICITY() {

		LOGGER.info("Inside test IND_ADD_REMOVE_PROV_ETHNICITY");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "";

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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					String actEthnicity = profilePage.getIndEthnicity();
					List<String> ethnicity = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName().getEthnicity();
					if (ethnicity != null) {
						for (String expEthnicity : ethnicity) {
							result = actEthnicity.equalsIgnoreCase(expEthnicity) ? PASSED : FAILED;
							if (result == PASSED) {
								resultSet.add(new ResultTuple("Ethnicity", actEthnicity, expEthnicity, result,
										"Ethnicity is matching"));
							} else {
								resultSet.add(new ResultTuple("Ethnicity", actEthnicity, expEthnicity, result,
										"Ethnicity is not matching"));
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

	@Test(description = "SPDS-133020 - Edit Individual - Add/Edit Titles", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133020", author = "Reshmi C", transactionId = "IND_ADD_EDIT_TITLE", sprint = "Sprint 192")
	public void IND_ADD_EDIT_TITLE() {

		LOGGER.info("Inside test IND_ADD_EDIT_TITLE");
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
					ProfilePage profilePage = new ProfilePage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String entID = json.getHeader().getLegacyID() != null ? json.getHeader().getLegacyID() : "";
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					String profTitleCode = profilePage.getIndProfessionalTitle();
					List<String> expProfTitleCodes = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName().getProfessionalTitleCodes();

					for (String expProfTitleCode : expProfTitleCodes) {
						if (!profTitleCode.contains(expProfTitleCode)) {
							resultSet.add(new ResultTuple("Professional Title Code", profTitleCode, expProfTitleCode,
									FAILED, "Addition failed"));
						} else {
							resultSet.add(new ResultTuple("Professional Title Code", profTitleCode, expProfTitleCode,
									PASSED, "Addition success"));
							profTitleCode = profTitleCode.replaceAll(expProfTitleCode, "");
						}
					}

					if (profTitleCode.matches(".*[a-zA-Z]+.*")) {
						// Professional Title Code removal failed
						resultSet.add(new ResultTuple("Professional Title Code", profTitleCode, "", FAILED,
								"Removal failed"));
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
