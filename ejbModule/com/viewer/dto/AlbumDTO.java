package com.viewer.dto;

import java.io.File;

public class AlbumDTO {

	private long id;
	private File source;
	private String name;
	private String subtitle;
	private long coverId;
	private long parentId;

	public AlbumDTO(long id, File source, String name, long coverId) {
		this.id = id;
		this.source = source;
		this.name = name;
		this.coverId = coverId;
	}

	public AlbumDTO(long id, File source, String name, String subtitle,
			long coverId, long parentId) {
		this.id = id;
		this.source = source;
		this.name = name;
		this.subtitle = subtitle;
		this.coverId = coverId;
		this.parentId = parentId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
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
