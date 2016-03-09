package com.test.albums.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.viewer.dao.AccountDAO;
import com.viewer.dao.AlbumDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dao.impl.SQLAccountDAO;
import com.viewer.dao.impl.SQLAlbumDAO;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.UserDataDTO;

public class DatabaseTest {

	private AccountDAO accountDAO;
	private AlbumDAO albumDAO;

	private String mockUser1 = "StandardUser1";
	private String mockUser2 = "StandardUser2";
	private String mockUser3 = "StandardUser3";
	private String mockUser4 = "StandardUser4";

	@BeforeClass
	public static void initialize() {
		SQLConnector.connect();
	}

	@AfterClass
	public static void deInitialize() {
		SQLConnector.disconnect();
	}

	@Before
	public void setUp() {
		// Initialize Global Objects
		accountDAO = new SQLAccountDAO();
		albumDAO = new SQLAlbumDAO();
		// Creating Mock User
		createMockUser(mockUser1);
		createMockUser(mockUser2);
		createMockUser(mockUser3);
	}

	private long createMockUser(String username) {
		try {
			UserDataDTO userData = UserDataDTO.createRegularUser(username, "password".getBytes(), "Mock#" + username, true,
					"Test User #" + username);
			return accountDAO.registerUser(userData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private boolean deleteMockUsers(String username) {
		try {
			return accountDAO.deleteUser(username, "password".getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@After
	public void tearDown() {
		deleteMockUsers(mockUser1);
		deleteMockUsers(mockUser2);
		deleteMockUsers(mockUser3);
	}

	/** Tests **/

	@Test
	public void fetchUserAlbums() {
		try {
			// Create albums
			long albumId1$1 = albumDAO.createAlbum(mockUser1, "Album1", "User1 (PUBLIC)", "PUBLIC");
			long albumId1$2 = albumDAO.createAlbum(mockUser1, "Album1-1", "User1 (PUBLIC)", albumId1$1);

			long albumId2$1 = albumDAO.createAlbum(mockUser2, "Album2", "User2 (PUBLIC)", "PUBLIC");
			long albumId2$2 = albumDAO.createAlbum(mockUser2, "Album3", "User2 (PRIVATE)", "PRIVATE");
			long albumId2$3 = albumDAO.createAlbum(mockUser2, "Album4", "User2 (LIMITED)", "LIMITED");

			List<String> allowedUsers2$3 = new ArrayList<String>();
			allowedUsers2$3.add(mockUser1);
			albumDAO.addPermissionToAlbum(mockUser2, albumId2$3, allowedUsers2$3);

			long albumId3$1 = albumDAO.createAlbum(mockUser3, "Album5", "User3 (PUBLIC)", "PUBLIC");
			long albumId3$2 = albumDAO.createAlbum(mockUser3, "Album5-1", "User3 (PUBLIC)", albumId3$1);
			long albumId3$3 = albumDAO.createAlbum(mockUser3, "Album5-1-1", "User3 (PUBLIC)", albumId3$2);

			// Fetch public albums
			List<AlbumDTO> publicAlbums = albumDAO.fetchAllPublicAlbums(25, 0);

			// Fetch viewable albums
			List<AlbumDTO> limitedAlbums1 = albumDAO.fetchViewableAlbums(mockUser1, 0);
			List<AlbumDTO> limitedAlbums2 = albumDAO.fetchViewableAlbums(mockUser1, albumId1$1);

			List<AlbumDTO> limitedAlbums3 = albumDAO.fetchViewableAlbums(mockUser2, 0);

			List<AlbumDTO> limitedAlbums4 = albumDAO.fetchViewableAlbums(mockUser3, 0);
			List<AlbumDTO> limitedAlbums5 = albumDAO.fetchViewableAlbums(mockUser3, albumId3$1);
			List<AlbumDTO> limitedAlbums6 = albumDAO.fetchViewableAlbums(mockUser3, albumId3$2);

			// Delete All Albums
			albumDAO.deleteAlbum(mockUser1, albumId1$1);
			albumDAO.deleteAlbum(mockUser1, albumId1$2);
			albumDAO.deleteAlbum(mockUser2, albumId2$1);
			albumDAO.deleteAlbum(mockUser2, albumId2$2);
			albumDAO.deleteAlbum(mockUser2, albumId2$3);
			albumDAO.deleteAlbum(mockUser3, albumId3$1);
			albumDAO.deleteAlbum(mockUser3, albumId3$2);
			albumDAO.deleteAlbum(mockUser3, albumId3$3);

			// Check Assertions
			checkCorrectAlbumsIDs(publicAlbums, albumId1$1, albumId2$1, albumId3$1);
			checkCorrectAlbumsIDs(limitedAlbums1, albumId1$1, albumId2$1, albumId2$3, albumId3$1); // User1
			checkCorrectAlbumsIDs(limitedAlbums2, albumId1$2);

			checkCorrectAlbumsIDs(limitedAlbums3, albumId1$1, albumId2$1, albumId2$2, albumId2$3, albumId3$1); // User2

			checkCorrectAlbumsIDs(limitedAlbums4, albumId1$1, albumId2$1, albumId3$1); // User3
			checkCorrectAlbumsIDs(limitedAlbums5, albumId3$2);
			checkCorrectAlbumsIDs(limitedAlbums6, albumId3$3);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fetchSubscribedAlbums() {

		// Create albums
		try {

			long albumId1$1 = albumDAO.createAlbum(mockUser1, "Album1", "User1 (PUBLIC)", "PUBLIC");
			long albumId1$2 = albumDAO.createAlbum(mockUser1, "Album1-1", "User1 (PUBLIC)", albumId1$1);

			long albumId2$1 = albumDAO.createAlbum(mockUser2, "Album2", "User2 (PUBLIC)", "PUBLIC");
			long albumId2$2 = albumDAO.createAlbum(mockUser2, "Album3", "User2 (PRIVATE)", "PRIVATE");
			long albumId2$3 = albumDAO.createAlbum(mockUser2, "Album4", "User2 (LIMITED)", "LIMITED");

			List<String> allowedUsers2$3 = new ArrayList<String>();
			allowedUsers2$3.add(mockUser1);
			albumDAO.addPermissionToAlbum(mockUser2, albumId2$3, allowedUsers2$3);

			long albumId3$1 = albumDAO.createAlbum(mockUser3, "Album3", "User3 (PRIVATE)", "PRIVATE");
			long albumId3$2 = albumDAO.createAlbum(mockUser3, "Album3-1", "User3 (PRIVATE)", albumId3$1);
			long albumId3$3 = albumDAO.createAlbum(mockUser3, "Album3-1-1", "User3 (PRIVATE)", albumId3$2);

			// Subscribe to Albums
			albumDAO.subscribeToAlbum(mockUser1, albumId2$1);
			albumDAO.subscribeToAlbum(mockUser1, albumId2$2);

			// Fetch subscribed albums
			List<AlbumDTO> limitedAlbums1 = albumDAO.fetchAllSubscribedAlbums(mockUser1, 0);

			// Delete Subscriptions
			albumDAO.unsubscribeToAlbum(mockUser1, albumId2$1);
			albumDAO.unsubscribeToAlbum(mockUser1, albumId2$2);

			// Delete All Albums
			albumDAO.deleteAlbum(mockUser1, albumId1$1);
			albumDAO.deleteAlbum(mockUser1, albumId1$2);
			albumDAO.deleteAlbum(mockUser2, albumId2$1);
			albumDAO.deleteAlbum(mockUser2, albumId2$2);
			albumDAO.deleteAlbum(mockUser2, albumId2$3);
			albumDAO.deleteAlbum(mockUser3, albumId3$1);
			albumDAO.deleteAlbum(mockUser3, albumId3$2);
			albumDAO.deleteAlbum(mockUser3, albumId3$3);

			// Test Assertions
			checkCorrectAlbumsIDs(limitedAlbums1, albumId1$1, albumId2$1, albumId2$3);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPhotoAccess() {
		try {
			// Create Album & Photos
			long albumId1 = albumDAO.createAlbum(mockUser1, "Album1", "User1 Album1", "PUBLIC");
			long albumId2 = albumDAO.createAlbum(mockUser1, "Album1-1", "User1 Album", albumId1);
			long albumId3 = albumDAO.createAlbum(mockUser1, "Album1-1-1", "User1 Album", albumId2);
			PhotoDTO photoId1U1 = albumDAO.insertPhoto(mockUser1, albumId1, "A1(P1)");
			PhotoDTO photoId1U2 = albumDAO.insertPhoto(mockUser1, albumId2, "A1-1(P2)");
			PhotoDTO photoId1U3 = albumDAO.insertPhoto(mockUser1, albumId2, "A1-1(P3)");
			PhotoDTO photoId1U4 = albumDAO.insertPhoto(mockUser1, albumId3, "A1-1-1(P4)");

			long albumId4 = albumDAO.createAlbum(mockUser2, "Album2", "User2 Album1", "LIMITED");
			long albumId5 = albumDAO.createAlbum(mockUser2, "Album2-1", "User2 Album", albumId4);
			long albumId6 = albumDAO.createAlbum(mockUser2, "Album2-1-1", "User2 Album", albumId5);
			PhotoDTO photoId2U1 = albumDAO.insertPhoto(mockUser2, albumId4, "A2(P5)");
			PhotoDTO photoId2U2 = albumDAO.insertPhoto(mockUser2, albumId5, "A2-1(P6)");
			PhotoDTO photoId2U3 = albumDAO.insertPhoto(mockUser2, albumId6, "A2-1-1(P7)");

			long albumId7 = albumDAO.createAlbum(mockUser3, "Album3", "User3 Album1", "PRIVATE");
			long albumId8 = albumDAO.createAlbum(mockUser3, "Album3-1", "User3 Album (PRIVATE)", albumId7);
			long albumId9 = albumDAO.createAlbum(mockUser3, "Album3-1-1", "User3 Album (PRIVATE)", albumId8);
			PhotoDTO photoId3U1 = albumDAO.insertPhoto(mockUser3, albumId7, "A3(P8)");
			PhotoDTO photoId3U2 = albumDAO.insertPhoto(mockUser3, albumId8, "A3-1(P9)");
			PhotoDTO photoId3U3 = albumDAO.insertPhoto(mockUser3, albumId9, "A3-1-1(P10)");

			List<String> allowedUsers = new ArrayList<String>();
			allowedUsers.add(mockUser1);
			albumDAO.addPermissionToAlbum(mockUser2, albumId4, allowedUsers);
			albumDAO.addPermissionToAlbum(mockUser2, albumId5, allowedUsers);
			albumDAO.addPermissionToAlbum(mockUser2, albumId6, allowedUsers);

			// Test User 1 Accessibility
			List<PhotoDTO> user1PhotosA1 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId1);
			List<PhotoDTO> user1PhotosA2 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId2);
			List<PhotoDTO> user1PhotosA3 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId3);
			List<PhotoDTO> user1PhotosA4 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId4);
			List<PhotoDTO> user1PhotosA5 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId5);
			List<PhotoDTO> user1PhotosA6 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId6);
			List<PhotoDTO> user1PhotosA7 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId7);
			List<PhotoDTO> user1PhotosA8 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId8);
			List<PhotoDTO> user1PhotosA9 = albumDAO.fetchUserAlbumPhotos(mockUser1, albumId9);

			// Test User 2 Accessibility
			List<PhotoDTO> user2PhotosA1 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId1);
			List<PhotoDTO> user2PhotosA2 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId2);
			List<PhotoDTO> user2PhotosA3 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId3);
			List<PhotoDTO> user2PhotosA4 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId4);
			List<PhotoDTO> user2PhotosA5 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId5);
			List<PhotoDTO> user2PhotosA6 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId6);
			List<PhotoDTO> user2PhotosA7 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId7);
			List<PhotoDTO> user2PhotosA8 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId8);
			List<PhotoDTO> user2PhotosA9 = albumDAO.fetchUserAlbumPhotos(mockUser2, albumId9);

			// Test User 3 Accessibility
			List<PhotoDTO> user3PhotosA1 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId1);
			List<PhotoDTO> user3PhotosA2 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId2);
			List<PhotoDTO> user3PhotosA3 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId3);
			List<PhotoDTO> user3PhotosA4 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId4);
			List<PhotoDTO> user3PhotosA5 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId5);
			List<PhotoDTO> user3PhotosA6 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId6);
			List<PhotoDTO> user3PhotosA7 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId7);
			List<PhotoDTO> user3PhotosA8 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId8);
			List<PhotoDTO> user3PhotosA9 = albumDAO.fetchUserAlbumPhotos(mockUser3, albumId9);

			// Delete All Albums
			albumDAO.deleteAlbum(mockUser1, albumId1);
			albumDAO.deleteAlbum(mockUser1, albumId2);
			albumDAO.deleteAlbum(mockUser1, albumId3);
			albumDAO.deleteAlbum(mockUser2, albumId4);
			albumDAO.deleteAlbum(mockUser2, albumId5);
			albumDAO.deleteAlbum(mockUser2, albumId6);
			albumDAO.deleteAlbum(mockUser3, albumId7);
			albumDAO.deleteAlbum(mockUser3, albumId8);
			albumDAO.deleteAlbum(mockUser3, albumId9);

			// Check Assertions
			checkCorrectPhotoIDs(user1PhotosA1, photoId1U1.getId()); // User 1
			checkCorrectPhotoIDs(user1PhotosA2, photoId1U2.getId(), photoId1U3.getId());
			checkCorrectPhotoIDs(user1PhotosA3, photoId1U4.getId());
			checkCorrectPhotoIDs(user1PhotosA4, photoId2U1.getId());
			checkCorrectPhotoIDs(user1PhotosA5, photoId2U2.getId());
			checkCorrectPhotoIDs(user1PhotosA6, photoId2U3.getId());
			checkCorrectPhotoIDs(user1PhotosA7);
			checkCorrectPhotoIDs(user1PhotosA8);
			checkCorrectPhotoIDs(user1PhotosA9);

			checkCorrectPhotoIDs(user2PhotosA1, photoId1U1.getId()); // User 2
			checkCorrectPhotoIDs(user2PhotosA2, photoId1U2.getId(), photoId1U3.getId());
			checkCorrectPhotoIDs(user2PhotosA3, photoId1U4.getId());
			checkCorrectPhotoIDs(user2PhotosA4, photoId2U1.getId());
			checkCorrectPhotoIDs(user2PhotosA5, photoId2U2.getId());
			checkCorrectPhotoIDs(user2PhotosA6, photoId2U3.getId());
			checkCorrectPhotoIDs(user2PhotosA7);
			checkCorrectPhotoIDs(user2PhotosA8);
			checkCorrectPhotoIDs(user2PhotosA9);

			checkCorrectPhotoIDs(user3PhotosA1, photoId1U1.getId()); // User 3
			checkCorrectPhotoIDs(user3PhotosA2, photoId1U2.getId(), photoId1U3.getId());
			checkCorrectPhotoIDs(user3PhotosA3, photoId1U4.getId());
			checkCorrectPhotoIDs(user3PhotosA4);
			checkCorrectPhotoIDs(user3PhotosA5);
			checkCorrectPhotoIDs(user3PhotosA6);
			checkCorrectPhotoIDs(user3PhotosA7, photoId3U1.getId());
			checkCorrectPhotoIDs(user3PhotosA8, photoId3U2.getId());
			checkCorrectPhotoIDs(user3PhotosA9, photoId3U3.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void checkCorrectAlbumsIDs(List<AlbumDTO> albums, long... ids) {
		Assert.assertEquals(ids.length, albums.size());
		Set<Long> correctIds = createIdSets(ids);
		for (AlbumDTO dto : albums) {
			Assert.assertTrue(correctIds.contains(dto.getId()));
			correctIds.remove(dto.getId());
		}
	}

	private void checkCorrectPhotoIDs(List<PhotoDTO> photos, long... ids) {
		Assert.assertEquals(ids.length, photos.size());
		Set<Long> correctIds = createIdSets(ids);
		for (PhotoDTO dto : photos) {
			Assert.assertTrue(correctIds.contains(dto.getId()));
			correctIds.remove(dto.getId());
		}
	}

	private Set<Long> createIdSets(long... ids) {
		Set<Long> set = new HashSet<Long>();
		for (long id : ids) {
			set.add(id);
		}
		return set;
	}

	@SuppressWarnings("unused")
	private void deleteAllSubscriptions() throws SQLException {
		Connection conn = SQLConnector.connect();

		PreparedStatement stmt = conn.prepareStatement("DELETE FROM UserSubscriptions");
		stmt.executeUpdate();
		stmt.close();
	}

	@SuppressWarnings("unused")
	private void deleteAllAlbums() throws SQLException {
		Connection conn = SQLConnector.connect();

		PreparedStatement stmt = conn.prepareStatement("DELETE FROM Albums");
		stmt.executeUpdate();
		stmt.close();
	}
}
