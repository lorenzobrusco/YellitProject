package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lorenzo on 22/02/2017.
 */

public class User {

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("email")
    private String email;

    @SerializedName("pathImg")
    private String pathImg;

    @SerializedName("sesso")
    private String sesso;

    @SerializedName("altezza")
    private String altezza;

    @SerializedName("peso")
    private String peso;

    @SerializedName("città")
    private String città;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("relazione")
    private String relazione;

    @SerializedName("friends")
    private Friend[] friends;

    public User(String nickname, String fullname, String email, String pathImg, String sesso, String altezza, String peso, String città, String birthday, String relazione) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.pathImg = pathImg;
        this.altezza = altezza;
        this.peso = peso;
        this.città = città;
        this.birthday = birthday;
        this.relazione = relazione;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
