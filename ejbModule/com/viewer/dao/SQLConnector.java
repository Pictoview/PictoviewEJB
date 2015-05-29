package com.viewer.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnector {

	public static Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager
					.getConnection("jdbc:sqlite:E:/PictoViewDB/database/pdb.db");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return con;
	}
}