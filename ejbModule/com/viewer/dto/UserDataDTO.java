package com.viewer.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long uid;
	private String username;
	private byte[] passkey;
	private int role; // (0, admin) (1, staff) (2, p_user) (3, user) (5, guest)
	private String status;
	private int points;
	private Timestamp lastAccessed;

	private String name;
	private boolean gender; // true = male
	private String address;
	private String description;

	public static UserDataDTO createRegularUser(String username, byte[] passkey, String name, boolean gender,
			String description) {
		return new UserDataDTO(username, passkey, 3, "NORMAL", 0, new Timestamp(System.currentTimeMillis()), name, gender, "",
				description);
	}

	public static UserDataDTO createAdmin(String username, byte[] passkey, String name, boolean gender) {
		return new UserDataDTO(username, passkey, 3, "FULL", 0, new Timestamp(System.currentTimeMillis()), name, gender, "",
				"Administrator");
	}

	private UserDataDTO(String username, byte[] passkey, int role, String status, int points, Timestamp lastAccessed,
			String name, boolean gender, String address, String description) {
		this.username = username;
		this.passkey = passkey;
		this.role = role;
		this.name = name;
		this.gender = gender;
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

	public byte[] getPasskey() {
		return passkey;
	}

	public void setPasskey(byte[] passkey) {
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
}
