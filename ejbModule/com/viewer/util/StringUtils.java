package com.viewer.util;

public class StringUtils {
	public static boolean notNullEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	public static boolean notNullEmpty(String... values) {
		for (String s : values) {
			if (s == null || s.isEmpty()) return false;
		}
		return true;
	}

	public static String emptyIfNull(String value) {
		if (value == null) return "";
		return value;
	}

	public static boolean equals(String pattern, String expected) {
		return notNullEmpty(pattern) && pattern.equals(expected);
	}

	public static boolean equalsAll(String pattern, String... expected) {
		for (String p : expected)
			if (!StringUtils.equals(pattern, p)) return false;
		return true;
	}

	public static boolean equalsAny(String pattern, String... expected) {
		for (String p : expected)
			if (StringUtils.equals(pattern, p)) return true;
		return false;
	}
}
