package com.viewer.dao;

import java.sql.SQLException;

import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserCredentialDTO;

public interface AccountDAO {

	public long registerUser(UserDataDTO userData) throws SQLException;

	public boolean deleteUser(String username, String passkey) throws SQLException;

	public boolean changePassword(UserCredentialDTO userCreds) throws SQLException;

	public boolean updateUserInfo(UserDataDTO userData) throws SQLException;

	public UserCredentialDTO verifyUser(String username) throws SQLException;
	
	public UserDataDTO fetchUserInformation(String username) throws SQLException;
}
