package unical.master.computerscience.yellit.connection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Lorenzo on 21/03/2017.
 */

public interface PostGestureService {

//    @POST("NewPost")
//    Call<Post> getProfile(@Body JSONObject post);

    @GET("Posts")
    Call<String> sendNewPost(@Query("mode") String mode);

    @POST("Posts")
    Call<Post[]> getAllPosts(@Query("mode") String mode);

    @Multipart
    @POST("Posts")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("email") RequestBody userMail, @Part("comment") RequestBody comment,  @Part("place") RequestBody place,  @Part("category") RequestBody category);

}
