package com.tests.epds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.pages.EPDSPageObjects.Alternate;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class Day_02Run extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaOrgTestsS192.class);

	@Test(description = "SPDS-133023 - Edit Organization - Edit/Add/Terminate NPI Details", enabled = false, groups = {
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
					Alternate altIdPage = new Alternate(driver);

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

							result = altIdPage.searchNpiDetails(npiDetail, ProviderType.ORGANIZATION) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "NPI record is not available" : "NPI record is available";
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

	@Test(description = "SPDS-132968 - Edit Organization Profile Term Date and Term Reason", enabled = false, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-132968", author = "Shiva", transactionId = "ORG_TERM_PROFILE", sprint = "Sprint 192")
	public void ORG_TERM_PROFILE() {

		LOGGER.info("Inside test ORG_TERM_PROFILE");
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

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Provider Organization is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Provider Organization is present...");

					String expTermDate = json.getProfile().getProviderActive().getTerminationDate();
					String actTermDate = profilePage.getOrgProvTermDate();

					if (!actTermDate.contains(expTermDate)) {
						resultSet.add(new ResultTuple("Provider TerminationDate", actTermDate, expTermDate, FAILED,
								"TerminationDate not matching"));
					} else {
						resultSet.add(new ResultTuple("TerminationDate", actTermDate, expTermDate, PASSED,
								"TerminationDate is matching"));
					}

					String expTermReasonCode = (Objects
							.toString(json.getProfile().getProviderActive().getTerminationReasonCode(), ""))
									.toUpperCase();
					String actTermreasonCode = (profilePage.getOrgProvTermReasonCode()).toUpperCase();

					if (!actTermreasonCode.contains(expTermReasonCode)) {
						resultSet.add(new ResultTuple("TerminationReasonCode", actTermreasonCode, expTermReasonCode,
								FAILED, "TerminationReasonCode not matching"));
					} else {
						resultSet.add(new ResultTuple("TerminationReasonCode", actTermreasonCode, expTermReasonCode,
								PASSED, "TerminationReasonCode is matching"));
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

	@Test(description = "SPDS-132966 - Edit Organization Name", enabled = true, groups = { "org" }, priority = 1)
	@TestDetails(userstory = "SPDS-132966", author = "Murali", transactionId = "ORG_EDIT_NAME", sprint = "192")
	public void ORG_EDIT_NAME() {

		LOGGER.info("Inside test ORG_EDIT_NAME");
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

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Provider Orgnization is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Provider Organization is present...");

					String expOrgName = (Objects
							.toString(json.getProfile().getNameQualifier().getOrgNameQualifier().getOrgName(), ""))
									.toUpperCase();

					String actOrgName = (Objects.toString(profilePage.getOrgProvName(), "")).toUpperCase();

					if (!actOrgName.contains(expOrgName)) {
						resultSet.add(new ResultTuple("Organization Name", actOrgName, expOrgName, FAILED,
								"Organization name not matching"));
					} else {
						resultSet.add(new ResultTuple("Organization Name", actOrgName, expOrgName, PASSED,
								"Organization name is matching"));
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
