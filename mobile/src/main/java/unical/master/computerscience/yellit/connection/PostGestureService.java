package unical.master.computerscience.yellit.connection;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Lorenzo on 21/03/2017.
 */

public interface PostGestureService {

//    @POST("NewPost")
//    Call<Post> getProfile(@Body JSONObject post);

    @GET("NewPost")
    Call<String> sendNewPost(@Query("text") String text);
}