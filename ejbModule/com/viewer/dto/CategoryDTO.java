package com.viewer.dto;

import java.util.List;

public class CategoryDTO {
	private final String category;
	private List<String> tags;

	public CategoryDTO(String category, List<String> tags) {
		this.category = category;
		this.tags = tags;
	}
	
	public CategoryDTO(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String toString() {
		String value = category + " : ";
		for (String s : tags)
			value += s + ",";
		return value;
	}
}
