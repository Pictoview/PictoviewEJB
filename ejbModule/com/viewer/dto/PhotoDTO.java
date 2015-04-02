package com.viewer.dto;

import java.io.File;

public class PhotoDTO {
	private long id;
	private String name;
	private File source;

	public PhotoDTO(long id, File source) {
		this.id = id;
		this.source = source;
	}

	public PhotoDTO(long id, String name, File source) {
		this.id = id;
		this.name = name;
		this.source = source;
	}

	public PhotoDTO(long id, String source) {
		this.id = id;
		this.source = new File(source);
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
}
