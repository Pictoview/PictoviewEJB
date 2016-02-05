package com.viewer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.viewer.dto.UserInfoDTO;

public class SQLAccountDAO implements AccountDAO {

	@Override
	public long registerUser(String username, byte[] passkey, String name, boolean gender) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "INSERT INTO Users VALUES (NULL, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, username);
		stmt.setBytes(2, passkey);

		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}

		sql = "INSERT INTO UserInfo VALUES (?, ?, ?, '', NULL)";
		PreparedStatement stmt2 = conn.prepareStatement(sql);
		stmt2.setLong(1, id);
		stmt2.setBoolean(2, gender);
		stmt2.setString(3, name);

		// conn.close();
		return id;
	}

	@Override
	public boolean updateUserInfo(long uid, String name, boolean gender) throws SQLException {
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
