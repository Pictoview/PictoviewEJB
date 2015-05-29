package com.viewer.dto;


/**
 * @author ArthurXu
 *
 */
public class PhotoDTO {
	private final long id;
	private String name;
	private String source;

	public PhotoDTO(long id, String source) {
		this.id = id;
		this.source = source;
	}

	public PhotoDTO(long id, String name, String source) {
		this.id = id;
		this.name = name;
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getId() {
		return id;
	}
	
}
