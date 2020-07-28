package com.tests.mdxjson;

import java.sql.Connection;
import java.util.LinkedHashSet;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.scripted.database.SQLServerUtil;
import com.utilities.mdxjson.MdxJsonTestUtilities;
import com.utilities.mdxjson.ResultTuple;

public class MdxJsonTestBaseClass {

	public static Logger LOGGER = Logger.getLogger(MdxJsonTestBaseClass.class);

	SQLServerUtil sqlUtil = new SQLServerUtil();
	MdxJsonTestUtilities mdxTestUtil = new MdxJsonTestUtilities();

	public static Connection mdxDbConn = null;
	public static LinkedHashSet<ResultTuple> resultList = new LinkedHashSet<ResultTuple>();

	public final static String PASSED = "PASSED";
	public final static String FAILED = "FAILED";
	public final static String emptyStr = "";
	public final static String TRUE = "TRUE";

	@BeforeTest
	public void beforeTest() throws Exception {
		mdxDbConn = mdxTestUtil.connectToMdxDB();
	}

	@AfterTest
	public void afterTest() throws Exception {
		mdxDbConn.close();
	}

	public void compareStrings(String fieldName, String recordValue, String jsonValue) {

		jsonValue = Objects.toString(jsonValue, "");
		if (!recordValue.equalsIgnoreCase(jsonValue)) {
			System.out.println(fieldName + " : " + jsonValue + " : " + recordValue + " : " + "FAILURE");
			resultList.add(new ResultTuple(fieldName, jsonValue, recordValue, FAILED, "Value mismatch"));
		} else {
			System.out.println(fieldName + " : " + jsonValue + " : " + recordValue + " : " + "SUCCESS");
		}
	}

	public void containedInString(String fieldName, String recordValue, String jsonValue) {

		if (recordValue == emptyStr) {
			if (jsonValue != emptyStr) {
				System.out.println(fieldName + " : " + jsonValue + " : " + recordValue + " : " + "FAILURE");
				resultList.add(new ResultTuple(fieldName, jsonValue, recordValue, FAILED, "Value mismatch"));
			}
		} else {
			if (!jsonValue.contains(recordValue)) {
				System.out.println(fieldName + " : " + jsonValue + " : " + recordValue + " : " + "FAILURE");
				resultList.add(new ResultTuple(fieldName, jsonValue, recordValue, FAILED, "Value mismatch"));
			} else {
				System.out.println(fieldName + " : " + jsonValue + " : " + recordValue + " : " + "SUCCESS");
			}
		}
	}

	public boolean isContainedInString(String recordValue, String jsonValue) {

		if (recordValue == emptyStr) {
			if (jsonValue != emptyStr)
				return false;
			else
				return true;
		} else {
			if (!jsonValue.contains(recordValue))
				return false;
			else
				return true;
		}
	}

}
