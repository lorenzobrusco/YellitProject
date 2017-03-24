package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lorenzo on 20/02/2017.
 */

public class Friend {

    @SerializedName("friend")
    private String email;

    public Friend(String email) {
        this.email = email;
    }

    public Friend(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


