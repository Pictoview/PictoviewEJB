package com.viewer.beans;

import java.sql.SQLException;

import javax.ejb.Stateless;

import com.viewer.dao.AccountDAO;
import com.viewer.dao.impl.SQLAccountDAO;
import com.viewer.dto.UserDataDTO;
import com.viewer.dto.UserInfoDTO;

@Stateless
public class AccountBean implements AccountBeanLocal {
	AccountDAO accountDAO;

	public AccountBean() {
		accountDAO = new SQLAccountDAO();
	}

	public long registerUser(String username, String passkey, String name, boolean gender) {
		try {
			return accountDAO.registerUser(UserDataDTO.createRegularUser(username, passkey.getBytes(), name, gender, ""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean updateUserInfo(long uid, String name, boolean gender) {
		return false;
	}

	public UserInfoDTO verifyUser(String username) {
		try {
			return accountDAO.verifyUser(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
