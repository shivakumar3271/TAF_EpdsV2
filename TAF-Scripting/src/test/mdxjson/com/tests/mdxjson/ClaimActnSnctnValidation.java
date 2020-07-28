package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.ClaimActionSanction;
import com.utilities.mdxjson.ResultTuple;

public class ClaimActnSnctnValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(ClaimActnSnctnValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateClaimAction(LinkedHashMap<String, Object> recordHashMap,
			List<ClaimActionSanction> claimActionList, List<ClaimActionSanction> dummyClaimActnList) {

		this.recordHashMap = recordHashMap;

		String claimType, claimValue, claimCriteria, lowRange, highRange, effectiveDate, terminationDate,
				termReasonCode;
		String expClaimActnRecord;
		Boolean claimActionFlag = false;

		claimType = recordHashMap.get("claimactionsanctions_claimactionsanctiontype").toString().trim();
		claimValue = recordHashMap.get("claimactionsanctions_claimactionsanctionvalue").toString().trim();
		claimCriteria = recordHashMap.get("claimactionsanctions_claimactionsanctioncriteria").toString().trim();
		lowRange = recordHashMap.get("claimactionsanctions_lowrange").toString().trim();
		highRange = recordHashMap.get("claimactionsanctions_highrange").toString().trim();
		effectiveDate = recordHashMap.get("claimactionsanctions_claimactionsanctionactive_effectivedate").toString()
				.trim();
		terminationDate = recordHashMap.get("claimactionsanctions_claimactionsanctionactive_terminationdate").toString()
				.trim();
		termReasonCode = recordHashMap.get("claimactionsanctions_claimactionsanctionactive_terminationreasoncode")
				.toString().trim();

		expClaimActnRecord = claimType + " : " + claimValue + " : " + claimCriteria + " : " + lowRange + " : "
				+ highRange + " : " + effectiveDate + " : " + terminationDate + " : " + termReasonCode;

		if (claimType == emptyStr && claimValue == emptyStr && claimCriteria == emptyStr && lowRange == emptyStr
				&& highRange == emptyStr && effectiveDate == emptyStr && terminationDate == emptyStr
				&& termReasonCode == emptyStr) {
			if (claimActionList != null) {
				resultList.add(new ResultTuple("Claim Action Sanction node", "non null", "null", FAILED,
						"Claim Action Sanction node is not empty"));
			} else {
				System.out.println("Ok no more Claim Action Sanction node validation");
			}
		} else {
			if (claimActionList == null) {
				resultList.add(new ResultTuple("Claim Action Sanction node", "null", expClaimActnRecord, FAILED,
						"Claim Action Sanction node is missing"));
			} else {
				int i = 0;
				for (ClaimActionSanction cliamAction : claimActionList) {
					if (claimType.equalsIgnoreCase(Objects.toString(cliamAction.getClaimActionSanctionType(), ""))
							&& claimValue
									.equalsIgnoreCase(Objects.toString(cliamAction.getClaimActionSanctionValue(), ""))
							&& claimCriteria.equalsIgnoreCase(
									Objects.toString(cliamAction.getClaimActionSanctionCriteria(), ""))
							&& lowRange.equalsIgnoreCase(Objects.toString(cliamAction.getLowRange(), ""))
							&& highRange.equalsIgnoreCase(Objects.toString(cliamAction.getHighRange(), ""))
							&& effectiveDate.equalsIgnoreCase(
									Objects.toString(cliamAction.getClaimActionSanctionActive().getEffectiveDate(), ""))
							&& terminationDate.equalsIgnoreCase(Objects
									.toString(cliamAction.getClaimActionSanctionActive().getTerminationDate(), ""))
							&& termReasonCode.equalsIgnoreCase(Objects.toString(
									cliamAction.getClaimActionSanctionActive().getTerminationReasonCode(), ""))) {

						claimActionFlag = true;
						ClaimActionSanction dummyClaimActn = dummyClaimActnList.get(i);
						setClaimActionSanction(dummyClaimActn);
						break;
					}
					i++;
				}
				if (claimActionFlag == false)
					resultList.add(new ResultTuple("Claim Action Sanction record", "", expClaimActnRecord, FAILED,
							"Claim Action Sanction record is missing"));
			}
		}
	}

	public void setClaimActionSanction(ClaimActionSanction dummyClaimAction) {

		dummyClaimAction.setClaimActionSanctionType(TRUE);
		dummyClaimAction.setClaimActionSanctionValue(TRUE);
		dummyClaimAction.setClaimActionSanctionCriteria(TRUE);
		dummyClaimAction.setLowRange(TRUE);
		dummyClaimAction.setHighRange(TRUE);
		dummyClaimAction.getClaimActionSanctionActive().setEffectiveDate(TRUE);
		dummyClaimAction.getClaimActionSanctionActive().setTerminationDate(TRUE);
		dummyClaimAction.getClaimActionSanctionActive().setTerminationReasonCode(TRUE);
	}

}
