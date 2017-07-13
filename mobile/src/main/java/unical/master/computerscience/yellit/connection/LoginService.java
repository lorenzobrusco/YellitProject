package unical.master.computerscience.yellit.connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Used to login , it called a servelt on the server site
 */
public interface LoginService {

    @GET("Login")
    Call<User> getProfile(@Query("email") String email, @Query("password") String password);
}
