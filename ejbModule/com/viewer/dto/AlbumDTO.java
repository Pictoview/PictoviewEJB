package com.viewer.dto;

public class AlbumDTO {

	private final long id;
	private long ownerid;
	private String name;
	private String subtitle;
	private long parentId;
	private String description;
	private double rating;
	
	private boolean isSubscribed;

	public AlbumDTO(long id, long ownerid, String name) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
	}
	
	public AlbumDTO(long id, long ownerid, String name, String subtitle, long parentId, String description, boolean isSubscribed) {
		this.id = id;
		this.ownerid = ownerid;
		this.name = name;
		this.subtitle = subtitle;
		this.parentId = parentId;
		this.description = description;
		this.isSubscribed = isSubscribed;
	}

	public String toString() {
		return id + ": " + name + " [" + subtitle + "] " + " Parent: " + parentId + " isSubbed: " + isSubscribed + " Rating: " + rating;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
