package unical.master.computerscience.yellit.connection;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.Post;

/**
 * Used to get or uploda a new post,
 * called a servelt on the server site
 */
public interface PostGestureService {

    @POST("Posts")
    Call<Post[]> getAllPosts(@Query("mode") String mode);

    @Multipart
    @POST("Posts")
    Call<ServerResponse> uploadPost(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("email") RequestBody userMail, @Part("comment") RequestBody comment,
                                    @Part("place") RequestBody place, @Part("category") RequestBody category, @Part("lat") RequestBody lat, @Part("longi") RequestBody longi, @Query("mode") String mode);

}
