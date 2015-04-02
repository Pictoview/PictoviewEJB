package com.viewer.beans;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

@Remote
public interface AlbumBeanRemote {
	public List<AlbumDTO> fetchAllUserAlbums(long userid) throws SQLException;

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid)
			throws SQLException;

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid);

	public PhotoDTO fetchPhoto(long photoid);

	public boolean createAlbum(long userid, AlbumDTO album);

	public PhotoDTO fetchAlbumCover(long albumid);
}
