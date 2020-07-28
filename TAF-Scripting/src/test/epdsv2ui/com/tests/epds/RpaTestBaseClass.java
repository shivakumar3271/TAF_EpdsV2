package com.tests.epds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.scripted.reporting.AllureListener;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.RpaReporting;
import com.utilities.epds.RpaTestUtilities;

@Listeners({ AllureListener.class })
public class RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(RpaTestBaseClass.class);

	WebDriver driver = null;
	public static Connection mdxDbConn = null;
	public static Map<String, String> epdsPropertyMap = new HashMap<String, String>();

	RpaTestUtilities util = new RpaTestUtilities();
	RpaReporting reporting = new RpaReporting();

	public final static String PASSED = "PASSED";
	public final static String FAILED = "FAILED";

	@BeforeSuite
	public void testExecSetup() {

		BasicConfigurator.configure();
		LOGGER.info("Before Suite method: testExecSetup()");

		epdsPropertyMap = util.getEpdsPropertyMap();
		prepareTestFiles();
		mdxDbConn = util.connectToMdxDB();
	}

	@AfterSuite
	public void testTearDown() {
		LOGGER.info("After Suite method: testTearDown()");
		util.closeMdxDBConn(mdxDbConn);
	}

	public void launchBrowser() {

		LOGGER.info("Before test : launchBrowser()");

		// Use this for local execution
		driver = BrowserDriver.funcGetWebdriver();

		// Use this for parallel execution on grid
		//BrowserDriver.getSeleniunGridDriver("chrome");
		//driver = BrowserDriver.getDriver();

		// AllureListener.setDriver(driver);
		BrowserDriver.pageWait();

		LOGGER.info("Logging in to EPDSv2 UI...");
		BrowserDriver.launchWebURL(epdsPropertyMap.get("epdsv2_sit_url"));
	}

	public void closeBrowser() {
		LOGGER.info("After test : closeBrowser()");
		//BrowserDriver.getDriver().close();
	}

	public void prepareTestFiles() {

		LOGGER.info("Preparing the test case report excel...");
		String srcReportFilePath = epdsPropertyMap.get("src_report_location");
		String finalReportFilePath = epdsPropertyMap.get("final_report_location");

		File finalReportFile = new File(finalReportFilePath);
		if (finalReportFile.exists())
			finalReportFile.delete();

		Path source = Paths.get(srcReportFilePath);
		Path dest = Paths.get(finalReportFilePath);
		try {
			Files.copy(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
