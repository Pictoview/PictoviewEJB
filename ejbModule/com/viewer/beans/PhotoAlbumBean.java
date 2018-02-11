package com.viewer.beans;

import javax.ejb.Stateless;

import com.viewer.dao.impl.SQLAlbumDAO.MediaType;

@Stateless
public class PhotoAlbumBean extends AlbumBean implements AlbumBeanLocal {

	public PhotoAlbumBean() {
		super(MediaType.PHOTO);
	}

}
