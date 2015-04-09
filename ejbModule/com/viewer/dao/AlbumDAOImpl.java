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

	public List<AlbumDTO> fetchAllUserAlbums(long userid) throws SQLException {
		List<AlbumDTO> dto = new ArrayList<AlbumDTO>();
		Connection conn = Connector.connect();

		// Create Statement
		String sql = "SELECT id, name, source, coverid FROM Albums WHERE uid = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, userid);

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

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid)
			throws SQLException {
		List<PhotoDTO> photos = new ArrayList<PhotoDTO>();
		Connection conn = Connector.connect();

		// Create Statement
		String sql = "SELECT Photos.id, Photos.name, Photos.source, Photos.ordering FROM Photos"
				+ " INNER JOIN Albums ON Albums.id = Photos.albumid WHERE uid = ? AND albumid = ?";
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
		String sql = "SELECT id, name, source, ordering FROM Photos WHERE id = ?";
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
	public PhotoDTO fetchAlbumCover(long albumid) throws SQLException {
		Connection conn = Connector.connect();

		// Create Statement
		String sql = "SELECT Photos.id, Photos.name, Photos.source FROM Photos "
				+ " INNER JOIN Albums ON Photos.albumId = Albums.id "
				+ " WHERE Albums.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, albumid);

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
}