package com.viewer.beans;

import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;
import javax.imageio.stream.ImageInputStream;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.SearchQueryDTO;

@Local
public interface AlbumBeanLocal {
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId);
	
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid, SearchQueryDTO searchQuery);

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid);

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid);

	public PhotoDTO fetchPhoto(long userid, long photoid);

	public long createAlbum(long userid, String name, String subtitle, long parentId);

	public ImageInputStream fetchPhotoData(long userid, long photoid);
	
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid, int flags);
	
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid);
	
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid);

	boolean tagUserAlbum(long userid, long albumid, String tag, String category);
	
	boolean createCategory(long userid, String category);

	public List<String> fetchAllUserCategories(long userid);
	
	public boolean uploadPhoto(long userid, long albumId, String name, InputStream data);
}