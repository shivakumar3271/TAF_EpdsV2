package com.tests.mdxjson;

import java.util.Arrays;
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
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.RemittanceDetail;
import com.beans.mdxjson.RemittanceNPI;
import com.beans.mdxjson.SchedulingDetail;
import com.utilities.mdxjson.ResultTuple;

public class AddressValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AddressValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateAddressDetails(LinkedHashMap<String, Object> recordHashMap, List<Address> addressList,
			List<Address> dummyAddrList) {

		this.recordHashMap = recordHashMap;

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

		List<ContactDetail> dummyContactDetails = null;
		List<AreasOFocus> dummyAreasOFocus = null;
		List<OfficeDetail> dummyOfficeDetails = null;
		List<String> dummyOfficeServices = null;
		List<SchedulingDetail> dummySchedDetails = null;
		List<RemittanceDetail> dummyRemitDetails = null;

		// TODO: verify the db col name
		// practiceInd = (Boolean) recordHashMap.get("primarypracticeindicator");
		expAddressType = recordHashMap.get("addresses_addressdetails_addresstype").toString().trim();
		expAddressLine1 = recordHashMap.get("addresses_addressdetails_addressline1").toString().trim();
		expAddressLine2 = recordHashMap.get("addresses_addressdetails_addressline2").toString().trim();
		expAddressLine3 = recordHashMap.get("addresses_addressdetails_addressline3").toString().trim();
		expCounty = recordHashMap.get("addresses_addressdetails_county").toString().trim();
		expCity = recordHashMap.get("addresses_addressdetails_city").toString().trim();
		expState = recordHashMap.get("addresses_addressdetails_state").toString().trim();
		expZip = recordHashMap.get("addresses_addressdetails_zip").toString().trim();
		expCityVanityName = recordHashMap.get("addresses_addressdetails_cityvanityname").toString().trim();
		expEffDate = recordHashMap.get("addresses_addressdetails_addressactive_effectivedate").toString().trim();
		expTermDate = recordHashMap.get("addresses_addressdetails_addressactive_terminationdate").toString().trim();
		expTermReasonCode = recordHashMap.get("addresses_addressdetails_addressactive_terminationreasoncode").toString()
				.trim();

		expAddrRecord = expAddressType + " : " + expAddressLine1 + " : " + expAddressLine2 + " : " + expAddressLine3
				+ " : " + expCounty + " : " + expCity + " : " + expState + " : " + expZip + " : " + expCityVanityName
				+ " : " + expEffDate + " : " + expTermDate + " : " + expTermReasonCode;

		if (expAddressType == emptyStr && expAddressLine1 == emptyStr && expAddressLine2 == emptyStr
				&& expAddressLine3 == emptyStr && expCounty == emptyStr && expCity == emptyStr && expState == emptyStr
				&& expZip == emptyStr && expCityVanityName == emptyStr && expEffDate == emptyStr
				&& expTermDate == emptyStr && expTermReasonCode == emptyStr) {
			if (addressList != null) {
				resultList
						.add(new ResultTuple("Address Node", "non null", "null", FAILED, "Address node is not empty"));
			} else {
				System.out.println("Ok no more address node validation");
			}
		} else {
			if (addressList == null) {
				resultList
						.add(new ResultTuple("Address Node", "null", expAddrRecord, FAILED, "Address node is missing"));
			} else {
				int i = 0;
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
						Address dummyAddress = dummyAddrList.get(i);
						setAddress(dummyAddress);

						AddressAdditionalDetails addrAddDetails = address.getAddressAdditionalDetails();
						if (addrAddDetails != null) {
							contactDetails = addrAddDetails.getContactDetails();
							areasOFocus = addrAddDetails.getAreasOFocus();
							officeDetails = addrAddDetails.getOfficeDetails();
							officeServices = addrAddDetails.getOfficeServiceTypes();
							schedulingDetails = addrAddDetails.getSchedulingDetails();
							remittanceDetails = addrAddDetails.getRemittanceDetails();

							dummyContactDetails = dummyAddress.getAddressAdditionalDetails().getContactDetails();
							dummyAreasOFocus = dummyAddress.getAddressAdditionalDetails().getAreasOFocus();
							dummyOfficeDetails = dummyAddress.getAddressAdditionalDetails().getOfficeDetails();
							dummyOfficeServices = dummyAddress.getAddressAdditionalDetails().getOfficeServiceTypes();
							dummySchedDetails = dummyAddress.getAddressAdditionalDetails().getSchedulingDetails();
							dummyRemitDetails = dummyAddress.getAddressAdditionalDetails().getRemittanceDetails();
						}

						validateContactDetails(contactDetails, expAddressLine1, dummyContactDetails);
						validateAreaOfFocusDetails(areasOFocus, expAddressLine1, dummyAreasOFocus);
						validateOfficeDetails(officeDetails, expAddressLine1, dummyOfficeDetails);
						validateOfficeServices(officeServices, expAddressLine1, dummyOfficeServices);
						validateSchedulingDetails(schedulingDetails, expAddressLine1, dummySchedDetails);
						validateRemittanceDetails(remittanceDetails, expAddressLine1, dummyRemitDetails);

						break;
					}
					i++;
				}
				if (addressFlag == false)
					resultList.add(
							new ResultTuple("Address Record", "", expAddrRecord, FAILED, "Address record is missing"));
			}
		}
	}

	public void validateContactDetails(List<ContactDetail> contactList, String addressLine1,
			List<ContactDetail> dummyContactList) {

		String firstName, middleName, lastName, suffix, profTitleCode, fax, phone, email, webAddress;
		String expContactRecord;
		Boolean contactFlag = false;

		firstName = recordHashMap.get("addressadditionaldetails_contactdetails_firstname").toString().trim();
		middleName = recordHashMap.get("addressadditionaldetails_contactdetails_middlename").toString().trim();
		lastName = recordHashMap.get("addressadditionaldetails_contactdetails_lastname").toString().trim();
		suffix = recordHashMap.get("addressadditionaldetails_contactdetails_suffix").toString().trim();
		profTitleCode = recordHashMap.get("addressadditionaldetails_contactdetails_professionaltitlecodes").toString()
				.trim();
		fax = recordHashMap.get("addressadditionaldetails_contactdetails_fax").toString().trim();
		phone = recordHashMap.get("addressadditionaldetails_contactdetails_phone").toString().trim();
		email = recordHashMap.get("addressadditionaldetails_contactdetails_email").toString().trim();
		webAddress = recordHashMap.get("addressadditionaldetails_contactdetails_webaddress").toString().trim();

		expContactRecord = firstName + " : " + middleName + " : " + lastName + " : " + suffix + " : " + profTitleCode
				+ " : " + fax + " : " + phone + " : " + email + " : " + webAddress;

		if (firstName == emptyStr && middleName == emptyStr && lastName == emptyStr && suffix == emptyStr
				&& profTitleCode == emptyStr && fax == emptyStr && phone == emptyStr && email == emptyStr
				&& webAddress == emptyStr) {
			if (contactList != null) {
				resultList.add(new ResultTuple("Contact Node", "non null", "null", FAILED,
						"Contact node is not empty for Address : " + addressLine1));
			} else {
				System.out.println("Ok no more contact node validation");
			}
		} else {
			if (contactList == null) {
				resultList.add(new ResultTuple("Contact Node", "null", expContactRecord, FAILED,
						"Contact node is missing for Address : " + addressLine1));
			} else {
				int i = 0;
				for (ContactDetail contactDetail : contactList) {
					if (firstName.equalsIgnoreCase(Objects.toString(contactDetail.getFirstName(), ""))
							&& middleName.equalsIgnoreCase(Objects.toString(contactDetail.getMiddleName(), ""))
							&& lastName.equalsIgnoreCase(Objects.toString(contactDetail.getLastName(), ""))
							&& suffix.equalsIgnoreCase(Objects.toString(contactDetail.getSuffix(), ""))
							&& Objects.toString(contactDetail.getProfessionalTitleCodes(), "").contains(profTitleCode)
							&& fax.equalsIgnoreCase(Objects.toString(contactDetail.getFax(), ""))
							&& phone.equalsIgnoreCase(Objects.toString(contactDetail.getPhone(), ""))
							&& email.equalsIgnoreCase(Objects.toString(contactDetail.getEmail(), ""))
							&& webAddress.equalsIgnoreCase(Objects.toString(contactDetail.getWebAddress(), ""))) {

						contactFlag = true;
						setContactDetails(dummyContactList.get(i));
						break;
					}
					i++;
				}
				if (contactFlag == false)
					resultList.add(new ResultTuple("Contact record", "", expContactRecord, FAILED,
							"Contact record is missing for Address : " + addressLine1));
			}
		}
	}

	public void validateAreaOfFocusDetails(List<AreasOFocus> areasOfFocus, String addressLine1,
			List<AreasOFocus> dummyAreasOfFocus) {

		String areaOfFocus, effDate, termDate, termReasonCode;
		String expAreaOfFocus;
		Boolean areaOfFocusFlag = false;

		areaOfFocus = recordHashMap.get("addressadditionaldetails_areasoffocus_areasoffocus").toString().trim();
		effDate = recordHashMap.get("addressadditionaldetails_areasoffocus_areaoffocusactive_effectivedate").toString()
				.trim();
		termDate = recordHashMap.get("addressadditionaldetails_areasoffocus_areaoffocusactive_terminationdate")
				.toString().trim();
		termReasonCode = recordHashMap
				.get("addressadditionaldetails_areasoffocus_areaoffocusactive_terminationreasoncode").toString().trim();

		expAreaOfFocus = areaOfFocus + effDate + termDate + termReasonCode;

		if (areaOfFocus == emptyStr && effDate == emptyStr && termDate == emptyStr && termReasonCode == emptyStr) {
			if (areasOfFocus != null) {
				resultList.add(new ResultTuple("Area of Focus node", "non null", "null", FAILED,
						"Area of Focus node is not empty for Address : " + addressLine1));
			} else {
				System.out.println("Ok no more Area of Focus node validation");
			}
		} else {
			if (areasOfFocus == null) {
				resultList.add(new ResultTuple("Area of Focus node", "null", expAreaOfFocus, FAILED,
						"Area of Focus node is missing in Json for Address : " + addressLine1));
			} else {
				int i = 0;
				for (AreasOFocus areaFocusRec : areasOfFocus) {
					if (areaOfFocus.trim().equalsIgnoreCase(areaFocusRec.getAreaOfFocus().trim())
							&& effDate.trim()
									.equalsIgnoreCase(areaFocusRec.getAreaOfFocusActive().getEffectiveDate().trim())
							&& termDate.trim()
									.equalsIgnoreCase(areaFocusRec.getAreaOfFocusActive().getTerminationDate().trim())
							&& termReasonCode.trim().equalsIgnoreCase(
									areaFocusRec.getAreaOfFocusActive().getTerminationReasonCode().trim())) {

						areaOfFocusFlag = true;
						setAreasOFocusDetails(dummyAreasOfFocus.get(i));
						break;
					}
					i++;
				}
				if (areaOfFocusFlag == false)
					resultList.add(new ResultTuple("Area of Focus record", "null", expAreaOfFocus, FAILED,
							"Mismatch in Area of Focus record for Address : " + addressLine1));
			}
		}
	}

	public void validateOfficeDetails(List<OfficeDetail> officeDetails, String addressLine1,
			List<OfficeDetail> dummyOfficeDetails) {

		String accessCode, staffLanguage, acceptPatients, officeTech, officeRestrict, officeSystemName,
				officeServiceCertifCode;
		String expOffDetailRecord;
		Boolean officeDetailsFlag = false;

		accessCode = recordHashMap.get("addressadditionaldetails_officedetails_accessibilitycode").toString().trim();
		staffLanguage = recordHashMap.get("addressadditionaldetails_officedetails_stafflanguage").toString().trim();
		acceptPatients = recordHashMap.get("addressadditionaldetails_officedetails_acceptingpatients").toString()
				.trim();
		officeTech = recordHashMap.get("addressadditionaldetails_officedetails_officetechnology").toString().trim();
		officeRestrict = recordHashMap.get("addressadditionaldetails_officedetails_officerestriction").toString()
				.trim();
		officeSystemName = recordHashMap.get("addressadditionaldetails_officedetails_officesystemname").toString()
				.trim();
		officeServiceCertifCode = recordHashMap
				.get("addressadditionaldetails_officedetails_officeservicecertificationcode").toString().trim();

		expOffDetailRecord = accessCode + " : " + staffLanguage + " : " + acceptPatients + " : " + officeTech + " : "
				+ officeRestrict + " : " + officeSystemName + " : " + officeServiceCertifCode;

		if (accessCode == emptyStr && staffLanguage == emptyStr && acceptPatients == emptyStr && officeTech == emptyStr
				&& officeRestrict == emptyStr && officeSystemName == emptyStr && officeServiceCertifCode == emptyStr) {
			if (officeDetails != null) {
				resultList.add(new ResultTuple("Office Details node", "non null", "null", FAILED,
						"Office Details node is not empty for Address : " + addressLine1));
			} else {
				System.out.println("Ok no more Office Details node validation");
			}
		} else {
			if (officeDetails == null) {
				resultList.add(new ResultTuple("Office Details node", "null", expOffDetailRecord, FAILED,
						"Office Details node is missing for Address : " + addressLine1));
			} else {
				int i = 0;
				for (OfficeDetail officeDetail : officeDetails) {
					if (isContainedInString(accessCode, Objects.toString(officeDetail.getAccessibilityCode(), ""))
							&& isContainedInString(staffLanguage, Objects.toString(officeDetail.getStaffLanguage(), ""))
							&& acceptPatients
									.equalsIgnoreCase(Objects.toString(officeDetail.getAcceptingPatients(), ""))
							&& isContainedInString(officeTech, Objects.toString(officeDetail.getOfficeTechnology(), ""))
							&& isContainedInString(officeRestrict,
									Objects.toString(officeDetail.getOfficeRestriction(), ""))
							&& isContainedInString(officeSystemName,
									Objects.toString(officeDetail.getOfficeSystemName(), ""))
							&& isContainedInString(officeServiceCertifCode,
									Objects.toString(officeDetail.getOfficeServiceCertificationCode(), ""))) {

						officeDetailsFlag = true;
						setOfficeDetail(dummyOfficeDetails.get(i));
						break;
					}
					i++;
				}
			}
			if (officeDetailsFlag == false)
				resultList.add(new ResultTuple("Office Detail record", "", expOffDetailRecord, FAILED,
						"Office Detail record is missing for Address : " + addressLine1));
		}
	}

	public void validateOfficeServices(List<String> officeServices, String addressLine1,
			List<String> dummyOfficeServices) {

		String offServiceType, expOffServiceType;

		expOffServiceType = recordHashMap.get("addressadditionaldetails_officeservicetypes").toString().trim();
		offServiceType = Objects.toString(officeServices, "");

		containedInString("Office Services", expOffServiceType, offServiceType);
		dummyOfficeServices = Arrays.asList("TRUE");
	}

	public void validateSchedulingDetails(List<SchedulingDetail> schedulingDetails, String addressLine1,
			List<SchedulingDetail> dummySchedDetails) {

		String days, openTime, closeTime;
		String expSchedulingRec;
		Boolean schedulingDetailsFlag = false;

		days = recordHashMap.get("addressadditionaldetails_schedulingdetails_days").toString().trim();
		openTime = recordHashMap.get("addressadditionaldetails_schedulingdetails_opentime").toString().trim();
		closeTime = recordHashMap.get("addressadditionaldetails_schedulingdetails_closetime").toString().trim();

		expSchedulingRec = days + " : " + openTime + " : " + closeTime;

		if (days == emptyStr && openTime == emptyStr && closeTime == emptyStr) {
			if (schedulingDetails != null) {
				resultList.add(new ResultTuple("Scheduling Details node", "non null", "null", FAILED,
						"Scheduling Details node is not empty for Address : " + addressLine1));
			} else {
				System.out.println("Ok no more Scheduling Details node validation");
			}
		} else {
			if (schedulingDetails == null) {
				resultList.add(new ResultTuple("scheduling Details node", "null", expSchedulingRec, FAILED,
						"scheduling Details node is missing for Address : " + addressLine1));
			} else {
				int i = 0;
				for (SchedulingDetail schedulingDetail : schedulingDetails) {
					if (days.equalsIgnoreCase(Objects.toString(schedulingDetail.getDays(), ""))
							&& openTime.equalsIgnoreCase(Objects.toString(schedulingDetail.getOpenTime(), ""))
							&& closeTime.equalsIgnoreCase(Objects.toString(schedulingDetail.getCloseTime(), ""))) {

						schedulingDetailsFlag = true;
						setSchedulingDetail(dummySchedDetails.get(i));
						break;
					}
					i++;
				}
				if (schedulingDetailsFlag == false)
					resultList.add(new ResultTuple("Scheduling Detail record", "", expSchedulingRec, FAILED,
							"Scheduling Detail record is missing for Address : " + addressLine1));
			}
		}
	}

//	public void validateSpecialPrograms(List<SpecialProgram> specialprograms, String addressLine1) {
//
//		String providerSpecialProgramType, providerSpecialProgramEffDate, providerSpecialProgramtermDate,
//				providerSpecialProgramTermReasonCode;
//		String expspecialprogramsRecord;
//		Boolean specialprogramsFlag = false;
//
//		providerSpecialProgramType = recordHashMap
//				.get("addressadditionaldetails_specialprograms_providerspecialprogramtype").toString().trim();
//		providerSpecialProgramEffDate = recordHashMap
//				.get("addressadditionaldetails_specialprograms_programactive_effectivedate").toString().trim();
//		providerSpecialProgramtermDate = recordHashMap
//				.get("addressadditionaldetails_specialprograms_programactive_terminationdate").toString().trim();
//		providerSpecialProgramTermReasonCode = recordHashMap
//				.get("addressadditionaldetails_specialprograms_programactive_terminationreasoncode").toString().trim();
//		expspecialprogramsRecord = providerSpecialProgramType + providerSpecialProgramEffDate
//				+ providerSpecialProgramtermDate + providerSpecialProgramTermReasonCode;
//
//		if (providerSpecialProgramType == emptyStr && providerSpecialProgramEffDate == emptyStr
//				&& providerSpecialProgramtermDate == emptyStr && providerSpecialProgramTermReasonCode == emptyStr) {
//			if (specialprograms != null) {
//				resultList.add(new ResultTuple("specialprograms Node", "non null", "null", FAILED,
//						"specialprograms node is not empty"));
//			} else {
//				System.out.println("Ok no more specialprograms node validation");
//			}
//		} else {
//			if (specialprograms == null) {
//				resultList.add(new ResultTuple("specialprograms Node", "null", "non null", FAILED,
//						"specialprograms node is missing"));
//			} else {
//				for (SpecialProgram sp : specialprograms) {
//
//					if (providerSpecialProgramType
//							.equalsIgnoreCase(Objects.toString(sp.getProviderSpecialProgramType(), ""))
//							&& providerSpecialProgramEffDate
//									.equalsIgnoreCase(Objects.toString(sp.getProgramActive().getEffectiveDate(), ""))
//							&& providerSpecialProgramtermDate
//									.equalsIgnoreCase(Objects.toString(sp.getProgramActive().getTerminationDate(), ""))
//							&& providerSpecialProgramTermReasonCode.equalsIgnoreCase(
//									Objects.toString(sp.getProgramActive().getTerminationReasonCode(), ""))) {
//
//						specialprogramsFlag = true;
//						break;
//					}
//				}
//				if (specialprogramsFlag == false)
//					resultList.add(new ResultTuple("specialprograms record", "", expspecialprogramsRecord, FAILED,
//							"specialprograms record is missing"));
//			}
//		}
//	}

	public void validateRemittanceDetails(List<RemittanceDetail> remitDetails, String addressLine1,
			List<RemittanceDetail> dummyRemitDetails) {

		String checkName, npiEligibilityType, npiType, npiValue, remitEffDate, remitTermDate, remitTermReasonCode;
		String actNpiEligibilityType = "", actNpiType = "", actNpiValue = "";
		String expRemitRecord;
		Boolean remittanceFlag = false;

		checkName = recordHashMap.get("addressadditionaldetails_remittancedetails_checkname").toString().trim();
		npiEligibilityType = recordHashMap
				.get("addressadditionaldetails_remittancedetails_remittancenpi_npieligibilitytype").toString().trim();
		npiType = recordHashMap.get("addressadditionaldetails_remittancedetails_remittancenpi_npitype").toString()
				.trim();
		npiValue = recordHashMap.get("addressadditionaldetails_remittancedetails_remittancenpi_npivalue").toString()
				.trim();
		remitEffDate = recordHashMap.get("addressadditionaldetails_remittancedetails_remittanceactive_effectivedate")
				.toString().trim();
		remitTermDate = recordHashMap.get("addressadditionaldetails_remittancedetails_remittanceactive_terminationdate")
				.toString().trim();
		remitTermReasonCode = recordHashMap
				.get("addressadditionaldetails_remittancedetails_remittanceactive_terminationreasoncode").toString()
				.trim();

		expRemitRecord = checkName + " : " + npiEligibilityType + " : " + npiType + " : " + npiValue + " : "
				+ remitEffDate + " : " + remitTermDate + " : " + remitTermReasonCode;

		if (checkName == emptyStr && npiEligibilityType == emptyStr && npiType == emptyStr && npiValue == emptyStr
				&& remitEffDate == emptyStr && remitTermDate == emptyStr && remitTermReasonCode == emptyStr) {
			if (remitDetails != null) {
				resultList.add(new ResultTuple("Remittance Detail node", "non null", "null", FAILED,
						"Remittance Detail node is not empty for Address : " + addressLine1));
			} else {
				System.out.println("Ok no more Remittance Detail node validation");
			}
		} else {
			if (remitDetails == null) {
				resultList.add(new ResultTuple("Remittance Detail node", "null", expRemitRecord, FAILED,
						"Remittance Detail node is missing for Address : " + addressLine1));
			} else {
				int i = 0;
				for (RemittanceDetail remitDetail : remitDetails) {
					RemittanceNPI remitNpi = remitDetail.getRemittanceNPI();
					if (remitNpi != null) {
						actNpiEligibilityType = Objects.toString(remitNpi.getNpiEligibilityType(), "");
						actNpiType = Objects.toString(remitNpi.getNpiType(), "");
						actNpiValue = Objects.toString(remitNpi.getNpiValue(), "");
					}

					if (checkName.equalsIgnoreCase(Objects.toString(remitDetail.getCheckName(), ""))
							&& npiEligibilityType.equalsIgnoreCase(actNpiEligibilityType)
							&& npiType.equalsIgnoreCase(actNpiType) && npiValue.equalsIgnoreCase(actNpiValue)
							&& remitEffDate.equalsIgnoreCase(
									Objects.toString(remitDetail.getRemittanceActive().getEffectiveDate(), ""))
							&& remitTermDate.equalsIgnoreCase(
									Objects.toString(remitDetail.getRemittanceActive().getTerminationDate(), ""))
							&& remitTermReasonCode.equalsIgnoreCase(Objects
									.toString(remitDetail.getRemittanceActive().getTerminationReasonCode(), ""))) {

						remittanceFlag = true;
						setRemittanceDetail(dummyRemitDetails.get(i));
						break;
					}
					i++;
				}
				if (remittanceFlag == false)
					resultList.add(new ResultTuple("Remittance Detail record", "", expRemitRecord, FAILED,
							"Remittance Detail record is missing for Address : " + addressLine1));
			}
		}
	}

	public void setAddress(Address dummyAddress) {

		if (dummyAddress.getAddressDetails() != null) {
			dummyAddress.getAddressDetails().setAddressType(TRUE);
			dummyAddress.getAddressDetails().setAddressLine1(TRUE);
			dummyAddress.getAddressDetails().setAddressLine2(TRUE);
			dummyAddress.getAddressDetails().setAddressLine3(TRUE);
			dummyAddress.getAddressDetails().setCounty(TRUE);
			dummyAddress.getAddressDetails().setCity(TRUE);
			dummyAddress.getAddressDetails().setState(TRUE);
			dummyAddress.getAddressDetails().setZip(TRUE);
			dummyAddress.getAddressDetails().setCityVanityName(TRUE);
		}
		if (dummyAddress.getAddressActive() != null) {
			dummyAddress.getAddressActive().setEffectiveDate(TRUE);
			dummyAddress.getAddressActive().setTerminationDate(TRUE);
			dummyAddress.getAddressActive().setTerminationReasonCode(TRUE);
		}
	}

	public void setContactDetails(ContactDetail dummyContact) {

		String profTitleCode = recordHashMap.get("addressadditionaldetails_contactdetails_professionaltitlecodes")
				.toString().trim();

		dummyContact.setFirstName(TRUE);
		dummyContact.setLastName(TRUE);
		dummyContact.setMiddleName(TRUE);
		dummyContact.setSuffix(TRUE);

		List<String> profTitleList = dummyContact.getProfessionalTitleCodes();
		if (profTitleList != null) {
			profTitleList.remove(profTitleCode);
			dummyContact.setProfessionalTitleCodes(profTitleList);
		}

		dummyContact.setFax(TRUE);
		dummyContact.setPhone(TRUE);
		dummyContact.setEmail(TRUE);
		dummyContact.setWebAddress(TRUE);
	}

	public void setAreasOFocusDetails(AreasOFocus dummyAreaOfFocus) {

		dummyAreaOfFocus.setAreaOfFocus(TRUE);
		if (dummyAreaOfFocus.getAreaOfFocusActive() != null) {
			dummyAreaOfFocus.getAreaOfFocusActive().setEffectiveDate(TRUE);
			dummyAreaOfFocus.getAreaOfFocusActive().setTerminationDate(TRUE);
			dummyAreaOfFocus.getAreaOfFocusActive().setTerminationReasonCode(TRUE);
		}
	}

	public void setOfficeDetail(OfficeDetail dummyOfficeDetail) {

		String accessCode, staffLanguage, officeTech, officeRestrict, officeSystemName, officeServiceCertifCode;

		accessCode = recordHashMap.get("addressadditionaldetails_officedetails_accessibilitycode").toString().trim();
		staffLanguage = recordHashMap.get("addressadditionaldetails_officedetails_stafflanguage").toString().trim();
		officeTech = recordHashMap.get("addressadditionaldetails_officedetails_officetechnology").toString().trim();
		officeRestrict = recordHashMap.get("addressadditionaldetails_officedetails_officerestriction").toString()
				.trim();
		officeSystemName = recordHashMap.get("addressadditionaldetails_officedetails_officesystemname").toString()
				.trim();
		officeServiceCertifCode = recordHashMap
				.get("addressadditionaldetails_officedetails_officeservicecertificationcode").toString().trim();

		List<String> offcDetailList = dummyOfficeDetail.getAccessibilityCode();
		if (offcDetailList != null) {
			offcDetailList.remove(accessCode);
			dummyOfficeDetail.setAccessibilityCode(offcDetailList);
		}

		dummyOfficeDetail.setAcceptingPatients(TRUE);

		offcDetailList = dummyOfficeDetail.getOfficeRestriction();
		if (offcDetailList != null) {
			offcDetailList.remove(officeRestrict);
			dummyOfficeDetail.setOfficeRestriction(offcDetailList);
		}

		offcDetailList = dummyOfficeDetail.getOfficeServiceCertificationCode();
		if (offcDetailList != null) {
			offcDetailList.remove(officeServiceCertifCode);
			dummyOfficeDetail.setOfficeServiceCertificationCode(offcDetailList);
		}

		offcDetailList = dummyOfficeDetail.getOfficeSystemName();
		if (offcDetailList != null) {
			offcDetailList.remove(officeSystemName);
			dummyOfficeDetail.setOfficeSystemName(offcDetailList);
		}

		offcDetailList = dummyOfficeDetail.getOfficeTechnology();
		if (offcDetailList != null) {
			offcDetailList.remove(officeTech);
			dummyOfficeDetail.setOfficeTechnology(offcDetailList);
		}

		offcDetailList = dummyOfficeDetail.getStaffLanguage();
		if (offcDetailList != null) {
			offcDetailList.remove(staffLanguage);
			dummyOfficeDetail.setStaffLanguage(offcDetailList);
		}
	}

	public void setSchedulingDetail(SchedulingDetail dummySchedDetail) {

		dummySchedDetail.setDays(TRUE);
		dummySchedDetail.setOpenTime(TRUE);
		dummySchedDetail.setCloseTime(TRUE);
	}

	public void setRemittanceDetail(RemittanceDetail dummyRemitDetail) {

		dummyRemitDetail.setCheckName(TRUE);
		if (dummyRemitDetail.getRemittanceNPI() != null) {
			dummyRemitDetail.getRemittanceNPI().setNpiEligibilityType(TRUE);
			dummyRemitDetail.getRemittanceNPI().setNpiType(TRUE);
			dummyRemitDetail.getRemittanceNPI().setNpiValue(TRUE);
		}
		if (dummyRemitDetail.getRemittanceActive() != null) {
			dummyRemitDetail.getRemittanceActive().setEffectiveDate(TRUE);
			dummyRemitDetail.getRemittanceActive().setTerminationDate(TRUE);
			dummyRemitDetail.getRemittanceActive().setTerminationReasonCode(TRUE);
		}
	}

}
