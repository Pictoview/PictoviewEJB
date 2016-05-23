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

	public static enum Permission {
		PUBLIC, PRIVATE, LIMITED
	};

	// Album Fetch Operations

	/**
	 * Fetches all the public albums viewable by all users
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<AlbumDTO> fetchAllPublicAlbums(int limit, int offset);

	/**
	 * Fetches all Albums viewable by the user
	 * 
	 * @param userid
	 * @param parentId
	 * @return List of DTO encapsulating information regarding album
	 */
	public List<AlbumDTO> fetchViewableAlbums(String username, long parentId);

	/**
	 * Fetches all viewable albums subscribed by the user
	 * 
	 * @param username
	 * @param parentId
	 * @return List of DTO encapsulating information regarding album
	 */
	public List<AlbumDTO> fetchUserSubscriptions(String username, long parentId);

	/**
	 * Fetches all albums matching search criteria (Regardless of file hierarchy
	 * duplication)
	 * 
	 * @param userid
	 * @param searchQuery
	 *            DTO encapsulating information regarding search criteria. This
	 *            includes the name and list of category tag associations.
	 * @return List of DTO encapsulating information regarding album
	 */
	public List<AlbumDTO> fetchSearchedUserAlbums(String username, SearchQueryDTO searchQuery);

	/**
	 * Fetches a single Album associated with user
	 * 
	 * @param userid
	 * @param albumid
	 * @return List of DTO encapsulating information regarding album
	 */
	public AlbumDTO fetchUserAlbumInfo(String username, long albumid);

	/**
	 * Check if album with name exists within parent album
	 * 
	 * @param userid
	 * @param name
	 * @param parentId
	 * @return albumId or -1 if not exist
	 */
	public long albumExist(String username, String name, long parentId);

	// Permission Operations

	/**
	 * Subscribe to album
	 * 
	 * @param username
	 * @param albumId
	 * @return
	 */
	public boolean subscribeToAlbum(String username, long albumId);

	/**
	 * Unsubscribe from album
	 * 
	 * @param username
	 * @param albumId
	 * @return
	 */
	public boolean unsubscribeToAlbum(String username, long albumId);

	/**
	 * Give permission to view album for a single user
	 * 
	 * @param username
	 * @param albumId
	 * @param user
	 */
	public void addPermissionToAlbum(String username, long albumId, String user);

	/**
	 * Give permission to view album for a list of users
	 * 
	 * @param username
	 * @param albumId
	 * @param users
	 */
	public void addPermissionToAlbum(String username, long albumId, List<String> users);

	/**
	 * Remove permission to view album for list of users
	 * 
	 * @param username
	 * @param albumId
	 * @param users
	 */
	public void revokePermissionToAlbum(String username, long albumId, List<String> users);

	// Album Updates

	/**
	 * Create a new empty album
	 * 
	 * @param userid
	 * @param name
	 * @param subtitle
	 * @param parentId
	 * @return ID of created album
	 */
	public long createAlbum(String username, String name, String subtitle, String description, long parentId);

	/**
	 * Create a new empty album
	 * 
	 * @param userid
	 * @param name
	 * @param subtitle
	 * @param permission
	 * @return ID of created album
	 */
	public long createAlbum(String username, String name, String subtitle, String description, String permission);

	/**
	 * Sets the album's cover photo
	 * 
	 * @param username
	 * @param albumid
	 * @param photoid
	 */
	public void setAlbumCoverPhoto(String username, long albumid, long photoid);

	// Photo Related

	/**
	 * Fetches meta-data for list of photos
	 * 
	 * @param userid
	 * @param albumid
	 * @return List of DTO encapsulating information regarding photo including
	 *         source directory
	 */
	public List<PhotoDTO> fetchUserAlbumPhotos(String username, long albumid);

	/**
	 * Fetches meta-data of photo
	 * 
	 * @param userid
	 * @param photoid
	 * @return DTO encapsulating information regarding photo including source
	 *         directory
	 */
	public PhotoDTO fetchPhoto(String username, long photoid);

	/**
	 * Fetches the data encapsulated in the photo
	 * 
	 * @param userid
	 * @param photoid
	 * @return Stream of photo file data
	 */
	public ImageInputStream fetchPhotoData(String username, long photoid);

	/**
	 * Fetches the data encapsulated in the photo thumbnail
	 * 
	 * @param userid
	 * @param photoid
	 * @param flags
	 * @return Stream of photo thumbnail file data
	 */
	public ImageInputStream fetchPhotoThumbnailData(String username, long photoid, int flags);

	/**
	 * Fetches the data encapsulated in the photo thumbnail
	 * 
	 * @param userid
	 * @param photoid
	 * @return Stream of photo thumbnail file data
	 */
	public ImageInputStream fetchPhotoThumbnailData(String username, long photoid);

	public ImageInputStream fetchAlbumCoverThumbnail(String username, long photoid, int flags);

	/**
	 * Uploads a photo to server file repository
	 * 
	 * @param userid
	 * @param albumId
	 * @param name
	 *            New name of file
	 * @param data
	 *            InputStream to file being transferred
	 * @return Success status of action
	 */
	public PhotoDTO uploadPhoto(String username, long albumId, String name, String ext, InputStream data, int flags);

	// Category & Tags

	/**
	 * Fetches all the tags and categories associated with the album
	 * 
	 * @param userid
	 * @param albumid
	 * @return DTO encapsulating the id and map of category and tags
	 */
	public AlbumTagsDTO fetchUserAlbumTags(String username, long albumid);

	/**
	 * Creates a tag associated to the album
	 * 
	 * @param userid
	 * @param albumid
	 * @param tag
	 * @param category
	 * @return Success status of action
	 */
	boolean tagUserAlbum(String username, long albumid, String tag, String category);

	/**
	 * Creates an category and associates it with the user
	 * 
	 * @param userid
	 * @param category
	 * @return Success status of action
	 */
	boolean createCategory(String username, String category);

	/**
	 * Fetches all categories associated with the user
	 * 
	 * @param userid
	 * @return List of categories
	 */
	public List<String> fetchAllUserCategories(String username);

	/**
	 * Clears all tags on Album without removing categories
	 * 
	 * @param userid
	 * @param albumid
	 * @return Success status of action
	 */
	boolean clearAlbumTag(String username, long albumid);
}