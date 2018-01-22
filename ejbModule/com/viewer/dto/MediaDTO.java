package com.viewer.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.viewer.file.AlbumFileManager;

/**
 * @author ArthurXu
 *
 */
public class MediaDTO {
	private final long id;
	private long albumId;
	private String ext;
	private String name;
	private long ownerid;
	private int mediaType;

	public MediaDTO(long id, String name, String ext, long albumId, long ownerid, int mediaType) {
		this.id = id;
		this.name = name;
		this.albumId = albumId;
		this.ext = ext;
		this.ownerid = ownerid;
		this.mediaType = mediaType;
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

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public InputStream getThumbnailDataStream() throws FileNotFoundException {
		return new FileInputStream(new File(AlbumFileManager.ThumbnailStorageLocation + getSource()));
	}

	public InputStream getDataStream() throws FileNotFoundException {
		return new FileInputStream(new File(AlbumFileManager.StorageLocation + getSource()));
	}

	public String toString() {
		return id + ": " + name + " @" + getSource();
	}
}
