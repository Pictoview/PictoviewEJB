package com.viewer.dao;

import java.sql.SQLException;

import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserInfoDTO;

public interface AccountDAO {

	public long registerUser(UserDataDTO userData) throws SQLException;

	public boolean deleteUser(String username, byte[] passkey) throws SQLException;

	public boolean updateUserInfo(UserDataDTO userData) throws SQLException;

	public UserInfoDTO verifyUser(String username) throws SQLException;
}
