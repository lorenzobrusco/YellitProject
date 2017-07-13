package unical.master.computerscience.yellit.connection;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Used to sign in, create a new user in the server
 * called a servelt on the server site
 */
public interface SigninService {

    @Multipart
    @POST("Signin")
    Call<User> createProfile(@Query("nickname") String nickname, @Query("email") String email,
                             @Query("password") String password, @Part MultipartBody.Part file, @Part("file") RequestBody name);
}
