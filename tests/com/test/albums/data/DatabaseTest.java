package com.test.albums.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	
	private Map<String, Long> userIds = new HashMap<String, Long>();

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
		userIds.put(mockUser1, createMockUser(mockUser1));
		userIds.put(mockUser2, createMockUser(mockUser2));
		userIds.put(mockUser3, createMockUser(mockUser3));
		userIds.put(mockUser4, createMockUser(mockUser4));
	}

	private long createMockUser(String username) {
		try {
			UserDataDTO userData = UserDataDTO.createRegularUser(username, "password", "Mock#" + username, true,
					username + "@email.com", "Test User #" + username);
			return accountDAO.registerUser(userData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private boolean deleteMockUsers(String username) {
		try {
			return accountDAO.deleteUser(username, "password");
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
		deleteMockUsers(mockUser4);
	}

	/** Tests **/

	@Test
	public void fetchUserAlbums() {
		try {
			// Create albums
			long albumId1$1 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1", "User1 (PUBLIC)", "AlbumDescription", "PUBLIC");
			long albumId1$2 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1-1", "User1 (PUBLIC)", "AlbumDescription", albumId1$1);

			long albumId2$1 = albumDAO.createAlbum(userIds.get(mockUser2), "Album2", "User2 (PUBLIC)", "AlbumDescription", "PUBLIC");
			long albumId2$2 = albumDAO.createAlbum(userIds.get(mockUser2), "Album3", "User2 (PRIVATE)", "AlbumDescription", "PRIVATE");
			long albumId2$3 = albumDAO.createAlbum(userIds.get(mockUser2), "Album4", "User2 (LIMITED)", "AlbumDescription", "LIMITED");

			List<String> allowedUsers2$3 = new ArrayList<String>();
			allowedUsers2$3.add(mockUser1);
			albumDAO.addPermissionToAlbum(userIds.get(mockUser2), albumId2$3, allowedUsers2$3);

			long albumId3$1 = albumDAO.createAlbum(userIds.get(mockUser3), "Album5", "User3 (PUBLIC)", "AlbumDescription", "PUBLIC");
			long albumId3$2 = albumDAO.createAlbum(userIds.get(mockUser3), "Album5-1", "User3 (PUBLIC)", "AlbumDescription", albumId3$1);
			long albumId3$3 = albumDAO.createAlbum(userIds.get(mockUser3), "Album5-1-1", "User3 (PUBLIC)", "AlbumDescription", albumId3$2);

			// Fetch public albums
			List<AlbumDTO> publicAlbums = albumDAO.fetchAllPublicAlbums(25, 0);

			// Fetch viewable albums
			List<AlbumDTO> limitedAlbums1 = albumDAO.fetchViewableAlbums(userIds.get(mockUser1), 0, 0, 50, 0);
			List<AlbumDTO> limitedAlbums2 = albumDAO.fetchViewableAlbums(userIds.get(mockUser1), albumId1$1, 0, 50, 0);

			List<AlbumDTO> limitedAlbums3 = albumDAO.fetchViewableAlbums(userIds.get(mockUser2), 0, 0, 50, 0);

			List<AlbumDTO> limitedAlbums4 = albumDAO.fetchViewableAlbums(userIds.get(mockUser3), 0, 0, 50, 0);
			List<AlbumDTO> limitedAlbums5 = albumDAO.fetchViewableAlbums(userIds.get(mockUser3), albumId3$1, 0, 50, 0);
			List<AlbumDTO> limitedAlbums6 = albumDAO.fetchViewableAlbums(userIds.get(mockUser3), albumId3$2, 0, 50, 0);

			// Delete All Albums
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId1$1);
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId1$2);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$1);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$2);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$3);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$1);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$2);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$3);

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

			long albumId1$1 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1", "User1 (PUBLIC)", "AlbumDescription", "PUBLIC");
			long albumId1$2 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1-1", "User1 (PUBLIC)", "AlbumDescription", albumId1$1);

			long albumId2$1 = albumDAO.createAlbum(userIds.get(mockUser2), "Album2", "User2 (PUBLIC)", "AlbumDescription", "PUBLIC");
			long albumId2$2 = albumDAO.createAlbum(userIds.get(mockUser2), "Album3", "User2 (PRIVATE)", "AlbumDescription", "PRIVATE");
			long albumId2$3 = albumDAO.createAlbum(userIds.get(mockUser2), "Album4", "User2 (LIMITED)", "AlbumDescription", "LIMITED");

			List<String> allowedUsers2$3 = new ArrayList<String>();
			allowedUsers2$3.add(mockUser1);
			albumDAO.addPermissionToAlbum(userIds.get(mockUser2), albumId2$3, allowedUsers2$3);

			long albumId3$1 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3", "User3 (PRIVATE)", "AlbumDescription", "PRIVATE");
			long albumId3$2 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3-1", "User3 (PRIVATE)", "AlbumDescription", albumId3$1);
			long albumId3$3 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3-1-1", "User3 (PRIVATE)", "AlbumDescription", albumId3$2);

			// Subscribe to Albums
			albumDAO.subscribeToAlbum(userIds.get(mockUser1), albumId2$1);
			albumDAO.subscribeToAlbum(userIds.get(mockUser1), albumId2$2);

			// Fetch subscribed albums
			List<AlbumDTO> limitedAlbums1 = albumDAO.fetchAllSubscribedAlbums(userIds.get(mockUser1), 0, 0, 50, 0);

			// Delete Subscriptions
			albumDAO.unsubscribeToAlbum(userIds.get(mockUser1), albumId2$1);
			albumDAO.unsubscribeToAlbum(userIds.get(mockUser1), albumId2$2);

			// Delete All Albums
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId1$1);
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId1$2);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$1);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$2);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId2$3);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$1);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$2);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId3$3);

			// Test Assertions
			checkCorrectAlbumsIDs(limitedAlbums1, albumId1$1, albumId2$1, albumId2$3);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPhotoAccess() {
		try {
			// Create Album & Photos
			long albumId1 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1", "User1 Album1", "AlbumDescription", "PUBLIC");
			long albumId2 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1-1", "User1 Album", "AlbumDescription", albumId1);
			long albumId3 = albumDAO.createAlbum(userIds.get(mockUser1), "Album1-1-1", "User1 Album", "AlbumDescription", albumId2);
			PhotoDTO photoId1U1 = albumDAO.insertPhoto(userIds.get(mockUser1), albumId1, "jpg", "A1(P1)");
			PhotoDTO photoId1U2 = albumDAO.insertPhoto(userIds.get(mockUser1), albumId2, "jpg", "A1-1(P2)");
			PhotoDTO photoId1U3 = albumDAO.insertPhoto(userIds.get(mockUser1), albumId2, "jpg", "A1-1(P3)");
			PhotoDTO photoId1U4 = albumDAO.insertPhoto(userIds.get(mockUser1), albumId3, "jpg", "A1-1-1(P4)");
			
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser1), albumId1, photoId1U1.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser1), albumId2, photoId1U2.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser1), albumId3, photoId1U4.getId());

			long albumId4 = albumDAO.createAlbum(userIds.get(mockUser2), "Album2", "User2 Album1", "AlbumDescription", "LIMITED");
			long albumId5 = albumDAO.createAlbum(userIds.get(mockUser2), "Album2-1", "User2 Album", "AlbumDescription", albumId4);
			long albumId6 = albumDAO.createAlbum(userIds.get(mockUser2), "Album2-1-1", "User2 Album", "AlbumDescription", albumId5);
			PhotoDTO photoId2U1 = albumDAO.insertPhoto(userIds.get(mockUser2), albumId4, "jpg", "A2(P5)");
			PhotoDTO photoId2U2 = albumDAO.insertPhoto(userIds.get(mockUser2), albumId5, "jpg", "A2-1(P6)");
			PhotoDTO photoId2U3 = albumDAO.insertPhoto(userIds.get(mockUser2), albumId6, "jpg", "A2-1-1(P7)");
			
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser2), albumId4, photoId2U1.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser2), albumId5, photoId2U2.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser2), albumId6, photoId2U3.getId());

			long albumId7 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3", "User3 Album1", "AlbumDescription", "PRIVATE");
			long albumId8 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3-1", "User3 Album (PRIVATE)", "AlbumDescription", albumId7);
			long albumId9 = albumDAO.createAlbum(userIds.get(mockUser3), "Album3-1-1", "User3 Album (PRIVATE)", "AlbumDescription", albumId8);
			PhotoDTO photoId3U1 = albumDAO.insertPhoto(userIds.get(mockUser3), albumId7, "jpg", "A3(P8)");
			PhotoDTO photoId3U2 = albumDAO.insertPhoto(userIds.get(mockUser3), albumId8, "jpg", "A3-1(P9)");
			PhotoDTO photoId3U3 = albumDAO.insertPhoto(userIds.get(mockUser3), albumId9, "jpg", "A3-1-1(P10)");
			
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser3), albumId7, photoId3U1.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser3), albumId8, photoId3U2.getId());
			albumDAO.setAlbumCoverPhoto(userIds.get(mockUser3), albumId9, photoId3U3.getId());

			List<String> allowedUsers = new ArrayList<String>();
			allowedUsers.add(mockUser1);
			albumDAO.addPermissionToAlbum(userIds.get(mockUser2), albumId4, allowedUsers);
			albumDAO.addPermissionToAlbum(userIds.get(mockUser2), albumId5, allowedUsers);
			albumDAO.addPermissionToAlbum(userIds.get(mockUser2), albumId6, allowedUsers);

			// Test User 1 Accessibility
			List<PhotoDTO> user1PhotosA1 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId1);
			List<PhotoDTO> user1PhotosA2 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId2);
			List<PhotoDTO> user1PhotosA3 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId3);
			List<PhotoDTO> user1PhotosA4 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId4);
			List<PhotoDTO> user1PhotosA5 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId5);
			List<PhotoDTO> user1PhotosA6 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId6);
			List<PhotoDTO> user1PhotosA7 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId7);
			List<PhotoDTO> user1PhotosA8 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId8);
			List<PhotoDTO> user1PhotosA9 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser1), albumId9);

			// Test User 2 Accessibility
			List<PhotoDTO> user2PhotosA1 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId1);
			List<PhotoDTO> user2PhotosA2 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId2);
			List<PhotoDTO> user2PhotosA3 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId3);
			List<PhotoDTO> user2PhotosA4 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId4);
			List<PhotoDTO> user2PhotosA5 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId5);
			List<PhotoDTO> user2PhotosA6 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId6);
			List<PhotoDTO> user2PhotosA7 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId7);
			List<PhotoDTO> user2PhotosA8 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId8);
			List<PhotoDTO> user2PhotosA9 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser2), albumId9);

			// Test User 3 Accessibility
			List<PhotoDTO> user3PhotosA1 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId1);
			List<PhotoDTO> user3PhotosA2 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId2);
			List<PhotoDTO> user3PhotosA3 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId3);
			List<PhotoDTO> user3PhotosA4 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId4);
			List<PhotoDTO> user3PhotosA5 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId5);
			List<PhotoDTO> user3PhotosA6 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId6);
			List<PhotoDTO> user3PhotosA7 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId7);
			List<PhotoDTO> user3PhotosA8 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId8);
			List<PhotoDTO> user3PhotosA9 = albumDAO.fetchUserAlbumPhotos(userIds.get(mockUser3), albumId9);
			
			// Check Album Cover photos
			PhotoDTO coverPhoto1 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser1), albumId1);
			PhotoDTO coverPhoto2 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser1), albumId2);
			PhotoDTO coverPhoto3 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser1), albumId3);
			PhotoDTO coverPhoto4 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser2), albumId4);
			PhotoDTO coverPhoto5 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser2), albumId5);
			PhotoDTO coverPhoto6 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser2), albumId6);
			PhotoDTO coverPhoto7 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser3), albumId7);
			PhotoDTO coverPhoto8 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser3), albumId8);
			PhotoDTO coverPhoto9 = albumDAO.fetchAlbumCoverPhoto(userIds.get(mockUser3), albumId9);
			

			// Delete All Albums
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId1);
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId2);
			albumDAO.deleteAlbum(userIds.get(mockUser1), albumId3);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId4);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId5);
			albumDAO.deleteAlbum(userIds.get(mockUser2), albumId6);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId7);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId8);
			albumDAO.deleteAlbum(userIds.get(mockUser3), albumId9);

			// Check Album AL Assertions
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
			
			// Check Cover Photo Assertions
			checkCorrectCoverID(coverPhoto1, albumId1, photoId1U1.getId());
			checkCorrectCoverID(coverPhoto2, albumId2, photoId1U2.getId());
			checkCorrectCoverID(coverPhoto3, albumId3, photoId1U4.getId());
			checkCorrectCoverID(coverPhoto4, albumId4, photoId2U1.getId());
			checkCorrectCoverID(coverPhoto5, albumId5, photoId2U2.getId());
			checkCorrectCoverID(coverPhoto6, albumId6, photoId2U3.getId());
			checkCorrectCoverID(coverPhoto7, albumId7, photoId3U1.getId());
			checkCorrectCoverID(coverPhoto8, albumId8, photoId3U2.getId());
			checkCorrectCoverID(coverPhoto9, albumId9, photoId3U3.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/***** UTILS *****/

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
	
	private void checkCorrectCoverID(PhotoDTO cover, long albumId, long coverid) {
		Assert.assertNotNull(cover);
		Assert.assertEquals(cover.getAlbumId(), albumId);
		Assert.assertEquals(cover.getId(), coverid);
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
