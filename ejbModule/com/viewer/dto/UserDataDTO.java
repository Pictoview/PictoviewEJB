package com.viewer.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long uid;
	private String username;
	private String passkey;
	private int role; // (0, admin) (1, staff) (2, p_user) (3, user) (5, guest)
	private String status;
	private int points;
	private Timestamp lastAccessed;

	private String name;
	private boolean gender; // true = male
	private String address;
	private String email;
	private String description;

	public static int ROLE_ADMIN = 0, ROLE_STAFF = 1, ROLE_PREMIUM = 2, ROLE_USER = 3, ROLE_GUEST = 5;

	public static UserDataDTO createRegularUser(String username, String passkey, String name, boolean gender, String email,
			String description) {
		return new UserDataDTO(username, passkey, ROLE_USER, "NORMAL", 0, new Timestamp(System.currentTimeMillis()), name,
				gender, email, "", description);
	}

	public static UserDataDTO createAdmin(String username, String passkey, String name, boolean gender, String email) {
		return new UserDataDTO(username, passkey, ROLE_USER, "FULL", 0, new Timestamp(System.currentTimeMillis()), name, gender,
				email, "", "Administrator");
	}
	
	public void markAsRegularUser() {
		this.role = ROLE_USER;
		this.status = "NORMAL";
		this.points = 0;
		this.lastAccessed = new Timestamp(System.currentTimeMillis());
	}
	
	public UserDataDTO() {}

	private UserDataDTO(String username, String passkey, int role, String status, int points, Timestamp lastAccessed,
			String name, boolean gender, String email, String address, String description) {
		this.username = username;
		this.passkey = passkey;
		this.role = role;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.address = address;
		this.description = description;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Timestamp getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Timestamp lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
