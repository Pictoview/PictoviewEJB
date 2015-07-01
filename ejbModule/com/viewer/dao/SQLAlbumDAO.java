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
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.name, Albums.subtitle, Albums.source, MIN(Photos.name), Photos.id FROM Albums"
				+ " LEFT JOIN Photos ON Albums.id = Photos.albumId" + " WHERE Albums.uid = ? AND Albums.parent = ?"
				+ " GROUP BY Albums.id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setLong(2, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), new File(rs.getString(4)), rs.getString(2), rs.getString(3),
					rs.getLong(6), parentId);
			dto.add(album);
		}
		conn.close();
		return dto;
	}

	@Override
	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT id, name, source, coverid FROM Albums WHERE uid = ? AND id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		AlbumDTO album = null;
		if (rs.next()) {
			album = new AlbumDTO(rs.getLong(1), new File(rs.getString(3)), rs.getString(2), rs.getLong(4));
		}
		conn.close();
		return album;
	}

	@Override
	public long albumExist(long userid, String name, long parentId) throws SQLException {
		Connection conn = SQLConnector.connect();
		long albumId = -1;

		// Create Statement
		String sql = "SELECT id FROM Albums WHERE uid = ? AND name = ? AND parent = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setString(2, name);
		stmt.setLong(3, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			albumId = rs.getInt(1);
		}
		conn.close();
		return albumId;
	}

	@Override
	public List<AlbumDTO> fetchSearchUserAlbums(long userid, SearchQueryDTO searchQuery) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		List<String> names = searchQuery.getNames();
		List<CategoryDTO> categories = searchQuery.getAllTags();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.name, Albums.source, MIN(Photos.name), Photos.id FROM Albums"
				+ " LEFT JOIN Photos ON Albums.id = Photos.albumId" + " LEFT JOIN AlbumTags ON AlbumTags.albumid = Albums.id"
				+ " LEFT JOIN Category ON AlbumTags.cateid = Category.id" + " WHERE Albums.uid = ?";
		for (int i = 0; i < names.size(); i++) {
			sql += " AND (Albums.name LIKE ? OR Albums.subtitle LIKE ?)";
		}
		for (CategoryDTO category : categories) {
			sql += " AND ( Category.name = ?";
			for (int i = 0; i < category.getTags().size(); i++) {
				sql += " AND AlbumTags.name = ?";
			}
			sql += ")";
		}
		sql += " GROUP BY Albums.id";
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set Statement
		stmt.setLong(1, userid);
		int index = 2;
		for (String name : names) {
			stmt.setString(index, '%' + name + '%');
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
			AlbumDTO album = new AlbumDTO(rs.getLong(1), new File(rs.getString(3)), rs.getString(2), rs.getLong(5));
			dto.add(album);
		}
		conn.close();
		return dto;
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, long parentId) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql;
		if (parentId == 0) sql = "INSERT INTO Albums VALUES (NULL, ?, ?, ?, ?, ?)";
		else sql = "INSERT INTO Albums VALUES (NULL, ?, ?, ?, ?, (SELECT name FROM Albums WHERE id = ?) || ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.setString(2, subtitle);
		stmt.setLong(3, userid);
		stmt.setLong(4, parentId);
		if (parentId == 0) {
			stmt.setString(5, name);
		} else {
			stmt.setLong(5, parentId);
			stmt.setString(6, "/" + name);
		}
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		conn.close();
		return id;
	}

	/** Photo Methods **/

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid) throws SQLException {
		List<PhotoDTO> photos = new ArrayList<PhotoDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Photos.id, Photos.name, Photos.source FROM Photos"
				+ " WHERE Photos.uid = ? AND Photos.albumid = ? ORDER BY Photos.name";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			photos.add(new PhotoDTO(rs.getLong(1), rs.getString(2), rs.getString(3)));
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
			photo = new PhotoDTO(rs.getLong(1), rs.getString(2), rs.getString(3));
		}
		conn.close();
		return photo;
	}

	/** Category & Tag Methods **/

	@Override
	public boolean tagAlbum(long userid, String name, long albumid, String category) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT INTO AlbumTags VALUES (NULL, ?, (SELECT Category.id FROM Category WHERE Category.name = ?), ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setString(2, category);
		stmt.setLong(3, albumid);
		stmt.executeUpdate();
		conn.close();
		return true;
	}

	@Override
	public boolean clearAlbumTag(long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "DELETE AlbumTags WHERE albumid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, albumid);
		stmt.executeUpdate();
		conn.close();
		return true;
	}

	@Override
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT AlbumTags.id, AlbumTags.name, Category.name FROM AlbumTags"
				+ " INNER JOIN Category ON AlbumTags.cateid = Category.id"
				+ " INNER JOIN UserCategory ON UserCategory.cateid = Category.id"
				+ " WHERE UserCategory.uid = ? AND AlbumTags.albumid = ?";
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
	public int createCategory(long userid, String name) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT OR IGNORE INTO Category VALUES (NULL, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		stmt.close();
		sql = "INSERT INTO UserCategory VALUES (NULL, ?, ?)";
		PreparedStatement userCate = conn.prepareStatement(sql);
		userCate.setLong(1, id);
		userCate.setLong(2, userid);
		userCate.executeUpdate();
		conn.close();
		return id;
	}

	@Override
	public List<String> fetchAllUserCategories(long userid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT name FROM Category INNER JOIN UserCategory ON Category.id = UserCategory.cateid WHERE uid = ?";
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

	@Override
	public PhotoDTO insertPhoto(long userid, long albumId, String name) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "INSERT INTO Photos VALUES (NULL, ?, (SELECT source FROM Albums WHERE id = ?) || '/' || (SELECT COALESCE(MAX(id) + 1, 1) FROM Photos) || ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.setLong(2, albumId);
		stmt.setString(3, "-" + name);
		stmt.setLong(4, albumId);
		stmt.setLong(5, userid);

		// Get Id
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		stmt.close();
		// Execute Statement
		String retrieveSql = "SELECT source FROM Photos WHERE id = ?";
		PreparedStatement rstmt = conn.prepareStatement(retrieveSql);
		rstmt.setLong(1, id);
		rs = rstmt.executeQuery();
		PhotoDTO photo = null;
		if (rs.next()) {
			photo = new PhotoDTO(id, name, rs.getString(1));
		}
		conn.close();
		return photo;
	}
}