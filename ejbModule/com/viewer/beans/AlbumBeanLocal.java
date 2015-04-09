package com.viewer.beans;

import java.util.List;

import javax.ejb.Local;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

@Local
public interface AlbumBeanLocal {
	public List<AlbumDTO> fetchAllUserAlbums(long userid);

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid);

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid);

	public PhotoDTO fetchPhoto(long photoid);

	public boolean createAlbum(long userid, AlbumDTO album);

	public PhotoDTO fetchAlbumCover(long albumid);

	public byte[] fetchPhotoData(long photoid);
}