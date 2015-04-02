package com.viewer.dao;

import java.sql.SQLException;
import java.util.List;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

public interface AlbumDAO {

	public List<AlbumDTO> fetchAllUserAlbums(long userid) throws SQLException;

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid)
			throws SQLException;

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid)
			throws SQLException;

	public PhotoDTO fetchPhoto(long photoid) throws SQLException;

	public boolean createAlbum(long userid, AlbumDTO album) throws SQLException;

	public PhotoDTO fetchAlbumCover(long albumid) throws SQLException;
}
