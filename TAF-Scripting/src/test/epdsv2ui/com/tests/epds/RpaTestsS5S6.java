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
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.SpecialProgram;
import com.beans.mdxjson.Specialty;
import com.beans.mdxjson.TimelyFilingInd;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AffiliationsRelationships;
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

public class RpaTestsS5S6 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaTestsS5S6.class);

	@Test(description = "SPDS-133089 - Individual -add/edit profile specialty and board certification", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133089", author = "Reshmi C", transactionId = "IND_PROFILE_ADD_EDIT_SPECIALTY_BOARD_CERT", sprint = "Sprint 6")
	public void IND_PROFILE_ADD_EDIT_SPECIALTY_BOARD_CERT() {

		LOGGER.info("Inside test IND_PROFILE_ADD_EDIT_SPECIALTY_BOARD_CERT");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String spltyCode, boardCertifAgencyName, boardCertifRecertifDate, boardCertifEffDate, boardCertifExpDate,
				spltyEffDate, spltyTermDate;
		boolean spltyInd;
		String spltyRec;
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

					List<Specialty> specialties = json.getSpecialties();
					if (specialties != null) {

						loginPage.navToSpecialityTaxonomyTab();
						for (Specialty specialty : specialties) {

							spltyCode = specialty.getSpecialtyCode();
							spltyInd = specialty.getPrimarySpecialtyIndicator();
							boardCertifAgencyName = specialty.getBoardCertificationAgencyName();
							boardCertifRecertifDate = specialty.getBoardCertificationRecertificationDate();
							boardCertifEffDate = specialty.getBoardCertificationDate();
							boardCertifExpDate = specialty.getBoardCertificationExpirationDate();
							spltyEffDate = specialty.getSpecialtyActive().getEffectiveDate();
							spltyTermDate = specialty.getSpecialtyActive().getTerminationDate();

							spltyRec = spltyCode + " : " + spltyInd + " : " + boardCertifAgencyName + " : "
									+ boardCertifRecertifDate + " : " + boardCertifEffDate + " : " + boardCertifExpDate
									+ " : " + spltyEffDate + " : " + spltyTermDate;
							result = spltyTxnmyPage.searchSpecialtyDetailsTable(specialty) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Speciality is not available in EPDSv2 UI"
									: "Speciality is available in EPDSv2 UI";
							resultSet.add(new ResultTuple("Speciality", "", spltyRec, result, comment));
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

	@Test(description = "SPDS-133005 - Individual - profile - termination date and reason code", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133005", author = "Reshmi C", transactionId = "IND_PROFILE_TERM_DATE", sprint = "Sprint 5")
	public void IND_PROFILE_TERM_DATE() {

		LOGGER.info("Inside test IND_PROFILE_TERM_DATE");
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

					String expTermDate = json.getProfile().getProviderActive().getTerminationDate();
					String expTermReasonCode = json.getProfile().getProviderActive().getTerminationReasonCode();
					if (expTermReasonCode == null)
						expTermReasonCode = "";

					String termDate = profilePage.getIndTerminationDate();
					String termReasonCode = Objects.toString(profilePage.getIndTermReasonCode(), "");

					result = termDate.equals(expTermDate) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Profile Term Date", termDate, expTermDate, result, ""));

					result = termReasonCode.equals(expTermReasonCode) ? PASSED : FAILED;
					resultSet.add(
							new ResultTuple("Profile Term Reason Code", termReasonCode, expTermReasonCode, result, ""));

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

	@Test(description = "SPDS-133013 - Edit Organization - Term existing and add new Remit Address", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133013", author = "Reshmi C", transactionId = "ORG_TERM_ADD_REMIT_ADDR", sprint = "Sprint 6")
	public void ORG_TERM_ADD_REMIT_ADDR() {

		LOGGER.info("Inside test ORG_TERM_ADD_REMIT_ADDR");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String fName, mName, lName, title, phoneNumber, faxNumber, url, emailAddress;
		String checkName, effectiveDate, terminationDate, npiValue;
		String contactStr, remitStr;
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

						if (address.getAddressActive().getTerminationReasonCode() == null) {

							List<ContactDetail> contacts = address.getAddressAdditionalDetails().getContactDetails();
							if (contacts != null) {

								addressPage.navToContactTab();
								for (ContactDetail contact : contacts) {

									fName = contact.getFirstName() != null ? contact.getFirstName() : "";
									mName = contact.getMiddleName() != null ? contact.getMiddleName() : "";
									lName = contact.getLastName() != null ? contact.getLastName() : "";
									title = contact.getSuffix() != null ? contact.getSuffix() : "";
									phoneNumber = contact.getPhone() != null ? contact.getPhone() : "";
									phoneNumber = phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
									faxNumber = contact.getFax() != null ? contact.getFax() : "";
									faxNumber = faxNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
									url = contact.getWebAddress() != null ? contact.getWebAddress() : "";
									emailAddress = contact.getEmail() != null ? contact.getEmail() : "";
									contactStr = fName + " : " + mName + " : " + lName + " : " + title + " : "
											+ phoneNumber + " : " + faxNumber + " : " + url + " : " + emailAddress;

									result = addressPage.searchContactTable(contact, ProviderType.ORGANIZATION) ? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "Contact details is not updated in UI"
											: "Contact details is updated in UI";
									resultSet.add(new ResultTuple("Contact Detail", "", contactStr, result, comment));
								}
							}

							List<RemittanceDetail> remittanceDetails = address.getAddressAdditionalDetails()
									.getRemittanceDetails();
							if (remittanceDetails != null) {

								addressPage.navToRemitInfoTab();
								for (RemittanceDetail remitDetail : remittanceDetails) {

									checkName = remitDetail.getCheckName();
									effectiveDate = remitDetail.getRemittanceActive().getEffectiveDate();
									terminationDate = remitDetail.getRemittanceActive().getTerminationDate();
									npiValue = remitDetail.getRemittanceNPI().getNpiValue();
									remitStr = checkName + " : " + effectiveDate + " : " + terminationDate + " : "
											+ npiValue;

									result = addressPage.searchRemitInfoTable(remitDetail) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Remittance details is not updated in UI"
											: "Remittance details is not updated in UI";
									resultSet.add(new ResultTuple("Remittance Detail", "", remitStr, result, comment));
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

	@Test(description = "SPDS-132993 - Edit Individual existing practice address - Contact Info", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132993", author = "Shiva", transactionId = "IND_EXIST_ADDR_CONTACT_INFO", sprint = "Sprint 5")
	public void IND_EXIST_ADDR_CONTACT_INFO() {

		LOGGER.info("Inside test IND_EXIST_ADDR_CONTACT_INFO");
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
					for (Address addressDetail : addressess) {
						loginPage.navToAddressTab();

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
								: "Alt ID record is available";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltIdSrc, result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToContactTab();

						List<ContactDetail> contactDetails = addressDetail.getAddressAdditionalDetails()
								.getContactDetails();
						if (contactDetails != null) {
							for (ContactDetail contactDetail : contactDetails) {
								result = addressPage.searchContactTable(contactDetail, ProviderType.INDIVIDUAL) ? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "Contact record is not available"
										: "Contact record is available";
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

	// SPDS-132996 is abandoned
	@Test(description = "SPDS-132996 - Edit Individual existing practice address - Special Programs", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132996", author = "Shiva", transactionId = "IND_EXIST_ADDR_EDIT_SPECIAL_PROGRAMS", sprint = "Sprint 5")
	public void IND_EXIST_ADDR_EDIT_SPECIAL_PROGRAMS() {

		LOGGER.info("Inside test IND_EXIST_ADDR_EDIT_SPECIAL_PROGRAMS");
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

					List<Address> addresses = json.getAddresses();
					for (Address addressDetail : addresses) {
						loginPage.navToAddressTab();

						result = addressPage.filterAndSelectIndAddress(addressDetail) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Address record is not available" : "";
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
						comment = result.equals(FAILED) ? "Alt ID record is not available" : "";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltIdSrc, result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToSpecialPrgmTab();
						List<SpecialProgram> splPrgmList = addressDetail.getAddressAdditionalDetails()
								.getSpecialPrograms();
						if (splPrgmList != null) {
							for (SpecialProgram splProgram : splPrgmList) {
								String expSplPrgmType = splProgram.getProviderSpecialProgramType();
								String expSplPrgmEffDate = splProgram.getProgramActive().getEffectiveDate();

								result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate) ? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "SpecialProgram record is not available" : "";
								resultSet.add(new ResultTuple("SpecialProgram", "",
										expSplPrgmType + " : " + expSplPrgmEffDate, result, comment));
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

	@Test(description = "SPDS-133330 -  Edit Individual - existing practice address and affiliation and network - term existing and add new Reimbursement", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133330", author = "Shiva", transactionId = "IND_EXIST_ADDR_AFF_NTWK_ADD_TERM_REIMB", sprint = "Sprint 6")
	public void IND_EXIST_ADDR_AFF_NTWK_ADD_TERM_REIMB() {

		LOGGER.info("Inside test IND_EXIST_ADDR_AFF_NTWK_ADD_TERM_REIMB");
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
					LOGGER.info("Individual provider is present...");

					List<Affiliation> affiliationList = json.getAffiliations();
					for (Affiliation affiliation : affiliationList) {

						List<Address> addrresses = affiliation.getAddresses();
						for (Address address : addrresses) {

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

							List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
							for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
								result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Network ID, effective date , termination date"
										: "Record is available with the matching Network ID, effective date , termination date";
								resultSet.add(new ResultTuple("Network ID", "",
										"Expected ID: " + ntwrkAffil.getNetworkID() + "Expected Effective Date: "
												+ ntwrkAffil.getNetworkSourceSystem() + "Expected Termination  date: "
												+ ntwrkAffil.getNetworkActive().getEffectiveDate()
												+ ntwrkAffil.getNetworkActive().getTerminationDate()
												+ ntwrkAffil.getNetworkActive().getTerminationReasonCode(),
										result, comment));
								if (result == FAILED)
									continue;

								List<Reimbursement> reimbList = affiliation.getReimbursements();
								for (Reimbursement reimbursement : reimbList) {

									result = reimbNtwrkPage.valIndAssociatedReimbTable(reimbursement) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "Record is not available with the matching Reimbursement value, effective date , termination date"
											: "Record is available with the matching Reimbursement value, effective date , termination date";
									resultSet.add(new ResultTuple("Reimbursement value", "",
											"Expected reimbursement value: " + reimbursement.getReimbursementValue()
													+ "Expected Effective Date: "
													+ reimbursement.getReimbursementActive().getEffectiveDate()
													+ "Expected Termination  date: "
													+ reimbursement.getReimbursementActive().getTerminationDate(),
											result, comment));
									LOGGER.info("Inside Individual reimbursement page");
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

	@Test(description = "SPDS-132982 - Edit Organization - add new practice and/or correspondence address - includes Net/Rmb", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132982", author = "Shiva", transactionId = "ORG_ADD_PRACTICE_ADDR_NTWK_REIMB", sprint = "Sprint 6")
	public void IND_EXIST_ADDR_ADD_AFF_NTWK_REIMB() {

		LOGGER.info("Inside test ORG_ADD_PRACTICE_ADDR_NTWK_REIMB");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String expPARIndicator = null;
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
					AddressPage addressPage = new AddressPage(driver);

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
					LOGGER.info("Individual provider is present...");

					loginPage.navToAddressTab();

					List<Address> addresses = json.getAddresses();
					for (Address addressDetails : addresses) {

						result = addressPage.filterAndSelectOrgAddress(addressDetails) ? PASSED : FAILED;
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

						//
						addressPage.navToContactTab();
						Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

						List<ContactDetail> contactDetails = addressDetails.getAddressAdditionalDetails()
								.getContactDetails();
						if (contactDetails != null) {
							for (ContactDetail contDetails : contactDetails) { // contact table

								result = addressPage.searchContactTable(contDetails, ProviderType.ORGANIZATION) ? PASSED
										: FAILED;
								comment = result.equals(FAILED)
										? "Contact record is not available with the matching values"
										: "Contact record is available with the matching values";
								resultSet.add(new ResultTuple("Contact details", "",
										"Expected First Name: " + contDetails.getFirstName() + " Expected Last Name: "
												+ contDetails.getLastName() + " Expected Middle Name: "
												+ contDetails.getMiddleName() + " Expected Phone : "
												+ contDetails.getPhone() + " Expected Fax: " + contDetails.getFax(),
										result, comment));
							}
						}
						Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

						// office details tab

						addressPage.navToOfficeDetailsTab();
						List<OfficeDetail> officeDetail = addressDetails.getAddressAdditionalDetails()
								.getOfficeDetails();
						if (officeDetail != null) {
							for (OfficeDetail officeDetails : officeDetail) {

								List<String> officeAccessCode = officeDetails.getAccessibilityCode();
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
							}
						}

						// Schedule tab

						addressPage.navToScheduleTab();
						List<SchedulingDetail> scheduleDetail = addressDetails.getAddressAdditionalDetails()
								.getSchedulingDetails();
						if (scheduleDetail != null) {
							for (SchedulingDetail schedulingDetail : scheduleDetail) {
								result = addressPage.valSchedulingDetail(schedulingDetail) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Scheduling Detail record is not available"
										: "Scheduling Detail record is available";
								resultSet.add(new ResultTuple("scheduleDetail", "", schedulingDetail.getDays() + " : "
										+ schedulingDetail.getOpenTime() + " " + schedulingDetail.getCloseTime(),
										result, comment));
							}
						}

						// checking speciality
						addressPage.navToOrgSpecialty();
						List<Specialty> specialties = json.getSpecialties();
						if (specialties != null) {
							for (Specialty speciality : specialties) {
								result = addressPage.valSpecialty(speciality, ProviderType.ORGANIZATION) ? PASSED
										: FAILED;
								comment = result.equals(FAILED) ? "Speciality record is not available"
										: "Speciality record is available";
								resultSet.add(new ResultTuple("Speciality", "", speciality.getSpecialtyCode(), result,
										comment));
								if (result == FAILED)
									continue;
							}
						}

						// checking SPECIALITY PROGRAM
						addressPage.navToOrgSpecialPrgmTab();
						List<SpecialProgram> splPrm = addressDetails.getAddressAdditionalDetails().getSpecialPrograms();
						if (splPrm != null) {
							for (SpecialProgram specialProgram : splPrm) {
								String expSplPrgmType = specialProgram.getProviderSpecialProgramType();
								String expSplPrgmEffDate = specialProgram.getProgramActive().getEffectiveDate();
								String expSplPrgmTermDate = specialProgram.getProgramActive().getTerminationDate();
								String expSplPrgmTermCode = specialProgram.getProgramActive()
										.getTerminationReasonCode();

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

					//
					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> address = affiliation.getAddresses();
						if (address != null) {
							for (Address ntwrkAddr : address) {
								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

										result = reimbNtwrkPage.filterAndSelectOrgNetId(ntwrkAddr, ntwrkAffil) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Network ID record is not available with the matching values"
												: "Network ID record is available with the matching values";
										resultSet.add(new ResultTuple("Network details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected Network ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Network effective date: "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Network termination date: "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expNtwrkId = ntwrkAffil.getNetworkID();
										String expNtwrkEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
										String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
										String expNtwrkTermReasonCode = Objects
												.toString(ntwrkAffil.getNetworkActive().getTerminationReasonCode(), "");
										String expDirectoryInd = Objects.toString(ntwrkAffil.getDirectoryIndicator(), "");

										String actNtwrkId = reimbNtwrkPage.getOrgNetId();
										String actNtwrkSrcSystem = reimbNtwrkPage.getOrgNetwrkSourceSystem();
										String actNtwrkEffDate = reimbNtwrkPage.getOrgNetwrkEffDate();
										String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
										String actNtwrkTermReasonCode = reimbNtwrkPage.getOrgNetwrkTermCode();
										String actDirectoryInd = reimbNtwrkPage.getOrgDirIndicator();
										String actAccPatientaInd = reimbNtwrkPage.getOrgAcceptingPatientsIndicator();
										String actTimelyFilingInd = reimbNtwrkPage.getOrgTimlyFiling();
										String actParIndicator = reimbNtwrkPage.getOrgPARIndicator();

										LOGGER.info("Actual source value : " + actNtwrkSrcSystem);
										LOGGER.info("Actual network ID" + actNtwrkId);
										LOGGER.info("Actual eff date : " + actNtwrkEffDate);
										LOGGER.info("Actual termination Date: " + actNtwrkTermDate);
										LOGGER.info("Actual termination Code" + actNtwrkTermReasonCode);
										LOGGER.info("Actual Directory indicator" + actDirectoryInd);
										LOGGER.info("Actual Accepting Patients Indicator: " + actAccPatientaInd);

										result = actNtwrkSrcSystem.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The NetworkSourceSystem value is not matching with the actual value"
												: "The NetworkSourceSystem value is matching with the actual value";
										resultSet.add(new ResultTuple("NetworkSourceSystem value ",
												"Actual value: " + actNtwrkSrcSystem, " Expected :QCARE ", result,
												comment));

										result = actNtwrkId.equalsIgnoreCase(expNtwrkId) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Organization Network ID is not matching with the actual value"
												: "The Organization Network ID is matching with the actual value";
										resultSet.add(new ResultTuple("Organization Network ID ",
												"Actual value: " + actNtwrkId, " Expected : " + expNtwrkId, result,
												comment));

										result = actNtwrkEffDate.equals(expNtwrkEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network Effective date is not matching with the actual value"
												: "The Network Effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Network Effective date ",
												"Actual value: " + actNtwrkEffDate, " Expected : " + expNtwrkEffDate,
												result, comment));

										result = actNtwrkTermDate.equals(expNtwrkTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermDate, " Expected : " + expNtwrkTermDate,
												result, comment));

										result = actNtwrkTermReasonCode.equalsIgnoreCase(expNtwrkTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermReasonCode,
												" Expected : " + expNtwrkTermReasonCode, result, comment));

										result = actDirectoryInd.equalsIgnoreCase(expDirectoryInd) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Directory indicator value is not matching with the actual value"
												: "The Directory indicator value is matching with the actual value";
										resultSet.add(new ResultTuple("Directory indicator value ",
												"Actual value: " + actDirectoryInd, " Expected : " + expDirectoryInd,
												result, comment));

										result = actParIndicator.equalsIgnoreCase(expPARIndicator) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "PAR Indicator is not matching with the actual value"
												: "PAR Indicator is matching with the actual value";
										resultSet.add(
												new ResultTuple("PAR Indicator", "Actual value: " + actParIndicator,
														"Expected : " + expPARIndicator, result, comment));
										List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
												.getAcceptingPatientsInds();
										if (accPatientsIndList != null) {
											for (AcceptingPatientsInd expAccPatientsInd : accPatientsIndList) {
												LOGGER.info("Accepting patient indicators: "
														+ expAccPatientsInd.getAcceptingPatientsIndicator());

												result = expAccPatientsInd.getAcceptingPatientsIndicator()
														.equalsIgnoreCase(actAccPatientaInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The accepting patients indicator value is not matching with the actual value"
														: "The accepting patients indicator value is matching with the actual value";
												resultSet.add(new ResultTuple("Accepting patients indicator value ",
														"Actual value: " + actAccPatientaInd,
														" Expected : "
																+ expAccPatientsInd.getAcceptingPatientsIndicator(),
														result, comment));
											}
										}

										List<TimelyFilingInd> timFilingIndList = ntwrkAffil.getTimelyFilingInds();
										if (timFilingIndList != null) {
											for (TimelyFilingInd expTimelyFilingInd : timFilingIndList) {
												result = expTimelyFilingInd.getTimelyFiling()
														.equalsIgnoreCase(actTimelyFilingInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The timely filing indicator is not matching with the actual value"
														: "The timely filing indicator is matching with the actual value";
												resultSet.add(new ResultTuple("Timely filing indicator ",
														"Actual value: " + actTimelyFilingInd,
														" Expected : " + expTimelyFilingInd.getTimelyFiling(), result,
														comment));
											}
										}

										List<Specialty> SpecialtyList = json.getSpecialties();
										if (SpecialtyList != null) {
											for (Specialty Speciality : SpecialtyList) {
												List<NetworkAffiliation> Netaffliation = Speciality
														.getNetworkAffiliations();
												for (NetworkAffiliation netwrkDetails : Netaffliation) {
													if (netwrkDetails.getNetworkID()
															.equals(ntwrkAffil.getNetworkID())) {
														result = reimbNtwrkPage.searchSpeciality(Speciality) ? PASSED
																: FAILED;
														comment = result.equals(FAILED)
																? "Speciality code is not available with the matching values"
																: "Speciality code is available with the matching values";
														resultSet
																.add(new ResultTuple("Speciality Code", "Actual code: ",
																		"Expected  code: "
																				+ Speciality.getSpecialtyCode(),
																		result, comment));
													}
													LOGGER.info("No specialty record available for the network");
												}
											}
										}

										reimbNtwrkPage.orgNetworkDetailsClose();
									}
								}

								List<Reimbursement> reimbList = affiliation.getReimbursements();
								if (reimbList != null) {

									for (Reimbursement reimbursement : reimbList) {

										result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(ntwrkAddr,
												reimbursement) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Reimbursement  record is not available with the matching values"
												: "Reimbursement  record is available with the matching values";
										resultSet.add(new ResultTuple("Reimbursement details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected reimbursement value: "
														+ reimbursement.getReimbursementValue()
														+ "Expected reimbursement effective date: "
														+ reimbursement.getReimbursementActive().getEffectiveDate()
														+ "Expected reimbursement termination date: "
														+ reimbursement.getReimbursementActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expReimbValue = reimbursement.getReimbursementValue();
										String expReimbEffDate = reimbursement.getReimbursementActive()
												.getEffectiveDate();
										String expReimbTermDate = reimbursement.getReimbursementActive()
												.getTerminationDate();
										String expReimbTermReasonCode = Objects.toString(reimbursement.getReimbursementActive()
												.getTerminationReasonCode(), "");

										String actReimbSrc = reimbNtwrkPage.getOrgReimbursementSource();
										LOGGER.info("Actual Source value: " + actReimbSrc);

										String actReimbEffDate = reimbNtwrkPage.getOrgReimbursementEffdate();
										LOGGER.info("Actual Reimbursement effective date: " + actReimbEffDate);

										String actReimbValue = reimbNtwrkPage.getOrgReimbursementValue();
										LOGGER.info("Actual Reimbursement value: " + actReimbValue);

										String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
										LOGGER.info("Actual termination date: " + actReimbTermDate);

										String actReimbTermReasonCode = reimbNtwrkPage.getOrgReimbursementTermCode();
										LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

										result = actReimbSrc.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Source value is not matching with the actual value"
												: "The Source value is matching with the actual value";
										resultSet.add(new ResultTuple("Source Value", "Actual value: " + actReimbSrc,
												" Expected : QCARE", result, comment));

										result = actReimbValue.equalsIgnoreCase(expReimbValue) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement reason value is not matching with the actual value"
												: "The Reimbursement reason value is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement reason value ",
												"Actual value: " + actReimbValue, " Expected : " + expReimbValue,
												result, comment));

										result = actReimbEffDate.equals(expReimbEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement effective date is not matching with the actual value"
												: "The Reimbursement effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement effective date ",
												"Actual value: " + actReimbEffDate, " Expected : " + expReimbEffDate,
												result, comment));

										result = actReimbTermDate.equals(expReimbTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination date is not matching with the actual value"
												: "The Reimbursement termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination date ",
												"Actual value: " + actReimbTermDate, " Expected : " + expReimbTermDate,
												result, comment));

										result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination code is not matching with the actual value"
												: "The Reimbursement termination code is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination code ",
												"Actual value: " + actReimbTermReasonCode,
												" Expected : " + expReimbTermReasonCode, result, comment));

										reimbNtwrkPage.orgNetworkDetailsClose();
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

	@Test(description = "SPDS-133092 - Edit Organization - Term existing and Add new Reimbursement", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133092", author = "Murali", transactionId = "ORG_TERM_ADD_REIMB", sprint = "Sprint 5")
	public void ORG_TERM_ADD_REIMB() {

		LOGGER.info("Inside test ORG_TERM_ADD_REIMB");
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

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address netwrkAddr : addresses) {
								List<Reimbursement> reimbList = affiliation.getReimbursements();
								if (reimbList != null) {
									for (Reimbursement reimbursement : reimbList) {
										if (!reimbursement.getReimbursementActive().getTerminationDate()
												.equals(EpdsConstants.TERMINATION_DATE)) {

											result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(netwrkAddr,
													reimbursement) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "Reimbursement  record is not available with the matching values"
													: "Reimbursement  record is available with the matching values";
											resultSet.add(new ResultTuple("Reimbursement details", "",
													"Expected address: "
															+ netwrkAddr.getAddressDetails().getAddressLine1()
															+ "Expected reimbursement value: "
															+ reimbursement.getReimbursementValue()
															+ "Expected reimbursement effective date: "
															+ reimbursement.getReimbursementActive().getEffectiveDate()
															+ "Expected reimbursement termination date: "
															+ reimbursement.getReimbursementActive()
																	.getTerminationDate(),
													result, comment));
											if (result == FAILED)
												continue;

											String expReimbTermDate = reimbursement.getReimbursementActive()
													.getTerminationDate();
											String expReimbTermReasonCode = Objects.toString(reimbursement.getReimbursementActive()
													.getTerminationReasonCode(), "");
											String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
											LOGGER.info("Actual termination date: " + actReimbTermDate);

											String actReimbTermReasonCode = reimbNtwrkPage
													.getOrgReimbursementTermCode();
											LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

											result = actReimbTermDate.equals(expReimbTermDate) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement termination date is not matching with the actual value"
													: "The Reimbursement termination date is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement termination date ",
													"Actual value: " + actReimbTermDate,
													" Expected : " + expReimbTermDate, result, comment));

											result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
													? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement termination code is not matching with the actual value"
													: "The Reimbursement termination code is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement termination code ",
													"Actual value: " + actReimbTermReasonCode,
													" Expected : " + expReimbTermReasonCode, result, comment));

										} else {
											result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(netwrkAddr,
													reimbursement) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "Reimbursement  record is not available with the matching values"
													: "Reimbursement  record is available with the matching values";
											resultSet.add(new ResultTuple("Reimbursement details", "",
													"Expected address: "
															+ netwrkAddr.getAddressDetails().getAddressLine1()
															+ "Expected reimbursement value: "
															+ reimbursement.getReimbursementValue()
															+ "Expected reimbursement effective date: "
															+ reimbursement.getReimbursementActive().getEffectiveDate()
															+ "Expected reimbursement termination date: "
															+ reimbursement.getReimbursementActive()
																	.getTerminationDate(),
													result, comment));
											if (result == FAILED)
												continue;

											String expReimbVal = reimbursement.getReimbursementValue();
											String expReimbEffDate = reimbursement.getReimbursementActive()
													.getEffectiveDate();
											String expReimbTermDate = reimbursement.getReimbursementActive()
													.getTerminationDate();
											String expReimbTermReasonCode = Objects.toString(reimbursement.getReimbursementActive()
													.getTerminationReasonCode(), "");

											String actReimbSrc = reimbNtwrkPage.getOrgReimbursementSource();
											LOGGER.info("Actual Source value: " + actReimbSrc);

											String actReimbEffDate = reimbNtwrkPage.getOrgReimbursementEffdate();
											LOGGER.info("Actual Reimbursement effective date: " + actReimbEffDate);

											String actReimbVal = reimbNtwrkPage.getOrgReimbursementValue();
											LOGGER.info("Actual Reimbursement value: " + actReimbVal);

											String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
											LOGGER.info("Actual termination date: " + actReimbTermDate);

											String actReimbTermReasonCode = reimbNtwrkPage
													.getOrgReimbursementTermCode();
											LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

											result = actReimbSrc.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The Source value is not matching with the actual value"
													: "The Source value is matching with the actual value";
											resultSet
													.add(new ResultTuple("Source Value", "Actual value: " + actReimbSrc,
															" Expected : QCARE", result, comment));

											result = actReimbVal.equalsIgnoreCase(expReimbVal) ? PASSED : FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement reason value is not matching with the actual value"
													: "The Reimbursement reason value is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement reason value ",
													"Actual value: " + actReimbVal, " Expected : " + expReimbVal,
													result, comment));

											result = actReimbEffDate.equals(expReimbEffDate) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement effective date is not matching with the actual value"
													: "The Reimbursement effective date is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement effective date ",
													"Actual value: " + actReimbEffDate,
													" Expected : " + expReimbEffDate, result, comment));

											result = actReimbTermDate.equals(expReimbTermDate) ? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement termination date is not matching with the actual value"
													: "The Reimbursement termination date is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement termination date ",
													"Actual value: " + actReimbTermDate,
													" Expected : " + expReimbTermDate, result, comment));

											result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
													? PASSED
													: FAILED;
											comment = result.equals(FAILED)
													? "The Reimbursement termination code is not matching with the actual value"
													: "The Reimbursement termination code is matching with the actual value";
											resultSet.add(new ResultTuple("Reimbursement termination code ",
													"Actual value: " + actReimbTermReasonCode,
													" Expected : " + expReimbTermReasonCode, result, comment));
										}
										reimbNtwrkPage.orgNetworkDetailsClose();
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

	@Test(description = "SPDS-133207 - Edit Organization - Add new Network and/or Reimbursement", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133207", author = "Murali", transactionId = "ORG_ADD_NTWK_REIMB", sprint = "Sprint 6")
	public void ORG_ADD_NTWK_REIMB() {

		LOGGER.info("Inside test ORG_ADD_NTWK_REIMB");
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

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {
								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

										result = reimbNtwrkPage.filterAndSelectOrgNetId(ntwrkAddr, ntwrkAffil) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Network ID record is not available with the matching values"
												: "Network ID record is available with the matching values";
										resultSet.add(new ResultTuple("Network details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected Network ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Network effective date: "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Network termination date: "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expNtwrkId = ntwrkAffil.getNetworkID();
										String expNtwrkEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
										String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
										String expNtwrkTermReasonCode = Objects.toString(ntwrkAffil.getNetworkActive()
												.getTerminationReasonCode(), "");
										String expDirectoryInd = Objects.toString(ntwrkAffil.getDirectoryIndicator(), "");

										String actNtwrkId = reimbNtwrkPage.getOrgNetId();
										String actNtwrkSrcSystem = reimbNtwrkPage.getOrgNetwrkSourceSystem();
										String actNtwrkEffDate = reimbNtwrkPage.getOrgNetwrkEffDate();
										String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
										String actNtwrkTermReasonCode = reimbNtwrkPage.getOrgNetwrkTermCode();
										String actDirectoryInd = reimbNtwrkPage.getOrgDirIndicator();
										String actAccPatientaInd = reimbNtwrkPage.getOrgAcceptingPatientsIndicator();
										String actTimelyFilingInd = reimbNtwrkPage.getOrgTimlyFiling();
										String actParIndicator = reimbNtwrkPage.getOrgPARIndicator();

										LOGGER.info("Actual source value : " + actNtwrkSrcSystem);
										LOGGER.info("Actual network ID" + actNtwrkId);
										LOGGER.info("Actual eff date : " + actNtwrkEffDate);
										LOGGER.info("Actual termination Date: " + actNtwrkTermDate);
										LOGGER.info("Actual termination Code" + actNtwrkTermReasonCode);
										LOGGER.info("Actual Directory indicator" + actDirectoryInd);
										LOGGER.info("Actual Accepting Patients Indicator: " + actAccPatientaInd);

										result = actNtwrkSrcSystem.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The NetworkSourceSystem value is not matching with the actual value"
												: "The NetworkSourceSystem value is matching with the actual value";
										resultSet.add(new ResultTuple("NetworkSourceSystem value ",
												"Actual value: " + actNtwrkSrcSystem, " Expected :QCARE ", result,
												comment));

										result = actNtwrkId.equalsIgnoreCase(expNtwrkId) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Organization Network ID is not matching with the actual value"
												: "The Organization Network ID is matching with the actual value";
										resultSet.add(new ResultTuple("Organization Network ID ",
												"Actual value: " + actNtwrkId, " Expected : " + expNtwrkId, result,
												comment));

										result = actNtwrkEffDate.equals(expNtwrkEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network Effective date is not matching with the actual value"
												: "The Network Effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Network Effective date ",
												"Actual value: " + actNtwrkEffDate, " Expected : " + expNtwrkEffDate,
												result, comment));

										result = actNtwrkTermDate.equals(expNtwrkTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermDate, " Expected : " + expNtwrkTermDate,
												result, comment));

										result = actNtwrkTermReasonCode.equalsIgnoreCase(expNtwrkTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermReasonCode,
												" Expected : " + expNtwrkTermReasonCode, result, comment));

										result = actDirectoryInd.equalsIgnoreCase(expDirectoryInd) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Directory indicator value is not matching with the actual value"
												: "The Directory indicator value is matching with the actual value";
										resultSet.add(new ResultTuple("Directory indicator value ",
												"Actual value: " + actDirectoryInd, " Expected : " + expDirectoryInd,
												result, comment));

										result = actParIndicator.equalsIgnoreCase(expPARIndicator) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "PAR Indicator is not matching with the actual value"
												: "PAR Indicator is matching with the actual value";
										resultSet.add(
												new ResultTuple("PAR Indicator", "Actual value: " + actParIndicator,
														"Expected : " + expPARIndicator, result, comment));
										List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
												.getAcceptingPatientsInds();
										if (accPatientsIndList != null) {
											for (AcceptingPatientsInd expAccPatientsInd : accPatientsIndList) {
												LOGGER.info("Accepting patient indicators: "
														+ expAccPatientsInd.getAcceptingPatientsIndicator());

												result = expAccPatientsInd.getAcceptingPatientsIndicator()
														.equalsIgnoreCase(actAccPatientaInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The accepting patients indicator value is not matching with the actual value"
														: "The accepting patients indicator value is matching with the actual value";
												resultSet.add(new ResultTuple("Accepting patients indicator value ",
														"Actual value: " + actAccPatientaInd,
														" Expected : "
																+ expAccPatientsInd.getAcceptingPatientsIndicator(),
														result, comment));
											}
										}

										List<TimelyFilingInd> timFilingIndList = ntwrkAffil.getTimelyFilingInds();
										if (timFilingIndList != null) {
											for (TimelyFilingInd expTimelyFilingInd : timFilingIndList) {
												result = expTimelyFilingInd.getTimelyFiling()
														.equalsIgnoreCase(actTimelyFilingInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The timely filing indicator is not matching with the actual value"
														: "The timely filing indicator is matching with the actual value";
												resultSet.add(new ResultTuple("Timely filing indicator ",
														"Actual value: " + actTimelyFilingInd,
														" Expected : " + expTimelyFilingInd.getTimelyFiling(), result,
														comment));
											}
										}

										List<Specialty> SpecialtyList = json.getSpecialties();
										if (SpecialtyList != null) {
											for (Specialty Speciality : SpecialtyList) {
												List<NetworkAffiliation> Netaffliation = Speciality
														.getNetworkAffiliations();
												for (NetworkAffiliation netwrkDetails : Netaffliation) {
													if (netwrkDetails.getNetworkID()
															.equals(ntwrkAffil.getNetworkID())) {
														result = reimbNtwrkPage.searchSpeciality(Speciality) ? PASSED
																: FAILED;
														comment = result.equals(FAILED)
																? "Speciality code is not available with the matching values"
																: "Speciality code is available with the matching values";
														resultSet
																.add(new ResultTuple("Speciality Code", "Actual code: ",
																		"Expected  code: "
																				+ Speciality.getSpecialtyCode(),
																		result, comment));
													}
													LOGGER.info("No specialty record available for the network");
												}
											}
										}

										reimbNtwrkPage.orgNetworkDetailsClose();
									}
								}

								List<Reimbursement> reimbList = affiliation.getReimbursements();
								if (reimbList != null) {

									for (Reimbursement reimbursement : reimbList) {

										result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(ntwrkAddr,
												reimbursement) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Reimbursement  record is not available with the matching values"
												: "Reimbursement  record is available with the matching values";
										resultSet.add(new ResultTuple("Reimbursement details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected reimbursement value: "
														+ reimbursement.getReimbursementValue()
														+ "Expected reimbursement effective date: "
														+ reimbursement.getReimbursementActive().getEffectiveDate()
														+ "Expected reimbursement termination date: "
														+ reimbursement.getReimbursementActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expReimbValue = reimbursement.getReimbursementValue();
										String expReimbEffDate = reimbursement.getReimbursementActive()
												.getEffectiveDate();
										String expReimbTermDate = reimbursement.getReimbursementActive()
												.getTerminationDate();
										String expReimbTermReasonCode = Objects.toString(reimbursement.getReimbursementActive()
												.getTerminationReasonCode(), "");

										String actReimbSrc = reimbNtwrkPage.getOrgReimbursementSource();
										LOGGER.info("Actual Source value: " + actReimbSrc);

										String actReimbEffDate = reimbNtwrkPage.getOrgReimbursementEffdate();
										LOGGER.info("Actual Reimbursement effective date: " + actReimbEffDate);

										String actReimbValue = reimbNtwrkPage.getOrgReimbursementValue();
										LOGGER.info("Actual Reimbursement value: " + actReimbValue);

										String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
										LOGGER.info("Actual termination date: " + actReimbTermDate);

										String actReimbTermReasonCode = reimbNtwrkPage.getOrgReimbursementTermCode();
										LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

										result = actReimbSrc.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Source value is not matching with the actual value"
												: "The Source value is matching with the actual value";
										resultSet.add(new ResultTuple("Source Value", "Actual value: " + actReimbSrc,
												" Expected : QCARE", result, comment));

										result = actReimbValue.equalsIgnoreCase(expReimbValue) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement reason value is not matching with the actual value"
												: "The Reimbursement reason value is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement reason value ",
												"Actual value: " + actReimbValue, " Expected : " + expReimbValue,
												result, comment));

										result = actReimbEffDate.equals(expReimbEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement effective date is not matching with the actual value"
												: "The Reimbursement effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement effective date ",
												"Actual value: " + actReimbEffDate, " Expected : " + expReimbEffDate,
												result, comment));

										result = actReimbTermDate.equals(expReimbTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination date is not matching with the actual value"
												: "The Reimbursement termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination date ",
												"Actual value: " + actReimbTermDate, " Expected : " + expReimbTermDate,
												result, comment));

										result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination code is not matching with the actual value"
												: "The Reimbursement termination code is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination code ",
												"Actual value: " + actReimbTermReasonCode,
												" Expected : " + expReimbTermReasonCode, result, comment));

										reimbNtwrkPage.orgNetworkDetailsClose();
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

	@Test(description = "SPDS-133093 - Edit Organization - Term Network and Reimbursement - no replacements", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133093", author = "Murali", transactionId = "ORG_TERM_NTWK_REIMB_NO_REPLACEMENTS", sprint = "Sprint 6")
	public void ORG_TERM_NTWK_REIMB_NO_REPLACEMENTS() {

		LOGGER.info("Inside test ORG_TERM_NTWK_REIMB_NO_REPLACEMENTS");
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

					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {
								List<NetworkAffiliation> ntwrkAffList = affiliation.getNetworkAffiliations();
								if (ntwrkAffList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffList) {

										result = reimbNtwrkPage.filterAndSelectOrgNetId(ntwrkAddr, ntwrkAffil) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Network ID record is not available with the matching values"
												: "Network ID record is available with the matching values";
										resultSet.add(new ResultTuple("Network details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected Network ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Network effective date: "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Network termination date: "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
										String expNtwrkTermReasonCode = Objects.toString(ntwrkAffil.getNetworkActive()
												.getTerminationReasonCode(), "");
										String expDirectoryInd = Objects.toString(ntwrkAffil.getDirectoryIndicator(), "");

										String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
										String actNtwrkTermReasonCode = reimbNtwrkPage.getOrgNetwrkTermCode();
										String actDirectoryInd = reimbNtwrkPage.getOrgDirIndicator();

										LOGGER.info("Actual Directory indicator" + actDirectoryInd);
										LOGGER.info("Actual termination Date: " + actNtwrkTermDate);
										LOGGER.info("Actual termination Code" + actNtwrkTermReasonCode);

										result = actDirectoryInd.equalsIgnoreCase(expDirectoryInd) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Directory indicator value is not matching with the actual value"
												: "The Directory indicator value is matching with the actual value";
										resultSet.add(new ResultTuple("Directory indicator value ",
												"Actual value: " + actDirectoryInd, " Expected : " + expDirectoryInd,
												result, comment));

										result = actNtwrkTermDate.equals(expNtwrkTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermDate, " Expected : " + expNtwrkTermDate,
												result, comment));

										result = actNtwrkTermReasonCode.equalsIgnoreCase(expNtwrkTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermReasonCode,
												" Expected : " + expNtwrkTermReasonCode, result, comment));

										reimbNtwrkPage.orgNetworkDetailsClose();
										Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
									}
								}

								List<Reimbursement> reimbList = affiliation.getReimbursements();
								if (reimbList != null) {
									for (Reimbursement reimbursement : reimbList) {

										result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(ntwrkAddr,
												reimbursement) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Reimbursement  record is not available with the matching values"
												: "Reimbursement  record is available with the matching values";
										resultSet.add(new ResultTuple("Reimbursement details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected reimbursement value: "
														+ reimbursement.getReimbursementValue()
														+ "Expected reimbursement effective date: "
														+ reimbursement.getReimbursementActive().getEffectiveDate()
														+ "Expected reimbursement termination date: "
														+ reimbursement.getReimbursementActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expReimbTermDate = reimbursement.getReimbursementActive()
												.getTerminationDate();
										String expReimbTermReasonCode = Objects.toString(reimbursement.getReimbursementActive()
												.getTerminationReasonCode(), "");

										String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
										LOGGER.info("Actual termination date: " + actReimbTermDate);

										String actReimbTermReasonCode = reimbNtwrkPage.getOrgReimbursementTermCode();
										LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

										result = actReimbTermDate.equals(expReimbTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination date is not matching with the actual value"
												: "The Reimbursement termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination date ",
												"Actual value: " + actReimbTermDate, " Expected : " + expReimbTermDate,
												result, comment));

										result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination code is not matching with the actual value"
												: "The Reimbursement termination code is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination code ",
												"Actual value: " + actReimbTermReasonCode,
												" Expected : " + expReimbTermReasonCode, result, comment));

										reimbNtwrkPage.orgNetworkDetailsClose();
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

	@Test(description = "SPDS-133091 - Edit Organization - Term and Add new Network to existing RMB - plus all child attributes", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133091", author = "Murali", transactionId = "ORG_TERM_ADD_NTWK_ALL_DATA", sprint = "Sprint 6")
	public void ORG_TERM_ADD_NTWK_ALL_DATA() {

		LOGGER.info("Inside test ORG_TERM_ADD_NTWK_ALL_DATA");
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

					loginPage.navToReimbNtwrkTab();

					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {
								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
										if (!ntwrkAffil.getNetworkActive().getTerminationDate()
												.equals(EpdsConstants.TERMINATION_DATE)) {
											List<Reimbursement> reimbList = affiliation.getReimbursements();
											if (reimbList != null) {
												for (Reimbursement reimbursemnet : reimbList) {
													result = reimbNtwrkPage.filterAndSelectRembIdWithNetID(ntwrkAddr,
															ntwrkAffil, reimbursemnet) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Network ID record is not available with the matching values"
															: "Network ID record is available with the matching values";
													resultSet.add(new ResultTuple("Network details", "",
															"Expected address: "
																	+ ntwrkAddr.getAddressDetails().getAddressLine1()
																	+ "Expected Network ID: "
																	+ ntwrkAffil.getNetworkID()
																	+ "Expected Network effective date: "
																	+ ntwrkAffil.getNetworkActive().getEffectiveDate()
																	+ "Expected Network termination date: " + ntwrkAffil
																			.getNetworkActive().getTerminationDate(),
															result, comment));
													if (result == FAILED)
														continue;
													reimbNtwrkPage.navToOrgViewNetworkTab();
													String expNtwrkTermDate = ntwrkAffil.getNetworkActive()
															.getTerminationDate();
													String expNtwrkTermReasonCode = Objects.toString(ntwrkAffil.getNetworkActive()
															.getTerminationDate(), "");

													String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
													String actNtwrkTermReasonCode = reimbNtwrkPage
															.getOrgNetwrkTermCode();

													result = actNtwrkTermDate.equalsIgnoreCase(expNtwrkTermDate)
															? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Network termaination date value is not matching."
															: "Network termaination date value is matching.";
													resultSet.add(new ResultTuple("Network termaination date ",
															"Actual value: " + actNtwrkTermDate,
															"Expected : " + expNtwrkTermDate, result, comment));

													result = actNtwrkTermReasonCode
															.equalsIgnoreCase(expNtwrkTermReasonCode) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Network termaination code value is not matching"
															: "Network termaination code value is matching";
													resultSet.add(new ResultTuple("Network termaination code ",
															"Actual value: " + actNtwrkTermReasonCode,
															" Expected : " + expNtwrkTermReasonCode, result, comment));

													// Thread.sleep(3000);
												}
												reimbNtwrkPage.orgNetworkDetailsClose();
											}

										} else {

											List<Reimbursement> reimbList = affiliation.getReimbursements();
											if (reimbList != null) {
												for (Reimbursement reimbursemnet : reimbList) {

													result = reimbNtwrkPage.filterAndSelectRembIdWithNetID(ntwrkAddr,
															ntwrkAffil, reimbursemnet) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Network ID record is not available with the matching values"
															: "Network ID record is available with the matching values";
													resultSet.add(new ResultTuple("Network details", "",
															"Expected address: "
																	+ ntwrkAddr.getAddressDetails().getAddressLine1()
																	+ "Expected Network ID: "
																	+ ntwrkAffil.getNetworkID()
																	+ "Expected Network effective date: "
																	+ ntwrkAffil.getNetworkActive().getEffectiveDate()
																	+ "Expected Network termination date: " + ntwrkAffil
																			.getNetworkActive().getTerminationDate(),
															result, comment));
													if (result == FAILED)
														continue;

													reimbNtwrkPage.navToOrgViewNetworkTab();

													String expNtwrkSrc = ntwrkAffil.getNetworkSourceSystem();
													String expNtwrkId = ntwrkAffil.getNetworkID();
													String expNtwrkEffDate = ntwrkAffil.getNetworkActive()
															.getEffectiveDate();
													String expNtwrkTermDate = ntwrkAffil.getNetworkActive()
															.getTerminationDate();
													String expNtwrkTermReasonCode = Objects.toString(ntwrkAffil.getNetworkActive()
															.getTerminationDate(), "");

													String expDirectoryInd = Objects.toString(ntwrkAffil.getDirectoryIndicator(), "");
													String expAgeFrom = Objects.toString(ntwrkAffil.getPatientPreferences()
															.getAgeFrom(), "");
													String expAgeTo = Objects.toString(ntwrkAffil.getPatientPreferences()
															.getAgeTo(), "");
													String expPatientGender = Objects.toString(ntwrkAffil.getPatientPreferences()
															.getPatientGenderPreference(), "");
													String expMemberCapacity = Objects.toString(ntwrkAffil.getPatientPreferences()
															.getMemberCapacityCount(), "");

													String actNtwrkSrc = reimbNtwrkPage.getOrgNetwrkSourceSystem();
													String actNtwrkId = reimbNtwrkPage.getOrgNetId();
													String actNtwrkEffDate = reimbNtwrkPage.getOrgNetwrkEffDate();
													String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
													String actNtwrkTermReasonCode = reimbNtwrkPage
															.getOrgNetwrkTermCode();
													String actDirectoryInd = reimbNtwrkPage.getOrgDirIndicator();
													String actAgeFrom = reimbNtwrkPage.getOrgAgeFrom();
													String actAgeTo = reimbNtwrkPage.getOrgAgeTo();
													String actPatientGender = reimbNtwrkPage.getOrgPatGender();
													String actMemCapacity = reimbNtwrkPage.getOrgMemCapacity();

													LOGGER.info(expNtwrkSrc + "" + actNtwrkSrc);
													LOGGER.info(expNtwrkId + "" + actNtwrkId);
													LOGGER.info(expNtwrkTermDate + "" + actNtwrkTermDate);
													LOGGER.info(expNtwrkTermReasonCode + "" + actNtwrkTermReasonCode);
													LOGGER.info(expDirectoryInd + "" + actDirectoryInd);
													LOGGER.info(expAgeFrom + "" + actAgeFrom);
													LOGGER.info(expAgeTo + "" + actAgeTo);
													LOGGER.info(expNtwrkTermReasonCode + "" + actPatientGender);
													LOGGER.info(expMemberCapacity + "" + actMemCapacity);

													result = actNtwrkSrc.equalsIgnoreCase(expNtwrkSrc) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Network Source value is not matching."
															: "Network Source value is matching.";
													resultSet.add(new ResultTuple("Network source",
															"Actual value: " + actNtwrkSrc, "Expected : " + expNtwrkSrc,
															result, comment));

													result = actNtwrkId.equalsIgnoreCase(expNtwrkId) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Network ID value is not matching."
															: "Network ID value is matching.";
													resultSet.add(
															new ResultTuple("Network ID", "Actual value: " + actNtwrkId,
																	"Expected : " + expNtwrkId, result, comment));

													result = actNtwrkEffDate.equals(expNtwrkEffDate) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Network Effective Date is not matching"
															: "Network Effective Date is matching";
													resultSet.add(new ResultTuple("Network Effective Date ",
															"Actual value: " + actNtwrkEffDate,
															" Expected : " + expNtwrkEffDate, result, comment));

													result = actNtwrkTermDate.equals(expNtwrkTermDate)
															? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Network termaination date value is not matching."
															: "Network termaination date value is matching.";
													resultSet.add(new ResultTuple("Network termaination date ",
															"Actual value: " + actNtwrkTermDate,
															"Expected : " + expNtwrkTermDate, result, comment));

													result = actNtwrkTermReasonCode
															.equalsIgnoreCase(expNtwrkTermReasonCode) ? PASSED : FAILED;
													comment = result.equals(FAILED)
															? "Network termaination code value is not matching"
															: "Network termaination code value is matching";
													resultSet.add(new ResultTuple("Network termaination code ",
															"Actual value: " + actNtwrkTermReasonCode,
															" Expected : " + expNtwrkTermReasonCode, result, comment));

													result = actDirectoryInd.equalsIgnoreCase(expDirectoryInd) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "The Directory indicator value is not matching"
															: "The Directory indicator value is matching";
													resultSet.add(new ResultTuple("Directory indicator ",
															"Actual value: " + actDirectoryInd,
															"Expected : " + expDirectoryInd, result, comment));

													result = actAgeFrom.equalsIgnoreCase(expAgeFrom) ? PASSED : FAILED;
													comment = result.equals(FAILED) ? "Age From value is not matching."
															: "Age From value is matching.";
													resultSet.add(
															new ResultTuple("Age From ", "Actual value: " + actAgeFrom,
																	"Expected : " + expAgeFrom, result, comment));

													result = actAgeTo.equalsIgnoreCase(expAgeTo) ? PASSED : FAILED;
													comment = result.equals(FAILED) ? "Age To value is not matching."
															: "Age To value is matching.";
													resultSet
															.add(new ResultTuple("Age To ", "Actual value: " + actAgeTo,
																	" Expected : " + expAgeTo, result, comment));

													result = actPatientGender.equalsIgnoreCase(expPatientGender)
															? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "Patient Gender value is not matching."
															: "Patient Gender value is matching.";
													resultSet.add(new ResultTuple("Patient Gender",
															"Actual value: " + actPatientGender,
															"Expected : " + expPatientGender, result, comment));

													result = actMemCapacity.equalsIgnoreCase(expMemberCapacity) ? PASSED
															: FAILED;
													comment = result.equals(FAILED)
															? "MemberCapacity value is not matching."
															: "MemberCapacity value is matching.";
													resultSet.add(new ResultTuple("MemberCapacity",
															"Actual value: " + actMemCapacity,
															"Expected : " + expMemberCapacity, result, comment));

													List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
															.getAcceptingPatientsInds();
													for (AcceptingPatientsInd accPatientsInd : accPatientsIndList) {

														String expAccpetPatIndicator = accPatientsInd
																.getAcceptingPatientsIndicator();
														String actorgAcceptingPatientsInd = reimbNtwrkPage
																.getOrgAcceptingPatientsIndicator();
														LOGGER.info(expAccpetPatIndicator + ""
																+ actorgAcceptingPatientsInd);

														result = actorgAcceptingPatientsInd.equalsIgnoreCase(
																expAccpetPatIndicator) ? PASSED : FAILED;
														comment = result.equals(FAILED)
																? "Accepting Patients Indicator value is not matching."
																: "Accepting Patients Indicator value is matching.";
														resultSet.add(new ResultTuple("Accepting Patients Indicator",
																"Actual value: " + actorgAcceptingPatientsInd,
																"Expected : " + expAccpetPatIndicator, result,
																comment));
													}

													List<TimelyFilingInd> timelyFilingIndList = ntwrkAffil
															.getTimelyFilingInds();
													for (TimelyFilingInd timelyFilingInd : timelyFilingIndList) {

														String expTimelyFiling = timelyFilingInd.getTimelyFiling();
														String actTimelyFiling = reimbNtwrkPage.getOrgTimlyFiling();
														LOGGER.info(expTimelyFiling + "" + actTimelyFiling);

														result = actTimelyFiling.equalsIgnoreCase(expTimelyFiling)
																? PASSED
																: FAILED;
														comment = result.equals(FAILED)
																? "TimelyFiling value is not matching."
																: "TimelyFiling value is matching.";
														resultSet.add(new ResultTuple("TimelyFiling ",
																"Actual value: " + actTimelyFiling,
																"Expected : " + expTimelyFiling, result, comment));
													}

													reimbNtwrkPage.orgNetworkDetailsClose();
													// Thread.sleep(3000);
												}
											}
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

	@Test(description = "SPDS-133083 - Edit Individual - Network - Add/Edit Patient Preferences", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133083", author = "Murali", transactionId = "IND_NTWK_ADD_EDIT_PATIENT_PREFS", sprint = "Sprint 5")
	public void IND_NTWK_ADD_EDIT_PATIENT_PREFS() {

		LOGGER.info("Inside test IND_NTWK_ADD_EDIT_PATIENT_PREFS");
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

					List<Affiliation> affiliationList = json.getAffiliations();
					for (Affiliation affiliation : affiliationList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address netwrkAddr : addresses) {

								result = reimbNtwrkPage.selectAvailableAddrCheckbox(netwrkAddr) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Record is not available with the matching address"
										: "Record is available with the matching address";
								resultSet.add(new ResultTuple("AvailableAddrCheckbox", "", "Address: "
										+ netwrkAddr.getAddressDetails().getAddressLine1() + "Effective Date: "
										+ affiliation.getAffiliationActive().getEffectiveDate() + "Termination  date: "
										+ affiliation.getAffiliationActive().getTerminationDate(), result, comment));
								if (result == FAILED)
									continue;

								result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
										: "Record is available with the matching Affiliation Legacy ID, effective date , termination date";
								resultSet.add(new ResultTuple("Affiliation Legacy ID", "", "Affiliated Legacy ID: "
										+ affiliation.getAffiliatedLegacyID() + "Effective Date: "
										+ affiliation.getAffiliationActive().getEffectiveDate() + "Termination  date: "
										+ affiliation.getAffiliationActive().getTerminationDate(), result, comment));
								if (result == FAILED)
									continue;

								List<NetworkAffiliation> ntwrkAffAffilList = affiliation.getNetworkAffiliations();
								for (NetworkAffiliation ntwrlAffil : ntwrkAffAffilList) {

									result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrlAffil) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "Record is not available with the matching Network ID, effective date , termination date"
											: "Record is available with the matching Network ID, effective date , termination date";
									resultSet.add(new ResultTuple("Network ID", "",
											"Affiliation Legacy ID: " + affiliation.getAffiliatedLegacyID()
													+ "Effective Date: "
													+ affiliation.getAffiliationActive().getEffectiveDate()
													+ "Termination  date: "
													+ affiliation.getAffiliationActive().getTerminationDate(),
											result, comment));
									if (result == FAILED)
										continue;

									String expAgeFrom = Objects.toString(ntwrlAffil.getPatientPreferences().getAgeFrom(), "");
									String expAgeTo = Objects.toString(ntwrlAffil.getPatientPreferences().getAgeTo(), "");
									String expPatientGender = Objects.toString(ntwrlAffil.getPatientPreferences()
											.getPatientGenderPreference(), "");
									String expMemberCapacity = Objects.toString(ntwrlAffil.getPatientPreferences()
											.getMemberCapacityCount(), "");

									String actAgeFrom = reimbNtwrkPage.getIndAgeFrom();
									String actAgeTo = reimbNtwrkPage.getIndAgeTo();
									String actPatientGender = reimbNtwrkPage.getIndPatientGender();
									String actMemCapacity = reimbNtwrkPage.getIndMemberCapacity();

									LOGGER.info(expAgeFrom + "" + actAgeFrom);
									LOGGER.info(expAgeTo + "" + actAgeTo);
									LOGGER.info(expPatientGender + "" + actPatientGender);
									LOGGER.info(expMemberCapacity + "" + actMemCapacity);

									result = actAgeFrom.equalsIgnoreCase(expAgeFrom) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Age From value is not matching." : "Age From value is matching.";
									resultSet.add(new ResultTuple("Age From ", "Actual value: " + actAgeFrom,
											" Expected : " + expAgeFrom, result, comment));

									result = actAgeTo.equalsIgnoreCase(expAgeTo) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Age To value is not matching." : "Age To value is matching.";
									resultSet.add(new ResultTuple("Age To ", "Actual value: " + actAgeTo,
											" Expected : " + expAgeTo, result, comment));

									result = actPatientGender.equalsIgnoreCase(expPatientGender) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Patient Gender value is not matching." : "Patient Gender value is matching.";
									resultSet.add(new ResultTuple("Patient Gender", "Actual value: " + actPatientGender,
											" Expected : " + expPatientGender, result, comment));

									result = actMemCapacity.equalsIgnoreCase(expMemberCapacity) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "MemberCapacity value is not matching." : "MemberCapacity value is matching.";
									resultSet.add(new ResultTuple("MemberCapacity", "Actual value: " + actMemCapacity,
											" Expected : " + expMemberCapacity, result, comment));
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

}
