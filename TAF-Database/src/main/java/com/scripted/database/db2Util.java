package com.scripted.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import junit.framework.Assert;

public class db2Util {

	private static Connection con;
	private static Statement st;
	private static ResultSet rs;
	public static Logger LOGGER = Logger.getLogger(db2Util.class);

	public static void connectToDB2db(String databaseURL, String username, String password) {
		con = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			LOGGER.info("Connecting to Database...");
			con = DriverManager.getConnection(databaseURL, username, password);
			if (con != null)
				LOGGER.info("Connection Established...");
		} catch (SQLException e) {
			LOGGER.error("Error while connecting to DB2"+"SQLException"+e.getMessage());
			Assert.fail("Error while connecting to DB2"+"SQLException"+e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error while connecting to DB2"+"SQLException"+e.getMessage());
			Assert.fail("Error while connecting to DB2"+"SQLException"+e.getMessage());
		}
	}

	public static String getResult(String query, String colName) {
		String result = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				result = rs.getString(colName);
			}
		} catch (SQLException e) {
			LOGGER.error("Error while getting results from DB2"+"SQLException"+e.getMessage());
			Assert.fail("Error while getting results from DB2"+"SQLException"+e.getMessage());
		}
		return result;
	}

	public static void executeSqlQuery(String query) {
		try {
			st = con.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			LOGGER.error("Error while executing query in  DB2 database"+"SQLException"+e.getMessage());
			Assert.fail("Error while executing query in  DB2 database"+"SQLException"+e.getMessage());
		}
	}

	public static void closeConnection() {
		if (con != null) {
			try {
				LOGGER.info("Closing Database Connection...");
				con.close();
			} catch (SQLException e) {
				LOGGER.error("Error while closing connection to DB2"+"SQLException"+e.getMessage());
				Assert.fail("Error while closing connection to DB2"+"SQLException"+e.getMessage());
			}
		}
	}
}
