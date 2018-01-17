package com.viewer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.viewer.file.ConfigProperties;

public class SQLConnector {

	private static Connection con = null;

	public static Connection connect() {
		// Connection con = null;
		if (con == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(ConfigProperties.getProperty("jdbcLoc"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return con;
	}
	
	public static Connection connectTestDB() {
		if (con == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(ConfigProperties.getProperty("jdbcTestLoc"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return con;
	}
	
	public static boolean disconnectTestDB() {
		try {
			if (con == null || con.isClosed()) return true;
			else con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean disconnect() {
		try {
			if (con == null || con.isClosed()) return true;
			else con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}