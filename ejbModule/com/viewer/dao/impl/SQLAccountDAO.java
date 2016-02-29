package com.viewer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.viewer.dao.AccountDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserInfoDTO;

public class SQLAccountDAO implements AccountDAO {

	@Override
	public long registerUser(UserDataDTO userData) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "INSERT INTO Users VALUES (NULL, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, userData.getUsername());
		stmt.setBytes(2, userData.getPasskey());
		stmt.setInt(3, userData.getRole());
		stmt.setInt(4, userData.getPoints());
		stmt.setString(5, userData.getStatus());

		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}

		sql = "INSERT INTO UserInfo VALUES (NULL, ?, ?, ?, ?, ?)";
		PreparedStatement stmt2 = conn.prepareStatement(sql);
		stmt2.setLong(1, id);
		stmt2.setString(2, userData.getName());
		stmt2.setBoolean(3, userData.isGender());
		stmt2.setString(4, userData.getAddress());
		stmt2.setString(5, userData.getDescription());
		stmt2.executeUpdate();
		stmt2.close();

		// conn.close();
		return id;
	}

	@Override
	public boolean deleteUser(String username, byte[] passkey) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "DELETE FROM Users WHERE username = ? AND passkey = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setBytes(2, passkey);
		stmt.executeUpdate();
		stmt.close();

		// conn.close();
		return true;
	}

	@Override
	public boolean updateUserInfo(UserDataDTO userData) throws SQLException {
		return true;
	}

	@Override
	public UserInfoDTO verifyUser(String username) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Users.uid, UserInfo.name, Users.passkey FROM Users LEFT JOIN UserInfo ON Users.uid = UserInfo.id WHERE Users.username = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		UserInfoDTO usi = null;
		if (rs.next()) {
			usi = new UserInfoDTO(rs.getLong(1), username, rs.getString(2), rs.getString(3), true);
		}
		// conn.close();
		return usi;
	}
}
