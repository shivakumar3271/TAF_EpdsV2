package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.AcceptingPatientsInd;
import com.beans.mdxjson.Address;
import com.beans.mdxjson.AddressActive;
import com.beans.mdxjson.AddressAdditionalDetails;
import com.beans.mdxjson.AddressDetails;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AffliatedTaxID;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.PatientPreferences;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.TimelyFilingInd;
import com.utilities.mdxjson.ResultTuple;

public class AffiliationValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AffiliationValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateAffiliationDetails(LinkedHashMap<String, Object> recordHashMap,
			List<Affiliation> affiliationList, List<Affiliation> dummyAffilList) {

		this.recordHashMap = recordHashMap;

		String affilLegacyId, affilType, groupingCode, groupingtype, pcpId, affilEffDate, affilTermDate,
				affilTermReasonCode;
		String expAffilRecord;
		Boolean affiliationFlag = false;

		affilLegacyId = recordHashMap.get("affiliations_affiliatedlegacyid").toString().trim();
		affilType = recordHashMap.get("affiliations_affiliationtype").toString().trim();
		groupingCode = recordHashMap.get("affiliations_groupingcode").toString().trim();
		groupingtype = recordHashMap.get("affiliations_groupingtype").toString().trim();
		pcpId = recordHashMap.get("affiliations_pcpid").toString().trim();
		affilEffDate = recordHashMap.get("affiliations_affiliationactive_effectivedate").toString().trim();
		affilTermDate = recordHashMap.get("affiliations_affiliationactive_terminationdate").toString().trim();
		affilTermReasonCode = recordHashMap.get("affiliations_affiliationactive_terminationreasoncode").toString()
				.trim();

		expAffilRecord = affilLegacyId + " : " + affilType + " : " + groupingCode + " : " + groupingtype + " : " + pcpId
				+ " : " + affilEffDate + " : " + affilTermDate + " : " + affilTermReasonCode;

		if (affilLegacyId == emptyStr && affilType == emptyStr && groupingCode == emptyStr && groupingtype == emptyStr
				&& pcpId == emptyStr && affilEffDate == emptyStr && affilTermDate == emptyStr
				&& affilTermReasonCode == emptyStr) {
			if (affiliationList != null) {
				resultList.add(new ResultTuple("Affiliation node", "non null", "null", FAILED,
						"Affiliation node is not empty"));
			} else {
				System.out.println("Ok no more Affiliation node validation");
			}
		} else {
			if (affiliationList == null) {
				resultList.add(new ResultTuple("Affiliation node", "null", expAffilRecord, FAILED,
						"Affiliation node is missing"));
			} else {
				int i = 0;
				for (Affiliation affiliation : affiliationList) {
					if (affilLegacyId.equalsIgnoreCase(Objects.toString(affiliation.getAffiliatedLegacyID(), ""))
							&& affilType.equalsIgnoreCase(Objects.toString(affiliation.getAffiliationType(), ""))
							&& groupingCode.equalsIgnoreCase(Objects.toString(affiliation.getGroupingCode(), ""))
							&& groupingtype.equalsIgnoreCase(Objects.toString(affiliation.getGroupingType(), ""))
							&& pcpId.equalsIgnoreCase(Objects.toString(affiliation.getPcpID(), ""))
							&& affilEffDate.equalsIgnoreCase(
									Objects.toString(affiliation.getAffiliationActive().getEffectiveDate(), ""))
							&& affilTermDate.equalsIgnoreCase(
									Objects.toString(affiliation.getAffiliationActive().getTerminationDate(), ""))
							&& affilTermReasonCode.equalsIgnoreCase(Objects
									.toString(affiliation.getAffiliationActive().getTerminationReasonCode(), ""))) {

						affiliationFlag = true;
						Affiliation dummyAffil = dummyAffilList.get(i);
						setAffilDetail(dummyAffil);

						validateNpiDetails(affiliation.getNpiDetails(), affilLegacyId, dummyAffil.getNpiDetails());
						validateTaxIds(affiliation.getAffliatedTaxIDs(), affilLegacyId,
								dummyAffil.getAffliatedTaxIDs());
						// TODO: Clarification pending
//						validateAddresses(affiliation.getAddresses(), affilLegacyId);
						validateNtwrkAffil(affiliation.getNetworkAffiliations(), affilLegacyId,
								dummyAffil.getNetworkAffiliations());
						validateReimbursement(affiliation.getReimbursements(), affilLegacyId,
								dummyAffil.getReimbursements());

						break;
					}
					i++;
				}
				if (affiliationFlag == false)
					resultList.add(new ResultTuple("Affiliation record", "", expAffilRecord, FAILED,
							"Affiliation record is missing"));
			}
		}
	}

	public void validateNpiDetails(List<NpiDetail> npiDetails, String affilLegacyId, List<NpiDetail> dummyNpiDetails) {

		String npiEligibilityType, npiType, npiValue;
		String expNpiRecord;
		Boolean npiFlag = false;

		npiEligibilityType = recordHashMap.get("affiliations_npidetails_npieligibilitytype").toString().trim();
		npiType = recordHashMap.get("affiliations_npidetails_npitype").toString().trim();
		npiValue = recordHashMap.get("affiliations_npidetails_npivalue").toString().trim();

		expNpiRecord = npiEligibilityType + " : " + npiType + " : " + npiValue;

		if (npiEligibilityType == emptyStr && npiType == emptyStr && npiValue == emptyStr) {
			if (npiDetails != null) {
				resultList.add(new ResultTuple("Affiliation NPI Details node", "non null", "null", FAILED,
						"NPI Details node is not empty for Affiliation : " + affilLegacyId));
			} else {
				System.out.println("Ok no more Affiliation NPI Details node validation");
			}
		} else {
			if (npiDetails == null) {
				resultList.add(new ResultTuple("Affiliation NPI Details Node", "null", expNpiRecord, FAILED,
						"NPI Details node is missing for Affiliation : " + affilLegacyId));
			} else {
				int i = 0;
				for (NpiDetail npiValues : npiDetails) {
					if (npiEligibilityType.equalsIgnoreCase(Objects.toString(npiValues.getNpiEligibilityType(), ""))
							&& npiType.equalsIgnoreCase(Objects.toString(npiValues.getNpiType(), ""))
							&& npiValue.equalsIgnoreCase(Objects.toString(npiValues.getNpiValue(), ""))) {

						npiFlag = true;
						setNpiDetail(dummyNpiDetails.get(i));
						break;
					}
					i++;
				}
				if (npiFlag == false)
					resultList.add(new ResultTuple("Affiliation NPI Detail record", "", expNpiRecord, FAILED,
							"NPI Detail record is missing for Affiliation : " + affilLegacyId));
			}
		}
	}

	public void validateTaxIds(List<AffliatedTaxID> affiliatedTaxIds, String affilLegacyId,
			List<AffliatedTaxID> dummyAffilTaxIds) {

		String taxIdValue, taxIdIndicator, taxIdEffDate, taxIdTermDate, taxIdTermReasonCode;
		String expTaxIdRecord;
		Boolean taxIdFlag = false;

		taxIdValue = recordHashMap.get("affiliations_affiliatedtaxid_taxidvalue").toString().trim();
		taxIdIndicator = ""; // TODO:
								// recordHashMap.get("affiliations_affiliatedtaxid_taxidindicator").toString().trim();
		taxIdEffDate = recordHashMap.get("affiliations_affiliatedtaxid_taxidactive_effectivedate").toString().trim();
		taxIdTermDate = recordHashMap.get("affiliations_affiliatedtaxid_taxidactive_terminationdate").toString().trim();
		taxIdTermReasonCode = recordHashMap.get("affiliations_affiliatedtaxid_taxidactive_terminationreasoncode")
				.toString().trim();

		expTaxIdRecord = taxIdValue + " : " + taxIdIndicator + " : " + taxIdEffDate + " : " + taxIdTermDate + " : "
				+ taxIdTermReasonCode;

		if (taxIdValue == emptyStr && taxIdIndicator == emptyStr && taxIdEffDate == emptyStr
				&& taxIdTermDate == emptyStr && taxIdTermReasonCode == emptyStr) {
			if (affiliatedTaxIds != null) {
				resultList.add(new ResultTuple("Affiliated TaxIds node", "non null", "null", FAILED,
						"TaxIds node is not empty for Affiliation : " + affilLegacyId));
			} else {
				System.out.println("Ok no more Affiliated TaxIds node validation");
			}
		} else {
			if (affiliatedTaxIds == null) {
				resultList.add(new ResultTuple("Affiliated TaxIds node", "null", expTaxIdRecord, FAILED,
						"TaxIds node is missing for Affiliation : " + affilLegacyId));
			} else {
				int i = 0;
				for (AffliatedTaxID affilTaxId : affiliatedTaxIds) {
					if (taxIdValue.equalsIgnoreCase(Objects.toString(affilTaxId.getTaxIDValue(), ""))
							&& taxIdIndicator.equalsIgnoreCase(Objects.toString(affilTaxId.getTaxIDIndicator(), ""))
							&& taxIdEffDate.equalsIgnoreCase(
									Objects.toString(affilTaxId.getTaxIDActive().getEffectiveDate(), ""))
							&& taxIdTermDate.equalsIgnoreCase(
									Objects.toString(affilTaxId.getTaxIDActive().getTerminationDate(), ""))
							&& taxIdTermReasonCode.equalsIgnoreCase(
									Objects.toString(affilTaxId.getTaxIDActive().getTerminationReasonCode(), ""))) {

						taxIdFlag = true;
						setAffilTaxId(dummyAffilTaxIds.get(i));
						break;
					}
					i++;
				}
				if (taxIdFlag == false)
					resultList.add(new ResultTuple("Affiliated TaxId record", "", expTaxIdRecord, FAILED,
							"Tax Id record is missing for Affiliation : " + affilLegacyId));
			}
		}
	}

	// TODO: Clarification and rework pending
	public void validateAddresses(List<Address> addressList, String affilLegacyId) {

		// Boolean practiceInd;
		String expAddressType, expAddressLine1, expAddressLine2, expAddressLine3, expCounty, expCity, expState, expZip,
				expCityVanityName, expEffDate, expTermDate = "", expTermReasonCode = "";
		String expAddrRecord;
		String addressType = "", addressLine1 = "", addressLine2 = "", addressLine3 = "", county = "", city = "",
				state = "", zip = "", cityVanityName = "", effDate = "", termDate = "", termReasonCode = "";
		Boolean addressFlag = false;

		List<ContactDetail> contactDetails = null;
		List<AreasOFocus> areasOFocus = null;
		List<OfficeDetail> officeDetails = null;
		List<String> officeServices = null;
		List<SchedulingDetail> schedulingDetails = null;
		List<RemittanceDetail> remittanceDetails = null;

		// TODO: verify the db col name
		// practiceInd = (Boolean) recordHashMap.get("primarypracticeindicator");
		// TODO: change all db column names
		expAddressType = recordHashMap.get("affiliations_addresses_addressdetails_addresstype").toString().trim();
		expAddressLine1 = recordHashMap.get("affiliations_addresses_addressdetails_addressline1").toString().trim();
		expAddressLine2 = recordHashMap.get("affiliations_addresses_addressdetails_addressline2").toString().trim();
		expAddressLine3 = recordHashMap.get("affiliations_addresses_addressdetails_addressline3").toString().trim();
		expCounty = recordHashMap.get("affiliations_addresses_addressdetails_county").toString().trim();
		expCity = recordHashMap.get("affiliations_addresses_addressdetails_city").toString().trim();
		expState = recordHashMap.get("affiliations_addresses_addressdetails_state").toString().trim();
		expZip = recordHashMap.get("affiliations_addresses_addressdetails_zip").toString().trim();
		expCityVanityName = recordHashMap.get("affiliations_addresses_addressdetails_cityvanityname").toString().trim();
		expEffDate = recordHashMap.get("affiliations_addresses_addressactive_effectivedate").toString().trim();
		expTermDate = recordHashMap.get("affiliations_addresses_addressactive_terminationdate").toString().trim();
		expTermReasonCode = recordHashMap.get("affiliations_addresses_addressactive_terminationreasoncode").toString()
				.trim();

		expAddrRecord = expAddressType + " : " + expAddressLine1 + " : " + expAddressLine2 + " : " + expAddressLine3
				+ " : " + expCounty + " : " + expCity + " : " + expState + " : " + expZip + " : " + expCityVanityName
				+ " : " + expEffDate + " : " + expTermDate + " : " + expTermReasonCode;

		if (expAddressType == emptyStr && expAddressLine1 == emptyStr && expAddressLine2 == emptyStr
				&& expAddressLine3 == emptyStr && expCounty == emptyStr && expCity == emptyStr && expState == emptyStr
				&& expZip == emptyStr && expCityVanityName == emptyStr && expEffDate == emptyStr
				&& expTermDate == emptyStr && expTermReasonCode == emptyStr) {
			if (addressList != null) {
				resultList.add(new ResultTuple("Address Node", "non null", "null", FAILED,
						"Address node is not empty for Affiliation : " + affilLegacyId));
			} else {
				System.out.println("Ok no more address node validation");
			}
		} else {
			if (addressList == null) {
				resultList.add(new ResultTuple("Address Node", "null", expAddrRecord, FAILED,
						"Address node is missing for Affiliation : " + affilLegacyId));
			} else {
				for (Address address : addressList) {

					AddressDetails addrDetails = address.getAddressDetails();
					if (addrDetails != null) {
						addressType = Objects.toString(addrDetails.getAddressType(), "");
						addressLine1 = Objects.toString(addrDetails.getAddressLine1(), "");
						addressLine2 = Objects.toString(addrDetails.getAddressLine2(), "");
						addressLine3 = Objects.toString(addrDetails.getAddressLine3(), "");
						county = Objects.toString(addrDetails.getCounty(), "");
						city = Objects.toString(addrDetails.getCity(), "");
						state = Objects.toString(addrDetails.getState(), "");
						zip = Objects.toString(addrDetails.getZip(), "");
						cityVanityName = Objects.toString(addrDetails.getCityVanityName(), "");
					}

					AddressActive addrActive = address.getAddressActive();
					if (addrActive != null) {
						effDate = Objects.toString(addrActive.getEffectiveDate(), "");
						termDate = Objects.toString(addrActive.getTerminationDate(), "");
						termReasonCode = Objects.toString(addrActive.getTerminationReasonCode(), "");
					}

					if (expAddressType.equalsIgnoreCase(addressType) && expAddressLine1.equalsIgnoreCase(addressLine1)
							&& expAddressLine2.equalsIgnoreCase(addressLine2)
							&& expAddressLine3.equalsIgnoreCase(addressLine3) && expCounty.equalsIgnoreCase(county)
							&& expCity.equalsIgnoreCase(city) && expState.equalsIgnoreCase(state)
							&& expZip.equalsIgnoreCase(zip) && expCityVanityName.equalsIgnoreCase(cityVanityName)
							&& expEffDate.equalsIgnoreCase(effDate) && expTermDate.equalsIgnoreCase(termDate)
							&& expTermReasonCode.equalsIgnoreCase(termReasonCode)) {

						addressFlag = true;

						AddressAdditionalDetails addrAddDetails = address.getAddressAdditionalDetails();
						if (addrAddDetails != null) {
							contactDetails = addrAddDetails.getContactDetails();
							areasOFocus = addrAddDetails.getAreasOFocus();
							officeDetails = addrAddDetails.getOfficeDetails();
							officeServices = addrAddDetails.getOfficeServiceTypes();
							schedulingDetails = addrAddDetails.getSchedulingDetails();
							remittanceDetails = addrAddDetails.getRemittanceDetails();
						}

//						validateContactDetails(contactDetails, expAddressLine1);
//						validateAreaOfFocusDetails(areasOFocus, expAddressLine1);
//						validateOfficeDetails(officeDetails, expAddressLine1);
//						validateOfficeServices(officeServices, expAddressLine1);
//						validateSchedulingDetails(schedulingDetails, expAddressLine1);
//						validateRemittanceDetails(remittanceDetails, expAddressLine1);

						break;
					}
				}
				if (addressFlag == false)
					resultList.add(new ResultTuple("Address Record", "", expAddrRecord, FAILED,
							"Address record is missing for Affiliation : " + affilLegacyId));
			}
		}
	}

	public void validateNtwrkAffil(List<NetworkAffiliation> ntwrkAffilList, String affilLegacyId,
			List<NetworkAffiliation> dummyNtwrkAffilList) {

		String ntwrkContractedState, ntwrkSrcSystem, ntwrkId, directoryInd, ntwrkEffDate, ntwrkTermDate,
				ntwrkTermReasonCode, ageFrom, ageTo, patientGenderPref, memberCapacityCount, hospitalBasedNonParInds;
		String expNtwrkAffilRecord;
		String actAgeFrom = "", actAgeTo = "", actPatientGenderPref = "", actMemberCapacityCount = "",
				actNonParInds = "";
		Boolean ntwrkAffilFlag = false;

		ntwrkContractedState = recordHashMap.get("affiliations_networkaffiliations_networkcontractedstate").toString()
				.trim();
		ntwrkSrcSystem = recordHashMap.get("affiliations_networkaffiliations_networksourcesystem").toString().trim();
		ntwrkId = recordHashMap.get("affiliations_networkaffiliations_networkid").toString().trim();
		directoryInd = recordHashMap.get("affiliations_networkaffiliations_directoryindicator").toString().trim();
		ntwrkEffDate = recordHashMap.get("affiliations_networkaffiliations_networkactive_effectivedate").toString()
				.trim();
		ntwrkTermDate = recordHashMap.get("affiliations_networkaffiliations_networkactive_terminationdate").toString()
				.trim();
		ntwrkTermReasonCode = recordHashMap.get("affiliations_networkaffiliations_networkactive_terminationreasoncode")
				.toString().trim();
		ageFrom = recordHashMap.get("affiliations_networkaffiliations_patientpreferences_agefrom").toString().trim();
		ageTo = recordHashMap.get("affiliations_networkaffiliations_patientpreferences_ageto").toString().trim();
		patientGenderPref = recordHashMap
				.get("affiliations_networkaffiliations_patientpreferences_patientgenderpreference").toString().trim();
		memberCapacityCount = recordHashMap
				.get("affiliations_networkaffiliations_patientpreferences_membercapacitycount").toString().trim();
		hospitalBasedNonParInds = recordHashMap
				.get("affiliations_networkaffiliations_patientpreferences_hospitalbasednonparinds").toString().trim();

		expNtwrkAffilRecord = ntwrkContractedState + " : " + ntwrkSrcSystem + " : " + ntwrkId + " : " + directoryInd
				+ " : " + ntwrkEffDate + " : " + ntwrkTermDate + " : " + ntwrkTermReasonCode + " : " + ageFrom + " : "
				+ ageTo + " : " + patientGenderPref + " : " + memberCapacityCount + " : " + hospitalBasedNonParInds;

		if (ntwrkContractedState == emptyStr && ntwrkSrcSystem == emptyStr && ntwrkId == emptyStr
				&& directoryInd == emptyStr && ntwrkEffDate == emptyStr && ntwrkTermDate == emptyStr
				&& ntwrkTermReasonCode == emptyStr && ageFrom == emptyStr && ageTo == emptyStr
				&& patientGenderPref == emptyStr && memberCapacityCount == emptyStr
				&& hospitalBasedNonParInds == emptyStr) {
			if (ntwrkAffilList != null) {
				resultList.add(new ResultTuple("Affil NetworkAffil node", "non null", "null", FAILED,
						"NetworkAffil node is not empty for Affiliation : " + affilLegacyId));
			} else {
				System.out.println("Ok no more NetworkAffil node validation");
			}
		} else {
			if (ntwrkAffilList == null) {
				resultList.add(new ResultTuple("Affil NetworkAffil node", "null", expNtwrkAffilRecord, FAILED,
						"NetworkAffil node is missing for Affiliation : " + affilLegacyId));
			} else {
				int i = 0;
				for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {
					PatientPreferences patientPref = ntwrkAffil.getPatientPreferences();
					if (patientPref != null) {
						actAgeFrom = Objects.toString(patientPref.getAgeFrom(), "");
						actAgeTo = Objects.toString(patientPref.getAgeTo(), "");
						actPatientGenderPref = Objects.toString(patientPref.getPatientGenderPreference(), "");
						actMemberCapacityCount = Objects.toString(patientPref.getMemberCapacityCount(), "");
						actNonParInds = Objects.toString(patientPref.getHospitalBasedNONPARInds(), "");
					}

					if (ntwrkContractedState
							.equalsIgnoreCase(Objects.toString(ntwrkAffil.getNetworkContractedState(), ""))
							&& ntwrkSrcSystem
									.equalsIgnoreCase(Objects.toString(ntwrkAffil.getNetworkSourceSystem(), ""))
							&& ntwrkId.equalsIgnoreCase(Objects.toString(ntwrkAffil.getNetworkID(), ""))
							&& directoryInd.equalsIgnoreCase(Objects.toString(ntwrkAffil.getDirectoryIndicator(), ""))
							&& ntwrkEffDate.equalsIgnoreCase(
									Objects.toString(ntwrkAffil.getNetworkActive().getEffectiveDate(), ""))
							&& ntwrkTermDate.equalsIgnoreCase(
									Objects.toString(ntwrkAffil.getNetworkActive().getTerminationDate(), ""))
							&& ntwrkTermReasonCode.equalsIgnoreCase(
									Objects.toString(ntwrkAffil.getNetworkActive().getTerminationReasonCode(), ""))
							&& ageFrom.equalsIgnoreCase(actAgeFrom) && ageTo.equalsIgnoreCase(actAgeTo)
							&& patientGenderPref.equalsIgnoreCase(actPatientGenderPref)
							&& memberCapacityCount.equalsIgnoreCase(actMemberCapacityCount)
							&& isContainedInString(hospitalBasedNonParInds, actNonParInds)) {

						ntwrkAffilFlag = true;
						NetworkAffiliation dummyNtwrkAffil = dummyNtwrkAffilList.get(i);
						setNtwrkAffil(dummyNtwrkAffil);

						validateAcceptPatientsInds(ntwrkAffil.getAcceptingPatientsInds(),
								affilLegacyId + " : " + ntwrkId, dummyNtwrkAffil.getAcceptingPatientsInds());
						validateTimelyFilingInds(ntwrkAffil.getTimelyFilingInds(), affilLegacyId + " : " + ntwrkId,
								dummyNtwrkAffil.getTimelyFilingInds());

						break;
					}
					i++;
				}
				if (ntwrkAffilFlag == false)
					resultList.add(new ResultTuple("Affil NetworkAffil record", "", expNtwrkAffilRecord, FAILED,
							"NetworkAffil record is missing for Affiliation : " + affilLegacyId));
			}
		}
	}

	public void validateAcceptPatientsInds(List<AcceptingPatientsInd> acceptPatientsIndList, String ntwrkAffil,
			List<AcceptingPatientsInd> dummyAccPatsIndList) {

		String acceptPatientsIndStr, acceptPatientsEffDate, acceptPatientsTermDate, acceptPatientsTermReasonCode;
		String expAcceptPatientsRecord;
		Boolean acceptPatientsFlag = false;

		acceptPatientsIndStr = recordHashMap
				.get("affiliations_networkaffiliations_acceptingpatientsinds_acceptingpatientsindicator").toString()
				.trim();
		acceptPatientsEffDate = recordHashMap.get(
				"affiliations_networkaffiliations_acceptingpatientsinds_acceptingpatientsindicatoractive_effectivedate")
				.toString().trim();
		acceptPatientsTermDate = recordHashMap.get(
				"affiliations_networkaffiliations_acceptingpatientsinds_acceptingpatientsindicatoractive_terminationdate")
				.toString().trim();
		acceptPatientsTermReasonCode = recordHashMap.get(
				"affiliations_networkaffiliations_acceptingpatientsinds_acceptingpatientsindicatoractive_terminationreasoncode")
				.toString().trim();

		expAcceptPatientsRecord = acceptPatientsIndStr + " : " + acceptPatientsEffDate + " : " + acceptPatientsTermDate
				+ " : " + acceptPatientsTermReasonCode;

		if (acceptPatientsIndStr == emptyStr && acceptPatientsEffDate == emptyStr && acceptPatientsTermDate == emptyStr
				&& acceptPatientsTermReasonCode == emptyStr) {
			if (acceptPatientsIndList != null) {
				resultList.add(new ResultTuple("Accepting Patients Inds node", "non null", "null", FAILED,
						"Accepting Patients Inds node is not empty"));
			} else {
				System.out.println("Ok no more Accepting Patients Inds node validation");
			}
		} else {
			if (acceptPatientsIndList == null) {
				resultList.add(new ResultTuple("Accepting Patients Inds node", "null", "non null", FAILED,
						"Accepting Patients Inds node is missing"));
			} else {
				int i = 0;
				for (AcceptingPatientsInd acceptPatientsInd : acceptPatientsIndList) {
					if (acceptPatientsIndStr
							.equalsIgnoreCase(Objects.toString(acceptPatientsInd.getAcceptingPatientsIndicator(), ""))
							&& acceptPatientsEffDate.equalsIgnoreCase(Objects.toString(
									acceptPatientsInd.getAcceptingPatientsIndicatorActive().getEffectiveDate(), ""))
							&& acceptPatientsTermDate.equalsIgnoreCase(Objects.toString(
									acceptPatientsInd.getAcceptingPatientsIndicatorActive().getTerminationDate(), ""))
							&& acceptPatientsTermReasonCode.equalsIgnoreCase(Objects.toString(
									acceptPatientsInd.getAcceptingPatientsIndicatorActive().getTerminationReasonCode(),
									""))) {

						acceptPatientsFlag = true;
						setAcceptPatientsInd(dummyAccPatsIndList.get(i));
						break;
					}
					i++;
				}
				if (acceptPatientsFlag == false)
					resultList.add(new ResultTuple("Accepting Patients Ind record", "", expAcceptPatientsRecord, FAILED,
							"Accepting Patients Ind record is missing for ntwrkAffil : " + ntwrkAffil));
			}
		}
	}

	public void validateTimelyFilingInds(List<TimelyFilingInd> timelyFilingInds, String ntwrkAffil,
			List<TimelyFilingInd> dummyTimelyFiling) {

		String timelyFilingStr, timelyFilingEffDate, timelyFilingTermDate, timelyFilingTermReasonCode;
		String expTimelyFilingRecord;
		Boolean timelyFilingFlag = false;

		timelyFilingStr = recordHashMap.get("affiliations_networkaffiliations_timelyfilinginds_timelyfiling").toString()
				.trim();
		timelyFilingEffDate = recordHashMap
				.get("affiliations_networkaffiliations_timelyfilinginds_timelyfilingactive_effectivedate").toString()
				.trim();
		timelyFilingTermDate = recordHashMap
				.get("affiliations_networkaffiliations_timelyfilinginds_timelyfilingactive_terminationdate").toString()
				.trim();
		timelyFilingTermReasonCode = recordHashMap
				.get("affiliations_networkaffiliations_timelyfilinginds_timelyfilingactive_terminationreasoncode")
				.toString().trim();

		expTimelyFilingRecord = timelyFilingStr + " : " + timelyFilingEffDate + " : " + timelyFilingTermDate + " : "
				+ timelyFilingTermReasonCode;

		if (timelyFilingStr == emptyStr && timelyFilingEffDate == emptyStr && timelyFilingTermDate == emptyStr
				&& timelyFilingTermReasonCode == emptyStr) {
			if (timelyFilingInds != null) {
				resultList.add(new ResultTuple("Timely Filing Inds node", "non null", "null", FAILED,
						"Timely Filing Inds node is not empty for ntwrkAffil : " + ntwrkAffil));
			} else {
				System.out.println("Ok no more Timely Filing Inds node validation");
			}
		} else {
			if (timelyFilingInds == null) {
				resultList.add(new ResultTuple("Timely Filing Inds node", "null", "non null", FAILED,
						"Timely Filing Inds node is missing for ntwrkAffil : " + ntwrkAffil));
			} else {
				int i = 0;
				for (TimelyFilingInd timelyFiling : timelyFilingInds) {
					if (timelyFilingStr.equalsIgnoreCase(Objects.toString(timelyFiling.getTimelyFiling(), ""))
							&& timelyFilingEffDate.equalsIgnoreCase(
									Objects.toString(timelyFiling.getTimelyFilingActive().getEffectiveDate(), ""))
							&& timelyFilingTermDate.equalsIgnoreCase(
									Objects.toString(timelyFiling.getTimelyFilingActive().getTerminationDate(), ""))
							&& timelyFilingTermReasonCode.equalsIgnoreCase(Objects
									.toString(timelyFiling.getTimelyFilingActive().getTerminationReasonCode(), ""))) {

						timelyFilingFlag = true;
						setTimelyFilingInd(dummyTimelyFiling.get(i));
						break;
					}
					i++;
				}
				if (timelyFilingFlag == false)
					resultList.add(new ResultTuple("Timely Filing Ind record", "", expTimelyFilingRecord, FAILED,
							"Timely Filing Ind record is missing for ntwrkAffil : " + ntwrkAffil));
			}
		}
	}

	public void validateReimbursement(List<Reimbursement> reimbList, String affilLegacyId,
			List<Reimbursement> dummyReimbList) {

		String networkId, reimbValue, reimbEffDate, reimbTermDate, reimbTermReasonCode;
		String expReimbRecord;
		Boolean reimbFlag = false;

		networkId = recordHashMap.get("affiliations_reimbursements_networkid").toString().trim();
		reimbValue = recordHashMap.get("affiliations_reimbursements_reimbursementvalue").toString().trim();
		reimbEffDate = recordHashMap.get("affiliations_reimbursements_reimbursementactive_effectivedate").toString()
				.trim();
		reimbTermDate = recordHashMap.get("affiliations_reimbursements_reimbursementactive_terminationdate").toString()
				.trim();
		reimbTermReasonCode = recordHashMap.get("affiliations_reimbursements_reimbursementactive_terminationreasoncode")
				.toString().trim();

		expReimbRecord = networkId + " : " + reimbValue + " : " + reimbEffDate + " : " + reimbTermDate + " : "
				+ reimbTermReasonCode;

		if (networkId == emptyStr && reimbValue == emptyStr && reimbEffDate == emptyStr && reimbTermDate == emptyStr
				&& reimbTermReasonCode == emptyStr) {
			if (reimbList != null) {
				resultList.add(new ResultTuple("Affil Reimbursement node", "non null", "null", FAILED,
						"Reimbursement node is not empty for Affiliation : " + affilLegacyId));
			} else {
				System.out.println("Ok no more Reimbursement node validation");
			}
		} else {
			if (reimbList == null) {
				resultList.add(new ResultTuple("Affil Reimbursement node", "null", expReimbRecord, FAILED,
						"Reimbursement node is missing for Affiliation : " + affilLegacyId));
			} else {
				int i = 0;
				for (Reimbursement reimbursement : reimbList) {
					if (networkId.equalsIgnoreCase(Objects.toString(reimbursement.getNetworkID(), ""))
							&& reimbValue.equalsIgnoreCase(Objects.toString(reimbursement.getReimbursementValue(), ""))
							&& reimbEffDate.equalsIgnoreCase(
									Objects.toString(reimbursement.getReimbursementActive().getEffectiveDate(), ""))
							&& reimbTermDate.equalsIgnoreCase(
									Objects.toString(reimbursement.getReimbursementActive().getTerminationDate(), ""))
							&& reimbTermReasonCode.equalsIgnoreCase(Objects
									.toString(reimbursement.getReimbursementActive().getTerminationReasonCode(), ""))) {

						reimbFlag = true;
						setReimbursement(dummyReimbList.get(i));
						break;
					}
					i++;
				}
				if (reimbFlag == false)
					resultList.add(new ResultTuple("Affil Reimbursement record", "", expReimbRecord, FAILED,
							"Reimbursement record is missing for Affiliation : " + affilLegacyId));
			}
		}
	}

	public void setAffilDetail(Affiliation dummyAffilDetail) {

		dummyAffilDetail.setAffiliatedLegacyID(TRUE);
		dummyAffilDetail.setAffiliationType(TRUE);
		dummyAffilDetail.setGroupingCode(TRUE);
		dummyAffilDetail.setGroupingType(TRUE);
		dummyAffilDetail.setPcpID(TRUE);
		dummyAffilDetail.getAffiliationActive().setEffectiveDate(TRUE);
		dummyAffilDetail.getAffiliationActive().setTerminationDate(TRUE);
		dummyAffilDetail.getAffiliationActive().setTerminationReasonCode(TRUE);
	}

	public void setNpiDetail(NpiDetail dummyNpiDetail) {

		dummyNpiDetail.setNpiEligibilityType(TRUE);
		dummyNpiDetail.setNpiType(TRUE);
		dummyNpiDetail.setNpiValue(TRUE);
	}

	public void setAffilTaxId(AffliatedTaxID dummyAffilTaxId) {

		dummyAffilTaxId.setTaxIDValue(TRUE);
		dummyAffilTaxId.setTaxIDIndicator(TRUE);
		dummyAffilTaxId.getTaxIDActive().setEffectiveDate(TRUE);
		dummyAffilTaxId.getTaxIDActive().setTerminationDate(TRUE);
		dummyAffilTaxId.getTaxIDActive().setTerminationReasonCode(TRUE);
	}

	public void setNtwrkAffil(NetworkAffiliation dummyNtwrkAffil) {

		String hospitalBasedNonParInds = recordHashMap
				.get("affiliations_networkaffiliations_patientpreferences_hospitalbasednonparinds").toString().trim();

		dummyNtwrkAffil.setNetworkContractedState(TRUE);
		dummyNtwrkAffil.setNetworkSourceSystem(TRUE);
		dummyNtwrkAffil.setNetworkID(TRUE);
		dummyNtwrkAffil.setDirectoryIndicator(TRUE);
		dummyNtwrkAffil.getNetworkActive().setEffectiveDate(TRUE);
		dummyNtwrkAffil.getNetworkActive().setTerminationDate(TRUE);
		dummyNtwrkAffil.getNetworkActive().setTerminationReasonCode(TRUE);

		if (dummyNtwrkAffil.getPatientPreferences() != null) {
			dummyNtwrkAffil.getPatientPreferences().setAgeFrom(TRUE);
			dummyNtwrkAffil.getPatientPreferences().setAgeTo(TRUE);
			dummyNtwrkAffil.getPatientPreferences().setPatientGenderPreference(TRUE);
			dummyNtwrkAffil.getPatientPreferences().setMemberCapacityCount(TRUE);

			List<String> nonParIndList = dummyNtwrkAffil.getPatientPreferences().getHospitalBasedNONPARInds();
			if (nonParIndList != null) {
				nonParIndList.remove(hospitalBasedNonParInds);
				dummyNtwrkAffil.getPatientPreferences().setHospitalBasedNONPARInds(nonParIndList);
			}
		}
	}

	public void setAcceptPatientsInd(AcceptingPatientsInd dummyAcceptPatientsInd) {

		dummyAcceptPatientsInd.setAcceptingPatientsIndicator(TRUE);
		dummyAcceptPatientsInd.getAcceptingPatientsIndicatorActive().setEffectiveDate(TRUE);
		dummyAcceptPatientsInd.getAcceptingPatientsIndicatorActive().setTerminationDate(TRUE);
		dummyAcceptPatientsInd.getAcceptingPatientsIndicatorActive().setTerminationReasonCode(TRUE);
	}

	public void setTimelyFilingInd(TimelyFilingInd dummyTimelyFiling) {

		dummyTimelyFiling.setTimelyFiling(TRUE);
		dummyTimelyFiling.getTimelyFilingActive().setEffectiveDate(TRUE);
		dummyTimelyFiling.getTimelyFilingActive().setTerminationDate(TRUE);
		dummyTimelyFiling.getTimelyFilingActive().setTerminationReasonCode(TRUE);
	}

	public void setReimbursement(Reimbursement dummyReimbursement) {

		dummyReimbursement.setNetworkID(TRUE);
		dummyReimbursement.setReimbursementValue(TRUE);
		dummyReimbursement.getReimbursementActive().setEffectiveDate(TRUE);
		dummyReimbursement.getReimbursementActive().setTerminationDate(TRUE);
		dummyReimbursement.getReimbursementActive().setTerminationReasonCode(TRUE);
	}

}
