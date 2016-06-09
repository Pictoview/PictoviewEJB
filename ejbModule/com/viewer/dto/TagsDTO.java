package com.viewer.dto;

public class TagsDTO {
	private final long id;
	private final String name;
	private long relevance;

	public TagsDTO(long id, String name, long relevance) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getRelevance() {
		return relevance;
	}

	public void setRelevance(long relevance) {
		this.relevance = relevance;
	}
}
