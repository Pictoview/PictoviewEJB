package com.viewer.dto;

public class AlbumDTO {

	private final long id;
	private long ownerid;
	private String name;
	private String subtitle;
	private long coverId;
	private long parentId;

	public AlbumDTO(long id, long ownerid, String name, long coverId) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
		this.coverId = coverId;
	}

	public AlbumDTO(long id, long ownerid, String name, String subtitle, long coverId, long parentId) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
		this.subtitle = subtitle;
		this.coverId = coverId;
		this.parentId = parentId;
	}

	public String toString() {
		return id + ": " + name + " [" + subtitle + "] " + "Cover: " + coverId + " Parent: " + parentId;
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

	public long getCoverId() {
		return coverId;
	}

	public void setCoverId(long coverId) {
		this.coverId = coverId;
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
