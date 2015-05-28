package com.viewer.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.CategoryDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.SearchQueryDTO;

public class SQLAlbumDAO implements AlbumDAO {

	/** Album Methods **/

	@Override
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId)
			throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.name, Albums.source, MIN(Photos.id) FROM Albums"
				+ " LEFT JOIN Photos ON Albums.id = Photos.albumId"
				+ " WHERE Albums.uid = ? AND Albums.parent = ?"
				+ " GROUP BY Albums.id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setLong(2, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), new File(
					rs.getString(3)), rs.getString(2), rs.getLong(4));
			dto.add(album);
		}
		conn.close();
		return dto;
	}

	@Override
	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid)
			throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT id, name, source, coverid FROM Albums WHERE uid = ? AND id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		AlbumDTO album = null;
		if (rs.next()) {
			album = new AlbumDTO(rs.getLong(1), new File(rs.getString(3)),
					rs.getString(2), rs.getLong(4));
		}
		conn.close();
		return album;
	}

	@Override
	public List<AlbumDTO> fetchSearchUserAlbums(long userid,
			SearchQueryDTO searchQuery) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		List<String> names = searchQuery.getNames();
		List<CategoryDTO> categories = searchQuery.getAllTags();
		
		System.out.println(categories);

		// Create Statement
		String sql = "SELECT Albums.id, Albums.name, Albums.source, MIN(Photos.id) FROM Albums"
				+ " LEFT JOIN Photos ON Albums.id = Photos.albumId"
				+ " LEFT JOIN AlbumTags ON AlbumTags.albumid = Albums.id"
				+ " LEFT JOIN Category ON AlbumTags.cateid = Category.id"
				+ " WHERE Albums.uid = ?";
		for (int i = 0; i < names.size(); i++) {
			sql += " AND (Albums.name LIKE ? OR Albums.subtitle LIKE ?)";
		}
		if (!names.isEmpty()) sql += ")";
		for (CategoryDTO category : categories) {
			sql += " AND ( Category.name = ?";
			for (int i = 0; i < category.getTags().size(); i++) {
				sql += " AND AlbumTags.name = ?";
			}
			sql += ")";
		}
		sql += " GROUP BY Albums.id";
		System.out.println(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set Statement
		stmt.setLong(1, userid);
		int index = 2;
		for (String name : names) {
			stmt.setString(index, '%'+name+'%');
			index++;
		}
		for (CategoryDTO category : categories) {
			stmt.setString(index, category.getCategory());
			index++;
			for (String tag : category.getTags()) {
				stmt.setString(index, tag);
				index++;
			}
		}

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), new File(
					rs.getString(3)), rs.getString(2), rs.getLong(4));
			dto.add(album);
		}
		conn.close();
		return dto;
	}

	@Override
	public boolean createAlbum(long userid, AlbumDTO album) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT INTO Albums VALUES (NULL, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, album.getName());
		stmt.setString(2, album.getSubtitle());
		stmt.setString(3, album.getSource().getAbsolutePath());
		stmt.setLong(4, album.getCoverId());
		stmt.setLong(5, userid);
		stmt.setLong(6, album.getParentId());
		return stmt.execute();
	}

	/** Photo Methods **/

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid)
			throws SQLException {
		List<PhotoDTO> photos = new ArrayList<PhotoDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Photos.id, Photos.name, Photos.source FROM Photos"
				+ " WHERE Photos.uid = ? AND Photos.albumid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			photos.add(new PhotoDTO(rs.getLong(1), rs.getString(2), new File(rs
					.getString(3))));
		}
		conn.close();
		return photos;
	}

	@Override
	public PhotoDTO fetchPhoto(long photoid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT id, name, source FROM Photos WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, photoid);

		// Execute Statement
		PhotoDTO photo = null;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			photo = new PhotoDTO(rs.getLong(1), rs.getString(2), new File(
					rs.getString(3)));
		}
		conn.close();
		return photo;
	}

	@Override
	public PhotoDTO fetchPhotoThumbnail(long photoid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT id, name, thumbnail FROM Photos WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, photoid);

		// Execute Statement
		PhotoDTO photo = null;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			photo = new PhotoDTO(rs.getLong(1), rs.getString(2), new File(
					rs.getString(3)));
		}
		conn.close();
		return photo;
	}

	/** Category & Tag Methods **/

	@Override
	public boolean tagAlbum(long userid, String name, long albumid,
			String category) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT INTO AlbumTags VALUES (NULL, ?, (SELECT Category.id FROM Category WHERE Category.name = ?), ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setString(2, category);
		stmt.setLong(3, albumid);
		return stmt.execute();
	}

	@Override
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid)
			throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT AlbumTags.id, AlbumTags.name, Category.name FROM AlbumTags"
				+ " INNER JOIN Category ON AlbumTags.cateid = Category.id"
				+ " WHERE Category.uid = ? AND AlbumTags.albumid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		AlbumTagsDTO tags = new AlbumTagsDTO(albumid);
		while (rs.next()) {
			tags.insertTag(rs.getLong(1), rs.getString(2), rs.getString(3));
		}
		conn.close();
		return tags;
	}

	@Override
	public boolean createCategory(long userid, String name) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT INTO Category VALUES (NULL, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setLong(2, userid);

		return stmt.execute();
	}

	@Override
	public List<String> fetchAllUserCategories(long userid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT name FROM Category WHERE uid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		List<String> categories = new ArrayList<String>();
		while (rs.next())
			categories.add(rs.getString(1));
		conn.close();
		return categories;
	}
}