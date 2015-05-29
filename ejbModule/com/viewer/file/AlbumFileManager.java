package com.viewer.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class AlbumFileManager {
	public static final String StorageLocation = "E:/PictoViewDB/Albums/";
	public static final String ThumbnailStorageLocation = "E:/PictoViewDB/Thumbnail/";

	public static boolean createAlbumDirectory(String path) {
		return false;
	}

	public static void createPhotoFile(String source, InputStream data)
			throws IOException {
		File sourceFile = new File(StorageLocation + source);
		if (!sourceFile.getParentFile().exists())
			sourceFile.getParentFile().mkdirs();

		FileOutputStream fo = new FileOutputStream(sourceFile);

		byte[] buffer = new byte[4096];
		while (data.read(buffer) != -1) {
			fo.write(buffer);
		}
		fo.close();
		data.close();
	}

	private static BufferedImage scaleImage(File sourceFile) throws IOException {
		final int width = 120, height = 120;
		// Scale Image
		BufferedImage image = ImageIO.read(sourceFile);
		Image scaledImage = image.getScaledInstance(width, height,
				Image.SCALE_DEFAULT);
		image.getGraphics().drawImage(scaledImage, 0, 0, null);

		BufferedImage retImage = new BufferedImage(width, height,
				image.getType());
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	public static void createPhotoThumbnail(String source)
			throws IOException {
		File sourceFile = new File(StorageLocation + source);
		File thumbnailFile = new File(ThumbnailStorageLocation + source);
		if (!thumbnailFile.getParentFile().exists())
			thumbnailFile.getParentFile().mkdirs();

		BufferedImage scaledImageBuffer = scaleImage(sourceFile);
		ImageIO.write(scaledImageBuffer, "jpg", thumbnailFile);
	}
}
