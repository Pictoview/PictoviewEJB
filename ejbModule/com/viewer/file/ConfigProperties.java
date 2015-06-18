package com.viewer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
	
	private static Properties prop = new Properties();
	
	static {
		InputStream is = null;
		try {
			is = new FileInputStream(new File("config.properties"));
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		String property = prop.getProperty(key);
		return property;
	}
}