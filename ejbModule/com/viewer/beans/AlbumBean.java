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
import com.viewer.dao.impl.SQLAlbumDAO;
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

	private AlbumDAO albumDAO;

	public AlbumBean() {
		albumDAO = new SQLAlbumDAO();
	}

	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) {
		try {
			return albumDAO.fetchAllPublicAlbums(limit, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AlbumDTO> fetchAllUserAlbums(String username, long parentId) {
		try {
			return albumDAO.fetchAllSubscribedAlbums(username, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AlbumDTO fetchUserAlbumInfo(String username, long albumid) {
		try {
			return albumDAO.fetchUserAlbumInfo(username, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long albumExist(String username, String name, long parentId) {
		try {
			return albumDAO.albumExist(username, name, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<PhotoDTO> fetchUserAlbumPhotos(String username, long albumid) {
		try {
			return albumDAO.fetchUserAlbumPhotos(username, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PhotoDTO fetchPhoto(String username, long photoid) {
		try {
			return albumDAO.fetchPhoto(photoid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ImageInputStream fetchPhotoData(String username, long photoid) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			String source = AlbumFileManager.StorageLocation + photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ImageInputStream fetchPhotoThumbnailData(String username, long photoid) {
		return fetchPhotoThumbnailData(username, photoid, 0);
	}

	@Override
	public ImageInputStream fetchPhotoThumbnailData(String username, long photoid, int flags) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(photoid);
			String source = AlbumFileManager.ThumbnailStorageLocation + photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public long createAlbum(String username, String name, String subtitle, String permission) {
		try {
			return albumDAO.createAlbum(username, name, subtitle, permission);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public long createAlbum(String username, String name, String subtitle, long parentId) {
		try {
			return albumDAO.createAlbum(username, name, subtitle, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<AlbumDTO> fetchSearchedUserAlbums(String username, SearchQueryDTO searchQuery) {
		try {
			return albumDAO.fetchSearchUserAlbums(username, searchQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AlbumTagsDTO fetchUserAlbumTags(String username, long albumid) {
		try {
			return albumDAO.fetchUserAlbumTags(username, albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean tagUserAlbum(String username, long albumid, String tag, String category) {
		try {
			return albumDAO.tagAlbum(username, tag, albumid, category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean clearAlbumTag(String username, long albumid) {
		try {
			return albumDAO.clearAlbumTag(albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createCategory(String username, String category) {
		try {
			albumDAO.createCategory(username, category);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> fetchAllUserCategories(String username) {
		try {
			return albumDAO.fetchAllCategories(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean uploadPhoto(String username, long albumId, String name, InputStream data, int flags) {
		try {
			PhotoDTO photo = albumDAO.insertPhoto(username, albumId, name);
			AlbumFileManager.createPhotoFile(photo.getSource(), data, flags);
			AlbumFileManager.createPhotoThumbnail(photo.getSource());
			return true;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
