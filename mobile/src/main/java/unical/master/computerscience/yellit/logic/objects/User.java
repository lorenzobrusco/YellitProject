package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lorenzo on 22/02/2017.
 */

public class User {

    @SerializedName("surname")
    private String surname;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("pathImg")
    private String pathImg;

    @SerializedName("friends")
    private Friend[] friends;

    public User(String name, String surname, String email, String pathImg) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pathImg = pathImg;
    }

    public User(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getPathImg() {
        return pathImg;
    }

    public Friend[] getFriends() {
        return friends;
    }

    public void setFriends(Friend[] friends) {
        this.friends = friends;
    }
}
