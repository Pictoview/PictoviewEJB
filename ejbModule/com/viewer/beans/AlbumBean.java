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

	// Album Fetch Operations

	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset) {
		try {
			return albumDAO.fetchAllPublicAlbums(limit, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AlbumDTO> fetchViewableAlbums(long userid, long parentId) {
		try {
			return albumDAO.fetchViewableAlbums(userid, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AlbumDTO> fetchUserSubscriptions(long userid, long parentId) {
		try {
			return albumDAO.fetchAllSubscribedAlbums(userid, parentId);
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

	// Permission Operations

	@Override
	public boolean subscribeToAlbum(long userid, long albumId) {
		try {
			return albumDAO.subscribeToAlbum(userid, albumId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean unsubscribeToAlbum(long userid, long albumId) {
		try {
			return albumDAO.unsubscribeToAlbum(userid, albumId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void addPermissionToAlbum(long userid, long albumId, String user) {
		try {
			albumDAO.addPermissionToAlbum(userid, albumId, user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addPermissionToAlbum(long userid, long albumId, List<String> users) {
		try {
			albumDAO.addPermissionToAlbum(userid, albumId, users);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void revokePermissionToAlbum(long userid, long albumId, List<String> users) {
		try {
			albumDAO.revokePermissionToAlbum(userid, albumId, users);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Album Updates

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, String permission) {
		try {
			return albumDAO.createAlbum(userid, name, subtitle, description, permission);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public long createAlbum(long userid, String name, String subtitle, String description, long parentId) {
		try {
			return albumDAO.createAlbum(userid, name, subtitle, description, parentId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public void setAlbumCoverPhoto(long userid, long albumid, long photoid) {
		try {
			albumDAO.setAlbumCoverPhoto(userid, albumid,  photoid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Photo Related

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
			return albumDAO.fetchPhoto(userid, photoid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ImageInputStream fetchPhotoData(long userid, long photoid) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(userid, photoid);
			String source = AlbumFileManager.StorageLocation + photoDTO.getSource();
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
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid, int flags) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchPhoto(userid, photoid);
			String source = AlbumFileManager.ThumbnailStorageLocation + photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ImageInputStream fetchAlbumCoverThumbnail(long userid, long albumid, int flags) {
		try {
			PhotoDTO photoDTO = albumDAO.fetchAlbumCoverPhoto(userid, albumid);
			String source = AlbumFileManager.ThumbnailStorageLocation + photoDTO.getSource();
			return ImageIO.createImageInputStream(new File(source));
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
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
	public boolean tagUserAlbum(long userid, long albumid, String tag, String category) {
		try {
			return albumDAO.tagAlbum(userid, tag, albumid, category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean tagUserAlbum(long userid, long albumid, List<String> tag, String category) {
		try {
			return albumDAO.tagAlbum(userid, tag, albumid, category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean tagRelevanceAlbum(long tagId) {
		try {
			return albumDAO.tagRelevanceAlbum(tagId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean clearAlbumTag(long userid, long albumid) {
		try {
			return albumDAO.clearAlbumTag(albumid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createCategory(long userid, String category) {
		try {
			albumDAO.createCategory(userid, category);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> fetchAllUserCategories(long userid) {
		try {
			return albumDAO.fetchAllCategories("FULL");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PhotoDTO uploadPhoto(long userid, long albumId, String name, String ext, InputStream data, int flags) {
		try {
			PhotoDTO photo = albumDAO.insertPhoto(userid, albumId, name, ext);
			AlbumFileManager.createPhotoFile(photo.getSource(), data, flags);
			AlbumFileManager.createPhotoThumbnail(photo.getSource());
			return photo;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
