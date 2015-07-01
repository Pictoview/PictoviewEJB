package com.viewer.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.common.io.Files;

public class AlbumFileManager {
	public static final String StorageLocation = ConfigProperties.getProperty("albumDirectory");
	public static final String ThumbnailStorageLocation = ConfigProperties.getProperty("thumbnailDirectory");

	public static final int DEFAULT_PHOTO = 0;
	public static final int MEDIUM_PHOTO = 1;

	public static boolean createAlbumDirectory(String path) {
		return false;
	}

	public static void createPhotoFile(String source, InputStream data,
			int flags) throws IOException {
		File sourceFile = new File(StorageLocation + source);
		if (!sourceFile.getParentFile().exists())
			sourceFile.getParentFile().mkdirs();

		switch (flags) {
		case DEFAULT_PHOTO:
			createDefaultPhoto(sourceFile, data);
			break;
		case MEDIUM_PHOTO:
			createMediumPhoto(sourceFile, data);
			break;
		}
	}

	private static void createMediumPhoto(File sourceFile, InputStream data)
			throws IOException {
		String ext = Files.getFileExtension(sourceFile.getName());
		BufferedImage scaledImageBuffer = scaleImageRatio(data, 800, 600);
		ImageIO.write(scaledImageBuffer, ext, sourceFile);
	}

	private static void createDefaultPhoto(File sourceFile, InputStream data)
			throws IOException {
		FileOutputStream fo = new FileOutputStream(sourceFile);

		byte[] buffer = new byte[4096];
		while (data.read(buffer) != -1) {
			fo.write(buffer);
		}
		fo.close();
		data.close();
	}

	private static BufferedImage scaleImage(File sourceFile, int width,
			int height) throws IOException {
		// Scale Image
		BufferedImage image = ImageIO.read(sourceFile);
		Image scaledImage = image.getScaledInstance(width, height,
				Image.SCALE_DEFAULT);
		int imageType = image.getType();
		if (imageType == 0) imageType = BufferedImage.TYPE_INT_ARGB;
		BufferedImage retImage = new BufferedImage(width, height, imageType);
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	private static BufferedImage scaleImageRatio(InputStream data, int width,
			int height) throws IOException {
		BufferedImage image = ImageIO
				.read(ImageIO.createImageInputStream(data));

		// Find scale dimensions
		double scaleH = height, scaleW = width;
		if (image.getWidth() > 800 || image.getHeight() > 600) {
			double ratio = 1;
			if (image.getHeight() > image.getWidth()) {
				ratio = ((double) height) / ((double) image.getHeight());
				scaleH = height;
				scaleW = image.getWidth() * ratio;
			} else {
				ratio = ((double) width) / ((double) image.getWidth());
				scaleW = width;
				scaleH = (double) image.getHeight() * ratio;
			}
		}

		Image scaledImage = image.getScaledInstance((int) scaleW, (int) scaleH,
				Image.SCALE_SMOOTH);
		int imageType = image.getType();
		if (imageType == 0) imageType = BufferedImage.TYPE_INT_ARGB;
		BufferedImage retImage = new BufferedImage((int) scaleW, (int) scaleH,
				imageType);
		retImage.getGraphics().drawImage(scaledImage, 0, 0, null);
		return retImage;
	}

	public static void createPhotoThumbnail(String source) throws IOException {
		File sourceFile = new File(StorageLocation + source);
		File thumbnailFile = new File(ThumbnailStorageLocation + source);
		if (!thumbnailFile.getParentFile().exists())
			thumbnailFile.getParentFile().mkdirs();

		String ext = Files.getFileExtension(source);
		BufferedImage scaledImageBuffer = scaleImage(sourceFile, 256, 256);
		ImageIO.write(scaledImageBuffer, ext, thumbnailFile);
	}
}
