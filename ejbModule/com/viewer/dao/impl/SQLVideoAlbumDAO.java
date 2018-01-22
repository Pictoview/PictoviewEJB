package com.viewer.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.viewer.dao.AlbumDAO;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.MediaDTO;
import com.viewer.dto.SearchQueryDTO;

public class SQLVideoAlbumDAO extends SQLAlbumDAO implements AlbumDAO {
	
	private static final int MEDIA_TYPE_VIDEO = 2;

	@Override
	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) throws SQLException {
		return super.fetchAllPublicAlbums(limit, offset, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchUserAlbums(long userid, long parentId, int ordering, int limit, int offset) throws SQLException {
		return super.fetchUserAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchViewableAlbums(long userid, long parentId, int ordering, int limit, int offset)
			throws SQLException {
		return super.fetchViewableAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchAllSubscribedAlbums(long userid, long parentId, int ordering, int limit, int offset)
			throws SQLException {
		return super.fetchAllSubscribedAlbums(userid, parentId, ordering, limit, offset, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserViewableAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserViewableAlbums(userid, searchQuery, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserSubscribedAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserSubscribedAlbums(userid, searchQuery, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<AlbumDTO> fetchSearchUserAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		return super.fetchSearchUserAlbums(userid, searchQuery, MEDIA_TYPE_VIDEO);
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, String permission)
			throws SQLException {
		return super.createAlbum(userid, name, subtitle, description, permission, MEDIA_TYPE_VIDEO);
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, long parentId) throws SQLException {
		return super.createAlbum(userid, name, subtitle, description, parentId, MEDIA_TYPE_VIDEO);
	}

	@Override
	public List<MediaDTO> fetchUserAlbumPhotos(long userid, long albumid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaDTO fetchPhoto(long userid, long photoid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
