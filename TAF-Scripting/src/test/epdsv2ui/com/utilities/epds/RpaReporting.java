package com.utilities.epds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.beans.mdxjson.RPAJsonTransaction;
import com.utilities.epds.RpaTestUtilities.TestDetails;

// This class contains methods for reporting purposes.
public class RpaReporting {

	public static Logger LOGGER = Logger.getLogger(RpaReporting.class);

	// This method will write the detailed test result to input stream
	public synchronized void writeDetailedReport(TestDetails testInfo, RPAJsonTransaction json, List<ResultTuple> resultSet) {

		LOGGER.info("Inside RpaReporting writeDetailedReport()");
		FileInputStream inputStream = null;
		Workbook workbook;
		Sheet sheet;
		CellStyle style;
		FalloutRecord falloutValues;

		int xlRowCount = 0;
		int xlResultCol = 0;

		String userstory = testInfo.userstory();
		String sprint = testInfo.sprint();
		String transactionId = testInfo.transactionId();

		Map<String, String> epdsPropertyMap = new RpaTestUtilities().getEpdsPropertyMap();

		// Reading the detailed report sheet column ids
		int rpa_report_userstory_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_userstory_col_number"));
		int rpa_report_sprint_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_sprint_col_number"));
		int rpa_report_transactionId_col_number = Integer
				.parseInt(epdsPropertyMap.get("rpa_report_transactionId_col_number"));
		int rpa_report_provider_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_provider_col_number"));
		int rpa_report_resultType_col_number = Integer
				.parseInt(epdsPropertyMap.get("rpa_report_resultType_col_number"));

		// Reading the user stories/ transactions which have no EPDSV2 ID in Json
		String no_legacyId_txns = epdsPropertyMap.get("no_legacyId_txns");

		for (ResultTuple rt : resultSet) {
			LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result + " : "
					+ rt.comments);
		}

		try {
			// Creating and writing input stream to write to detailed report
			inputStream = new FileInputStream(new File(epdsPropertyMap.get("final_report_location")));
			workbook = WorkbookFactory.create(inputStream);
			sheet = workbook.getSheet("Report");
			xlRowCount = sheet.getLastRowNum();

			Row row = sheet.createRow(++xlRowCount);
			row = sheet.createRow(++xlRowCount);

			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Writing the test details
			Cell cell = row.createCell(rpa_report_userstory_col_number);
			cell.setCellValue(userstory);
			cell = row.createCell(rpa_report_sprint_col_number);
			cell.setCellValue(sprint);
			cell = row.createCell(rpa_report_transactionId_col_number);
			cell.setCellValue(transactionId);

			// Writing provider ID
			// TODO: Workaround to handle null values in Json, to be removed
			String spsId = "", grpSiteCode = "", legacyId = "";
			try {
				legacyId = json.getHeader().getLegacyID();
				spsId = String.join(",", json.getHeader().getSpsIDs());
				if (transactionId.startsWith("GRP"))
					grpSiteCode = json.getProfile().getNameQualifier().getGroupingNameQualifier().getGroupingSiteCode();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			cell = row.createCell(rpa_report_provider_col_number);
			if (no_legacyId_txns.contains(transactionId))
				cell.setCellValue("SPS ID : " + spsId);
			else if (transactionId.startsWith("GRP"))
				cell.setCellValue(grpSiteCode);
			else
				cell.setCellValue(legacyId);

			// Writing the fallout report values
			falloutValues = new FalloutRecord(json, transactionId);
			cell = row.createCell(rpa_report_provider_col_number + 1);
			cell.setCellValue(falloutValues.ticketType);
			cell = row.createCell(rpa_report_provider_col_number + 2);
			cell.setCellValue(falloutValues.providerType);
			cell = row.createCell(rpa_report_provider_col_number + 3);
			cell.setCellValue(falloutValues.providerName);
			cell = row.createCell(rpa_report_provider_col_number + 4);
			cell.setCellValue(falloutValues.providerTitle);
			cell = row.createCell(rpa_report_provider_col_number + 5);
			cell.setCellValue(falloutValues.taxId);
			cell = row.createCell(rpa_report_provider_col_number + 6);
			cell.setCellValue(falloutValues.npi);
			cell = row.createCell(rpa_report_provider_col_number + 7);
			cell.setCellValue(falloutValues.practiceAddressLine1);
			cell = row.createCell(rpa_report_provider_col_number + 8);
			cell.setCellValue(falloutValues.practiceAddressLine2);
			cell = row.createCell(rpa_report_provider_col_number + 9);
			cell.setCellValue(falloutValues.city);
			cell = row.createCell(rpa_report_provider_col_number + 10);
			cell.setCellValue(falloutValues.state);
			cell = row.createCell(rpa_report_provider_col_number + 11);
			cell.setCellValue(falloutValues.zip);
			cell = row.createCell(rpa_report_provider_col_number + 12);
			cell.setCellValue(falloutValues.county);

			cell = row.createCell(rpa_report_resultType_col_number);
			cell.setCellValue("Fields");

			// Coloring the first row of results
			for (int i = 0; i <= rpa_report_resultType_col_number; i++) {
				row.getCell(i).setCellStyle(style);
			}

			Row actualRow = sheet.createRow(++xlRowCount);
			cell = actualRow.createCell(rpa_report_resultType_col_number);
			cell.setCellValue("Actual");

			Row expectedRow = sheet.createRow(++xlRowCount);
			cell = expectedRow.createCell(rpa_report_resultType_col_number);
			cell.setCellValue("Expected");

			Row resultRow = sheet.createRow(++xlRowCount);
			cell = resultRow.createCell(rpa_report_resultType_col_number);
			cell.setCellValue("Result");

			Row commentsRow = sheet.createRow(++xlRowCount);
			cell = commentsRow.createCell(rpa_report_resultType_col_number);
			cell.setCellValue("Comments");

			xlResultCol = rpa_report_resultType_col_number + 1;

			// Writing the test results
			for (ResultTuple rt : resultSet) {
				cell = row.createCell(xlResultCol);
				cell.setCellValue(rt.fieldName);
				cell.setCellStyle(style);

				cell = actualRow.createCell(xlResultCol);
				cell.setCellValue(rt.actual);

				cell = expectedRow.createCell(xlResultCol);
				cell.setCellValue(rt.expected);

				cell = resultRow.createCell(xlResultCol);
				cell.setCellValue(rt.result);

				cell = commentsRow.createCell(xlResultCol);
				cell.setCellValue(rt.comments);

				xlResultCol++;
			}

			// Write the input stream workbook to detailed report
			writeToXl(epdsPropertyMap.get("final_report_location"), workbook);
			workbook.close();
			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method will write the input stream to excel workbook
	public void writeToXl(String xlLocation, Workbook workbook) {

		LOGGER.info("Inside RpaReporting writeToXl()");
		FileOutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(new File(xlLocation));
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void writeDetailedReport(TestDetails testInfo, String providerId, List<ResultTuple> resultSet) {
//
//		LOGGER.info("Inside Reporting writeDetailedReport()");
//		FileInputStream inputStream = null;
//
//		String userstory = testInfo.userstory();
//		String sprint = testInfo.sprint();
//		String transactionId = testInfo.transactionId();
//
//		int xlRowCount = 0;
//		int xlResultCol = 0;
//
//		for (ResultTuple rt : resultSet) {
//			LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result + " : "
//					+ rt.comments);
//		}
//
//		Map<String, String> epdsPropertyMap = new RpaTestUtilities().getEpdsPropertyMap();
//
//		// Reading the detailed report sheet column ids
//		int rpa_report_userstory_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_userstory_col_number"));
//		int rpa_report_sprint_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_sprint_col_number"));
//		int rpa_report_transactionId_col_number = Integer
//				.parseInt(epdsPropertyMap.get("rpa_report_transactionId_col_number"));
//		int rpa_report_provider_col_number = Integer.parseInt(epdsPropertyMap.get("rpa_report_provider_col_number"));
//		int rpa_report_resultType_col_number = Integer
//				.parseInt(epdsPropertyMap.get("rpa_report_resultType_col_number"));
//
//		try {
//			// Creating and writing input stream to write to detailed report
//			inputStream = new FileInputStream(new File(epdsPropertyMap.get("final_report_location")));
//			Workbook workbook = WorkbookFactory.create(inputStream);
//			Sheet sheet = workbook.getSheet("Report");
//			xlRowCount = sheet.getLastRowNum();
//
//			Row row = sheet.createRow(++xlRowCount);
//			row = sheet.createRow(++xlRowCount);
//
//			CellStyle style = workbook.createCellStyle();
//			style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
//			style.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
//			row.setRowStyle(style);
//
//			Cell cell = row.createCell(rpa_report_userstory_col_number);
//			cell.setCellValue(userstory);
//			cell = row.createCell(rpa_report_sprint_col_number);
//			cell.setCellValue(sprint);
//			cell = row.createCell(rpa_report_transactionId_col_number);
//			cell.setCellValue(transactionId);
//			cell = row.createCell(rpa_report_provider_col_number);
//			cell.setCellValue(providerId);
//
//			xlResultCol = rpa_report_resultType_col_number + 1;
//
//			cell = row.createCell(rpa_report_resultType_col_number);
//			cell.setCellValue("Fields");
//
//			Row actualRow = sheet.createRow(++xlRowCount);
//			cell = actualRow.createCell(rpa_report_resultType_col_number);
//			cell.setCellValue("Actual");
//
//			Row expectedRow = sheet.createRow(++xlRowCount);
//			cell = expectedRow.createCell(rpa_report_resultType_col_number);
//			cell.setCellValue("Expected");
//
//			Row resultRow = sheet.createRow(++xlRowCount);
//			cell = resultRow.createCell(rpa_report_resultType_col_number);
//			cell.setCellValue("Result");
//
//			Row commentsRow = sheet.createRow(++xlRowCount);
//			cell = commentsRow.createCell(rpa_report_resultType_col_number);
//			cell.setCellValue("Comments");
//
//			for (ResultTuple rt : resultSet) {
//				cell = row.createCell(xlResultCol);
//				cell.setCellValue(rt.fieldName);
//
//				cell = actualRow.createCell(xlResultCol);
//				cell.setCellValue(rt.actual);
//
//				cell = expectedRow.createCell(xlResultCol);
//				cell.setCellValue(rt.expected);
//
//				cell = resultRow.createCell(xlResultCol);
//				cell.setCellValue(rt.result);
//
//				cell = commentsRow.createCell(xlResultCol);
//				cell.setCellValue(rt.comments);
//
//				xlResultCol++;
//			}
//
//			// Write the input stream workbook to detailed report
//			writeToXl(epdsPropertyMap.get("final_report_location"), workbook);
//			workbook.close();
//			inputStream.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
