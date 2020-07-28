package com.tests.epds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.ProviderDistinctionDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.SpecialProgram;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.pages.EPDSPageObjects.ReimbusermentNetworks;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class RpaOrgTestsS192 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaOrgTestsS192.class);

	@Test(description = "SPDS-133320 - Edit Organization existing practice address - Add/Edit Office Services", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133320", author = "Reshmi C", transactionId = "ORG_EXIST_ADDR_EDIT_OFFICE_SERVICES", sprint = "Sprint 192")
	public void ORG_EXIST_ADDR_EDIT_OFFICE_SERVICES() {

		LOGGER.info("Inside test ORG_EXIST_ADDR_EDIT_OFFICE_SERVICES");
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
					AddressPage addressPage = new AddressPage(driver);

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

					for (Address address : json.getAddresses()) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToOfficeServicesTab();
						String officeServiceDetails = addressPage.getOfficeServiceDetail();
						if (officeServiceDetails.equals("No records"))
							officeServiceDetails = "";

						List<String> officeServiceTypes = address.getAddressAdditionalDetails().getOfficeServiceTypes();
						if (officeServiceTypes != null) {
							for (String officeServiceType : officeServiceTypes) {
								if (!officeServiceDetails.contains(officeServiceType)) {
									resultSet.add(new ResultTuple("Office Service Type", officeServiceDetails,
											officeServiceType, FAILED, "Addition failed"));
								} else {
									resultSet.add(new ResultTuple("Office Service Type", officeServiceDetails,
											officeServiceType, PASSED, "Addition success"));
									officeServiceDetails = officeServiceDetails.replaceAll(officeServiceType, "");
								}
							}
						}

						if (officeServiceDetails.matches(".*[a-zA-Z]+.*")) {
							// Office Service Type removal failed
							resultSet.add(new ResultTuple("Office Service Type", officeServiceDetails, "", FAILED,
									"Removal failed"));
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

	@Test(description = "SPDS-133000 - Edit Organization existing practice address - Add/Edit Schedule Details", enabled = true, groups = {
			"org" }, priority = 1)
	@TestDetails(userstory = "SPDS-133000", author = "Reshmi C", transactionId = "ORG_EXIST_ADDR_EDIT_SCHEDULE_DETAILS", sprint = "Sprint 192")
	public void ORG_EXIST_ADDR_EDIT_SCHEDULE_DETAILS() {

		LOGGER.info("Inside test ORG_EXIST_ADDR_EDIT_SCHEDULE_DETAILS");
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
					AddressPage addressPage = new AddressPage(driver);

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

					for (Address address : json.getAddresses()) {

						List<SchedulingDetail> schedulingDetails = address.getAddressAdditionalDetails()
								.getSchedulingDetails();
						if (schedulingDetails != null) {

							loginPage.navToAddressTab();
							result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Address record is not available"
									: "Address record is available";
							resultSet.add(new ResultTuple("Address", "",
									address.getAddressDetails().getAddressType() + " : "
											+ address.getAddressDetails().getAddressLine1() + " "
											+ address.getAddressDetails().getCity() + " "
											+ address.getAddressDetails().getState() + " "
											+ address.getAddressDetails().getZip() + " : "
											+ address.getAddressActive().getEffectiveDate() + " : "
											+ address.getAddressActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							addressPage.navToScheduleTab();
							for (SchedulingDetail schedulingDetail : schedulingDetails) {
								result = addressPage.valSchedulingDetail(schedulingDetail) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Scheduling Detail record is not available"
										: "Scheduling Detail record is available";
								resultSet.add(new ResultTuple("Schedule Detail", "", schedulingDetail.getDays() + " : "
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

	@Test(description = "SPDS-132968 - Edit Organization Profile Term Date and Term Reason", enabled = true, groups = {
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

					if (!actTermDate.equals(expTermDate)) {
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

					if (!actTermreasonCode.equalsIgnoreCase(expTermReasonCode)) {
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

					String expOrgName = Objects
							.toString(json.getProfile().getNameQualifier().getOrgNameQualifier().getOrgName(), "");

					String actOrgName = Objects.toString(profilePage.getOrgProvName(), "");

					if (!actOrgName.equalsIgnoreCase(expOrgName)) {
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

	@Test(description = "SPDS-133014 - Edit Organization remit address - add/edit Contact info", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133014", author = "Shiva", transactionId = "ORG_EDIT_REMIT_ADDR_ADD_EDIT_CONTACT_INFO", sprint = "SPS RPA 192")
	public void ORG_EDIT_REMIT_ADDR_ADD_EDIT_CONTACT_INFO() {

		LOGGER.info("Inside test ORG_EDIT_REMIT_ADDR_ADD_EDIT_CONTACT_INFO");
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
					AddressPage addressPage = new AddressPage(driver);

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
					LOGGER.info("Organization provider is present...");

					List<Address> addresses = json.getAddresses();
					for (Address addressDetails : addresses) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(addressDetails) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "",
								addressDetails.getAddressDetails().getAddressType() + " : "
										+ addressDetails.getAddressDetails().getAddressLine1() + ", "
										+ addressDetails.getAddressDetails().getCity() + ", "
										+ addressDetails.getAddressDetails().getState() + ", "
										+ addressDetails.getAddressDetails().getZip() + " : "
										+ addressDetails.getAddressActive().getEffectiveDate() + " : "
										+ addressDetails.getAddressActive().getTerminationDate(),
								result, comment));
						if (result == FAILED)
							continue;
						LOGGER.info("Remit address selected...");

						List<ContactDetail> contactDetails = addressDetails.getAddressAdditionalDetails()
								.getContactDetails();
						if (contactDetails != null) {
							for (ContactDetail contactDetail : contactDetails) {
								result = addressPage.searchContactTable(contactDetail, ProviderType.ORGANIZATION)
										? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "ContactDetail is not available"
										: "ContactDetail is available";
								resultSet.add(new ResultTuple("ContactDetail", "",
										contactDetail.getFirstName() + " : " + contactDetail.getMiddleName() + ", "
												+ contactDetail.getLastName() + ", " + contactDetail.getPhone() + ", "
												+ contactDetail.getFax() + " : " + contactDetail.getEmail(),
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

	@Test(description = "SPDS-133049 - Edit Organization remit address - change Remittance Check Name", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133049", author = "Shiva", transactionId = "ORG_EDIT_REMIT_ADDR_CHANGE_REMITTANCE_CHECK", sprint = "Sprint 192")

	public void ORG_EDIT_REMIT_ADDR_CHANGE_REMITTANCE_CHECK() {

		LOGGER.info("Inside test ORG_EDIT_REMIT_ADDR_CHANGE_REMITTANCE_CHECK");
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
					AddressPage addressPage = new AddressPage(driver);

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

					LOGGER.info("Organization provider is present...");
					for (Address address : json.getAddresses()) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						LOGGER.info("Pratice address selected...");

						List<RemittanceDetail> remitDetails = address.getAddressAdditionalDetails()
								.getRemittanceDetails();
						if (remitDetails != null) {
							addressPage.navToRemitInfoTab();
							for (RemittanceDetail remitDetail : remitDetails) {
								result = addressPage.searchRemitInfoTable(remitDetail) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "RemittanceDetail record is not available"
										: "RemittanceDetail record is available";

								resultSet.add(new ResultTuple("RemittanceDetail", "",
										remitDetail.getCheckName() + " : "
												+ remitDetail.getRemittanceActive().getEffectiveDate() + " "
												+ remitDetail.getRemittanceActive().getTerminationDate() + " "
												+ remitDetail.getRemittanceActive().getTerminationReasonCode() + " "
												+ remitDetail.getRemittanceNPI().getNpiValue(),
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

	// SPDS-133001 is abandoned
	@Test(description = "SPDS-133001 - Edit Organization existing practice address - Add/Edit Special Programs", enabled = false, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133001", author = "Shiva", transactionId = "ORG_EXIST_PRACTICE_ADDR_EDIT_SPECIAL_PROGRAMS", sprint = "Sprint 192")
	public void ORG_EXIST_PRACTICE_ADDR_EDIT_SPECIAL_PROGRAMS() {

		LOGGER.info("Inside test ORG_EXIST_PRACTICE_ADDR_EDIT_SPECIAL_PROGRAMS");
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
					AddressPage addressPage = new AddressPage(driver);

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
					LOGGER.info("Organization provider is present...");

					List<Address> addresses = json.getAddresses();
					for (Address address : addresses) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToOrgSpecialPrgmTab();
						List<SpecialProgram> splPrgms = address.getAddressAdditionalDetails().getSpecialPrograms();
						if (splPrgms != null) {
							for (SpecialProgram splPrgm : splPrgms) {
								String expSplPrgmType = splPrgm.getProviderSpecialProgramType();
								String expSplPrgmEffDate = splPrgm.getProgramActive().getEffectiveDate();
								String expSplPrgmTermDate = splPrgm.getProgramActive().getTerminationDate();
								String expSplPrgmTermCode = splPrgm.getProgramActive().getTerminationReasonCode();

								result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate) ? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "SpecialProgram records is not available"
										: "SpecialProgram records is available";
								resultSet
										.add(new ResultTuple(
												"SpecialProgram", "", expSplPrgmType + " : " + expSplPrgmEffDate + ", "
														+ expSplPrgmTermDate + ", " + expSplPrgmTermCode,
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

	@Test(description = "SPDS-132998 -  Edit Organization existing practice address - Add/Edit Contact info", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132998", author = "Shiva", transactionId = "ORG_EDIT_PRACTICE_ADDR_ADD_EDIT_CONTACT_INFO", sprint = "Sprint 192")
	public void ORG_EDIT_PRACTICE_ADDR_ADD_EDIT_CONTACT_INFO() {

		LOGGER.info("Inside test ORG_EDIT_PRACTICE_ADDR_ADD_EDIT_CONTACT_INFO");
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
					AddressPage addressPage = new AddressPage(driver);

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
								new ResultTuple("Provider", "", entID, PASSED, "Organization Provider is present"));
					}
					LOGGER.info("Organization provider is present...");

					List<Address> addresses = json.getAddresses();
					for (Address addressDetail : addresses) {

						List<ContactDetail> contactDetails = addressDetail.getAddressAdditionalDetails()
								.getContactDetails();
						if (contactDetails != null) {

							loginPage.navToAddressTab();
							result = addressPage.filterAndSelectOrgAddress(addressDetail) ? PASSED : FAILED;
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

							addressPage.navToContactTab();
							for (ContactDetail contactDetail : contactDetails) {
								result = addressPage.searchContactTable(contactDetail, ProviderType.ORGANIZATION)
										? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "Contact Details is not available"
										: "Contact Details is available";

								resultSet.add(new ResultTuple("ContactDetail", "",
										contactDetail.getFirstName() + " : " + contactDetail.getMiddleName() + " "
												+ contactDetail.getLastName() + " " + contactDetail.getPhone() + " "
												+ contactDetail.getFax() + " : " + contactDetail.getEmail() + " : "
												+ contactDetail.getWebAddress(),
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

	@Test(description = "SPDS-133200 - Edit Organization - term a practice address and related NET/RMBs - Professional Orgs only", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133200", author = "Murali", transactionId = "ORG_TERM_ADDR_PROF_ORGS_ONLY", sprint = "Sprint 192")
	public void ORG_TERM_ADDR_PROF_ORGS_ONLY() {

		LOGGER.info("Inside test ORG_TERM_ADDR_PROF_ORGS_ONLY");
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
					AddressPage addressPage = new AddressPage(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

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
								new ResultTuple("Provider", "", entID, PASSED, "Organization Provider is present"));
					}
					LOGGER.info("Provider Organization is present...");

					for (Address address : json.getAddresses()) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						String expTermDate = address.getAddressActive().getTerminationDate();
						String expTermCode = (Objects.toString(address.getAddressActive().getTerminationReasonCode(),
								"")).toUpperCase();
						if (expTermCode == null) {
							expTermCode = "";
						}
						String actTermDate = addressPage.getAddrTermDate();
						String actTermCode = addressPage.getAddrTermReasonCode();

						result = actTermDate.equalsIgnoreCase(expTermDate) ? PASSED : FAILED;
						comment = result.equals(FAILED)
								? "The Address Termination Date is not matching with the actual value"
								: "";
						resultSet.add(new ResultTuple("Address Termination Date ", "Actual value: " + actTermDate,
								" Expected : " + expTermDate, result, comment));

						result = actTermCode.equalsIgnoreCase(expTermCode) ? PASSED : FAILED;
						comment = result.equals(FAILED)
								? "The Address Termination Code is not matching with the actual value"
								: "";
						resultSet.add(new ResultTuple("Address Termination Code ", "Actual value: " + actTermCode,
								" Expected : " + expTermCode, result, comment));
					}

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							List<Address> addresses = affiliation.getAddresses();
							if (addresses != null) {
								for (Address ntwrkAddr : addresses) {

									String addrLine1 = ntwrkAddr.getAddressDetails().getAddressLine1();
									reimbNtwrkPage.searchAddressline1(addrLine1);

									List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
									if (ntwrkAffilList != null) {
										for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

											result = reimbNtwrkPage.SelectNetwkIdWithAddress(ntwrkAddr, ntwrkAffil)
													? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "Network ID record is not available with the matching values"
													: "Network ID record is available with the matching values";
											resultSet.add(new ResultTuple("Network details", "",
													"Expected address: "
															+ ntwrkAddr.getAddressDetails().getAddressLine1()
															+ "Expected Network ID: " + ntwrkAffil.getNetworkID()
															+ "Expected Network effective date: "
															+ ntwrkAffil.getNetworkActive().getEffectiveDate()
															+ "Expected Network termination date: "
															+ ntwrkAffil.getNetworkActive().getTerminationDate(),
													result, comment));
											if (result == FAILED)
												continue;

											String expTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
											String expTermCode = Objects
													.toString(ntwrkAffil.getNetworkActive().getTerminationReasonCode());

											String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
											String actNtwrkTermReasonCode = (reimbNtwrkPage.getOrgNetwrkTermCode())
													.toUpperCase();

											result = actNtwrkTermDate.equals(expTermDate) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "Network Termination Date is not matching with the actual value"
													: "Network Termination Date is matching with the actual value";
											resultSet.add(new ResultTuple("Network Termination Date ",
													"Actual value: " + actNtwrkTermDate, " Expected : " + expTermDate,
													result, comment));

											result = actNtwrkTermReasonCode.equalsIgnoreCase(expTermCode) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "Network Termination Code is not matching with the actual value"
													: "Network Termination Code is matching with the actual value";
											resultSet.add(new ResultTuple("Network Termination Code ",
													"Actual value: " + actNtwrkTermReasonCode,
													" Expected : " + expTermCode, result, comment));

											reimbNtwrkPage.orgNetworkDetailsClose();
											Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
										}
									}
									List<Reimbursement> reimbList = affiliation.getReimbursements();
									if (reimbList != null) {
										for (Reimbursement ntwrkReimb : reimbList) {

											result = reimbNtwrkPage.selectReimbId(ntwrkAddr, ntwrkReimb) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "Reimbursement record is not available with the matching values"
													: "Reimbursement record is available with the matching values";
											resultSet.add(new ResultTuple("Reimbursement details", "",
													"Expected address: "
															+ ntwrkAddr.getAddressDetails().getAddressLine1()
															+ "Expected reimbursement value: "
															+ ntwrkReimb.getReimbursementValue()
															+ "Expected reimbursement effective date: "
															+ ntwrkReimb.getReimbursementActive().getEffectiveDate()
															+ "Expected reimbursement termination date: "
															+ ntwrkReimb.getReimbursementActive().getTerminationDate(),
													result, comment));
											if (result == FAILED)
												continue;

											String expReimbTermDate = ntwrkReimb.getReimbursementActive()
													.getTerminationDate();
											String expReimbTermReasonCode = Objects.toString(
													ntwrkReimb.getReimbursementActive().getTerminationReasonCode());
											String actRemimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
											String actReimbTermReasonCode = (reimbNtwrkPage
													.getOrgReimbursementTermCode()).toUpperCase();

											result = actRemimbTermDate.equals(expReimbTermDate) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "Reimbursement Termination date is not matching with the actual value"
													: "Reimbursement Termination date is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement Termination date ",
													"Actual value: " + actRemimbTermDate,
													" Expected : " + expReimbTermDate, result, comment));

											result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
													? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "Reimbursement Termination Code is not matching with the actual value"
													: "Reimbursement Termination Code is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement Termination Code ",
													"Actual value: " + actReimbTermReasonCode,
													" Expected : " + expReimbTermReasonCode, result, comment));

											reimbNtwrkPage.orgNetworkDetailsClose();
										}
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

	@Test(description = "SPDS-132969 - Add/Edit/Term Organization Profile Credentialing Details", enabled = true, priority = 5, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132969", author = "Murali", transactionId = "ORG_PROFILE_ADD_EDIT_TERM_CREDENTIALING_DETAILS", sprint = "Sprint 192")
	public void ORG_PROFILE_ADD_EDIT_TERM_CREDENTIALING_DETAILS() {

		LOGGER.info("Inside test ORG_PROFILE_ADD_EDIT_TERM_CREDENTIALING_DETAILS");
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
								new ResultTuple("Provider", "", entID, FAILED, "Provider Organization is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", entID, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Provider Organization is present...");

					List<CredentialingDetail> credentialingDetails = json.getProfileAdditionalDetails()
							.getCredentialingDetails();
					if (credentialingDetails != null) {
						for (CredentialingDetail credentialDetail : credentialingDetails) {
							result = profilePage.searchCredentialingDetails(credentialDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED)
									? "Credentialing record is not available with the matching values"
									: "Credentialing record is available with the matching values";
							resultSet.add(new ResultTuple("Credentialing details", "",
									"Expected Credentialing source: " + credentialDetail.getCredentialingSource()
											+ " Expected Credentialing effective date: "
											+ credentialDetail.getCredentialingActive().getEffectiveDate()
											+ " Expected Credentialing termination date: "
											+ credentialDetail.getCredentialingActive().getTerminationDate(),
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

	// This test is out of scope
	@Test(description = "SPDS-132971 - Add/Remove Organization Provider Distinction details", enabled = false, priority = 3, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132971", author = "Murali", transactionId = "ORG_ADD_REMOVE_PROV_DISTINCTION_DETAILS", sprint = "Sprint 192")
	public void ORG_ADD_REMOVE_PROV_DISTINCTION_DETAILS() {

		LOGGER.info("Inside test ORG_ADD_REMOVE_PROV_DISTINCTION_DETAILS");
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
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Provider Organization is present...");

					List<ProviderDistinctionDetail> provDistinctDetails = json.getProfileAdditionalDetails()
							.getProviderDistinctionDetails();
					if (provDistinctDetails != null) {
						for (ProviderDistinctionDetail distinctDetail : provDistinctDetails) {
							result = profilePage.searchDistinctionDetails(distinctDetail) ? PASSED : FAILED;
							comment = result.equals(FAILED)
									? "Distinction record is not available with the matching values"
									: "";
							resultSet.add(new ResultTuple("Distinction details", "",
									"Expected Distinction source: " + distinctDetail.getProviderDistinctionCode()
											+ " Expected Distinction effective date: "
											+ distinctDetail.getProviderDistinctionDetailsActive().getEffectiveDate()
											+ " Expected Distinction termination date: "
											+ distinctDetail.getProviderDistinctionDetailsActive().getTerminationDate(),
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

	@Test(description = "SPDS-132999 - Edit Organization existing practice address - Add/Edit Office Details", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132999", author = "Murali", transactionId = "ORG_EXIST_ADDR_OFFICE_DETAILS", sprint = "Sprint 192")
	public void ORG_EXIST_ADDR_OFFICE_DETAILS() {

		LOGGER.info("Inside test ORG_EXIST_ADDR_OFFICE_DETAILS");

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
					AddressPage addressPage = new AddressPage(driver);

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
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Organization Provider is present"));
					}
					LOGGER.info("Provider Organization is present...");

					for (Address address : json.getAddresses()) {
						loginPage.navToAddressTab();
						result = addressPage.filterAndSelectOrgAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available"
								: "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToOfficeDetailsTab();
						List<OfficeDetail> officeDetails = address.getAddressAdditionalDetails().getOfficeDetails();
						if (officeDetails != null) {
							for (OfficeDetail officeDetail : officeDetails) {
								List<String> officeAccessCode = officeDetail.getAccessibilityCode();
								if (officeAccessCode != null) {
									for (String accessibilityCode : officeAccessCode) {
										result = addressPage.valOfficeAccessCode(accessibilityCode,
												ProviderType.ORGANIZATION) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "Office Accessibility Code is not available"
												: "Office Accessibility Code is available";
										resultSet.add(new ResultTuple("Office Accessibility Code", "",
												accessibilityCode, result, comment));
									}
								}

								List<String> staffLangs = officeDetail.getStaffLanguage();
								if (staffLangs != null) {
									for (String staffLanguage : staffLangs) {
										String staffLang = addressPage.getStaffLanguage();
										result = staffLang.contains(staffLanguage) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "Staff Language is not available"
												: "Staff Language is available";
										resultSet.add(
												new ResultTuple("Staff Language", "", staffLanguage, result, comment));
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
