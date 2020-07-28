package com.utilities.mdxjson;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.beans.mdxjson.RPAJsonTransaction;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.database.SQLServerUtil;
import com.scripted.dataload.PropertyDriver;

public class MdxJsonTestUtilities {

	public static Logger LOGGER = Logger.getLogger(MdxJsonTestUtilities.class);

	public Connection connectToMdxDB() {

		LOGGER.info("Inside MdxTestUtilities connectToMdxDB()");
		Connection mdxDbConn = null;
		Map<String, String> mdxPropertyMap = getMdxJsonPropertyMap();

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

	public LinkedHashMap<String, Object> convertResultsetToMap(ResultSet resultSet) throws SQLException {

		LinkedHashMap<String, Object> resultsMap = new LinkedHashMap<String, Object>();
		ResultSetMetaData rsmd = resultSet.getMetaData();

		int colCount = rsmd.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			String colName = rsmd.getColumnName(i).toLowerCase();
			Object object = Objects.toString(resultSet.getObject(i), "");
			resultsMap.put(colName, object);
		}
		return resultsMap;
	}

	public RPAJsonTransaction jacksonParser() {

		LOGGER.info("Inside RpaTestUtilities jacksonParser()");
		RPAJsonTransaction jsonObj = new RPAJsonTransaction();
		ObjectMapper mapper = new ObjectMapper();

		try {
			jsonObj = mapper.readValue(new File("src/main/resources/RPAJsonFiles/json_test_addr.json"),
					RPAJsonTransaction.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	// This method will return the MDX Json property map
	public Map<String, String> getMdxJsonPropertyMap() {
		LOGGER.info("Inside MdxTestUtilities getMdxJsonPropertyMap()");
		PropertyDriver mdxJsonConfigProp = new PropertyDriver();
		mdxJsonConfigProp.setPropFilePath("src\\main\\resources\\properties\\MdxJsonConfig.properties");
		return mdxJsonConfigProp.readProp();
	}
}
