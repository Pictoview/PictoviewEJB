package com.viewer.file;

import java.util.Properties;

public class ConfigProperties {
	
	private static Properties prop = new Properties();
	
	/* static {
		InputStream is = null;
		try {
			ConfigProperties.class.getClassLoader();
			is =  ClassLoader.getSystemResourceAsStream("config/config.properties");
			if (is != null) {
				prop.load(is);
			} else System.out.println("Can't find Config file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} */

	public static String getProperty(String key) {
		//String property = prop.getProperty(key);
		if (key == null) return null;
		if (key.equals("jdbcLoc")) {
			return "jdbc:sqlite:E:/PictoViewDB/database/pdb.db";
		} else if (key.equals("albumDirectory")) {
			return "E:/PictoViewDB/User/Albums/";
		} else if (key.equals("thumbnailDirectory")) {
			return "E:/PictoViewDB/User/Thumbnail/";
		}
		return null;
	}
}