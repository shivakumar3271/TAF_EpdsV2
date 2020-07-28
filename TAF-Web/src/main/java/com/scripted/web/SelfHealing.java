package com.scripted.web;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.scripted.dataload.DBDriver;

import junit.framework.Assert;


public class SelfHealing {

	public static Logger LOGGER = Logger.getLogger(SelfHealing.class);
	static HashMap<Integer, String> locPriority = new HashMap<Integer, String>();
	static HashMap<String, String> objLocValues = new HashMap<String, String>();
	static WebElement element = null;

	public static void getElementPriority(String appName) {
		try {
			DBDriver db = new DBDriver("mysql", "jdbc:mysql://localhost:3306/selfhealing", "root", "system");
			db.getConnection();
			db.getResultSet("select priority1,priority2,priority3 from locatorpriority where applicationID "
					+ "in (select applicationID from applicationdetails where applicationName = '" + appName + "')");
			locPriority.put(1, db.getData(0, 1));
			locPriority.put(2, db.getData(0, 2));
			locPriority.put(3, db.getData(0, 3));
			LOGGER.info(locPriority);
		} catch (Exception e) {
			LOGGER.error("Error while trying to get priority values for elements"+e);
			Assert.fail("Error while trying to get priority values for elements"+e);
		}
	}

	public static void getObjLocValues(String objName, String appName) {
		try {
			DBDriver db = new DBDriver("mysql", "jdbc:mysql://localhost:3306/selfhealing", "root", "system");
			db.getConnection();
			db.getResultSet("select " + locPriority.get(1) + " from objectlocators where objname='" + objName
					+ "' and applicationID in (select applicationID from applicationdetails where applicationName = '"
					+ appName + "')");
			objLocValues.put(locPriority.get(1), db.getData(0, 1));
			db.getResultSet("select " + locPriority.get(2) + " from objectlocators where objname='" + objName
					+ "' and applicationID in (select applicationID from applicationdetails where applicationName = '"
					+ appName + "')");
			objLocValues.put(locPriority.get(2), db.getData(0, 1));
			db.getResultSet("select " + locPriority.get(3) + " from objectlocators where objname='" + objName
					+ "' and applicationID in (select applicationID from applicationdetails where applicationName = '"
					+ appName + "')");
			objLocValues.put(locPriority.get(3), db.getData(0, 1));

			LOGGER.info(objLocValues);
		} catch (Exception e) {
			LOGGER.error("Error while trying to get Object location values"+e);
			Assert.fail("Error while trying to get Object location values"+e);
		}
	}

	public static boolean verifyElement() {
		boolean eleFound = false;
		try {
			for (int i = 1; i <= locPriority.size(); i++) {
				switch (locPriority.get(i)) {
				case "id":
					try {
						element = BrowserDriver.driver.findElement(By.id(objLocValues.get("id")));
						eleFound = true;
						break;
					} catch (Exception e) {
						eleFound = false;
						continue;
					}
				case "name":
					try {
						element = BrowserDriver.driver.findElement(By.name(objLocValues.get("name")));
						eleFound = true;
						break;
					} catch (Exception e) {
						eleFound = false;
						continue;
					}
				case "xpath":
					try {
						element = BrowserDriver.driver.findElement(By.xpath(objLocValues.get("xpath")));
						eleFound = true;
						break;
					} catch (Exception e) {
						eleFound = false;
						continue;
					}
				}
				break;
			}
			return eleFound;
		} catch (Exception e) {
			LOGGER.error("Error while veryfying element"+e);
			Assert.fail("Error while veryfying element"+e);
		}
		return eleFound;
	}

	// Sample for implementation illustration
	public static void main(String[] args) {
		boolean flag = false;
		try {

			getElementPriority("application1");
			getObjLocValues("txtlogin", "application1");

			BrowserDriver.funcGetWebdriver();
			BrowserDriver.driver.get("https://www.google.com");
			BrowserDriver.driver.findElement(By.xpath("//input[@name='button']")).click();
			flag = verifyElement();
			if (flag) {
				element.sendKeys("hello");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
