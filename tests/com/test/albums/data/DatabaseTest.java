package com.test.albums.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import com.viewer.dto.UserDataDTO;

public class DatabaseTest {

	private AccountDAO accountDAO;
	private AlbumDAO albumDAO;

	private String mockUser1 = "StandardUser1";
	private String mockUser2 = "StandardUser2";
	private String mockUser3 = "StandardUser3";

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
			long albumId1 = albumDAO.createAlbum(mockUser1, "Album1", "User1 Album1", "PUBLIC");
			long albumId2 = albumDAO.createAlbum(mockUser1, "Album1-1", "User1 Album1-1", albumId1);

			long albumId3 = albumDAO.createAlbum(mockUser2, "Album2", "User2 Album1", "PUBLIC");
			long albumId4 = albumDAO.createAlbum(mockUser2, "Album2-2", "User2 Album1 (PRIVATE)", "PRIVATE");

			long albumId5 = albumDAO.createAlbum(mockUser3, "Album3", "User3 Album1", "PUBLIC");
			long albumId6 = albumDAO.createAlbum(mockUser3, "Album3-1", "User3 Album (PRIVATE)", albumId5);
			long albumId7 = albumDAO.createAlbum(mockUser3, "Album3-1-1", "User3 Album (PRIVATE)", albumId6);

			// Fetch public albums
			List<AlbumDTO> publicAlbums = albumDAO.fetchAllPublicAlbums(25, 0);

			// Fetch viewable albums
			List<AlbumDTO> limitedAlbums = albumDAO.fetchViewableAlbums(mockUser1, 0);
			List<AlbumDTO> limitedAlbums1 = albumDAO.fetchViewableAlbums(mockUser1, albumId1);

			// Delete All Albums
			albumDAO.deleteAlbum(mockUser1, albumId1);
			albumDAO.deleteAlbum(mockUser1, albumId2);
			albumDAO.deleteAlbum(mockUser2, albumId3);
			albumDAO.deleteAlbum(mockUser2, albumId4);
			albumDAO.deleteAlbum(mockUser3, albumId5);
			albumDAO.deleteAlbum(mockUser3, albumId6);
			albumDAO.deleteAlbum(mockUser3, albumId7);

			// Check Assertions
			checkCorrectAlbumsIDs(publicAlbums, albumId1, albumId3, albumId5);
			checkCorrectAlbumsIDs(limitedAlbums, albumId1, albumId3, albumId5);
			checkCorrectAlbumsIDs(limitedAlbums1, albumId2);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fetchSubscribedAlbums() {

		// Create albums
		try {
			long albumId1 = albumDAO.createAlbum(mockUser1, "Album1", "User1 Album1", "PUBLIC");
			long albumId2 = albumDAO.createAlbum(mockUser1, "Album1-1", "User1 Album1-1", 1);
			long albumId3 = albumDAO.createAlbum(mockUser2, "Album2", "User2 Album1", "PUBLIC");
			long albumId4 = albumDAO.createAlbum(mockUser2, "Album2-2", "User2 Album1", "PUBLIC");
			long albumId5 = albumDAO.createAlbum(mockUser3, "Album3", "User3 Album1", "PRIVATE");
			long albumId6 = albumDAO.createAlbum(mockUser3, "Album3-1", "User3 Album (PRIVATE)", albumId5);
			long albumId7 = albumDAO.createAlbum(mockUser3, "Album3-1-1", "User3 Album (PRIVATE)", albumId6);

			// Subscribe to Albums
			albumDAO.subscribeToAlbum(mockUser1, albumId3);
			albumDAO.subscribeToAlbum(mockUser1, albumId4);

			// Fetch subscribed albums
			List<AlbumDTO> limitedAlbums = albumDAO.fetchAllSubscribedAlbums(mockUser1, 0);

			// Delete Subscriptions
			albumDAO.unsubscribeToAlbum(mockUser1, albumId3);
			albumDAO.unsubscribeToAlbum(mockUser1, albumId4);

			// Delete All Albums
			albumDAO.deleteAlbum(mockUser1, albumId1);
			albumDAO.deleteAlbum(mockUser1, albumId2);
			albumDAO.deleteAlbum(mockUser2, albumId3);
			albumDAO.deleteAlbum(mockUser2, albumId4);
			albumDAO.deleteAlbum(mockUser3, albumId5);
			albumDAO.deleteAlbum(mockUser3, albumId6);
			albumDAO.deleteAlbum(mockUser3, albumId7);

			// Test Assertions
			checkCorrectAlbumsIDs(limitedAlbums, albumId1, albumId2, albumId3, albumId4);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
