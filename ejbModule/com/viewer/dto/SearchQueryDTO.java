package com.viewer.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchQueryDTO {

	private List<String> names;
	private HashMap<String, List<String>> tags;

	public SearchQueryDTO() {
		this.names = new ArrayList<String>();
		this.tags = new HashMap<String, List<String>>();
	}

	public void insertName(String name) {
		names.add(name);
	}

	public void insertTag(String category, String value) {
		List<String> tagList = tags.get(category);
		if (tagList == null) {
			tagList = new ArrayList<String>();
			tags.put(category, tagList);
		}
		tagList.add(value);
	}

	public List<String> getTagList(String category) {
		return tags.get(category);
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
}
