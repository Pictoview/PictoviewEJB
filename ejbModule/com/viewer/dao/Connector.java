package com.viewer.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {

	public static Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager
					.getConnection("jdbc:sqlite:C:/Users/ArthurXu/Pictures/pictureDB/pdb.db");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return con;
	}
}