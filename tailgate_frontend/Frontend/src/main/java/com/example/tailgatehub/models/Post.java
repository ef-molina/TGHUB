package com.example.tailgatehub.models;

/**
 * This class is responsible for
 * storing all the post information
 * */
public class Post {
    private String postId;
    private String userId;
    private String content;
    private String imageUrl;
//    private long timeStamp;

    // constructor
    public Post(String postId, String userId,String content, String imageUrl){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public long getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(long timeStamp) {
//        this.timeStamp = timeStamp;
//    }
}
