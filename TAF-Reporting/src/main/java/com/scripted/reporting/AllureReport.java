package com.scripted.reporting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;
import junit.framework.Assert;

public class AllureReport {

	public static String screenshotPath = "";
	public static WebDriver driver = null;
	private static String cdir = System.getProperty("user.dir");
	public static Logger LOGGER = Logger.getLogger(AllureReport.class);

	public static void addStep(String stepDescription) {
		Allure.step(stepDescription);
	}

	public static void addScreenshot(String screenShotname) {

		try {
			// Save screenshot in the SkriptMate Report
			driver = AllureListener.getDriver();
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
			screenshotPath = cdir + "\\src\\main\\resources\\Screenshots\\" + timeStamp + "\\";
			com.scripted.generic.FileUtils.makeDirs(screenshotPath);
			File DestFile = new File(screenshotPath + SrcFile.getName());
			FileUtils.copyFile(SrcFile, DestFile);
			Path content = Paths.get(DestFile.getPath());
			try (InputStream is = Files.newInputStream(content)) {
				Allure.addAttachment(screenShotname, is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while saving screenshot in the SkriptMate Report ");
			Assert.fail("Error while saving screenshot in the SkriptMate Report ");
		}
	}

	/*
	 * public static void addLog(String logName) { try { // Save log in the
	 * Skriptmate Report Allure.addAttachment(logName,
	 * "C:\\Users\\U51582\\Desktop\\sample.text");
	 * 
	 * }catch(Exception e) { System.out.println(e.getMessage()); } }
	 */

	public static void customizeReport() throws Exception {
		try {
			// Customize the Skriptmate Report
			LOGGER.info("Generating Skriptmate Allure Report");
			String workingDir = com.scripted.generic.FileUtils.getCurrentDir();
			String reportGenFolder = "SkriptmateReport\\Allure";
			String currentTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String filePath = workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp
					+ "\\widgets\\environment.json";

			// Execute the command to generate allure report
			String command = "allure generate -c " + "\"" + workingDir + "\\allure-results" + "\"" + " -o " + "\""
					+ workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp + "\"";
			System.out.println("Command : " + command);
			final Process process = Runtime.getRuntime().exec("cmd /c start cmd.exe /C" + command);

			while (process.isAlive() == true) {
				Thread.sleep(5000);
				LOGGER.info("Generating Report....");
			}

			LOGGER.info("Report Folder " + reportGenFolder + "\\" + currentTimeStamp);
			
			com.scripted.generic.FileUtils.waitforFile(
					workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp + "\\" + "styles.css", 1000);

			com.scripted.generic.FileUtils
					.deleteFile(workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp + "\\styles.css");
			com.scripted.generic.FileUtils.fileCopy("src\\main\\resources\\SkriptmateArtefacts\\styles.css",
					workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp + "\\styles.css");

			File f1 = new File("src\\main\\resources\\SkriptmateArtefacts\\Logo.png");
			File f2 = new File(workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp + "\\Logo.png");
			FileUtils.copyFile(f1, f2);

			summaryJsonEdit(workingDir + "\\" + reportGenFolder + "\\" + currentTimeStamp);

			String apackageName = "Skriptmate Package";
			String start = "---";
			String end = "---";
			String totalcount = "---";
			String passedcount = "---";
			String failedcount = "---";
			String bname = "Skriptmate";

			StringBuffer sb = new StringBuffer();
			sb.append("[{");
			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Project Name :" + apackageName + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + apackageName + "\"");
			 */
			/*
			 * sb.append(" :\"" + "apackageName" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Start Date and Time :" + start + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + start + "\"");
			 */
			/*
			 * sb.append(" :\"" + "start" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "End Date and Time :" + end + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + end + "\"");
			 */
			/*
			 * sb.append(" :\"" + "end" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Total Number of Test Cases :" + totalcount + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + totalcount + "\"");
			 */
			/*
			 * sb.append(" :\"" + "totalcount" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Passed :" + passedcount + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + passedcount + "\"");
			 */
			/*
			 * sb.append(" :\"" + "passedcount" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Failed :" + failedcount + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + failedcount + "\"");
			 */
			/*
			 * sb.append(" :\"" + "failedcount" + "\"");
			 */ sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Execution browser :" + bname + "\"");
			sb.append("\n");
			/*
			 * sb.append("\"buildName\""); sb.append(" :\"" + bname + "\"");
			 */
			/*
			 * sb.append(" :\"" + "bname" + "\"");
			 */ sb.append("\n");

			sb.append("}]");

			/*
			 * FileWriter writer = new FileWriter( workingDir + "//" + reportGenFolder +
			 * "//" + currentTimeStamp + "//" + "widgets//environment.json");
			 */
			Files.write(new File(filePath).toPath(), sb.toString().getBytes());
			LOGGER.info("Skriptmate Allure Report Generated Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while generating skriptMate Allure report" + e.getMessage());
			Assert.fail("Error while generating skriptMate Allure report" + e.getMessage());
		}

	}

	public static void summaryJsonEdit(String reportPath) {
		try {

			JSONParser parser = new JSONParser();
			String summaryJsonPath = reportPath + "\\widgets\\summary.json";
			com.scripted.generic.FileUtils.waitforFile(summaryJsonPath, 1000);

			Reader reader = new FileReader(summaryJsonPath);

			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			jsonObject.put("reportName", "Skriptmate Allure Report");

			FileWriter file = new FileWriter(summaryJsonPath);
			file.write(jsonObject.toString());
			file.flush();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while summarizing Skriptmate Allure Report");
			Assert.fail("Error while summarizing Skriptmate Allure Report");
		}
	}

}
