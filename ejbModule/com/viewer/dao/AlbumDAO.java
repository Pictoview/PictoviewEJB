package com.viewer.dao;

import java.sql.SQLException;
import java.util.List;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.SearchQueryDTO;

public interface AlbumDAO {

	/** Album Methods **/

	// Fetch Methods

	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) throws SQLException;

	public List<AlbumDTO> fetchViewableAlbums(String username, long parentId) throws SQLException;

	public List<AlbumDTO> fetchAllSubscribedAlbums(String username, long parentId) throws SQLException;

	public List<AlbumDTO> fetchSearchUserAlbums(String username, SearchQueryDTO searchQuery) throws SQLException;

	public AlbumDTO fetchUserAlbumInfo(String username, long albumid) throws SQLException;

	// Update Methods

	public long albumExist(String username, String name, long parentId) throws SQLException;

	public long createAlbum(String username, String name, String subtitle, String description, String permission) throws SQLException;

	public long createAlbum(String username, String name, String subtitle, String description, long parentId) throws SQLException;
	
	public void setAlbumCoverPhoto(String username, long albumid, long photoid) throws SQLException;

	public boolean deleteAlbum(String username, long albumId) throws SQLException;
	
	// Permission Operations

	public boolean subscribeToAlbum(String username, long albumId) throws SQLException;

	public boolean unsubscribeToAlbum(String username, long albumId) throws SQLException;

	public void addPermissionToAlbum(String username, long albumId, String user) throws SQLException;

	public void addPermissionToAlbum(String username, long albumId, List<String> users) throws SQLException;

	public void revokePermissionToAlbum(String username, long albumId, List<String> users) throws SQLException;

	// Tags & Categories

	public boolean tagAlbum(String username, String name, long albumid, String category) throws SQLException;

	public int createCategory(String username, String name) throws SQLException;

	public AlbumTagsDTO fetchUserAlbumTags(String username, long albumid) throws SQLException;

	// Photo Methods
	
	public PhotoDTO fetchAlbumCoverPhoto(String username, long albumId) throws SQLException;

	public List<PhotoDTO> fetchUserAlbumPhotos(String username, long albumid) throws SQLException;

	public PhotoDTO fetchPhoto(long photoid) throws SQLException;

	public List<String> fetchAllCategories(String visibility) throws SQLException;

	public PhotoDTO insertPhoto(String username, long albumId, String name, String ext) throws SQLException;

	public boolean clearAlbumTag(long albumid) throws SQLException;
}
