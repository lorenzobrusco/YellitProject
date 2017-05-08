package unical.master.computerscience.yellit.connection;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Altair07 on 06/05/2017.
 */

public class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }
}
