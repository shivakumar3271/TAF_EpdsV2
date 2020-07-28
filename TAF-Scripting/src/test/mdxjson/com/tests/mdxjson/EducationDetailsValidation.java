package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.EducationDetail;
import com.utilities.mdxjson.ResultTuple;

public class EducationDetailsValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(EducationDetailsValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateEducationDetails(LinkedHashMap<String, Object> recordHashMap,
			List<EducationDetail> educationDetails, List<EducationDetail> dummyEduDetails) {

		this.recordHashMap = recordHashMap;

		String eduTypeCode, schoolName, degree, schoolGradYear, startDate, endDate;
		String expEduRecord;
		Boolean eduDetailsFlag = false;

		eduTypeCode = recordHashMap.get("educationdetails_educationtypecode").toString().trim();
		schoolName = recordHashMap.get("educationdetails_schoolname").toString().trim();
		degree = recordHashMap.get("educationdetails_degree").toString().trim();
		schoolGradYear = recordHashMap.get("educationdetails_schoolgraduationyear").toString().trim();
		startDate = recordHashMap.get("educationdetails_startdate").toString().trim();
		endDate = recordHashMap.get("educationdetails_enddate").toString().trim();

		expEduRecord = eduTypeCode + " : " + schoolName + " : " + degree + " : " + schoolGradYear + " : " + startDate
				+ " : " + endDate;

		if (eduTypeCode == emptyStr && schoolName == emptyStr && degree == emptyStr && schoolGradYear == emptyStr
				&& startDate == emptyStr && endDate == emptyStr) {
			if (educationDetails != null) {
				resultList.add(new ResultTuple("Education Details Node", "non null", "null", FAILED,
						"Education Details node is not empty"));
			} else {
				LOGGER.info("Ok no more Education Details node validation");
			}
		} else {
			if (educationDetails == null) {
				resultList.add(new ResultTuple("Education Details node", "null", expEduRecord, FAILED,
						"Education Details node is missing"));
			} else {
				int i = 0;
				for (EducationDetail eduDetail : educationDetails) {
					if (eduTypeCode.equalsIgnoreCase(Objects.toString(eduDetail.getEducationTypeCode(), ""))
							&& schoolName.equalsIgnoreCase(Objects.toString(eduDetail.getSchoolName(), ""))
							&& degree.equalsIgnoreCase(Objects.toString(eduDetail.getDegree(), ""))
							&& schoolGradYear
									.equalsIgnoreCase(Objects.toString(eduDetail.getSchoolGraduationYear(), ""))
							&& startDate.equalsIgnoreCase(Objects.toString(eduDetail.getStartDate(), ""))
							&& endDate.equalsIgnoreCase(Objects.toString(eduDetail.getEndDate(), ""))) {

						eduDetailsFlag = true;
						EducationDetail dummyEduDetail = dummyEduDetails.get(i);
						setEducationDetail(dummyEduDetail);
						break;
					}
					i++;
				}
				if (eduDetailsFlag == false)
					resultList.add(new ResultTuple("Education Details record", "", expEduRecord, FAILED,
							"Education Details record is missing"));
			}
		}
	}

	public void setEducationDetail(EducationDetail dummyEduDetail) {

		dummyEduDetail.setEducationTypeCode(TRUE);
		dummyEduDetail.setSchoolName(TRUE);
		dummyEduDetail.setDegree(TRUE);
		dummyEduDetail.setSchoolGraduationYear(TRUE);
		dummyEduDetail.setStartDate(TRUE);
		dummyEduDetail.setEndDate(TRUE);
	}

}
