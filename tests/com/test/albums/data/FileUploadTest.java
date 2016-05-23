package com.test.albums.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.tools.jxc.gen.config.Config;
import com.viewer.beans.AlbumBean;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dao.AccountDAO;
import com.viewer.dao.AlbumDAO;
import com.viewer.dao.SQLConnector;
import com.viewer.dao.impl.SQLAccountDAO;
import com.viewer.dao.impl.SQLAlbumDAO;
import com.viewer.dto.PhotoDTO;
import com.viewer.dto.UserDataDTO;
import com.viewer.file.AlbumFileManager;
import com.viewer.file.ConfigProperties;

public class FileUploadTest {

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

		// Delete Mockuser data files
		File thumbnailDirectory = new File(ConfigProperties.getProperty("thumbnailDirectory") + mockUser1);
		File fullImageDirectory = new File(ConfigProperties.getProperty("albumDirectory") + mockUser1);
		/*
		for (File albumFile : thumbnailDirectory.listFiles())
			for (File photoFile : albumFile.listFiles())
				photoFile.delete();
		thumbnailDirectory.delete();
		for (File albumFile : fullImageDirectory.listFiles())
			for (File photoFile : albumFile.listFiles())
				photoFile.delete();
		fullImageDirectory.delete(); */
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

	@Test
	public void test1() {
		try {
			InputStream data = new FileInputStream(
					new File(ConfigProperties.getProperty("testStorageDirectory") + "image1.jpg"));

			AlbumBeanLocal albumBean = new AlbumBean();
			long albumid = albumBean.createAlbum(mockUser1, "TestAlbum1", "", "Description", 0);
			PhotoDTO photo1 = albumBean.uploadPhoto(mockUser1, albumid, "image1.jpg", "jpg", data, 0);

			albumDAO.deleteAlbum(mockUser1, albumid);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
