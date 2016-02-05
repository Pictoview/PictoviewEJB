package com.viewer.beans;

import javax.ejb.Local;

import com.viewer.dto.UserInfoDTO;

@Local
public interface AccountBeanLocal {
	/**
	 * Registers a new user
	 * 
	 * @param username
	 * @param passkey
	 * @param name
	 * @param gender
	 * @return Registered user id
	 */
	public long registerUser(String username, String passkey, String name, boolean gender);

	public boolean updateUserInfo(long uid, String name, boolean gender);

	/**
	 * Checks if user entered correct information
	 * 
	 * @param username
	 * @param passkey
	 * @return User id or -1 upon failure lookup
	 */
	public UserInfoDTO verifyUser(String username);
}
