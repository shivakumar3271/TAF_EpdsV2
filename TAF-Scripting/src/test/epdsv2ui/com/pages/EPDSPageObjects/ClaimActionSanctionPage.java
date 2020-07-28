package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beans.mdxjson.ClaimActionSanction;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;
import com.utilities.epds.EpdsConstants;

public class ClaimActionSanctionPage {

	public static Logger LOGGER = Logger.getLogger(ClaimActionSanctionPage.class);

	WebDriver driver;

	@FindBy(xpath = "//*[@id='contentAreaScrollDiv']/table[2]/tbody/tr/td/div/div[2]/div/table/tbody")
	WebElement claimActionTable;

	@FindBy(xpath = "//*[@id='prvInfo-form:tab0:globalFilter']")
	WebElement claimActionFilter;
	
	public ClaimActionSanctionPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean searchClaimActions(ClaimActionSanction claimDetails) throws InterruptedException {

		String claimType = claimDetails.getClaimActionSanctionType();
		String claimValue = claimDetails.getClaimActionSanctionValue();
		String claimCriteria = Objects.toString(claimDetails.getClaimActionSanctionCriteria(), "");
		String claimLowRange = Objects.toString(claimDetails.getLowRange(), "");
		String claimHighRange = Objects.toString(claimDetails.getHighRange(), "");
		String claimEffDate = claimDetails.getClaimActionSanctionActive().getEffectiveDate();
		String claimTermDate = claimDetails.getClaimActionSanctionActive().getTerminationDate();
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String claimTermReasonCode = Objects
				.toString(claimDetails.getClaimActionSanctionActive().getTerminationReasonCode(), "");

		WebWaitHelper.waitForElement(claimActionFilter);
		claimActionFilter.click();
		claimActionFilter.clear();
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		JavascriptExecutor runJS = ((JavascriptExecutor) driver);
		runJS.executeScript("document.getElementById('prvInfo-form:tab0:globalFilter').value='" + claimValue + "'");
		claimActionFilter.sendKeys(Keys.TAB);

		//WebHandlers.enterText(claimActionFilter, claimValue);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		String srchStr = WebHandlers.getTblTdVal(claimActionTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(claimActionTable);
			LOGGER.info("Claim Action table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_VALUE);
				if (srchStr.contains(claimValue)) {
					srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_TYPE);
					if (srchStr.contains(claimType)) {
						srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_REASON_CRITERIA);
						if (srchStr.contains(claimCriteria)) {
							srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_LOW_RANGE);
							if (srchStr.equals(claimLowRange)) {
								srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_HIGH_RANGE);
								if (srchStr.equals(claimHighRange)) {
									srchStr = WebHandlers.getTblTdVal(claimActionTable, i,
											EpdsConstants.CLAIM_EFF_DATE);
									if (srchStr.equals(claimEffDate)) {
										srchStr = WebHandlers.getTblTdVal(claimActionTable, i,
												EpdsConstants.CLAIM_TERM_DATE);
										if (srchStr.equals(claimTermDate) && srchStr.equals(terminationDate)) {
											return true;
										} else {
											if (srchStr.equals(claimTermDate)) {
												srchStr = WebHandlers.getTblTdVal(claimActionTable, i,
														EpdsConstants.CLAIM_TERM_REASON_CODE);
												if (srchStr.equalsIgnoreCase(claimTermReasonCode) && srchStr != null) {
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Claim Action record is not found.");
		return false;
	}

	public boolean searchEditClaimActions(ClaimActionSanction claimDetails) throws InterruptedException {

		String claimValue = claimDetails.getClaimActionSanctionValue();
		String claimEffDate = claimDetails.getClaimActionSanctionActive().getEffectiveDate();
		String claimTermDate = claimDetails.getClaimActionSanctionActive().getTerminationDate();
		String claimTermReasonCode = Objects.toString(claimDetails.getClaimActionSanctionActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;

		WebWaitHelper.waitForElement(claimActionFilter);
		claimActionFilter.click();
		claimActionFilter.clear();
		Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

		WebHandlers.enterText(claimActionFilter, claimValue);
		Thread.sleep(EpdsConstants.MEDIUM_THREAD_VALUE);

		String srchStr = WebHandlers.getTblTdVal(claimActionTable, 0, 0);
		if (!(srchStr.equals("No records found."))) {

			int rowCount = WebHandlers.getTblRowCount(claimActionTable);
			LOGGER.info("Claim Action table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_VALUE);
				if (srchStr.contains(claimValue)) {
					srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_EFF_DATE);
					if (srchStr.equals(claimEffDate)) {
						srchStr = WebHandlers.getTblTdVal(claimActionTable, i, EpdsConstants.CLAIM_TERM_DATE);
						if (srchStr.equals(claimTermDate) && srchStr.equals(terminationDate)) {
							return true;
						} else {
							if (srchStr.equals(claimTermDate)) {
								srchStr = WebHandlers.getTblTdVal(claimActionTable, i,
										EpdsConstants.CLAIM_TERM_REASON_CODE);
								if (srchStr.equalsIgnoreCase(claimTermReasonCode) && srchStr != null) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Claim Action record is not found.");
		return false;
	}
}
