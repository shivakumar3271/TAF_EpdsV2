package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.AlternateIDs;
import com.beans.mdxjson.NpiDetail;
import com.utilities.mdxjson.ResultTuple;

public class AlternateIdValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AlternateIdValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateNpiDetails(LinkedHashMap<String, Object> recordHashMap, AlternateIDs alternateIds,
			AlternateIDs dummyAltIds) {

		this.recordHashMap = recordHashMap;

		String npiEligibilityType, npiType, npiValue, npiEffDate, npiTermDate, npiTermReasonCode;
		String expNpiRecord;
		Boolean npiDetailsFlag = false;

		npiEligibilityType = recordHashMap.get("alternateids_npidetails_npieligibilitytype").toString().trim();
		npiType = recordHashMap.get("alternateids_npidetails_npitype").toString().trim();
		npiValue = recordHashMap.get("alternateids_npidetails_npivalue").toString().trim();
		npiEffDate = recordHashMap.get("alternateids_npidetails_npiactive_effectivedate").toString().trim();
		npiTermDate = recordHashMap.get("alternateids_npidetails_npiactive_terminationdate").toString().trim();
		npiTermReasonCode = recordHashMap.get("alternateids_npidetails_npiactive_terminationreasoncode").toString()
				.trim();

		expNpiRecord = npiEligibilityType + " : " + npiType + " : " + npiValue + " : " + npiEffDate + " : "
				+ npiTermDate + " : " + npiTermReasonCode;

		List<NpiDetail> npiDetailsList = null;
		if (alternateIds != null)
			npiDetailsList = alternateIds.getNpiDetails();

		if (npiEligibilityType == emptyStr && npiType == emptyStr && npiValue == emptyStr && npiEffDate == emptyStr
				&& npiTermDate == emptyStr && npiTermReasonCode == emptyStr) {
			if (npiDetailsList != null) {
				resultList.add(new ResultTuple("Alt ID - NPI Details node", "non null", "null", FAILED,
						"NPI Details node is not empty"));
			} else {
				System.out.println("Ok no more Alt ID - NPI Details node validation");
			}
		} else {
			if (npiDetailsList == null) {
				resultList.add(new ResultTuple("Alt ID - NPI Details node", "null", expNpiRecord, FAILED,
						"Npi Details node is missing"));
			} else {
				int i = 0;
				for (NpiDetail npiDetail : npiDetailsList) {
					if (npiEligibilityType.equalsIgnoreCase(Objects.toString(npiDetail.getNpiEligibilityType(), ""))
							&& npiType.equalsIgnoreCase(Objects.toString(npiDetail.getNpiType(), ""))
							&& npiValue.equalsIgnoreCase(Objects.toString(npiDetail.getNpiValue(), ""))
							&& npiEffDate
									.equalsIgnoreCase(Objects.toString(npiDetail.getNpiActive().getEffectiveDate(), ""))
							&& npiTermDate.equalsIgnoreCase(
									Objects.toString(npiDetail.getNpiActive().getTerminationDate(), ""))
							&& npiTermReasonCode.equalsIgnoreCase(
									Objects.toString(npiDetail.getNpiActive().getTerminationReasonCode(), ""))) {

						npiDetailsFlag = true;
						NpiDetail dummyNpiDetail = dummyAltIds.getNpiDetails().get(i);
						setNpiDetail(dummyNpiDetail);
						break;
					}
					i++;
				}
				if (npiDetailsFlag == false)
					resultList.add(new ResultTuple("Alt ID - NPI Details Record", "", expNpiRecord, FAILED,
							"Npi Details record is missing"));
			}
		}
	}

	public void validateAltIdDetails(LinkedHashMap<String, Object> recordHashMap, AlternateIDs alternateIds,
			AlternateIDs dummyAltIds) {

		this.recordHashMap = recordHashMap;

		String altIdSource, altIdType, altIdValue, altIdEffDate, altIdTermDate, altIdTermReasonCode;
		String expAltidRecord;
		Boolean altIdDetailsFlag = false;

		altIdSource = recordHashMap.get("alternateids_altiddetails_altidsource").toString().trim();
		altIdType = recordHashMap.get("alternateids_altiddetails_altidtype").toString().trim();
		altIdValue = recordHashMap.get("alternateids_altiddetails_altidvalue").toString().trim();
		altIdEffDate = recordHashMap.get("alternateids_altiddetails_alternateidactive_effectivedate").toString().trim();
		altIdTermDate = recordHashMap.get("alternateids_altiddetails_alternateidactive_terminationdate").toString()
				.trim();
		altIdTermReasonCode = recordHashMap.get("alternateids_altiddetails_alternateidactive_terminationreasoncode")
				.toString().trim();

		expAltidRecord = altIdSource + " : " + altIdType + " : " + altIdValue + " : " + altIdEffDate + " : "
				+ altIdTermDate + " : " + altIdTermReasonCode;

		List<AltIdDetail> altIdDetailsList = null;
		if (alternateIds != null)
			altIdDetailsList = alternateIds.getAltIdDetails();

		if (altIdSource == emptyStr && altIdType == emptyStr && altIdValue == emptyStr && altIdEffDate == emptyStr
				&& altIdTermDate == emptyStr && altIdTermReasonCode == emptyStr) {
			if (altIdDetailsList != null) {
				resultList.add(new ResultTuple("AltId Details node", "non null", "null", FAILED,
						"AltId Details node is not empty"));
			} else {
				System.out.println("Ok no more AltId Details node validation");
			}
		} else {
			if (altIdDetailsList == null) {
				resultList.add(new ResultTuple("AltId Details node", "null", expAltidRecord, FAILED,
						"AltId Details node is missing"));
			} else {
				int i = 0;
				for (AltIdDetail altIdDet : altIdDetailsList) {
					if (altIdSource.equalsIgnoreCase(Objects.toString(altIdDet.getAltIDSource(), ""))
							&& altIdType.equalsIgnoreCase(Objects.toString(altIdDet.getAltIDType(), ""))
							&& altIdValue.equalsIgnoreCase(Objects.toString(altIdDet.getAltIDValue(), ""))
							&& altIdEffDate.equalsIgnoreCase(
									Objects.toString(altIdDet.getAlternateIDActive().getEffectiveDate(), ""))
							&& altIdTermDate.equalsIgnoreCase(
									Objects.toString(altIdDet.getAlternateIDActive().getTerminationDate(), ""))
							&& altIdTermReasonCode.equalsIgnoreCase(
									Objects.toString(altIdDet.getAlternateIDActive().getTerminationReasonCode(), ""))) {

						altIdDetailsFlag = true;
						AltIdDetail dummyAltIdDetail = dummyAltIds.getAltIdDetails().get(i);
						setAltIdDetail(dummyAltIdDetail);
						break;
					}
					i++;
				}
				if (altIdDetailsFlag == false)
					resultList.add(new ResultTuple("AltId Details record", "", expAltidRecord, FAILED,
							"AltId Details record is missing"));
			}
		}
	}

	public void setNpiDetail(NpiDetail dummyNpiDetail) {

		dummyNpiDetail.setNpiEligibilityType(TRUE);
		dummyNpiDetail.setNpiType(TRUE);
		dummyNpiDetail.setNpiValue(TRUE);
		dummyNpiDetail.getNpiActive().setEffectiveDate(TRUE);
		dummyNpiDetail.getNpiActive().setTerminationDate(TRUE);
		dummyNpiDetail.getNpiActive().setTerminationReasonCode(TRUE);
	}

	public void setAltIdDetail(AltIdDetail dummyAltIdDetail) {

		dummyAltIdDetail.setAltIDSource(TRUE);
		dummyAltIdDetail.setAltIDType(TRUE);
		dummyAltIdDetail.setAltIDValue(TRUE);
		dummyAltIdDetail.getAlternateIDActive().setEffectiveDate(TRUE);
		dummyAltIdDetail.getAlternateIDActive().setTerminationDate(TRUE);
		dummyAltIdDetail.getAlternateIDActive().setTerminationReasonCode(TRUE);
	}

}
