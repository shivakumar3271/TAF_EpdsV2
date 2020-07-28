package com.tests.mdxjson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.AliasName;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.GroupingNameQualifier;
import com.beans.mdxjson.Profile;
import com.beans.mdxjson.ProfileName;
import com.beans.mdxjson.TaxID;
import com.utilities.mdxjson.ResultTuple;

public class ProfileValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(ProfileValidation.class);

	LinkedHashMap<String, Object> recordHashMap;

	public void validateProvider(LinkedHashMap<String, Object> recordHashMap, Profile profile, Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String provType = "", provEffDate = "", provTermDate = "", provTermReasonCode = "";
		String expProvType, expProvEffDate, expProvTermDate, expProvTermReasonCode;

		expProvType = recordHashMap.get("profile_providertype").toString().trim();
		expProvEffDate = recordHashMap.get("profile_provideractive_effectivedate").toString().trim();
		expProvTermDate = recordHashMap.get("profile_provideractive_terminationdate").toString().trim();
		expProvTermReasonCode = recordHashMap.get("profile_provideractive_terminationreasoncode").toString().trim();

		if (profile != null) {
			provType = Objects.toString(profile.getProviderType(), "");
			if (profile.getProviderActive() != null) {
				provEffDate = Objects.toString(profile.getProviderActive().getEffectiveDate(), "");
				provTermDate = Objects.toString(profile.getProviderActive().getTerminationDate(), "");
				provTermReasonCode = Objects.toString(profile.getProviderActive().getTerminationReasonCode(), "");
			}
			setProfile(dummyProfile);
		}

		compareStrings("Provider Type", expProvType, provType);
		compareStrings("Prov Eff Date", expProvEffDate, provEffDate);
		compareStrings("Prov Term Date", expProvTermDate, provTermDate);
		compareStrings("Prov Term Reason", expProvTermReasonCode, provTermReasonCode);
	}

	public void validateGrpNameQualifier(LinkedHashMap<String, Object> recordHashMap, Profile profile,
			Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String grpType = "", grpName = "", grpSiteCode = "";
		String expGrpType, expGrpName, expGrpSiteCode;

		expGrpType = recordHashMap.get("profile_namequalifier_groupingnamequalifier_groupingtype").toString().trim();
		expGrpName = recordHashMap.get("profile_namequalifier_groupingnamequalifier_groupingname").toString().trim();
		expGrpSiteCode = recordHashMap.get("profile_namequalifier_groupingnamequalifier_groupingsitecode").toString()
				.trim();

		if (profile != null && profile.getNameQualifier() != null) {
			GroupingNameQualifier grpNameQualifier = profile.getNameQualifier().getGroupingNameQualifier();
			if (grpNameQualifier != null) {
				grpType = Objects.toString(grpNameQualifier.getGroupingType(), "");
				grpName = Objects.toString(grpNameQualifier.getGroupingName(), "");
				grpSiteCode = Objects.toString(grpNameQualifier.getGroupingSiteCode(), "");

				setGrpNameQualifier(dummyProfile.getNameQualifier().getGroupingNameQualifier());
			}
		}

		compareStrings("Grouping Type", expGrpType, grpType);
		compareStrings("Grouping Name", expGrpName, grpName);
		compareStrings("Grouping Site Code", expGrpSiteCode, grpSiteCode);
	}

	public void validateIndNameQualifier(LinkedHashMap<String, Object> recordHashMap, Profile profile,
			Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String fName = "", mName = "", lName = "", suffix = "", profTitleCode = "", dob = "", gender = "",
				ethnicity = "";
		String expFName, expMName, expLName, expSuffix, expProfTitleCode, expDob, expGender, expEthnicity;

		expFName = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_firstname").toString()
				.trim();
		expMName = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_middlename").toString()
				.trim();
		expLName = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_lastname").toString()
				.trim();
		expSuffix = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_suffix").toString()
				.trim();
		expProfTitleCode = recordHashMap
				.get("profile_namequalifier_individualnamequalifier_profilename_professionaltitlecodes").toString()
				.trim();
		expDob = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_dateofbirth").toString()
				.trim();
		expGender = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_gender").toString()
				.trim();
		expEthnicity = recordHashMap.get("profile_namequalifier_individualnamequalifier_profilename_ethnicity")
				.toString().trim();

		if (profile != null && profile.getNameQualifier() != null
				&& profile.getNameQualifier().getIndividualNameQualifier() != null) {
			ProfileName profileName = profile.getNameQualifier().getIndividualNameQualifier().getProfileName();
			if (profileName != null) {
				fName = Objects.toString(profileName.getFirstName(), "");
				mName = Objects.toString(profileName.getMiddleName(), "");
				lName = Objects.toString(profileName.getLastName(), "");
				suffix = Objects.toString(profileName.getSuffix(), "");
				profTitleCode = Objects.toString(profileName.getProfessionalTitleCodes(), "");
				dob = Objects.toString(profileName.getDateOfBirth(), "");
				gender = Objects.toString(profileName.getGender(), "");
				ethnicity = Objects.toString(profileName.getEthnicity(), "");

				setProfileName(dummyProfile.getNameQualifier().getIndividualNameQualifier().getProfileName());
			}
		}

		compareStrings("Profile - FName", expFName, fName);
		compareStrings("Profile - MName", expMName, mName);
		compareStrings("Profile - LName", expLName, lName);
		compareStrings("Profile - Suffix", expSuffix, suffix);
		containedInString("Profile - ProfTitleCode", expProfTitleCode, profTitleCode);
		compareStrings("Profile - DOB", expDob, dob);
		compareStrings("Profile - Gender", expGender, gender);
		compareStrings("Profile - Ethnicity", expEthnicity, ethnicity);
	}

	public void validateAliasNames(LinkedHashMap<String, Object> recordHashMap, Profile profile, Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String firstName, middleName, lastName, suffix;
		String expAliasNameRec;
		Boolean aliasNameFlag = false;

		firstName = recordHashMap.get("profile_namequalifier_individualnamequalifier_aliasname_firstname").toString()
				.trim();
		middleName = recordHashMap.get("profile_namequalifier_individualnamequalifier_aliasname_middlename").toString()
				.trim();
		lastName = recordHashMap.get("profile_namequalifier_individualnamequalifier_aliasname_lastname").toString()
				.trim();
		suffix = recordHashMap.get("profile_namequalifier_individualnamequalifier_aliasname_suffix").toString().trim();

		expAliasNameRec = firstName + middleName + lastName + suffix;

		List<AliasName> aliasNameList = null;
		if (profile != null && profile.getNameQualifier() != null
				&& profile.getNameQualifier().getIndividualNameQualifier() != null)
			aliasNameList = profile.getNameQualifier().getIndividualNameQualifier().getAliasNames();

		if (firstName == emptyStr && middleName == emptyStr && lastName == emptyStr && suffix == emptyStr) {
			if (aliasNameList != null) {
				resultList.add(new ResultTuple("Alias Names node", "non null", "null", FAILED,
						"Alias Names node is not empty"));
			} else {
				System.out.println("Ok no more Alias Names node validation");
			}
		} else {
			if (aliasNameList == null) {
				resultList.add(new ResultTuple("Alias Names node", "null", expAliasNameRec, FAILED,
						"Alias Names node is missing"));
			} else {
				int i = 0;
				for (AliasName aliasName : aliasNameList) {
					if (firstName.equalsIgnoreCase(Objects.toString(aliasName.getFirstName(), ""))
							&& middleName.equalsIgnoreCase(Objects.toString(aliasName.getMiddleName(), ""))
							&& lastName.equalsIgnoreCase(Objects.toString(aliasName.getLastName(), ""))
							&& suffix.equalsIgnoreCase(Objects.toString(aliasName.getSuffix(), ""))) {

						aliasNameFlag = true;
						setAliasName(
								dummyProfile.getNameQualifier().getIndividualNameQualifier().getAliasNames().get(i));
						break;
					}
					i++;
				}
				if (aliasNameFlag == false)
					resultList.add(new ResultTuple("Alias Name record", "", expAliasNameRec, FAILED,
							"Alias Name record is missing"));
			}
		}
	}

	public void validateTaxIds(LinkedHashMap<String, Object> recordHashMap, Profile profile, Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String taxIdValue, taxIdInd, taxIdEffDate, taxIdTermDate, taxIdTermReasonCode;
		String expTaxIdRec;
		Boolean taxIdFlag = false;

		taxIdValue = recordHashMap.get("profile_taxids_taxidvalue").toString().trim();
		taxIdInd = recordHashMap.get("profile_taxids_taxidindicator").toString().trim();
		taxIdEffDate = recordHashMap.get("profile_taxids_taxidactive_effectivedate").toString().trim();
		taxIdTermDate = recordHashMap.get("profile_taxids_taxidactive_terminationdate").toString().trim();
		taxIdTermReasonCode = recordHashMap.get("profile_taxids_taxidactive_terminationreasoncode").toString().trim();

		expTaxIdRec = taxIdValue + " : " + taxIdInd + " : " + taxIdEffDate + " : " + taxIdTermDate + " : "
				+ taxIdTermReasonCode;

		List<TaxID> taxIdList = null;
		if (profile != null)
			taxIdList = profile.getTaxIDs();

		if (taxIdValue == emptyStr && taxIdInd == emptyStr && taxIdEffDate == emptyStr && taxIdTermDate == emptyStr
				&& taxIdTermReasonCode == emptyStr) {
			if (taxIdList != null) {
				resultList.add(new ResultTuple("Profile - Tax IDs node", "non null", "null", FAILED,
						"Tax IDs node is not empty"));
			} else {
				System.out.println("Ok no more Profile - Tax IDs node validation");
			}
		} else {
			if (taxIdList == null) {
				resultList.add(new ResultTuple("Profile - Tax IDs node", "null", expTaxIdRec, FAILED,
						"Tax IDs node is missing"));
			} else {
				int i = 0;
				for (TaxID taxId : taxIdList) {
					if (taxIdValue.equalsIgnoreCase(Objects.toString(taxId.getTaxIDValue(), ""))
							&& taxIdInd.equalsIgnoreCase(Objects.toString(taxId.getTaxIDIndicator(), ""))
							&& taxIdEffDate
									.equalsIgnoreCase(Objects.toString(taxId.getTaxIDActive().getEffectiveDate(), ""))
							&& taxIdTermDate
									.equalsIgnoreCase(Objects.toString(taxId.getTaxIDActive().getTerminationDate(), ""))
							&& taxIdTermReasonCode.equalsIgnoreCase(
									Objects.toString(taxId.getTaxIDActive().getTerminationReasonCode(), ""))) {

						taxIdFlag = true;
						setTaxID(dummyProfile.getTaxIDs().get(i));
						break;
					}
					i++;
				}
				if (taxIdFlag == false)
					resultList.add(new ResultTuple("Profile - Tax ID record", "", expTaxIdRec, FAILED,
							"Tax ID record is missing"));
			}
		}
	}

	public void validateAreasOfFocus(LinkedHashMap<String, Object> recordHashMap, Profile profile,
			Profile dummyProfile) {

		this.recordHashMap = recordHashMap;

		String areaOfFocusVal, aofEffDate, aofTermDate, aofTermReasonCode;
		String expAreaOfFocusRec;
		Boolean areaOfFocusFlag = false;

		areaOfFocusVal = recordHashMap.get("profile_areasoffocus_areasoffocus").toString().trim();
		aofEffDate = recordHashMap.get("profile_areasoffocus_areaoffocusactive_effectivedate").toString().trim();
		aofTermDate = recordHashMap.get("profile_areasoffocus_areaoffocusactive_terminationdate").toString().trim();
		aofTermReasonCode = recordHashMap.get("profile_areasoffocus_areaoffocusactive_terminationreasoncode").toString()
				.trim();

		expAreaOfFocusRec = areaOfFocusVal + " : " + aofEffDate + " : " + aofTermDate + " : " + aofTermReasonCode;

		List<AreasOFocus> areasOfFocusList = null;
		if (profile != null)
			areasOfFocusList = profile.getAreasOFocus();

		if (areaOfFocusVal == emptyStr && aofEffDate == emptyStr && aofTermDate == emptyStr
				&& aofTermReasonCode == emptyStr) {
			if (areasOfFocusList != null) {
				resultList.add(new ResultTuple("Profile - Areas of Focus node", "non null", "null", FAILED,
						"Areas of Focus node is not empty"));
			} else {
				System.out.println("Ok no more Profile - Areas of Focus node validation");
			}
		} else {
			if (areasOfFocusList == null) {
				resultList.add(new ResultTuple("Profile - Areas of Focus node", "null", expAreaOfFocusRec, FAILED,
						"Areas of Focus node is missing"));
			} else {
				int i = 0;
				for (AreasOFocus areaOfFocus : areasOfFocusList) {
					if (areaOfFocusVal.equalsIgnoreCase(Objects.toString(areaOfFocus.getAreaOfFocus(), ""))
							&& aofEffDate.equalsIgnoreCase(
									Objects.toString(areaOfFocus.getAreaOfFocusActive().getEffectiveDate(), ""))
							&& aofTermDate.equalsIgnoreCase(
									Objects.toString(areaOfFocus.getAreaOfFocusActive().getTerminationDate(), ""))
							&& aofEffDate.equalsIgnoreCase(Objects
									.toString(areaOfFocus.getAreaOfFocusActive().getTerminationReasonCode(), ""))) {

						areaOfFocusFlag = true;
						setAreasOFocus(dummyProfile.getAreasOFocus().get(i));
						break;
					}
					i++;
				}
				if (areaOfFocusFlag == false)
					resultList.add(new ResultTuple("Profile - Area of Focus record", "", expAreaOfFocusRec, FAILED,
							"Area of Focus record is missing"));
			}
		}
	}

	public void setProfile(Profile dummyProfile) {

		dummyProfile.setProviderType(TRUE);
		dummyProfile.getProviderActive().setEffectiveDate(TRUE);
		dummyProfile.getProviderActive().setTerminationDate(TRUE);
		dummyProfile.getProviderActive().setTerminationReasonCode(TRUE);
	}

	public void setGrpNameQualifier(GroupingNameQualifier dummyGrpNameQualifier) {

		dummyGrpNameQualifier.setGroupingType(TRUE);
		dummyGrpNameQualifier.setGroupingName(TRUE);
		dummyGrpNameQualifier.setGroupingSiteCode(TRUE);
	}

	public void setProfileName(ProfileName dummyProfileName) {

		String expProfTitleCode = recordHashMap
				.get("profile_namequalifier_individualnamequalifier_profilename_professionaltitlecodes").toString()
				.trim();

		dummyProfileName.setFirstName(TRUE);
		dummyProfileName.setMiddleName(TRUE);
		dummyProfileName.setLastName(TRUE);
		dummyProfileName.setSuffix(TRUE);

		List<String> profTitleList = dummyProfileName.getProfessionalTitleCodes();
		if (profTitleList != null) {
			profTitleList.remove(expProfTitleCode);
			dummyProfileName.setProfessionalTitleCodes(profTitleList);
		}

		dummyProfileName.setDateOfBirth(TRUE);
		dummyProfileName.setGender(TRUE);
		dummyProfileName.setEthnicity(TRUE);
	}

	public void setAliasName(AliasName dummyAliasName) {

		dummyAliasName.setFirstName(TRUE);
		dummyAliasName.setMiddleName(TRUE);
		dummyAliasName.setLastName(TRUE);
		dummyAliasName.setSuffix(TRUE);
	}

	public void setTaxID(TaxID dummyTaxId) {

		dummyTaxId.setTaxIDValue(TRUE);
		dummyTaxId.setTaxIDIndicator(TRUE);
		dummyTaxId.getTaxIDActive().setEffectiveDate(TRUE);
		dummyTaxId.getTaxIDActive().setTerminationDate(TRUE);
		dummyTaxId.getTaxIDActive().setTerminationReasonCode(TRUE);
	}

	public void setAreasOFocus(AreasOFocus dummyAreaOfFocus) {

		dummyAreaOfFocus.setAreaOfFocus(TRUE);
		dummyAreaOfFocus.getAreaOfFocusActive().setEffectiveDate(TRUE);
		dummyAreaOfFocus.getAreaOfFocusActive().setTerminationDate(TRUE);
		dummyAreaOfFocus.getAreaOfFocusActive().setTerminationReasonCode(TRUE);
	}

}
