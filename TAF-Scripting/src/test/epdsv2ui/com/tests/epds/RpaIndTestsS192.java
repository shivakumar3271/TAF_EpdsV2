package com.tests.epds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.AcceptingPatientsInd;
import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AliasName;
import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.ClaimActionSanction;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.EducationDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.ProfileName;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.SpecialProgram;
import com.beans.mdxjson.Specialty;
import com.beans.mdxjson.TimelyFilingInd;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AffiliationsRelationships;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.ClaimActionSanctionPage;
import com.pages.EPDSPageObjects.Education;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.pages.EPDSPageObjects.ReimbusermentNetworks;
import com.pages.EPDSPageObjects.SpecialityTaxonomy;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class RpaIndTestsS192 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaIndTestsS192.class);

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

	@Test(description = "SPDS-132976 - Edit Individual - Add/Remove provider Ethnicity", enabled = true, groups = {
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

	@Test(description = "SPDS-132974 - Edit Individual - Add/remove provider language", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-132974", author = "Reshmi C", transactionId = "IND_ADD_REMOVE_PROV_LANGUAGE", sprint = "Sprint 192")
	public void IND_ADD_REMOVE_PROV_LANGUAGE() {

		LOGGER.info("Inside test IND_ADD_REMOVE_PROV_LANGUAGE");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					String provLanguages = profilePage.getIndProviderLanguages();
					List<String> expProvLanguageList = json.getProfile().getProviderLanguages();
					if (expProvLanguageList != null) {

						for (String expProvLanguage : expProvLanguageList) {
							if (!provLanguages.contains(expProvLanguage)) {
								resultSet.add(new ResultTuple("Language", provLanguages, expProvLanguage, FAILED,
										"Addition failed"));
							} else {
								resultSet.add(new ResultTuple("Language", provLanguages, expProvLanguage, PASSED,
										"Addition success"));
								provLanguages = provLanguages.replaceAll(expProvLanguage, "");
							}
						}
					}

					if (provLanguages.matches(".*[a-zA-Z]+.*")) {
						// Language removal failed
						resultSet.add(new ResultTuple("Language", provLanguages, "", FAILED, "Removal failed"));
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

	@Test(description = "SPDS-133318 - Edit Individual - Profile - Gender and/or DOB", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133318", author = "Shiva", transactionId = "IND_PROFILE_EDIT_DOB_GENDER", sprint = "Sprint 192")
	public void IND_PROFILE_EDIT_DOB_GENDER() {

		LOGGER.info("Inside test IND_PROFILE_EDIT_DOB_GENDER");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					String expGender = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName().getGender();
					String expDateOfBirth = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName().getDateOfBirth();

					String actGender = profilePage.getIndProvGender();
					String actDateOfBirth = profilePage.getIndProvDOB();

					if (!actGender.equalsIgnoreCase(expGender)) {
						resultSet.add(new ResultTuple("Gender", actGender, expGender, FAILED, "Gender not matching"));
					} else {
						resultSet.add(new ResultTuple("Gender", actGender, expGender, PASSED, "Gender is matching"));
					}

					if (!actDateOfBirth.equalsIgnoreCase(expDateOfBirth)) {
						resultSet.add(
								new ResultTuple("DOB", actDateOfBirth, expDateOfBirth, FAILED, "DOB not matching"));
					} else {
						resultSet.add(new ResultTuple("DOB", actDateOfBirth, expDateOfBirth, PASSED, "DOB matching"));
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

	@Test(description = "SPDS-133004 - Edit Individual - Alias name", enabled = true, groups = { "ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133004", author = "Shiva", transactionId = "IND_PROFILE_ALIAS_NAME", sprint = "Sprint 192")
	public void IND_PROFILE_ALIAS_NAME() {

		LOGGER.info("Inside test IND_PROFILE_ALIAS_NAME");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					List<AliasName> aliasNames = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getAliasNames();
					if (aliasNames != null) {
						for (AliasName aliNames : aliasNames) {

							String expFirstName = aliNames.getFirstName();
							String expMiddleName = aliNames.getMiddleName();
							String expLastName = aliNames.getLastName();
							String expSuffix = aliNames.getSuffix();

							String actFirstName = profilePage.getIndAliasFirstName();
							String actMiddleName = profilePage.getIndAliasMiddleName();
							String actLastName = profilePage.getIndAliasLastName();
							String actSuffix = profilePage.getIndAliasSuffix();

							LOGGER.info("validating provider Alias Names...");

							if (!actFirstName.equalsIgnoreCase(expFirstName)) {
								resultSet.add(new ResultTuple("Alias First Name", actFirstName, expFirstName, FAILED,
										"First name not matching"));
							} else {
								resultSet.add(new ResultTuple("FirstName", actFirstName, expFirstName, PASSED,
										"First name is matching"));
							}

							if (!actMiddleName.equalsIgnoreCase(expMiddleName)) {
								resultSet.add(new ResultTuple("Alias Middle Name", actMiddleName, expMiddleName, FAILED,
										"Middle name not matching"));
							} else {
								resultSet.add(new ResultTuple("MiddleName", actMiddleName, expMiddleName, PASSED,
										"Middle name is matching"));
							}

							if (!actLastName.equalsIgnoreCase(expLastName)) {
								resultSet.add(new ResultTuple("Alias Last Name", actLastName, expLastName, FAILED,
										"Last name not matching"));
							} else {
								resultSet.add(new ResultTuple("LastName", actLastName, expLastName, PASSED,
										"Last name is matching"));
							}

							if (!actSuffix.equalsIgnoreCase(expSuffix)) {
								resultSet.add(new ResultTuple("Alias Suffix", actSuffix, expSuffix, FAILED,
										"Suffix name not matching"));
							} else {
								resultSet.add(new ResultTuple("Suffix)", actSuffix, expSuffix, PASSED,
										"Suffix name is matching"));
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

	@Test(description = "SPDS-133022 - Edit Individual - Term/Add NPI", enabled = true, groups = {
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

	@Test(description = "SPDS-132995 - Edit Individual existing practice address - Schedule Details ", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-132995", author = "Shiva", transactionId = "IND_EXIST_ADDR_EDIT_SCHEDULE_DETAILS", sprint = "Sprint 192")
	public void IND_EXIST_ADDR_EDIT_SCHEDULE_DETAILS() {

		LOGGER.info("Inside test IND_EXIST_ADDR_EDIT_SCHEDULE_DETAILS");
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

					List<Address> addressess = json.getAddresses();
					for (Address addressDetails : addressess) {
						loginPage.navToAddressTab();

						result = addressPage.filterAndSelectIndAddress(addressDetails) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "",
								addressDetails.getAddressDetails().getAddressType() + " : "
										+ addressDetails.getAddressDetails().getAddressLine1() + " "
										+ addressDetails.getAddressDetails().getCity() + " "
										+ addressDetails.getAddressDetails().getState() + " "
										+ addressDetails.getAddressDetails().getZip() + " : "
										+ addressDetails.getAddressActive().getEffectiveDate() + " : "
										+ addressDetails.getAddressActive().getTerminationDate(),
								result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToAltIdsTab();
						result = addressPage.valAltIdTypeSource(expAltId, expAltSource) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Alt ID record is not available"
								: "Alt ID record is available";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltSource, result, comment));
						if (result == FAILED)
							continue;

						List<SchedulingDetail> scheduleDetail = addressDetails.getAddressAdditionalDetails()
								.getSchedulingDetails();
						if (scheduleDetail != null) {
							addressPage.navToScheduleTab();
							for (SchedulingDetail schedulingDetail : scheduleDetail) {
								result = addressPage.valSchedulingDetail(schedulingDetail) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Scheduling Detail record is not available"
										: "Scheduling Detail record is available";
								resultSet.add(new ResultTuple("scheduleDetail", "", schedulingDetail.getDays() + " : "
										+ schedulingDetail.getOpenTime() + " : " + schedulingDetail.getCloseTime(),
										result, comment));
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

	@Test(description = "SPDS-133008 - Edit Individual - Add Alternate ID", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133008", author = "Shiva", transactionId = "IND_PROFILE_ADD_ALT_ID", sprint = "Sprint 192")
	public void IND_PROFILE_ADD_ALT_ID() {

		LOGGER.info("Inside test IND_PROFILE_ADD_ALT_ID");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet
								.add(new ResultTuple("Provider", "", entID, FAILED, "Grouping Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					List<AltIdDetail> altIdDetails = json.getAlternateIDs().getAltIdDetails();
					if (altIdDetails != null) {

						loginPage.navToAltIdTab();
						for (AltIdDetail altIdDetail : json.getAlternateIDs().getAltIdDetails()) {

							result = altIdPage.searchAltIdDetails(altIdDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Alternate Id record is not available"
									: "Alternate Id record available";
							resultSet.add(new ResultTuple("AltIdDetail", "",
									altIdDetail.getAltIDType() + " : " + altIdDetail.getAltIDSource() + " : "
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

	@Test(description = "SPDS-133003 - Edit Individual - Profile - Edit Name", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133003", author = "Murali", transactionId = "IND_PROFILE_EDIT_NAME", sprint = "Sprint 192")
	public void IND_PROFILE_EDIT_NAME() {

		LOGGER.info("Inside test IND_PROFILE_EDIT_NAME");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					ProfileName indProfileDetails = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName();

					String expIndFname = indProfileDetails.getFirstName() != null ? indProfileDetails.getFirstName()
							: "";
					String expIndMname = indProfileDetails.getMiddleName() != null ? indProfileDetails.getMiddleName()
							: "";
					String expIndLname = indProfileDetails.getLastName() != null ? indProfileDetails.getLastName() : "";
					String expIndSuffix = indProfileDetails.getSuffix() != null ? indProfileDetails.getSuffix() : "";

					String actIndFname = profilePage.getIndProvFirstname();
					String actIndMname = profilePage.getIndProvMidname();
					String actIndLname = profilePage.getIndProvLastname();
					String actIndSuffix = profilePage.getIndProvSuffix();

					if (!actIndFname.equalsIgnoreCase(expIndFname)) {
						resultSet.add(new ResultTuple("First Name", actIndFname, expIndFname, FAILED,
								"First name not matching"));
					} else {
						resultSet.add(new ResultTuple("First Name", actIndFname, expIndFname, PASSED,
								"First name is matching"));
					}

					if (!actIndMname.equalsIgnoreCase(expIndMname)) {
						resultSet.add(new ResultTuple("Middle Name", actIndMname, expIndMname, FAILED,
								"Middle name not matching"));
					} else {
						resultSet.add(new ResultTuple("Middle Name", actIndMname, expIndMname, PASSED,
								"Middle name is matching"));
					}

					if (!actIndLname.equalsIgnoreCase(expIndLname)) {
						resultSet.add(new ResultTuple("Last Name", actIndLname, expIndLname, FAILED,
								"Last name not matching"));
					} else {
						resultSet.add(new ResultTuple("Last Name", actIndLname, expIndLname, PASSED,
								"Last name is matching"));
					}

					if (!actIndSuffix.equalsIgnoreCase(expIndSuffix)) {
						resultSet.add(
								new ResultTuple("Suffix", actIndSuffix, expIndSuffix, FAILED, "Suffix not matching"));
					} else {
						resultSet.add(
								new ResultTuple("Suffix", actIndSuffix, expIndSuffix, PASSED, "Suffix is matching"));
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

	@Test(description = "SPDS-133007 - Edit Individual - Edit / Term Alternate ID", enabled = true, groups = {
			"ind" }, priority = 1)
	@TestDetails(userstory = "SPDS-133007", author = "Murali", transactionId = "IND_PROFILE_EDIT_TERM_ALT_ID", sprint = "Sprint 192")
	public void IND_PROFILE_EDIT_TERM_ALT_ID() {

		LOGGER.info("Inside test IND_PROFILE_EDIT_TERM_ALT_ID");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					List<AltIdDetail> altIdDetails = json.getAlternateIDs().getAltIdDetails();
					if (altIdDetails != null) {

						loginPage.navToAltIdTab();
						for (AltIdDetail altIdDetail : altIdDetails) {

							result = altIdPage.searchAltIdDetails(altIdDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Allternate Id record is not available"
									: "Allternate Id record is available";
							resultSet.add(new ResultTuple("ALT details", "",
									altIdDetail.getAltIDType() + " : " + altIdDetail.getAltIDSource() + " : "
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

	@Test(description = "SPDS-133356 - Edit Individual - Add/Edit Education Details", enabled = true, groups = {
			"Ind" })
	@TestDetails(userstory = "SPDS-133356", author = "Shiva", transactionId = "IND_ADD_EDIT_EDUCATION_DETAILS", sprint = "Sprint 192")
	public void IND_ADD_EDIT_EDUCATION_DETAILS() {

		LOGGER.info("Inside test IND_ADD_EDIT_EDUCATION_DETAILS");
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
					Education educationPage = new Education(driver);

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

					loginPage.navToEducationTab();

					List<EducationDetail> educationDetails = json.getEducationDetails();
					if (educationDetails != null) {
						for (EducationDetail educationDetail : educationDetails) {
							result = educationPage.searchEducationDetails(educationDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "EducationDetail is not available"
									: "EducationDetail is available";
							resultSet.add(new ResultTuple("Address", "",
									educationDetail.getEducationTypeCode() + " : " + educationDetail.getSchoolName()
											+ " " + educationDetail.getDegree() + " "
											+ educationDetail.getSchoolGraduationYear() + " "
											+ educationDetail.getStartDate() + " : " + educationDetail.getEndDate(),
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

	@Test(description = "SPDS-133090 - Edit Individual - Edit/Add Profile Credentialing Details", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133090", author = "Shiva", transactionId = "IND_PROFILE_ADD_EDIT_CREDENTIALING_DETAILS", sprint = "Sprint 192")
	public void IND_PROFILE_ADD_EDIT_CREDENTIALING_DETAILS() {

		LOGGER.info("Inside test IND_PROFILE_ADD_EDIT_CREDENTIALING_DETAILS");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					List<CredentialingDetail> credentialingDetails = json.getProfileAdditionalDetails()
							.getCredentialingDetails();
					if (credentialingDetails != null) {
						for (CredentialingDetail credentialingDetail : credentialingDetails) {
							result = profilePage.searchCredentialingDetails(credentialingDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Credentialing details are not available"
									: "Credentialing details are available";
							resultSet.add(new ResultTuple("CredentialingDetail", "",
									credentialingDetail.getCredentialingSource() + " : "
											+ credentialingDetail.getCredentialingStatus() + " "
											+ credentialingDetail.getCredentialingActive().getEffectiveDate() + " "
											+ credentialingDetail.getCredentialingActive().getTerminationDate() + " "
											+ credentialingDetail.getCredentialingActive().getTerminationReasonCode(),
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

	@Test(description = "SPDS-133018 - Edit Individual - Add Claim Action-Sanction", enabled = true, groups = { "ind" })
	@TestDetails(userstory = "SPDS-133018", author = "Shiva", transactionId = "IND_ADD_CLAIM_ACTION_SANCTION", sprint = "Sprint 192")
	public void IND_ADD_CLAIM_ACTION_SANCTION() {

		LOGGER.info("Inside test IND_ADD_CLAIM_ACTION_SANCTION");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					loginPage.navToClaimActSanctTab();

					List<ClaimActionSanction> claimActionsSnctns = json.getClaimActionSanctions();
					if (claimActionsSnctns != null) {
						for (ClaimActionSanction claimActionSnctn : claimActionsSnctns) {

							result = claimActnSnctnPage.searchClaimActions(claimActionSnctn) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "ClaimActionSanction details is not available"
									: "ClaimActionSanction details available";
							resultSet.add(new ResultTuple("ClaimActionSanction", "",
									claimActionSnctn.getClaimActionSanctionType() + " : "
											+ claimActionSnctn.getClaimActionSanctionValue() + " "
											+ claimActionSnctn.getClaimActionSanctionCriteria() + " "
											+ claimActionSnctn.getLowRange() + " " + claimActionSnctn.getHighRange()
											+ " " + claimActionSnctn.getClaimActionSanctionActive().getEffectiveDate()
											+ " " + claimActionSnctn.getClaimActionSanctionActive().getTerminationDate()
											+ " " + claimActionSnctn.getClaimActionSanctionActive()
													.getTerminationReasonCode(),
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

	@Test(description = "SPDS-133071 - Edit Individual - add new Grouping linkage for PCP ID assignment", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133071", author = "Shiva", transactionId = "IND_ADD_GRP_RELATNSHIP_TO_IND_CREATE_PCP_ID", sprint = "Sprint 192")
	public void IND_ADD_GRP_RELATNSHIP_TO_IND_CREATE_PCP_ID() {

		LOGGER.info("Inside test IND_ADD_GRP_RELATNSHIP_TO_IND_CREATE_PCP_ID");
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
					affilRelationPage.selectGrpRelationsTab();

					List<Affiliation> affiliationDetails = json.getAffiliations();
					if (affiliationDetails != null) {
						for (Affiliation affiliation : affiliationDetails) {

							result = affilRelationPage.searchGroupingRelations(affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation Grouping Relations details is not available"
									: "Affiliation Grouping Relations details available";
							resultSet.add(new ResultTuple("Affiliation Grouping Relations", "",
									affiliation.getAffiliationType() + " : " + affiliation.getGroupingCode() + " "
											+ affiliation.getAffiliationActive().getEffectiveDate() + " "
											+ affiliation.getAffiliationActive().getTerminationDate() + " "
											+ affiliation.getAffiliationActive().getTerminationReasonCode() + " ",
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

	@Test(description = "SPDS-141888 - Edit Individual - Term Affiliation, Network, and Reimbursement - Doc left Org", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-141888", author = "Shiva", transactionId = "IND_EDIT_EXIST_ADDR_TERM_ORG_AFF", sprint = "Sprint 192")
	public void IND_EDIT_EXIST_ADDR_TERM_ORG_AFF() {

		LOGGER.info("Inside test IND_EDIT_EXIST_ADDR_TERM_ORG_AFF");
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
						String affiliationId = affiliation.getAffiliatedLegacyID();
						affilRelationPage.searchAffilEid(affiliationId);

						List<Address> addresses = affiliation.getAddresses();
						for (Address address : addresses) {
							result = affilRelationPage.searchIndAffiliation(affiliation, address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation  details is not available"
									: "Affiliation  details is available";
							resultSet.add(new ResultTuple("Affiliation ", "",
									affiliation.getAffiliatedLegacyID() + " : "
											+ address.getAddressActive().getEffectiveDate() + " "
											+ address.getAddressActive().getTerminationDate() + " "
											+ address.getAddressActive().getTerminationReasonCode(),
									result, comment));
							if (result == FAILED)
								continue;

							loginPage.navToReimbNtwrkTab();

							result = reimbNtwrkPage.selectAvailableAddrCheckbox(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Record is not available with the matching address"
									: "Record is available with the matching address";
							resultSet.add(new ResultTuple("AvailableAddrCheckbox", "", "Expected address: "
									+ address.getAddressDetails().getAddressLine1() + "Expected Effective Date: "
									+ address.getAddressActive().getEffectiveDate() + "Expected Termination  date: "
									+ address.getAddressActive().getTerminationDate(), result, comment));

							if (result == FAILED)
								continue;

							result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED)
									? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
									: "Record is available with the matching Affiliation Legacy ID, effective date , termination date";
							resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
									"Expected ID: " + affiliation.getAffiliatedLegacyID() + "Expected Effective Date: "
											+ affiliation.getAffiliationActive().getEffectiveDate()
											+ "Expected Termination  date: "
											+ affiliation.getAffiliationActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							List<NetworkAffiliation> ntwrkAffiliations = affiliation.getNetworkAffiliations();
							for (NetworkAffiliation ntwrkAffiliation : ntwrkAffiliations) {

								result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffiliation) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Network ID, effective date , termination date"
										: "Record is available with the matching Network ID, effective date , termination date";
								resultSet.add(new ResultTuple("Network ID", "",
										"Expected ID: " + ntwrkAffiliation.getNetworkID() + "Expected Effective Date: "
												+ ntwrkAffiliation.getNetworkSourceSystem()
												+ "Expected Termination  date: "
												+ ntwrkAffiliation.getNetworkActive().getEffectiveDate()
												+ ntwrkAffiliation.getNetworkActive().getTerminationDate()
												+ ntwrkAffiliation.getNetworkActive().getTerminationReasonCode(),
										result, comment));
								if (result == FAILED)
									continue;

								String actNetwkTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
								String expTermDate = ntwrkAffiliation.getNetworkActive().getTerminationDate();

								if (!actNetwkTermDate.equals(expTermDate)) {
									resultSet.add(new ResultTuple("NetwkTermDate", actNetwkTermDate, expTermDate,
											FAILED, "NetwkTermDate not matching"));
								} else {
									resultSet.add(new ResultTuple("NetwkTermDate", actNetwkTermDate, expTermDate,
											PASSED, "NetwkTermDate matching"));
								}

								String actNetwkTermreasonCode = reimbNtwrkPage.getIndNetwrkTermReasonCode();
								String expTermReasonCode = Objects
										.toString(ntwrkAffiliation.getNetworkActive().getTerminationReasonCode(), "");

								if (!actNetwkTermreasonCode.equalsIgnoreCase(expTermReasonCode)) {
									resultSet.add(new ResultTuple("NetwkTermreasonCode", actNetwkTermreasonCode,
											expTermReasonCode, FAILED, "NetwkTermreasonCode not matching"));
								} else {
									resultSet.add(new ResultTuple("NetwkTermreasonCode", actNetwkTermreasonCode,
											expTermReasonCode, PASSED, "NetwkTermreasonCode matching"));
								}
								reimbNtwrkPage.indClickCancel();
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

	@Test(description = "SPDS-133019 - Edit Individual - Add/Edit/Term PDM Indicators", enabled = true, priority = 1, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133019", author = "Murali", transactionId = "IND_ADD_EDIT_TERM_PDM_IND", sprint = "Sprint 192")
	public void IND_ADD_EDIT_TERM_PDM_IND() {

		LOGGER.info("Inside test IND_ADD_EDIT_TERM_PDM_IND");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					loginPage.navToProfileTab();
					List<PdmIndicatorDetail> pdmDetailList = json.getProfileAdditionalDetails()
							.getPdmIndicatorDetails();
					if (pdmDetailList != null) {
						for (PdmIndicatorDetail pdmIndDetail : pdmDetailList) {
							result = profilePage.searchPDMIndicators(pdmIndDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "PDM record is not available with the matching values"
									: "PDM record is available with the matching values";
							resultSet.add(new ResultTuple("PDM details", "",
									"Expected PDMIndicator: " + pdmIndDetail.getPdmIndicator()
											+ " Expected PDM effective date: "
											+ pdmIndDetail.getPdmIndicatorActive().getEffectiveDate()
											+ " Expected Network termination date: "
											+ pdmIndDetail.getPdmIndicatorActive().getTerminationDate(),
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

	@Test(description = "SPDS-133017 - Edit Individual - Edit/Term Claim Action-Sanction", enabled = true, priority = 3, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133017", author = "Murali", transactionId = "IND_EDIT_TERM_CLAIM_ACTION_SANCTION", sprint = "Sprint 192")
	public void IND_EDIT_TERM_CLAIM_ACTION_SANCTION() {

		LOGGER.info("Inside test IND_EDIT_TERM_CLAIM_ACTION_SANCTION");
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
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					loginPage.navToClaimActSanctTab();
					List<ClaimActionSanction> claimActionList = json.getClaimActionSanctions();
					if (claimActionList != null) {
						for (ClaimActionSanction claimActnDetail : claimActionList) {
							result = claimActnSnctnPage.searchEditClaimActions(claimActnDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Claim record is not available with the matching values"
									: "Claim record is available with the matching values";
							resultSet
									.add(new ResultTuple("Claim details", "",
											"Expected Claim value: " + claimActnDetail.getClaimActionSanctionValue()
													+ " Expected Claim effective date: "
													+ claimActnDetail.getClaimActionSanctionActive().getEffectiveDate()
													+ " Expected Claim termination date: " + claimActnDetail
															.getClaimActionSanctionActive().getTerminationDate(),
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

	@Test(description = "SPDS-132997 - Edit Individual existing practice address - Specialty", enabled = true, priority = 3, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132997", author = "Murali", transactionId = "IND_EXIST_ADDR_EDIT_SPECIALTY", sprint = "Sprint 192")
	public void IND_EXIST_ADDR_EDIT_SPECIALTY() {

		LOGGER.info("Inside test IND_EXIST_ADDR_EDIT_SPECIALTY");
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
					SpecialityTaxonomy spltyTxnmyPage = new SpecialityTaxonomy(driver);
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

					loginPage.navToSpecialityTaxonomyTab();
					List<Specialty> specialties = json.getSpecialties();
					for (Specialty speciality : specialties) {
						for (Address address : speciality.getAddresses()) {
							String srchAddress = address.getAddressDetails().getAddressLine1() + " "
									+ address.getAddressDetails().getCity() + " "
									+ address.getAddressDetails().getState() + " "
									+ address.getAddressDetails().getZip();

							result = spltyTxnmyPage.searchSpecialityDetails(address, speciality) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Speciality record is not available"
									: "Speciality record is available";
							resultSet.add(new ResultTuple("Speciality", "", speciality.getSpecialtyCode() + " : "
									+ speciality.getSpecialtyActive().getEffectiveDate() + " "
									+ speciality.getSpecialtyActive().getTerminationDate() + " " + srchAddress + " ",
									result, comment));
							if (result == FAILED)
								continue;
						}
					}

					List<Address> addresses = json.getAddresses();
					for (Address addressDetails : addresses) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectIndAddress(addressDetails) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "",
								addressDetails.getAddressDetails().getAddressType() + " : "
										+ addressDetails.getAddressDetails().getAddressLine1() + " "
										+ addressDetails.getAddressDetails().getCity() + " "
										+ addressDetails.getAddressDetails().getState() + " "
										+ addressDetails.getAddressDetails().getZip() + " : "
										+ addressDetails.getAddressActive().getEffectiveDate() + " : "
										+ addressDetails.getAddressActive().getTerminationDate(),
								result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToAltIdsTab();
						result = addressPage.valAltIdTypeSource(expAltId, expAltSource) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Alt ID record is not available"
								: "Alt ID record is available";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltSource, result, comment));
						if (result == FAILED)
							continue;

						// checking speciality
						addressPage.navToSpecialty();
						List<Specialty> specialtiy = json.getSpecialties();
						if (specialties != null) {
							for (Specialty specialityDetails : specialtiy) {
								String firstSpecialityCode = specialties.get(0).getSpecialtyCode();
								boolean specialityStatus = addressPage.valSpecialtyCount(firstSpecialityCode);
								if (!specialityStatus) {
									result = "FAILED";
									comment = result.equals(FAILED)
											? "More than one Speciality record is not available for the practice address"
											: "More than one Speciality record is available for the practice address";
									resultSet.add(new ResultTuple("Speciality", "",
											specialityDetails.getSpecialtyCode(), result, comment));
									break;
								} else {
									result = addressPage.valSpecialty(specialityDetails, ProviderType.INDIVIDUAL)
											? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "Speciality record is not available"
											: "Speciality record is available";
									resultSet.add(new ResultTuple("Speciality", "",
											specialityDetails.getSpecialtyCode(), result, comment));
									if (result == FAILED)
										continue;
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

	@Test(description = "SPDS-133319- Edit Individual - Modify existing Practice Address", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133319", author = "Murali", transactionId = "IND_EDIT_EXIST_ADDR", sprint = "Sprint 192")
	public void IND_EDIT_EXIST_ADDR() {

		LOGGER.info("Inside test IND_EDIT_EXIST_ADDR");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String expAltId = "Q/Care License ID";
		String expAltIdSrc = "QCARE";
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

					LOGGER.info("Validating Individual Address Details...");
					for (Address addressDetail : json.getAddresses()) {

						loginPage.navToAddressTab();
						if (!addressDetail.getAddressActive().getTerminationDate()
								.equals(EpdsConstants.TERMINATION_DATE)) {

							result = addressPage.filterAndSelectIndAddress(addressDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Address record is not available"
									: "Address record is available";
							resultSet.add(new ResultTuple("Address", "",
									addressDetail.getAddressDetails().getAddressType() + " : "
											+ addressDetail.getAddressDetails().getAddressLine1() + " "
											+ addressDetail.getAddressDetails().getCity() + " "
											+ addressDetail.getAddressDetails().getState() + " "
											+ addressDetail.getAddressDetails().getZip() + " : "
											+ addressDetail.getAddressActive().getEffectiveDate() + " : "
											+ addressDetail.getAddressActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							addressPage.navToAltIdsTab();
							result = addressPage.valAltIdTypeSource(expAltId, expAltIdSrc) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Alt ID record is not available"
									: "Alt ID record is not available";
							resultSet.add(
									new ResultTuple("Alt Id", "", expAltId + " : " + expAltIdSrc, result, comment));
							if (result == FAILED)
								continue;

							String expaddrTermDate = addressDetail.getAddressActive().getTerminationDate();
							String expTermReasonCode = Objects
									.toString(addressDetail.getAddressActive().getTerminationReasonCode(), "");

							String actAddrTermDate = addressPage.getAddrTermDate();
							String actTermReasonCode = addressPage.getAddrTermReasonCode();
							if (!actAddrTermDate.equalsIgnoreCase(expaddrTermDate)) {
								resultSet.add(new ResultTuple("Termination date", actAddrTermDate, expaddrTermDate,
										FAILED, "Termination date not matching"));
							} else {
								resultSet.add(new ResultTuple("Termination date", actAddrTermDate, expaddrTermDate,
										PASSED, "Termination date is matching"));
							}

							if (!actTermReasonCode.equalsIgnoreCase(expTermReasonCode)) {
								resultSet.add(new ResultTuple("Termination reason", actAddrTermDate, expTermReasonCode,
										FAILED, "Termination reason not matching"));
							} else {
								resultSet.add(new ResultTuple("Termination reason", actTermReasonCode,
										expTermReasonCode, PASSED, "Termination reason is matching"));
							}
						}

						else {
							result = addressPage.filterAndSelectIndAddress(addressDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Address record is not available"
									: "Address record is available";
							resultSet.add(new ResultTuple("Address", "",
									addressDetail.getAddressDetails().getAddressType() + " : "
											+ addressDetail.getAddressDetails().getAddressLine1() + " "
											+ addressDetail.getAddressDetails().getCity() + " "
											+ addressDetail.getAddressDetails().getState() + " "
											+ addressDetail.getAddressDetails().getZip() + " : "
											+ addressDetail.getAddressActive().getEffectiveDate() + " : "
											+ addressDetail.getAddressActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							String expAddressLine1 = addressDetail.getAddressDetails().getAddressLine1();
							String expAddressLine2 = Objects
									.toString(addressDetail.getAddressDetails().getAddressLine2(), "");
							String expZipCode = addressDetail.getAddressDetails().getZip();
							String expAddrEffDate = addressDetail.getAddressActive().getEffectiveDate();
							String expAddrTermDate = addressDetail.getAddressActive().getTerminationDate();
							String expTermReasonCode = Objects
									.toString(addressDetail.getAddressActive().getTerminationReasonCode(), "");

							Boolean isPrimaryPracInd = addressDetail.getAddressDetails().getPrimaryPracticeIndicator();
							String expPrimaryPracInd = null;
							if (isPrimaryPracInd) {
								expPrimaryPracInd = "Y";
							} else {
								expPrimaryPracInd = "N";
							}

							String actAddressLine1 = addressPage.getAddressLine1();
							String actAddressLine2 = addressPage.getAddressLine2();
							String actZipCode = addressPage.getAddrZip();
							String actAddrEffDate = addressPage.getAddrEffectDate();
							String actAddrTermDate = addressPage.getAddrTermDate();
							String actTermReassonCode = addressPage.getAddrTermReasonCode();
							String actPrimaryPracInd = addressPage.getPracticeIndicator();

							if (!actAddressLine1.equalsIgnoreCase(expAddressLine1)) {
								resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1, FAILED,
										"AddressLine1 not matching"));
							} else {
								resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1, PASSED,
										"AddressLine1 is matching"));
							}

							if (!actAddressLine2.equalsIgnoreCase(expAddressLine2)) {
								resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2, FAILED,
										"AddressLine2 not matching"));
							} else {
								resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2, PASSED,
										"AddressLine2 is matching"));
							}

							if (!actPrimaryPracInd.equalsIgnoreCase(expPrimaryPracInd)) {
								resultSet.add(new ResultTuple("	Primary Practice Indicator", actPrimaryPracInd,
										expPrimaryPracInd, FAILED, "Primary Practice Indicator not matching"));
							} else {
								resultSet.add(new ResultTuple("Primary Practice Indicator", actPrimaryPracInd,
										expPrimaryPracInd, PASSED, "Primary Practice Indicator is matching"));
							}

							if (!actZipCode.equals(expZipCode)) {
								resultSet.add(new ResultTuple("Zip code", actZipCode, expZipCode, FAILED,
										"Zipcode not matching"));
							} else {
								resultSet.add(
										new ResultTuple("Zip code", actZipCode, expZipCode, PASSED, "Zip is matching"));
							}

							if (!actAddrEffDate.equals(expAddrEffDate)) {
								resultSet.add(new ResultTuple("Effective date", actAddrEffDate, expAddrEffDate, FAILED,
										"Effective date not matching"));
							} else {
								resultSet.add(new ResultTuple("Effective date", actAddrEffDate, expAddrEffDate, PASSED,
										"Effective date is matching"));
							}

							if (!actAddrTermDate.equals(expAddrTermDate)) {
								resultSet.add(new ResultTuple("Termination date", actAddrTermDate, expAddrTermDate,
										FAILED, "Termination date not matching"));
							} else {
								resultSet.add(new ResultTuple("Termination date", actAddrTermDate, expAddrTermDate,
										PASSED, "Termination date is matching"));
							}

							if (!actTermReassonCode.equalsIgnoreCase(expTermReasonCode)) {
								resultSet.add(new ResultTuple("Termination reason", actAddrTermDate, expTermReasonCode,
										FAILED, "Termination reason not matching"));
							} else {
								resultSet.add(new ResultTuple("Termination reason", actTermReassonCode,
										expTermReasonCode, PASSED, "Termination reason is matching"));
							}

							// checking contact table
							addressPage.navToContactTab();
							Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

							List<ContactDetail> contactDetails = addressDetail.getAddressAdditionalDetails()
									.getContactDetails();
							if (contactDetails != null) {
								for (ContactDetail contactDetail : contactDetails) { // contact table
									result = addressPage.searchContactTable(contactDetail, ProviderType.INDIVIDUAL)
											? PASSED
											: FAILED;
									comment = result.equals(FAILED)
											? "Contact record is not available with the matching values"
											: "Contact record is available with the matching values";
									resultSet.add(new ResultTuple("Contact details", "",
											"Expected First Name: " + contactDetail.getFirstName()
													+ " Expected Last Name: " + contactDetail.getLastName()
													+ " Expected Middle Name: " + contactDetail.getMiddleName()
													+ " Expected Phone : " + contactDetail.getPhone()
													+ " Expected Fax: " + contactDetail.getFax(),
											result, comment));
								}
							}
							Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

							// Checking AREA OF FOCUS
							addressPage.navToAreaOfFocusTab();
							List<AreasOFocus> areasOfFocus;
							areasOfFocus = addressDetail.getAddressAdditionalDetails().getAreasOFocus();
							if (!areasOfFocus.isEmpty()) {
								for (AreasOFocus areaOfFocus : areasOfFocus) {
									if (areaOfFocus.getAreaOfFocusActive().getTerminationReasonCode() != null) {
										result = addressPage.valAreaOfFocusMatch(areaOfFocus.getAreaOfFocus()) ? PASSED
												: FAILED;
										if (result == PASSED)
											comment = "Field is added";
										else
											comment = "Field is not added";
										resultSet.add(new ResultTuple("Area of Focus", "", areaOfFocus.getAreaOfFocus(),
												result, comment));
									}
								}
							}

							addressPage.navToOfficeDetailsTab();
							List<OfficeDetail> officeDetailList = addressDetail.getAddressAdditionalDetails()
									.getOfficeDetails();
							if (officeDetailList != null) {
								for (OfficeDetail officeDetail : officeDetailList) {
									List<String> officeAccessCode = officeDetail.getAccessibilityCode();
									if (officeAccessCode != null) {
										for (String accessibilityCode : officeAccessCode) {
											result = addressPage.valOfficeAccessCode(accessibilityCode,
													ProviderType.INDIVIDUAL) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "Office Accessibility Code is not available"
													: "Office Accessibility Code is available";
											resultSet.add(new ResultTuple("Office Accessibility Code", "",
													accessibilityCode, result, comment));
										}
									}
								}
							}

							// checking speciality
							addressPage.navToSpecialty();
							List<Specialty> spltyList = json.getSpecialties();
							if (spltyList != null) {
								for (Specialty speciality : spltyList) {
									result = addressPage.valSpecialty(speciality, ProviderType.INDIVIDUAL) ? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "Speciality record is not available"
											: "Speciality record is available";
									resultSet.add(new ResultTuple("Speciality", "", speciality.getSpecialtyCode(),
											result, comment));
									if (result == FAILED)
										continue;
								}
							}

							// checking SPECIALITY PROGRAM
							addressPage.navToSpecialPrgmTab();
							List<SpecialProgram> splPrgmList = addressDetail.getAddressAdditionalDetails()
									.getSpecialPrograms();
							if (splPrgmList != null) {
								for (SpecialProgram specialProgram : splPrgmList) {
									String expSplPrgmType = specialProgram.getProviderSpecialProgramType();
									String expSplPrgmEffDate = specialProgram.getProgramActive().getEffectiveDate();
									String expSplPrgmTermDate = specialProgram.getProgramActive().getTerminationDate();
									String expSplPrgmTermCode = Objects
											.toString(specialProgram.getProgramActive().getTerminationReasonCode(), "");

									result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate) ? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "SpecialProgram records is not available"
											: "SpecialProgram records is available";
									resultSet
											.add(new ResultTuple("SpecialProgram", "",
													expSplPrgmType + " : " + expSplPrgmEffDate + ", "
															+ expSplPrgmTermDate + ", " + expSplPrgmTermCode,
													result, comment));
								}
							}
						}
						addressPage.clickAddressCloseBtn();
						Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
					}

					List<Affiliation> affiliationList = json.getAffiliations();
					if (affiliationList != null) {
						for (Affiliation affilDetail : affiliationList) {
							String providerEId = affilDetail.getAffiliatedLegacyID();
							List<Address> addresses = affilDetail.getAddresses();
							if (addresses != null) {
								for (Address address : addresses) {
									loginPage.navToAffilRelationTab();
									affilRelationPage.searchAffilEid(providerEId);
									result = affilRelationPage.searchIndAffiliation(affilDetail, address) ? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "Affiliation  details is not available"
											: "Affiliation  details is available";
									resultSet.add(new ResultTuple("Affiliation ", "",
											affilDetail.getAffiliatedLegacyID() + " " + affilDetail.getAffiliationType()
													+ " " + address.getAddressActive().getEffectiveDate() + " "
													+ address.getAddressActive().getTerminationDate() + " "
													+ address.getAddressActive().getTerminationReasonCode(),
											result, comment));
									if (result == FAILED)
										continue;
								}
							}
						}
					}

					for (Affiliation affilDetail : affiliationList) {
						loginPage.navToReimbNtwrkTab();
						List<Address> addresses = affilDetail.getAddresses();
						LOGGER.info("Inside checking NETWORK and REIMBURSEMENT tab");
						if (addresses != null) {
							for (Address address : addresses) {

								result = reimbNtwrkPage.selectAvailableAddrCheckbox(address) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Record is not available with the matching address"
										: "Record is available with the matching address";
								resultSet.add(new ResultTuple("AvailableAddrCheckbox", "", "Expected address: "
										+ address.getAddressDetails().getAddressLine1() + "Expected Effective Date: "
										+ address.getAddressActive().getEffectiveDate() + "Expected Termination  date: "
										+ address.getAddressActive().getTerminationDate(), result, comment));
								if (result == FAILED)
									continue;

								result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affilDetail) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
										: "Record is available with the matching Affiliation Legacy ID, effective date , termination date";
								resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
										"Expected ID: " + affilDetail.getAffiliatedLegacyID()
												+ "Expected Effective Date: "
												+ affilDetail.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affilDetail.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								List<NetworkAffiliation> ntwrkAffilList = affilDetail.getNetworkAffiliations();
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

										result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Record is not available with the matching Network ID, effective date , termination date"
												: "Record is available with the matching Network ID, effective date , termination date";
										resultSet.add(new ResultTuple("Network ID", "",
												"Expected ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Effective Date: "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Termination  date: "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;
										Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

										String actNetId = reimbNtwrkPage.getIndNetwrkId();
										String actNetEffDate = reimbNtwrkPage.getIndNetwrkEffDate();
										String actTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
										String actTermReasonCode = reimbNtwrkPage.getIndNetwrkTermReasonCode();
										String actTimlyfiling = reimbNtwrkPage.getIndTimlyFiling();
										String accPatientsInd = reimbNtwrkPage.getIndAcceptPatientsIndicator();
										String actNetDirInd = reimbNtwrkPage.getIndNetDirIndicator();
										String actAgeFrom = reimbNtwrkPage.getIndAgeFrom();
										String actAgeTo = reimbNtwrkPage.getIndAgeTo();
										String actPatientGender = reimbNtwrkPage.getIndPatientGender();

										LOGGER.info("Actual Network Effective Date" + actNetEffDate);
										LOGGER.info("Actual termination date: " + actTermDate);
										LOGGER.info("Actual Termination reason code : " + actTermReasonCode);
										LOGGER.info("Actual Timily filing: " + actTimlyfiling);
										LOGGER.info("Actual accept patients indicator: " + accPatientsInd);
										LOGGER.info("Actual Network Directory Indicator" + actNetDirInd);

										String expNetID = ntwrkAffil.getNetworkID();
										String expEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
										String expTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
										String expTermReasonCode = Objects
												.toString(ntwrkAffil.getNetworkActive().getTerminationReasonCode(), "");
										String expNetDirInd = ntwrkAffil.getDirectoryIndicator();
										LOGGER.info("Expected directory indicator : " + expNetDirInd);

										String expNetAgeFrom = Objects
												.toString(ntwrkAffil.getPatientPreferences().getAgeFrom(), "");
										String expNetAgeTo = Objects
												.toString(ntwrkAffil.getPatientPreferences().getAgeTo(), "");
										String expGenderPref = Objects.toString(
												ntwrkAffil.getPatientPreferences().getPatientGenderPreference(), "");

										result = actNetId.equalsIgnoreCase(expNetID) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network ID is not matching with the actual value"
												: "The Network ID is matching with the actual value";
										resultSet.add(new ResultTuple("Network ID ", "Actual value: " + actNetId,
												"Expected : " + expNetID, result, comment));

										result = actNetEffDate.equals(expEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network effective date is not matching with the actual value"
												: "The Network effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Network effective date ",
												"Actual value: " + actNetEffDate, "Expected : " + expEffDate, result,
												comment));

										result = actTermDate.equals(expTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actTermDate, "Expected : " + expTermDate, result,
												comment));

										result = actTermReasonCode.equalsIgnoreCase(expTermReasonCode) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The termination reason code is not matching with the actual value"
												: "The termination reason code is matching with the actual value";
										resultSet.add(new ResultTuple("Termination reason code",
												"Actual value: " + actTermReasonCode,
												" Expected : " + expTermReasonCode, result, comment));

										result = actNetDirInd.equalsIgnoreCase(expNetDirInd) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Networking directory indicator is not matching with the actual value"
												: "The Networking directory indicator is matching with the actual value";
										resultSet.add(new ResultTuple("Networking directory indicator ",
												"Actual value: " + actNetDirInd, " Expected : " + expNetDirInd, result,
												comment));

										result = actAgeFrom.equals(expNetAgeFrom) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Age From value is not matching with the actual value"
												: "Age From value is matching with the actual value";
										resultSet.add(new ResultTuple("Age From ", "Actual value: " + actAgeFrom,
												" Expected : " + expNetAgeFrom, result, comment));

										result = actAgeTo.equals(expNetAgeTo) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Age To value is not matching with the actual value"
												: "Age To value is matching with the actual value";
										resultSet.add(new ResultTuple("Age To ", "Actual value: " + actAgeTo,
												" Expected : " + expNetAgeTo, result, comment));

										result = actPatientGender.equalsIgnoreCase(expGenderPref) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Gender Preference value is not matching with the actual value"
												: "Gender Preference value is matching with the actual value";
										resultSet.add(new ResultTuple("Gender Preference",
												"Actual value: " + actPatientGender, " Expected : " + expGenderPref,
												result, comment));

										List<TimelyFilingInd> timelyFilingIndList = ntwrkAffil.getTimelyFilingInds();
										if (timelyFilingIndList != null) {
											for (TimelyFilingInd timelyFilingInd : timelyFilingIndList) {
												LOGGER.info("Timely Filing Indicators: "
														+ timelyFilingInd.getTimelyFiling());

												result = timelyFilingInd.getTimelyFiling()
														.equalsIgnoreCase(actTimlyfiling) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The Timily filing value is not matching with the actual value"
														: "The Timily filing value is matching with the actual value";
												resultSet.add(new ResultTuple(" Timily filing value ",
														"Actual value: " + actTimlyfiling,
														"Expected : " + timelyFilingInd.getTimelyFiling(), result,
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
														: "The Accepting patients indicator is matching with the actual value";
												resultSet.add(new ResultTuple("Accepting patients indicator ",
														"Actual value: " + accPatientsInd,
														"Expected : "
																+ expAccPatientsInd.getAcceptingPatientsIndicator(),
														result, comment));
											}
										}

										List<Reimbursement> reimbursementList = affilDetail.getReimbursements();
										if (reimbursementList != null) {
											for (Reimbursement reimbursement : reimbursementList) {
												if (reimbursement.getNetworkID().equals(ntwrkAffil.getNetworkID()))
													LOGGER.info(
															"Reimbursement Network ID and Network affiliation network ID matches");
												{

													result = reimbNtwrkPage.valIndAssociatedReimbTable(reimbursement)
															? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Record is not available with the matching Reimbursement value, effective date , termination date"
															: "Record is available with the matching Reimbursement value, effective date , termination date";
													resultSet
															.add(new ResultTuple("Reimbursement value", "",
																	"Expected reimbursement value: "
																			+ reimbursement.getReimbursementValue()
																			+ "Expected Effective Date: "
																			+ reimbursement.getReimbursementActive()
																					.getEffectiveDate()
																			+ "Expected Termination  date: "
																			+ reimbursement.getReimbursementActive()
																					.getTerminationDate(),
																	result, comment));
													if (result == FAILED)
														continue;

													LOGGER.info("Inside Individual reimbursement page");
													String expReimbValue = reimbursement.getReimbursementValue();
													String expReimbEffDate = reimbursement.getReimbursementActive()
															.getEffectiveDate();
													String expReimbTermDate = reimbursement.getReimbursementActive()
															.getTerminationDate();
													String expReimbTermReasonCode = Objects.toString(reimbursement
															.getReimbursementActive().getTerminationReasonCode(), "");

													String actReimbValue = reimbNtwrkPage.getIndReimbValue();
													String actReimbEffDate = reimbNtwrkPage.getIndReimbEffDate();
													String actReimbTermDate = reimbNtwrkPage.getIndReimbTermDate();
													String actReimbReasonCode = reimbNtwrkPage
															.getIndReimbTermReasonCode();

													LOGGER.info("Actual reimbursement value: " + actReimbValue);
													LOGGER.info("Actual Effective Date : " + actReimbEffDate);

													result = actReimbValue.equalsIgnoreCase(expReimbValue) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Reimbursement Value value is not matching with the actual value"
															: "Reimbursement Value value is matching with the actual value";
													resultSet.add(new ResultTuple("Reimbursement Value ",
															"Actual value: " + actReimbValue,
															" Expected : " + expReimbValue, result, comment));

													result = actReimbEffDate.equals(expReimbEffDate) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Reimbursement Effective Date value is not matching with the actual value"
															: "Reimbursement Effective Date value is matching with the actual value";
													resultSet.add(new ResultTuple("Reimbursement Effective Date ",
															"Actual value: " + actReimbEffDate,
															" Expected : " + expReimbEffDate, result, comment));

													result = actReimbTermDate.equals(expReimbTermDate) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Reimbursement Termination Date value is not matching with the actual value"
															: "Reimbursement Termination Date value is matching with the actual value";
													resultSet.add(new ResultTuple("Reimbursement Termination Date",
															"Actual value: " + actReimbTermDate,
															" Expected : " + expReimbTermDate, result, comment));

													result = actReimbReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
															? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Reimbursement Termination Reason Code value is not matching with the actual value"
															: "Reimbursement Termination Reason Code value is matching with the actual value";
													resultSet.add(new ResultTuple(
															"Reimbursement Termination Reason Code",
															"Actual value: " + actReimbReasonCode,
															" Expected : " + expReimbTermReasonCode, result, comment));

													reimbNtwrkPage.indReimbClickCancel();
													Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
												}
											}
										}
										reimbNtwrkPage.indClickCancel();
										Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
									}
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

}
