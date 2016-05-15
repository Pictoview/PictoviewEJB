package com.viewer.beans;

import javax.ejb.Local;

import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserCredentialDTO;

@Local
public interface AccountBeanLocal {
	/**
	 * Registers a new user
	 * 
	 * @param username
	 * @param passkey
	 * @param name
	 * @param gender
	 * @return Registered user id or -1 if unsuccessful
	 */
	public long registerUser(UserDataDTO userInfo);

	/**
	 * Changes the user's password if old password matches
	 * 
	 * @param userInfo
	 * @return Operation success status
	 */
	public boolean changePassword(UserCredentialDTO userInfo);

	/**
	 * Updates user personal information
	 * 
	 * @param userData
	 * @return Operation success status
	 */
	public boolean updateUserInfo(UserDataDTO userData);

	/**
	 * Checks if user entered correct information
	 * 
	 * @param username
	 * @param passkey
	 * @return User id or -1 upon failure lookup
	 */
	public UserCredentialDTO verifyUser(String username);

	/**
	 * Fetches the user's data
	 * @param username
	 * @return User data
	 */
	public UserDataDTO fetchUserInformation(String username);
}
