package com.viewer.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import com.viewer.file.ConfigProperties;

public class SQLConnector {

	public static Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager
					.getConnection(ConfigProperties.getProperty("jdbcLoc"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return con;
	}
}