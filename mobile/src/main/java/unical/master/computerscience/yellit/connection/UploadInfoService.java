package unical.master.computerscience.yellit.connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Francesco on 09/05/2017.
 */

public interface UploadInfoService {


    /* Pue u vidimu cit. Lollo. scegliere come e cosa passare (perchè non si potrebbe aggiornare tutto)
    * per ora nel lato server ci passiamo tutto*/
    @GET("UploadInfo")
    Call<User> uploadInfo(@Query("email") String email);
}
