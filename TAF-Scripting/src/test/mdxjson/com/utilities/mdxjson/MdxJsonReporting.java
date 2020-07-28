package com.utilities.mdxjson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

// This class contains methods for reporting purposes.
public class MdxJsonReporting {

	public static Logger LOGGER = Logger.getLogger(MdxJsonReporting.class);

	// This method will write the detailed test result to input stream
	public void writeDetailedReport(LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>> consolidatedResultMap) {

		LOGGER.info("Inside MdxJsonReporting writeDetailedReport()");
		FileInputStream inputStream = null;
		Workbook workbook;
		Sheet sheet;
		CellStyle style;

		int xlRowCount = 0;
		int xlResultCol = 0;

		Map<String, String> mdxJsonPropertyMap = new MdxJsonTestUtilities().getMdxJsonPropertyMap();

		// Reading the detailed report sheet column ids
		int rpa_report_provider_col_number = Integer.parseInt(mdxJsonPropertyMap.get("mdxjson_report_provider_col_number"));
		int rpa_report_spsid_col_number = Integer.parseInt(mdxJsonPropertyMap.get("mdxjson_report_spsid_col_number"));
		int rpa_report_prov_type_col_number = Integer
				.parseInt(mdxJsonPropertyMap.get("mdxjson_report_prov_type_col_number"));
		int rpa_report_result_col_number = Integer.parseInt(mdxJsonPropertyMap.get("mdxjson_report_result_col_number"));

//		Set<OdsmRecordDetail> recDetailSet = consolidatedResultMap.keySet();
//		for (OdsmRecordDetail recDetail : recDetailSet) {
//			List<ResultTuple> resList = consolidatedResultMap.get(recDetail);
//			for (ResultTuple rt : resList) {
//				LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result
//						+ " : " + rt.comments);
//			}
//		}

		try {
			// Creating and writing input stream to write to detailed report
			inputStream = new FileInputStream(new File(mdxJsonPropertyMap.get("final_report_location")));
			workbook = WorkbookFactory.create(inputStream);
			sheet = workbook.getSheet("Report");

			Set<ProviderDetail> recDetailSet = consolidatedResultMap.keySet();
			for (ProviderDetail recDetail : recDetailSet) {
				LinkedHashSet<ResultTuple> resultList = consolidatedResultMap.get(recDetail);

				String epdsId = recDetail.epdsId;
				String spsId = recDetail.spsId;
				String provType = recDetail.providerType;

				xlRowCount = sheet.getLastRowNum();
				Row row = sheet.createRow(++xlRowCount);
				row = sheet.createRow(++xlRowCount);

				style = workbook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				// Writing the test details
				Cell cell = row.createCell(rpa_report_provider_col_number);
				cell.setCellValue(epdsId);
				cell = row.createCell(rpa_report_spsid_col_number);
				cell.setCellValue(spsId);
				cell = row.createCell(rpa_report_prov_type_col_number);
				cell.setCellValue(provType);

				cell = row.createCell(rpa_report_result_col_number);
				cell.setCellValue("Fields");

				// Coloring the first row of results
				for (int i = 0; i <= rpa_report_result_col_number; i++) {
					row.getCell(i).setCellStyle(style);
				}

				Row actualRow = sheet.createRow(++xlRowCount);
				cell = actualRow.createCell(rpa_report_result_col_number);
				cell.setCellValue("Json Value");

				Row expectedRow = sheet.createRow(++xlRowCount);
				cell = expectedRow.createCell(rpa_report_result_col_number);
				cell.setCellValue("Record Value");

				Row resultRow = sheet.createRow(++xlRowCount);
				cell = resultRow.createCell(rpa_report_result_col_number);
				cell.setCellValue("Result");

				Row commentsRow = sheet.createRow(++xlRowCount);
				cell = commentsRow.createCell(rpa_report_result_col_number);
				cell.setCellValue("Comments");

				xlResultCol = rpa_report_result_col_number + 1;

				// Writing the test results
				for (ResultTuple rt : resultList) {
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
			}

			// Write the input stream workbook to detailed report
			writeToXl(mdxJsonPropertyMap.get("final_report_location"), workbook);
			workbook.close();
			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method will write the input stream to excel workbook
	public void writeToXl(String xlLocation, Workbook workbook) {

		LOGGER.info("Inside Reporting writeToXl()");
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

}
