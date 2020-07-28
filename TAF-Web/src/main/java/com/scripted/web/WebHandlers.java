package com.scripted.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import com.scripted.dataload.PropertyDriver;
import com.scripted.generic.FileUtils;

import junit.framework.Assert;

public class WebHandlers {
	public static Actions action = new Actions(BrowserDriver.getDriver());
	public static Logger LOGGER = Logger.getLogger(WebHandlers.class);
	public static WiniumDriver desktopDriver = null;
	public static WebDriver driver;
	static PropertyDriver configProp = null;

	public static WebElement getElement(WebDriver driver, By locator) {
		WebElement element = driver.findElement(locator);
		return element;
	}

	// return ByType of WebElement
	public static By webElementToBy(WebElement webEle) {
		// By format = "[foundFrom] -> locator: term"
		// see RemoteWebElement toString() implementation
		String flag = webEle.toString();
		flag = flag.substring(1, flag.length() - 1);
		String[] data = null;
		data = flag.split(" -> ")[1].split(": ");

		String locator = data[0];
		String term = data[1];

		switch (locator) {
		case "xpath":
			return By.xpath(term);
		case "css selector":
			return By.cssSelector(term);
		case "id":
			return By.id(term);
		case "tag name":
			return By.tagName(term);
		case "name":
			return By.name(term);
		case "link text":
			return By.linkText(term);
		case "class name":
			return By.className(term);
		}
		return (By) webEle;
	}

	public static void clickByJsExecutor(By locator) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static boolean verifyText(WebElement locator, String strVText) {
		boolean vflag = true;
		String actualText = "";
		try {
			if (locator.getTagName().equals("select")) {
				Select seleObj = new Select(locator);
				actualText = seleObj.getFirstSelectedOption().getText().trim();
				vflag = compareText(actualText, strVText);
			} else {
				WebWaitHelper.waitForElementPresence(locator);
				actualText = locator.getText();
				if (actualText == null || actualText.isEmpty()) {
					actualText = locator.getAttribute("innerText");
					if (actualText == null || actualText.isEmpty()) {
						actualText = locator.getAttribute("value");
						vflag = compareText(actualText, strVText);
					} else {
						vflag = compareText(actualText, strVText);
					}
				} else {
					vflag = compareText(actualText, strVText);
				}
			}
			LOGGER.info("Text verified successfully for the locator: " + locator);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while trying to verify the text " + "Exception :" + e);
			Assert.fail("Text verified successfully for the locator: " + locator);
		}
		return vflag;
	}

	public static boolean compareText(String strActualText, String strCompText) {
		boolean compFlag = false;
		try {
			if (strActualText.equals(strCompText)) {
				compFlag = true;
			} else {
				compFlag = false;
				Assert.fail(strActualText + " doesnot match with " + strCompText);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while doing the compareText action " + "Exception :" + e);
			Assert.fail("Error occurred while doing the compareText action " + "Exception :" + e);
		}
		return compFlag;
	}

	public static boolean existText(WebElement locator) {
		boolean flag = true;
		try {
			WebWaitHelper.waitForElement(locator);
			if (locator.getAttribute("value").isEmpty()) {
				flag = false;
				Assert.fail("Text does not exists");
			} else {
				flag = true;
				LOGGER.info("Text exits " + locator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while trying to check whether the text exists " + "Exception :" + e);
			Assert.fail("Error occurred while trying to check whether the text exists " + "Exception :" + e);
		}
		return flag;
	}

	public static void clearText(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			locator.clear();
			LOGGER.info("Text cleared successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while trying to clear text " + "Exception :" + e);
			Assert.fail("Error occurred while trying to clear text " + "Exception :" + e);
		}
	}

	public static boolean objDisabled(WebElement locator) {
		boolean eFlag = false;
		try {
			if (!locator.isEnabled()) {
				eFlag = true;
				LOGGER.info("Object is disabled");
			} else {
				eFlag = false;
				LOGGER.info("Object is enabled");
			}
			LOGGER.info("objDisabled check completed successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while checking whether the object is disabled " + "Exception :" + e);
			Assert.fail("Error occurred while checking whether the object is disabled " + "Exception :" + e);

		}
		return eFlag;
	}

	public static String fetchPropertyVal(WebElement locator, String property) {
		String propValue = "";
		try {
			propValue = locator.getAttribute(property).toString();
			LOGGER.info(
					"Property value fetched  successfully for the locator: " + locator + "prop value :" + propValue);
			return propValue;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while fetching  the property value " + "Exception :" + e);
			Assert.fail("Error  while fetching  the property value " + "Exception :" + e);
		}
		return propValue;
	}

	public void verifyProperty(WebElement locator, String property, String expected) {
		String actual = "";
		try {
			actual = fetchPropertyVal(locator, property);
			LOGGER.info("Property value fetched successfully for the locator: " + locator);
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to verify the property " + "Exception :" + e);
			Assert.fail("Error while trying to verify the property " + "Exception :" + e);
		}
		if (!expected.equals(actual)) {
			LOGGER.info("property does not  matches" + "locator value: " + locator + " Expected value :" + expected
					+ "Actual value :" + actual);
			Assert.fail("Property does not matches");
		} else {
			LOGGER.info("Property matches" + "locator value: " + locator + " Expected value :" + expected
					+ "Actual value :" + actual);
		}
	}

	public static void doubleClick(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			action.doubleClick(locator).perform();
			LOGGER.info("Double click performed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing double click " + "Exception: " + e);
			Assert.fail("Error while performing double click " + "Exception: " + e);
		}
	}

	public static void rightClick(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			action.contextClick(locator).perform();
			LOGGER.info("Right click performed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing right click " + "Exception: " + e);
			Assert.fail("Error while performing right click " + "Exception: " + e);
		}
	}

	public static void objExists(WebElement locator) {
		try {
			By byEle = WebHandlers.webElementToBy(locator);
			WebWaitHelper.waitForPresence(byEle, WebWaitHelper.getElementTimeout());
			LOGGER.info("objExists check completed successfully, Object exists ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while checking whether the object exists " + "Exception :" + e);
			Assert.fail("Error occurred while checking whether the object exists " + "Exception :" + e);
		}
	}

	public static boolean chkboxIsChecked(WebElement locator) {
		WebWaitHelper.waitForElement(locator);
		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Checkbox is not checked");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the chkboxIsChecked action for the locator: " + locator + "Exception :"
					+ e);
			Assert.fail("Error while performing the chkboxIsChecked action for the locator: " + locator + "Exception :"
					+ e);
		}
		return eFlag;
	}

	public static boolean radiobtnNotChecked(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is checked");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radiobtnNotChecked action " + "Exception :" + e);
			Assert.fail("Error while performing radiobtnNotChecked action " + "Exception :" + e);
		}
		return eFlag;
	}

	public static boolean radioBtnIsSelected(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is not selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radioBtnIsSelected action " + "Exception :" + e);
			Assert.fail("Error while performing radioBtnIsSelected action " + "Exception :" + e);
		}
		return eFlag;
	}

	public static boolean radioBtnIsNotSelected(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is  selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
			Assert.fail("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
		}
		return eFlag;

	}

	public static HashMap<Integer, String> sltCtrlReadAllVal(WebElement locator) {
		HashMap<Integer, String> comboValuesMap = new HashMap<Integer, String>();
		try {
			WebWaitHelper.waitForElement(locator);
			comboValuesMap = new HashMap<Integer, String>();
			Select dropdown = new Select(locator);
			List<WebElement> dd = dropdown.getOptions();
			for (int j = 0; j < dd.size(); j++) {
				comboValuesMap.put(j, dd.get(j).getText());
			}
			System.out.println(comboValuesMap);
			LOGGER.info("The values are " + comboValuesMap);
			return comboValuesMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
					+ "Exception :" + e);
			Assert.fail("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
					+ "Exception :" + e);
		}
		return comboValuesMap;
	}

	public static String dropDownGetCurrentSelection(WebElement locator) {
		String cmbSelectedValue = "";
		try {
			WebWaitHelper.waitForElement(locator);
			Select seleObj = new Select(locator);
			cmbSelectedValue = seleObj.getFirstSelectedOption().getText().trim();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to get current selection of dropdown for the locator: " + locator
					+ "Exception :" + e);
			Assert.fail("Error while trying to get current selection of dropdown for the locator " + locator
					+ "Exception :" + e);
		}
		return cmbSelectedValue;

	}

	public static void dropDownSelectByIndex(WebElement locator, int index) {
		try {
			WebWaitHelper.waitForElement(locator);
			String cmbSelectedValue = "";
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			cmbSelectedValue = cmbOptions.get(index).getText();
			LOGGER.info("Selected value :" + cmbSelectedValue);
			System.out.println(cmbSelectedValue);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to select index from drop down  the locator: " + locator + "Exception :" + e);
			Assert.fail(
					"Error while trying to select index from drop down  the locator: " + locator + "Exception :" + e);
		}
	}

	public static boolean dropDownSetByVal(WebElement locator, String value) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			for (int i = 0; i <= cmbOptions.size(); i++) {
				if (value.equalsIgnoreCase(cmbOptions.get(i).getText())) {
					dropdown.selectByIndex(i);
					flag = true;
					break;
				}
			}
			LOGGER.info(" The action dropDownSetByVal completed successfully ");
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to set  drop down by value for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error while trying to set drop down by value for the locator:" + locator + "Exception :" + e);
		}
		return flag;
	}

	public static boolean dropDownSetByIndex(WebElement locator, int index) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			for (int i = 0; i <= cmbOptions.size(); i++) {
				if (!cmbOptions.get(i).getText().isEmpty()) {
					dropdown.selectByIndex(index);
					flag = true;
					break;
				}
			}
			LOGGER.info(" The action dropDownSetByIndex completed successfully ");
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to set drop down by index for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error while trying to set drop down by index for the locator:" + locator + "Exception :" + e);
		}
		return flag;
	}

	public void switchToFrame(WebElement locator) {
		String actual = "";
		try {
			actual = String.valueOf(locator.isDisplayed());
			BrowserDriver.getDriver().switchTo().frame(locator);
			if (actual == null)
				throw new WebAutomationException(locator, "Switch to Frame", "Frame not Found");
			actual = actual.trim().toLowerCase();
		} catch (WebAutomationException e) {
			// log.info(e.getStackTrace());
			e.printStackTrace();
			LOGGER.error("Tried switching to next frame, not found " + locator + "Exception: " + e);
			Assert.fail("Tried switching to next frame, not found " + locator + "Exception: " + e);
			// throw new WebAutomationException(locator, "Switch to Frame", "Frame not
			// Found");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to switch to next frame" + "Exception: " + e);
			Assert.fail("Error while trying to switch to next frame" + "Exception: " + e);
		}
	}

	public void swithBackFromFrame() {
		try {
			BrowserDriver.getDriver().switchTo().defaultContent();
			// getFrame().swithBackFromFrame();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform switch back from frame" + " Exception: " + e);
			Assert.fail("Error while trying to perform switch back from frame" + " Exception: " + e);
		}
	}

	public static boolean multiDeselectByText(WebElement locator, String options) {
		boolean flag = false;
		ArrayList<String> optionList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.deselectByVisibleText(optionList.get(i));
			}
			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multideselect by text " + " Exception: " + e);
			Assert.fail("Error while trying to perform multideselect by text " + " Exception: " + e);
		}
		return flag;
	}

	public static boolean multiDeselectAll(WebElement locator) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select multiple = new Select(locator);
			multiple.deselectAll();
			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multideselect" + " Exception: " + e);
			Assert.fail("Error while trying to perform multideselect" + " Exception: " + e);
		}
		return flag;
	}

	public static boolean multiSelectByText(WebElement locator, String options) {
		boolean flag = false;
		ArrayList<String> optionList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.selectByVisibleText(optionList.get(i));
			}
			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multiSelectByText" + " Exception: " + e);
			Assert.fail("Error while trying to perform multiSelectByText" + " Exception: " + e);
		}
		return flag;
	}

	public static boolean multiSelectByIndex(WebElement locator, String indexes) {
		boolean flag = false;
		ArrayList<String> indexList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			indexList = optionsSplit(indexes);
			ArrayList<Integer> intList = (ArrayList<Integer>) indexList.stream().map(Integer::parseInt)
					.collect(Collectors.toList());
			for (int i = 0; i < intList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.selectByIndex(intList.get(i));
			}

			flag = true;
			return flag;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
			Assert.fail("Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
		}
		return flag;
	}

	public static ArrayList<String> optionsSplit(String options) {
		ArrayList<String> tempOptionList = null;
		try {
			String[] items = options.split(":");
			tempOptionList = new ArrayList<String>(Arrays.asList(items));
			return tempOptionList;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing  action optionsSplit" + " Exception: " + e);
			Assert.fail("Error while performing  action optionsSplit" + " Exception: " + e);
		}
		return tempOptionList;
	}

	public static LinkedHashMap<String, Integer> getTblHeaderVal(WebElement locator) {
		LinkedHashMap<String, Integer> headermap = new LinkedHashMap<String, Integer>();
		try {
			WebWaitHelper.waitForElement(locator);
			WebElement mytableHead = locator.findElement(By.tagName("thead"));
			List<WebElement> rowsTable = mytableHead.findElements(By.tagName("tr"));
			for (int row = 0; row < rowsTable.size(); row++) {
				List<WebElement> colRow = rowsTable.get(row).findElements(By.tagName("th"));
				for (int column = 0; column < colRow.size(); column++) {
					headermap.put(colRow.get(column).getText(), column);
				}
			}
			LOGGER.info("getTblHeaderVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing getTblHeaderVal action " + "Exception :" + e);
			Assert.fail("Error  while performing getTblHeaderVal action " + "Exception :" + e);
		}
		return headermap;
	}

	public static LinkedHashMap<String, Integer> getTblBodyVal(WebElement locator) {
		LinkedHashMap<String, Integer> bodymap = new LinkedHashMap<String, Integer>();
		try {
			WebWaitHelper.waitForElement(locator);
			WebElement mytableBody = locator.findElement(By.tagName("tbody"));
			List<WebElement> rowsTable = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < rowsTable.size(); row++) {
				List<WebElement> colRow = rowsTable.get(row).findElements(By.tagName("td"));
				for (int column = 0; column < colRow.size(); column++) {
					bodymap.put(colRow.get(column).getText(), column);
				}
			}
			LOGGER.info("getTblBodyVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the getTblBodyVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getTblBodyVal action " + "Exception :" + e);
		}
		return bodymap;
	}

	public static String getTblThVal(WebElement locator, int rowIndex, int colIndex) {

		WebElement ele = null;
		WebElement tblEle = null;
		// WebElement rowEle = null;
		WebElement myTblHead = null;
		List<WebElement> myTblHeadRow = null;
		List<WebElement> myTblHeadRowCol = null;
		String tblCellValue = "";
		try {
			WebWaitHelper.waitForElement(locator);
			myTblHead = locator.findElement(By.tagName("thead"));
			myTblHeadRow = myTblHead.findElements(By.xpath("tr"));
			for (int row = 0; row < myTblHeadRow.size(); row++) {
				tblEle = myTblHeadRow.get(row);
				myTblHeadRowCol = tblEle.findElements(By.tagName("th"));
				// List<WebElement> hdrRowCol = rowEle.findElements(By.tagName("th"));
				ele = myTblHeadRowCol.get(colIndex);
				tblCellValue = ele.getText();
			}
			LOGGER.info("getTblThVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getTblThVal action " + "Exception :" + e);
			// Assert.fail("Error while performing the getTblThVal action " + "Exception :"
			// + e);
		}
		return tblCellValue;
	}

	public static String getIndexofVal(WebElement locator, String value) {
		// WebElement ele = null;
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		String indexVal = "";
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				for (int column = 0; column < mytableBodycol.size(); column++) {
					if (mytableBodycol.get(column).getText().equalsIgnoreCase(value)) {
						System.out.println("Index of " + value + "is (" + row + "," + column + ")");
						indexVal = row + "," + column;
						break;
					}
				}
			}
			LOGGER.info("getIndexofVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the getIndexofVal action " + "Exception :" + e);
			// Assert.fail("Error while performing the getIndexofVal action " + "Exception
			// :" + e);
		}
		return indexVal;
	}

	public static String getFirstIndexofVal(WebElement locator, String value) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		String indexVal = "";
		boolean flag = false;
		try {
			WebWaitHelper.waitForElementPresence(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				for (int column = 0; column < mytableBodycol.size(); column++) {
					if (mytableBodycol.get(column).getText().equalsIgnoreCase(value)) {
						row = row + 1;
						column = column + 1;
						LOGGER.info("Index of " + value + "is (" + row + "," + column + ")");
						indexVal = row + "," + column;
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the getFirstIndexofVal action " + "Exception :" + e);
			/*
			 * Assert.fail("Error while performing the getFirstIndexofVal action " +
			 * "Exception :" + e);
			 */
		}
		return indexVal;

	}

	public static LinkedHashMap<String, String> getColMapByHdrVal(WebElement locator, String colHeader) {
		LinkedHashMap<String, String> colMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> headerMap = new LinkedHashMap<String, Integer>();
		// WebElement ele = null;
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		try {
			WebWaitHelper.waitForElement(locator);
			headerMap = getTblHeaderVal(locator);
			int colNum = headerMap.get(colHeader);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				colMap.put(mytableBodycol.get(colNum).getText(), "Row Number is " + row);
			}
			return colMap;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
		}
		return colMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByIndxVal(WebElement locator, int rowIndex) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		// HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).size(); i++) {
				rowMap.put(mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
			Assert.fail("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByHdrVal(WebElement locator, String rowHeader) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			WebWaitHelper.waitForElement(locator);
			bodyMap = getTblBodyVal(locator);
			int rowNum = bodyMap.get(rowHeader);

			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowNum).findElements(By.tagName("td")).size(); i++) {
				rowMap.put(mytableBodyrow.get(rowNum).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getColMapByIndxVal(WebElement locator, int colIndex) {
		LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement mytableBodyrowCol = null;
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.size(); i++) {
				mytableBodyrowCol = mytableBodyrow.get(i).findElements(By.tagName("td")).get(colIndex);
				colMap.put(mytableBodyrowCol.getText() + i, colIndex);
				System.out.println(i);
			}
			return colMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
		}
		return colMap;
	}

	public static void uploadFile(String fileNameWithExtention) {
		try {
			String uploadFilePath = FileUtils.getFilePath("src\\main\\resources\\Upload\\" + fileNameWithExtention);
			String DriverPath = WebDriverPathUtil.getWiniumDriverPath();
			Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
			process.waitFor();
			process.destroy();
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("C:\\Windows\\System32\\openfiles.exe");
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(new File(DriverPath))
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			desktopDriver = new WiniumDriver(service, options);
			desktopDriver.findElementByName("File name:").sendKeys(uploadFilePath);
			desktopDriver.findElementByXPath("//*[@Name='Cancel']//preceding-sibling::*[@Name='Open']").click();
		} catch (Exception e) {
			LOGGER.error("Error while uploading file" + e);
			Assert.fail("Error while uploading file" + e);
		}
	}

	public static void divSpanListBox(WebElement locator, String value) {
		try {
			WebWaitHelper.waitForElement(locator);
			locator.click();
			List<WebElement> listSpan = locator.findElements(By.tagName("span"));
			for (WebElement span : listSpan) {
				if (span.getText().trim().equalsIgnoreCase(value)) {
					span.click();
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception :" + e);
			Assert.fail("Exception :" + e);
		}
	}

	public static void scrollToElement(WebElement locator) {
		By byEle = WebHandlers.webElementToBy(locator);
		WebWaitHelper.scrollToElement(byEle);
	}

	public static int getTblRowCount(WebElement locator) {
		WebWaitHelper.waitForElement(locator);
		// WebElement mytableHead = locator.findElement(By.tagName("thead"));
		List<WebElement> rowsTable = locator.findElements(By.tagName("tr"));
		return rowsTable.size();
	}

//************************************************************************

	public static void selectDropdownValue(WebElement locator, String value) {
		try {
			List<WebElement> dropDownList = locator.findElements(By.tagName("li"));
			for (int i = 0; i < dropDownList.size(); i++) {
				if (value.equalsIgnoreCase(dropDownList.get(i).getText())) {
					dropDownList.get(i).click();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the selectDropdownValue action " + "Exception :" + e);
			Assert.fail("Error  while performing the selectDropdownValue action " + "Exception :" + e);
		}
	}

	public static int getContactTblRowCount(WebElement locator) {
		WebWaitHelper.waitForElement(locator);
		By byEle = WebHandlers.webElementToBy(locator);
		List<WebElement> rowsTable = BrowserDriver.getDriver().findElements(byEle);
		return rowsTable.size();
	}

//************* changed methods to handle stale element

	public static void enterText(WebElement locator, String value) {
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForClear(locator);
				locator.clear();
				locator.sendKeys(new String[] { value });
				locator.sendKeys(Keys.TAB);
				LOGGER.info("Text entered successfully" + locator);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while entering text for the locator: " + locator + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while entering text for the locator: " + locator + "Exception :" + e);
				}

			}
		}
	}

	public static void click(WebElement locator) {
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElement(locator);
				locator.click();
				BrowserDriver.pageWait();
				LOGGER.info("Click action completed successfully for the locator: " + locator);
				break;
			} catch (Exception e) {

				LOGGER.error("Error while performing the click action for the locator: " + locator + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail(
							"Error while performing the click action for the locator: " + locator + "Exception :" + e);
				}
			}
		}
	}

	public static String getContactTblTdValue(WebElement locator, int rowIndex, int colIndex) {
		String tableData = "";
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				By byEle = WebHandlers.webElementToBy(locator);
				LOGGER.info("count value inside getContactTblTdValue  :" + count);
				List<WebElement> rowsTable = BrowserDriver.getDriver().findElements(byEle);
				List<WebElement> cols = rowsTable.get(rowIndex).findElements(By.tagName("span"));
				tableData = cols.get(colIndex).getText();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getTblTdVal action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the getTblTdVal action " + "Exception :" + e);
				}
			}
		}
		return tableData;
	}

	public static void selectTblCelChkbox(WebElement tbllocator, int rowIndex, int colIndex)
			throws InterruptedException {
		WebElement eleLoc = null;
		int loopCount = countValue();
		int count = 0;
		rowIndex += 1;
		colIndex += 1;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElementPresence(tbllocator);
				LOGGER.info("count value inside selectTblCelChkbox  :" + count);
				eleLoc = tbllocator.findElement(By.xpath("tr[" + rowIndex + "]//td[" + colIndex + "]/div/div/div"));
				eleLoc.click();
				break;
			} catch (Exception e) {
				LOGGER.error("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				}
			}
		}
	}

	public static void TblCelEleClick(WebElement tbllocator, String value, int chkColIndex, String eleXpath) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement eleRow = null;
		Boolean clicked = false;
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				String index = getFirstIndexofVal(tbllocator, value);
				String[] arrSplit = index.split(",");
				LOGGER.info("count value inside TblCelEleClick  :" + count);
				WebWaitHelper.waitForElement(tbllocator);
				mytableBody = tbllocator.findElement(By.tagName("tbody"));
				mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
				eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
				WebElement eleRowCol = eleRow.findElement(By.xpath("//td[" + chkColIndex + "]" + eleXpath));
				eleRowCol.click();
				clicked = true;
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while trying to click an element inside table" + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while trying to click an element inside table " + "Exception :" + e);
				}
			}
		}
	}

	public static void TblCelLinkClick(WebElement locator, String value) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				String index = getIndexofVal(locator, value);
				String[] arrSplit = index.split(",");
				WebWaitHelper.waitForElement(locator);
				LOGGER.info("count value inside TblCelLinkClick  :" + count);
				mytableBody = locator.findElement(By.tagName("tbody"));
				mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
				WebElement eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
				WebElement eleRowCol = eleRow.findElements(By.tagName("td")).get(Integer.parseInt(arrSplit[1]));
				WebElement ele = eleRowCol.findElement(By.tagName("a"));
				ele.click();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while trying to click a link inside table " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while trying to click a link inside table " + "Exception :" + e);
				}

			}
		}
	}

	public static void clickTblVal(WebElement locator, int rowIndex, int colIndex) {
		WebElement ele = null;
		WebElement rowele = null;
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElement(locator);
				LOGGER.info("count value inside clickTblVal  :" + count);
				List<WebElement> mytables = locator.findElements(By.tagName("tr"));
				rowele = mytables.get(rowIndex);
				List<WebElement> rowsTable = rowele.findElements(By.tagName("td"));
				ele = rowsTable.get(colIndex);
				ele.click();
				LOGGER.info("The clickTblVal action completed successfully");
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while performing the getTblTdVal action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error while performing the getTblTdVal action " + "Exception :" + e);
				}
			}
		}
	}

	public static String getTblTdVal(WebElement locator, int rowIndex, int colIndex) {
		WebElement ele = null;
		WebElement rowele = null;
		String tblCellValue = "";
		int loopCount = countValue();
		int count = 0;
		boolean valueObtained = false;
		while (count < loopCount) {
			try {
				count++;
				// WebWaitHelper.waitForElement(locator);
				List<WebElement> mytables = locator.findElements(By.tagName("tr"));
				rowele = mytables.get(rowIndex);
				List<WebElement> rowsTable = rowele.findElements(By.tagName("td"));
				ele = rowsTable.get(colIndex);
				LOGGER.info("count value inside getTblTdVal  :" + count);
				WebWaitHelper.waitForElement(locator);
				tblCellValue = ele.getText();
				valueObtained = true;
				LOGGER.info("The getTblTdVal action completed successfully");
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getTblTdVal action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the getTblTdVal action " + "Exception :" + e);
				}
			}
		}
		return tblCellValue;
	}

	public static void TblCelChkbox(WebElement tbllocator, int rowIndex, int colIndex) throws InterruptedException {
		WebElement eleLoc = null;
		int loopCount = countValue();
		int count = 0;
		rowIndex += 1;
		colIndex += 1;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElementPresence(tbllocator);
				LOGGER.info("count value inside TblCelChkbox  :" + count);
				eleLoc = tbllocator.findElement(By.xpath("tr[" + rowIndex + "]//td[" + colIndex + "]/div/div/div"));
				eleLoc.click();
				break;
			} catch (Exception e) {
				LOGGER.error("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				}
			}
		}
	}

	public static void TblCelChkboxClick(WebElement tbllocator, String value, int chkColIndex) {
		WebElement mytableBody = null;
		// List<WebElement> mytableBodyrow = null;
		// WebElement eleRow = null;
		String[] arrSplit = null;
		Boolean clicked = false;
		int loopCount = countValue();
		int count = 0;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElementPresence(tbllocator);
				String index = getFirstIndexofVal(tbllocator, value);
				arrSplit = index.split(",");
				LOGGER.info("count value inside TblCelChkboxClick" + count);
				mytableBody = tbllocator.findElement(By.tagName("tbody"));
				WebElement eleRowCol = mytableBody
						.findElement(By.xpath("tr[" + arrSplit[0] + "]//td[" + chkColIndex + "]//input"));
				eleRowCol.click();
				clicked = true;
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while trying to click a checkbox inside table" + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while trying to click a checkbox inside table " + "Exception :" + e);
				}
			}
		}
	}

	public static void clickTblLinkValue(WebElement tbllocator, int rowIndex, int colIndex) {
		WebElement eleLoc = null;
		int loopCount = countValue();
		int count = 0;
		rowIndex += 1;
		colIndex += 1;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElementPresence(tbllocator);
				LOGGER.info("count value inside clickTblLinkValue :" + count);
				eleLoc = tbllocator.findElement(By.xpath("tr[" + rowIndex + "]//td[" + colIndex + "]/div/a"));
				eleLoc.click();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the TblCelChkbox action " + "Exception :" + e);
				}

			}
		}
	}

	public static void clickGroupTblValue(WebElement tableLocator, int rowIndex, int colIndex) {
		int loopCount = countValue();
		int count = 0;
		rowIndex += 1;
		colIndex += 1;
		while (count < loopCount) {
			try {
				count++;
				WebWaitHelper.waitForElementPresence(tableLocator);
				LOGGER.info("count value inside clickGroupTblValue  :" + count);
				WebElement eleLoc = tableLocator
						.findElement(By.xpath("tr[" + rowIndex + "]//td[" + colIndex + "]/div/div/a[2]"));
				eleLoc.click();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error  while performing the clickGroupTblValue action " + "Exception :" + e);
				if (count == (loopCount - 1)) {
					Assert.fail("Error  while performing the clickGroupTblValue action " + "Exception :" + e);
				}
			}
		}
	}

	public static String getNPITblVal(WebElement locator, int rowIndex, int colIndex) {
		WebElement eleLoc;
		String eleLoc1 = null;
		rowIndex += 1;
		colIndex += 1;
		try {
			WebWaitHelper.waitForElementPresence(locator);
			eleLoc = locator.findElement(By.xpath("tr[" + rowIndex + "]/td[" + colIndex + "]/div"));
			eleLoc1 = eleLoc.getText();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the TblCelChkbox action " + "Exception :" + e);
		}
		return eleLoc1;
	}

	public static int countValue() {
		int loopCountMaxValue = 0;
		try {
			readPropertyFile();
			loopCountMaxValue = Integer.parseInt(configProp.readProp("webelement_retry_count"));
		} catch (Exception e) {
			LOGGER.error("Error while reading count value from property file");
		}
		return loopCountMaxValue;
	}

	public static void readPropertyFile() {
		configProp = new PropertyDriver();
		configProp.setPropFilePath("src\\main\\resources\\properties\\EpdsConfig.properties");
	}

	// added
	public static boolean isGreyColorGrid(WebElement locator, int rowIndex) {
		try {
			rowIndex += 1;
			WebElement searchTableRow = locator.findElement(By.xpath("tr[" + rowIndex + "]"));
			String colorClass = searchTableRow.getAttribute("class");

			if (colorClass.contains("colored"))
				return true;

		} catch (Exception e) {
			LOGGER.error("Error  while performing the GreyColorGrid action " + "Exception :" + e);
		}
		return false;
	}

	/*
	 * public static void searchText(WebElement locator, String value){ int
	 * loopCount = countValue(); int count = 0; while (count < loopCount) { try {
	 * count++; WebWaitHelper.waitForElementPresence(locator); locator.clear();
	 * locator.click(); setClipboardData(value); action.keyDown(locator,
	 * Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();
	 * LOGGER.info("Text entered successfully" + locator); break; } catch (Exception
	 * e) { e.printStackTrace();
	 * LOGGER.error("Error  while entering text for the locator: " + locator +
	 * "Exception :" + e); if (count == (loopCount - 1)) {
	 * Assert.fail("Error  while entering text for the locator: " + locator +
	 * "Exception :" + e); } } } }
	 * 
	 * 
	 * public static void setClipboardData(String string) { StringSelection
	 * stringSelection = new StringSelection(string);
	 * Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,
	 * null); }
	 */
}
