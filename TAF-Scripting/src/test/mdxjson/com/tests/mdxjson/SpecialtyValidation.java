package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.Address;
import com.beans.mdxjson.AddressActive;
import com.beans.mdxjson.AddressAdditionalDetails;
import com.beans.mdxjson.AddressDetails;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.PatientPreferences;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.Specialty;
import com.utilities.mdxjson.ResultTuple;

public class SpecialtyValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(SpecialtyValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateSpecialty(LinkedHashMap<String, Object> recordHashMap, List<Specialty> specialtyList,
			List<Specialty> dummySpltyList) {

		this.recordHashMap = recordHashMap;

		String billingForm, primarySpecialtyIndicator, specialtyCode, boardCertificationAgencyName,
				boardCertificationLifetimeCertificationIndicator, boardCertificationRecertificationDate,
				boardCertificationDate, boardCertificationExpirationDate, spltyEffDate, spltyTermDate,
				spltyTermReasoncode;
		String expSpecialtyRecord;
		Boolean specialtyFlag = false;

		billingForm = recordHashMap.get("specialties_billingform").toString().trim();
		primarySpecialtyIndicator = recordHashMap.get("specialties_primaryspecialtyindicator").toString().trim();
		specialtyCode = recordHashMap.get("specialties_specialtycode").toString().trim();
		boardCertificationAgencyName = recordHashMap.get("specialties_boardcertificationagencyname").toString().trim();
		boardCertificationLifetimeCertificationIndicator = recordHashMap
				.get("specialties_boardcertificationlifetimecertificationindicator").toString().trim();
		boardCertificationRecertificationDate = recordHashMap.get("specialties_boardcertificationrecertificationdate")
				.toString().trim();
		boardCertificationDate = recordHashMap.get("specialties_boardcertificationdate").toString().trim();
		boardCertificationExpirationDate = recordHashMap.get("specialties_boardcertificationexpirationdate").toString()
				.trim();
		spltyEffDate = recordHashMap.get("specialties_specialtyactive_effectivedate").toString().trim();
		spltyTermDate = recordHashMap.get("specialties_specialtyactive_terminationdate").toString().trim();
		spltyTermReasoncode = recordHashMap.get("specialties_specialtyactive_terminationreasoncode").toString().trim();

		expSpecialtyRecord = billingForm + ":" + primarySpecialtyIndicator + ":" + specialtyCode + ":"
				+ boardCertificationAgencyName + ":" + boardCertificationLifetimeCertificationIndicator + ":"
				+ boardCertificationRecertificationDate + ":" + boardCertificationDate + ":"
				+ boardCertificationExpirationDate + ":" + spltyEffDate + ":" + spltyTermDate + ":"
				+ spltyTermReasoncode;

		if (billingForm == emptyStr && primarySpecialtyIndicator == emptyStr && specialtyCode == emptyStr
				&& boardCertificationAgencyName == emptyStr
				&& boardCertificationLifetimeCertificationIndicator == emptyStr
				&& boardCertificationRecertificationDate == emptyStr && boardCertificationDate == emptyStr
				&& boardCertificationExpirationDate == emptyStr && spltyEffDate == emptyStr && spltyTermDate == emptyStr
				&& spltyTermReasoncode == emptyStr) {
			if (specialtyList != null) {
				resultList.add(
						new ResultTuple("Specialty node", "non null", "null", FAILED, "Specialty node is not empty"));
			} else {
				System.out.println("Ok no more Specialty node validation");
			}
		} else {
			if (specialtyList == null) {
				resultList.add(new ResultTuple("Specialty node", "null", expSpecialtyRecord, FAILED,
						"Specialty node is missing"));
			} else {
				int i = 0;
				for (Specialty specialty : specialtyList) {
					if (billingForm.equalsIgnoreCase(Objects.toString(specialty.getBillingform(), ""))
							&& primarySpecialtyIndicator
									.equalsIgnoreCase(Objects.toString(specialty.getPrimarySpecialtyIndicator(), ""))
							&& specialtyCode.equalsIgnoreCase(Objects.toString(specialty.getSpecialtyCode(), ""))
							&& boardCertificationAgencyName
									.equalsIgnoreCase(Objects.toString(specialty.getBoardCertificationAgencyName(), ""))
							&& boardCertificationLifetimeCertificationIndicator.equalsIgnoreCase(Objects
									.toString(specialty.getBoardCertificationLifetimeCertificationIndicator(), ""))
							&& boardCertificationRecertificationDate.equalsIgnoreCase(
									Objects.toString(specialty.getBoardCertificationRecertificationDate(), ""))
							&& boardCertificationDate
									.equalsIgnoreCase(Objects.toString(specialty.getBoardCertificationDate(), ""))
							&& boardCertificationExpirationDate.equalsIgnoreCase(
									Objects.toString(specialty.getBoardCertificationExpirationDate(), ""))
							&& spltyEffDate.equalsIgnoreCase(
									Objects.toString(specialty.getSpecialtyActive().getEffectiveDate(), ""))
							&& spltyTermDate.equalsIgnoreCase(
									Objects.toString(specialty.getSpecialtyActive().getTerminationDate(), ""))
							&& spltyTermReasoncode.equalsIgnoreCase(
									Objects.toString(specialty.getSpecialtyActive().getTerminationReasonCode(), ""))) {

						specialtyFlag = true;
						setSpecialty(dummySpltyList.get(i));

						// TODO: Clarification pending
//						validateAddresses(specialty.getAddresses(), specialtyCode);
//						validateNetworkAffil(specialty.getNetworkAffiliations(), specialtyCode);

						break;
					}
					i++;
				}
				if (specialtyFlag == false)
					resultList.add(new ResultTuple("Specialty record", "", expSpecialtyRecord, FAILED,
							"Specialty record is missing"));
			}
		}
	}

	// TODO: Clarification and rework pending
	public void validateAddresses(List<Address> addressList, String specialtyCode) {

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
		expAddressType = recordHashMap.get("specialty_addresses_addressdetails_addresstype").toString().trim();
		expAddressLine1 = recordHashMap.get("specialty_addresses_addressdetails_addressline1").toString().trim();
		expAddressLine2 = recordHashMap.get("specialty_addresses_addressdetails_addressline2").toString().trim();
		expAddressLine3 = recordHashMap.get("specialty_addresses_addressdetails_addressline3").toString().trim();
		expCounty = recordHashMap.get("specialty_addresses_addressdetails_county").toString().trim();
		expCity = recordHashMap.get("specialty_addresses_addressdetails_city").toString().trim();
		expState = recordHashMap.get("specialty_addresses_addressdetails_state").toString().trim();
		expZip = recordHashMap.get("specialty_addresses_addressdetails_zip").toString().trim();
		expCityVanityName = recordHashMap.get("specialty_addresses_addressdetails_cityvanityname").toString().trim();
		expEffDate = recordHashMap.get("specialty_addresses_addressactive_effectivedate").toString().trim();
		expTermDate = recordHashMap.get("specialty_addresses_addressactive_terminationdate").toString().trim();
		expTermReasonCode = recordHashMap.get("specialty_addresses_addressactive_terminationreasoncode").toString()
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
						"Address node is not empty for Specialty : " + specialtyCode));
			} else {
				System.out.println("Ok no more address node validation");
			}
		} else {
			if (addressList == null) {
				resultList.add(new ResultTuple("Address Node", "null", expAddrRecord, FAILED,
						"Address node is missing for Specialty : " + specialtyCode));
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

//								validateContactDetails(contactDetails, expAddressLine1);
//								validateAreaOfFocusDetails(areasOFocus, expAddressLine1);
//								validateOfficeDetails(officeDetails, expAddressLine1);
//								validateOfficeServices(officeServices, expAddressLine1);
//								validateSchedulingDetails(schedulingDetails, expAddressLine1);
//								validateRemittanceDetails(remittanceDetails, expAddressLine1);

						break;
					}
				}
				if (addressFlag == false)
					resultList.add(new ResultTuple("Address Record", "", expAddrRecord, FAILED,
							"Address record is missing for Specialty : " + specialtyCode));
			}
		}
	}

	// TODO: Clarification and rework pending
	public void validateNetworkAffil(List<NetworkAffiliation> ntwrkAffilList, String specialtyCode,
			List<NetworkAffiliation> dummyNtwrkAffilList) {

		String ntwrkContractedState, ntwrkSrcSystem, networkId, directoryInd, ntwrkEffDate, ntwrkTermDate,
				ntwrkTermReasonCode, ageFrom, ageTo, patientGenderPref, memberCapacityCount, hospitalBasedNonParInds;
		String expNtwrkAffilRecord;
		String actAgeFrom = "", actAgeTo = "", actPatientGenderPref = "", actMemberCapacityCount = "",
				actNonParInds = "";
		Boolean ntwrkAffilFlag = false;

		ntwrkContractedState = recordHashMap.get("affiliations_networkaffiliations_networkcontractedstate").toString()
				.trim();
		ntwrkSrcSystem = recordHashMap.get("specialty_networkaffiliations_networksourcesystem").toString().trim();
		networkId = recordHashMap.get("specialty_networkaffiliations_networkid").toString().trim();
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

		expNtwrkAffilRecord = ntwrkContractedState + " : " + ntwrkSrcSystem + " : " + networkId + " : " + directoryInd
				+ " : " + ntwrkEffDate + " : " + ntwrkTermDate + " : " + ntwrkTermReasonCode + " : " + ageFrom + " : "
				+ ageTo + " : " + patientGenderPref + " : " + memberCapacityCount + " : " + hospitalBasedNonParInds;

		if (ntwrkContractedState == emptyStr && ntwrkSrcSystem == emptyStr && networkId == emptyStr
				&& directoryInd == emptyStr && ntwrkEffDate == emptyStr && ntwrkTermDate == emptyStr
				&& ntwrkTermReasonCode == emptyStr && ageFrom == emptyStr && ageTo == emptyStr
				&& patientGenderPref == emptyStr && memberCapacityCount == emptyStr
				&& hospitalBasedNonParInds == emptyStr) {
			if (ntwrkAffilList != null) {
				resultList.add(new ResultTuple("Network Affiliation node", "non null", "null", FAILED,
						"Network Affiliation node is not empty for Specialty : " + specialtyCode));
			} else {
				System.out.println("Ok no more Network Affiliation node validation");
			}
		} else {
			if (ntwrkAffilList == null) {
				resultList.add(new ResultTuple("Network Affiliation node", "null", "non null", FAILED,
						"Network Affiliation node is missing for Specialty : " + specialtyCode));
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
							&& networkId.equalsIgnoreCase(Objects.toString(ntwrkAffil.getNetworkID(), ""))
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
						setNtwrkAffil(dummyNtwrkAffilList.get(i));
						break;
					}
				}
				if (ntwrkAffilFlag == false)
					resultList.add(new ResultTuple("Network Affiliation record", "", expNtwrkAffilRecord, FAILED,
							"Network Affiliation is missing for Specialty : " + specialtyCode));
			}
		}
	}

	public void setSpecialty(Specialty dummySpecialty) {

		dummySpecialty.setBillingform(TRUE);
		dummySpecialty.setPrimarySpecialtyIndicator(true);
		dummySpecialty.setSpecialtyCode(TRUE);
		dummySpecialty.setBoardCertificationAgencyName(TRUE);
		dummySpecialty.setBoardCertificationLifetimeCertificationIndicator(TRUE);
		dummySpecialty.setBoardCertificationRecertificationDate(TRUE);
		dummySpecialty.setBoardCertificationDate(TRUE);
		dummySpecialty.setBoardCertificationExpirationDate(TRUE);
		dummySpecialty.getSpecialtyActive().setEffectiveDate(TRUE);
		dummySpecialty.getSpecialtyActive().setTerminationDate(TRUE);
		dummySpecialty.getSpecialtyActive().setTerminationReasonCode(TRUE);
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
}
