package com.tests.epds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.AcceptingPatientsInd;
import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
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
import com.scripted.web.BrowserDriver;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class RpaTestsS1S2 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaTestsS1S2.class);

	@Test(description = "SPDS-132992 - Individual - existing practice address - area of focus", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132992", author = "Reshmi C", transactionId = "IND_EXIST_ADDR_AREA_OF_FOCUS", sprint = "Sprint 2")
	public void IND_EXIST_ADDR_AREA_OF_FOCUS() {

		LOGGER.info("Inside test IND_EXIST_ADDR_AREA_OF_FOCUS");
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
						comment = result.equals(FAILED) ? "Address record is not available" : "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToAltIdsTab();
						result = addressPage.valAltIdTypeSource(expAltId, expAltSource) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Alt ID record is not available" : "Alt ID record is available";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltSource, result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToAreaOfFocusTab();
						List<AreasOFocus> areasOfFocus = address.getAddressAdditionalDetails().getAreasOFocus();
						if (!areasOfFocus.isEmpty()) {
							for (AreasOFocus areaOfFocus : areasOfFocus) {
								if (areaOfFocus.getAreaOfFocusActive().getTerminationReasonCode() != null) {
									result = addressPage.valAreaOfFocusMatch(areaOfFocus.getAreaOfFocus()) ? FAILED
											: PASSED;
									if (result == PASSED)
										comment = "Field is removed";
									else
										comment = "Field is not removed";
									resultSet.add(new ResultTuple("Area of Focus", "", areaOfFocus.getAreaOfFocus(),
											result, comment));
								} else {
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

	@Test(description = "SPDS-133021 - Individual - Add/edit profile area of focus", enabled = true, groups = { "ind" })
	@TestDetails(userstory = "SPDS-133021", author = "Reshmi C", transactionId = "IND_ADD_EDIT_PROFILE_AREA_OF_FOCUS", sprint = "Sprint 2")
	public void IND_ADD_EDIT_PROFILE_AREA_OF_FOCUS() {

		LOGGER.info("Inside test IND_ADD_EDIT_PROFILE_AREA_OF_FOCUS");
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

					List<AreasOFocus> areasOfFocus = json.getProfile().getAreasOFocus();
					for (AreasOFocus areaOfFocus : areasOfFocus) {
						if (areaOfFocus.getAreaOfFocusActive().getTerminationReasonCode() != null) {
							result = profilePage.getIndAreaOfFocus().contains(areaOfFocus.getAreaOfFocus()) ? FAILED
									: PASSED;
							if (result == PASSED)
								comment = "Field is removed";
							else
								comment = "Field is not removed";
							resultSet.add(new ResultTuple("Area of Focus", "", areaOfFocus.getAreaOfFocus(), result,
									comment));
						} else {
							result = profilePage.getIndAreaOfFocus().contains(areaOfFocus.getAreaOfFocus()) ? PASSED
									: FAILED;
							if (result == PASSED)
								comment = "Field is added";
							else
								comment = "Field is not added";
							resultSet.add(new ResultTuple("Area of Focus", "", areaOfFocus.getAreaOfFocus(), result,
									comment));
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

	@Test(description = "SPDS-132994 - Individual - existing practice address - office details", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132994", author = "Reshmi C", transactionId = "IND_EXIST_ADDR_OFFICE_DETAILS", sprint = "Sprint 2")
	public void IND_EXIST_ADDR_OFFICE_DETAILS() {

		LOGGER.info("Inside test IND_EXIST_ADDR_OFFICE_DETAILS");
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
						comment = result.equals(FAILED) ? "Address record is not available" : "Address record is available";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressType()
								+ " : " + address.getAddressDetails().getAddressLine1() + " "
								+ address.getAddressDetails().getCity() + " " + address.getAddressDetails().getState()
								+ " " + address.getAddressDetails().getZip() + " : "
								+ address.getAddressActive().getEffectiveDate() + " : "
								+ address.getAddressActive().getTerminationDate(), result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToAltIdsTab();
						result = addressPage.valAltIdTypeSource(expAltId, expAltSource) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Alt ID record is not available" : "Alt ID record is available";
						resultSet.add(new ResultTuple("Alt Id", "", expAltId + " : " + expAltSource, result, comment));
						if (result == FAILED)
							continue;

						addressPage.navToOfficeDetailsTab();
						List<OfficeDetail> officeDetails = address.getAddressAdditionalDetails().getOfficeDetails();
						if (officeDetails != null) {

							for (OfficeDetail officeDetail : officeDetails) {
								List<String> accessibilityCodes = officeDetail.getAccessibilityCode();
								if (accessibilityCodes != null) {

									for (String accessibilityCode : accessibilityCodes) {
										result = addressPage.valOfficeAccessCode(accessibilityCode,
												ProviderType.INDIVIDUAL) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "Office Accessibility Code is not available"
												: "Office Accessibility Code is available";
										resultSet.add(new ResultTuple("Office Accessibility Code", "",
												accessibilityCode, result, comment));
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

	@Test(description = "SPDS-133086 - Edit Individual - existing practice address - add new affiliation and networks/reimbursements", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133086", author = "Shiva", transactionId = "IND_EXIST_ADDR_ADD_AFF_NTWK_REIMB", sprint = "Sprint 1")
	public void IND_EXIST_ADDR_ADD_AFF_NTWK_REIMB() {

		LOGGER.info("Inside test IND_EXIST_ADDR_ADD_AFF_NTWK_REIMB");
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

					List<Affiliation> affiliations = json.getAffiliations();
					for (Affiliation affiliation : affiliations) {
						String providerEid = affiliation.getAffiliatedLegacyID();

						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address address : addresses) {
								loginPage.navToAffilRelationTab();
								affilRelationPage.searchAffilEid(providerEid);
								result = affilRelationPage.searchIndAffiliation(affiliation, address) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Affiliation  details is not available" : "Affiliation  details is available";
								resultSet.add(new ResultTuple("Affiliation ", "",
										affiliation.getAffiliatedLegacyID() + " " + affiliation.getAffiliationType()
												+ " " + address.getAddressActive().getEffectiveDate() + " "
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
										"Expected ID: " + affiliation.getAffiliatedLegacyID()
												+ "Expected Effective Date: "
												+ affiliation.getAffiliationActive().getEffectiveDate()
												+ "Expected Termination  date: "
												+ affiliation.getAffiliationActive().getTerminationDate(),
										result, comment));
								if (result == FAILED)
									continue;

								List<NetworkAffiliation> ntwrkAffiliations = affiliation.getNetworkAffiliations();
								if (ntwrkAffiliations != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffiliations) {

										result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Record is not available with the matching Network ID, effective date , termination date"
												: "Record is available with the matching Network ID, effective date , termination date";
										resultSet.add(new ResultTuple("Network ID", "", "Expected ID: "
												+ ntwrkAffil.getNetworkID() + "Expected Effective Date: "
												+ ntwrkAffil.getNetworkSourceSystem() + "Expected Termination  date: "
												+ ntwrkAffil.getNetworkActive().getEffectiveDate()
												+ ntwrkAffil.getNetworkActive().getTerminationDate()
												+ ntwrkAffil.getNetworkActive().getTerminationReasonCode(), result,
												comment));
										if (result == FAILED)
											continue;

										String actNtwrkId = reimbNtwrkPage.getIndNetwrkId();
										String expNtwrkId = ntwrkAffil.getNetworkID();
										if (!actNtwrkId.contains(expNtwrkId)) {
											resultSet.add(new ResultTuple("NetwkId", actNtwrkId, expNtwrkId, FAILED,
													"NetwkId not matching"));
										} else {
											resultSet.add(new ResultTuple("NetwkId", actNtwrkId, expNtwrkId, PASSED,
													"NetwkId matching"));
										}

										String actNtwrkEffDate = reimbNtwrkPage.getIndNetwrkEffDate();
										String expNtwrkEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
										if (!actNtwrkEffDate.contains(expNtwrkEffDate)) {
											resultSet.add(new ResultTuple("IndNetwkEffDate", actNtwrkEffDate,
													expNtwrkEffDate, FAILED, "actIndNetwkEffDate not matching"));
										} else {
											resultSet.add(new ResultTuple("IndNetwkEffDate", actNtwrkEffDate,
													expNtwrkEffDate, PASSED, "actIndNetwkEffDate matching"));
										}

										String actNetDirIndicator = reimbNtwrkPage.getIndNetDirIndicator();
										String expNetDirIndicator = ntwrkAffil.getDirectoryIndicator();
										if (!actNetDirIndicator.contains(expNetDirIndicator)) {
											resultSet.add(new ResultTuple("IndDirectoryindicator", actNetDirIndicator,
													expNetDirIndicator, FAILED, "actIndNetwkEffDate not matching"));
										} else {
											resultSet.add(new ResultTuple("IndDirectoryindicator", actNetDirIndicator,
													expNetDirIndicator, PASSED, "actIndNetwkEffDate matching"));
										}

										List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
												.getAcceptingPatientsInds();
										for (AcceptingPatientsInd accPatientsInd : accPatientsIndList) {
											String expAccPatientsInd = accPatientsInd.getAcceptingPatientsIndicator();
											String actAccPatientsInd = reimbNtwrkPage.getIndAcceptPatientsIndicator();
											if (!actAccPatientsInd.equalsIgnoreCase(expAccPatientsInd)) {
												resultSet.add(new ResultTuple("actAcceptPatientsIndicator",
														actAccPatientsInd, actAccPatientsInd, FAILED,
														"AcceptPatientsIndicator not matching"));
											} else {
												resultSet.add(new ResultTuple("actAcceptPatientsIndicator",
														actAccPatientsInd, actAccPatientsInd, PASSED,
														"AcceptPatientsIndicator matching"));
											}
										}

										List<TimelyFilingInd> timelyFilingIndList = ntwrkAffil.getTimelyFilingInds();
										if (timelyFilingIndList != null) {
											for (TimelyFilingInd timelyFilingInd : timelyFilingIndList) {
												String expTimelyFilingInd = timelyFilingInd.getTimelyFiling();
												String actTimelyFilingInd = reimbNtwrkPage.getIndTimlyFiling();
												if (!actTimelyFilingInd.equalsIgnoreCase(expTimelyFilingInd)) {
													resultSet.add(new ResultTuple("TimelyFilingInd", actTimelyFilingInd,
															expTimelyFilingInd, FAILED,
															"actIndNetwkEffDate not matching"));
												} else {
													resultSet.add(new ResultTuple("TimelyFilingInd", actTimelyFilingInd,
															expTimelyFilingInd, PASSED, "actIndNetwkEffDate matching"));
												}
											}
										}

										List<Reimbursement> reimbList = affiliation.getReimbursements();
										if (reimbList != null) {
											for (Reimbursement reimbursement : reimbList) {

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
											}
											reimbNtwrkPage.indNetwrkClickCancel();
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

	@Test(description = "SPDS-132978 -  Edit Individual add new address/affiliations/networks-reimbursements", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-132978", author = "Shiva", transactionId = "IND_ADD_ADDR_AFF_NTWK_REIMB", sprint = "Sprint 2")
	public void IND_ADD_ADDR_AFF_NTWK_REIMB() {

		LOGGER.info("Inside test IND_ADD_ADDR_AFF_NTWK_REIMB");
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
					System.out.println("Individual provider is present...");

					List<Address> addressess = json.getAddresses();
					if (addressess != null) {
						for (Address addressDetails : addressess) {
							loginPage.navToAddressTab();

							result = addressPage.filterAndSelectIndAddress(addressDetails) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Address record is not available" : "Adress record is available";
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

							addressPage.navToContactTab();
							List<ContactDetail> contactDetails = addressDetails.getAddressAdditionalDetails()
									.getContactDetails();
							if (contactDetails != null) {
								for (ContactDetail contactDetail : contactDetails) {
									result = addressPage.searchContactTable(contactDetail, ProviderType.INDIVIDUAL)
											? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "Address record is not available" : "Address record is available";
									resultSet.add(new ResultTuple("Address", "",
											contactDetail.getFirstName() + " : " + contactDetail.getMiddleName() + " "
													+ contactDetail.getLastName() + " " + contactDetail.getPhone() + " "
													+ contactDetail.getFax() + " : " + contactDetail.getEmail(),
											result, comment));
								}
							}

							addressPage.navToAreaOfFocusTab();
							List<AreasOFocus> areasOfFocus = addressDetails.getAddressAdditionalDetails()
									.getAreasOFocus();
							if (areasOfFocus != null) {
								for (AreasOFocus areaOfFocus : areasOfFocus) {
									String expAreaOfFocus = areaOfFocus.getAreaOfFocus();
									addressPage.valAreaOfFocusMatch(expAreaOfFocus);
								}
							}

							addressPage.navToOfficeDetailsTab();
							List<OfficeDetail> officeDetails = addressDetails.getAddressAdditionalDetails()
									.getOfficeDetails();
							if (officeDetails != null) {
								for (OfficeDetail officeDetail : officeDetails) {
									List<String> officeAccessCode = officeDetail.getAccessibilityCode();
									for (String accessibilityCode : officeAccessCode) {
										result = addressPage.valOfficeAccessCode(accessibilityCode,
												ProviderType.INDIVIDUAL) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "Accessibility Code is not available" : "Accessibility Code is available";
										resultSet.add(new ResultTuple("AccessibilityCode", "", accessibilityCode,
												result, comment));
									}
								}
							}

							addressPage.navToScheduleTab();
							List<SchedulingDetail> scheduleDetails = addressDetails.getAddressAdditionalDetails()
									.getSchedulingDetails();
							if (scheduleDetails != null) {
								for (SchedulingDetail scheduleDetail : scheduleDetails) {
									result = addressPage.valSchedulingDetail(scheduleDetail) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "SchedulingDetail details is not available" : "SchedulingDetail details is available";
									resultSet.add(new ResultTuple("scheduleDetail", "", scheduleDetail.getDays() + " : "
											+ scheduleDetail.getOpenTime() + " " + scheduleDetail.getCloseTime(),
											result, comment));
								}
							}

							addressPage.navToSpecialty();
							List<Specialty> specialties = json.getSpecialties();
							if (specialties != null) {
								for (Specialty specialty : specialties) {
									result = addressPage.valSpecialty(specialty, ProviderType.INDIVIDUAL) ? PASSED
											: FAILED;
									comment = result.equals(FAILED) ? "specialProgram details is not available" : "specialProgram details is available";
									resultSet.add(new ResultTuple("specialProgram", "", specialty.getSpecialtyCode(),
											result, comment));
								}
							}

							addressPage.navToSpecialPrgmTab();
							List<SpecialProgram> splPrgms = addressDetails.getAddressAdditionalDetails()
									.getSpecialPrograms();
							if (splPrgms != null) {
								for (SpecialProgram specialProgram : splPrgms) {
									String expSplPrgmType = specialProgram.getProviderSpecialProgramType();
									String expSplPrgmEffDate = specialProgram.getProgramActive().getEffectiveDate();

									result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate) ? PASSED
											: FAILED;
									comment = result.equals(FAILED)
											? "expSplPrgmType, expSplPrgmEffDate record is not available"
											: "expSplPrgmType, expSplPrgmEffDate record is available";
									resultSet.add(new ResultTuple("Address", "",
											specialProgram.getProviderSpecialProgramType() + " : "
													+ specialProgram.getProgramActive().getEffectiveDate(),
											result, comment));
								}
							}
						}
					}
					List<Affiliation> affiliations = json.getAffiliations();
					if (affiliations != null) {
						for (Affiliation affiliation : affiliations) {
							String providerEid = affiliation.getAffiliatedLegacyID();

							List<Address> addresses = affiliation.getAddresses();
							for (Address address : addresses) {

								loginPage.navToAffilRelationTab();
								affilRelationPage.searchAffilEid(providerEid);
								result = affilRelationPage.searchIndAffiliation(affiliation, address) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Affiliation  details is not available" : "Affiliation  details is available";
								resultSet.add(new ResultTuple("Affiliation ", "",
										affiliation.getAffiliatedLegacyID() + " " + affiliation.getAffiliationType()
												+ " " + address.getAddressActive().getEffectiveDate() + " "
												+ address.getAddressActive().getTerminationDate() + " "
												+ address.getAddressActive().getTerminationReasonCode(),
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

	@Test(description = "SPDS-133080 - Edit Individual - Network - Term/Add Accepting Patients Indicator", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133080", author = "Murali", transactionId = "IND_NTWK_ADD_TERM_ACCPT_PATNTS_IND", sprint = "Sprint 1")
	public void IND_NTWK_ADD_TERM_ACCPT_PATNTS_IND() {

		LOGGER.info("Inside test IND_NTWK_ADD_TERM_ACCPT_PATNTS_IND");
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

							for (Address netwrkAddr : addresses) {
								result = reimbNtwrkPage.selectAvailableAddrCheckbox(netwrkAddr) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Record is not available with the matching address"
										: "Record is available with the matching address";
								resultSet.add(new ResultTuple("AvailableAddrCheckbox", "",
										"Expected address: " + netwrkAddr.getAddressDetails().getAddressLine1()
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
										: "Record is available with the matching Affiliation Legacy ID, effective date , termination date";
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
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

										result = reimbNtwrkPage.filterAndSelectIndNetId(ntwrkAffil) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Record is not available with the matching Network ID, effective date , termination date"
												: "Record is available with the matching Network ID, effective date , termination date";
										resultSet.add(new ResultTuple("Network ID", "",
												"Expected ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Effective Date:  "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Termination  date:  "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String actTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
										String accPatientsInd = reimbNtwrkPage.getIndAcceptPatientsIndicator();
										LOGGER.info("Actual termination date: " + actTermDate);
										LOGGER.info("Actual patients indicator: " + accPatientsInd);

										List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
												.getAcceptingPatientsInds();
										if (accPatientsIndList != null) {
											for (AcceptingPatientsInd expAccPatientsInd : accPatientsIndList) {
												LOGGER.info("Accepting patient indicators: "
														+ expAccPatientsInd.getAcceptingPatientsIndicator());

												result = expAccPatientsInd.getAcceptingPatientsIndicator()
														.equalsIgnoreCase(accPatientsInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The Accepting patient indicator is not matching with the actual value"
														: "The Accepting patient indicator is matching with the actual value";
												resultSet.add(new ResultTuple("Accepting patient indicator ",
														"Actual value: " + accPatientsInd,
														"Expected : "
																+ expAccPatientsInd.getAcceptingPatientsIndicator(),
														result, comment));

												String expTermDate = expAccPatientsInd
														.getAcceptingPatientsIndicatorActive().getTerminationDate();

												result = actTermDate.equalsIgnoreCase(expTermDate) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The Network termination date is not matching with the actual value"
														: "The Network termination date is matching with the actual value";
												resultSet.add(new ResultTuple("Network termination date ",
														"Actual value: " + actTermDate, " Expected : " + expTermDate,
														result, comment));

											}
											reimbNtwrkPage.indClickCancel();
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
}
