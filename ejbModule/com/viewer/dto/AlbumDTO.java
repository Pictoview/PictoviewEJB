package com.viewer.dto;

public class AlbumDTO {

	private final long id;
	private long ownerid;
	private String name;
	private String subtitle;
	private long parentId;

	public AlbumDTO(long id, long ownerid, String name) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
	}

	public AlbumDTO(long id, long ownerid, String name, String subtitle, long parentId) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
		this.subtitle = subtitle;
		this.parentId = parentId;
	}

	public String toString() {
		return id + ": " + name + " [" + subtitle + "] " + " Parent: " + parentId;
	}

	public long getId() {
		return id;
	}

	public long getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(long ownerid) {
		this.ownerid = ownerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
}
