package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Francesco on 08/05/2017.
 */

public class Like {

    @SerializedName("likes")
    private int count;

    @SerializedName("isLike")
    private int isLike;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }
}
