package com.viewer.util;

public class MediaFileUtils {
	
	public enum Media {
		UNSUPPORTED, PHOTO, VIDEO, IMAGE_VIDEO
	}

	public static Media convertType(String ext) {
		if (StringUtils.equalsAny(ext, "jpg", "jpeg", "png", "bmp")) {
			return Media.PHOTO;
		} else if (StringUtils.equalsAny(ext, "avi", "flv", "wmv", "mov", "mp4")) {
			return Media.VIDEO;
		} else if (StringUtils.equalsAny(ext, "gif")) {
			return Media.IMAGE_VIDEO;
		}
		return Media.UNSUPPORTED;
	}
}
