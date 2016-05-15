package com.viewer.dto;

import java.io.Serializable;

/**
 * Encapsulates common user information data
 */
public class UserCredentialDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String passkey;
	private long userid;
	private boolean enabled;
	
	private String oldPassword;

	public UserCredentialDTO() {
	}

	public UserCredentialDTO(long userid, String username, String password, boolean enabled) {
		this.userid = userid;
		this.username = username;
		this.passkey = password;
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasskey() {
		return passkey;
	}

	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
