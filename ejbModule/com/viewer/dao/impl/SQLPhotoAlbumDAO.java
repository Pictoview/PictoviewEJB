package com.viewer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dao.AlbumDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.MediaDTO;
import com.viewer.dto.SearchQueryDTO;

public class SQLPhotoAlbumDAO extends SQLAlbumDAO implements AlbumDAO {

	@Override
	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) throws SQLException {
		return super.fetchAllPublicAlbums(limit, offset, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchUserAlbums(long userid, long parentId, int ordering, int limit, int offset) throws SQLException {
		return super.fetchUserAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchViewableAlbums(long userid, long parentId, int ordering, int limit, int offset) throws SQLException {
		return super.fetchViewableAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchAllSubscribedAlbums(long userid, long parentId, int ordering, int limit, int offset) throws SQLException {
		return super.fetchAllSubscribedAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserViewableAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserViewableAlbums(userid, searchQuery, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserSubscribedAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserSubscribedAlbums(userid, searchQuery, MEDIA_TYPE_PHOTO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserAlbums(userid, searchQuery, MEDIA_TYPE_PHOTO);
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, String permission)
			throws SQLException {
		return super.createAlbum(userid, name, subtitle, description, permission, MEDIA_TYPE_PHOTO);
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, long parentId) throws SQLException {
		return super.createAlbum(userid, name, subtitle, description, parentId, MEDIA_TYPE_PHOTO);
	}

	/** Photo Methods **/
	@Override
	public List<MediaDTO> fetchUserAlbumPhotos(long userid, long albumid) throws SQLException {
		List<MediaDTO> photos = new ArrayList<MediaDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String selectPhotos = "SELECT Photos.id, Photos.name, Albums.id FROM Photos"
				+ " LEFT JOIN Albums ON Photos.albumid = Albums.id"
				+ " LEFT JOIN AlbumAccess ON AlbumAccess.albumid = Albums.id AND AlbumAccess.owner = Albums.owner"
				+ " WHERE (AlbumAccess.visitor = ? OR Albums.permission = 'PUBLIC') AND Photos.albumid = ? ORDER BY Photos.name";
		PreparedStatement stmt = conn.prepareStatement(selectPhotos);
		stmt.setLong(1, userid);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			photos.add(new MediaDTO(rs.getLong(1), rs.getString(2), rs.getString(3), albumid, userid, MEDIA_TYPE_PHOTO));
		}
		// conn.close();
		return photos;
	}

	@Override
	public MediaDTO fetchPhoto(long userid, long photoid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String selectPhotos = "SELECT Photos.id, Photos.name, Photos.ext, Photos.albumid FROM Photos"
				+ " WHERE Photos.owner = ? AND Photos.id = ?";
		PreparedStatement stmt = conn.prepareStatement(selectPhotos);
		stmt.setLong(1, userid);
		stmt.setLong(2, photoid);

		// Execute Statement
		MediaDTO photo = null;
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			photo = new MediaDTO(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4), userid, MEDIA_TYPE_PHOTO);
		}
		// conn.close();
		return photo;
	}
}
