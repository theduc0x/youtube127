package com.example.youtubeapp.model.itemrecycleview;

import com.example.youtubeapp.model.listcomment.RepliesComment;

import java.io.Serializable;

public class CommentItem implements Serializable {
    private String idComment;
    private String textDisplay;
    private String authorName;
    private String authorLogoUrl;
    private String authorChannelId;
    private String likeCount;
    private String publishedAt;
    private String updateAt;
    private String totalReplyCount;
    private RepliesComment repliesComent;

    public CommentItem(String idComment, String textDisplay, String authorName,
                       String authorLogoUrl, String authorChannelId,
                       String likeCount, String publishedAt,
                       String updateAt, String totalReplyCount,
                       RepliesComment repliesComent) {
        this.idComment = idComment;
        this.textDisplay = textDisplay;
        this.authorName = authorName;
        this.authorLogoUrl = authorLogoUrl;
        this.authorChannelId = authorChannelId;
        this.likeCount = likeCount;
        this.publishedAt = publishedAt;
        this.updateAt = updateAt;
        this.totalReplyCount = totalReplyCount;
        this.repliesComent = repliesComent;
    }
    public CommentItem() {

    }
    public CommentItem(String textDisplay) {
        this.textDisplay = textDisplay;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(String totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getTextDisplay() {
        return textDisplay;
    }

    public void setTextDisplay(String textDisplay) {
        this.textDisplay = textDisplay;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLogoUrl() {
        return authorLogoUrl;
    }

    public void setAuthorLogoUrl(String authorLogoUrl) {
        this.authorLogoUrl = authorLogoUrl;
    }

    public String getAuthorChannelId() {
        return authorChannelId;
    }

    public void setAuthorChannelId(String authorChannelId) {
        this.authorChannelId = authorChannelId;
    }



    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }


    public RepliesComment getRepliesComent() {
        return repliesComent;
    }

    public void setRepliesComent(RepliesComment repliesComent) {
        this.repliesComent = repliesComent;
    }
}
