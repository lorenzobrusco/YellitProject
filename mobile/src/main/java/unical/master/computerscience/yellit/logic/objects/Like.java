package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Francesco on 08/05/2017.
 */

public class Like {

    @SerializedName("likes")
    private Integer count;

    public Like(Integer count, Integer idPost) {
        this.count = count;

    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
