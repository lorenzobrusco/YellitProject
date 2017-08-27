package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName("friend")
    private String email;

    @SerializedName("path")
    private String path;

    public Friend(String email, String path) {
        this.email = email;
        this.path = path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}


