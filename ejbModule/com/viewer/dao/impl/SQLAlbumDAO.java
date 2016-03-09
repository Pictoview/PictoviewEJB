package com.viewer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dao.AlbumDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.CategoryDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.SearchQueryDTO;

public class SQLAlbumDAO implements AlbumDAO {

	/** Album Fetch Methods **/

	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle FROM Albums"
				+ " WHERE Albums.permission = 'PUBLIC' AND Albums.parent = 0 GROUP BY Albums.id LIMIT " + limit + " OFFSET "
				+ offset;
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), 0);
			dto.add(album);
		}
		stmt.close();

		// conn.close();
		return dto;
	}

	@Override
	public List<AlbumDTO> fetchViewableAlbums(String username, long parentId) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String selectViewable = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle FROM AlbumAccess"
				+ " LEFT JOIN Users ON Users.uid = AlbumAccess.visitor" + " LEFT JOIN Albums ON Albums.id = AlbumAccess.albumid"
				+ " WHERE (Users.username = ? OR Albums.permission = 'PUBLIC') AND Albums.parent = ?";
		PreparedStatement stmt = conn.prepareStatement(selectViewable);
		stmt.setString(1, username);
		stmt.setLong(2, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), 0);
			dto.add(album);
		}
		// conn.close();
		return dto;
	}

	@Override
	public List<AlbumDTO> fetchAllSubscribedAlbums(String username, long parentId) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String selectSubscribed = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle FROM AlbumAccess"
				+ " LEFT JOIN Users ON Users.uid = AlbumAccess.visitor"
				+ " LEFT JOIN UserSubscriptions ON Users.uid = UserSubscriptions.uid AND AlbumAccess.albumid = UserSubscriptions.albumid"
				+ " LEFT JOIN Albums ON Albums.id = AlbumAccess.albumid"
				+ " WHERE (Users.username = ? OR Albums.permission = 'PUBLIC') AND Albums.parent = ?";
		PreparedStatement stmt = conn.prepareStatement(selectSubscribed);
		stmt.setString(1, username);
		stmt.setLong(2, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			AlbumDTO album = new AlbumDTO(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), parentId);
			dto.add(album);
		}
		// conn.close();
		return dto;
	}

	@Override
	public List<AlbumDTO> fetchSearchUserAlbums(String username, SearchQueryDTO searchQuery) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = SQLConnector.connect();

		List<String> names = searchQuery.getNames();
		List<CategoryDTO> categories = searchQuery.getAllTags();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle FROM AlbumAccess"
				+ " LEFT JOIN Users ON Users.uid = AlbumAccess.visitor" + " LEFT JOIN Albums ON Albums.id = AlbumAccess.albumid"
				+ " WHERE (Users.username = ? OR Albums.permission = 'PUBLIC') AND Albums.parent = ?";
		for (int i = 0; i < names.size(); i++) {
			sql += " AND Albums.name LIKE ?";
		}
		for (CategoryDTO category : categories) {
			sql += " AND (TagCategory.name = ?";
			for (int i = 0; i < category.getTags().size(); i++) {
				sql += " AND AlbumTags.name = ?";
			}
			sql += ")";
		}
		PreparedStatement stmt = conn.prepareStatement(sql);

		// Set Statement
		stmt.setString(1, username);
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
			AlbumDTO album = new AlbumDTO(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getLong(7));
			dto.add(album);
		}
		// conn.close();
		return dto;
	}

	@Override
	public AlbumDTO fetchUserAlbumInfo(String username, long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle, Albums.parent FROM Albums"
				+ " LEFT JOIN Users ON Users.uid = Albums.owner" + " WHERE Users.username = ? AND Albums.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		AlbumDTO album = null;
		if (rs.next()) {
			album = new AlbumDTO(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getLong(5));
		}
		// conn.close();
		return album;
	}

	/** Album Access Methods **/

	@Override
	public boolean subscribeToAlbum(String username, long albumId) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "INSERT OR IGNORE INTO UserSubscriptions VALUES (NULL, (SELECT Users.uid FROM Users WHERE Users.username = ?), ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setLong(2, albumId);
		stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

		// Execute Statement
		int result = stmt.executeUpdate();
		stmt.close();
		// conn.close();
		return result > 0;
	}

	@Override
	public boolean unsubscribeToAlbum(String username, long albumId) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "DELETE FROM UserSubscriptions WHERE UserSubscriptions.albumid IN ("
				+ "SELECT UserSubscriptions.albumid FROM UserSubscriptions"
				+ " LEFT JOIN Users ON Users.uid = UserSubscriptions.uid WHERE Users.username = ? AND UserSubscriptions.albumid = ?"
				+ ")";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setLong(2, albumId);

		// Execute Statement
		int result = stmt.executeUpdate();
		stmt.close();
		// conn.close();
		return result > 0;
	}

	@Override
	public void addPermissionToAlbum(String username, long albumId, String user) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "INSERT INTO AlbumAccess VALUES (NULL, ?, (SELECT Users.uid FROM Users WHERE Users.username = ?), (SELECT Users.uid FROM Users WHERE Users.username = ?))";
		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setLong(1, albumId);
		stmt.setString(2, username);
		stmt.setString(3, user);
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	public void addPermissionToAlbum(String username, long albumId, List<String> users) throws SQLException {
		if (users.isEmpty()) return;
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "INSERT INTO AlbumAccess VALUES (NULL, ?, (SELECT Users.uid FROM Users WHERE Users.username = ?), (SELECT Users.uid FROM Users WHERE Users.username = ?))";
		PreparedStatement stmt = conn.prepareStatement(sql);

		for (String user : users) {
			stmt.setLong(1, albumId);
			stmt.setString(2, username);
			stmt.setString(3, user);
			stmt.addBatch();
		}
		stmt.executeBatch();
		stmt.close();
	}

	@Override
	public void revokePermissionToAlbum(String username, long albumId, List<String> users) throws SQLException {
		if (users.isEmpty()) return;
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "DELETE FROM AlbumAccess WHERE albumid = ? AND owner IN (SELECT Users.uid FROM Users WHERE Users.username = ?) AND visitor IN (SELECT Users.uid FROM Users WHERE Users.username = ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);

		for (String user : users) {
			stmt.setLong(1, albumId);
			stmt.setString(2, username);
			stmt.setString(3, user);
			stmt.addBatch();
		}
		stmt.executeBatch();
		stmt.close();
	}

	@Override
	public long albumExist(String username, String name, long parentId) throws SQLException {
		Connection conn = SQLConnector.connect();
		long albumId = -1;

		// Create Statement
		String sql = "SELECT id FROM Albums LEFT JOIN Users ON Users.uid = Albums.uid WHERE Users.username = ? AND Albums.name = ? AND Albums.parent = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setString(2, name);
		stmt.setLong(3, parentId);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			albumId = rs.getInt(1);
		}
		// conn.close();
		return albumId;
	}

	/** Create Albums **/

	@Override
	public long createAlbum(String username, String name, String subtitle, String permission) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "INSERT INTO Albums VALUES (NULL, ?, ?, (SELECT Users.uid FROM Users WHERE Users.username = ?), ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.setString(2, subtitle);
		stmt.setString(3, username);
		stmt.setLong(4, 0);
		stmt.setString(5, permission);
		stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		stmt.close();
		addPermissionToAlbum(username, id, username);
		// conn.close();
		return id;
	}

	@Override
	public long createAlbum(String username, String name, String subtitle, long parentId) throws SQLException {
		Connection conn = SQLConnector.connect();

		String sql = "INSERT INTO Albums VALUES (NULL, ?, ?, (SELECT Users.uid FROM Users WHERE Users.username = ?), ?, (SELECT Albums.permission FROM Albums WHERE id = ?), ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.setString(2, subtitle);
		stmt.setString(3, username);
		stmt.setLong(4, parentId);
		stmt.setLong(5, parentId);
		stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		stmt.close();
		addPermissionToAlbum(username, id, username);
		// conn.close();
		return id;
	}

	@Override
	public boolean deleteAlbum(String username, long albumId) throws SQLException {
		Connection conn = SQLConnector.connect();

		String deletePhotos = "DELETE FROM Photos WHERE Photos.AlbumId = ? AND EXISTS (SELECT Users.uid FROM Users WHERE username = ?)";

		PreparedStatement stmt = conn.prepareStatement(deletePhotos);
		stmt.setLong(1, albumId);
		stmt.setString(2, username);
		int row = stmt.executeUpdate();
		stmt.close();

		String deleteAlbum = "DELETE FROM Albums WHERE Albums.id = ? AND EXISTS (SELECT Users.uid FROM Users WHERE username = ?)";
		PreparedStatement stmt2 = conn.prepareStatement(deleteAlbum);
		stmt2.setLong(1, albumId);
		stmt2.setString(2, username);
		row = stmt2.executeUpdate();
		stmt2.close();

		if (row > 0) {
			String deleteAlbumAccess = "DELETE FROM AlbumAccess WHERE AlbumAccess.albumid = ?";
			PreparedStatement stmt3 = conn.prepareStatement(deleteAlbumAccess);
			stmt3.setLong(1, albumId);
			stmt3.executeUpdate();
			stmt3.close();
		}

		// conn.close();
		return true;
	}

	/** Photo Methods **/

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(String username, long albumid) throws SQLException {
		List<PhotoDTO> photos = new ArrayList<PhotoDTO>();
		Connection conn = SQLConnector.connect();

		// Create Statement
		String selectViewable = "SELECT Albums.id, Albums.owner, Albums.name, Albums.subtitle FROM AlbumAccess"
				+ " LEFT JOIN Users ON Users.uid = AlbumAccess.visitor" + " LEFT JOIN Albums ON Albums.id = AlbumAccess.albumid"
				+ " WHERE (Users.username = ? OR Albums.permission = 'PUBLIC') AND Albums.parent = ?";
		String selectPhotos = "SELECT Photos.id, Photos.name, Albums.id FROM Photos"
				+ " LEFT JOIN Albums ON Photos.albumid = Albums.id"
				+ " LEFT JOIN AlbumAccess ON AlbumAccess.albumid = Albums.id AND AlbumAccess.visitor = Users.uid"
				+ " LEFT JOIN Users ON Users.uid = AlbumAccess.visitor"
				+ " WHERE (Users.username = ? OR Albums.permission = 'PUBLIC') AND Photos.albumid = ? ORDER BY Photos.name";
		PreparedStatement stmt = conn.prepareStatement(selectPhotos);
		stmt.setString(1, username);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			photos.add(new PhotoDTO(rs.getLong(1), rs.getString(2), username + "/" + rs.getLong(3) + "/" + rs.getString(2)));
		}
		// conn.close();
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
		// conn.close();
		return photo;
	}

	/** Category & Tag Methods **/

	@Override
	public boolean tagAlbum(String username, String name, long albumid, String category) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT INTO AlbumTags VALUES (NULL, ?, (SELECT TagCategory.id FROM TagCategory WHERE TagCategory.name = ?), ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setString(2, category);
		stmt.setLong(3, albumid);
		stmt.executeUpdate();
		// conn.close();
		return true;
	}

	@Override
	public boolean clearAlbumTag(long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "DELETE FROM AlbumTags WHERE albumid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, albumid);
		stmt.executeUpdate();
		// conn.close();
		return true;
	}

	@Override
	public AlbumTagsDTO fetchUserAlbumTags(String username, long albumid) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT AlbumTags.id, AlbumTags.name, TagCategory.name FROM AlbumTags"
				+ " INNER JOIN TagCategory ON AlbumTags.cateid = Category.id"
				+ " LEFT JOIN Albums ON Albums.id = AlbumTags.albumid" + " LEFT JOIN Users ON Users.uid = Albums.owner"
				+ " WHERE Users.username = ? AND AlbumTags.albumid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		stmt.setLong(2, albumid);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		AlbumTagsDTO tags = new AlbumTagsDTO(albumid);
		while (rs.next()) {
			tags.insertTag(rs.getLong(1), rs.getString(2), rs.getString(3));
		}
		stmt.close();
		// conn.close();
		return tags;
	}

	@Override
	public int createCategory(String username, String name) throws SQLException {
		Connection conn = SQLConnector.connect();
		String sql = "INSERT OR IGNORE INTO TagCategory VALUES (NULL, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);
		}
		stmt.close();
		// conn.close();
		return id;
	}

	@Override
	public List<String> fetchAllCategories(String username) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "SELECT name FROM TagCategory WHERE visibility = ? OR visibility = 'FULL'";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);

		// Execute Statement
		ResultSet rs = stmt.executeQuery();
		List<String> categories = new ArrayList<String>();
		while (rs.next())
			categories.add(rs.getString(1));
		stmt.close();
		// conn.close();
		return categories;
	}

	@Override
	public PhotoDTO insertPhoto(String username, long albumId, String name) throws SQLException {
		Connection conn = SQLConnector.connect();

		// Create Statement
		String sql = "INSERT INTO Photos VALUES (NULL, ?, ?,?, (SELECT Users.uid FROM Users WHERE Users.username = ?), ?)";
		PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, name);
		stmt.setString(2, "");
		stmt.setLong(3, albumId);
		stmt.setString(4, username);
		stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

		// Get Id
		PhotoDTO photo = null;
		int id = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			photo = new PhotoDTO(rs.getLong(1), name, username + "/" + albumId + "/" + id);
		}
		stmt.close();
		// conn.close();
		return photo;
	}
}