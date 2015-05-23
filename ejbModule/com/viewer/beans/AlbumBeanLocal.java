package com.viewer.beans;

import java.util.List;

import javax.ejb.Local;
import javax.imageio.stream.ImageInputStream;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.PhotoDTO;

@Local
public interface AlbumBeanLocal {
	public List<AlbumDTO> fetchAllUserAlbums(long userid, long parentId);
	
	public List<AlbumDTO> fetchSearchedUserAlbums(long userid, String searchName, String... tags);

	public AlbumDTO fetchUserAlbumInfo(long userid, long albumid);

	public List<PhotoDTO> fetchUserAlbumPhotos(long userid, long albumid);

	public PhotoDTO fetchPhoto(long userid, long photoid);

	public boolean createAlbum(long userid, AlbumDTO album);

	public ImageInputStream fetchPhotoData(long userid, long photoid);
	
	public byte[] fetchPhotoThumbnailData(long userid, long photoid, int flags);
	
	public byte[] fetchPhotoThumbnailData(long userid, long photoid);
	
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid);

	boolean tagUserAlbum(long userid, long albumid, String tag, String category);

	public List<String> fetchAllUserCategories(long userid);
}