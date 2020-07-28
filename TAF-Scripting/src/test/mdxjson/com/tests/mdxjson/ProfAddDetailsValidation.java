package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.BundledPaymentProcess;
import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.EbpProgram;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.ProfileAdditionalDetails;
import com.beans.mdxjson.ProviderDistinctionDetail;
import com.utilities.mdxjson.ResultTuple;

public class ProfAddDetailsValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(ProfAddDetailsValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validatePdmIndicatorDetails(LinkedHashMap<String, Object> recordHashMap,
			ProfileAdditionalDetails profAddDetails, ProfileAdditionalDetails dummyProfAddDetails) {

		this.recordHashMap = recordHashMap;

		String pdmIndicatorVal, pdmIndEffDate, pdmIndTermDate, pdmIndTermReasonCode;
		String expPdmIndRecord;
		Boolean pdmIndFlag = false;

		pdmIndicatorVal = recordHashMap.get("profileadditionaldetails_pdmindicatordetails_pdmindicator").toString()
				.trim();
		pdmIndEffDate = recordHashMap
				.get("profileadditionaldetails_pdmindicatordetails_pdmindicatoractive_effectivedate").toString().trim();
		pdmIndTermDate = recordHashMap
				.get("profileadditionaldetails_pdmindicatordetails_pdmindicatoractive_terminationdate").toString()
				.trim();
		pdmIndTermReasonCode = recordHashMap
				.get("profileadditionaldetails_pdmindicatordetails_pdmindicatoractive_terminationreasoncode").toString()
				.trim();

		expPdmIndRecord = pdmIndicatorVal + " : " + pdmIndEffDate + " : " + pdmIndTermDate + " : "
				+ pdmIndTermReasonCode;

		List<PdmIndicatorDetail> pdmIndDetails = null;
		if (profAddDetails != null)
			pdmIndDetails = profAddDetails.getPdmIndicatorDetails();

		if (pdmIndicatorVal == emptyStr && pdmIndEffDate == emptyStr && pdmIndTermDate == emptyStr
				&& pdmIndEffDate == emptyStr) {
			if (pdmIndDetails != null) {
				resultList.add(new ResultTuple("Profile - PDMIndicator Details node", "non null", "null", FAILED,
						"Profile- PDMIndicator Details node is not empty"));
			} else {
				System.out.println("Ok no more Profile - PDMIndicator Details node validation");
			}
		} else {
			if (pdmIndDetails == null) {
				resultList.add(new ResultTuple("Profile - PDMIndicator Details node", "null", expPdmIndRecord, FAILED,
						"Profileadditionaldetails_pdmindicatordetails node is missing"));
			} else {
				int i = 0;
				for (PdmIndicatorDetail pdmIndicatorDetails : pdmIndDetails) {

					if (pdmIndicatorVal.equalsIgnoreCase(Objects.toString(pdmIndicatorDetails.getPdmIndicator(), ""))
							&& pdmIndEffDate.equalsIgnoreCase(Objects
									.toString(pdmIndicatorDetails.getPdmIndicatorActive().getEffectiveDate(), ""))
							&& pdmIndTermDate.equalsIgnoreCase(Objects
									.toString(pdmIndicatorDetails.getPdmIndicatorActive().getTerminationDate(), ""))
							&& pdmIndTermReasonCode.equalsIgnoreCase(Objects.toString(
									pdmIndicatorDetails.getPdmIndicatorActive().getTerminationReasonCode(), ""))) {

						pdmIndFlag = true;
						List<PdmIndicatorDetail> dummyPdmIndDetails = dummyProfAddDetails.getPdmIndicatorDetails();
						setPdmIndicator(dummyPdmIndDetails.get(i));
						break;
					}
					i++;
				}
				if (pdmIndFlag == false)
					resultList.add(new ResultTuple("Profile - PDMIndicator Details record", "", expPdmIndRecord, FAILED,
							"Profile- PDMIndicator Details record is missing"));
			}
		}
	}

	public void validateCredentialingDetails(LinkedHashMap<String, Object> recordHashMap,
			ProfileAdditionalDetails profAddDetails, ProfileAdditionalDetails dummyProfAddDetails) {

		this.recordHashMap = recordHashMap;

		String credentialingSource, credentialingStatus, credentialingEffDate, credentialingTermDate,
				credentialingTermReasonCode;
		String expCredentialingRecord;
		Boolean credentialingFlag = false;

		credentialingSource = recordHashMap.get("profileadditionaldetails_credentialingdetails_credentialingsource")
				.toString().trim();
		credentialingStatus = recordHashMap.get("profileadditionaldetails_credentialingdetails_credentialingstatus")
				.toString().trim();
		credentialingEffDate = recordHashMap
				.get("profileadditionaldetails_credentialingdetails_credentialingactive_effectivedate").toString()
				.trim();
		credentialingTermDate = recordHashMap
				.get("profileadditionaldetails_credentialingdetails_credentialingactive_terminationdate").toString()
				.trim();
		credentialingTermReasonCode = recordHashMap
				.get("profileadditionaldetails_credentialingdetails_credentialingactive_terminationreasoncode")
				.toString().trim();

		expCredentialingRecord = credentialingSource + " : " + credentialingStatus + " : " + credentialingEffDate
				+ " : " + credentialingTermDate + " : " + credentialingTermReasonCode;

		List<CredentialingDetail> credentialingDetails = null;
		if (profAddDetails != null)
			credentialingDetails = profAddDetails.getCredentialingDetails();

		if (credentialingSource == emptyStr && credentialingStatus == emptyStr && credentialingEffDate == emptyStr
				&& credentialingTermDate == emptyStr && credentialingTermReasonCode == emptyStr) {
			if (credentialingDetails != null) {
				resultList.add(new ResultTuple("Credentialing Details node", "non null", "null", FAILED,
						"Credentialing Details node is not empty"));
			} else {
				System.out.println("Ok no more Credentialing Details node validation");
			}
		} else {
			if (credentialingDetails == null) {
				resultList.add(new ResultTuple("Credentialing Details node", "null", expCredentialingRecord, FAILED,
						"Credentialing Details node is missing"));
			} else {
				int i = 0;
				for (CredentialingDetail credentialingDetail : credentialingDetails) {
					if (credentialingSource
							.equalsIgnoreCase(Objects.toString(credentialingDetail.getCredentialingSource(), ""))
							&& credentialingStatus.equalsIgnoreCase(
									Objects.toString(credentialingDetail.getCredentialingStatus(), ""))
							&& credentialingEffDate.equalsIgnoreCase(Objects
									.toString(credentialingDetail.getCredentialingActive().getEffectiveDate(), ""))
							&& credentialingTermDate.equalsIgnoreCase(Objects
									.toString(credentialingDetail.getCredentialingActive().getTerminationDate(), ""))
							&& credentialingTermReasonCode.equalsIgnoreCase(Objects.toString(
									credentialingDetail.getCredentialingActive().getTerminationReasonCode(), ""))) {

						credentialingFlag = true;
						List<CredentialingDetail> dummyCredDetails = dummyProfAddDetails.getCredentialingDetails();
						setCredentialingDetail(dummyCredDetails.get(i));
						break;
					}
					i++;
				}
				if (credentialingFlag == false)
					resultList.add(new ResultTuple("Credentialing Details record", "", expCredentialingRecord, FAILED,
							"Credentialing Details record is missing"));
			}
		}
	}

	public void validateProviderDistinctDetails(LinkedHashMap<String, Object> recordHashMap,
			ProfileAdditionalDetails profAddDetails, ProfileAdditionalDetails dummyProfAddDetails) {

		this.recordHashMap = recordHashMap;

		String provDistinctCode, provDistinctEffDate, provDistinctTermDate, provDistinctTermReasonCode;
		String expProvDistinctRecord;
		Boolean provDistinctFlag = false;

		provDistinctCode = recordHashMap
				.get("profileadditionaldetails_providerdistinctiondetails_providerdistinctioncode").toString().trim();
		provDistinctEffDate = recordHashMap.get(
				"profileadditionaldetails_providerdistinctiondetails_providerdistinctiondetailsactive_effectivedate")
				.toString().trim();
		provDistinctTermDate = recordHashMap.get(
				"profileadditionaldetails_providerdistinctiondetails_providerdistinctiondetailsactive_terminationdate")
				.toString().trim();
		provDistinctTermReasonCode = recordHashMap.get(
				"profileadditionaldetails_providerdistinctiondetails_providerdistinctiondetailsactive_terminationreasoncode")
				.toString().trim();

		expProvDistinctRecord = provDistinctCode + " : " + provDistinctEffDate + " : " + provDistinctTermDate + " : "
				+ provDistinctTermReasonCode;

		List<ProviderDistinctionDetail> provDistinctDetails = null;
		if (profAddDetails != null)
			provDistinctDetails = profAddDetails.getProviderDistinctionDetails();

		if (provDistinctCode == emptyStr && provDistinctEffDate == emptyStr && provDistinctTermDate == emptyStr
				&& provDistinctTermReasonCode == emptyStr) {
			if (provDistinctDetails != null) {
				resultList.add(new ResultTuple("Profile - Provider Distinction Details node", "non null", "null",
						FAILED, "Profile - Provider Distinction Details node is not empty"));
			} else {
				System.out.println("Ok no more Profile - Provider Distinction Details node validation");
			}
		} else {
			if (provDistinctDetails == null) {
				resultList.add(new ResultTuple("Profile - Provider Distinction Details node", "null",
						expProvDistinctRecord, FAILED, "Profile - Provider Distinction Details node is missing"));
			} else {
				int i = 0;
				for (ProviderDistinctionDetail provDistinctDetail : provDistinctDetails) {
					if (provDistinctCode
							.equalsIgnoreCase(Objects.toString(provDistinctDetail.getProviderDistinctionCode(), ""))
							&& provDistinctEffDate.equalsIgnoreCase(Objects.toString(
									provDistinctDetail.getProviderDistinctionDetailsActive().getEffectiveDate(), ""))
							&& provDistinctTermDate.equalsIgnoreCase(Objects.toString(
									provDistinctDetail.getProviderDistinctionDetailsActive().getTerminationDate(), ""))
							&& provDistinctTermReasonCode.equalsIgnoreCase(Objects.toString(
									provDistinctDetail.getProviderDistinctionDetailsActive().getTerminationReasonCode(),
									""))) {

						provDistinctFlag = true;
						List<ProviderDistinctionDetail> dummyProvDistDetails = dummyProfAddDetails
								.getProviderDistinctionDetails();
						setProviderDistinctionDetail(dummyProvDistDetails.get(i));
						break;
					}
					i++;
				}
				if (provDistinctFlag == false)
					resultList.add(new ResultTuple("Profile - Provider Distinction Details record", "",
							expProvDistinctRecord, FAILED, "Profile - Provider Distinction Details record is missing"));
			}
		}
	}

	public void validateEbpPrograms(LinkedHashMap<String, Object> recordHashMap,
			ProfileAdditionalDetails profAddDetails, ProfileAdditionalDetails dummyProfAddDetails) {

		this.recordHashMap = recordHashMap;

		String ebpProgramId, ebpContractStateCode, ebpEffDate, ebpTermDate, ebpTermReasonCode;
		String expEbpRecord;
		Boolean ebpProgramFlag = false;

		ebpProgramId = recordHashMap.get("profileadditionaldetails_ebpprograms_ebpprogramid").toString().trim();
		ebpContractStateCode = recordHashMap.get("profileadditionaldetails_ebpprograms_ebpcontractstatecode").toString()
				.trim();
		ebpEffDate = recordHashMap.get("profileadditionaldetails_ebpprograms_ebpprogramsactive_effectivedate")
				.toString().trim();
		ebpTermDate = recordHashMap.get("profileadditionaldetails_ebpprograms_ebpprogramsactive_terminationdate")
				.toString().trim();
		ebpTermReasonCode = recordHashMap
				.get("profileadditionaldetails_ebpprograms_ebpprogramsactive_terminationreasoncode").toString().trim();

		expEbpRecord = ebpProgramId + " : " + ebpContractStateCode + " : " + ebpEffDate + " : " + ebpTermDate + " : "
				+ ebpTermReasonCode;

		List<EbpProgram> ebpProgramsList = null;
		if (profAddDetails != null)
			ebpProgramsList = profAddDetails.getEbpPrograms();

		if (ebpProgramId == emptyStr && ebpContractStateCode == emptyStr && ebpEffDate == emptyStr
				&& ebpTermDate == emptyStr && ebpTermReasonCode == emptyStr) {
			if (ebpProgramsList != null) {
				resultList.add(new ResultTuple("EBP Programs node", "non null", "null", FAILED,
						"EBP Programs node is not empty"));
			} else {
				System.out.println("Ok no more EBP Programs node validation");
			}
		} else {
			if (ebpProgramsList == null) {
				resultList.add(new ResultTuple("EBP Programs node", "null", expEbpRecord, FAILED,
						"EBP Programs node is missing"));
			} else {
				int i = 0;
				for (EbpProgram ebpProgram : ebpProgramsList) {

					if (ebpProgramId.equalsIgnoreCase(Objects.toString(ebpProgram.getEbpProgramID(), ""))
							&& ebpContractStateCode
									.equalsIgnoreCase(Objects.toString(ebpProgram.getEbpContractStateCode(), ""))
							&& ebpEffDate.equalsIgnoreCase(
									Objects.toString(ebpProgram.getEbpProgramsActive().getEffectiveDate(), ""))
							&& ebpTermDate.equalsIgnoreCase(
									Objects.toString(ebpProgram.getEbpProgramsActive().getTerminationDate(), ""))
							&& ebpTermReasonCode.equalsIgnoreCase(Objects
									.toString(ebpProgram.getEbpProgramsActive().getTerminationReasonCode(), ""))) {

						ebpProgramFlag = true;
						List<EbpProgram> dummyEbpPrgms = dummyProfAddDetails.getEbpPrograms();
						setEbpProgram(dummyEbpPrgms.get(i));
						break;
					}
					i++;
				}
				if (ebpProgramFlag == false)
					resultList.add(new ResultTuple("EBP Programs record", "", expEbpRecord, FAILED,
							"EBP Programs record is missing"));
			}
		}
	}

	public void validateBundledPaymentProcess(LinkedHashMap<String, Object> recordHashMap,
			ProfileAdditionalDetails profAddDetails, ProfileAdditionalDetails dummyProfAddDetails) {

		this.recordHashMap = recordHashMap;

		String bpProcessMode, bpProcessEffDate, bpProcessTermDate, bpProcessTermReasonCode;
		String expBpProcessRecord;
		Boolean bpProcessFlag = false;

		bpProcessMode = recordHashMap.get("profileadditionaldetails_bundledpaymentprocesses_bundledpaymentprocessmode")
				.toString().trim();
		bpProcessEffDate = recordHashMap
				.get("profileadditionaldetails_bundledpaymentprocesses_bundledpaymentprocessactive_effectivedate")
				.toString().trim();
		bpProcessTermDate = recordHashMap
				.get("profileadditionaldetails_bundledpaymentprocesses_bundledpaymentprocessactive_terminationdate")
				.toString().trim();
		bpProcessTermReasonCode = recordHashMap.get(
				"profileadditionaldetails_bundledpaymentprocesses_bundledpaymentprocessactive_terminationreasoncode")
				.toString().trim();

		expBpProcessRecord = bpProcessMode + " : " + bpProcessEffDate + " : " + bpProcessTermDate + " : "
				+ bpProcessTermReasonCode;

		List<BundledPaymentProcess> bpProcessList = null;
		if (profAddDetails != null)
			bpProcessList = profAddDetails.getBundledPaymentProcesses();

		if (bpProcessMode == emptyStr && bpProcessEffDate == emptyStr && bpProcessTermDate == emptyStr
				&& bpProcessTermReasonCode == emptyStr) {
			if (bpProcessList != null) {
				resultList.add(new ResultTuple("Bundled Payment Process node", "non null", "null", FAILED,
						"Bundled Payment Process node is not empty"));
			} else {
				System.out.println("Ok no more Bundled Payment Process node validation");
			}
		} else {
			if (bpProcessList == null) {
				resultList.add(new ResultTuple("Bundled Payment Process node", "null", expBpProcessRecord, FAILED,
						"Bundled Payment Process node is missing"));
			} else {
				int i = 0;
				for (BundledPaymentProcess bpProcess : bpProcessList) {
					if (bpProcessMode.equalsIgnoreCase(Objects.toString(bpProcess.getBundledPaymentProcessMode(), ""))
							&& bpProcessEffDate.equalsIgnoreCase(
									Objects.toString(bpProcess.getBundledPaymentProcessActive().getEffectiveDate(), ""))
							&& bpProcessTermDate.equalsIgnoreCase(Objects
									.toString(bpProcess.getBundledPaymentProcessActive().getTerminationDate(), ""))
							&& bpProcessTermReasonCode.equalsIgnoreCase(Objects.toString(
									bpProcess.getBundledPaymentProcessActive().getTerminationReasonCode(), ""))) {

						bpProcessFlag = true;
						List<BundledPaymentProcess> dummyBpProcessList = dummyProfAddDetails
								.getBundledPaymentProcesses();
						setBundledPaymentProcess(dummyBpProcessList.get(i));
						break;
					}
					i++;
				}
				if (bpProcessFlag == false)
					resultList.add(new ResultTuple("Bundled Payment Process record", "", expBpProcessRecord, FAILED,
							"Bundled Payment Process record is missing"));
			}
		}
	}

	public void setPdmIndicator(PdmIndicatorDetail dummyPdmInd) {

		dummyPdmInd.setPdmIndicator(TRUE);
		dummyPdmInd.getPdmIndicatorActive().setEffectiveDate(TRUE);
		dummyPdmInd.getPdmIndicatorActive().setTerminationDate(TRUE);
		dummyPdmInd.getPdmIndicatorActive().setTerminationReasonCode(TRUE);
	}

	public void setCredentialingDetail(CredentialingDetail dummyCredDetail) {

		dummyCredDetail.setCredentialingSource(TRUE);
		dummyCredDetail.setCredentialingStatus(TRUE);
		dummyCredDetail.getCredentialingActive().setEffectiveDate(TRUE);
		dummyCredDetail.getCredentialingActive().setTerminationDate(TRUE);
		dummyCredDetail.getCredentialingActive().setTerminationReasonCode(TRUE);
	}

	public void setProviderDistinctionDetail(ProviderDistinctionDetail dummyProvDistDetail) {

		dummyProvDistDetail.setProviderDistinctionCode(TRUE);
		dummyProvDistDetail.getProviderDistinctionDetailsActive().setEffectiveDate(TRUE);
		dummyProvDistDetail.getProviderDistinctionDetailsActive().setTerminationDate(TRUE);
		dummyProvDistDetail.getProviderDistinctionDetailsActive().setTerminationReasonCode(TRUE);
	}

	public void setEbpProgram(EbpProgram dummyEbpPrgm) {

		dummyEbpPrgm.setEbpProgramID(TRUE);
		dummyEbpPrgm.setEbpContractStateCode(TRUE);
		dummyEbpPrgm.getEbpProgramsActive().setEffectiveDate(TRUE);
		dummyEbpPrgm.getEbpProgramsActive().setTerminationDate(TRUE);
		dummyEbpPrgm.getEbpProgramsActive().setTerminationReasonCode(TRUE);
	}

	public void setBundledPaymentProcess(BundledPaymentProcess dummyBpProcess) {

		dummyBpProcess.setBundledPaymentProcessMode(TRUE);
		dummyBpProcess.getBundledPaymentProcessActive().setEffectiveDate(TRUE);
		dummyBpProcess.getBundledPaymentProcessActive().setTerminationDate(TRUE);
		dummyBpProcess.getBundledPaymentProcessActive().setTerminationReasonCode(TRUE);
	}

}
