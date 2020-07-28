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
import com.beans.mdxjson.AltIdDetail;
import com.beans.mdxjson.ContactDetail;
import com.beans.mdxjson.CredentialingDetail;
import com.beans.mdxjson.NetworkAffiliation;
import com.beans.mdxjson.NpiDetail;
import com.beans.mdxjson.OfficeDetail;
import com.beans.mdxjson.PdmIndicatorDetail;
import com.beans.mdxjson.ProviderDistinctionDetail;
import com.beans.mdxjson.RPAJsonTransaction;
import com.beans.mdxjson.Reimbursement;
import com.beans.mdxjson.SchedulingDetail;
import com.beans.mdxjson.SpecialProgram;
import com.beans.mdxjson.Specialty;
import com.beans.mdxjson.TaxID;
import com.beans.mdxjson.TimelyFilingInd;
import com.pages.EPDSPageObjects.AddressPage;
import com.pages.EPDSPageObjects.AlternateId;
import com.pages.EPDSPageObjects.LegacyInfo;
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

public class AddOrganizationEntireFlow extends RpaTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(AddOrganizationEntireFlow.class);

	@Test(description = "SPDS-132958, 136447, 136448 - Add New Organization - full flow in EPDSv2", enabled = true, groups = {
			"org" })
	@TestDetails(userstory = "SPDS-132958, SPDS-136447, SPDS-136447", author = "Murali", transactionId = "ORG_ADD_FULL_FLOW_IN_EPDSV2", sprint = "Sprint 192")
	public void ORG_ADD_FULL_FLOW_IN_EPDSV2() {

		LOGGER.info("Inside test ORG_ADD_FULL_FLOW_IN_EPDSV2");
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
					SpecialityTaxonomy spltyTxnmyPage = new SpecialityTaxonomy(driver);
					ReimbusermentNetworks reimbNtwrkPage = new ReimbusermentNetworks(driver);
					LegacyInfo legacyInfoPage = new LegacyInfo(driver);

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

					if (!providerSearch.searchAndSelectOrganization(entID)) {
						resultSet.add(
								new ResultTuple("Provider", "", entID, FAILED, "Individual Provider is not listed"));
						reporting.writeDetailedReport(testInfo, json, resultSet);
						continue;
					} else {
						resultSet.add(new ResultTuple("Provider", "", entID, PASSED, "Individual Provider is present"));
					}
					LOGGER.info("Provider Organization is present...");

					/******* Provider Details */

					LOGGER.info("Inside Provider details");

					String actOrgName = profilePage.getOrgProvName();
					String expOrgName = json.getProfile().getNameQualifier().getOrgNameQualifier().getOrgName() != null
							? json.getProfile().getNameQualifier().getOrgNameQualifier().getOrgName()
							: "";

					result = actOrgName.equalsIgnoreCase(expOrgName) ? PASSED : FAILED;
					comment = result.equals(FAILED) ? "Organization Name is not matching with the actual value" : "";
					resultSet.add(new ResultTuple("Organization Name ", "Actual value: " + actOrgName,
							"Expected : " + expOrgName, result, comment));

					String actEffDate = profilePage.getOrgProvEffDate();
					String expEffDate = json.getProfile().getProviderActive().getEffectiveDate() != null
							? json.getProfile().getProviderActive().getEffectiveDate()
							: "";

					result = actEffDate.equalsIgnoreCase(expEffDate) ? PASSED : FAILED;
					comment = result.equals(FAILED) ? "Effective Date value is not matching with the actual value" : "";
					resultSet.add(new ResultTuple("Effective Date ", "Actual value: " + actEffDate,
							"Expected : " + expEffDate, result, comment));

					String actTermDate = profilePage.getOrgProvTermDate();
					String expTermDate = json.getProfile().getProviderActive().getTerminationDate() != null
							? json.getProfile().getProviderActive().getTerminationDate()
							: "";

					result = actTermDate.equalsIgnoreCase(expTermDate) ? PASSED : FAILED;
					comment = result.equals(FAILED) ? "Termination Date value is not matching with the actual value"
							: "";
					resultSet.add(new ResultTuple("Termination Date ", "Actual value: " + actTermDate,
							"Expected : " + expTermDate, result, comment));

					String actTermreasonCode = profilePage.getOrgProvTermReasonCode();
					String expTermReasonCode = json.getProfile().getProviderActive().getTerminationReasonCode() != null
							? json.getProfile().getProviderActive().getTerminationReasonCode()
							: "";

					result = actTermreasonCode.equalsIgnoreCase(expTermReasonCode) ? PASSED : FAILED;
					comment = result.equals(FAILED)
							? "Termination Reason Code vale is not matching with the actual value"
							: "";
					resultSet.add(new ResultTuple("Termination Reason Code ", "Actual value: " + actTermreasonCode,
							"Expected : " + expTermReasonCode, result, comment));

					/******* Tax Details */
					LOGGER.info("Inside Provider Tax details");

					List<TaxID> taxId = json.getProfile().getTaxIDs();
					if (taxId != null) {
						for (TaxID taxIdDetails : taxId) {

							result = profilePage.searchTaxDetails(taxIdDetails) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Tax ID details is not available with the matching values"
									: "Tax ID details is available with the matching values";

							resultSet.add(new ResultTuple("Tax Code", "",
									"Expected Tax Code: " + taxIdDetails.getTaxIDValue() + "Expected Effective Date: "
											+ taxIdDetails.getTaxIDActive().getEffectiveDate()
											+ "Expected Termination  date: "
											+ taxIdDetails.getTaxIDActive().getTerminationDate(),
									result, comment));
						}
					}

					/******* PDM IndicatorDetails */
					LOGGER.info("Inside Provider PDM details");
					if (json.getProfileAdditionalDetails() != null) {
						List<PdmIndicatorDetail> pdmDetails = json.getProfileAdditionalDetails()
								.getPdmIndicatorDetails();
						if (pdmDetails != null) {
							for (PdmIndicatorDetail pdmIndDetails : pdmDetails) {
								result = profilePage.searchPDMIndicators(pdmIndDetails) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "PDM record is not available with the matching values"
										: "PDM record is available with the matching values";
								resultSet.add(new ResultTuple("PDM details", "",
										"Expected PDMIndicator: " + pdmIndDetails.getPdmIndicator()
												+ " Expected PDM effective date: "
												+ pdmIndDetails.getPdmIndicatorActive().getEffectiveDate()
												+ " Expected Network termination date: "
												+ pdmIndDetails.getPdmIndicatorActive().getTerminationDate(),
										result, comment));
							}
						}
					}

					/******* Organization CredentialingDetails */

					LOGGER.info("Inside Provider credentialing details");
					if (json.getProfileAdditionalDetails() != null) {
						List<CredentialingDetail> credentialingDetails = json.getProfileAdditionalDetails()
								.getCredentialingDetails();
						if (credentialingDetails != null) {
							for (CredentialingDetail credential : credentialingDetails) {
								result = profilePage.searchCredentialingDetails(credential) ? PASSED : FAILED;
								comment = result.equals(FAILED)
										? "Credentialing record is not available with the matching values"
										: "";
								resultSet.add(new ResultTuple("Credentialing details", "",
										"Expected Credentialing source: " + credential.getCredentialingSource()
												+ " Expected Credentialing effective date: "
												+ credential.getCredentialingActive().getEffectiveDate()
												+ " Expected Credentialing termination date: "
												+ credential.getCredentialingActive().getTerminationDate(),
										result, comment));
							}
						}
					}

					/******* Organization DistinctionDetails distinction details abandoned */

					/*
					 * LOGGER.info("Inside Provider distinction details");
					 * List<ProviderDistinctionDetail> provideDistinctionDetails =
					 * json.getProfileAdditionalDetails() .getProviderDistinctionDetails(); if
					 * (provideDistinctionDetails != null) { for (ProviderDistinctionDetail
					 * distinctionDetails : provideDistinctionDetails) {
					 * 
					 * result = profilePage.searchDistinctionDetails(distinctionDetails) ? PASSED :
					 * FAILED; comment = result.equals(FAILED) ?
					 * "Distinction record is not available with the matching values" : "";
					 * resultSet.add(new ResultTuple("Distinction details", "",
					 * "Expected Distinction source: " +
					 * distinctionDetails.getProviderDistinctionCode() +
					 * " Expected Distinction effective date: " +
					 * distinctionDetails.getProviderDistinctionDetailsActive().getEffectiveDate() +
					 * " Expected Distinction termination date: " +
					 * distinctionDetails.getProviderDistinctionDetailsActive().getTerminationDate()
					 * , result, comment));
					 * 
					 * } }
					 */

					/******* NPI Details */
					LOGGER.info("Inside Provider NPI details");

					List<NpiDetail> npiDetails = json.getAlternateIDs().getNpiDetails();
					if (npiDetails != null) {
						loginPage.navToAltIdTab();

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
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType1,
									PASSED, " Eligibility Type : Exempt"));
						} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType1.equalsIgnoreCase("Exempt")
								&& actEgilibilityType.equalsIgnoreCase("Eligible")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
									FAILED, " Eligibility Type not matching"));
						} else if (result.equalsIgnoreCase(FAILED) && expEgilibilityType.equalsIgnoreCase("Eligible")
								&& actEgilibilityType.equalsIgnoreCase("Exempt")) {
							resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType, expEgilibilityType,
									FAILED, " Eligibility Type not matching"));
						}

						for (NpiDetail npidetails : npiDetails) {
							/*
							 * String expEgilibilityType = npidetails.getNpiEligibilityType(); String
							 * actEgilibilityType = altIdPage.getNpiEligibilityType(); if
							 * (!actEgilibilityType.equalsIgnoreCase(expEgilibilityType)) {
							 * resultSet.add(new ResultTuple("EgilibilityType", actEgilibilityType,
							 * expEgilibilityType, FAILED, "Addition failed")); }
							 */

							result = altIdPage.searchNpiDetails(npidetails, ProviderType.ORGANIZATION) ? PASSED
									: FAILED;
							comment = result.equals(FAILED) ? "NPI record is not available" : "";
							resultSet.add(new ResultTuple("NPI details", "",
									npidetails.getNpiType() + " " + npidetails.getNpiValue() + " "
											+ npidetails.getNpiActive().getEffectiveDate() + " "
											+ npidetails.getNpiActive().getTerminationDate() + " "
											+ npidetails.getNpiActive().getTerminationReasonCode(),
									result, comment));

						}
					}

					/****************** ALT ID *********************/
					LOGGER.info("Inside Provider ALTID details");

					List<AltIdDetail> altDetails = json.getAlternateIDs().getAltIdDetails();
					if (altDetails != null) {
						for (AltIdDetail altIddetails : altDetails) {
							result = altIdPage.searchAltIdDetails(altIddetails) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "ALT record is not available" : "ALT record is available";
							resultSet.add(new ResultTuple("ALT details", "",
									altIddetails.getAltIDSource() + " " + altIddetails.getAltIDType() + " "
											+ altIddetails.getAltIDValue() + " "
											+ altIddetails.getAlternateIDActive().getEffectiveDate() + " "
											+ altIddetails.getAlternateIDActive().getTerminationDate() + " "
											+ altIddetails.getAlternateIDActive().getTerminationReasonCode(),
									result, comment));
						}
					}

					/****************** Specialty *********************/
					LOGGER.info("Inside Provider Specialty details");

					loginPage.navToSpecialityTaxonomyTab();

					List<Specialty> specialityDetails = json.getSpecialties();
					if (specialityDetails != null) {

						for (Specialty specialdetails : specialityDetails) {
							result = spltyTxnmyPage.searchProfileSpeciality(specialdetails) ? PASSED : FAILED;
							comment = result.equals(FAILED) ? "Speciality Profile details is not available"
									: "Speciality Profile details is available";
							resultSet.add(new ResultTuple("Speciality Profile details", "",
									specialdetails.getSpecialtyCode() + " "
											+ specialdetails.getSpecialtyActive().getEffectiveDate() + " "
											+ specialdetails.getSpecialtyActive().getTerminationDate() + " "
											+ specialdetails.getSpecialtyActive().getTerminationReasonCode(),
									result, comment));
						}
					}

					/****************** Address Details *********************/

					LOGGER.info("Inside Provider address details");
					List<Address> addressess = json.getAddresses();
					if (addressess != null) {
						for (Address addressDetails : addressess) {
							loginPage.navToAddressTab();

							if (addressDetails.getAddressDetails().getAddressType().equalsIgnoreCase("Practice")) {
								result = addressPage.filterAndSelectOrgAddress(addressDetails) ? PASSED : FAILED;
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

								String expAddressLine1 = addressDetails.getAddressDetails().getAddressLine1();
								String expAddressLine2 = Objects
										.toString(addressDetails.getAddressDetails().getAddressLine2(), "");
								String expAddressLine3 = Objects
										.toString(addressDetails.getAddressDetails().getAddressLine3(), "");
								String expZipCode = addressDetails.getAddressDetails().getZip();
								String expAddressEffDate = addressDetails.getAddressActive().getEffectiveDate();
								String expAddressTermDate = addressDetails.getAddressActive().getTerminationDate();
								String expAddressTermRsnCode = Objects
										.toString(addressDetails.getAddressActive().getTerminationReasonCode(), "");
								Boolean expPriPracInd = addressDetails.getAddressDetails()
										.getPrimaryPracticeIndicator();

								String expPrimaryPracInd = null;
								if (expPriPracInd) {
									expPrimaryPracInd = "Y";

								} else {
									expPrimaryPracInd = "N";
								}

								String actAddressLine1 = addressPage.getAddressLine1();
								String actAddressLine2 = addressPage.getAddressLine2();
								String actAddressLine3 = addressPage.getAddressLine3();
								String actZipcode = addressPage.getAddrZip();
								String actAddressEffDate = addressPage.getAddrEffectDate();
								String actAddressTerminationDate = addressPage.getAddrTermDate();
								String actTerminationRsnCode = addressPage.getAddrTermReasonCode();
								String actPriPracInd = addressPage.getPracticeIndicator();

								if (!actAddressLine1.equalsIgnoreCase(expAddressLine1)) {
									resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1,
											FAILED, "AddressLine1 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1,
											PASSED, "AddressLine1 is matching"));
								}

								if (!actAddressLine2.equalsIgnoreCase(expAddressLine2)) {
									resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2,
											FAILED, "AddressLine2 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2,
											PASSED, "AddressLine2 is matching"));
								}

								if (!actAddressLine3.equalsIgnoreCase(expAddressLine3)) {
									resultSet.add(new ResultTuple("AddressLine3", actAddressLine3, expAddressLine3,
											FAILED, "AddressLine3 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine3", actAddressLine3, expAddressLine3,
											PASSED, "AddressLine3 is matching"));
								}

								if (!actPriPracInd.equalsIgnoreCase(expPrimaryPracInd)) {
									resultSet.add(new ResultTuple("	Primary Practice Indicator", actPriPracInd,
											expPrimaryPracInd, FAILED, "Primary Practice Indicator not matching"));
								} else {
									resultSet.add(new ResultTuple("Primary Practice Indicator", actPriPracInd,
											expPrimaryPracInd, PASSED, "Primary Practice Indicator is matching"));
								}

								if (!actZipcode.equalsIgnoreCase(expZipCode)) {
									resultSet.add(new ResultTuple("Zip code", actZipcode, expZipCode, FAILED,
											"Zipcode not matching"));
								} else {
									resultSet.add(new ResultTuple("Zip code", actZipcode, expZipCode, PASSED,
											"Zip is matching"));
								}

								if (!actAddressEffDate.equalsIgnoreCase(expAddressEffDate)) {
									resultSet.add(new ResultTuple("Effective date", actAddressEffDate,
											expAddressEffDate, FAILED, "Effective date not matching"));
								} else {
									resultSet.add(new ResultTuple("Effective date", actAddressEffDate,
											expAddressEffDate, PASSED, "Effective date is matching"));
								}

								if (!actAddressTerminationDate.equalsIgnoreCase(expAddressTermDate)) {
									resultSet.add(new ResultTuple("Termination date", actAddressTerminationDate,
											expAddressTermDate, FAILED, "Termination date not matching"));
								} else {
									resultSet.add(new ResultTuple("Termination date", actAddressTerminationDate,
											expAddressTermDate, PASSED, "Termination date is matching"));
								}

								if (!actTerminationRsnCode.equalsIgnoreCase(expAddressTermRsnCode)) {
									resultSet.add(new ResultTuple("Termination reason", actAddressTerminationDate,
											expAddressTermRsnCode, FAILED, "Termination reason not matching"));
								} else {
									resultSet.add(new ResultTuple("Termination reason", actTerminationRsnCode,
											expAddressTermRsnCode, PASSED, "Termination reason is matching"));
								}

								// contact tab
								addressPage.navToContactTab();
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

								List<ContactDetail> contactDetails = addressDetails.getAddressAdditionalDetails()
										.getContactDetails();
								if (contactDetails != null) {
									for (ContactDetail contDetails : contactDetails) { // contact table

										result = addressPage.searchContactTable(contDetails, ProviderType.ORGANIZATION)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Contact record is not available with the matching values"
												: "Contact record is available with the matching values";
										resultSet.add(new ResultTuple("Contact details", "",
												"Expected First Name: " + contDetails.getFirstName()
														+ " Expected Last Name: " + contDetails.getLastName()
														+ " Expected Middle Name: " + contDetails.getMiddleName()
														+ " Expected Phone : " + contDetails.getPhone()
														+ " Expected Fax: " + contDetails.getFax(),
												result, comment));
									}
								}
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

								// office details tab

								addressPage.navToOfficeDetailsTab();
								List<OfficeDetail> officeDetails = addressDetails.getAddressAdditionalDetails()
										.getOfficeDetails();
								if (officeDetails != null) {
									for (OfficeDetail officeDetail : officeDetails) {
										List<String> officeAccessCode = officeDetail.getAccessibilityCode();
										if (officeAccessCode != null) {
											for (String accessibilityCode : officeAccessCode) {
												result = addressPage.valOfficeAccessCode(accessibilityCode,
														ProviderType.ORGANIZATION) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "Office Accessibility Code is not available"
														: "Office Accessibility Code is available";
												resultSet.add(new ResultTuple("Office Accessibility Code", "",
														accessibilityCode, result, comment));
											}
										}

										List<String> staffLangs = officeDetail.getStaffLanguage();
										if (staffLangs != null) {
											for (String staffLanguage : staffLangs) {
												String staffLang = addressPage.getStaffLanguage();
												result = staffLang.contains(staffLanguage) ? PASSED : FAILED;
												comment = result.equals(FAILED) ? "Staff Language is not available"
														: "Staff Language is available";
												resultSet.add(new ResultTuple("Staff Language", "", staffLanguage,
														result, comment));
											}
										}
									}
								}

								// Schedule tab
								addressPage.navToScheduleTab();
								List<SchedulingDetail> scheduleDetail = addressDetails.getAddressAdditionalDetails()
										.getSchedulingDetails();
								if (scheduleDetail != null) {
									for (SchedulingDetail schedulingDetail : scheduleDetail) {
										result = addressPage.valSchedulingDetail(schedulingDetail) ? PASSED : FAILED;
										comment = result.equals(FAILED) ? "Scheduling Detail record is not available"
												: "";
										resultSet.add(new ResultTuple("scheduleDetail", "",
												schedulingDetail.getDays() + " : " + schedulingDetail.getOpenTime()
														+ " " + schedulingDetail.getCloseTime(),
												result, comment));
									}
								}

								// checking speciality
								addressPage.navToOrgSpecialty();
								List<Specialty> specialties = json.getSpecialties();
								if (specialties != null) {
									for (Specialty speciality : specialties) {
										result = addressPage.valSpecialty(speciality, ProviderType.ORGANIZATION)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED) ? "Speciality record is not available"
												: "Speciality record is available";
										resultSet.add(new ResultTuple("Speciality", "", speciality.getSpecialtyCode(),
												result, comment));
										if (result == FAILED)
											continue;
									}
								}

								// Special pgm features abandoned
								/*
								 * addressPage.navToOrgSpecialPrgmTab(); List<SpecialProgram> splPrm =
								 * addressDetails.getAddressAdditionalDetails() .getSpecialPrograms(); if
								 * (splPrm != null) { for (SpecialProgram specialProgram : splPrm) { String
								 * expSplPrgmType = specialProgram.getProviderSpecialProgramType(); String
								 * expSplPrgmEffDate = specialProgram.getProgramActive().getEffectiveDate();
								 * String expSplPrgmTermDate = specialProgram.getProgramActive()
								 * .getTerminationDate(); String expSplPrgmTermCode =
								 * specialProgram.getProgramActive() .getTerminationReasonCode();
								 * 
								 * result = addressPage.valSplProgramsType(expSplPrgmType, expSplPrgmEffDate) ?
								 * PASSED : FAILED; comment = result.equals(FAILED) ?
								 * "SpecialProgram records is not available" : ""; resultSet.add(new
								 * ResultTuple( "SpecialProgram", "", expSplPrgmType + " : " + expSplPrgmEffDate
								 * + ", " + expSplPrgmTermDate + ", " + expSplPrgmTermCode, result, comment)); }
								 * }
								 */
							} else if (addressDetails.getAddressDetails().getAddressType().equalsIgnoreCase("Remit")) {

								result = addressPage.filterAndSelectOrgAddress(addressDetails) ? PASSED : FAILED;
								comment = result.equals(FAILED) ? "Remit Address record is not available"
										: "Remit Address record is available";
								resultSet.add(new ResultTuple("Remit Address", "",
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

								String expAddressLine1 = addressDetails.getAddressDetails().getAddressLine1();
								String expAddressLine2 = Objects
										.toString(addressDetails.getAddressDetails().getAddressLine2(), "");
								String expAddressLine3 = Objects
										.toString(addressDetails.getAddressDetails().getAddressLine3(), "");
								String expZipCode = addressDetails.getAddressDetails().getZip();
								String expRemitAddressEffDate = addressDetails.getAddressActive().getEffectiveDate();
								String expRemitAddressTermDate = addressDetails.getAddressActive().getTerminationDate();
								String expRemitAddressTermRsnCode = Objects
										.toString(addressDetails.getAddressActive().getTerminationReasonCode(), "");
								Boolean expPriRemmitInd = addressDetails.getAddressDetails()
										.getPrimaryPracticeIndicator();
								String expPrimaryRemInd = null;
								if (expPriRemmitInd) {
									expPrimaryRemInd = "Y";

								} else {
									expPrimaryRemInd = "N";
								}

								String actAddressLine1 = addressPage.getAddressLine1();
								String actAddressLine2 = addressPage.getAddressLine2();
								String actAddressLine3 = addressPage.getAddressLine3();
								String actZipCode = addressPage.getAddrZip();
								String actPriRemInd = addressPage.getRemitPracticeInd();
								String actRemitAddressEffDate = addressPage.getAddrEffectDate();
								String actRemitAddressTerminationDate = addressPage.getAddrTermDate();
								String actRemitTerminationRsnCode = addressPage.getAddrTermReasonCode();

								if (!actAddressLine1.equalsIgnoreCase(expAddressLine1)) {
									resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1,
											FAILED, "AddressLine1 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine1", actAddressLine1, expAddressLine1,
											PASSED, "AddressLine1 is matching"));
								}

								if (!actAddressLine2.equalsIgnoreCase(expAddressLine2)) {
									resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2,
											FAILED, "AddressLine2 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine2", actAddressLine2, expAddressLine2,
											PASSED, "AddressLine2 is matching"));
								}

								if (!actAddressLine3.equalsIgnoreCase(expAddressLine3)) {
									resultSet.add(new ResultTuple("AddressLine3", actAddressLine3, expAddressLine3,
											FAILED, "AddressLine3 not matching"));
								} else {
									resultSet.add(new ResultTuple("AddressLine3", actAddressLine3, expAddressLine3,
											PASSED, "AddressLine3 is matching"));
								}

								if (!actPriRemInd.equalsIgnoreCase(expPrimaryRemInd)) {
									resultSet.add(new ResultTuple("	Primary Practice Indicator", actPriRemInd,
											expPrimaryRemInd, FAILED, "Primary Remmit Indicator not matching"));
								} else {
									resultSet.add(new ResultTuple("Primary Practice Indicator", actPriRemInd,
											expPrimaryRemInd, PASSED, "Primary Remmit Indicator is matching"));
								}

								if (!actZipCode.equals(expZipCode)) {
									resultSet.add(new ResultTuple("Zip code", actZipCode, expZipCode, FAILED,
											"Zipcode not matching"));
								} else {
									resultSet.add(new ResultTuple("Zip code", actZipCode, expZipCode, PASSED,
											"Zip is matching"));
								}

								if (!actRemitAddressEffDate.equals(expRemitAddressEffDate)) {
									resultSet.add(new ResultTuple("Effective date", actRemitAddressEffDate,
											expRemitAddressEffDate, FAILED, "Effective date not matching"));
								} else {
									resultSet.add(new ResultTuple("Effective date", actRemitAddressEffDate,
											expRemitAddressEffDate, PASSED, "Effective date is matching"));
								}

								if (!actRemitAddressTerminationDate.equals(expRemitAddressTermDate)) {
									resultSet.add(new ResultTuple("Termination date", actRemitAddressTerminationDate,
											expRemitAddressTermDate, FAILED, "Termination date not matching"));
								} else {
									resultSet.add(new ResultTuple("Termination date", actRemitAddressTerminationDate,
											expRemitAddressTermDate, PASSED, "Termination date is matching"));
								}

								if (!actRemitTerminationRsnCode.equals(expRemitAddressTermRsnCode)) {
									resultSet.add(new ResultTuple("Termination reason", actRemitTerminationRsnCode,
											expRemitAddressTermRsnCode, FAILED, "Termination reason not matching"));
								} else {
									resultSet.add(new ResultTuple("Termination reason", actRemitTerminationRsnCode,
											expRemitAddressTermRsnCode, PASSED, "Termination reason is matching"));
								}

								// contact tab
								addressPage.navToContactTab();
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

								List<ContactDetail> contactDetails = addressDetails.getAddressAdditionalDetails()
										.getContactDetails();
								if (contactDetails != null) {
									for (ContactDetail contDetails : contactDetails) { // contact table

										result = addressPage.searchOrgRemitAddrContactTable(contDetails) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Contact record is not available with the matching values"
												: "Contact record is available with the matching values";
										resultSet.add(new ResultTuple("Contact details", "",
												"Expected First Name: " + contDetails.getFirstName()
														+ " Expected Last Name: " + contDetails.getLastName()
														+ " Expected Middle Name: " + contDetails.getMiddleName()
														+ " Expected Phone : " + contDetails.getPhone()
														+ " Expected Fax: " + contDetails.getFax(),
												result, comment));
									}
								}
								Thread.sleep(EpdsConstants.LOW_THREAD_VALUE);

							} else if (addressDetails.getAddressDetails().getAddressType()
									.equalsIgnoreCase("Correspondence")) {
								System.out.println("No additional records for correspondence address.");
							}

						}
					}

					/****************** Reimbursements and Networks Details *********************/
					String expPARIndicator = "Yes";
					loginPage.navToReimbNtwrkTab();
					List<Affiliation> affilList = json.getAffiliations();
					for (Affiliation affiliation : affilList) {
						List<Address> addresses = affiliation.getAddresses();
						if (addresses != null) {
							for (Address ntwrkAddr : addresses) {
								List<NetworkAffiliation> ntwrkAffilList = affiliation.getNetworkAffiliations();
								if (ntwrkAffilList != null) {
									for (NetworkAffiliation ntwrkAffil : ntwrkAffilList) {

										result = reimbNtwrkPage.filterAndSelectOrgNetId(ntwrkAddr, ntwrkAffil) ? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "Network ID record is not available with the matching values"
												: "Network ID record is available with the matching values";
										resultSet.add(new ResultTuple("Network details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected Network ID: " + ntwrkAffil.getNetworkID()
														+ "Expected Network effective date: "
														+ ntwrkAffil.getNetworkActive().getEffectiveDate()
														+ "Expected Network termination date: "
														+ ntwrkAffil.getNetworkActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expNtwrkId = ntwrkAffil.getNetworkID();
										String expNtwrkEffDate = ntwrkAffil.getNetworkActive().getEffectiveDate();
										String expNtwrkTermDate = ntwrkAffil.getNetworkActive().getTerminationDate();
										String expNtwrkTermReasonCode = Objects
												.toString(ntwrkAffil.getNetworkActive().getTerminationReasonCode(), "");
										String expDirectoryInd = Objects.toString(ntwrkAffil.getDirectoryIndicator(),
												"");

										String actNtwrkId = reimbNtwrkPage.getOrgNetId();
										String actNtwrkSrcSystem = reimbNtwrkPage.getOrgNetwrkSourceSystem();
										String actNtwrkEffDate = reimbNtwrkPage.getOrgNetwrkEffDate();
										String actNtwrkTermDate = reimbNtwrkPage.getOrgNetwkTermdate();
										String actNtwrkTermReasonCode = reimbNtwrkPage.getOrgNetwrkTermCode();
										String actDirectoryInd = reimbNtwrkPage.getOrgDirIndicator();
										String actAccPatientaInd = reimbNtwrkPage.getOrgAcceptingPatientsIndicator();
										String actTimelyFilingInd = reimbNtwrkPage.getOrgTimlyFiling();
										String actParIndicator = reimbNtwrkPage.getOrgPARIndicator();

										LOGGER.info("Actual source value : " + actNtwrkSrcSystem);
										LOGGER.info("Actual network ID" + actNtwrkId);
										LOGGER.info("Actual eff date : " + actNtwrkEffDate);
										LOGGER.info("Actual termination Date: " + actNtwrkTermDate);
										LOGGER.info("Actual termination Code" + actNtwrkTermReasonCode);
										LOGGER.info("Actual Directory indicator" + actDirectoryInd);
										LOGGER.info("Actual Accepting Patients Indicator: " + actAccPatientaInd);

										result = actNtwrkSrcSystem.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "NetworkSourceSystem value is not matching with the actual value"
												: "NetworkSourceSystem value is matching with the actual value";
										resultSet.add(new ResultTuple("NetworkSourceSystem value ",
												"Actual value: " + actNtwrkSrcSystem, " Expected :QCARE ", result,
												comment));

										result = actNtwrkId.equalsIgnoreCase(expNtwrkId) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Organization Network ID is not matching with the actual value"
												: "Organization Network ID is matching with the actual value";
										resultSet.add(new ResultTuple("Organization Network ID ",
												"Actual value: " + actNtwrkId, " Expected : " + expNtwrkId, result,
												comment));

										result = actNtwrkEffDate.equalsIgnoreCase(expNtwrkEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Network Effective date is not matching with the actual value"
												: "Network Effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Network Effective date ",
												"Actual value: " + actNtwrkEffDate, " Expected : " + expNtwrkEffDate,
												result, comment));

										result = actNtwrkTermDate.equalsIgnoreCase(expNtwrkTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Network termination date is not matching with the actual value"
												: "Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermDate, " Expected : " + expNtwrkTermDate,
												result, comment));

										result = actNtwrkTermReasonCode.equalsIgnoreCase(expNtwrkTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Network termination date is not matching with the actual value"
												: "The Network termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Network termination date ",
												"Actual value: " + actNtwrkTermReasonCode,
												" Expected : " + expNtwrkTermReasonCode, result, comment));

										result = actDirectoryInd.equalsIgnoreCase(expDirectoryInd) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Directory indicator value is not matching with the actual value"
												: "The Directory indicator value is matching with the actual value";
										resultSet.add(new ResultTuple("Directory indicator value ",
												"Actual value: " + actDirectoryInd, " Expected : " + expDirectoryInd,
												result, comment));

										result = actParIndicator.equalsIgnoreCase(expPARIndicator) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "PAR Indicator is not matching with the actual value"
												: "PAR Indicator is matching with the actual value";
										resultSet.add(
												new ResultTuple("PAR Indicator", "Actual value: " + actParIndicator,
														"Expected : " + expPARIndicator, result, comment));
										List<AcceptingPatientsInd> accPatientsIndList = ntwrkAffil
												.getAcceptingPatientsInds();
										if (accPatientsIndList != null) {
											for (AcceptingPatientsInd expAccPatientsInd : accPatientsIndList) {
												LOGGER.info("Accepting patient indicators: "
														+ expAccPatientsInd.getAcceptingPatientsIndicator());

												result = expAccPatientsInd.getAcceptingPatientsIndicator()
														.equalsIgnoreCase(actAccPatientaInd) ? PASSED : FAILED;
												comment = result.equals(FAILED)
														? "The accepting patients indicator value is not matching with the actual value"
														: "The accepting patients indicator value is matching with the actual value";
												resultSet.add(new ResultTuple("Accepting patients indicator value ",
														"Actual value: " + actAccPatientaInd,
														" Expected : "
																+ expAccPatientsInd.getAcceptingPatientsIndicator(),
														result, comment));
											}
										}

										result = actTimelyFilingInd.equalsIgnoreCase(EpdsConstants.TIMELY_FILING)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The timely filing indicator is not matching with the actual value"
												: "The timely filing indicator is matching with the actual value";
										resultSet.add(new ResultTuple("Timely filing indicator ",
												"Actual value: " + actTimelyFilingInd, " Expected :180 ", result,
												comment));

										List<Specialty> SpecialtyList = json.getSpecialties();
										if (SpecialtyList != null) {
											for (Specialty Speciality : SpecialtyList) {
												List<NetworkAffiliation> Netaffliation = Speciality
														.getNetworkAffiliations();
												for (NetworkAffiliation netwrkDetails : Netaffliation) {
													if (netwrkDetails.getNetworkID()
															.equals(ntwrkAffil.getNetworkID())) {
														result = reimbNtwrkPage.searchSpeciality(Speciality) ? PASSED
																: FAILED;
														comment = result.equals(FAILED)
																? "Speciality code is not available with the matching values"
																: "Speciality code is available with the matching values";
														resultSet
																.add(new ResultTuple("Speciality Code", "Actual code: ",
																		"Expected  code: "
																				+ Speciality.getSpecialtyCode(),
																		result, comment));
													}
													LOGGER.info("No specialty record available for the network");
												}
											}
										}

										reimbNtwrkPage.orgNetworkDetailsClose();
									}
								}

								List<Reimbursement> reimbList = affiliation.getReimbursements();
								if (reimbList != null) {

									for (Reimbursement reimbursement : reimbList) {

										result = reimbNtwrkPage.filterAndSelectReimbIdWithNetID(ntwrkAddr,
												reimbursement) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "Reimbursement  record is not available with the matching values"
												: "Reimbursement  record is available with the matching values";
										resultSet.add(new ResultTuple("Reimbursement details", "",
												"Expected address: " + ntwrkAddr.getAddressDetails().getAddressLine1()
														+ "Expected reimbursement value: "
														+ reimbursement.getReimbursementValue()
														+ "Expected reimbursement effective date: "
														+ reimbursement.getReimbursementActive().getEffectiveDate()
														+ "Expected reimbursement termination date: "
														+ reimbursement.getReimbursementActive().getTerminationDate(),
												result, comment));
										if (result == FAILED)
											continue;

										String expReimbValue = reimbursement.getReimbursementValue();
										String expReimbEffDate = reimbursement.getReimbursementActive()
												.getEffectiveDate();
										String expReimbTermDate = reimbursement.getReimbursementActive()
												.getTerminationDate();
										String expReimbTermReasonCode = Objects.toString(
												reimbursement.getReimbursementActive().getTerminationReasonCode(), "");

										String actReimbSrc = reimbNtwrkPage.getOrgReimbursementSource();
										LOGGER.info("Actual Source value: " + actReimbSrc);

										String actReimbEffDate = reimbNtwrkPage.getOrgReimbursementEffdate();
										LOGGER.info("Actual Reimbursement effective date: " + actReimbEffDate);

										String actReimbValue = reimbNtwrkPage.getOrgReimbursementValue();
										LOGGER.info("Actual Reimbursement value: " + actReimbValue);

										String actReimbTermDate = reimbNtwrkPage.getOrgReimbursementTermDate();
										LOGGER.info("Actual termination date: " + actReimbTermDate);

										String actReimbTermReasonCode = reimbNtwrkPage.getOrgReimbursementTermCode();
										LOGGER.info("Actual termination code: " + actReimbTermReasonCode);

										result = actReimbSrc.equalsIgnoreCase("QCARE") ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Source value is not matching with the actual value"
												: "The Source value is matching with the actual value";
										resultSet.add(new ResultTuple("Source Value", "Actual value: " + actReimbSrc,
												" Expected : QCARE", result, comment));

										result = actReimbValue.equalsIgnoreCase(expReimbValue) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement reason value is not matching with the actual value"
												: "The Reimbursement reason value is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement reason value ",
												"Actual value: " + actReimbValue, " Expected : " + expReimbValue,
												result, comment));

										result = actReimbEffDate.equalsIgnoreCase(expReimbEffDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement effective date is not matching with the actual value"
												: "The Reimbursement effective date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement effective date ",
												"Actual value: " + actReimbEffDate, " Expected : " + expReimbEffDate,
												result, comment));

										result = actReimbTermDate.equalsIgnoreCase(expReimbTermDate) ? PASSED : FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination date is not matching with the actual value"
												: "The Reimbursement termination date is matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination date ",
												"Actual value: " + actReimbTermDate, " Expected : " + expReimbTermDate,
												result, comment));

										result = actReimbTermReasonCode.equalsIgnoreCase(expReimbTermReasonCode)
												? PASSED
												: FAILED;
										comment = result.equals(FAILED)
												? "The Reimbursement termination code is not matching with the actual value"
												: "The Reimbursement termination is not matching with the actual value";
										resultSet.add(new ResultTuple("Reimbursement termination code ",
												"Actual value: " + actReimbTermReasonCode,
												" Expected : " + expReimbTermReasonCode, result, comment));

										reimbNtwrkPage.orgNetworkDetailsClose();
									}
								}
							}
						}
					}

					// Legacy info

					loginPage.navToLegacyInfoTab();
					legacyInfoPage.selectLegacyFieldDropdown();
					// String expContractIndicator = json.getLegacy().getContractIndicator();
					String expContractIndicator = Objects.toString(json.getLegacy().getContractIndicator(), "");
					String actContractIndicator = legacyInfoPage.legacyContractIndicator();
					if (!actContractIndicator.contains(expContractIndicator)) {
						resultSet.add(new ResultTuple("IndNetwrkTermDate", actContractIndicator, expContractIndicator,
								FAILED, "ContractIndicator not matching"));
					} else {
						resultSet.add(new ResultTuple("ContractIndicator", actContractIndicator, expContractIndicator,
								PASSED, "ContractIndicator matching"));
					}

					String expNWMIndicator = json.getLegacy().getNwm();
					String actNWMIndicator = legacyInfoPage.nwmInd();
					if (!actContractIndicator.contains(expNWMIndicator)) {
						resultSet.add(new ResultTuple("NWMIndicator", actNWMIndicator, expNWMIndicator, FAILED,
								"NWMIndicator not matching"));
					} else {
						resultSet.add(new ResultTuple("NWMIndicator", actNWMIndicator, expNWMIndicator, PASSED,
								"NWMIndicator matching"));
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

}
