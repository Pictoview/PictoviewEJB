package com.viewer.beans;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

@Remote
public interface AlbumBeanRemote {
	public List<AlbumDTO> fetchAllUserAlbums(long userid);
	
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid, String name, String[] tags);

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid);

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid);

	public PhotoDTO fetchPhoto(long userid, long photoid);

	public boolean createAlbum(long userid, AlbumDTO album);

	public byte[] fetchPhotoData(long userid, long photoid);
}
