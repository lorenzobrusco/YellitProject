package unical.master.computerscience.yellit.connection;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Lorenzo on 22/02/2017.
 */

public interface LoginService {

    @GET("Login")
    Call<User> getProfile(@Query("email") String email, @Query("password") String password);
}
