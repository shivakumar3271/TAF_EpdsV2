package com.utilities.epds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.beans.mdxjson.RPAJsonTransaction;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.database.SQLServerUtil;
import com.scripted.dataload.PropertyDriver;

public class RpaTestUtilities {

	public static Logger LOGGER = Logger.getLogger(RpaTestUtilities.class);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface TestDetails {

		String userstory() default "none";

		String transactionId() default "none";

		String sprint() default "none";

		String author() default "none";
	}

	public static enum ProviderType {
		INDIVIDUAL, ORGANIZATION, GROUP
	};

	public RPAJsonTransaction jacksonParser(String jsonExtract) {

		LOGGER.info("Inside RpaTestUtilities jacksonParser()");
		RPAJsonTransaction jsonObj = new RPAJsonTransaction();
		ObjectMapper mapper = new ObjectMapper();

		try {
			jsonObj = mapper.readValue(jsonExtract, RPAJsonTransaction.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	public String spsDbDateConversion(String dateStr) {

		Date date;
		String newDateStr = null;

		try {
			date = new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);
			DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
			newDateStr = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (newDateStr);
	}

	public Connection connectToMdxDB() {

		LOGGER.info("Inside RpaTestUtilities connectToMdxDB()");
		Connection mdxDbConn = null;
		Map<String, String> mdxPropertyMap = getMdxPropertyMap();

		String username = mdxPropertyMap.get("mdx_db_username");
		String password = mdxPropertyMap.get("mdx_db_password");
		String dbEnv = mdxPropertyMap.get("mdx_db_env");
		String dbName = mdxPropertyMap.get("mdx_db_name");

		try {
			String connectionURL = "jdbc:jtds:sqlserver://" + dbEnv + ":10001;" + "instanceName=SQL01;databaseName="
					+ dbName + ";"
					+ "authenticationScheme=NativeAuthentication;UseNTLMv2=true;Domain=DEVAD;Trusted_Connection=yes;"
					+ "user=" + username + ";password=" + password + ";ssl=request";
			mdxDbConn = new SQLServerUtil().connectToDB(connectionURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mdxDbConn;
	}

	public void closeMdxDBConn(Connection mdxDbConn) {

		LOGGER.info("Inside RpaTestUtilities closeMdxDBConn()");
		try {
			mdxDbConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> retrieveJsonExtracts(Connection mdxDbConn, String txnId) {

		LOGGER.info("Inside RpaTestUtilities retrieveJsonExtracts()");
		List<String> jsonExtractList = new ArrayList<String>();
		ResultSet rs = null;
		BufferedReader br = null;

		String query = "", jsonString = "", str = "";
		String jsonLocation = getEpdsProperty("rpa_json_location");
		String jsonFileLocation = getEpdsProperty("rpa_json_file_location");
		String jsonStrColName = getEpdsProperty("json_extract_column_label");

		try {
			if (jsonLocation.equals("0")) {
				if (txnId.startsWith("IND"))
					query = MdxJsonQueries.MDX_JSON_IND_TXN_QUERY;
				else if (txnId.startsWith("ORG"))
					query = MdxJsonQueries.MDX_JSON_ORG_TXN_QUERY;
				else if (txnId.startsWith("GRP"))
					query = MdxJsonQueries.MDX_JSON_GRP_TXN_QUERY;
				
				query = formatMdxJsonQuery(query, txnId);
				rs = new SQLServerUtil().executeQuery(mdxDbConn, query);

				if (rs.next()) {
					do {
						jsonExtractList.add(rs.getString(jsonStrColName));
					} while (rs.next());
				}
			} else {
				File jsonFile = new File(jsonFileLocation);
				br = new BufferedReader(new FileReader(jsonFile));

				while ((str = br.readLine()) != null) {
					jsonString = jsonString + str;
				}
				br.close();
				jsonExtractList.add(jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonExtractList;
	}

	public String formatMdxJsonQuery(String query, String txnId) {

		LOGGER.info("Inside RpaTestUtilities formatMdxJsonQuery()");

		String txnFlag = "'" + getEpdsProperty("transaction_based_flag") + "'";
		String addFlowTxns = getEpdsProperty("no_legacyId_txns");
		
		String addFlowFlag;
		if (addFlowTxns.contains(txnId))
			addFlowFlag = "'Y'";
		else
			addFlowFlag = "'N'";
		
		String sampleRecordsFlag = "'" + getEpdsProperty("sample_records_flag") + "'";
		
		String epdsIdList = "'" + getEpdsProperty("legacy_id_list") + "'";
		epdsIdList = epdsIdList.replaceAll(",", "','");
		String spsIdList = "'" + getEpdsProperty("sps_id_list") + "'";
		spsIdList = spsIdList.replaceAll(",", "','");
		
		String provFlag = "'" + getEpdsProperty("provider_based_flag") + "'";
		
		query = query.replaceAll("#TRANSACTION_BASED_FLAG#", txnFlag);
		query = query.replaceAll("#ADD_FLOW_FLAG#", addFlowFlag);
		query = query.replaceAll("#SAMPLE_RECORDS_FLAG#", sampleRecordsFlag);
		query = query.replaceAll("#LEGACY_ID#", epdsIdList);
		query = query.replaceAll("#SPS_ID#", spsIdList);
		query = query.replaceAll("#PROVIDER_BASED_FLAG#", provFlag);
		
		txnId = txnId.replaceAll("_", "[_]");
		query = query.replaceAll("#TRANSACTION_ID#", txnId);
		System.out.println(query);

		return query;
	}

	// This method will return the EPDS property map
	public Map<String, String> getEpdsPropertyMap() {
		LOGGER.info("Inside RpaTestUtilities getEpdsPropertyMap()");
		PropertyDriver epdsConfigProp = new PropertyDriver();
		epdsConfigProp.setPropFilePath("src\\main\\resources\\properties\\EpdsConfig.properties");
		return epdsConfigProp.readProp();
	}

	// This method will return the EPDS property value
	public String getEpdsProperty(String key) {
		LOGGER.info("Inside RpaTestUtilities getEpdsProperty()");
		PropertyDriver epdsConfigProp = new PropertyDriver();
		epdsConfigProp.setPropFilePath("src\\main\\resources\\properties\\EpdsConfig.properties");
		return epdsConfigProp.readProp(key);
	}

	// This method will return the SPS property map
	public Map<String, String> getSpsPropertyMap() {
		LOGGER.info("Inside RpaTestUtilities getSpsPropertyMap()");
		PropertyDriver spsConfigProp = new PropertyDriver();
		spsConfigProp.setPropFilePath("src\\main\\resources\\properties\\SpsConfig.properties");
		return spsConfigProp.readProp();
	}

	// This method will return the MDX property map
	public Map<String, String> getMdxPropertyMap() {
		LOGGER.info("Inside RpaTestUtilities getMdxPropertyMap()");
		PropertyDriver mdxConfigProp = new PropertyDriver();
		mdxConfigProp.setPropFilePath("src\\main\\resources\\properties\\MdxConfig.properties");
		return mdxConfigProp.readProp();
	}
}
