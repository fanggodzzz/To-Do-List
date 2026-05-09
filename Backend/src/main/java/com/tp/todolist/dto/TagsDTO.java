package com.tp.todolist.dto;

public class TagsDTO {
    private String tagName;
    private Long tagId;

    public TagsDTO() {
    }

    public TagsDTO(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
