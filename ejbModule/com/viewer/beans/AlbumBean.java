package com.viewer.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import com.viewer.dao.AlbumDAO;
import com.viewer.dao.AlbumDAOImpl;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;

/**
 * Session Bean implementation class AlbumBean
 */
@Stateless
public class AlbumBean implements AlbumBeanRemote, AlbumBeanLocal {

	AlbumDAO albumDAO;

	public AlbumBean() {
		albumDAO = new AlbumDAOImpl();
	}

	@Override
	public List<AlbumDTO> fetchAllUserAlbums(long userid) {
		try {
			return albumDAO.fetchAllUserAlbums(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid) {
		try {
			return albumDAO.fetchUserAlbumInfo(userid, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid) {
		try {
			return albumDAO.fetchUserAlbumPhotos(userid, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PhotoDTO fetchPhoto(long photoid) {
		try {
			return albumDAO.fetchPhoto(photoid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public byte[] fetchPhotoData(long photoid) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			// Read data
			Path path = Paths.get(photoDTO.getSource().getAbsolutePath());
			byte [] bytes = Files.readAllBytes(path);
			return bytes;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean createAlbum(long userid, AlbumDTO album) {
		try {
			return albumDAO.createAlbum(userid, album);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public PhotoDTO fetchAlbumCover(long albumid) {
		try {
			return albumDAO.fetchAlbumCover(albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
