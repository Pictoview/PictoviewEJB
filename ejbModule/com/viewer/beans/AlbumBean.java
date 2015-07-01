package com.viewer.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.viewer.file.AlbumFileManager;

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
	public long albumExist(long userid, String name, long parentId) {
		try {
			return albumDAO.albumExist(userid, name, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
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
			String source = AlbumFileManager.StorageLocation
					+ photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid) {
		return fetchPhotoThumbnailData(userid, photoid, 0);
	}

	@Override
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid,
			int flags) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			String source = AlbumFileManager.ThumbnailStorageLocation
					+ photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle,
			long parentId) {
		try {
			return albumDAO.createAlbum(userid, name, subtitle, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid,
			SearchQueryDTO searchQuery) {
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
	public boolean createCategory(long userid, String category) {
		try {
			albumDAO.createCategory(1, category);
			return true;
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

	@Override
	public boolean uploadPhoto(long userid, long albumId, String name,
			InputStream data, int flags) {
		try {
			PhotoDTO photo = albumDAO.insertPhoto(userid, albumId, name);
			AlbumFileManager.createPhotoFile(photo.getSource(), data, flags);
			AlbumFileManager.createPhotoThumbnail(photo.getSource());
			return true;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
