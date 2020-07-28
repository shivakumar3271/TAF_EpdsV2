package com.tests.epds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.beans.mdxjson.AcceptingPatientsInd;
import com.beans.mdxjson.Address;
import com.beans.mdxjson.Affiliation;
import com.beans.mdxjson.AliasName;
import com.beans.mdxjson.AreasOFocus;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.EducationDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.ProfileName;
import com.beans.mdxjson.ProviderActive;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.SpecialProgram;
import com.beans.mdxjson.Specialty;
import com.beans.mdxjson.TimelyFilingInd;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AffiliationsRelationships;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.Education;
import com.pages.EPDSPageObjects.LoginPage;
import com.pages.EPDSPageObjects.ProfilePage;
import com.pages.EPDSPageObjects.ProviderSearch;
import com.pages.EPDSPageObjects.ReimbusermentNetworks;
import com.pages.EPDSPageObjects.SpecialityTaxonomy;
import com.scripted.web.BrowserDriver;
import com.utilities.epds.EpdsConstants;
import com.utilities.epds.ResultTuple;
import com.utilities.epds.RpaTestUtilities.ProviderType;
import com.utilities.epds.RpaTestUtilities.TestDetails;

public class AddIndividualEntireFlow extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AddIndividualEntireFlow.class);

	@Test(description = "SPDS-133070- Add Individual - new provider - entire flow", enabled = true, groups = { "Ind" })
	@TestDetails(userstory = "SPDS-133070", author = "Shiva", transactionId = "IND_NEW_PROV_ENTIRE_FLOW", sprint = "Sprint 2")
	public void IND_NEW_PROV_ENTIRE_FLOW() {

		LOGGER.info("Inside test IND_NEW_PROV_ENTIRE_FLOW");
		TestDetails testInfo = new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(TestDetails.class);

		List<String> jsonExtractList = new ArrayList<String>();
		String result = "", comment = "";

		jsonExtractList = util.retrieveJsonExtracts(mdxDbConn, testInfo.transactionId());
		if (jsonExtractList == null || jsonExtractList.isEmpty())
			LOGGER.info("No Json extracts were retrieved for transaction : " + testInfo.transactionId());
		else {
			for (String jsonExtract : jsonExtractList) {
				RPAJsonTransaction json = null;
				List<ResultTuple> resultSet = null;

				try {
					launchBrowser();
					WebDriver driver = BrowserDriver.getDriver();
					LoginPage loginPage = new LoginPage(driver);
					ProviderSearch providerSearch = new ProviderSearch(driver);
					AlternateId altIdPage = new AlternateId(driver);
					ProfilePage profilePage = new ProfilePage(driver);
					AddressPage addressPage = new AddressPage(driver);
					AffiliationsRelationships affilRelationPage = new AffiliationsRelationships(driver);
					SpecialityTaxonomy spltyTxnmyPage = new SpecialityTaxonomy(driver);
					Education educationPage = new Education(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);

					json = util.jacksonParser(jsonExtract);
					resultSet = new ArrayList<ResultTuple>();

					loginPage.loginToEpdsv2(epdsPropertyMap.get("epdsv2_username"), epdsPropertyMap.get("epdsv2_pwd"));
					LOGGER.info("Successfully logged in to EPDSv2 UI...");

					/*
					 * List<String> spsIdList = json.getHeader().getSpsIDs(); String spsIds = "";
					 * for (String spsId : spsIdList) { spsIds = spsIds + "'" + spsId + "',"; }
					 * spsIds = spsIds.substring(0, spsIds.length() - 1);
					 * 
					 * String altIdSrc, altIdType; String expAltIdSrc = "", expAltIdType = "",
					 * expAltIdVal = ""; boolean flag = false;
					 * 
					 * altIdSrc = "EPDSV2.0"; altIdType = "Legacy ID";
					 * 
					 * String query = SPSQueries.SPS_ORG_ALT_ID_QUERY; query =
					 * query.replaceAll("<SPS_EID>", spsIds); ResultSet rs =
					 * dbUtil.executeQuery(conn, query);
					 * 
					 * try { while (rs.next()) { expAltIdSrc = rs.getString("ALTID_SOR_CD");
					 * expAltIdType = rs.getString("ALTID_TYPE_CD"); if
					 * (expAltIdSrc.equalsIgnoreCase(altIdSrc) &&
					 * expAltIdType.equalsIgnoreCase(altIdType)) { expAltIdVal =
					 * rs.getString("ALTID_TXT"); flag = true; } } rs.beforeFirst(); if (!flag) {
					 * resultSet.put("EPDS Enterprise ID", new ResultTuple("", ""));
					 * 
					 * } } catch (Exception e) { System.out.println(e); }
					 */

					String entID = json.getHeader().getLegacyID();
					if (!providerSearch.searchAndSelectIndividual(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}

					// ******Profile Tab
					LOGGER.info("Inside PROFILE PAGE");
					String indActProfileEffDate = profilePage.getIndEffectiveDate();
					String indActProfileTermDate = profilePage.getIndTerminationDate();
					String indActProfileTermRsnCode = profilePage.getIndTermReasonCode();
					String indActProfileFirstName = profilePage.getIndProvFirstname();
					String indActProfileMiddleName = profilePage.getIndProvMidname();
					String indActProfileLastName = profilePage.getIndProvLastname();
					String indActProfileSuffix = profilePage.getIndProvSuffix();
					String indActProfileTitleCode = profilePage.getIndProfessionalTitle();
					String indActProfileDOB = profilePage.getIndProvDOB();
					String indActProfileGender = profilePage.getIndProvGender();

					String indActProfileAliasFirstName = profilePage.getIndAliasFirstName();
					String indActProfileAliasMiddleName = profilePage.getIndAliasMiddleName();
					String indActProfileAliasLastName = profilePage.getIndAliasLastName();
					String indActProfileAliasSuffix = profilePage.getIndAliasSuffix();

					ProfileName indProfileDetails = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName();
					ProviderActive indProfileActive = json.getProfile().getProviderActive();

					// Termination date and reason comparison
					LOGGER.info(
							"Validating Termination date, Effective date,Termination reason code in Individual profile page ");
					String expIndTermDate = indProfileActive.getTerminationDate();
					String expIndTermRsnCode = Objects.toString(indProfileActive.getTerminationReasonCode(), "");
					String expIndEffDate = indProfileActive.getEffectiveDate();

					compareValues(indActProfileTermDate, expIndTermDate, "Termination Date");
					compareValues(indActProfileTermRsnCode, expIndTermRsnCode, "Termination Reason Code");
					compareValues(indActProfileEffDate, expIndEffDate, "Effective date ");

					// NAME comparison
					LOGGER.info("Validating names in Individual profile page ");
					String expIndFname = indProfileDetails.getFirstName();
					String expIndMname = Objects.toString(indProfileDetails.getMiddleName(), "");
					String expIndLname = indProfileDetails.getLastName();
					String expIndSuffix = Objects.toString(indProfileDetails.getSuffix(), "");

					compareValues(indActProfileFirstName, expIndFname, "FirstName");
					compareValues(indActProfileMiddleName, expIndMname, "Middle Name");
					compareValues(indActProfileLastName, expIndLname, "LastName");
					compareValues(indActProfileSuffix, expIndSuffix, "Suffix");

					// Validating professional title
					LOGGER.info("Validating professional title in Individual profile page");
					List<String> expProfTitleCodes = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getProfileName().getProfessionalTitleCodes();
					if (expProfTitleCodes != null) {
						for (String expProfTitleCode : expProfTitleCodes) {
							Boolean status = indActProfileTitleCode.contains(expProfTitleCode);
							resultChecking(status, "Profile title codes " + indActProfileTitleCode);
						}
					}

					// DOB and Gender Comparison
					LOGGER.info("Validating DOB and Gender in Individual profile page ");
					String expGender = indProfileDetails.getGender();
					String expDateOfBirth = Objects.toString(indProfileDetails.getDateOfBirth(), "");

					compareValues(indActProfileGender, expGender, "Gender value");
					compareValues(indActProfileDOB, expDateOfBirth, "DOB value");

					// ALIAS NAME comparison
					LOGGER.info("Validating Alias names in Individual profile page ");
					List<AliasName> aliasNames = json.getProfile().getNameQualifier().getIndividualNameQualifier()
							.getAliasNames();
					if (aliasNames != null) {
						for (AliasName aliNames : aliasNames) {
							String indExpProfileAliasFirstName = Objects.toString(aliNames.getFirstName(), "");
							String indExpProfileAliasMiddleName = Objects.toString(aliNames.getMiddleName(), "");
							String indExpProfileAliasLastName = Objects.toString(aliNames.getLastName(), "");
							String indExpProfileSuffix = Objects.toString(aliNames.getSuffix(), "");

							compareValues(indActProfileAliasFirstName, indExpProfileAliasFirstName, "Alias FirstName");
							compareValues(indActProfileAliasMiddleName, indExpProfileAliasMiddleName,
									"Alias Middle Name");
							compareValues(indActProfileAliasLastName, indExpProfileAliasLastName, "Alias LastName");
							compareValues(indActProfileAliasSuffix, indExpProfileSuffix, "Alias Suffix");
						}
					}

					// PDM indicator comparison
					LOGGER.info("Validating PDM indicator in Individual profile page ");
					List<PdmIndicatorDetail> pdmDetails = json.getProfileAdditionalDetails().getPdmIndicatorDetails();
					if (pdmDetails != null) {
						for (PdmIndicatorDetail pdmIndDetails : pdmDetails) {
							Boolean status = profilePage.searchPDMIndicators(pdmIndDetails);
							resultChecking(status, "PDM Indicator " + pdmIndDetails.getPdmIndicator());
						}
					}

					// Validating CREDENTIAL details
					LOGGER.info("Validating credential indicator in Individual profile page ");
					List<CredentialingDetail> credentialingDetails = json.getProfileAdditionalDetails()
							.getCredentialingDetails();
					if (credentialingDetails != null) {
						for (CredentialingDetail credential : credentialingDetails) {
							Boolean status = profilePage.searchCredentialingDetails(credential);
							resultChecking(status,
									"Credentialing Details for the source " + credential.getCredentialingSource());

						}
					}

					// Validating Area of Focus
					List<AreasOFocus> areasOfFocus = json.getProfile().getAreasOFocus();
					if (areasOfFocus != null) {
						for (AreasOFocus areaOfFocus : areasOfFocus) {
							String actAreaOfFocus = profilePage.getIndAreaOfFocus();
							Boolean status = actAreaOfFocus.contains(areaOfFocus.getAreaOfFocus());
							resultChecking(status, "Area of Focus values assigned");
						}
					}

					// validating language details
					LOGGER.info("Validating LANGUAGE details in Individual profile page ");
					String provLanguages = profilePage.getIndProviderLanguages();
					List<String> expProvLanguageList = json.getProfile().getProviderLanguages();
					if (expProvLanguageList != null) {
						for (String expProvLanguage : expProvLanguageList) {
							Boolean status = provLanguages.contains(expProvLanguage);
							resultChecking(status, "Language" + expProvLanguage);
						}
					}

					// ***validateIndividualAlternateID Tab
					loginPage.navToAltIdTab();
					if (json.getAlternateIDs() != null) {
						List<NpiDetail> npiDetails = json.getAlternateIDs().getNpiDetails();
						if (npiDetails != null) {

							String actEgilibilityType = altIdPage.getNpiEligibilityType();

							result = altIdPage.getEligibilityType(ProviderType.ORGANIZATION) ? PASSED : FAILED;
							String expEgilibilityType = altIdPage.npiEligibile;
							String expEgilibilityType1 = altIdPage.npiExcempt;
							if (result.equalsIgnoreCase(PASSED) && expEgilibilityType.equalsIgnoreCase("Eligible")
									&& actEgilibilityType.equalsIgnoreCase("Eligible")) {
								resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
										PASSED, " Eligibility Type : Eligible"));
							} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType1.equalsIgnoreCase("Exempt")
									&& actEgilibilityType.equalsIgnoreCase("Exempt")) {
								resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType,
										expEgilibilityType1, PASSED, " Eligibility Type : Exempt"));
							} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType1.equalsIgnoreCase("Exempt")
									&& actEgilibilityType.equalsIgnoreCase("Eligible")) {
								resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
										FAILED, " Eligibility Type not matching"));
							} else if (result.equalsIgnoreCase(FAILED)
									&& expEgilibilityType.equalsIgnoreCase("Eligible")
									&& actEgilibilityType.equalsIgnoreCase("Exempt")) {
								resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
										FAILED, " Eligibility Type not matching"));
							}

							for (NpiDetail npidetails : npiDetails) {
								Boolean NPIstatus = altIdPage.searchNpiDetails(npidetails, ProviderType.INDIVIDUAL);
								resultChecking(NPIstatus, "NPI details for NPI value : " + npidetails.getNpiValue());
							}
						}
					}

					// ***validateIndividualspeciality Tab
					loginPage.navToSpecialityTaxonomyTab();
					List<Specialty> specialties = json.getSpecialties();
					if (specialties != null) {
						for (Specialty speciality : specialties) {
							Boolean specialitystatus = spltyTxnmyPage.searchSpecialtyDetailsTable(speciality);
							resultChecking(specialitystatus, "Speciality details : ");
						}
					}

					// ******validateIndividualEducation Tab
					loginPage.navToEducationTab();
					List<EducationDetail> eduDetails = json.getEducationDetails();
					if (eduDetails != null) {
						for (EducationDetail educationDetails : eduDetails) {
							Boolean eduStatus = educationPage.searchEducationDetails(educationDetails);
							resultChecking(eduStatus, "Education details");
						}
					}

					// validateIndividualAddress Tab
					LOGGER.info("Inside validating Individual Address Details");

					List<Address> addressess = json.getAddresses();
					if (addressess != null) {
						loginPage.navToAddressTab();
						for (Address addressDetails : addressess) {

							result = addressPage.filterAndSelectIndAddress(addressDetails) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Address record is not available"
									: "Address record is available";
							resultSet.add(new ResultTuple("Address", "",
									addressDetails.getAddressDetails().getAddressType() + " : "
											+ addressDetails.getAddressDetails().getAddressLine1() + " "
											+ addressDetails.getAddressDetails().getCity() + " "
											+ addressDetails.getAddressDetails().getState() + " "
											+ addressDetails.getAddressDetails().getZip() + " : "
											+ addressDetails.getAddressActive().getEffectiveDate() + " : "
											+ addressDetails.getAddressActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							String expIndAddressLine1 = addressDetails.getAddressDetails().getAddressLine1();
							String expIndZipcode = addressDetails.getAddressDetails().getZip();
							String expIndAddressEffDate = addressDetails.getAddressActive().getEffectiveDate();
							String expIndAddressTermDate = addressDetails.getAddressActive().getTerminationDate();
							String expIndTermReasonCode = addressDetails.getAddressActive()
									.getTerminationReasonCode() != null
											? addressDetails.getAddressActive().getTerminationReasonCode()
											: "";
							String actIndAddressLine1 = addressPage.getAddressLine1();
							String actIndZipcode = addressPage.getAddrZip();
							String actIndAddressEffDate = addressPage.getAddrEffectDate();
							String actIndAddressTermDate = addressPage.getAddrTermDate();
							String actIndTermRsnCode = addressPage.getAddrTermReasonCode();

							compareValues(actIndAddressLine1, expIndAddressLine1, "Address Line 1 ");
							compareValues(actIndZipcode, expIndZipcode, "Zipcode ");
							compareValues(actIndAddressEffDate, expIndAddressEffDate, "Address Effective Date");
							compareValues(actIndAddressTermDate, expIndAddressTermDate, "Address Termination date");
							compareValues(expIndTermReasonCode, actIndTermRsnCode, "Address Termination reason code");
							if (addressDetails.getAddressAdditionalDetails() != null) {
								addressPage.navToContactTab();
								List<ContactDetail> contDetails = addressDetails.getAddressAdditionalDetails()
										.getContactDetails();
								if (contDetails != null) {
									for (ContactDetail contactDetail : contDetails) {
										result = addressPage.searchContactTable(contactDetail, ProviderType.INDIVIDUAL)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED) ? "Contact record is not available"
												: "Contact record is available";
										resultSet.add(new ResultTuple("Contact", "",
												contactDetail.getFirstName() + " : " + contactDetail.getMiddleName()
														+ " " + contactDetail.getLastName() + " "
														+ contactDetail.getPhone() + " " + contactDetail.getFax()
														+ " : " + contactDetail.getEmail(),
												result, comment));
									}
								}

								addressPage.navToAreaOfFocusTab();
								List<AreasOFocus> areaFocus = addressDetails.getAddressAdditionalDetails()
										.getAreasOFocus();
								if (areaFocus != null) {
									for (AreasOFocus areasOFocus : areaFocus) {
										String areaOfFocus = areasOFocus.getAreaOfFocus();
										addressPage.valAreaOfFocusMatch(areaOfFocus);
									}
								}
								addressPage.navToOfficeDetailsTab();
								List<OfficeDetail> officeDetail = addressDetails.getAddressAdditionalDetails()
										.getOfficeDetails();
								if (officeDetail != null) {
									for (OfficeDetail officeDetails : officeDetail) {
										List<String> officeAccessCode = officeDetails.getAccessibilityCode();
										if (officeAccessCode != null) {
											for (String accessibilityCode : officeAccessCode) {
												result = addressPage.valOfficeAccessCode(accessibilityCode,
														ProviderType.INDIVIDUAL) ? PASSED : FAILED;
												comment = result.equals(FAILED) ? "Accessibility Code is not available"
														: "Accessibility Code is available";
												resultSet.add(new ResultTuple("AccessibilityCode", "",
														accessibilityCode, result, comment));
											}
										}
									}
								}

								addressPage.navToScheduleTab();
								List<SchedulingDetail> scheduleDetail = addressDetails.getAddressAdditionalDetails()
										.getSchedulingDetails();
								if (scheduleDetail != null) {
									for (SchedulingDetail schedulingDetail : scheduleDetail) {
										result = addressPage.valSchedulingDetail(schedulingDetail) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "SchedulingDetail details is not available"
												: "SchedulingDetail details is available";

										resultSet.add(new ResultTuple("scheduleDetail", "",
												schedulingDetail.getDays() + " : " + schedulingDetail.getOpenTime()
														+ " " + schedulingDetail.getCloseTime(),
												result, comment));
									}
								}

								addressPage.navToSpecialty();
								List<Specialty> splty = json.getSpecialties();
								if (splty != null) {
									for (Specialty specialProgram : splty) {
										result = addressPage.valSpecialty(specialProgram, ProviderType.INDIVIDUAL)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED) ? "specialProgram details is not available"
												: "specialProgram details is available";
										resultSet.add(new ResultTuple("specialProgram", "",
												specialProgram.getSpecialtyCode(), result, comment));
									}
								}

								addressPage.navToSpecialPrgmTab();
								List<SpecialProgram> splPrm = addressDetails.getAddressAdditionalDetails()
										.getSpecialPrograms();
								if (splPrm != null) {
									for (SpecialProgram specialProgram : splPrm) {
										String expSplPrgmType = specialProgram.getProviderSpecialProgramType();
										String expSplPrgmEffDate = specialProgram.getProgramActive().getEffectiveDate();

										result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "expSplPrgmType, expSplPrgmEffDate record is not available"
												: "expSplPrgmType, expSplPrgmEffDate record is available";
										resultSet.add(new ResultTuple("Address", "",
												specialProgram.getProviderSpecialProgramType() + " : "
														+ specialProgram.getProgramActive().getEffectiveDate(),
												result, comment));
									}
								}
							}
						}
					}

					// **********validateIndividualAffiliation Tab

					loginPage.navToAffilRelationTab();
					List<Affiliation> affi = json.getAffiliations();
					for (Affiliation affiliation : affi) {
						String providerEId = affiliation.getAffiliatedLegacyID();

						List<Address> addr = affiliation.getAddresses();
						for (Address address : addr) {

							loginPage.navToAffilRelationTab();
							affilRelationPage.searchAffilEid(providerEId);
							result = affilRelationPage.searchIndAffiliation(affiliation, address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Affiliation  details is not available"
									: "Affiliation  details is available";
							resultSet.add(new ResultTuple("Affiliation ", "",
									affiliation.getAffiliatedLegacyID() + " " + affiliation.getAffiliationType() + " "
											+ address.getAddressActive().getEffectiveDate() + " "
											+ address.getAddressActive().getTerminationDate() + " "
											+ address.getAddressActive().getTerminationReasonCode(),
									result, comment));
						}
					}

					// *************validateIndividualNetworkReim Tab
					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affiliations = json.getAffiliations();
					System.out.println(affiliations);
					for (Affiliation affiliation : affiliations) {
						List<Address> addr = affiliation.getAddresses();
						LOGGER.info("Inside checking NETWORK and REIMBURSEMENT tab");
						for (Address address : addr) {

							result = reimbNtwrkPage.selectAvailableAddrCheckbox(address) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Record is not available with the matching address"
									: "Record is available with the matching address";
							resultSet.add(new ResultTuple("AvailableAddrCheckbox", "", "Expected address: "
									+ address.getAddressDetails().getAddressLine1() + "Expected Effective Date: "
									+ address.getAddressActive().getEffectiveDate() + "Expected Termination  date: "
									+ address.getAddressActive().getTerminationDate(), result, comment));

							if (result == FAILED)
								continue;

							result = reimbNtwrkPage.selectAffiliationAddrCheckbox(affiliation) ? PASSED : FAILED;
							comment = result.equals(FAILED)
									? "Record is not available with the matching Affiliation Legacy ID, effective date , termination date"
									: "Record is available with the matching Affiliation Legacy ID, effective date , termination date";
							resultSet.add(new ResultTuple("Affiliation Legacy ID", "",
									"Expected ID: " + affiliation.getAffiliatedLegacyID() + "Expected Effective Date: "
											+ affiliation.getAffiliationActive().getEffectiveDate()
											+ "Expected Termination  date: "
											+ affiliation.getAffiliationActive().getTerminationDate(),
									result, comment));
							if (result == FAILED)
								continue;

							List<NetworkAffiliation> netwrkAffi = affiliation.getNetworkAffiliations();
							for (NetworkAffiliation networkAffiliation : netwrkAffi) {

								result = reimbNtwrkPage.filterAndSelectIndNetId(networkAffiliation) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Record is not available with the matching Network ID, effective date , termination date"
										: "Record is available with the matching Network ID, effective date , termination date";
								resultSet.add(new ResultTuple("Network ID", "", "Expected ID: "
										+ networkAffiliation.getNetworkID() + "Expected Effective Date: "
										+ networkAffiliation.getNetworkSourceSystem() + "Expected Termination  date: "
										+ networkAffiliation.getNetworkActive().getEffectiveDate()
										+ networkAffiliation.getNetworkActive().getTerminationDate()
										+ networkAffiliation.getNetworkActive().getTerminationReasonCode(), result,
										comment));
								if (result == FAILED)
									continue;

								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);
								String indactTermDate = reimbNtwrkPage.getIndNetwrkTermDate();
								String indactTimlyfiling = reimbNtwrkPage.getIndTimlyFiling();
								String indaccpatientsindicator = reimbNtwrkPage.getIndAcceptPatientsIndicator();
								String indactNetEffDate = reimbNtwrkPage.getIndNetwrkEffDate();
								String indactTermRsnCode = reimbNtwrkPage.getIndNetwrkTermDate();
								String indactNetDirIndicator = reimbNtwrkPage.getIndNetDirIndicator();
								String indactNetSourceSys = reimbNtwrkPage.getindNetSourceSys();
								String indactNetID = reimbNtwrkPage.getIndNetwrkId();
								String indactAgePre = reimbNtwrkPage.getIndAgeFrom();
								String indactGenderPre = reimbNtwrkPage.getIndPatientGender();

								LOGGER.info("Actual termination date: " + indactTermDate);
								LOGGER.info("Actual Timily filing: " + indactTimlyfiling);
								LOGGER.info("Actual accept patients indicator: " + indaccpatientsindicator);
								LOGGER.info("Actual Network Effective Date" + indactNetEffDate);
								LOGGER.info("Actual Network Directory Indicator" + indactNetDirIndicator);
								LOGGER.info("Actual Termination reason code : " + indactTermRsnCode);

								String expTermDate = networkAffiliation.getNetworkActive().getTerminationDate();
								String expEffDate = networkAffiliation.getNetworkActive().getEffectiveDate();
								String expNetDirIndicator = networkAffiliation.getDirectoryIndicator();
								LOGGER.info("Expected directory indicator : " + expNetDirIndicator);
								String expNetAgeFrom = networkAffiliation.getPatientPreferences().getAgeFrom() != null
										? networkAffiliation.getPatientPreferences().getAgeFrom()
										: "";
								String expNetAgeTo = networkAffiliation.getPatientPreferences().getAgeTo() != null
										? networkAffiliation.getPatientPreferences().getAgeTo()
										: "";
								String expNetGenderPre = networkAffiliation.getPatientPreferences()
										.getPatientGenderPreference() != null
												? networkAffiliation.getPatientPreferences()
														.getPatientGenderPreference()
												: "";
								String expIndNetID = networkAffiliation.getNetworkID() != null
										? networkAffiliation.getNetworkID()
										: "";
								String expNetTermRsnCode = networkAffiliation.getNetworkActive()
										.getTerminationReasonCode() != null
												? networkAffiliation.getNetworkActive().getTerminationReasonCode()
												: "";

								compareValues(indactGenderPre, expNetGenderPre, "Patient Gender Preference: ");
								compareValues(indactNetID, expIndNetID, "Network ID");
								compareValues(indactNetSourceSys, "QCARE", "Network Source System");
								compareValues(indactAgePre, expNetAgeFrom + "-" + expNetAgeTo,
										"Patient Age Preference");
								compareValues(indactTermRsnCode, expNetTermRsnCode, "Termination reason code");
								compareValues(indactNetDirIndicator, expNetDirIndicator, "Network directory indicator");
								compareValues(indactNetEffDate, expEffDate, "Network effective date");
								compareValues(indactTermDate, expTermDate, "Termination date");

								List<AcceptingPatientsInd> AcceptingPatientsInd = networkAffiliation
										.getAcceptingPatientsInds();
								for (AcceptingPatientsInd expAccPatientsInd : AcceptingPatientsInd) {
									String expAcceptingPatientsInd = expAccPatientsInd.getAcceptingPatientsIndicator();
									String actAcceptPatientsIndicator = reimbNtwrkPage.getIndAcceptPatientsIndicator();
									if (!actAcceptPatientsIndicator.equalsIgnoreCase(expAcceptingPatientsInd)) {
										resultSet.add(new ResultTuple("actAcceptPatientsIndicator",
												actAcceptPatientsIndicator, actAcceptPatientsIndicator, FAILED,
												"AcceptPatientsIndicator not matching"));
									} else {
										resultSet.add(new ResultTuple("actAcceptPatientsIndicator",
												actAcceptPatientsIndicator, actAcceptPatientsIndicator, PASSED,
												"AcceptPatientsIndicator matching"));
									}
								}

								String expTimelyFilingInd = EpdsConstants.TIMELY_FILING;
								String actTimelyFilingInd = reimbNtwrkPage.getIndTimlyFiling();
								if (!actTimelyFilingInd.equalsIgnoreCase(expTimelyFilingInd)) {
									resultSet.add(new ResultTuple("TimelyFilingInd", actTimelyFilingInd,
											expTimelyFilingInd, FAILED, "TimelyFiling not matching"));
								} else {
									resultSet.add(new ResultTuple("TimelyFilingInd", actTimelyFilingInd,
											expTimelyFilingInd, PASSED, "TimelyFiling matching"));
								}

								// code for checking specialty inside network
								List<Specialty> netwrkSpecialities = json.getSpecialties();
								LOGGER.info("Specialities" + netwrkSpecialities);
								for (Specialty netwrkSpeciality : netwrkSpecialities) {
									Boolean statusSpeciality = reimbNtwrkPage.searchSpeciality(netwrkSpeciality);
									resultChecking(statusSpeciality, "Speciality in Network ");

								}

								List<Reimbursement> reimb = affiliation.getReimbursements();
								for (Reimbursement reimbursement : reimb) {

									result = reimbNtwrkPage.valIndAssociatedReimbTable(reimbursement) ? PASSED : FAILED;
									comment = result.equals(FAILED)
											? "Record is not available with the matching Reimbursement value, effective date , termination date"
											: "Record is available with the matching Reimbursement value, effective date , termination date";
									resultSet.add(new ResultTuple("Reimbursement value", "",
											"Expected reimbursement value: " + reimbursement.getReimbursementValue()
													+ "Expected Effective Date: "
													+ reimbursement.getReimbursementActive().getEffectiveDate()
													+ "Expected Termination  date: "
													+ reimbursement.getReimbursementActive().getTerminationDate(),
											result, comment));
								}
								reimbNtwrkPage.indClickCancel();
								Thread.sleep(EpdsConstants.HIGH_THREAD_VALUE);
							}
						}
					}
				} catch (Exception e) {
					resultSet.add(
							new ResultTuple("Test failure", e.toString(), "", FAILED, "Test failure due to exception"));
					e.printStackTrace();
				} finally {
					reporting.writeDetailedReport(testInfo, json, resultSet);
					closeBrowser();
				}
			}
		}
	}

	public void resultCheckingvalue(String result, String ResultMessage) {
		String comment = "";
		List<ResultTuple> resultSet = new ArrayList<ResultTuple>();
		if (result == PASSED)
			comment = ResultMessage + " in Individual profile page is matching ";
		else
			comment = ResultMessage + " in Individual profile is not matching ";
		resultSet.add(new ResultTuple(ResultMessage, " Actual value: ", " Expected value : " + " ", result, comment));
		LOGGER.info(ResultMessage + " Actual value: " + " Expected value : " + " " + result + " " + comment);
	}

	public void resultChecking(Boolean status, String ResultMessage) {
		String result = "", comment = "";
		List<ResultTuple> resultSet = new ArrayList<ResultTuple>();
		if (status == true) {
			result = PASSED;
		} else {
			result = FAILED;
		}

		if (result == PASSED)
			comment = ResultMessage + " in Individual profile page is matching ";
		else
			comment = ResultMessage + " in Individual profile is not matching ";
		resultSet.add(new ResultTuple(ResultMessage, " Actual value: ", " Expected value : " + " ", result, comment));
		LOGGER.info(ResultMessage + " Actual value: " + " Expected value : " + " " + result + " " + comment);
	}

	public void compareValues(String Actual, String Expected, String ResultMessage) {
		String result = "", comment = "";
		List<ResultTuple> resultSet = new ArrayList<ResultTuple>();
		result = Actual.equalsIgnoreCase(Expected) ? PASSED : FAILED;
		if (result == PASSED)
			comment = ResultMessage + " in Individual profile page is matching ";
		else
			comment = ResultMessage + " in Individual profile is not matching ";
		resultSet.add(new ResultTuple(ResultMessage, " Actual value: " + Actual, " Expected value : " + Expected + " ",
				result, comment));
		LOGGER.info(ResultMessage + " Actual value: " + Actual + " Expected value : " + Expected + " " + result + " "
				+ comment);
	}
}
