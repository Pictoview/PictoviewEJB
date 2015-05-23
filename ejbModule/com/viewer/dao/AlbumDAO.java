package com.viewer.dao;

import java.sql.SQLException;
import java.util.List;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.PhotoDTO;

public interface AlbumDAO {

	// Album Methods

	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId)
			throws SQLException;

	public List<AlbumDTO> fetchSearchUserAlbums(long userid, String name,
			String[] tags) throws SQLException;

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid)
			throws SQLException;

	public boolean createAlbum(long userid, AlbumDTO album) throws SQLException;

	// Tags & Categories

	public boolean tagAlbum(long userid, String name, long albumid, String category)
			throws SQLException;

	public boolean createCategory(long userid, String name) throws SQLException;

	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid)
			throws SQLException;

	// Photo Methods

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid)
			throws SQLException;

	public PhotoDTO fetchPhoto(long photoid) throws SQLException;
	
	public PhotoDTO fetchPhotoThumbnail(long photoid) throws SQLException;

	public List<String> fetchAllUserCategories(long userid) throws SQLException;
}
