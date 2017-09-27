package com.viewer.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchQueryDTO {

	private List<String> names;
	private HashMap<String, List<String>> tags;
	private int limit;
	private int offset;

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

	public void insertTag(String category, List<String> value) {
		List<String> tagList = tags.get(category);
		if (tagList == null) {
			tagList = new ArrayList<String>();
			tags.put(category, tagList);
		}
		tagList.addAll(value);
	}

	public List<String> getTagList(String category) {
		return tags.get(category);
	}

	public List<CategoryDTO> getAllTags() {
		List<CategoryDTO> allCategories = new ArrayList<CategoryDTO>();
		for (String c : tags.keySet()) {
			CategoryDTO category = new CategoryDTO(c, tags.get(c));
			allCategories.add(category);
		}
		return allCategories;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
