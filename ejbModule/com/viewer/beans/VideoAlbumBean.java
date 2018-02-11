package com.viewer.beans;

import com.viewer.dao.impl.SQLAlbumDAO.MediaType;

public class VideoAlbumBean extends AlbumBean implements AlbumBeanLocal {

	public VideoAlbumBean() {
		super(MediaType.VIDEO);
	}

}
