package com.tests.epds;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.scripted.database.OracleUtil;
import com.scripted.database.SQLServerUtil;
import com.utilities.epds.FalloutReportQueries;
import com.utilities.epds.RpaReporting;
import com.utilities.epds.RpaTestUtilities;

public class FalloutReportValidation {

	public static Logger LOGGER = Logger.getLogger(FalloutReportValidation.class);

	Connection pegaDbConn = null;
	Map<String, String> epdsPropertyMap = null;

	RpaTestUtilities util = new RpaTestUtilities();
	OracleUtil oracleDbUtil = new OracleUtil();
	SQLServerUtil sqlDbUtil = new SQLServerUtil();

	@Test(enabled = true)
	public void validateFalloutReport() {

		LOGGER.info("Inside test validateFalloutReport");
		FileInputStream inputStream = null;
		Workbook workbook;
		Sheet reportSheet;

		Row currentRow, fieldNameRow;
		int currRowInd, lastCellInd;
		String provId, txnId, errMsg = "";
		Map<String, String> falloutValues = new HashMap<String, String>();

		epdsPropertyMap = util.getEpdsPropertyMap();

		// Reading the report sheet column ids
		int rpa_report_userstory_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_userstory_col_number"));
		int rpa_report_transactionId_col_number = Integer
				.parseInt(epdsPropertyMap.get("rpa_report_transactionId_col_number"));
		int rpa_report_provider_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_provider_col_number"));
		int rpa_report_resultType_col_number = Integer
				.parseInt(epdsPropertyMap.get("rpa_report_resultType_col_number"));

		try {
			inputStream = new FileInputStream(new File(epdsPropertyMap.get("final_report_location")));
			workbook = WorkbookFactory.create(inputStream);
			reportSheet = workbook.getSheet("Report");

			Iterator<Row> iterator = reportSheet.iterator();

			while (iterator.hasNext()) {
				currentRow = iterator.next();

				if (currentRow.getCell(rpa_report_userstory_col_number) != null) {
					if (currentRow.getCell(rpa_report_userstory_col_number).getStringCellValue().startsWith("SPDS")) {

						// Reading provider details
						provId = currentRow.getCell(rpa_report_provider_col_number).getStringCellValue();
						txnId = currentRow.getCell(rpa_report_transactionId_col_number).getStringCellValue();

						falloutValues.put("ticketType",
								currentRow.getCell(rpa_report_provider_col_number + 1).getStringCellValue());
						falloutValues.put("providerType",
								currentRow.getCell(rpa_report_provider_col_number + 2).getStringCellValue());
						falloutValues.put("providerName",
								currentRow.getCell(rpa_report_provider_col_number + 3).getStringCellValue());
						falloutValues.put("providerTitle",
								currentRow.getCell(rpa_report_provider_col_number + 4).getStringCellValue());
						falloutValues.put("taxId",
								currentRow.getCell(rpa_report_provider_col_number + 5).getStringCellValue());
						falloutValues.put("npi",
								currentRow.getCell(rpa_report_provider_col_number + 6).getStringCellValue());
						falloutValues.put("addrLine1",
								currentRow.getCell(rpa_report_provider_col_number + 7).getStringCellValue());
						falloutValues.put("addrLine2",
								currentRow.getCell(rpa_report_provider_col_number + 8).getStringCellValue());
						falloutValues.put("city",
								currentRow.getCell(rpa_report_provider_col_number + 9).getStringCellValue());
						falloutValues.put("state",
								currentRow.getCell(rpa_report_provider_col_number + 10).getStringCellValue());
						falloutValues.put("zip",
								currentRow.getCell(rpa_report_provider_col_number + 11).getStringCellValue());
						falloutValues.put("county",
								currentRow.getCell(rpa_report_provider_col_number + 12).getStringCellValue());

						fieldNameRow = currentRow;
						currRowInd = currentRow.getRowNum() + 3;
						currentRow = reportSheet.getRow(currRowInd);

						if (currentRow.getCell(rpa_report_resultType_col_number).getStringCellValue()
								.equals("Result")) {
							lastCellInd = currentRow.getLastCellNum();

							int i = rpa_report_resultType_col_number + 1;
							for (; i < lastCellInd; i++) {
								if (currentRow.getCell(i).getStringCellValue().equals("FAILED")) {
//								currRowInd = currentRow.getRowNum() + 1;
//								nxtRow = reportSheet.getRow(currRowInd);
//								errMsg = nxtRow.getCell(i).getStringCellValue();
//								System.out.println(errMsg);
									break;
								}
							}
//							LOGGER.info(currentRow.getRowNum() + " : "
//									+ currentRow.getCell(lastCellInd - 1).getColumnIndex());
							fieldNameRow.createCell(lastCellInd).setCellValue("Final Result");
							if (i == lastCellInd) {
								currentRow.createCell(lastCellInd).setCellValue("PASS");
							} else {
								currentRow.createCell(lastCellInd).setCellValue("FAIL");
							}
							errMsg = validateRpaPegaDb(provId, txnId, falloutValues);
							fieldNameRow.createCell(lastCellInd + 1).setCellValue("RPA Error Message");
							currentRow.createCell(lastCellInd + 1).setCellValue(errMsg);
						}
					}
				}
			}
			// Write the input stream workbook to detailed report
			new RpaReporting().writeToXl(epdsPropertyMap.get("final_report_location"), workbook);
			workbook.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String validateRpaPegaDb(String provId, String txnId, Map<String, String> falloutValues) {

		LOGGER.info("Inside method validateRpaPegaDb()");
//		System.out.println(falloutValues.get("ticketType"));
//		System.out.println(falloutValues.get("providerType"));
//		System.out.println(falloutValues.get("providerName"));
//		System.out.println(falloutValues.get("providerTitle"));
//		System.out.println(falloutValues.get("taxId"));
//		System.out.println(falloutValues.get("npi"));
//		System.out.println(falloutValues.get("addrLine1"));
//		System.out.println(falloutValues.get("addrLine2"));
//		System.out.println(falloutValues.get("city"));
//		System.out.println(falloutValues.get("state"));
//		System.out.println(falloutValues.get("zip"));
//		System.out.println(falloutValues.get("county"));

		String providerState = "GA";
		String networks = "Commercial";
		String providersImpacted = "1";
		String licenseNumber = "NA";

		String query = "select * from rpapega_data.pc_auto_robot_work where enterpriseid = '" + provId
				+ "' and transactiontype = '" + txnId + "' and tickettype = '" + falloutValues.get("ticketType")
				+ "' and providertype = '" + falloutValues.get("providerType") + "' and providerstate = '"
				+ providerState + "' and networks = '" + networks + "' and providerimpacted = '" + providersImpacted
				+ "' and providername = '" + falloutValues.get("providerName") + "' and providertitle = '"
				+ falloutValues.get("providerTitle") + "' and licensenumber = '" + licenseNumber + "' and taxid = '"
				+ falloutValues.get("taxId") + "' and npi = '" + falloutValues.get("npi")
				+ "' and practiceaddressline1 = '" + falloutValues.get("addrLine1") + "' and practiceaddressline2 = '"
				+ falloutValues.get("addrLine2") + "' and city = '" + falloutValues.get("city") + "' and state = '"
				+ falloutValues.get("state") + "' and zip = '" + falloutValues.get("zip") + "' and county = '"
				+ falloutValues.get("county") + "'";
		// LOGGER.info("Query: " + query);

		try {
			ResultSet rs = oracleDbUtil.executeQuery(pegaDbConn, query);
			if (!rs.next()) {
				LOGGER.info("Failed to retrieve the fallout record from PEGA DB for Provider Id : " + provId
						+ " and Transaction ID : " + txnId);
				return ("Failed to retrieve the fallout record from PEGA DB");
			} else {
				return rs.getString("messagedetails");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ("Error in querying PEGA DB");
	}

	@Test
	public void validateStatisticalReport() {

		LOGGER.info("Inside test validateStatisticalReport");
		int totalTxns = 0, successfulTxns = 0, failedTxns;
		float successRate, failRate;

		Connection mdxDbConn = util.connectToMdxDB();

		String query = FalloutReportQueries.STATISTICAL_REPORT_MDX_TOTAL_TXNS;
		try {
			ResultSet rs = sqlDbUtil.executeQuery(mdxDbConn, query);
			mdxDbConn.close();
			if (rs.next()) {
				totalTxns = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		query = FalloutReportQueries.STATISTICAL_REPORT_PEGA_SUCCESS_TXNS;
		try {
			ResultSet rs = oracleDbUtil.executeQuery(pegaDbConn, query);
			if (rs.next()) {
				successfulTxns = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		failedTxns = totalTxns - successfulTxns;
		successRate = ((float) successfulTxns / totalTxns) * 100;
		failRate = ((float) failedTxns / totalTxns) * 100;

		LOGGER.info("********** RESULTS **********");
		LOGGER.info("Total number of transactions : " + totalTxns);
		LOGGER.info("Transactions successful : " + successfulTxns);
		LOGGER.info("Transaction success rate : " + successRate);
		LOGGER.info("Transactions failed : " + failedTxns);
		LOGGER.info("Transaction fail rate: " + failRate);
		LOGGER.info("*****************************");

	}

	@BeforeTest
	public void connectToPegaDB() {

		LOGGER.info("Connecting to PEGA DB...");
		Map<String, String> spsPropertyMap = util.getSpsPropertyMap();

		String dbSrvrHost = spsPropertyMap.get("pega_server_host");
		String dbSrvcName = spsPropertyMap.get("pega_service_name");
		String username = spsPropertyMap.get("pega_db_username");
		String password = spsPropertyMap.get("pega_db_password");

		String connStr = "jdbc:oracle:thin:@" + dbSrvrHost + "/" + dbSrvcName;

		try {
			pegaDbConn = oracleDbUtil.connectToDB(connStr, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterTest
	public void closeDbConn() {

		LOGGER.info("Closing PEGA DB connection...");
		try {
			pegaDbConn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
