package com.tests.epds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
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

public class AddGroupEntireFlow extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AddGroupEntireFlow.class);

	@Test(description = "SPDS-133077 - Grouping - Add new - entire flow", enabled = true, groups = { "grp" })
	@TestDetails(userstory = "SPDS-133077", author = "Jyothi", transactionId = "GRP_ADD_ENTIRE_FLOW", sprint = "Sprint 192")
	public void GRP_ADD_ENTIRE_FLOW() {

		LOGGER.info("Inside test GRP_ADD_ENTIRE_FLOW");
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
					ProfilePage profilePage = new ProfilePage(driver);
					AddressPage addressPage = new AddressPage(driver);
					AffiliationsRelationships affilRelationPage = new AffiliationsRelationships(driver);
					NetworkGrpPage ntwrkGrpPage = new NetworkGrpPage(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					String groupingName = (json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName()).toUpperCase();
					String groupingCode = (json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode()).toUpperCase();
					String groupingType = (json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType()).toUpperCase();
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

					// Profile Page
					String grpSiteCode = profilePage.getGroupingSiteCode();
					String grpType = profilePage.getGroupingTypeCode();
					String grpgName = profilePage.getGroupingName();
					String grpgEffDate = profilePage.getGroupingEffDate();
					String grpgTermDate = profilePage.getGroupingTermDate();
					String grpgTermReasonCode = profilePage.getGroupingTermReasonCode();

					String expGrpgSiteCode = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingSiteCode();
					String expGrpgType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					String expGrpgName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String expGrpgEffDate = json.getProfile().getProviderActive().getEffectiveDate();
					String expGrpgTermDate = json.getProfile().getProviderActive().getTerminationDate();
					String expGrpgTermReasonCode = Objects
							.toString(json.getProfile().getProviderActive().getTerminationReasonCode(), "");
					/*
					 * if (expGrpgTermReasonCode == null) expGrpgTermReasonCode = "";
					 */

					result = grpgName.equalsIgnoreCase(expGrpgName) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping Name", grpgName, expGrpgName, result, ""));

					result = grpSiteCode.equalsIgnoreCase(expGrpgSiteCode) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping SiteCode", grpSiteCode, expGrpgSiteCode, result, ""));

					result = grpType.equalsIgnoreCase(expGrpgType) ? PASSED : FAILED;
					resultSet.add(new ResultTuple("Grouping Type", grpType, expGrpgType, result, ""));

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

					// AlternateId Tab
					if (json.getAlternateIDs().getAltIdDetails() != null) {
						loginPage.navToGrpAltIdTab();
						for (AltIdDetail altId : json.getAlternateIDs().getAltIdDetails()) {
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

					// Address Tab
					loginPage.navToAddressTab();
					List<Address> addresses = json.getAddresses();
					if (addresses != null) {
						for (Address address : addresses) {
							result = addressPage.filterAndSelectGrpAddress(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Grouping record is not available" : "";
							resultSet.add(new ResultTuple("Address", "",
									address.getAddressDetails().getAddressLine1() + ", "
											+ address.getAddressDetails().getCounty() + ", "
											+ address.getAddressDetails().getState() + ", "
											+ address.getAddressDetails().getZip() + ", "
											+ address.getAddressActive().getEffectiveDate() + ", "
											+ address.getAddressActive().getTerminationDate() + ", "
											+ address.getAddressActive().getTerminationReasonCode(),
									result, comment));
						}
					}

					// Affiliation/Relationship Tab
					loginPage.navToGrpngAffilTab();
					affilRelationPage.navToProvTab();

					List<Affiliation> affiliationDetails = json.getAffiliations();
					if (affiliationDetails != null) {
						for (Affiliation affiliation : affiliationDetails) {
							String groupName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
									.getGroupingName();
							result = affilRelationPage.selectProviderDetails(groupName, affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation Grouping Reletions details is not available"
									: "Affiliation Grouping Reletions details is available";
							resultSet.add(new ResultTuple("Affiliation Grouping Reletions", "",
									affiliation.getAffiliationType() + " : " + affiliation.getGroupingCode() + " "
											+ affiliation.getAffiliationActive().getEffectiveDate() + " "
											+ affiliation.getAffiliationActive().getTerminationDate() + " "
											+ affiliation.getAffiliationActive().getTerminationReasonCode() + " ",
									result, comment));
						}
					}

					// NetworkGrp Tab
					loginPage.navToGrpNetworkTab();
					List<Affiliation> affilList = json.getAffiliations();
					if (affilList != null) {
						for (Affiliation affiliation : affilList) {
							List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
							if (ntwrkAffilList != null) {
								for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
									result = ntwrkGrpPage.searchNetworkDetails(ntwrkAffil) ? PASSED : FAILED;
									comment = result.equals(FAILED) ? "Network Detail record is not available"
											: "Network Detail record is available";
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

}
