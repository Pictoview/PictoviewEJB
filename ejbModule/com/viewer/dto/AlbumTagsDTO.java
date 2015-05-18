package com.viewer.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumTagsDTO {
	private final long albumid;
	private HashMap<String, List<TagsDTO>> tags;

	public AlbumTagsDTO(long albumid) {
		this.albumid = albumid;
		this.tags = new HashMap<String, List<TagsDTO>>();
	}

	public long getAlbumid() {
		return albumid;
	}

	public void insertTag(long id, String tagName, String category) {
		if (!tags.containsKey(category)) {
			List<TagsDTO> tagList = new ArrayList<TagsDTO>();
			tagList.add(new TagsDTO(id,tagName));
			tags.put(category, tagList);
		} else {
			tags.get(category).add(new TagsDTO(id, tagName));
		}
	}

	public void clearTagList() {
		tags.clear();
	}

	public HashMap<String, List<TagsDTO>> getTags() {
		return tags;
	}
}
