package com.viewer.beans;

import java.sql.SQLException;

import javax.ejb.Stateless;

import com.viewer.dao.AccountDAO;
import com.viewer.dao.impl.SQLAccountDAO;
import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserCredentialDTO;

@Stateless
public class AccountBean implements AccountBeanLocal {
	AccountDAO accountDAO;

	public AccountBean() {
		accountDAO = new SQLAccountDAO();
	}

	public long registerUser(UserDataDTO userInfo) {
		try {
			return accountDAO.registerUser(UserDataDTO.createRegularUser(userInfo.getUsername(), userInfo.getPasskey(),
					userInfo.getName(), userInfo.isGender(), userInfo.getEmail(), userInfo.getDescription()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean updateUserInfo(UserDataDTO userData) {
		try {
			return accountDAO.updateUserInfo(userData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean changePassword(UserCredentialDTO userCreds) {
		try {
			accountDAO.changePassword(userCreds);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public UserDataDTO fetchUserInformation(String username) {
		try {
			return accountDAO.fetchUserInformation(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserCredentialDTO verifyUser(String username) {
		try {
			return accountDAO.verifyUser(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
