package unical.master.computerscience.yellit.logic.objects;

import com.google.gson.annotations.SerializedName;

public class Like {

    @SerializedName("likes")
    private int count;

    @SerializedName("isLike")
    private int isLike;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
