package unical.master.computerscience.yellit.connection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import unical.master.computerscience.yellit.logic.objects.Like;

/**
 * Used to add or remove like from a post,
 * called a servelt on the server site
 */
public interface LikeService {

    @GET("Like")
    Call<Like> addLike(@Query("email") String email, @Query("idPost") String idPost,
                       @Query("emailPost") String emailPost, @Query("typeLike") String removeLikePost);

    @GET("Like")
    Call<Like> isLike(@Query("email") String email, @Query("idPost") String idPost,@Query("typeLike") String removeLikePost);

}
