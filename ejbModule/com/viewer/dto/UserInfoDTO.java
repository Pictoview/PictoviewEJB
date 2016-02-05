package com.viewer.dto;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String passkey;
	private String name;
	private long userid;
	private boolean enabled;

	public UserInfoDTO() {
	}

	public UserInfoDTO(long userid, String username, String name, String password, boolean enabled) {
		this.userid = userid;
		this.username = username;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
}
