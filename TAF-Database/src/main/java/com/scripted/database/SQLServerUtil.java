package com.scripted.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class SQLServerUtil {

	public static Logger LOGGER = Logger.getLogger(SQLServerUtil.class);

	public Connection connectToDB(String connectionURL) throws Exception {

		LOGGER.info("Inside SQLServerUtil connectToDB()");

		Connection conn = null;

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(connectionURL);

		LOGGER.info("SQL DB connection established successfully.");
		return (conn);
	}

	public ResultSet executeQuery(Connection conn, String query) {

		LOGGER.info("Inside SQLServerUtil executeQuery()");

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
