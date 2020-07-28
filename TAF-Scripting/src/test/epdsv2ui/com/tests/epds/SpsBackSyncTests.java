package com.tests.epds;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.scripted.database.OracleUtil;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;
import com.utilities.epds.SPSAltIdQueries;

public class SpsBackSyncTests extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(SpsBackSyncTests.class);

	public static Connection spsDbConn = null;
	OracleUtil dbUtil = new OracleUtil();
	
	Map<String, String> spsPropertyMap;

	@Test(description = "SPDS-133364 - Edit Individual - Add Alternate ID in SPS - EPDSv2 Legacy ID", enabled = true, groups = {
			"ind" })
	@TestDetails(userstory = "SPDS-133364", author = "Reshmi C", transactionId = "SpsBackSync-Ind", sprint = "Sprint 192")
	public void SpsBackSyncInd() {

		LOGGER.info("Inside test SpsBackSyncInd");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String txnId = "IND_NEW_PROV_ENTIRE_FLOW";
		String altIdSrc = "", altIdType = "", altIdVal = "", effDate = "", termDate = "", termReasonCode = "";
		String expAltIdSrc, expAltIdType, expAltIdVal, expEffDate, expTermDate, expTermReasonCode;
		boolean flag;
		String spsRecord, epdsRecord;
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, txnId);
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + txnId);
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
					flag = false;

					List<String> spsIdList = json.getHeader().getSpsIDs();
					String spsIds = "";
					for (String spsId : spsIdList) {
						spsIds = spsIds + "'" + spsId + "',";
					}
					spsIds = spsIds.substring(0, spsIds.length() - 1);

					expAltIdSrc = "EPDSV2.0";
					expAltIdType = "Legacy ID";
					String spsAltIdType = "Legacy Provider ID";

					String query = SPSAltIdQueries.SPS_IND_ALT_ID_QUERY;
					query = query.replaceAll("<SPS_DBNAME>", spsPropertyMap.get("sps_dbname"));
					query = query.replaceAll("<SPS_ID_LIST>", spsIds);
					ResultSet rs = dbUtil.executeQuery(spsDbConn, query);

					while (rs.next()) {
						altIdSrc = rs.getString("ALTID_SOR_CD");
						altIdType = rs.getString("ALTID_TYPE_CD");
						if (altIdSrc.equalsIgnoreCase(expAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)) {
							altIdVal = rs.getString("ALTID_TXT");
							effDate = rs.getString("PROV_ALTID_EFCTV_DT");
							effDate = util.spsDbDateConversion(effDate);
							termDate = rs.getString("PROV_ALTID_TRMNTN_DT");
							termDate = util.spsDbDateConversion(termDate);
							termReasonCode = rs.getString("PROV_ALTID_TRMNTN_RSN_CD");
							if (termReasonCode == null)
								termReasonCode = "";
							flag = true;
							break;
						}
					}
					if (!flag) {
						resultSet.add(new ResultTuple("EPDS Enterprise ID", "", "", FAILED,
								"EPDS Enterprise ID is not available in SPS for SPS IDs " + spsIds));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					}

					if (!providerSearch.searchAndSelectIndividual(altIdVal)) {
						resultSet.add(
								new ResultTuple("Provider", "", altIdVal, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", altIdVal, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Individual provider is present...");

					loginPage.navToAltIdTab();
					AltIdDetail altIdDetail = altIdPage.getAltIdDetails(expAltIdSrc, expAltIdType,
							ProviderType.INDIVIDUAL);

					expAltIdVal = altIdDetail.getAltIDValue();
					expEffDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
					expTermDate = altIdDetail.getAlternateIDActive().getTerminationDate();
					expTermReasonCode = altIdDetail.getAlternateIDActive().getTerminationReasonCode();

					if (altIdSrc.equalsIgnoreCase(expAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)
							&& altIdVal.equalsIgnoreCase(expAltIdVal) && effDate.equalsIgnoreCase(expEffDate)
							&& termDate.equalsIgnoreCase(expTermDate)
							&& termReasonCode.equalsIgnoreCase(expTermReasonCode)) {
						result = PASSED;
					} else {
						result = FAILED;
					}

					spsRecord = altIdSrc + " : " + altIdType + " : " + altIdVal + " : " + effDate + " : " + termDate
							+ " : " + termReasonCode;
					epdsRecord = expAltIdSrc + " : " + spsAltIdType + " : " + expAltIdVal + " : " + expEffDate + " : "
							+ expTermDate + " : " + expTermReasonCode;
					resultSet.add(new ResultTuple("Alt ID record", spsRecord, epdsRecord, result, ""));

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

	@Test(description = "SPDS-133365 - Edit Organization - Add Alternate ID in SPS - EPDSv2 EID, Q/CARE Tax+Suffix, and Payee ID", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-133365", author = "Reshmi C", transactionId = "SpsBackSync-Org", sprint = "Sprint 192")
	public void SpsBackSyncOrg() {

		LOGGER.info("Inside test SpsBackSyncOrg");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String txnId = "ORG_ADD_FULL_FLOW_IN_EPDSV2";
		String altIdSrc = "", altIdType = "", altIdVal = "", effDate = "", termDate = "", termReasonCode = "";
		String expAltIdSrc = "", expAltIdType = "", expAltIdVal = "", expEffDate = "", expTermDate = "",
				expTermReasonCode = "";
		boolean isSpsRecAvailable, isJsonRecAvailable;
		String spsRecord, epdsRecord;
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, txnId);
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + txnId);
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
					isSpsRecAvailable = false;
					isJsonRecAvailable = false;

					List<String> spsIdList = json.getHeader().getSpsIDs();
					String spsIds = "";
					for (String spsId : spsIdList) {
						spsIds = spsIds + "'" + spsId + "',";
					}
					spsIds = spsIds.substring(0, spsIds.length() - 1);

					String spsAltIdSrc = "EPDSV2.0";
					String spsAltIdType = "EPDSV2 ID";
					String epdsAltIdSrc = "EPDSV2.0";
					String epdsAltIdType = "Legacy ID";

					String query = SPSAltIdQueries.SPS_ORG_ALT_ID_QUERY;
					query = query.replaceAll("<SPS_DBNAME>", spsPropertyMap.get("sps_dbname"));
					query = query.replaceAll("<SPS_ID_LIST>", spsIds);
					ResultSet rs = dbUtil.executeQuery(spsDbConn, query);

					while (rs.next()) {
						altIdSrc = rs.getString("ALTID_SOR_CD");
						altIdType = rs.getString("ALTID_TYPE_CD");
						if (altIdSrc.equalsIgnoreCase(spsAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)) {
							altIdVal = rs.getString("ALTID_TXT");
							effDate = rs.getString("PROV_ALTID_EFCTV_DT");
							effDate = util.spsDbDateConversion(effDate);
							termDate = rs.getString("PROV_ALTID_TRMNTN_DT");
							termDate = util.spsDbDateConversion(termDate);
							termReasonCode = rs.getString("PROV_ALTID_TRMNTN_RSN_CD");
							if (termReasonCode == null)
								termReasonCode = "";
							isSpsRecAvailable = true;
							break;
						}
					}
					rs.beforeFirst();
					if (!isSpsRecAvailable) {
						resultSet.add(new ResultTuple("EPDS Enterprise ID", "", "", FAILED,
								"EPDS Enterprise ID is not available in SPS for SPS IDs " + spsIds));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					}

					if (!providerSearch.searchAndSelectOrganization(altIdVal)) {
						resultSet.add(new ResultTuple("Provider", "", altIdVal, FAILED,
								"Provider Organization is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(
								new ResultTuple("Provider", "", altIdVal, PASSED, "Provider Organization is present"));
					}
					LOGGER.info("Provider Organization is present...");

					loginPage.navToAltIdTab();

					for (int i = 1; i <= 3; i++) {

						isSpsRecAvailable = false;
						isJsonRecAvailable = false;

						switch (i) {
						case 2:
							spsAltIdType = "Q/Care Tax ID";
							epdsAltIdSrc = "QCARE";
							epdsAltIdType = "Q/Care Tax ID";
							break;
						case 3:
							spsAltIdType = "Payee ID";
							epdsAltIdSrc = "QCARE";
							epdsAltIdType = "Payee ID";
							break;
						}

						if (i == 1) {
							AltIdDetail altIdDetail = altIdPage.getAltIdDetails(epdsAltIdSrc, epdsAltIdType,
									ProviderType.ORGANIZATION);
							if (altIdDetail == null) {
								resultSet.add(new ResultTuple("Alt Id record", "", epdsAltIdSrc + " : " + epdsAltIdType,
										FAILED, "Alt Id record is not available in EPDSv2"));
								continue;
							}
							expAltIdVal = altIdDetail.getAltIDValue();
							expEffDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
							expTermDate = altIdDetail.getAlternateIDActive().getTerminationDate();
							expTermReasonCode = altIdDetail.getAlternateIDActive().getTerminationReasonCode();
						} else {
							while (rs.next()) {
								altIdSrc = rs.getString("ALTID_SOR_CD");
								altIdType = rs.getString("ALTID_TYPE_CD");
								if (altIdSrc.equalsIgnoreCase(spsAltIdSrc)
										&& altIdType.equalsIgnoreCase(spsAltIdType)) {
									altIdVal = rs.getString("ALTID_TXT");
									effDate = rs.getString("PROV_ALTID_EFCTV_DT");
									effDate = util.spsDbDateConversion(effDate);
									termDate = rs.getString("PROV_ALTID_TRMNTN_DT");
									termDate = util.spsDbDateConversion(termDate);
									termReasonCode = rs.getString("PROV_ALTID_TRMNTN_RSN_CD");
									if (termReasonCode == null)
										termReasonCode = "";
									isSpsRecAvailable = true;
									break;
								}
							}
							rs.beforeFirst();

							if (!isSpsRecAvailable) {
								resultSet.add(new ResultTuple("Alt Id record", "", spsAltIdSrc + " : " + spsAltIdType,
										FAILED, "Alt Id record with is not available in SPS for SPS IDs " + spsIds));
								continue;
							}

							List<AltIdDetail> altIdDetails = json.getAlternateIDs().getAltIdDetails();
							for (AltIdDetail altIdDetail : altIdDetails) {
								expAltIdSrc = altIdDetail.getAltIDSource();
								expAltIdType = altIdDetail.getAltIDType();
								if (expAltIdSrc.equalsIgnoreCase(epdsAltIdSrc)
										&& expAltIdType.equalsIgnoreCase(epdsAltIdType)) {
									expAltIdVal = altIdDetail.getAltIDValue();
									expEffDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
									expTermDate = altIdDetail.getAlternateIDActive().getTerminationDate();
									expTermReasonCode = altIdDetail.getAlternateIDActive().getTerminationReasonCode();
									if (expTermReasonCode == null) {
										expTermReasonCode = "";
									}
									isJsonRecAvailable = true;
									break;
								}
							}

							if (!isJsonRecAvailable) {
								resultSet.add(new ResultTuple("Alt Id record", "", epdsAltIdSrc + " : " + epdsAltIdType,
										FAILED, "Alt Id record is not available in Json"));
								continue;
							}
						}

						if (altIdSrc.equalsIgnoreCase(spsAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)
								&& altIdVal.equalsIgnoreCase(expAltIdVal) && effDate.equalsIgnoreCase(expEffDate)
								&& termDate.equalsIgnoreCase(expTermDate)
								&& termReasonCode.equalsIgnoreCase(expTermReasonCode)) {
							result = PASSED;
						} else {
							result = FAILED;
						}

						spsRecord = altIdSrc + " : " + altIdType + " : " + altIdVal + " : " + effDate + " : " + termDate
								+ " : " + termReasonCode;
						epdsRecord = spsAltIdSrc + " : " + spsAltIdType + " : " + expAltIdVal + " : " + expEffDate
								+ " : " + expTermDate + " : " + expTermReasonCode;
						resultSet.add(new ResultTuple("Alt ID record", spsRecord, epdsRecord, result, ""));
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

	@Test(description = "SPDS-133366 - Edit Grouping - Add Alternate ID in SPS - EPDSv2 Legacy ID", enabled = true, groups = {
			"grp" }) // TODO: incl. dependsOn add flow
	@TestDetails(userstory = "SPDS-133366", author = "Reshmi C", transactionId = "SpsBackSync-Grp", sprint = "Sprint 192")
	public void SpsBackSyncGrp() {

		LOGGER.info("Inside test SpsBackSyncGrp");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String txnId = "GRP_ADD_ENTIRE_FLOW";
		String altIdSrc = "", altIdType = "", altIdVal = "", effDate = "", termDate = "", termReasonCode = "";
		String expAltIdSrc, expAltIdType, expAltIdVal, expEffDate, expTermDate, expTermReasonCode;
		boolean flag;
		String spsRecord, epdsRecord;
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, txnId);
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + txnId);
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
					flag = false;

					List<String> spsIdList = json.getHeader().getSpsIDs();
					String spsIds = "";
					for (String spsId : spsIdList) {
						spsIds = spsIds + "'" + spsId + "',";
					}
					spsIds = spsIds.substring(0, spsIds.length() - 1);

					expAltIdSrc = "EPDSV2.0";
					expAltIdType = "Legacy ID";
					String spsAltIdType = "Legacy Provider ID";

					String query = SPSAltIdQueries.SPS_GRP_ALT_ID_QUERY;
					query = query.replaceAll("<SPS_DBNAME>", spsPropertyMap.get("sps_dbname"));
					query = query.replaceAll("<SPS_ID_LIST>", spsIds);
					ResultSet rs = dbUtil.executeQuery(spsDbConn, query);

					while (rs.next()) {
						altIdSrc = rs.getString("ALTID_SOR_CD");
						altIdType = rs.getString("ALTID_TYPE_CD");
						if (altIdSrc.equalsIgnoreCase(expAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)) {
							altIdVal = rs.getString("ALTID_TXT");
							effDate = rs.getString("GRPG_ALTID_EFCTV_DT");
							effDate = util.spsDbDateConversion(effDate);
							termDate = rs.getString("GRPG_ALTID_TRMNTN_DT");
							termDate = util.spsDbDateConversion(termDate);
							termReasonCode = rs.getString("GRPG_ALTID_TRMNTN_RSN_CD");
							if (termReasonCode == null)
								termReasonCode = "";
							flag = true;
							break;
						}
					}
					if (!flag) {
						resultSet.add(new ResultTuple("EPDS Grouping Code", "", "", FAILED,
								"EPDS Grouping Code is not available in SPS for SPS IDs " + spsIds));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					}

					String groupingName = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingName();
					String groupingType = json.getProfile().getNameQualifier().getGroupingNameQualifier()
							.getGroupingType();
					if (!providerSearch.searchAndSelectGrouping(groupingName, altIdVal, groupingType)) {
						resultSet.add(
								new ResultTuple("Provider", "", altIdVal, FAILED, "Provider Grouping is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet
								.add(new ResultTuple("Provider", "", altIdVal, PASSED, "Provider Grouping is present"));
					}
					LOGGER.info("Provider grouping is present...");

					loginPage.navToGrpAltIdTab();
					AltIdDetail altIdDetail = altIdPage.getAltIdDetails(expAltIdSrc, "WGS HMO Identifier Type Code",
							ProviderType.GROUP);

					expAltIdVal = altIdDetail.getAltIDValue();
					expEffDate = altIdDetail.getAlternateIDActive().getEffectiveDate();
					expTermDate = altIdDetail.getAlternateIDActive().getTerminationDate();
					expTermReasonCode = altIdDetail.getAlternateIDActive().getTerminationReasonCode();

					if (altIdSrc.equalsIgnoreCase(expAltIdSrc) && altIdType.equalsIgnoreCase(spsAltIdType)
							&& altIdVal.equalsIgnoreCase(expAltIdVal) && effDate.equalsIgnoreCase(expEffDate)
							&& termDate.equalsIgnoreCase(expTermDate)
							&& termReasonCode.equalsIgnoreCase(expTermReasonCode)) {
						result = PASSED;
					} else {
						result = FAILED;
					}

					spsRecord = altIdSrc + " : " + altIdType + " : " + altIdVal + " : " + effDate + " : " + termDate
							+ " : " + termReasonCode;
					epdsRecord = expAltIdSrc + " : " + spsAltIdType + " : " + expAltIdVal + " : " + expEffDate + " : "
							+ expTermDate + " : " + expTermReasonCode;
					resultSet.add(new ResultTuple("Alt ID record", spsRecord, epdsRecord, result, ""));

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

	@BeforeClass
	public void connectToSpsDb() {

		LOGGER.info("Connecting to SPS DB...");
		spsPropertyMap = util.getSpsPropertyMap();

		String dbSrvrHost = spsPropertyMap.get("sps_server_host");
		String dbSrvcName = spsPropertyMap.get("sps_service_name");
		String username = spsPropertyMap.get("sps_db_username");
		String password = spsPropertyMap.get("sps_db_password");

		String connStr = "jdbc:oracle:thin:@" + dbSrvrHost + "/" + dbSrvcName;

		try {
			spsDbConn = dbUtil.connectToDB(connStr, username, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void closeDbConn() {

		LOGGER.info("Closing SPS DB connection...");
		try {
			spsDbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
