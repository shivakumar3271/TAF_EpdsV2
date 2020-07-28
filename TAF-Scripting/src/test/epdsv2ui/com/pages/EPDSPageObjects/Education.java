package com.pages.EPDSPageObjects;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beans.mdxjson.EducationDetail;
import com.scripted.web.WebHandlers;
import com.utilities.epds.EpdsConstants;

public class Education {

	public static Logger LOGGER = Logger.getLogger(Education.class);

	WebDriver driver;
	@FindBy(xpath = "//*[@id=\"prvInfo-form:dataTable_education_data\"]")
	WebElement educationDetailsTable;

	public Education(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean searchEducationDetails(EducationDetail educationDetails) {

		String eduTypeCode = Objects.toString(educationDetails.getEducationTypeCode(), "");
		String schoolName = Objects.toString(educationDetails.getSchoolName(),"");
		String degree = Objects.toString(educationDetails.getDegree(), "");
		String schoolGradYear = Objects.toString(educationDetails.getSchoolGraduationYear(), "");
		String startDate = Objects.toString(educationDetails.getStartDate(), "");
		String endDate = Objects.toString(educationDetails.getEndDate(), "");
		String srchStr = null;
		srchStr = WebHandlers.getTblTdVal(educationDetailsTable, 0, 0);
		if (!(srchStr.equalsIgnoreCase("No records found."))) {
			int rowCount = WebHandlers.getTblRowCount(educationDetailsTable);
			LOGGER.info("Education details table row count : " + rowCount);

			for (int i = 0; i < rowCount; i++) {
				if (!WebHandlers.isGreyColorGrid(educationDetailsTable, i)) {
					srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i, EpdsConstants.EDUCATION_DETAILS);
					if (srchStr.equalsIgnoreCase(eduTypeCode)) {
						srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i,
								EpdsConstants.EDUCATION_SCHOOL_NAME);
						if (srchStr.equalsIgnoreCase(schoolName)) {
							srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i, EpdsConstants.EDUCATION_DEGREE);
							if (srchStr.equalsIgnoreCase(degree)) {
								srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i,
										EpdsConstants.EDUCATION_SCHOOL_GRADUATION_YEAR);
								if (srchStr.equals(schoolGradYear)) {
									srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i,
											EpdsConstants.EDUCATION_START_DATE);
									if (srchStr.equals(startDate)) {
										srchStr = WebHandlers.getTblTdVal(educationDetailsTable, i,
												EpdsConstants.EDUCATION_END_DATE);
										if (srchStr.equals(endDate))
											return true;
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Matching Education detail record is not available.");
		return false;
	}

}
