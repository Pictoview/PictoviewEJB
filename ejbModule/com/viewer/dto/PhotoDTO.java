package com.viewer.dto;

/**
 * @author ArthurXu
 *
 */
public class PhotoDTO {
	private final long id;
	private long albumId;
	private String ext;
	private String name;
	private long ownerid;

	public PhotoDTO(long id, String name, String ext, long albumId, long ownerid) {
		this.id = id;
		this.name = name;
		this.albumId = albumId;
		this.ext = ext;
		this.ownerid = ownerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return ownerid + "/" + albumId + "/" + id + "." + ext;
	}

	public long getId() {
		return id;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public long getAlbumId() {
		return albumId;
	}

	public long getOwnerid() {
		return ownerid;
	}

	public String toString() {
		return id + ": " + name + " @" + getSource();
	}

}
