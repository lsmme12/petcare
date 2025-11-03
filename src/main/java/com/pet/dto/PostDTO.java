package com.pet.dto;

import java.util.Date;

/**
 * 📦 PostDTO
 * - 게시글 데이터 전송 객체
 * - DB: POST, POST_LIKE, POST_VIEW, POST_COMMENT와 매핑
 * - 파일 업로드 필드 포함 (추후 확장 대비)
 */
public class PostDTO {

    // 📌 기본 게시글 정보
    private int postId;
    private int ownerId;
    private String ownerNickname;
    private String title;
    private String content;
    private String category;
    private Date createdAt;
    private Date updatedAt;
    private String isDeleted;

    // 📊 통계 필드
    private int viewCount;
    private int likeCount;
    private int commentCount;

    // 📁 파일 업로드 관련 필드
    private String originalFilename;
    private String storedFilename;
    private String contentType;
    private long fileSizeBytes;
    private String hasAttachment;

    // ===========================
    // 🧩 Getter / Setter
    // ===========================

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }
    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }
    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }
    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getHasAttachment() {
        return hasAttachment;
    }
    public void setHasAttachment(String hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
}
