package com.viewer.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

public class AlbumDAOImpl implements AlbumDAO {

	/** Album Methods **/

	@Override
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = Connector.connect();

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
		Connection conn = Connector.connect();

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
	public List<AlbumDTO> fetchSearchUserAlbums(long userid, String name,
			String[] tags) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = Connector.connect();

		// Create Statement
		String sql = "SELECT Albums.id, Albums.name, Albums.source, Albums.coverid FROM Albums"
				+ " LEFT JOIN AlbumTags ON AlbumTags.albumid = Albums.id"
				+ " WHERE Albums.uid = ?"
				+ " AND (Albums.subtitle LIKE ? OR Albums.name LIKE ?";
		for (int i = 0; i < tags.length; i++) {
			sql += " OR AlbumTags.name = ?";
		}
		sql += ")";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);
		stmt.setString(2, "%" + name + "%");
		stmt.setString(3, "%" + name + "%");
		int index = 4;
		for (String tag : tags) {
			stmt.setString(index, tag);
			index++;
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
		Connection conn = Connector.connect();
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
		Connection conn = Connector.connect();

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
		Connection conn = Connector.connect();

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

	/** Category & Tag Methods **/

	@Override
	public boolean tagAlbum(long userid, String name, long albumid)
			throws SQLException {
		Connection conn = Connector.connect();
		String sql = "INSERT INTO AlbumTags VALUES (NULL, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setLong(2, albumid);
		return stmt.execute();
	}

	@Override
	public boolean createCategory(long userid, String name) throws SQLException {
		Connection conn = Connector.connect();
		String sql = "INSERT INTO Category VALUES (NULL, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		stmt.setLong(2, userid);

		return stmt.execute();
	}

	@Override
	public boolean createCategoryElement(long userid, String name, long catid)
			throws SQLException {
		Connection conn = Connector.connect();
		String sql = "INSERT INTO CategoryElement VALUES (NULL, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, catid);
		stmt.setString(2, name);
		stmt.setLong(3, userid);

		return stmt.execute();
	}

	@Override
	public boolean linkCategory(long userid, long cateid, long albumid)
			throws SQLException {
		Connection conn = Connector.connect();
		String sql = "INSERT INTO CategoryElement VALUES (NULL, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, cateid);
		stmt.setLong(2, albumid);

		return stmt.execute();
	}
}