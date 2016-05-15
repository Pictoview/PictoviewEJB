package com.viewer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.viewer.dao.AccountDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserCredentialDTO;
import com.viewer.util.StringUtils;

/**
 * Sqlite Implementation for Account related stuff
 */
public class SQLAccountDAO implements AccountDAO {

	@Override
	public long registerUser(UserDataDTO userData) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "INSERT INTO Users VALUES (NULL, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, userData.getUsername());
		stmt.setString(2, userData.getPasskey());
		stmt.setInt(3, userData.getRole());
		stmt.setInt(4, userData.getPoints());
		stmt.setString(5, userData.getStatus());

		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}

		sql = "INSERT INTO UserInfo VALUES (NULL, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt2 = conn.prepareStatement(sql);
		stmt2.setLong(1, id);
		stmt2.setString(2, userData.getName());
		stmt2.setBoolean(3, userData.isGender());
		stmt2.setString(4, userData.getEmail());
		stmt2.setString(5, userData.getAddress());
		stmt2.setString(6, userData.getDescription());
		stmt2.executeUpdate();
		stmt2.close();

		// conn.close();
		return id;
	}

	@Override
	public boolean deleteUser(String username, String passkey) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "DELETE FROM Users WHERE username = ? AND passkey = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setString(2, passkey);
		stmt.executeUpdate();
		stmt.close();

		// conn.close();
		return true;
	}

	@Override
	public boolean changePassword(UserCredentialDTO userCreds) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "UPDATE Users SET passkey = ? WHERE Users.username = ? AND Users.passkey = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, userCreds.getPasskey());
		stmt.setString(2, userCreds.getUsername());
		stmt.setString(3, userCreds.getOldPassword());
		return stmt.executeUpdate() > 0;
	}

	@Override
	public boolean updateUserInfo(UserDataDTO userData) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "UPDATE UserInfo SET";
		if (StringUtils.notNullEmpty(userData.getName())) sql += " name=?";
		if (StringUtils.notNullEmpty(userData.getEmail())) sql += " email=?";
		if (StringUtils.notNullEmpty(userData.getAddress())) sql += " address=?";
		if (StringUtils.notNullEmpty(userData.getDescription())) sql += " description=?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		int index = 1;
		if (StringUtils.notNullEmpty(userData.getName())) {
			stmt.setString(index++, userData.getName());
			index++;
		}
		if (StringUtils.notNullEmpty(userData.getEmail())) {
			stmt.setString(index++, userData.getEmail());
			index++;
		}
		if (StringUtils.notNullEmpty(userData.getAddress())) {
			stmt.setString(index++, userData.getAddress());
			index++;
		}
		if (StringUtils.notNullEmpty(userData.getDescription())) {
			stmt.setString(index++, userData.getDescription());
			index++;
		}
		return stmt.executeUpdate() > 0;
	}

	@Override
	public UserCredentialDTO verifyUser(String username) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Users.uid, Users.passkey FROM Users LEFT JOIN UserInfo ON Users.uid = UserInfo.id WHERE Users.username = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		UserCredentialDTO usi = null;
		if (rs.next()) {
			usi = new UserCredentialDTO(rs.getLong(1), username, rs.getString(2), true);
		}
		// conn.close();
		return usi;
	}

	public UserDataDTO fetchUserInformation(String username) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Users.uid, UserInfo.gender, UserInfo.email, UserInfo.description, UserInfo.address "
				+ " FROM Users LEFT JOIN UserInfo ON Users.uid = UserInfo.id WHERE Users.username = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		UserDataDTO usi = null;
		if (rs.next()) {
			usi = UserDataDTO.createRegularUser(username, "", rs.getString(1), rs.getBoolean(2), rs.getString(3),
					rs.getString(4));
		}
		// conn.close();
		return usi;
	}
}
