package com.scripted.testscripts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.scripted.CRMPomObjects.CRMContactsPage;
import com.scripted.CRMPomObjects.CRMLoginPage;
import com.scripted.dataload.ExcelConnector;
import com.scripted.dataload.PropertyDriver;
import com.scripted.dataload.TestDataFactory;
import com.scripted.dataload.TestDataObject;
import com.scripted.generic.FileUtils;
import com.scripted.web.BrowserDriver;

public class TC001AddAndDeleteContactSel {
	static HashMap<Integer, String> headermap = new HashMap<Integer, String>();

	public static void main(String[] args) throws InterruptedException {

		try {
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/properties/selenoidBrowserConfig.properties");
			String strBrowserNameAndVersion = p.readProp("browserNameAndVersion");
			String[] browsers = strBrowserNameAndVersion.split("#");
			for (String browser : browsers) {
				String[] versionList = browser.split(":");
				String[] versions = versionList[1].split(",");
				for (String version : versions) {
					BrowserDriver.driver = BrowserDriver.getSelenoidDriverSeq(versionList[0], version);
					script(BrowserDriver.driver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void script(WebDriver driver) {
		try {
			BrowserDriver.launchWebURL("https://ui.freecrm.com");
			System.out.println(driver.getTitle());

			CRMLoginPage crmLPage = PageFactory.initElements(driver, CRMLoginPage.class);
			CRMContactsPage crmCPage = PageFactory.initElements(driver, CRMContactsPage.class);

			PropertyDriver propDriver = new PropertyDriver();
			propDriver.setPropFilePath("src/main/resources/properties/Cogmento.properties");
			String filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
			String sheetname = "TC001";
			String key = "";

			ExcelConnector con = new ExcelConnector(filename, sheetname);
			TestDataFactory test = new TestDataFactory();
			TestDataObject obj = test.GetData(key, con);

			LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();

			Map<String, String> fRow = (getAllData.get("1"));
			crmLPage.login(fRow.get("userName"), fRow.get("password"));
			crmCPage.clickContacts();
			crmCPage.enterPersonalDetails(fRow.get("firstName"), fRow.get("lastName"), fRow.get("company"),
					fRow.get("timezone"));
			crmCPage.enterBOD(fRow.get("bdate"), fRow.get("bmonth"), fRow.get("byear"));

			crmCPage.clickSaveBtn();
			Thread.sleep(2000);
			crmCPage.deleteRecord(fRow.get("firstName") + " " + fRow.get("lastName"));
			Thread.sleep(2000);
			crmCPage.logout();
			BrowserDriver.closeBrowser();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
