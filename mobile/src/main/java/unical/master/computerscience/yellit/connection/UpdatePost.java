package unical.master.computerscience.yellit.connection;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import unical.master.computerscience.yellit.logic.objects.Post;

/**
 * Created by Lorenzo on 21/03/2017.
 */

public interface UpdatePost {

    @POST("NewPost")
    Call<Post> getProfile(@Body JSONObject post);
}
