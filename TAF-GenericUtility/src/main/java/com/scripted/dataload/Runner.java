package com.scripted.dataload;

import java.util.LinkedHashMap;
import java.util.Map;

public class Runner {

	public static void main(String[] args) {
        String filename = "D:\\UserData\\data.xlsx";
        String sheetname = "TC001";
        String key = "";

        ExcelConnector con = new ExcelConnector(filename, "Sheet1");
        con.setMaxRows(100);
        con.setHasHeader(false);
        TestDataFactory test = new TestDataFactory();
        TestDataObject obj = test.GetData(key, con);

        // Get the whole data
        LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();
        System.out.println(getAllData);

	}

}
