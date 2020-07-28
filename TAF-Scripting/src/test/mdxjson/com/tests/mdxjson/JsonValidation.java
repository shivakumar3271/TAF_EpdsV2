package com.tests.mdxjson;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.beans.mdxjson.OrgNameQualifier;
import com.beans.mdxjson.RPAJsonTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utilities.mdxjson.MdxJsonReporting;
import com.utilities.mdxjson.MdxOdsmRecordQueries;
import com.utilities.mdxjson.ProviderDetail;
import com.utilities.mdxjson.ResultTuple;

public class JsonValidation extends MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(JsonValidation.class);

	@Test(description = "Validate MDX Jsons for Individual Provider", enabled = true)
	public void validateIndJson() {

		LOGGER.info("Inside test : Validating Individual Jsons...");
		RPAJsonTransaction dummyJson = null;
		// get distinct ind prov
		// for every ind prov
		LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>> consolidatedResultMap = new LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>>();
		ProviderDetail provDetail = null;
		resultList.clear();

		ResultSet rs = sqlUtil.executeQuery(mdxDbConn, MdxOdsmRecordQueries.ODSM_REC_QUERY_ORG_AFFIL);
		try {
			if (rs.next()) {

//				ResultSet jsonResultset = sqlUtil.executeQuery(mdxDbConn, MdxJsonQueries.MDX_JSON_IND_TXN_QUERY);
//				jsonResultset.next();
//				String jsonStr = jsonResultset.getString("Request");
//				RPAJsonTransaction jsonObj = rpaTestUtil.jacksonParser(jsonStr);

				RPAJsonTransaction jsonObj = mdxTestUtil.jacksonParser();
				dummyJson = mdxTestUtil.jacksonParser();

				int i = 1;
				do {
					LinkedHashMap<String, Object> recordMap = mdxTestUtil.convertResultsetToMap(rs);
					if (i == 1)
						provDetail = new ProviderDetail(recordMap.get("header_legacysystemid").toString(),
								recordMap.get("header_spsids").toString(),
								recordMap.get("profile_providertype").toString());

					for (String columnkey : recordMap.keySet()) {
						String value = recordMap.get(columnkey).toString().trim();

						System.out.println("FIELDNAME : " + columnkey);
						compare(columnkey, value, recordMap, jsonObj, dummyJson);
					}

					i++;
				} while (rs.next() && i < 5);
			}

			System.out.println("Dummy JSON : " + printJson(dummyJson));
			ArrayList<String> invalidNodeList = new DummyJsonValidation().validateDummyJson(dummyJson);
			String invalidNodes = "";
			for (String invalidNode : invalidNodeList) {
				invalidNodes += invalidNode + "\n";
			}
			resultList.add(new ResultTuple("Invalid Nodes", invalidNodes, "", FAILED,
					"These fields from Json are not present in ODSM records"));

			consolidatedResultMap.put(provDetail, resultList);

			Set<ProviderDetail> provDetailSet = consolidatedResultMap.keySet();
			for (ProviderDetail providerDetail : provDetailSet) {
				LinkedHashSet<ResultTuple> resList = consolidatedResultMap.get(providerDetail);
				for (ResultTuple rt : resList) {
					LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result
							+ " : " + rt.comments);
				}
			}

			new MdxJsonReporting().writeDetailedReport(consolidatedResultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void compare(String key, String value, LinkedHashMap<String, Object> recordHashMap, RPAJsonTransaction json,
			RPAJsonTransaction dummyJson) {

		switch (key) {
		case "header_legacyid":
			String legacyId = json.getHeader().getLegacyID();
			compareStrings("Legacy ID", value, legacyId);
			dummyJson.getHeader().setLegacyID("TRUE");
			break;

		case "header_legacysystemid":
			String legacySmId = json.getHeader().getLegacySystemID();
			compareStrings("Legacy System ID", value, legacySmId);
			dummyJson.getHeader().setLegacySystemID("TRUE");
			break;

		case "header_legacysystemname":
			String legacySmName = json.getHeader().getLegacySystemName();
			compareStrings("Legacy System Name", value, legacySmName);
			dummyJson.getHeader().setLegacySystemName("TRUE");
			break;

		case "header_spsids":
			String spsIds = "";
			if (json.getHeader() != null) {
				spsIds = Objects.toString(json.getHeader().getSpsIDs(), "");
				
				List<String> spsIdList = dummyJson.getHeader().getSpsIDs();
				if (spsIdList != null) {
					spsIdList.remove(value);
					dummyJson.getHeader().setSpsIDs(spsIdList);
				}
			}
			containedInString("SPS IDs", value, spsIds);
			break;

		case "profile_providertype":
			new ProfileValidation().validateProvider(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_namequalifier_groupingnamequalifier_groupingtype":
			new ProfileValidation().validateGrpNameQualifier(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_namequalifier_individualnamequalifier_profilename_firstname":
			new ProfileValidation().validateIndNameQualifier(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_namequalifier_individualnamequalifier_aliasname_firstname":
			new ProfileValidation().validateAliasNames(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_namequalifier_orgnamequalifier_orgname":
			String orgName = "";
			if (json.getProfile() != null && json.getProfile().getNameQualifier() != null) {
				OrgNameQualifier orgNameQualifier = json.getProfile().getNameQualifier().getOrgNameQualifier();
				if (orgNameQualifier != null) {
					orgName = orgNameQualifier.getOrgName();
					dummyJson.getProfile().getNameQualifier().getOrgNameQualifier().setOrgName("TRUE");
				}
			}
			compareStrings("Org Name", value, orgName);
			break;

		case "profile_taxids_taxidvalue":
			new ProfileValidation().validateTaxIds(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_areasoffocus_areasoffocus":
			new ProfileValidation().validateAreasOfFocus(recordHashMap, json.getProfile(), dummyJson.getProfile());
			break;

		case "profile_providerlanguages":
			String provLang = "";
			if (json.getProfile() != null) {
				provLang = Objects.toString(json.getProfile().getProviderLanguages(), "");
				
				List<String> provLangList = dummyJson.getProfile().getProviderLanguages();
				if (provLangList != null) {
					provLangList.remove(value);
					dummyJson.getProfile().setProviderLanguages(provLangList);
				}
			}
			containedInString("Prov Language", value, provLang);
			break;

		case "profileadditionaldetails_pdmindicatordetails_pdmindicator":
			new ProfAddDetailsValidation().validatePdmIndicatorDetails(recordHashMap,
					json.getProfileAdditionalDetails(), dummyJson.getProfileAdditionalDetails());
			break;

		case "profileadditionaldetails_credentialingdetails_credentialingsource":
			new ProfAddDetailsValidation().validateCredentialingDetails(recordHashMap,
					json.getProfileAdditionalDetails(), dummyJson.getProfileAdditionalDetails());
			break;

		case "profileadditionaldetails_providerdistinctiondetails_providerdistinctioncode":
			new ProfAddDetailsValidation().validateProviderDistinctDetails(recordHashMap,
					json.getProfileAdditionalDetails(), dummyJson.getProfileAdditionalDetails());
			break;

		case "profileadditionaldetails_ebpprograms_ebpprogramid":
			new ProfAddDetailsValidation().validateEbpPrograms(recordHashMap, json.getProfileAdditionalDetails(),
					dummyJson.getProfileAdditionalDetails());
			break;

		case "profileadditionaldetails_bundledpaymentprocesses_bundledpaymentprocessmode":
			new ProfAddDetailsValidation().validateBundledPaymentProcess(recordHashMap,
					json.getProfileAdditionalDetails(), dummyJson.getProfileAdditionalDetails());
			break;

		case "alternateids_npidetails_npieligibilitytype":
			new AlternateIdValidation().validateNpiDetails(recordHashMap, json.getAlternateIDs(),
					dummyJson.getAlternateIDs());
			break;

		case "alternateids_altiddetails_altidtype":
			new AlternateIdValidation().validateAltIdDetails(recordHashMap, json.getAlternateIDs(),
					dummyJson.getAlternateIDs());
			break;

		case "specialties_billingform":
			new SpecialtyValidation().validateSpecialty(recordHashMap, json.getSpecialties(),
					dummyJson.getSpecialties());
			break;

		case "educationdetails_educationtypecode":
			new EducationDetailsValidation().validateEducationDetails(recordHashMap, json.getEducationDetails(),
					dummyJson.getEducationDetails());
			break;

		case "addresses_addressdetails_addresstype":
			new AddressValidation().validateAddressDetails(recordHashMap, json.getAddresses(),
					dummyJson.getAddresses());
			break;

		case "affiliations_affiliatedlegacyid":
			new AffiliationValidation().validateAffiliationDetails(recordHashMap, json.getAffiliations(),
					dummyJson.getAffiliations());
			break;

		case "claimactionsanctions_claimactionsanctiontype":
			new ClaimActnSnctnValidation().validateClaimAction(recordHashMap, json.getClaimActionSanctions(),
					dummyJson.getClaimActionSanctions());
			break;

		case "legacy_contractindicator":
			String contractInd = "";
			if (json.getLegacy() != null) {
				contractInd = json.getLegacy().getContractIndicator();
				dummyJson.getLegacy().setContractIndicator("TRUE");
			}
			compareStrings("Legacy - Contract Ind", value, contractInd);
			break;

		case "legacy_nwm":
			String legacyNwm = "";
			if (json.getLegacy() != null) {
				legacyNwm = json.getLegacy().getNwm();
				dummyJson.getLegacy().setNwm("TRUE");
			}
			compareStrings("Legacy - Contract nwm", value, legacyNwm);
			break;

		default:
			break;
		}
	}

	@Test(description = "Validate MDX Jsons for Provider Organization", enabled = false)
	public void validateOrgJson() {

		LOGGER.info("Inside test : Validating Organization Jsons...");

		// get distict ind prov
		// for every ind prov
		LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>> consolidatedResultMap = new LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>>();
		ProviderDetail provDetail = null;
		resultList.clear();

		ResultSet rs = sqlUtil.executeQuery(mdxDbConn, MdxOdsmRecordQueries.ODSM_REC_QUERY_ORG_ADDR);
		try {
			if (rs.next()) {

//				ResultSet jsonResultset = sqlUtil.executeQuery(mdxDbConn, MdxJsonQueries.MDX_JSON_IND_TXN_QUERY);
//				jsonResultset.next();
//				String jsonStr = jsonResultset.getString("Request");
//				RPAJsonTransaction jsonObj = rpaTestUtil.jacksonParser(jsonStr);

				RPAJsonTransaction jsonObj = mdxTestUtil.jacksonParser();
				RPAJsonTransaction dummyJson = mdxTestUtil.jacksonParser();

				int i = 1;
				do {
					LinkedHashMap<String, Object> recordMap = mdxTestUtil.convertResultsetToMap(rs);
					if (i == 1)
						provDetail = new ProviderDetail(recordMap.get("header_legacysystemid").toString(),
								recordMap.get("header_spsids").toString(),
								recordMap.get("profile_providertype").toString());

					for (String columnkey : recordMap.keySet()) {
						String value = recordMap.get(columnkey).toString().trim();

						System.out.println("FIELDNAME : " + columnkey);
						compare(columnkey, value, recordMap, jsonObj, dummyJson);
					}

					i++;
				} while (rs.next() && i < 5);
			}

			consolidatedResultMap.put(provDetail, resultList);

			Set<ProviderDetail> provDetailSet = consolidatedResultMap.keySet();
			for (ProviderDetail providerDetail : provDetailSet) {
				LinkedHashSet<ResultTuple> resList = consolidatedResultMap.get(providerDetail);
				for (ResultTuple rt : resList) {
					LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result
							+ " : " + rt.comments);
				}
			}

			new MdxJsonReporting().writeDetailedReport(consolidatedResultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(description = "Validate MDX Jsons for Provider Grouping", enabled = false)
	public void validateGrpJson() {

		LOGGER.info("Inside test : Validating Grouping Jsons...");

		// get distict ind prov
		// for every ind prov
		LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>> consolidatedResultMap = new LinkedHashMap<ProviderDetail, LinkedHashSet<ResultTuple>>();
		ProviderDetail provDetail = null;
		resultList.clear();

		ResultSet rs = sqlUtil.executeQuery(mdxDbConn, MdxOdsmRecordQueries.ODSM_REC_QUERY_ORG_ADDR);
		try {
			if (rs.next()) {

//				ResultSet jsonResultset = sqlUtil.executeQuery(mdxDbConn, MdxJsonQueries.MDX_JSON_IND_TXN_QUERY);
//				jsonResultset.next();
//				String jsonStr = jsonResultset.getString("Request");
//				RPAJsonTransaction jsonObj = rpaTestUtil.jacksonParser(jsonStr);

				RPAJsonTransaction jsonObj = mdxTestUtil.jacksonParser();
				RPAJsonTransaction dummyJson = mdxTestUtil.jacksonParser();

				int i = 1;
				do {
					LinkedHashMap<String, Object> recordMap = mdxTestUtil.convertResultsetToMap(rs);
					if (i == 1)
						provDetail = new ProviderDetail(recordMap.get("header_legacysystemid").toString(),
								recordMap.get("header_spsids").toString(),
								recordMap.get("profile_providertype").toString());

					for (String columnkey : recordMap.keySet()) {
						String value = recordMap.get(columnkey).toString().trim();

						System.out.println("FIELDNAME : " + columnkey);
						compare(columnkey, value, recordMap, jsonObj, dummyJson);
					}

					i++;
				} while (rs.next() && i < 5);
			}

			consolidatedResultMap.put(provDetail, resultList);

			Set<ProviderDetail> provDetailSet = consolidatedResultMap.keySet();
			for (ProviderDetail providerDetail : provDetailSet) {
				LinkedHashSet<ResultTuple> resList = consolidatedResultMap.get(providerDetail);
				for (ResultTuple rt : resList) {
					LOGGER.info("RESULT : " + rt.fieldName + " : " + rt.actual + " : " + rt.expected + " : " + rt.result
							+ " : " + rt.comments);
				}
			}

			new MdxJsonReporting().writeDetailedReport(consolidatedResultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String printJson(RPAJsonTransaction dummyJson) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(dummyJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
