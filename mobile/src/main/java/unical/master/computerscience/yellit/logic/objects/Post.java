package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class Post {

    @SerializedName("id_post")
    private Integer idPost;

    @SerializedName("user_nick")
    private String userName;

    @SerializedName("user_image")
    private String userImagePath;

    @SerializedName("date")
    private String date;

    @SerializedName("type")
    private String type;

    @SerializedName("location")
    private String location;

    @SerializedName("comment")
    private String comment;

    @SerializedName("post_image")
    private String postImagePost;

    @SerializedName("post_video")
    private String postVideoPost;

    @SerializedName("lat")
    private double lat;

    @SerializedName("longi")
    private double longi;

    @SerializedName("likes")
    //private Like likes;
    private Integer likes;

    public Post(Integer idPost, String userName, String userImagePath, String date, String type, String location, String comment, String postImagePost,
                String postVideoPost, Integer likes) {
        this.idPost = idPost;
        this.userName = userName;
        this.userImagePath = userImagePath;
        this.date = date;
        this.type = type;
        this.location = location;
        this.comment = comment;
        this.postImagePost = postImagePost;
        this.postVideoPost = postVideoPost;
        this.likes = likes;
    }

    public Post(String userName) {
        this.userName = userName;
    }


    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostImagePost() {
        return postImagePost;
    }

    public void setPostImagePost(String postImagePost) {
        this.postImagePost = postImagePost;
    }

    public String getPostVideoPost() {
        return postVideoPost;
    }

    public void setPostVideoPost(String postVideoPost) {
        this.postVideoPost = postVideoPost;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
