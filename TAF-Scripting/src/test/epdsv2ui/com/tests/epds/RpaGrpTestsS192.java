package com.tests.epds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AffliatedTaxID;
import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.TaxID;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AffiliationsRelationships;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.NetworkGrpPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class RpaGrpTestsS192 extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaGrpTestsS192.class);

	@Test(description = "SPDS-133078 - Grouping - Edit Profile", enabled = true, groups = { "grp" }, priority = 1)
	@TestDetails(userstory = "SPDS-133078", author = "Reshmi C", transactionId = "GRP_EDIT_PROFILE", sprint = "Sprint 192")
	public void GRP_EDIT_PROFILE() {

		LOGGER.info("Inside test GRP_EDIT_PROFILE");
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

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					if (!providerSearch.searchAndSelectGrouping(groupingName, groupingCode, groupingType)) {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						// reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", groupingCode, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider Grouping is present...");

					String grpgName = profilePage.getGroupingName();
					String grpgEffDate = profilePage.getGroupingEffDate();
					String grpgTermDate = profilePage.getGroupingTermDate();
					String grpgTermReasonCode = profilePage.getGroupingTermReasonCode();

					String expGrpgName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String expGrpgEffDate = json.getProfile().getProviderActive().getEffectiveDate();
					String expGrpgTermDate = json.getProfile().getProviderActive().getTerminationDate();
					String expGrpgTermReasonCode = json.getProfile().getProviderActive().getTerminationReasonCode();
					if (expGrpgTermReasonCode == null)
						expGrpgTermReasonCode = "";

					result = grpgName.equalsIgnoreCase(expGrpgName) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping Name", grpgName, expGrpgName, result, ""));

					result = grpgEffDate.equalsIgnoreCase(expGrpgEffDate) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping Effective Date", grpgEffDate, expGrpgEffDate, result, ""));

					result = grpgTermDate.equalsIgnoreCase(expGrpgTermDate) ? PASSED : FAILED;
					resultSet.add(
							new ResultTuple("Grouping Termination Date", grpgTermDate, expGrpgTermDate, result, ""));

					result = grpgTermReasonCode.equalsIgnoreCase(expGrpgTermReasonCode) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping Termination Reason Code", grpgTermReasonCode,
							expGrpgTermReasonCode, result, ""));

					List<TaxID> taxIds = json.getProfile().getTaxIDs();
					if (taxIds != null) {
						for (TaxID taxId : taxIds) {
							result = profilePage.searchGrpTaxId(taxId) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Tax ID record is not available" : "";
							resultSet.add(new ResultTuple("Tax ID", "",
									taxId.getTaxIDValue() + " : " + taxId.getTaxIDIndicator() + " : "
											+ taxId.getTaxIDActive().getEffectiveDate() + " : "
											+ taxId.getTaxIDActive().getTerminationDate() + " : "
											+ taxId.getTaxIDActive().getTerminationReasonCode(),
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

	@Test(description = "SPDS-133382 - Grouping - Edit Network", enabled = true, groups = { "ind" }, priority = 4)
	@TestDetails(userstory = "SPDS-133382", author = "Shiva", transactionId = "GRP_EDIT_NTWK", sprint = "Sprint 192")
	public void GRP_EDIT_NTWK() {

		LOGGER.info("Inside test GRP_EDIT_NTWK");
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
					NetworkGrpPage ntwrkGrpPage = new NetworkGrpPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					if (!providerSearch.searchAndSelectGrouping(groupingName, groupingCode, groupingType)) {
						resultSet.add(new ResultTuple("GroupingSiteCode", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, PASSED,
								"Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					loginPage.navToGrpNetworkTab();
					List<Affiliation> affilList = json.getAffiliations();
					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
							if (ntwrkAffilList != null) {
								for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
									result = ntwrkGrpPage.searchNetworkDetails(ntwrkAffil) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Network Detail record is not available" : "";
									resultSet.add(new ResultTuple("Network Details", "",
											ntwrkAffil.getNetworkSourceSystem() + " : " + ntwrkAffil.getNetworkID()
													+ " : " + ntwrkAffil.getNetworkActive().getEffectiveDate() + " : "
													+ ntwrkAffil.getNetworkActive().getTerminationDate() + " : "
													+ ntwrkAffil.getNetworkActive().getTerminationReasonCode(),
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

	@Test(description = "SPDS-133379 - Grouping - Add/Term Alternate IDs", enabled = true, groups = { "Grp" })
	@TestDetails(userstory = "SPDS-133379", author = "Shiva", transactionId = "GRP_EDIT_ALT_ID", sprint = "Sprint 191")
	public void GRP_EDIT_ALT_ID() {

		LOGGER.info("Inside test GRP_EDIT_ALT_ID");
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

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					if (!providerSearch.searchAndSelectGrouping(groupingName, groupingCode, groupingType)) {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", groupingCode, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					if (json.getAlternateIDs().getAltIdDetails() != null) {
						for (AltIdDetail altId : json.getAlternateIDs().getAltIdDetails()) {
							loginPage.navToGrpAltIdTab();
							result = altIdPage.searchGrpAltIdDetails(altId) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "AlternateId details is not available" : "";
							resultSet.add(new ResultTuple("AltIdDetail", "",
									altId.getAltIDType() + " : " + altId.getAltIDSource() + " " + altId.getAltIDValue()
											+ " " + altId.getAlternateIDActive().getEffectiveDate() + " : "
											+ altId.getAlternateIDActive().getTerminationDate() + " : "
											+ altId.getAlternateIDActive().getTerminationReasonCode(),
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
//..
	@Test(description = "SPDS-133380 - Grouping - Edit Address", enabled = true, groups = { "Grp" })
	@TestDetails(userstory = "SPDS-133380", author = "Shiva", transactionId = "GRP_EDIT_ADDR", sprint = "Sprint 192")
	public void GRP_EDIT_ADDR() {

		LOGGER.info("Inside test GRP_EDIT_ADDR");
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
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AddressPage addressPage = new AddressPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					if (!providerSearch.searchAndSelectGrouping(groupingName, groupingCode, groupingType)) {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", groupingCode, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					loginPage.navToAddressTab();
					List<Address> addresses = json.getAddresses();
					for (Address address : addresses) {
						result = addressPage.filterAndSelectGrpAddress(address) ? PASSED : FAILED;
						comment = result.equals(FAILED) ? "Grouping record is not available" : "";
						resultSet.add(new ResultTuple("Address", "", address.getAddressDetails().getAddressLine1()
								+ ", " + address.getAddressDetails().getCounty() + ", "
								+ address.getAddressDetails().getState() + ", " + address.getAddressDetails().getZip()
								+ ", " + address.getAddressActive().getEffectiveDate() + ", "
								+ address.getAddressActive().getTerminationDate() + ", "
								+ address.getAddressActive().getTerminationReasonCode(), result, comment));
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

	@Test(description = "SPDS-133381 - Grouping - Edit Address", enabled = true, groups = { "grp" })
	@TestDetails(userstory = "SPDS-133381", author = "Jyothi K", transactionId = "GRP_EDIT_AFFL_RELATIONSHIP", sprint = "Sprint 192")
	public void GRP_EDIT_AFFL_RELATIONSHIP() {

		LOGGER.info("Inside test GRP_EDIT_AFFL_RELATIONSHIP");
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
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AffiliationsRelationships affilRelationPage = new AffiliationsRelationships(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();

					if (!providerSearch.searchAndSelectGrouping(groupingName, groupingCode, groupingType)) {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", groupingCode, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					loginPage.navToGrpngAffilTab();

					List<Affiliation> affilList = json.getAffiliations();

					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							List<AffliatedTaxID> taxes = affiliation.getAffliatedTaxIDs();
							if (taxes != null) {
								for (AffliatedTaxID affTax : taxes) {
									List<Address> addresses = affiliation.getAddresses();
									if (addresses != null) {
										for (Address affAddr : addresses) {
											String groupName = (json.getProfile().getNameQualifier()
													.getGroupingNameQualifier().getGroupingName()).replaceAll("\\s+", "");
											if (affiliation.getAffiliationType()
													.equalsIgnoreCase("Grouping to Provider Organization")) {
												result = affilRelationPage.selectAffliationDetails(groupName,
														affiliation, affTax, affAddr) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "Affiliation   record is not available with the matching values"
														: "Affiliation   record is available with the matching values";
												resultSet.add(new ResultTuple("Group affiliation details", "", ""
														+ affAddr.getAddressDetails().getAddressLine1() + " : "
														+ affiliation.getAffiliationType() + " : "
														+ affTax.getTaxIDValue() + " : "
														+ affiliation.getAffiliationActive().getEffectiveDate() + " : "
														+ affiliation.getAffiliationActive().getTerminationDate()
														+ " : "
														+ affiliation.getAffiliationActive().getTerminationReasonCode(),
														result, comment));
												if (result == FAILED)
													continue;
											}
										}
									}
								}
							}
						}
					}

					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							String groupName = (json.getProfile().getNameQualifier().getGroupingNameQualifier()
									.getGroupingName()).replaceAll("\\s+", "");
							if (affiliation.getAffiliationType().equalsIgnoreCase("Grouping to Individual Provider")) {
								affilRelationPage.navToProvTab();
								result = affilRelationPage.selectProviderDetails(groupName, affiliation) ? PASSED
										: FAILED;
								comment = result.equals(FAILED)
										? "Individual record is not available with the matching values"
										: "Individual record is available with the matching values";
								resultSet.add(new ResultTuple("Provider RelationShips details", "",

										affiliation.getAffiliationType() + " : " + affiliation.getPcpID() + " : "
												+ affiliation.getAffiliationActive().getEffectiveDate() + "  : "
												+ affiliation.getAffiliationActive().getTerminationDate() + " : "
												+ affiliation.getAffiliationActive().getTerminationReasonCode(),
										result, comment));
								if (result == FAILED)
									continue;
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

	@Test(description = "SPDS-133074 - Grouping - add new Grouping linkage for PHO assignment", enabled = true, groups = {
			"grp" })
	@TestDetails(userstory = "SPDS-133074", author = "Jyothi K", transactionId = "GRP_ADD_GRP_LINKAGE_FOR_PHO_ASSGNMT", sprint = "Sprint 192")
	public void GRP_ADD_GRP_LINKAGE_FOR_PHO_ASSGNMT() {

		LOGGER.info("Inside test GRP_ADD_GRP_LINKAGE_FOR_PHO_ASSGNMT");
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
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AffiliationsRelationships affilRelationPage = new AffiliationsRelationships(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();

					if (!providerSearch.searchAndSelectGroupingByGrpName(groupingName, groupingType)) {
						resultSet.add(new ResultTuple("Provider", "", groupingCode, FAILED,
								"Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", groupingCode, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					loginPage.navToGrpngAffilTab();

					List<Affiliation> affilList = json.getAffiliations();
					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							if (affiliation.getAffiliationType()
									.equalsIgnoreCase("Grouping to Provider Organization")) {
								List<AffliatedTaxID> taxes = affiliation.getAffliatedTaxIDs();
								if (taxes != null) {
									for (AffliatedTaxID affTax : taxes) {
										String groupName = (json.getProfile().getNameQualifier()
												.getGroupingNameQualifier().getGroupingName()).replaceAll("\\s+", "");;

										result = affilRelationPage.selectAffliationPHODetails(groupName, affiliation,
												affTax) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Affiliation record is not available with the matching values"
												: "Affiliation record is available with the matching values";
										resultSet.add(new ResultTuple("Group affiliation details", "",
												"Expected AffiliationType: " + affiliation.getAffiliationType()
														+ "Expected TaxIDValue: " + affTax.getTaxIDValue()
														+ "Expected affiliation effective date: "
														+ affiliation.getAffiliationActive().getEffectiveDate()
														+ "Expected affiliation Termination date: "
														+ affiliation.getAffiliationActive().getTerminationDate()
														+ "Expected affiliation Termination Code: "
														+ affiliation.getAffiliationActive().getTerminationReasonCode(),
												result, comment));
										if (result == FAILED)
											continue;
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
