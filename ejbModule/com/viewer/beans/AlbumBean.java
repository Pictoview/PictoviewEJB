package com.viewer.beans;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;

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
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId) {
		try {
			return albumDAO.fetchAllUserAlbums(userid, parentId);
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
	public PhotoDTO fetchPhoto(long userid, long photoid) {
		try {
			return albumDAO.fetchPhoto(photoid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] fetchPhotoData(long userid, long photoid) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			// Read data
			Path path = Paths.get(photoDTO.getSource().getAbsolutePath());
			byte[] bytes = Files.readAllBytes(path);
			return bytes;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public byte[] fetchPhotoThumbnailData(long userid, long photoid) {
		return fetchPhotoThumbnailData(userid, photoid, 0);
	}
	
	@Override
	public byte[] fetchPhotoThumbnailData(long userid, long photoid, int flags) {
		final int width = 30, height = 30;
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			
			// Scale Image
			BufferedImage image = ImageIO.read(new File(photoDTO.getSource().getAbsolutePath()));
			Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			image.getGraphics().drawImage(scaledImage, 0, 0 , null);
			

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();
			baos.close();
			return imageBytes;
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
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid, String name,
			String[] tags) {
		try {
			return albumDAO.fetchSearchUserAlbums(userid, name, tags);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
