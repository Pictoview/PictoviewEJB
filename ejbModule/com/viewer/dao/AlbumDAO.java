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

	public List<AlbumDTO> fetchUserAlbums(long userid, long parentId) throws SQLException;

	public List<AlbumDTO> fetchViewableAlbums(long userid, long parentId) throws SQLException;

	public List<AlbumDTO> fetchAllSubscribedAlbums(long userid, long parentId) throws SQLException;

	public List<AlbumDTO> fetchSearchUserViewableAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException;

	public List<AlbumDTO> fetchSearchUserSubscribedAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException;

	public List<AlbumDTO> fetchSearchUserAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException;

	// Update Methods

	public long albumExist(long userid, String name, long parentId) throws SQLException;

	public long createAlbum(long userid, String name, String subtitle, String description, String permission)
			throws SQLException;

	public long createAlbum(long userid, String name, String subtitle, String description, long parentId) throws SQLException;

	public void setAlbumCoverPhoto(long userid, long albumid, long photoid) throws SQLException;

	public boolean deleteAlbum(long userid, long albumId) throws SQLException;

	// Permission Operations

	public boolean subscribeToAlbum(long userid, long albumId) throws SQLException;

	public boolean unsubscribeToAlbum(long userid, long albumId) throws SQLException;

	public void addPermissionToAlbum(long ownerid, long albumId, String user) throws SQLException;

	public void addPermissionToAlbum(long ownerid, long albumId, List<String> users) throws SQLException;

	public void revokePermissionToAlbum(long ownerid, long albumId, List<String> users) throws SQLException;

	// Ratings

	public int fetchAlbumAverageRating(long userid, long albumId) throws SQLException;

	public boolean voteAlbumRating(long userid, long albumId, int rating) throws SQLException;

	public int fetchAlbumUserRating(long userid, long albumId) throws SQLException;

	// Tags & Categories

	public boolean tagAlbum(long userid, String name, long albumid, String category) throws SQLException;

	public boolean tagAlbum(long userid, List<String> name, long albumid, String category) throws SQLException;

	public int createCategory(long userid, String name) throws SQLException;

	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid) throws SQLException;

	public boolean tagRelevanceAlbum(long tagId) throws SQLException;

	// Photo Methods

	public PhotoDTO fetchAlbumCoverPhoto(long userid, long albumId) throws SQLException;

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid) throws SQLException;

	public PhotoDTO fetchPhoto(long userid, long photoid) throws SQLException;

	public List<String> fetchAllCategories(String visibility) throws SQLException;

	public PhotoDTO insertPhoto(long userid, long albumId, String name, String ext) throws SQLException;

	public boolean clearAlbumTag(long albumid) throws SQLException;

}
