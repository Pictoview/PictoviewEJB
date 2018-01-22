package com.viewer.beans;

import java.io.InputStream;
import java.util.List;

import javax.ejb.Local;
import javax.imageio.stream.ImageInputStream;

import com.viewer.dto.AlbumDTO;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.MediaDTO;
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
	 * Fetches all Albums belonging to the user
	 * @param userid
	 * @param parentId
	 * @return
	 */
	public List<AlbumDTO> fetchUserAlbums(long userid, long parentId, int ordering, int limit, int offset);
	
	/**
	 * Fetches all Albums viewable by the user
	 * 
	 * @param userid
	 * @param parentId
	 * @return List of DTO encapsulating information regarding album
	 */
	public List<AlbumDTO> fetchViewableAlbums(long userid, long parentId, int ordering, int limit, int offset);

	/**
	 * Fetches all viewable albums subscribed by the user
	 * 
	 * @param username
	 * @param parentId
	 * @return List of DTO encapsulating information regarding album
	 */
	public List<AlbumDTO> fetchUserSubscriptions(long userid, long parentId, int ordering, int limit, int offset);

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
	public List<AlbumDTO> fetchSearchUserSubscribedAlbums(long userid, SearchQueryDTO searchQuery);
	
	/**
	 * Fetches all albums matching search criteria for user
	 * @param userid
	 * @param parentId
	 * @param searchQuery
	 * @return
	 */
	public List<AlbumDTO> fetchSearchUserAlbums(long userid, SearchQueryDTO searchQuery);
	
	public List<AlbumDTO> fetchSearchUserViewableAlbums(long userid, SearchQueryDTO searchQuery);

	/**
	 * Check if album with name exists within parent album
	 * 
	 * @param userid
	 * @param name
	 * @param parentId
	 * @return albumId or -1 if not exist
	 */
	public long albumExist(long userid, String name, long parentId);

	// Permission Operations

	/**
	 * Subscribe to album
	 * 
	 * @param username
	 * @param albumId
	 * @return
	 */
	public boolean subscribeToAlbum(long userid, long albumId);

	/**
	 * Unsubscribe from album
	 * 
	 * @param username
	 * @param albumId
	 * @return
	 */
	public boolean unsubscribeToAlbum(long userid, long albumId);

	/**
	 * Give permission to view album for a single user
	 * 
	 * @param username
	 * @param albumId
	 * @param user
	 */
	public void addPermissionToAlbum(long userid, long albumId, String user);

	/**
	 * Give permission to view album for a list of users
	 * 
	 * @param username
	 * @param albumId
	 * @param users
	 */
	public void addPermissionToAlbum(long userid, long albumId, List<String> users);

	/**
	 * Remove permission to view album for list of users
	 * 
	 * @param username
	 * @param albumId
	 * @param users
	 */
	public void revokePermissionToAlbum(long userid, long albumId, List<String> users);

	// Album Ratings

	/**
	 * Fetch average rating for this album
	 * 
	 * @param userid
	 * @param albumId
	 * @return
	 */
	public int fetchAlbumAverageRating(long userid, long albumId);

	/**
	 * Assigns one rating for this album for a given user if they have
	 * permission to view this file
	 * 
	 * @param userid
	 * @param albumId
	 * @param rating
	 * @return
	 */
	public boolean voteAlbumRating(long userid, long albumId, int rating);

	/**
	 * Fetch the user's rating for this album
	 * 
	 * @param userid
	 * @param albumId
	 * @return
	 */
	public int fetchAlbumUserRating(long userid, long albumId);

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
	public long createAlbum(long userid, String name, String subtitle, String description, long parentId);

	/**
	 * Create a new empty album
	 * 
	 * @param userid
	 * @param name
	 * @param subtitle
	 * @param permission
	 * @return ID of created album
	 */
	public long createAlbum(long userid, String name, String subtitle, String description, String permission);

	/**
	 * Sets the album's cover photo
	 * 
	 * @param username
	 * @param albumid
	 * @param photoid
	 */
	public void setAlbumCoverPhoto(long userid, long albumid, long photoid);

	// Photo Related

	/**
	 * Fetches meta-data for list of photos
	 * 
	 * @param userid
	 * @param albumid
	 * @return List of DTO encapsulating information regarding photo including
	 *         source directory
	 */
	public List<MediaDTO> fetchUserAlbumPhotos(long userid, long albumid);

	/**
	 * Fetches meta-data of photo
	 * 
	 * @param userid
	 * @param photoid
	 * @return DTO encapsulating information regarding photo including source
	 *         directory
	 */
	public MediaDTO fetchPhoto(long userid, long photoid);

	/**
	 * Fetches the data encapsulated in the photo
	 * 
	 * @param userid
	 * @param photoid
	 * @return Stream of photo file data
	 */
	public ImageInputStream fetchPhotoData(long userid, long photoid);

	/**
	 * Fetches the data encapsulated in the photo thumbnail
	 * 
	 * @param userid
	 * @param photoid
	 * @param flags
	 * @return Stream of photo thumbnail file data
	 */
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid, int flags);

	/**
	 * Fetches the data encapsulated in the photo thumbnail
	 * 
	 * @param userid
	 * @param photoid
	 * @return Stream of photo thumbnail file data
	 */
	public ImageInputStream fetchPhotoThumbnailData(long userid, long photoid);

	public ImageInputStream fetchAlbumCoverThumbnail(long userid, long albumId, int flags);

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
	public MediaDTO uploadPhoto(long userid, long albumId, String name, String ext, InputStream data, int flags);

	// Category & Tags

	/**
	 * Fetches all the tags and categories associated with the album
	 * 
	 * @param userid
	 * @param albumid
	 * @return DTO encapsulating the id and map of category and tags
	 */
	public AlbumTagsDTO fetchUserAlbumTags(long userid, long albumid);

	/**
	 * Creates a tag associated to the album
	 * 
	 * @param userid
	 * @param albumid
	 * @param tag
	 * @param category
	 * @return Success status of action
	 */
	boolean tagUserAlbum(long userid, long albumid, String tag, String category);

	boolean tagUserAlbum(long userid, long albumid, List<String> tag, String category);

	/**
	 * Creates an category and associates it with the user
	 * 
	 * @param userid
	 * @param category
	 * @return Success status of action
	 */
	boolean createCategory(long userid, String category);

	/**
	 * Fetches all categories associated with the user
	 * 
	 * @param userid
	 * @return List of categories
	 */
	public List<String> fetchAllUserCategories(long userid);

	/**
	 * Clears all tags on Album without removing categories
	 * 
	 * @param userid
	 * @param albumid
	 * @return Success status of action
	 */
	boolean clearAlbumTag(long userid, long albumid);

	public boolean tagRelevanceAlbum(long tagId);
}