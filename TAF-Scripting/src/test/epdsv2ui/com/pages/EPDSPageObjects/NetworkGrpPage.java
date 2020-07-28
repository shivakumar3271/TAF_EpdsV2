package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beans.mdxjson.NetworkAffiliation;
import com.scripted.web.WebHandlers;
import com.utilities.epds.EpdsConstants;

public class NetworkGrpPage {

	public static Logger LOGGER = Logger.getLogger(NetworkGrpPage.class);

	WebDriver driver;

	@FindBy(xpath = "//*[@id=\"prvInfo-form:data_table_netAdded_data\"]")
	WebElement ntwrkDetailsTable;

	public NetworkGrpPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean searchNetworkDetails(NetworkAffiliation affiliation) {

		String expNtwrkSrcSystem = affiliation.getNetworkSourceSystem();
		String expGrpNtwrkId = affiliation.getNetworkID();
		String expGrpNtwrkEffDate = affiliation.getNetworkActive().getEffectiveDate();
		String expGrpNtwrkTermDate = affiliation.getNetworkActive().getTerminationDate();
		String expGrpNtwrkReasonCode = Objects.toString(affiliation.getNetworkActive().getTerminationReasonCode(), "");
		String terminationDate = EpdsConstants.TERMINATION_DATE;
		String srchStr = null;

		srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, 0, 0);
		if (!srchStr.equalsIgnoreCase("No records found.")) {
			int rowCount = WebHandlers.getTblRowCount(ntwrkDetailsTable);
			LOGGER.info("Network details table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, i, EpdsConstants.NTWRK_SRC_SYSTEM);
				if (srchStr.equalsIgnoreCase(expNtwrkSrcSystem)) {
					srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, i, EpdsConstants.NTWRK_GRP_NTWRK_ID);
					if (srchStr.equalsIgnoreCase(expGrpNtwrkId)) {
						srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, i, EpdsConstants.NTWRK_EFF_DATE);
						if (srchStr.equals(expGrpNtwrkEffDate)) {
							srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, i, EpdsConstants.NTWRK_TERM_DATE);
							if (srchStr.equals(expGrpNtwrkTermDate) && srchStr.equals(terminationDate)) {
								return true;
							} else {
								if (srchStr.equals(expGrpNtwrkTermDate)) {
									srchStr = WebHandlers.getTblTdVal(ntwrkDetailsTable, i,
											EpdsConstants.NTWRK_TERM_REASON_CODE);
									if (srchStr.equalsIgnoreCase(expGrpNtwrkReasonCode) && srchStr != null) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Network detail record is not found.");
		return false;
	}

}
