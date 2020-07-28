package com.scripted.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class OracleUtil {

	public static Logger LOGGER = Logger.getLogger(OracleUtil.class);

	public Connection connectToDB(String connectionURL, String username, String password) throws Exception {

		LOGGER.info("Inside OracleUtil connectToDB()");
		Connection conn = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(connectionURL, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		LOGGER.info("Oracle DB connection established successfully.");
		return (conn);
	}

	public ResultSet executeQuery(Connection conn, String query) {

		LOGGER.info("Inside OracleUtil executeQuery()");

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
