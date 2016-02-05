package com.viewer.dao;

import java.sql.SQLException;

import com.viewer.dto.UserInfoDTO;

public interface AccountDAO {

	public long registerUser(String username, byte[] passkey, String name, boolean gender) throws SQLException;

	public boolean updateUserInfo(long uid, String name, boolean gender) throws SQLException;

	public UserInfoDTO verifyUser(String username) throws SQLException;
}
