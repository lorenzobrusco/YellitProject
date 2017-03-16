package unical.master.computerscience.yellit.logic.Objects;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class Post {

    private String type, text;
    private User user;

    public Post(String type, String text, User user) {
        this.type = type;
        this.text = text;
        this.user = user;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
