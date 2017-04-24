package unical.master.computerscience.yellit.connection;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;



public interface SigninService {

    @GET("Signin")
    Call<User> createProfile(@Query("nickname") String nickname, @Query("email") String email, @Query("password") String password);
}
