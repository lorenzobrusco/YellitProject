package unical.master.computerscience.yellit.connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.Like;

/**
 * Created by Francesco on 08/05/2017.
 */

public interface LikeService {

    @GET("AddLike")
    Call<Like> addLike(@Query("email") String email, @Query("idPost") Integer idPost);


    @GET("RemoveLike")
    Call<Like> removeLike(@Query("email") String email, @Query("idPost") Integer idPost);
}
