package com.viewer.beans;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import com.viewer.dao.AlbumDAO;
import com.viewer.dao.SQLAlbumDAO;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.SearchQueryDTO;

/**
 * Session Bean implementation class AlbumBean
 */
@Stateless
public class AlbumBean implements AlbumBeanLocal {

	AlbumDAO albumDAO;

	public AlbumBean() {
		albumDAO = new SQLAlbumDAO();
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
	public ImageInputStream fetchPhotoData(long userid, long photoid) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			return ImageIO.createImageInputStream(photoDTO.getSource());
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
			PhotoDTO photoDTO = albumDAO.fetchPhotoThumbnail(photoid);

			// Scale Image
			BufferedImage image = ImageIO.read(new File(photoDTO.getSource()
					.getAbsolutePath()));
			Image scaledImage = image.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			image.getGraphics().drawImage(scaledImage, 0, 0, null);

			BufferedImage retImage = new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
			retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(retImage, "jpg", baos);
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
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid, SearchQueryDTO searchQuery) {
		try {
			return albumDAO.fetchSearchUserAlbums(userid, searchQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid) {
		try {
			return albumDAO.fetchUserAlbumTags(userid, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean tagUserAlbum(long userid, long albumid, String tag,
			String category) {
		try {
			return albumDAO.tagAlbum(userid, tag, albumid, category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> fetchAllUserCategories(long userid) {
		try {
			return albumDAO.fetchAllUserCategories(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
