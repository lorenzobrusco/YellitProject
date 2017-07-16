package unical.master.computerscience.yellit.connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Used to get all users
 */
public interface UsersService {

    @GET("User")
    Call<User[]> getAllUsers(@Query("type") String type);
}
