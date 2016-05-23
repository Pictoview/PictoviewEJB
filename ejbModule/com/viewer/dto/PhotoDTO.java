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
	private String ownername;

	public PhotoDTO(long id, String name, String ext, long albumId, String ownername) {
		this.id = id;
		this.name = name;
		this.albumId = albumId;
		this.ext = ext;
		this.ownername = ownername;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return ownername + "/" + albumId + "/" + id + "." + ext;
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

	public String getOwnername() {
		return ownername;
	}

	public String toString() {
		return id + ": " + name + " @" + getSource();
	}

}
